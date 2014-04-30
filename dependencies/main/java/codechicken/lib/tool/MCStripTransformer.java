package codechicken.lib.tool;

import codechicken.lib.asm.ASMHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

public class MCStripTransformer {
    public static class ReferenceDetector extends Remapper {
        boolean found = false;

        @Override
        public String map(String typeName) {
            if(typeName.startsWith("net/minecraft") || !typeName.contains("/"))
                found = true;
            return typeName;
        }
    }

    public static byte[] transform(byte[] bytes) {
        ClassNode cnode = ASMHelper.createClassNode(bytes, ClassReader.EXPAND_FRAMES);

        boolean changed = false;
        Iterator<MethodNode> it = cnode.methods.iterator();
        while(it.hasNext()) {
            MethodNode mnode = it.next();
            ReferenceDetector r = new ReferenceDetector();
            mnode.accept(new RemappingMethodAdapter(mnode.access, mnode.desc, new MethodVisitor(Opcodes.ASM4) {}, r));
            if(r.found) {
                it.remove();
                changed = true;
            }
        }
        if(changed)
            bytes = ASMHelper.createBytes(cnode, 0);
        return bytes;
    }
}
