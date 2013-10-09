package codechicken.core.asm;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import com.google.common.collect.HashMultimap;

import codechicken.lib.asm.ASMHelper;
import codechicken.lib.asm.ASMReader;
import codechicken.lib.asm.ObfMapping;
import codechicken.lib.asm.ASMHelper.MethodAltercator;
import codechicken.lib.asm.ASMReader.ASMBlock;
import codechicken.lib.config.ConfigTag;
import net.minecraft.launchwrapper.IClassTransformer;

import static codechicken.lib.asm.InstructionComparator.*;

public class TweakTransformer implements IClassTransformer, Opcodes
{
    private static HashMultimap<String, MethodAltercator> altercators = HashMultimap.create();
    private static Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/codechickencore/asm/tweaks.asm");
    public static ConfigTag tweaks;

    public static void load()
    {
        tweaks = CodeChickenCoreModContainer.config.getTag("tweaks")
                .setComment("Various tweaks that can be applied to game mechanics.").useBraces();
        tweaks.removeTag("persistantLava");
        
        if(tweaks.getTag("environmentallyFriendlyCreepers")
                .setComment("If set to true, creepers will not destroy landscape. (A version of mobGreifing setting just for creepers)")
                .getBooleanValue(false))
        {
            alterMethod(new MethodAltercator(new ObfMapping("net/minecraft/entity/monster/EntityCreeper", "onUpdate", "()V"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    InsnList needle = blocks.get("environmentallyFriendlyCreepers").insns;
                    
                    List<InsnListSection> lists = insnListFindL(mv.instructions, needle);
                    if(lists.size() != 1)
                        throw new RuntimeException("Needle found "+lists.size()+" times in Haystack: " + mv.instructions+"\n" + ASMHelper.printInsnList(needle));

                    InsnListSection subsection = lists.get(0);
                    mv.instructions.insertBefore(subsection.first, new InsnNode(ICONST_0));
                    ASMHelper.removeBlock(mv.instructions, subsection);
                }
            });
        }
        
        if(!tweaks.getTag("softLeafReplace")
                .setComment("If set to false, leaves will only replace air when growing")
                .getBooleanValue(false))
        {
            alterMethod(new MethodAltercator(new ObfMapping("net/minecraft/block/Block", "canBeReplacedByLeaves", "(Lnet/minecraft/world/World;III)Z"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    mv.instructions = blocks.get("softLeafReplace").insns;
                }
            });
        }
        
        if(tweaks.getTag("doFireTickOut")
                .setComment("If set to true and doFireTick is disabed in the game rules, fire will still dissipate if it's not over a fire source")
                .getBooleanValue(true))
        {
            alterMethod(new MethodAltercator(new ObfMapping("net/minecraft/block/BlockFire", "updateTick", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    InsnList needle = blocks.get("doFireTick_needle").insns;
                    
                    List<InsnListSection> lists = insnListFindL(mv.instructions, needle);
                    if(lists.size() != 1)
                        throw new RuntimeException("Needle found "+lists.size()+" times in Haystack: " + mv.instructions+"\n" + ASMHelper.printInsnList(needle));
                    
                    InsnListSection subsection = lists.get(0);
                    LabelNode jlabel = ((JumpInsnNode)subsection.last).label;
                    ASMBlock inject = blocks.get("doFireTick_injection");
                    mv.instructions.insertBefore(jlabel, new JumpInsnNode(GOTO, inject.get("LRET")));
                    mv.instructions.insert(jlabel, inject.insns);
                }
            });
        }
        
        if(tweaks.getTag("finiteWater")
                .setComment("If set to true two adjacent water source blocks will not generate a third.")
                .getBooleanValue(false))
        {
            alterMethod(new MethodAltercator(new ObfMapping("net/minecraft/block/BlockFlowing", "updateTick", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V"))
            {
                @Override
                public void alter(MethodNode mv)
                {
                    InsnList needle = blocks.get("finiteWater").insns;
                    
                    List<InsnListSection> lists = insnListFindL(mv.instructions, needle);
                    if(lists.size() != 1)
                        throw new RuntimeException("Needle found "+lists.size()+" times in Haystack: " + mv.instructions+"\n" + ASMHelper.printInsnList(needle));
                    
                    InsnListSection subsection = lists.get(0);
                    LabelNode jlabel = ((JumpInsnNode)subsection.last).label;
                    subsection.last = jlabel;
                    ASMHelper.removeBlock(mv.instructions, subsection);
                }
            });
        }
    }
    
    private static void alterMethod(MethodAltercator ma)
    {
        altercators.put(ma.method.javaClass(), ma);
    }
    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if (bytes == null) return null;
        bytes = ASMHelper.alterMethods(name, bytes, altercators);
        return bytes;
    }
}
