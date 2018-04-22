package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrightLamp extends BlockAdvanced implements IShiftDescription, ITileEntityProvider, ISortableBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
//    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.6F, 0.8F);
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.2F, 0.4F, 0.2F, 0.8F, 1.0F, 0.8F);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1.0F);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.4F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);

    //Metadata: bits 0-2 are the side of the base plate using standard side convention (0-5)

    public BlockBrightLamp(String assetName)
    {
        super(Material.GLASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));  //.withProperty(ACTIVE, true));
        this.setHardness(0.1F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
        this.setLightLevel(0.9F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(FACING))
        {
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        case NORTH:
            return NORTH_AABB;
        case DOWN:
            return DOWN_AABB;
        case UP:
        default:
            return UP_AABB;
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        Block block = state.getBlock();
        if (block != this)
        {
            return block.getLightValue(state);
        }
        /**
         * Gets the light value of the specified block coords. Args: x, y, z
         */

        if (world instanceof World)
        {
            return RedstoneUtil.isBlockReceivingRedstone((World) world, pos) ? 0 : this.lightValue;
        }

        return 0;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return 1;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        double boundsMin = 0.2D;
        double boundsMax = 0.8D;
        return new AxisAlignedBB(pos.getX() + boundsMin, pos.getY() + boundsMin, pos.getZ() + boundsMin, pos.getX() + boundsMax, pos.getY() + boundsMax, pos.getZ() + boundsMax);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing side : EnumFacing.VALUES)
        {
            BlockPos offsetPos = pos.offset(side);
            IBlockState state = worldIn.getBlockState(offsetPos);
            if (state.getBlock().isSideSolid(state, worldIn, offsetPos, side.getOpposite()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        EnumFacing opposite = facing.getOpposite();
        BlockPos offsetPos = pos.offset(opposite);
        IBlockState state = world.getBlockState(offsetPos);
        if (state.getBlock().isSideSolid(state, world, offsetPos, facing))
        {
            return this.getDefaultState().withProperty(FACING, opposite);
        }

        return this.getDefaultState().withProperty(FACING, facing);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        EnumFacing side = state.getValue(FACING);

        BlockPos offsetPos = pos.offset(side);
        IBlockState state1 = worldIn.getBlockState(offsetPos);
        if (state1.getBlock().isSideSolid(state1, worldIn, offsetPos, EnumFacing.getFront(side.getIndex() ^ 1)))
        {
            return;
        }

        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
    }

//    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        EnumFacing side = worldIn.getBlockState(pos).getValue(FACING);
//        float var8 = 0.3F;
//
//        if (side == EnumFacing.WEST)
//        {
//            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
//        }
//        else if (side == EnumFacing.EAST)
//        {
//            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
//        }
//        else if (side == EnumFacing.NORTH)
//        {
//            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
//        }
//        else if (side == EnumFacing.SOUTH)
//        {
//            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
//        }
//        else if (side == EnumFacing.DOWN)
//        {
//            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
//        }
//        else
//        {
//            this.setBlockBounds(0.5F - var8, 0.4F, 0.5F - var8, 0.5F + var8, 1.0F, 0.5F + var8);
//        }
//
//        return super.collisionRayTrace(worldIn, pos, start, end);
//    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityArclamp)
            {
                ((TileEntityArclamp) tile).facingChanged();
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityArclamp();
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

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);  //, ACTIVE });
    }

//    @Override
//    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
//    {
//        return state.withProperty(ACTIVE, ((TileEntityArclamp) worldIn.getTileEntity(pos)).getEnabled());
//    }
//
    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
