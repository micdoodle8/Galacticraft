package codechicken.lib.model.blockbakery;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by covers1624 on 28/10/2016.
 * TODO Document.
 */
public interface ILayeredBlockBakery extends ICustomBlockBakery {

    @SideOnly (Side.CLIENT)
    List<BakedQuad> bakeLayerFace(EnumFacing face, BlockRenderLayer layer, IExtendedBlockState state);

}
