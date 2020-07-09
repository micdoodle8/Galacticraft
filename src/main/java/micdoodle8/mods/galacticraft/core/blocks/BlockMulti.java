package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.*;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class BlockMulti extends BlockAdvanced implements IPartialSealableBlock, ITileEntityProvider
{
    public static final EnumProperty<EnumBlockMultiType> MULTI_TYPE = EnumProperty.create("type", EnumBlockMultiType.class);
    public static final IntegerProperty RENDER_TYPE = IntegerProperty.create("rendertype", 0, 7);

    protected static final VoxelShape FULL_BLOCK_AABB = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    protected static final VoxelShape AABB_PAD = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
    protected static final VoxelShape AABB_SOLAR = Block.makeCuboidShape(0.0F, 0.2F, 0.0F, 1.0F, 0.8F, 1.0F);
    protected static final VoxelShape AABB_SOLAR_POLE = Block.makeCuboidShape(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
    protected static final VoxelShape AABB_TURRET = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

    public enum EnumBlockMultiType implements IStringSerializable
    {
        SOLAR_PANEL_0(0, "solar"),
        SPACE_STATION_BASE(1, "ss_base"),
        ROCKET_PAD(2, "rocket_pad"),
        NASA_WORKBENCH(3, "nasa_workbench"),
        SOLAR_PANEL_1(4, "solar_panel"),
        CRYO_CHAMBER(5, "cryo_chamber"),
        BUGGY_FUEL_PAD(6, "buggy_pad"),
        MINER_BASE(7, "miner_base"),  //UNUSED
        DISH_LARGE(8, "dish_large"),
        LASER_TURRET(9, "laser_turret");

        private final int meta;
        private final String name;

        EnumBlockMultiType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumBlockMultiType[] values = values();

        public static EnumBlockMultiType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockMulti(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch (state.get(MULTI_TYPE))
        {
        case SOLAR_PANEL_0:
        case SOLAR_PANEL_1:
            boolean midPole = worldIn.getBlockState(pos.up()).getBlock() == this;
            boolean topPole = worldIn.getBlockState(pos.down()).getBlock() == this;
            if (topPole || midPole)
            {
                return midPole ? AABB_SOLAR_POLE : AABB_SOLAR;
            }
            else
            {
                return AABB_SOLAR;
            }
        case ROCKET_PAD:
        case BUGGY_FUEL_PAD:
            return AABB_PAD;
        case LASER_TURRET:
            return AABB_TURRET;
        default:
            return FULL_BLOCK_AABB;
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return null;
    }

    //    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        int meta = getMetaFromState(worldIn.getBlockState(pos));
//
//        if (meta == 2 || meta == 6)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//        }
//        else if (meta == 0 || meta == 4)
//        {
//            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, worldIn.getBlockState(pos.up()).getBlock() == this ? 1.0F : 0.6F, 0.7F);
//        }
//        else
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//        }
//    }

//    @SuppressWarnings("rawtypes")
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
//    {
//        int meta = getMetaFromState(state);
//
//        if (meta == 2 || meta == 6)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        else if (meta == 0 || meta == 4)
//        {
//            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, worldIn.getBlockState(pos.up()).getBlock() == this ? 1.0F : 0.6F, 0.7F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        /*else if (meta == 7)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
//            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
//        }*/
//        else
//        {
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
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

    public void makeFakeBlock(World worldObj, BlockPos pos, BlockPos mainBlock, EnumBlockMultiType type)
    {
        worldObj.setBlockState(pos, GCBlocks.fakeBlock.getDefaultState().with(MULTI_TYPE, type));
        worldObj.setTileEntity(pos, new TileEntityFake(mainBlock));
    }

    public void makeFakeBlock(World worldObj, Collection<BlockPos> posList, BlockPos mainBlock, EnumBlockMultiType type)
    {
        for (BlockPos pos : posList)
        {
            worldObj.setBlockState(pos, this.getDefaultState().with(MULTI_TYPE, type), type == EnumBlockMultiType.CRYO_CHAMBER ? 3 : 0);
            worldObj.setTileEntity(pos, new TileEntityFake(mainBlock));
        }
    }

    @Override
    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                return worldIn.getBlockState(mainBlockPosition).getBlock().getBlockHardness(blockState, worldIn, pos);
            }
        }

        return this.blockHardness;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
    {
        EnumBlockMultiType type = worldIn.getBlockState(pos).get(MULTI_TYPE);

        //Landing pad and refueling pad
        if (type == EnumBlockMultiType.ROCKET_PAD || type == EnumBlockMultiType.BUGGY_FUEL_PAD)
        {
            return direction == Direction.DOWN;
        }

        //Basic solar panel fixed top
        if (type == EnumBlockMultiType.SOLAR_PANEL_1)
        {
            return direction == Direction.UP;
        }

        return false;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityFake)
        {
            ((TileEntityFake) tileEntity).onBlockRemoval();
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntityFake tileEntity = (TileEntityFake) worldIn.getTileEntity(pos);
        if (tileEntity == null)
        {
            return false;
        }
        return tileEntity.onBlockActivated(worldIn, pos, playerIn);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntityFake tileEntity = (TileEntityFake) world.getTileEntity(pos);
        return tileEntity.onBlockWrenched(world, pos, entityPlayer, hand, hit);
    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public TileEntity createNewTileEntity(World var1, int meta)
//    {
//        return null;
//    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                BlockState mainBlockState = world.getBlockState(mainBlockPosition);

                if (Blocks.AIR != mainBlockState.getBlock())
                {
                    return mainBlockState.getBlock().getPickBlock(mainBlockState, target, world, mainBlockPosition, player);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public Direction getBedDirection(BlockState state, IWorldReader world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                BlockState mainState = world.getBlockState(mainBlockPosition);
                return mainState.getBlock().getBedDirection(mainState, world, mainBlockPosition);
            }
        }

        return Direction.UP; // TODO
    }

    @Override
    public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, Entity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityFake)
        {
            BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                BlockState mainState = world.getBlockState(mainBlockPosition);
                return mainState.getBlock().isBed(state, world, mainBlockPosition, player);
            }
        }

        return super.isBed(state, world, pos, player);
    }

    @Override
    public void setBedOccupied(BlockState state, IWorldReader world, BlockPos pos, LivingEntity sleeper, boolean occupied)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
        {
            world.getBlockState(mainBlockPosition).getBlock().setBedOccupied(state, world, pos, sleeper, occupied);
        }
        else
        {
            super.setBedOccupied(state, world, pos, sleeper, occupied);
        }
    }

    @Override
    public Optional<Vec3d> getBedSpawnPosition(EntityType<?> entityType, BlockState state, IWorldReader world, BlockPos pos, @Nullable LivingEntity sleeper)
    {
        if (!(world instanceof World))
        {
            return Optional.empty();
        }
        int tries = 3;
        World worldIn = (World) world;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;
        BlockState cryoChamber = worldIn.getBlockState(mainBlockPosition);
        Direction enumfacing = Direction.NORTH;
//        if (GalacticraftCore.isPlanetsLoaded && cryoChamber.getBlock() == MarsBlocks.machine)
//        {
//            enumfacing = cryoChamber.get(BlockMachineMars.FACING);
//        } TODO Planets
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (int l = 0; l <= 1; ++l)
        {
            int i1 = i - enumfacing.getXOffset() * l - 1;
            int j1 = k - enumfacing.getZOffset() * l - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;

            for (int i2 = i1; i2 <= k1; ++i2)
            {
                for (int j2 = j1; j2 <= l1; ++j2)
                {
                    BlockPos blockpos = new BlockPos(i2, j, j2);

                    if (hasRoomForPlayer(worldIn, blockpos))
                    {
                        if (tries <= 0)
                        {
                            return Optional.of(new Vec3d(blockpos));
                        }

                        --tries;
                    }
                }
            }
        }

        return Optional.empty();
    }

    private static boolean hasRoomForPlayer(World worldIn, BlockPos pos)
    {
        return /*worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), Direction.UP) &&*/ !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
    }

    @Override
    public boolean addHitEffects(BlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
    {
        if (target.getType() == RayTraceResult.Type.BLOCK)
        {
            BlockRayTraceResult result = (BlockRayTraceResult) target;
            TileEntity tileEntity = worldObj.getTileEntity(result.getPos());

            if (tileEntity instanceof TileEntityFake)
            {
                BlockPos mainBlockPosition = ((TileEntityFake) tileEntity).mainBlockPosition;

                if (mainBlockPosition != null && !mainBlockPosition.equals(result.getPos()))
                {
                    manager.addBlockHitEffects(mainBlockPosition, result);
                }
            }
        }

        return super.addHitEffects(state, worldObj, target, manager);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(MULTI_TYPE, EnumBlockMultiType.byMetadata(meta));
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(MULTI_TYPE, RENDER_TYPE);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        EnumBlockMultiType type = state.get(MULTI_TYPE);
//        int renderType = 0;
//
//        switch (type)
//        {
//        case CRYO_CHAMBER:
//            BlockState stateAbove = worldIn.getBlockState(pos.up());
//            TileEntityFake tile = (TileEntityFake) worldIn.getTileEntity(pos);
//            if (stateAbove.getBlock() == this && (stateAbove.get(MULTI_TYPE)) == EnumBlockMultiType.CRYO_CHAMBER)
//            {
//                renderType = 0;
//            }
//            else
//            {
//                renderType = 4;
//            }
//            if (tile != null && tile.mainBlockPosition != null && GalacticraftCore.isPlanetsLoaded)
//            {
//                BlockState stateMain = worldIn.getBlockState(tile.mainBlockPosition);
//                if (stateMain.getBlock() == MarsBlocks.machine && stateMain.get(BlockMachineMars.TYPE) == BlockMachineMars.EnumMachineType.CRYOGENIC_CHAMBER)
//                {
//                    Direction dir = stateMain.get(BlockMachineMars.FACING);
//                    renderType += dir.getHorizontalIndex();
//                }
//            }
//            break;
//        default:
//            break;
//        }
//
//        return state.with(RENDER_TYPE, renderType);
//    }

    public static void onPlacement(World worldIn, BlockPos pos, LivingEntity placer, Block callingBlock)
    {
        final TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof IMultiBlock)
        {
            boolean validSpot = true;
            List<BlockPos> toCheck = new LinkedList<>();
            ((IMultiBlock) tile).getPositions(pos, toCheck);
            for (BlockPos toTest : toCheck)
            {
                BlockState blockAt = worldIn.getBlockState(toTest);
                if (!blockAt.isReplaceable(new DirectionalPlaceContext(worldIn, toTest, Direction.DOWN, ItemStack.EMPTY, Direction.UP)))
                {
                    validSpot = false;
                    break;
                }
            }

            if (!validSpot)
            {
                worldIn.removeBlock(pos, false);

                if (!worldIn.isRemote && placer instanceof ServerPlayerEntity)
                {
                    ServerPlayerEntity player = (ServerPlayerEntity) placer;
                    player.sendMessage(new StringTextComponent(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                    if (!player.abilities.isCreativeMode)
                    {
                        final ItemStack nasaWorkbench = new ItemStack(callingBlock, 1);
                        final ItemEntity entityitem = player.dropItem(nasaWorkbench, false);
                        entityitem.setPickupDelay(0);
                        entityitem.setOwnerId(player.getUniqueID());
                    }
                }

                return;
            }

            ((IMultiBlock) tile).onCreate(worldIn, pos);
        }
    }
}
