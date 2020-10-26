package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTelepadFake;
import net.minecraft.block.*;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTelepadFake extends BlockAdvancedTile
{
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty CONNECTABLE = BooleanProperty.create("connectable");
    protected static final VoxelShape AABB_TOP = VoxelShapes.create(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
    protected static final VoxelShape AABB_BOTTOM = VoxelShapes.create(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);

    public BlockTelepadFake(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return state.get(TOP) ? AABB_TOP : AABB_BOTTOM;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
//    {
//        IBlockState state = world.getBlockState(pos);
//        boolean top = state.get(TOP);
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
//        boolean top = state.get(TOP);
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
//    @OnlyIn(Dist.CLIENT)
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

    public void makeFakeBlock(World worldObj, BlockPos pos, BlockPos mainBlock, BlockState state)
    {
        worldObj.setBlockState(pos, state, 3);
        ((TileEntityTelepadFake) worldObj.getTileEntity(pos)).setMainBlock(mainBlock);
    }

    @Override
    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos)
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
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            ((TileEntityTelepadFake) tileEntity).onBlockRemoval();
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        TileEntityTelepadFake tileEntity = (TileEntityTelepadFake) worldIn.getTileEntity(pos);
        return tileEntity.onActivated(playerIn);
    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTelepadFake();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
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
    public Direction getBedDirection(BlockState state, IWorldReader world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            return world.getBlockState(pos).getBlock().getBedDirection(world.getBlockState(mainBlockPosition), world, mainBlockPosition);
        }

        return state.get(DirectionalBlock.FACING);
    }

    @Override
    public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, @Nullable Entity player)
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
    public void setBedOccupied(BlockState state, IWorldReader world, BlockPos pos, LivingEntity sleeper, boolean occupied)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            world.getBlockState(pos).getBlock().setBedOccupied(state, world, pos, sleeper, occupied);
        }
        else
        {
            super.setBedOccupied(state, world, pos, sleeper, occupied);
        }
    }

    @Override
    public boolean addHitEffects(BlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
    {
        TileEntity tileEntity = worldObj.getTileEntity(new BlockPos(target.getHitVec()));

        if (tileEntity instanceof TileEntityTelepadFake && target instanceof BlockRayTraceResult)
        {
            BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                manager.addBlockHitEffects(mainBlockPosition, (BlockRayTraceResult) target);
            }
        }

        return super.addHitEffects(state, worldObj, target, manager);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(TOP, CONNECTABLE);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(TOP, meta % 2 == 1).with(CONNECTABLE, meta > 1);
//    }
}