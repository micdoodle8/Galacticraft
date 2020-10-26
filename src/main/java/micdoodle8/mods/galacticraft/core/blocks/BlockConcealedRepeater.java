package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockConcealedRepeater extends RepeaterBlock implements ISortable
{
    protected static final VoxelShape CUBE_AABB = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockConcealedRepeater(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return CUBE_AABB;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.DECORATION;
    }
}
