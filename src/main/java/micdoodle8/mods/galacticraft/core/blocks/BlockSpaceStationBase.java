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
import net.minecraft.world.World;

public class BlockSpaceStationBase extends BlockContainer implements ITileEntityProvider
{
    //private IIcon[] spaceStationIcons;

    public BlockSpaceStationBase(String assetName)
    {
        super(Material.rock);
        this.setHardness(-1);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        return -1.0F;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.spaceStationIcons = new IIcon[2];
        this.spaceStationIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "space_station_top");
        this.spaceStationIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "space_station_side");
        this.blockIcon = this.spaceStationIcons[0];
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        switch (par1)
        {
        case 1:
            return this.spaceStationIcons[0];
        default:
            return this.spaceStationIcons[1];
        }
    }*/

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
            ((IMultiBlock) tile).onCreate(pos);
        }
    }
}
