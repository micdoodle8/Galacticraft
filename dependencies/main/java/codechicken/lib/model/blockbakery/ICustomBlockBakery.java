package codechicken.lib.model.blockbakery;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by covers1624 on 28/10/2016.
 * TODO Document.
 * TODO ICustomBlockBakery > IItemBakery
 * TODO new Base class ICustomBakery
 */
public interface ICustomBlockBakery {

    //TODO Move to IBlockBakery.
    @SideOnly (Side.CLIENT)
    IExtendedBlockState handleState(IExtendedBlockState state, TileEntity tileEntity);

    @SideOnly (Side.CLIENT)
    List<BakedQuad> bakeItemQuads(EnumFacing face, ItemStack stack);
}
