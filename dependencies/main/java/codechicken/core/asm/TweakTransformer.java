package codechicken.core.asm;

import codechicken.lib.asm.*;
import codechicken.lib.asm.ModularASMTransformer.MethodReplacer;
import codechicken.lib.asm.ModularASMTransformer.MethodTransformer;
import codechicken.lib.asm.ModularASMTransformer.MethodWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import codechicken.lib.config.ConfigTag;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Map;

import static codechicken.lib.asm.InsnComparator.*;

public class TweakTransformer implements IClassTransformer, Opcodes
{
    static {
        ASMInit.init();
    }

    private static ModularASMTransformer transformer = new ModularASMTransformer();
    private static Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/codechickencore/asm/tweaks.asm");
    public static ConfigTag tweaks;

    public static void load() {
        CodeChickenCoreModContainer.loadConfig();
        tweaks = CodeChickenCoreModContainer.config.getTag("tweaks")
                .setComment("Various tweaks that can be applied to game mechanics.").useBraces();
        tweaks.removeTag("persistantLava");

        if (tweaks.getTag("environmentallyFriendlyCreepers")
                .setComment("If set to true, creepers will not destroy landscape. (A version of mobGriefing setting just for creepers)")
                .getBooleanValue(false)) {
            transformer.add(new MethodReplacer(new ObfMapping("net/minecraft/entity/monster/EntityCreeper", "func_146077_cc", "()V"),
                    blocks.get("d_environmentallyFriendlyCreepers"), blocks.get("environmentallyFriendlyCreepers")));
        }

        if (!tweaks.getTag("softLeafReplace")
                .setComment("If set to false, leaves will only replace air when growing")
                .getBooleanValue(false)) {
            transformer.add(new MethodWriter(ACC_PUBLIC, new ObfMapping("net/minecraft/block/Block", "canBeReplacedByLeaves", "(Lnet/minecraft/world/IBlockAccess;III)Z"), blocks.get("softLeafReplace")));
        }

        if (tweaks.getTag("doFireTickOut")
                .setComment("If set to true and doFireTick is disabled in the game rules, fire will still dissipate if it's not over a fire source")
                .getBooleanValue(true)) {
            transformer.add(new MethodTransformer(new ObfMapping("net/minecraft/block/BlockFire", "func_149674_a", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V"))
            {
                @Override
                public void transform(MethodNode mv) {
                    ASMBlock needle = blocks.get("n_doFireTick");
                    ASMBlock inject = blocks.get("doFireTick");

                    ASMBlock key = needle.applyLabels(findOnce(mv.instructions, needle.list));
                    LabelNode jlabel = key.get("LRET");
                    mv.instructions.insertBefore(jlabel, new JumpInsnNode(GOTO, inject.get("LSKIP")));
                    mv.instructions.insert(jlabel, inject.list.list);
                }
            });
        }

        if (tweaks.getTag("finiteWater")
                .setComment("If set to true two adjacent water source blocks will not generate a third.")
                .getBooleanValue(false)) {
            transformer.add(new MethodTransformer(new ObfMapping("net/minecraft/block/BlockDynamicLiquid", "func_149674_a", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V"))
            {
                @Override
                public void transform(MethodNode mv) {
                    InsnListSection key = findOnce(mv.instructions, blocks.get("finiteWater").list);
                    key.setLast(((JumpInsnNode) key.getLast()).label);
                    key.remove();
                }
            });
        }
    }

    @Override
    public byte[] transform(String name, String tname, byte[] bytes) {
        return transformer.transform(name, bytes);
    }
}
