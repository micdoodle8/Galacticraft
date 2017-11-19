package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;

public class WorldGenEggs extends WorldGenerator
{
    private Block eggBlock;

    public WorldGenEggs(Block egg)
    {
        this.eggBlock = egg;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        BlockPos newPos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

        if (!world.isBlockLoaded(newPos))
        {
            return false;
        }
        if (world.isAirBlock(newPos) && newPos.getY() < 127 && world.getBlockState(newPos.down()).getBlock() == MarsBlocks.marsBlock && world.getBlockState(newPos.down()).getValue(BlockBasicMars.BASIC_TYPE) == BlockBasicMars.EnumBlockBasic.SURFACE)
        {
            world.setBlockState(newPos, this.eggBlock.getStateFromMeta(rand.nextInt(3)), 2);
        }
        return true;
    }
}
