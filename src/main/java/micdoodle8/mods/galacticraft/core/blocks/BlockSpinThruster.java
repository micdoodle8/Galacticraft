package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityThruster;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSpinThruster extends BlockAdvanced implements IShiftDescription, ITileEntityProvider, ISortableBlock
{
    //public static IIcon thrusterIcon;
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    protected BlockSpinThruster(String assetName)
    {
        super(Material.circuits);
        this.setHardness(0.1F);
        this.setStepSound(Block.soundTypeWood);
        //this.setBlockTextureName("stone");
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    private static boolean isBlockSolidOnSide(World world, BlockPos pos, EnumFacing direction)
    {
        return world.getBlockState(pos).getBlock().isSideSolid(world, pos, direction);
    }

    //	@Override
    //	@SideOnly(Side.CLIENT)
    //	public IIcon getIcon(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
    //	{
    //		return BlockSpinThruster.thrusterIcon;
    //	}
    //
    //	@Override
    //	@SideOnly(Side.CLIENT)
    //	public IIcon getIcon(int par1, int x)
    //	{
    //		return BlockSpinThruster.thrusterIcon;
    //	}
    //
    //	@Override
    //	@SideOnly(Side.CLIENT)
    //	public void registerBlockIcons(IIconRegister par1IconRegister)
    //	{
    //		BlockSpinThruster.thrusterIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "spinThruster");
    //	}

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
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

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.WEST), EnumFacing.EAST)
                || BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.EAST), EnumFacing.WEST)
                || BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.NORTH), EnumFacing.SOUTH)
                || BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.SOUTH), EnumFacing.NORTH);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(facing.getHorizontalIndex());
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (getMetaFromState(state) == 0)
        {
            this.onBlockAdded(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        int metadata = getMetaFromState(state);
//        TileEntityThruster tile = (TileEntityThruster) worldIn.getTileEntity(pos);
//
//        if (metadata == 0)
//        {
//            if (BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.WEST), EnumFacing.EAST))
//            {
//                metadata = 2;
//            }
//            else if (BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.EAST), EnumFacing.WEST))
//            {
//                metadata = 3;
//            }
//            else if (BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.NORTH), EnumFacing.SOUTH))
//            {
//                metadata = 0;
//            }
//            else if (BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.SOUTH), EnumFacing.NORTH))
//            {
//                metadata = 1;
//            }
//
//            worldIn.setBlockState(pos, getStateFromMeta(metadata), 3);
//        }

        BlockPos baseBlock;
        switch (metadata)
        {
        case 1:
            baseBlock = pos.offset(EnumFacing.WEST);
            break;
        case 2:
            baseBlock = pos.offset(EnumFacing.EAST);
            break;
        case 3:
            baseBlock = pos.offset(EnumFacing.NORTH);
            break;
        case 4:
            baseBlock = pos.offset(EnumFacing.SOUTH);
            break;
        default:
            this.dropTorchIfCantStay(worldIn, pos);
            return;
        }

        if (!worldIn.isRemote)
        {
            if (worldIn.provider instanceof WorldProviderOrbit)
            {
                ((WorldProviderOrbit) worldIn.provider).checkSS(baseBlock, true);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (this.dropTorchIfCantStay(worldIn, pos))
        {
            final int var6 = getMetaFromState(state) & 7;
            boolean var7 = false;

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.WEST), EnumFacing.EAST) && var6 == 1)
            {
                var7 = true;
            }

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.EAST), EnumFacing.WEST) && var6 == 2)
            {
                var7 = true;
            }

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.NORTH), EnumFacing.SOUTH) && var6 == 3)
            {
                var7 = true;
            }

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.SOUTH), EnumFacing.NORTH) && var6 == 4)
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }

        if (!worldIn.isRemote)
        {
            if (worldIn.provider instanceof WorldProviderOrbit)
            {
                ((WorldProviderOrbit) worldIn.provider).checkSS(pos, true);
            }
        }
    }

    /**
     * Tests if the block can remain at its current location and will drop as an
     * item if it is unable to stay. Returns True if it can stay and False if it
     * drops. Args: world, x, y, z
     */
    private boolean dropTorchIfCantStay(World worldIn, BlockPos pos)
    {
        if (!this.canPlaceBlockAt(worldIn, pos))
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit. Args: world, x, y, z, startVec, endVec
     */
    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        float var8 = 0.3F;

        EnumFacing facing = worldIn.getBlockState(pos).getValue(BlockMachine.FACING);

        switch (facing)
        {
        case NORTH:
            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
            break;
        case EAST:
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
            break;
        case SOUTH:
            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
            break;
        case WEST:
            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
            break;
        }

        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        //TODO this is torch code as a placeholder, still need to adjust positioning and particle type
        //Also make small thrust sounds
        if (worldIn.provider instanceof WorldProviderOrbit)
        {
            if (((WorldProviderOrbit) worldIn.provider).thrustersFiring || rand.nextInt(80) == 0)
            {
                final int var6 = getMetaFromState(state) & 7;
                final double var7 = pos.getX() + 0.5F;
                final double var9 = pos.getY() + 0.7F;
                final double var11 = pos.getZ() + 0.5F;
                final double var13 = 0.2199999988079071D;
                final double var15 = 0.27000001072883606D;

                if (var6 == 1)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 2)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 3)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 4)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        EnumFacing currentFacing = world.getBlockState(pos).getValue(FACING);
        for (EnumFacing nextFacing = currentFacing.rotateY(); ; nextFacing = nextFacing.rotateY())
        {
            if (BlockSpinThruster.isBlockSolidOnSide(world, pos.offset(nextFacing.getOpposite()), nextFacing))
            {
                world.setBlockState(pos, getStateFromMeta(nextFacing.getHorizontalIndex()), 2);
                break;
            }

            if (nextFacing == currentFacing)
            {
                break;
            }
        }

        if (world.provider instanceof WorldProviderOrbit && !world.isRemote)
        {
            WorldProviderOrbit worldOrbital = (WorldProviderOrbit) world.provider;
            worldOrbital.checkSS(pos, true);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityThruster();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            final int facing = getMetaFromState(state) & 8;
            if (worldIn.provider instanceof WorldProviderOrbit)
            {
                WorldProviderOrbit worldOrbital = (WorldProviderOrbit) worldIn.provider;
                worldOrbital.removeThruster(pos, facing == 0);
                worldOrbital.updateSpinSpeed();
            }
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
