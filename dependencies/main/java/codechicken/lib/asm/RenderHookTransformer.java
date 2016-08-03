package codechicken.lib.asm;

import codechicken.lib.asm.ModularASMTransformer.MethodInjector;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Map;

public class RenderHookTransformer implements IClassTransformer {
    private ModularASMTransformer transformer = new ModularASMTransformer();

    public RenderHookTransformer() {
        Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/ccl/asm/hooks.asm");
        transformer.add(new MethodInjector(new ObfMapping("net/minecraft/client/renderer/entity/RenderItem", "func_180454_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"), blocks.get("n_IItemRenderer"), blocks.get("IItemRenderer"), true));
    }

    @Override
    public byte[] transform(String name, String tname, byte[] bytes) {
        return transformer.transform(name, bytes);
    }
}
