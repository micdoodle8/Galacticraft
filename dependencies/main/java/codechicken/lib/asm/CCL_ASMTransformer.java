package codechicken.lib.asm;

import codechicken.lib.asm.ModularASMTransformer.MethodInjector;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.Map;

/**
 * Created by covers1624 on 15/02/2017.
 *
 * Internal CCL class for ASM.
 */
public class CCL_ASMTransformer implements IClassTransformer {

    private ModularASMTransformer transformer = new ModularASMTransformer();

    public CCL_ASMTransformer() {
        Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/ccl/asm/hooks.asm");

        ObfMapping mapping = new ObfMapping("net/minecraft/client/renderer/block/model/ModelBlockDefinition", "func_178331_a", "(Ljava/io/Reader;)Lnet/minecraft/client/renderer/block/model/ModelBlockDefinition;");
        transformer.add(new MethodInjector(mapping, blocks.get("i_BlockStateLoader"), true));

        mapping = new ObfMapping("net/minecraft/client/renderer/ItemRenderer", "func_187461_a", "(Lnet/minecraft/item/ItemStack;)V");
        transformer.add(new MethodInjector(mapping, blocks.get("i_renderMapFirstPerson"), true));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return transformer.transform(name, basicClass);
    }
}
