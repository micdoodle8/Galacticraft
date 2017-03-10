package codechicken.lib.asm;

import codechicken.lib.config.ConfigTag;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ObfMapping {

    public static class ObfRemapper extends Remapper {

        private HashMap<String, String> fields = new HashMap<String, String>();
        private HashMap<String, String> funcs = new HashMap<String, String>();

        public ObfRemapper() {
            try {
                Field rawFieldMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawFieldMaps");
                Field rawMethodMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawMethodMaps");
                rawFieldMapsField.setAccessible(true);
                rawMethodMapsField.setAccessible(true);
                Map<String, Map<String, String>> rawFieldMaps = (Map<String, Map<String, String>>) rawFieldMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);
                Map<String, Map<String, String>> rawMethodMaps = (Map<String, Map<String, String>>) rawMethodMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);

                if (rawFieldMaps == null) {
                    throw new IllegalStateException("codechicken.lib.asm.ObfMapping loaded too early. Make sure all references are in or after the asm transformer load stage");
                }

                for (Map<String, String> map : rawFieldMaps.values()) {
                    for (Entry<String, String> entry : map.entrySet()) {
                        if (entry.getValue().startsWith("field")) {
                            fields.put(entry.getValue(), entry.getKey().substring(0, entry.getKey().indexOf(':')));
                        }
                    }
                }
                for (Map<String, String> map : rawMethodMaps.values()) {
                    for (Entry<String, String> entry : map.entrySet()) {
                        if (entry.getValue().startsWith("func")) {
                            funcs.put(entry.getValue(), entry.getKey().substring(0, entry.getKey().indexOf('(')));
                        }
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String mapMethodName(String owner, String name, String desc) {
            String s = funcs.get(name);
            return s == null ? name : s;
        }

        @Override
        public String mapFieldName(String owner, String name, String desc) {
            String s = fields.get(name);
            return s == null ? name : s;
        }

        @Override
        public String map(String typeName) {
            return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
        }

        public String unmap(String typeName) {
            return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
        }

        public boolean isObf(String typeName) {
            return !map(typeName).equals(typeName) || !unmap(typeName).equals(typeName);
        }
    }

    public static class MCPRemapper extends Remapper implements LineProcessor<Void> {

        public static File[] getConfFiles() {

            // check for GradleStart system vars
            if (!Strings.isNullOrEmpty(System.getProperty("net.minecraftforge.gradle.GradleStart.srg.notch-srg"))) {
                File notchSrg = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.srg.notch-srg"));
                File csvDir = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.csvDir"));

                if (notchSrg.exists() && csvDir.exists()) {
                    File fieldCsv = new File(csvDir, "fields.csv");
                    File methodCsv = new File(csvDir, "methods.csv");

                    if (notchSrg.exists() && fieldCsv.exists() && methodCsv.exists()) {
                        return new File[] { notchSrg, fieldCsv, methodCsv };
                    }
                }
            }

            ConfigTag tag = ASMHelper.config.getTag("mappingDir").setComment("Path to directory holding packaged.srg, fields.csv and methods.csv for mcp remapping");
            for (int i = 0; i < DIR_GUESSES + DIR_ASKS; i++) {
                File dir = confDirectoryGuess(i, tag);
                if (dir == null || dir.isFile()) {
                    continue;
                }

                File[] mappings;
                try {
                    mappings = parseConfDir(dir);
                } catch (Exception e) {
                    if (i >= DIR_GUESSES) {
                        e.printStackTrace();
                    }
                    continue;
                }

                tag.setValue(dir.getPath());
                return mappings;
            }

            throw new RuntimeException("Failed to select mappings directory, set it manually in the config");
        }

        private static final int DIR_GUESSES = 4;
        private static final int DIR_ASKS = 3;

        public static File confDirectoryGuess(int i, ConfigTag tag) {
            File mcDir = (File) FMLInjectionData.data()[6];
            switch (i) {
                case 0:
                    return tag.value != null ? new File(tag.getValue()) : null;
                case 1:
                    return new File(mcDir, "../conf");
                case 2:
                    return new File(mcDir, "../build/unpacked/conf");
                case 3:
                    return new File(System.getProperty("user.home"), ".gradle/caches/minecraft/net/minecraftforge/forge/" + FMLInjectionData.data()[4] + "-" + ForgeVersion.getVersion() + "/unpacked/conf");
                default:
                    JFileChooser fc = new JFileChooser(mcDir);
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fc.setDialogTitle("Select an mcp conf dir for the deobfuscator.");
                    int ret = fc.showDialog(null, "Select");
                    return ret == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
            }
        }

        public static File[] parseConfDir(File confDir) {
            File srgDir = new File(confDir, "conf");
            if (!srgDir.exists()) {
                srgDir = confDir;
            }

            File srgs = new File(srgDir, "packaged.srg");
            if (!srgs.exists()) {
                srgs = new File(srgDir, "joined.srg");
            }
            if (!srgs.exists()) {
                throw new RuntimeException("Could not find packaged.srg or joined.srg");
            }

            File mapDir = new File(confDir, "mappings");
            if (!mapDir.exists()) {
                mapDir = confDir;
            }

            File methods = new File(mapDir, "methods.csv");
            if (!methods.exists()) {
                throw new RuntimeException("Could not find methods.csv");
            }
            File fields = new File(mapDir, "fields.csv");
            if (!fields.exists()) {
                throw new RuntimeException("Could not find fields.csv");
            }

            return new File[] { srgs, methods, fields };
        }

        private HashMap<String, String> fields = new HashMap<String, String>();
        private HashMap<String, String> funcs = new HashMap<String, String>();

        public MCPRemapper() {
            File[] mappings = getConfFiles();
            try {
                Resources.readLines(mappings[1].toURI().toURL(), Charsets.UTF_8, this);
                Resources.readLines(mappings[2].toURI().toURL(), Charsets.UTF_8, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String mapMethodName(String owner, String name, String desc) {
            String s = funcs.get(name);
            return s == null ? name : s;
        }

        @Override
        public String mapFieldName(String owner, String name, String desc) {
            String s = fields.get(name);
            return s == null ? name : s;
        }

        @Override
        public boolean processLine(String line) throws IOException {
            int i = line.indexOf(',');
            String srg = line.substring(0, i);
            int i2 = i + 1;
            i = line.indexOf(',', i2);
            String mcp = line.substring(i2, i);
            (srg.startsWith("func") ? funcs : fields).put(srg, mcp);
            return true;
        }

        @Override
        public Void getResult() {
            return null;
        }
    }

    public static ObfRemapper obfMapper = new ObfRemapper();
    public static Remapper mcpMapper = null;

    public static void loadMCPRemapper() {
        if (mcpMapper == null) {
            mcpMapper = new MCPRemapper();
        }
    }

    public static final boolean obfuscated;

    static {
        boolean obf = true;
        try {
            obf = Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
        } catch (IOException ignored) {
        }
        obfuscated = obf;
        if (!obf) {
            loadMCPRemapper();
        }
    }

    public String s_owner;
    public String s_name;
    public String s_desc;

    public ObfMapping(String owner) {
        this(owner, "", "");
    }

    public ObfMapping(String owner, String name, String desc) {
        this.s_owner = owner;
        this.s_name = name;
        this.s_desc = desc;

        if (s_owner.contains(".")) {
            throw new IllegalArgumentException(s_owner);
        }
    }

    public ObfMapping(ObfMapping descmap, String subclass) {
        this(subclass, descmap.s_name, descmap.s_desc);
    }

    public static ObfMapping fromDesc(String s) {
        int lastDot = s.lastIndexOf('.');
        if (lastDot < 0) {
            return new ObfMapping(s, "", "");
        }
        int sep = s.indexOf('(');//methods
        int sep_end = sep;
        if (sep < 0) {
            sep = s.indexOf(' ');//some stuffs
            sep_end = sep + 1;
        }
        if (sep < 0) {
            sep = s.indexOf(':');//fields
            sep_end = sep + 1;
        }
        if (sep < 0) {
            return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1), "");
        }

        return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1, sep), s.substring(sep_end));
    }

    public ObfMapping subclass(String subclass) {
        return new ObfMapping(this, subclass);
    }

    public boolean matches(MethodNode node) {
        return s_name.equals(node.name) && s_desc.equals(node.desc);
    }

    public boolean matches(MethodInsnNode node) {
        return s_owner.equals(node.owner) && s_name.equals(node.name) && s_desc.equals(node.desc);
    }

    public AbstractInsnNode toInsn(int opcode) {
        if (isClass()) {
            return new TypeInsnNode(opcode, s_owner);
        } else if (isMethod()) {
            return new MethodInsnNode(opcode, s_owner, s_name, s_desc);
        } else {
            return new FieldInsnNode(opcode, s_owner, s_name, s_desc);
        }
    }

    public void visitTypeInsn(MethodVisitor mv, int opcode) {
        mv.visitTypeInsn(opcode, s_owner);
    }

    public void visitMethodInsn(MethodVisitor mv, int opcode) {
        mv.visitMethodInsn(opcode, s_owner, s_name, s_desc);
    }

    public void visitFieldInsn(MethodVisitor mv, int opcode) {
        mv.visitFieldInsn(opcode, s_owner, s_name, s_desc);
    }

    public MethodVisitor visitMethod(ClassVisitor visitor, int access, String[] exceptions) {
        return visitor.visitMethod(access, s_name, s_desc, null, exceptions);
    }

    public FieldVisitor visitField(ClassVisitor visitor, int access, Object value) {
        return visitor.visitField(access, s_name, s_desc, null, value);
    }

    public boolean isClass(String name) {
        return name.replace('.', '/').equals(s_owner);
    }

    public boolean matches(String name, String desc) {
        return s_name.equals(name) && s_desc.equals(desc);
    }

    public boolean matches(FieldNode node) {
        return s_name.equals(node.name) && s_desc.equals(node.desc);
    }

    public boolean matches(FieldInsnNode node) {
        return s_owner.equals(node.owner) && s_name.equals(node.name) && s_desc.equals(node.desc);
    }

    public String javaClass() {
        return s_owner.replace('/', '.');
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObfMapping)) {
            return false;
        }

        ObfMapping desc = (ObfMapping) obj;
        return s_owner.equals(desc.s_owner) && s_name.equals(desc.s_name) && s_desc.equals(desc.s_desc);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(s_desc, s_name, s_owner);
    }

    @Override
    public String toString() {
        if (s_name.length() == 0) {
            return "[" + s_owner + "]";
        }
        if (s_desc.length() == 0) {
            return "[" + s_owner + "." + s_name + "]";
        }
        return "[" + (isMethod() ? methodDesc() : fieldDesc()) + "]";
    }

    public String methodDesc() {
        return s_owner + "." + s_name + s_desc;
    }

    public String fieldDesc() {
        return s_owner + "." + s_name + ":" + s_desc;
    }

    public boolean isClass() {
        return s_name.length() == 0;
    }

    public boolean isMethod() {
        return s_desc.contains("(");
    }

    public boolean isField() {
        return !isClass() && !isMethod();
    }

    public ObfMapping map(Remapper mapper) {
        if (mapper == null) {
            return this;
        }

        if (isMethod()) {
            s_name = mapper.mapMethodName(s_owner, s_name, s_desc);
        } else if (isField()) {
            s_name = mapper.mapFieldName(s_owner, s_name, s_desc);
        }

        s_owner = mapper.mapType(s_owner);

        if (isMethod()) {
            s_desc = mapper.mapMethodDesc(s_desc);
        } else if (s_desc.length() > 0) {
            s_desc = mapper.mapDesc(s_desc);
        }

        return this;
    }

    public ObfMapping toRuntime() {
        map(mcpMapper);
        return this;
    }

    public ObfMapping toClassloading() {
        if (!obfuscated) {
            map(mcpMapper);
        } else if (obfMapper.isObf(s_owner)) {
            map(obfMapper);
        }
        return this;
    }

    public ObfMapping copy() {
        return new ObfMapping(s_owner, s_name, s_desc);
    }
}
