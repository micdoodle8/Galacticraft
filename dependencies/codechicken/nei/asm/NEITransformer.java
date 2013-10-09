package codechicken.nei.asm;

import java.util.Map;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import codechicken.core.asm.ClassOverrider;
import codechicken.lib.asm.ASMHelper;
import codechicken.lib.asm.ASMReader;
import codechicken.lib.asm.ClassHeirachyManager;
import codechicken.lib.asm.ObfMapping;
import codechicken.lib.asm.ASMReader.ASMBlock;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.ClassWriter.*;
import static codechicken.lib.asm.InstructionComparator.*;

public class NEITransformer implements IClassTransformer
{
    private Map<String, ASMBlock> asmblocks = ASMReader.loadResource("/assets/nei/asm/blocks.asm");
    
    /**
     * Adds super.updateScreen() to non implementing GuiContainer subclasses
     */
    private ObfMapping c_GuiContainer = new ObfMapping("net/minecraft/client/gui/inventory/GuiContainer");
    public byte[] transformer001(String name, byte[] bytes)
    {
        if(ClassHeirachyManager.classExtends(name, c_GuiContainer.javaClass()))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);

            ObfMapping methodmap = new ObfMapping("net/minecraft/client/gui/GuiScreen", "updateScreen", "()V");
            ObfMapping supermap = new ObfMapping(methodmap, cnode.superName);

            InsnList supercall = new InsnList();
            supercall.add(new VarInsnNode(ALOAD, 0));
            supercall.add(supermap.toInsn(INVOKESPECIAL));

            boolean changed = false;
            for(MethodNode methodnode : cnode.methods)
            {
                if(methodmap.matches(methodnode))
                {
                    InsnList importantNodeList = getImportantList(methodnode.instructions);
                    if(!insnListMatches(importantNodeList, supercall, 0))
                    {
                        methodnode.instructions.insertBefore(methodnode.instructions.getFirst(), supercall);
                        System.out.println("Inserted super call into " + name + "." + supermap.s_name);
                        changed = true;
                    }
                }
            }
            
            if(changed)
                bytes = ASMHelper.createBytes(cnode, COMPUTE_MAXS | COMPUTE_FRAMES);
        }
        return bytes;
    }

    /**
     * Generates method for setting the placed position for the mob spawner item
     */
    private ObfMapping c_BlockMobSpawner = new ObfMapping("net/minecraft/block/BlockMobSpawner");
    public byte[] transformer002(String name, byte[] bytes)
    {
        if(c_BlockMobSpawner.isClass(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);

            ObfMapping methodmap = new ObfMapping("net/minecraft/block/Block", "onBlockPlacedBy", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V");
            MethodVisitor mv = cnode.visitMethod(ACC_PUBLIC, methodmap.s_name, methodmap.s_desc, null, null);
            
            mv.visitCode();
            asmblocks.get("mobspawner").insns.accept(mv);
            mv.visitMaxs(1, 4);
            
            bytes = ASMHelper.createBytes(cnode, COMPUTE_MAXS | COMPUTE_FRAMES);
            System.out.println("Generated BlockMobSpawner helper method.");
        }
        return bytes;
    }

    private ObfMapping m_func_98281_h = new ObfMapping("net/minecraft/tileentity/MobSpawnerBaseLogic", "func_98281_h", "()Lnet/minecraft/entity/Entity;");
    public byte[] transformer003(String name, byte[] bytes)
    {
        if(m_func_98281_h.isClass(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);
            MethodNode mnode = ASMHelper.findMethod(m_func_98281_h, cnode);
            
            AbstractInsnNode node = insnListFindStart(mnode.instructions, 
                    asmblocks.get("needle4").insns).get(0);
            
            mnode.instructions.insertBefore(node, asmblocks.get("call4").insns);
            mnode.instructions.remove(node);
            
            bytes = ASMHelper.createBytes(cnode, COMPUTE_MAXS | COMPUTE_FRAMES);
        }
        return bytes;
    }

    private ObfMapping m_Block_init = new ObfMapping("net/minecraft/block/Block", "<init>", "(ILnet/minecraft/block/material/Material;)V");
    public byte[] transformer004(String name, byte[] bytes)
    {
        if(m_Block_init.isClass(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);
            MethodNode mnode = ASMHelper.findMethod(m_Block_init, cnode);
            
            AbstractInsnNode node1 = insnListFindStart(mnode.instructions, 
                    asmblocks.get("needle1").insns).get(0);
            AbstractInsnNode node2 = insnListFindStart(mnode.instructions, 
                    asmblocks.get("needle2").insns).get(0);
            
            mnode.instructions.insertBefore(node1, 
                    asmblocks.get("call").insns);
            ASMHelper.removeBlock(mnode.instructions, new InsnListSection(node1, node2));
            
            InsnListSection block3 = insnListFindL(mnode.instructions, 
                    asmblocks.get("needle3").insns).get(0);

            ASMBlock pre3 = asmblocks.get("pre3");
            mnode.instructions.insertBefore(block3.first, pre3.insns);
            mnode.instructions.insert(block3.last, pre3.get("LPOST3"));
            
            bytes = ASMHelper.createBytes(cnode, COMPUTE_MAXS | COMPUTE_FRAMES);
        }
        return bytes;
    }

    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if (bytes == null) return null;
        try
        {
            if(FMLLaunchHandler.side().isClient())
            {
                bytes = transformer001(name, bytes);
                bytes = transformer002(name, bytes);
                bytes = transformer003(name, bytes);
                bytes = transformer004(name, bytes);
                bytes = ClassOverrider.overrideBytes(name, bytes, new ObfMapping("net/minecraft/client/gui/inventory/GuiContainer"), NEICorePlugin.location);
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }

        return bytes;
    }
}
