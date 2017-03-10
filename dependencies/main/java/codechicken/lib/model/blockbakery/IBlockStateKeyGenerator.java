package codechicken.lib.model.blockbakery;

import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * Created by covers1624 on 26/11/2016.
 * TODO Document.
 */
public interface IBlockStateKeyGenerator {

    String generateKey(IExtendedBlockState state);

}
