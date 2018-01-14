package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenEggs extends WorldGenerator
{
    private Block eggBlock;

    public WorldGenEggs(Block egg)
    {
        this.eggBlock = egg;
    }

    @Override
    public boolean generate(World par1World, Random par2Random, BlockPos pos)
    {
        int i1 = pos.getX() + par2Random.nextInt(8) - par2Random.nextInt(8);
        int j1 = pos.getY() + par2Random.nextInt(4) - par2Random.nextInt(4);
        int k1 = pos.getZ() + par2Random.nextInt(8) - par2Random.nextInt(8);
        BlockPos newPos = new BlockPos(i1, j1, k1);

        if (!par1World.isBlockLoaded(newPos.add(1, 0, 1))) return false;

        if (par1World.isAirBlock(newPos) && (j1 < 127 || !par1World.provider.getHasNoSky()))
        {
            IBlockState below = par1World.getBlockState(newPos.down()); 
            if (below.getBlock() == MarsBlocks.marsBlock && below.getValue(BlockBasicMars.BASIC_TYPE) == BlockBasicMars.EnumBlockBasic.SURFACE)
            {
                par1World.setBlockState(newPos, this.eggBlock.getStateFromMeta(par2Random.nextInt(3)), 2);
            }
        }

        return true;
    }
}
