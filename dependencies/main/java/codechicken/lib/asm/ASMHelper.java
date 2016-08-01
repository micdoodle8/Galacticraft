package codechicken.lib.asm;

import codechicken.lib.config.ConfigFile;
import codechicken.lib.config.DefaultingConfigFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ASMHelper {
    public static ConfigFile config = loadConfig();
    public static Logger logger = LogManager.getLogger("CCL ASM");

    private static ConfigFile loadConfig() {
        try {//weak reference for environments without FML
            File mcDir = (File) ((Object[]) Class.forName("net.minecraftforge.fml.relauncher.FMLInjectionData").getMethod("data").invoke(null))[6];
            File file = new File(mcDir, "config/CodeChickenLib.cfg");
            if (ObfMapping.obfuscated) {
                return new DefaultingConfigFile(file);
            } else {
                return new ConfigFile(file).setComment("CodeChickenLib development configuration file.");
            }
        } catch (Exception ignored) {
            return null;//no config for these systems
        }
    }

    public static interface Acceptor {
        public void accept(ClassVisitor cv) throws IOException;
    }

    public static MethodNode findMethod(ObfMapping methodmap, ClassNode cnode) {
        for (MethodNode mnode : cnode.methods) {
            if (methodmap.matches(mnode)) {
                return mnode;
            }
        }
        return null;
    }

    public static FieldNode findField(ObfMapping fieldmap, ClassNode cnode) {
        for (FieldNode fnode : cnode.fields) {
            if (fieldmap.matches(fnode)) {
                return fnode;
            }
        }
        return null;
    }

    public static ClassNode createClassNode(byte[] bytes) {
        return createClassNode(bytes, 0);
    }

    public static ClassNode createClassNode(byte[] bytes, int flags) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, flags);
        return cnode;
    }

    public static byte[] createBytes(ClassNode cnode, int flags) {
        ClassWriter cw = new CC_ClassWriter(flags);
        cnode.accept(cw);
        return cw.toByteArray();
    }

    public static Map<LabelNode, LabelNode> cloneLabels(InsnList list) {
        return new InsnListSection(list).cloneLabels();
    }

    public static InsnList cloneInsnList(InsnList list) {
        return new InsnListSection(list).copy().list;
    }

    public static InsnList cloneInsnList(Map<LabelNode, LabelNode> labelMap, InsnList list) {
        return new InsnListSection(list).copy(labelMap).list;
    }

    public static List<TryCatchBlockNode> cloneTryCatchBlocks(Map<LabelNode, LabelNode> labelMap, List<TryCatchBlockNode> tcblocks) {
        ArrayList<TryCatchBlockNode> clone = new ArrayList<TryCatchBlockNode>();
        for (TryCatchBlockNode node : tcblocks) {
            clone.add(new TryCatchBlockNode(labelMap.get(node.start), labelMap.get(node.end), labelMap.get(node.handler), node.type));
        }

        return clone;
    }

    public static List<LocalVariableNode> cloneLocals(Map<LabelNode, LabelNode> labelMap, List<LocalVariableNode> locals) {
        ArrayList<LocalVariableNode> clone = new ArrayList<LocalVariableNode>(locals.size());
        for (LocalVariableNode node : locals) {
            clone.add(new LocalVariableNode(node.name, node.desc, node.signature, labelMap.get(node.start), labelMap.get(node.end), node.index));
        }

        return clone;
    }

    public static void copy(MethodNode src, MethodNode dst) {
        Map<LabelNode, LabelNode> labelMap = cloneLabels(src.instructions);
        dst.instructions = cloneInsnList(labelMap, src.instructions);
        dst.tryCatchBlocks = cloneTryCatchBlocks(labelMap, src.tryCatchBlocks);
        if (src.localVariables != null) {
            dst.localVariables = cloneLocals(labelMap, src.localVariables);
        }
        dst.visibleAnnotations = src.visibleAnnotations;
        dst.invisibleAnnotations = src.invisibleAnnotations;
        dst.visitMaxs(src.maxStack, src.maxLocals);
    }

    public static String toString(InsnList list) {
        return new InsnListSection(list).toString();
    }

    public static int getLocal(List<LocalVariableNode> list, String name) {
        int found = -1;
        for (LocalVariableNode node : list) {
            if (node.name.equals(name)) {
                if (found >= 0) {
                    throw new RuntimeException("Duplicate local variable: " + name + " not coded to handle this scenario.");
                }

                found = node.index;
            }
        }
        return found;
    }

    public static void replaceMethod(MethodNode original, MethodNode replacement) {
        original.instructions.clear();
        if (original.localVariables != null) {
            original.localVariables.clear();
        }
        if (original.tryCatchBlocks != null) {
            original.tryCatchBlocks.clear();
        }
        replacement.accept(original);
    }

    public static void dump(Acceptor acceptor, File file, boolean filterImportant, boolean sortLocals, boolean textify) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            PrintWriter pout = new PrintWriter(file);
            ClassVisitor cv = new TraceClassVisitor(null, textify ? new Textifier() : new ASMifier(), pout);
            if (filterImportant) {
                cv = new ImportantInsnVisitor(cv);
            }
            if (sortLocals) {
                cv = new LocalVariablesSorterVisitor(cv);
            }
            acceptor.accept(cv);
            pout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void dump(Acceptor acceptor, File file, boolean filterImportant, boolean sortLocals) {
        dump(acceptor, file, filterImportant, sortLocals, config.getTag("textify").getBooleanValue(true));
    }

    public static void dump(final byte[] bytes, File file, boolean filterImportant, boolean sortLocals) {
        dump(new Acceptor() {
            @Override
            public void accept(ClassVisitor cv) {
                new ClassReader(bytes).accept(cv, ClassReader.EXPAND_FRAMES);
            }
        }, file, filterImportant, sortLocals);
    }

    public static void dump(final InputStream is, File file, boolean filterImportant, boolean sortLocals) {
        dump(new Acceptor() {
            @Override
            public void accept(ClassVisitor cv) throws IOException {
                new ClassReader(is).accept(cv, ClassReader.EXPAND_FRAMES);
            }
        }, file, filterImportant, sortLocals);
    }

    public static void dump(final ClassNode cnode, File file, boolean filterImportant, boolean sortLocals) {
        dump(new Acceptor() {
            @Override
            public void accept(ClassVisitor cv) {
                cnode.accept(cv);
            }
        }, file, filterImportant, sortLocals);
    }
}
