package codechicken.lib.model.blockbakery.sub;

import codechicken.lib.model.blockbakery.BlockBakery;
import codechicken.lib.model.blockbakery.IItemStackKeyGenerator;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 30/12/2016.
 * TODO Document.
 */
public class SubItemStackKeyGenerator implements IItemStackKeyGenerator {

    private final Map<Integer, IItemStackKeyGenerator> subKeyGenMap = new HashMap<Integer, IItemStackKeyGenerator>();

    public void register(int meta, IItemStackKeyGenerator subGenerator) {
        subKeyGenMap.put(meta, subGenerator);
    }

    @Override
    public String generateKey(ItemStack stack) {
        int meta = stack.getMetadata();
        if (subKeyGenMap.containsKey(meta)) {
            IItemStackKeyGenerator generator = subKeyGenMap.get(meta);
            return generator.generateKey(stack);
        }
        return BlockBakery.defaultItemKeyGenerator.generateKey(stack);
    }
}
