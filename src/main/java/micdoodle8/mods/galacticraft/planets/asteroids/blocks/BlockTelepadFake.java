package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTelepadFake;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTelepadFake extends BlockAdvancedTile implements ITileEntityProvider
{
    public static final PropertyBool TOP = PropertyBool.create("top");
    public static final PropertyBool CONNECTABLE = PropertyBool.create("connectable");
    protected static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
    protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);

    public BlockTelepadFake(String assetName)
    {
        super(GCBlocks.machine);
        this.setSoundType(SoundType.METAL);
//        this.setBlockTextureName(Constants.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
//        this.setBlockTextureName(Constants.TEXTURE_PREFIX + "launch_pad");
        this.setResistance(1000000000000000.0F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return state.getValue(TOP) ? AABB_TOP : AABB_BOTTOM;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
//    {
//        IBlockState state = world.getBlockState(pos);
//        boolean top = state.getValue(TOP);
//
//        if (top)
//        {
//            this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
//        }
//        else
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//        }
//    }

//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        boolean top = state.getValue(TOP);
//
//        if (top)
//        {
//            this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        else
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }

    @Override
    public boolean canDropFromExplosion(Explosion par1Explosion)
    {
        return false;
    }

    public void makeFakeBlock(World worldObj, BlockPos pos, BlockPos mainBlock, IBlockState state)
    {
        worldObj.setBlockState(pos, state, 3);
        ((TileEntityTelepadFake) worldObj.getTileEntity(pos)).setMainBlock(mainBlock);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                return worldIn.getBlockState(mainBlockPosition).getBlock().getBlockHardness(worldIn.getBlockState(mainBlockPosition), worldIn, mainBlockPosition);
            }
        }

        return this.blockHardness;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            ((TileEntityTelepadFake) tileEntity).onBlockRemoval();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityTelepadFake tileEntity = (TileEntityTelepadFake) worldIn.getTileEntity(pos);
        return tileEntity.onActivated(playerIn);
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
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
    public TileEntity createNewTileEntity(World var1, int meta)
    {
        return new TileEntityTelepadFake();
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            Block mainBlockID = world.getBlockState(mainBlockPosition).getBlock();

            if (Blocks.AIR != mainBlockID)
            {
                return mainBlockID.getPickBlock(world.getBlockState(mainBlockPosition), target, world, mainBlockPosition, player);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public EnumFacing getBedDirection(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            return world.getBlockState(pos).getBlock().getBedDirection(world.getBlockState(mainBlockPosition), world, mainBlockPosition);
        }

        return getActualState(world.getBlockState(pos), world, pos).getValue(BlockDirectional.FACING);
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            return world.getBlockState(pos).getBlock().isBed(world.getBlockState(mainBlockPosition), world, mainBlockPosition, player);
        }

        return super.isBed(state, world, pos, player);
    }

    @Override
    public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            world.getBlockState(pos).getBlock().setBedOccupied(world, mainBlockPosition, player, occupied);
        }
        else
        {
            super.setBedOccupied(world, pos, player, occupied);
        }
    }

    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
    {
        TileEntity tileEntity = worldObj.getTileEntity(target.getBlockPos());

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                manager.addBlockHitEffects(mainBlockPosition, target);
            }
        }

        return super.addHitEffects(state, worldObj, target, manager);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TOP, CONNECTABLE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TOP, meta % 2 == 1).withProperty(CONNECTABLE, meta > 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(TOP) ? 1 : 0) + (state.getValue(CONNECTABLE) ? 2 : 0);
    }
}