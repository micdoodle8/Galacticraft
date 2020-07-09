package micdoodle8.mods.galacticraft.planets.venus.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockDungeonBrick extends Block
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
}
