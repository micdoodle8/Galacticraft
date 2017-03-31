package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
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

        if (!par1World.isBlockLoaded(newPos)) return false;

        if (par1World.isAirBlock(newPos) && (!par1World.provider.hasNoSky() || j1 < 127) && this.eggBlock.canPlaceBlockAt(par1World, newPos))
        {
            par1World.setBlockState(newPos, this.eggBlock.getStateFromMeta(par2Random.nextInt(3)), 2);
        }

        return true;
    }
}
