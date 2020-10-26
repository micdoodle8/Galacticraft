package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockDungeonBrick extends Block implements ISortable
{
    public BlockDungeonBrick(Properties properties)
    {
        super(properties);
    }

    @Override
    public MaterialColor getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return MaterialColor.RED_TERRACOTTA;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.BRICKS;
    }
}
