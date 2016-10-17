package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySpaceStationBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockSpaceStationBase extends BlockContainer implements ITileEntityProvider
{
    public BlockSpaceStationBase(String assetName)
    {
        super(Material.rock);
        this.setHardness(-1);    
        this.setUnlocalizedName(assetName);
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        return -1.0F;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity tileAt = worldIn.getTileEntity(pos);

        if (tileAt instanceof IMultiBlock)
        {
            ((IMultiBlock) tileAt).onDestroy(tileAt);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntitySpaceStationBase();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof IMultiBlock)
        {
            ((IMultiBlock) tile).onCreate(worldIn, pos);
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
    {
    	return null;
    }
}
