package codechicken.lib.render.item;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;

/**
 * Hooks to the location of RenderItem#renderItem(ItemStack, IBakedModel)
 */
//TODO, We need a perspective aware IItemRenderer
public interface IItemRenderer extends IBakedModel {

    /**
     * Used as a CallBack to render a TileEntity / Item after Transforms have been applied.
     *
     * @param item Stack to render.
     */
    void renderItem(ItemStack item);

    //boolean shouldHandleRender(ItemStack stack);
}
