package codechicken.core.asm;

import java.util.Iterator;

import codechicken.lib.asm.ASMInit;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import codechicken.lib.asm.ASMHelper;
import codechicken.lib.asm.ObfMapping;

import net.minecraft.launchwrapper.IClassTransformer;

public class InterfaceDependancyTransformer implements IClassTransformer
{
    static {
        ASMInit.init();
    }

    @Override
    public byte[] transform(String name, String tname, byte[] bytes) {
        if (bytes == null) return null;
        ClassNode cnode = ASMHelper.createClassNode(bytes);

        boolean hasDependancyInterfaces = false;
        if (cnode.visibleAnnotations != null)
            for (AnnotationNode ann : cnode.visibleAnnotations)
                if (ann.desc.equals(Type.getDescriptor(InterfaceDependancies.class))) {
                    hasDependancyInterfaces = true;
                    break;
                }

        if (!hasDependancyInterfaces)
            return bytes;

        hasDependancyInterfaces = false;
        for (Iterator<String> iterator = cnode.interfaces.iterator(); iterator.hasNext(); ) {
            try {
                Launch.classLoader.findClass(new ObfMapping(iterator.next()).toRuntime().javaClass());
            } catch (ClassNotFoundException cnfe) {
                iterator.remove();
                hasDependancyInterfaces = true;
            }
        }

        if (!hasDependancyInterfaces)
            return bytes;

        return ASMHelper.createBytes(cnode, 0);
    }
}
