package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockLandingPadFull extends BlockAdvancedTile implements IPartialSealableBlock
{
    //private IIcon[] icons = new IIcon[3];

    public BlockLandingPadFull(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundTypeStone);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
        this.maxY = 0.39D;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 9;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(GCBlocks.landingPad);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        switch (getMetaFromState(state))
        {
        case 0:
            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
        case 2:
            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
        default:
            return AxisAlignedBB.fromBounds(pos.getX() + 0.0D, pos.getY() + 0.0D, pos.getZ() + 0.0D,
                    pos.getX() + 1.0D, pos.getY() + 0.2D, pos.getZ() + 1.0D);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        switch (getMetaFromState(worldIn.getBlockState(pos)))
        {
        case 0:
            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
        case 2:
            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
        default:
            return AxisAlignedBB.fromBounds(pos.getX() + 0.0D, pos.getY() + 0.0D, pos.getZ() + 0.0D,
                    pos.getX() + 1.0D, pos.getY() + 0.2D, pos.getZ() + 1.0D);
        }
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    /*@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.icons[0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "launch_pad");
        this.icons[1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "buggy_fueler");
        this.icons[2] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "buggy_fueler_blank");
        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "launch_pad");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        switch (par2)
        {
        case 0:
            return this.icons[0];
        case 1:
            return this.icons[1];
        case 2:
            return this.icons[2];
        }

        return this.blockIcon;
    }*/

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (int x2 = -1; x2 < 2; ++x2)
        {
            for (int z2 = -1; z2 < 2; ++z2)
            {
                if (!super.canPlaceBlockAt(worldIn, new BlockPos(pos.getX() + x2, pos.getY(), pos.getZ() + z2)))
                {
                    return false;
                }
            }

        }

        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        switch (getMetaFromState(state))
        {
        case 0:
            return new TileEntityLandingPad();
        case 1:
            return new TileEntityBuggyFueler();
        // case 2:
        // return new GCCoreTileEntityCargoPad();
        default:
            return null;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        worldIn.markBlockForUpdate(pos);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return true;
	}

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return direction == EnumFacing.UP;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));
        return new ItemStack(Item.getItemFromBlock(GCBlocks.landingPad), 1, metadata);
    }
}
