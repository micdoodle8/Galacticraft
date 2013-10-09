package codechicken.core.asm;

import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import codechicken.lib.asm.ASMHelper;
import codechicken.lib.asm.ASMReader;
import codechicken.lib.asm.ObfMapping;
import codechicken.lib.asm.ASMReader.ASMBlock;

import net.minecraft.launchwrapper.IClassTransformer;

public class FeatureHackTransformer implements Opcodes, IClassTransformer
{    
    private static Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/codechickencore/asm/hacks.asm");
    
    /**
     * Allow GameData to hide some items.
     */
    ObfMapping m_newItemAdded = new ObfMapping("cpw/mods/fml/common/registry/GameData", "newItemAdded", "(Lnet/minecraft/item/Item;)V");
    
    private byte[] transformer001(String name, byte[] bytes)
    {
        ClassNode cnode = ASMHelper.createClassNode(bytes);
        MethodNode mnode = ASMHelper.findMethod(m_newItemAdded, cnode);
        
        mnode.instructions.insert(mnode.instructions.get(1), blocks.get("silentOverride").insns);
        
        bytes = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
        return bytes;
    }
    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if (bytes == null) return null;
        if(m_newItemAdded.isClass(name))
            bytes = transformer001(name, bytes);
        return bytes;
    }
}
