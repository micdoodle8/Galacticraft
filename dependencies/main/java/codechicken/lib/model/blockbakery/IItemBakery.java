package codechicken.lib.model.blockbakery;

import codechicken.lib.model.PerspectiveAwareModelProperties;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by covers1624 on 12/02/2017.
 */
public interface IItemBakery {

    @SideOnly (Side.CLIENT)
    List<BakedQuad> bakeItemQuads(EnumFacing face, ItemStack stack);

    /**
     * Allows ItemModels to define a custom properties. Suitable for thinks like fake blocks.
     * As this method has been added after release, we could potentially break binary compatibility.
     * Suitable checks have been added where this method is called to preserve such compatibility.
     *
     * @return The transforms to use for the model.
     */
    @SideOnly (Side.CLIENT)
    PerspectiveAwareModelProperties getModelProperties(ItemStack stack);
}
