package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockAirLockWall extends Block implements IPartialSealableBlock
{
    public static final EnumProperty<EnumAirLockSealConnection> CONNECTION_TYPE = EnumProperty.create("connection", EnumAirLockSealConnection.class);
    protected static final VoxelShape AABB_X = Block.makeCuboidShape(0.25, 0.0, 0.0, 0.75, 1.0, 1.0);
    protected static final VoxelShape AABB_Z = Block.makeCuboidShape(0.0, 0.0, 0.25, 1.0, 1.0, 0.75);
    protected static final VoxelShape AABB_FLAT = Block.makeCuboidShape(0.0, 0.25, 0.0, 1.0, 0.75, 1.0);

    public enum EnumAirLockSealConnection implements IStringSerializable
    {
        X("x"),
        Z("z"),
        FLAT("flat");

        private final String name;

        EnumAirLockSealConnection(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockAirLockWall(Properties builder)
    {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(CONNECTION_TYPE, EnumAirLockSealConnection.X));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch (getConnection(worldIn, pos))
        {
        case X:
            return AABB_X;
        case Z:
            return AABB_Z;
        default:
        case FLAT:
            return AABB_FLAT;
        }
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
    {
        return true;
    }

//    @Override
//    public Item getItem(World world, BlockPos pos)
//    {
//        return null;
//    }

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }

    public static EnumAirLockSealConnection getConnection(IBlockReader worldIn, BlockPos pos)
    {
        EnumAirLockSealConnection connection;

        Block frameID = GCBlocks.airLockFrame;
        Block sealID = GCBlocks.airLockSeal;

        Block idXMin = worldIn.getBlockState(pos.offset(Direction.WEST)).getBlock();
        Block idXMax = worldIn.getBlockState(pos.offset(Direction.WEST)).getBlock();

        if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
        {
            connection = EnumAirLockSealConnection.X;
        }
        else
        {
            int adjacentCount = 0;

            for (Direction dir : Direction.values())
            {
                if (dir.getAxis().isHorizontal())
                {
                    Block blockID = worldIn.getBlockState(pos.offset(dir)).getBlock();

                    if (blockID == GCBlocks.airLockFrame || blockID == GCBlocks.airLockSeal)
                    {
                        adjacentCount++;
                    }
                }
            }

            if (adjacentCount == 4)
            {
                connection = EnumAirLockSealConnection.FLAT;
            }
            else
            {
                connection = EnumAirLockSealConnection.Z;
            }
        }

        return connection;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CONNECTION_TYPE);
    }
}
