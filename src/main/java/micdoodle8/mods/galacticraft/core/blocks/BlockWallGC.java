//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.FenceGateBlock;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.properties.PropertyBool;
//import net.minecraft.block.properties.PropertyEnum;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.block.Blocks;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.Direction;
//import net.minecraft.util.IStringSerializable;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class BlockWallGC extends Block /* Do not extend BlockWall */ implements ISortable
//{
//    protected static final AxisAlignedBB[] AABB_BY_INDEX = VoxelShapes.create[] {
//            VoxelShapes.create(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
//            VoxelShapes.create(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),
//            VoxelShapes.create(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
//            VoxelShapes.create(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),
//            VoxelShapes.create(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),
//            VoxelShapes.create(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D),
//            VoxelShapes.create(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),
//            VoxelShapes.create(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D),
//            VoxelShapes.create(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D),
//            VoxelShapes.create(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),
//            VoxelShapes.create(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D),
//            VoxelShapes.create(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),
//            VoxelShapes.create(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),
//            VoxelShapes.create(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
//            VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),
//            VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
//    };
//    protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = VoxelShapes.create[] {
//            AABB_BY_INDEX[0].setMaxY(1.5D),
//            AABB_BY_INDEX[1].setMaxY(1.5D),
//            AABB_BY_INDEX[2].setMaxY(1.5D),
//            AABB_BY_INDEX[3].setMaxY(1.5D),
//            AABB_BY_INDEX[4].setMaxY(1.5D),
//            AABB_BY_INDEX[5].setMaxY(1.5D),
//            AABB_BY_INDEX[6].setMaxY(1.5D),
//            AABB_BY_INDEX[7].setMaxY(1.5D),
//            AABB_BY_INDEX[8].setMaxY(1.5D),
//            AABB_BY_INDEX[9].setMaxY(1.5D),
//            AABB_BY_INDEX[10].setMaxY(1.5D),
//            AABB_BY_INDEX[11].setMaxY(1.5D),
//            AABB_BY_INDEX[12].setMaxY(1.5D),
//            AABB_BY_INDEX[13].setMaxY(1.5D),
//            AABB_BY_INDEX[14].setMaxY(1.5D),
//            AABB_BY_INDEX[15].setMaxY(1.5D)
//    };
//
//    public final static PropertyBool UP = BooleanProperty.create("up");
//    public final static PropertyBool NORTH = BooleanProperty.create("north");
//    public final static PropertyBool EAST = BooleanProperty.create("east");
//    public final static PropertyBool SOUTH = BooleanProperty.create("south");
//    public final static PropertyBool WEST = BooleanProperty.create("west");
//    public final static EnumProperty<BlockType> VARIANT = EnumProperty.create("variant", BlockType.class);
//
//    public BlockWallGC(String name)
//    {
//        super(Material.ROCK);
//        this.setHardness(1.5F);
//        this.setResistance(2.5F);
//        this.setDefaultState(this.getDefaultState().with(UP, Boolean.FALSE).with(NORTH, Boolean.FALSE).with(EAST, Boolean.FALSE).with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(VARIANT, BlockType.TIN_1_WALL));
//        this.setUnlocalizedName(name);
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isPassable(IBlockReader world, BlockPos pos)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return face != Direction.UP && face != Direction.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
//    }
//
//    @Override
//    public boolean canPlaceTorchOnTop(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        return true;
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        state = this.getActualState(state, source, pos);
//        return AABB_BY_INDEX[getAABBIndex(state)];
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        blockState = this.getActualState(blockState, worldIn, pos);
//        return CLIP_AABB_BY_INDEX[getAABBIndex(blockState)];
//    }
//
//    private static int getAABBIndex(BlockState state)
//    {
//        int i = 0;
//
//        if (state.get(NORTH))
//        {
//            i |= 1 << Direction.NORTH.getHorizontalIndex();
//        }
//
//        if (state.get(EAST))
//        {
//            i |= 1 << Direction.EAST.getHorizontalIndex();
//        }
//
//        if (state.get(SOUTH))
//        {
//            i |= 1 << Direction.SOUTH.getHorizontalIndex();
//        }
//
//        if (state.get(WEST))
//        {
//            i |= 1 << Direction.WEST.getHorizontalIndex();
//        }
//
//        return i;
//    }
//
////    @Override
////    public void setBlockBoundsBasedOnState(IBlockReader world, BlockPos pos)
////    {
////        boolean flag = this.canConnectTo(world, pos.north());
////        boolean flag1 = this.canConnectTo(world, pos.south());
////        boolean flag2 = this.canConnectTo(world, pos.west());
////        boolean flag3 = this.canConnectTo(world, pos.east());
////        float f = 0.25F;
////        float f1 = 0.75F;
////        float f2 = 0.25F;
////        float f3 = 0.75F;
////        float f4 = 1.0F;
////
////        if (flag)
////        {
////            f2 = 0.0F;
////        }
////        if (flag1)
////        {
////            f3 = 1.0F;
////        }
////        if (flag2)
////        {
////            f = 0.0F;
////        }
////        if (flag3)
////        {
////            f1 = 1.0F;
////        }
////
////        if (flag && flag1 && !flag2 && !flag3)
////        {
////            f4 = 0.8125F;
////            f = 0.3125F;
////            f1 = 0.6875F;
////        }
////        else if (!flag && !flag1 && flag2 && flag3)
////        {
////            f4 = 0.8125F;
////            f2 = 0.3125F;
////            f3 = 0.6875F;
////        }
////        this.setBlockBounds(f, 0.0F, f2, f1, f4, f3);
////    }
//
////    @Override
////    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state)
////    {
////        this.setBlockBoundsBasedOnState(world, pos);
////        this.maxY = 1.5D;
////        return super.getCollisionBoundingBox(world, pos, state);
////    }
//
//    private boolean canConnectTo(IBlockReader world, BlockPos pos)
//    {
//        BlockState state = world.getBlockState(pos);
//        Block block = state.getBlock();
//        return block == Blocks.BARRIER ? false : block != this && !(block instanceof FenceGateBlock) ? block.getMaterial(state).isOpaque() && block.isFullCube(state) ? block.getMaterial(state) != Material.GOURD : false : true;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (int i = 0; i < (GalacticraftCore.isPlanetsLoaded ? 6 : 4); ++i)
//        {
//            list.add(new ItemStack(this, 1, i));
//        }
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return this.getMetaFromState(state);
//    }
//
//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return LogicalSide == Direction.DOWN ? super.shouldSideBeRendered(blockState, blockAccess, pos, LogicalSide) : true;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(VARIANT, BlockType.values()[meta % 6]);
//    }
//
//    @Override
//    public int getMetaFromState(BlockState state)
//    {
//        return ((BlockType) state.get(VARIANT)).ordinal();
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        boolean flag = this.canConnectTo(world, pos.north());
//        boolean flag1 = this.canConnectTo(world, pos.east());
//        boolean flag2 = this.canConnectTo(world, pos.south());
//        boolean flag3 = this.canConnectTo(world, pos.west());
//        boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;
//        return state.with(UP, Boolean.valueOf(!flag4 || !world.isAirBlock(pos.up()))).with(NORTH, Boolean.valueOf(flag)).with(EAST, Boolean.valueOf(flag1)).with(SOUTH, Boolean.valueOf(flag2)).with(WEST, Boolean.valueOf(flag3));
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState()
//    {
//        return new BlockStateContainer(this, new IProperty[] { UP, NORTH, EAST, WEST, SOUTH, VARIANT });
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        return EnumSortCategory.WALLS;
//    }
//
//    public enum BlockType implements IStringSerializable
//    {
//        TIN_1_WALL("tin_1_wall"),
//        TIN_2_WALL("tin_2_wall"),
//        MOON_STONE_WALL("moon_stone_wall"),
//        MOON_DUNGEON_BRICK_WALL("moon_dungeon_brick_wall"),
//        MARS_COBBLESTONE_WALL("mars_cobblestone_wall"),
//        MARS_DUNGEON_BRICK_WALL("mars_dungeon_brick_wall");
//
//        private String name;
//
//        BlockType(String name)
//        {
//            this.name = name;
//        }
//
//        @Override
//        public String toString()
//        {
//            return this.getName();
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }
//}