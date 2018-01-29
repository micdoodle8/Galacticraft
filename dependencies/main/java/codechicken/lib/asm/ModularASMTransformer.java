package codechicken.lib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import static codechicken.lib.asm.ASMHelper.*;

//TODO Java doc all the internal class constructors.
public class ModularASMTransformer {

    /**
     * Contains a list of transformers for a given class.
     * Also contains some basic logic for doing the actual transform.
     */
    public static class ClassNodeTransformerList {

        List<ClassNodeTransformer> transformers = new LinkedList<ClassNodeTransformer>();
        HashSet<ObfMapping> methodsToSort = new HashSet<ObfMapping>();

        public void add(ClassNodeTransformer t) {
            transformers.add(t);
            t.addMethodsToSort(methodsToSort);
        }

        public byte[] transform(byte[] bytes) {
            ClassNode cnode = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            ClassVisitor cv = cnode;
            if (!methodsToSort.isEmpty()) {
                cv = new LocalVariablesSorterVisitor(methodsToSort, cv);
            }
            reader.accept(cv, ClassReader.EXPAND_FRAMES);

            try {
                int writeFlags = 0;
                for (ClassNodeTransformer t : transformers) {
                    t.transform(cnode);
                    writeFlags |= t.writeFlags;
                }

                bytes = createBytes(cnode, writeFlags);
                if (config.getTag("dump_asm_raw").getBooleanValue(false)) {
                    File file = new File("asm/ccl_modular/" + cnode.name.replace('/', '#') + ".class");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();

                } else if (config.getTag("dump_asm").getBooleanValue(true)) {
                    dump(bytes, new File("asm/ccl_modular/" + cnode.name.replace('/', '#') + ".txt"), false, false);
                }
                return bytes;
            } catch (Exception e) {
                dump(bytes, new File("asm/ccl_modular/" + cnode.name.replace('/', '#') + ".txt"), false, false);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Parent to all transformer's.
     */
    public static abstract class ClassNodeTransformer {

        public int writeFlags;

        public ClassNodeTransformer(int writeFlags) {
            this.writeFlags = writeFlags;
        }

        public ClassNodeTransformer() {
            this(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        }

        public abstract String className();

        public abstract void transform(ClassNode cnode);

        public void addMethodsToSort(Set<ObfMapping> set) {
        }
    }

    /**
     * Base Method transformer.
     */
    public static abstract class MethodTransformer extends ClassNodeTransformer {

        public final ObfMapping method;

        public MethodTransformer(ObfMapping method) {
            this.method = method.toClassloading();
        }

        @Override
        public String className() {
            return method.javaClass();
        }

        @Override
        public void transform(ClassNode cnode) {
            MethodNode mv = findMethod(method, cnode);
            if (mv == null) {
                throw new RuntimeException("Method not found: " + method);
            }

            try {
                transform(mv);
            } catch (Exception e) {
                throw new RuntimeException("Error transforming method: " + method, e);
            }
        }

        public abstract void transform(MethodNode mv);
    }

    /**
     * Writes a method containing the provided InsnList with the ObfMapping as the method name and desc.
     */
    public static class MethodWriter extends ClassNodeTransformer {

        public final int access;
        public final ObfMapping method;
        public final String[] exceptions;
        public InsnList list;

        public MethodWriter(int access, ObfMapping method) {
            this(access, method, null, (InsnList) null);
        }

        public MethodWriter(int access, ObfMapping method, InsnList list) {
            this(access, method, null, list);
        }

        public MethodWriter(int access, ObfMapping method, ASMBlock block) {
            this(access, method, null, block);
        }

        public MethodWriter(int access, ObfMapping method, String[] exceptions) {
            this(access, method, exceptions, (InsnList) null);
        }

        public MethodWriter(int access, ObfMapping method, String[] exceptions, InsnList list) {
            this.access = access;
            this.method = method.toClassloading();
            this.exceptions = exceptions;
            this.list = list;
        }

        public MethodWriter(int access, ObfMapping method, String[] exceptions, ASMBlock block) {
            this(access, method, exceptions, block.rawListCopy());
        }

        @Override
        public String className() {
            return method.javaClass();
        }

        @Override
        public void transform(ClassNode cnode) {
            MethodNode mv = findMethod(method, cnode);
            if (mv == null) {
                mv = (MethodNode) method.visitMethod(cnode, access, exceptions);
            } else {
                mv.access = access;
                mv.instructions.clear();
                if (mv.localVariables != null) {
                    mv.localVariables.clear();
                }
                if (mv.tryCatchBlocks != null) {
                    mv.tryCatchBlocks.clear();
                }
            }

            write(mv);
        }

        public void write(MethodNode mv) {
            logger.debug("Writing method " + method);
            list.accept(mv);
        }
    }

    /**
     * Injects a call before or after the needle.
     * If needle is null it will inject at the start or end of the method.
     */
    public static class MethodInjector extends MethodTransformer {

        public ASMBlock needle;
        public ASMBlock injection;
        public boolean before;

        public MethodInjector(ObfMapping method, ASMBlock needle, ASMBlock injection, boolean before) {
            super(method);
            this.needle = needle;
            this.injection = injection;
            this.before = before;
        }

        public MethodInjector(ObfMapping method, ASMBlock injection, boolean before) {
            this(method, null, injection, before);
        }

        public MethodInjector(ObfMapping method, InsnList needle, InsnList injection, boolean before) {
            this(method, new ASMBlock(needle), new ASMBlock(injection), before);
        }

        public MethodInjector(ObfMapping method, InsnList injection, boolean before) {
            this(method, null, new ASMBlock(injection), before);
        }

        @Override
        public void addMethodsToSort(Set<ObfMapping> set) {
            set.add(method);
        }

        @Override
        public void transform(MethodNode mv) {
            if (needle == null) {
                logger.debug("Injecting " + (before ? "before" : "after") + " method " + method);
                if (before) {
                    mv.instructions.insert(injection.rawListCopy());
                } else {
                    mv.instructions.add(injection.rawListCopy());
                }
            } else {
                for (InsnListSection key : InsnComparator.findN(mv.instructions, needle.list)) {
                    logger.debug("Injecting " + (before ? "before" : "after") + " method " + method + " @ " + key.start + " - " + key.end);
                    ASMBlock injectBlock = injection.copy().mergeLabels(needle.applyLabels(key));

                    if (before) {
                        key.insertBefore(injectBlock.list.list);
                    } else {
                        key.insert(injectBlock.list.list);
                    }
                }
            }
        }
    }

    /**
     * Replaces a specific needle with a specific replacement.
     * Can replace more than one needle.
     */
    public static class MethodReplacer extends MethodTransformer {

        public ASMBlock needle;
        public ASMBlock replacement;

        public MethodReplacer(ObfMapping method, ASMBlock needle, ASMBlock replacement) {
            super(method);
            this.needle = needle;
            this.replacement = replacement;
        }

        public MethodReplacer(ObfMapping method, InsnList needle, InsnList replacement) {
            this(method, new ASMBlock(needle), new ASMBlock(replacement));
        }

        @Override
        public void addMethodsToSort(Set<ObfMapping> set) {
            set.add(method);
        }

        @Override
        public void transform(MethodNode mv) {
            for (InsnListSection key : InsnComparator.findN(mv.instructions, needle.list)) {
                logger.debug("Replacing method " + method + " @ " + key.start + " - " + key.end);
                ASMBlock replaceBlock = replacement.copy().pullLabels(needle.applyLabels(key));
                key.insert(replaceBlock.list.list);
            }
        }
    }

    /**
     * Writes a field to a class.
     * ObfMapping contains the class to put the field.
     */
    public static class FieldWriter extends ClassNodeTransformer {

        public final ObfMapping field;
        public final int access;
        public final Object value;

        public FieldWriter(int access, ObfMapping field, Object value) {
            this.field = field.toClassloading();
            this.access = access;
            this.value = value;
        }

        public FieldWriter(int access, ObfMapping field) {
            this(access, field, null);
        }

        @Override
        public String className() {
            return field.javaClass();
        }

        @Override
        public void transform(ClassNode cnode) {
            field.visitField(cnode, access, value);
        }
    }

    public HashMap<String, ClassNodeTransformerList> transformers = new HashMap<String, ClassNodeTransformerList>();

    /**
     * Adds a ClassNodeTransformer to this transformer.
     *
     * @param t Transformer to add.
     */
    public void add(ClassNodeTransformer t) {
        ClassNodeTransformerList list = transformers.get(t.className());
        if (list == null) {
            transformers.put(t.className(), list = new ClassNodeTransformerList());
        }
        list.add(t);
    }

    /**
     * Runs the transform.
     *
     * @param name  name of the class being loaded.
     * @param bytes Class bytes.
     * @return Returns null if the class passed is null, returns original class if there are no transformers for a given class.
     * Otherwise returns transformed class.
     */
    public byte[] transform(String name, byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        ClassNodeTransformerList list = transformers.get(name);
        return list == null ? bytes : list.transform(bytes);
    }
}
