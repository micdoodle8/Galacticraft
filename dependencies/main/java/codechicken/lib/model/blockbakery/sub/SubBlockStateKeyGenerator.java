package codechicken.lib.model.blockbakery.sub;

import codechicken.lib.model.blockbakery.BlockBakery;
import codechicken.lib.model.blockbakery.IBlockStateKeyGenerator;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 27/12/2016.
 * TODO Document.
 */
public class SubBlockStateKeyGenerator implements IBlockStateKeyGenerator {

    private final Map<Integer, IBlockStateKeyGenerator> subKeyGenMap = new HashMap<Integer, IBlockStateKeyGenerator>();

    public void register(int meta, IBlockStateKeyGenerator subGenerator) {
        subKeyGenMap.put(meta, subGenerator);
    }

    @Override
    public String generateKey(IExtendedBlockState state) {
        int meta = state.getBlock().getMetaFromState(state);
        if (subKeyGenMap.containsKey(meta)) {
            IBlockStateKeyGenerator keyGenerator = subKeyGenMap.get(meta);
            return keyGenerator.generateKey(state);
        }
        return BlockBakery.defaultBlockKeyGenerator.generateKey(state);
    }
}
