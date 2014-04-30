package codechicken.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.util.Set;

public class LocalVariablesSorterVisitor extends ClassVisitor
    {
        public Set<ObfMapping> methods;
        public String owner;

        public LocalVariablesSorterVisitor(Set<ObfMapping> methods, ClassVisitor cv) {
            super(Opcodes.ASM4, cv);
            this.methods = methods;
        }

        public LocalVariablesSorterVisitor(ClassVisitor cv) {
            this(null, cv);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            owner = name;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            return methods == null || methods.contains(new ObfMapping(owner, name, desc)) ? new LocalVariablesSorter(access, desc, mv) : mv;
        }
    }

    