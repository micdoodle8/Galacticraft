package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWallGC extends Block /* Do not extend BlockWall */ implements ISortableBlock
{
    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),
            new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D),
            new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),
            new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };
    protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[] {
            AABB_BY_INDEX[0].setMaxY(1.5D),
            AABB_BY_INDEX[1].setMaxY(1.5D),
            AABB_BY_INDEX[2].setMaxY(1.5D),
            AABB_BY_INDEX[3].setMaxY(1.5D),
            AABB_BY_INDEX[4].setMaxY(1.5D),
            AABB_BY_INDEX[5].setMaxY(1.5D),
            AABB_BY_INDEX[6].setMaxY(1.5D),
            AABB_BY_INDEX[7].setMaxY(1.5D),
            AABB_BY_INDEX[8].setMaxY(1.5D),
            AABB_BY_INDEX[9].setMaxY(1.5D),
            AABB_BY_INDEX[10].setMaxY(1.5D),
            AABB_BY_INDEX[11].setMaxY(1.5D),
            AABB_BY_INDEX[12].setMaxY(1.5D),
            AABB_BY_INDEX[13].setMaxY(1.5D),
            AABB_BY_INDEX[14].setMaxY(1.5D),
            AABB_BY_INDEX[15].setMaxY(1.5D)
    };

    public final static PropertyBool UP = PropertyBool.create("up");
    public final static PropertyBool NORTH = PropertyBool.create("north");
    public final static PropertyBool EAST = PropertyBool.create("east");
    public final static PropertyBool SOUTH = PropertyBool.create("south");
    public final static PropertyBool WEST = PropertyBool.create("west");
    public final static PropertyEnum<BlockType> VARIANT = PropertyEnum.create("variant", BlockType.class);

    public BlockWallGC(String name)
    {
        super(Material.ROCK);
        this.setHardness(1.5F);
        this.setResistance(2.5F);
        this.setDefaultState(this.getDefaultState().withProperty(UP, Boolean.FALSE).withProperty(NORTH, Boolean.FALSE).withProperty(EAST, Boolean.FALSE).withProperty(SOUTH, Boolean.FALSE).withProperty(WEST, Boolean.FALSE).withProperty(VARIANT, BlockType.TIN_1_WALL));
        this.setUnlocalizedName(name);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        state = this.getActualState(state, source, pos);
        return AABB_BY_INDEX[getAABBIndex(state)];
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        blockState = this.getActualState(blockState, worldIn, pos);
        return CLIP_AABB_BY_INDEX[getAABBIndex(blockState)];
    }

    private static int getAABBIndex(IBlockState state)
    {
        int i = 0;

        if (state.getValue(NORTH))
        {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (state.getValue(EAST))
        {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (state.getValue(SOUTH))
        {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (state.getValue(WEST))
        {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
//    {
//        boolean flag = this.canConnectTo(world, pos.north());
//        boolean flag1 = this.canConnectTo(world, pos.south());
//        boolean flag2 = this.canConnectTo(world, pos.west());
//        boolean flag3 = this.canConnectTo(world, pos.east());
//        float f = 0.25F;
//        float f1 = 0.75F;
//        float f2 = 0.25F;
//        float f3 = 0.75F;
//        float f4 = 1.0F;
//
//        if (flag)
//        {
//            f2 = 0.0F;
//        }
//        if (flag1)
//        {
//            f3 = 1.0F;
//        }
//        if (flag2)
//        {
//            f = 0.0F;
//        }
//        if (flag3)
//        {
//            f1 = 1.0F;
//        }
//
//        if (flag && flag1 && !flag2 && !flag3)
//        {
//            f4 = 0.8125F;
//            f = 0.3125F;
//            f1 = 0.6875F;
//        }
//        else if (!flag && !flag1 && flag2 && flag3)
//        {
//            f4 = 0.8125F;
//            f2 = 0.3125F;
//            f3 = 0.6875F;
//        }
//        this.setBlockBounds(f, 0.0F, f2, f1, f4, f3);
//    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state)
//    {
//        this.setBlockBoundsBasedOnState(world, pos);
//        this.maxY = 1.5D;
//        return super.getCollisionBoundingBox(world, pos, state);
//    }

    private boolean canConnectTo(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block == Blocks.BARRIER ? false : block != this && !(block instanceof BlockFenceGate) ? block.getMaterial(state).isOpaque() && block.isFullCube(state) ? block.getMaterial(state) != Material.GOURD : false : true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < (GalacticraftCore.isPlanetsLoaded ? 6 : 4); ++i)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.DOWN ? super.shouldSideBeRendered(blockState, blockAccess, pos, side) : true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockType.values()[meta % 6]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockType) state.getValue(VARIANT)).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        boolean flag = this.canConnectTo(world, pos.north());
        boolean flag1 = this.canConnectTo(world, pos.east());
        boolean flag2 = this.canConnectTo(world, pos.south());
        boolean flag3 = this.canConnectTo(world, pos.west());
        boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;
        return state.withProperty(UP, Boolean.valueOf(!flag4 || !world.isAirBlock(pos.up()))).withProperty(NORTH, Boolean.valueOf(flag)).withProperty(EAST, Boolean.valueOf(flag1)).withProperty(SOUTH, Boolean.valueOf(flag2)).withProperty(WEST, Boolean.valueOf(flag3));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { UP, NORTH, EAST, WEST, SOUTH, VARIANT });
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.WALLS;
    }

    public enum BlockType implements IStringSerializable
    {
        TIN_1_WALL("tin_1_wall"),
        TIN_2_WALL("tin_2_wall"),
        MOON_STONE_WALL("moon_stone_wall"),
        MOON_DUNGEON_BRICK_WALL("moon_dungeon_brick_wall"),
        MARS_COBBLESTONE_WALL("mars_cobblestone_wall"),
        MARS_DUNGEON_BRICK_WALL("mars_dungeon_brick_wall");

        private String name;

        BlockType(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return this.getName();
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
}