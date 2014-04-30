package codechicken.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

public class ImportantInsnVisitor extends ClassVisitor
{
    public class ImportantInsnMethodVisitor extends MethodVisitor
    {
        MethodVisitor delegate;

        public ImportantInsnMethodVisitor(int access, String name, String desc, String signature, String[] exceptions) {
            super(Opcodes.ASM4, new MethodNode(access, name, desc, signature, exceptions));
            delegate = cv.visitMethod(access, name, desc, signature, exceptions);
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            MethodNode mnode = (MethodNode)mv;
            mnode.instructions = InsnComparator.getImportantList(mnode.instructions);
            mnode.accept(delegate);
        }
    }

    public ImportantInsnVisitor(ClassVisitor cv) {
        super(Opcodes.ASM4, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new ImportantInsnMethodVisitor(access, name, desc, signature, exceptions);
    }
}
