package codechicken.lib.model.blockbakery;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implement this on an item and register a model with an ItemOverrideList.handleItemState calling BlockBakery.getCachedItemModel to use this.
 * Make sure you register an IItemStackKeyGenerator
 *
 * Created by covers1624 on 12/02/2017.
 */
@Deprecated//Still safe to use, 1.11 will have lots of changes to this entire pipeline.
public interface IBakeryItem {

    @SideOnly (Side.CLIENT)
    IItemBakery getBakery();

}
