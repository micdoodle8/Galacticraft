package codechicken.lib.render;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

/**
 * Hooks the location in RenderItem where TileEntityItemStackRenderer.renderByItem is called.
 * Be sure to override isBuiltInRenderer true
 */
public interface IItemRenderer extends IBakedModel {
    public void renderItem(ItemStack item);
}
