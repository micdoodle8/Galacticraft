package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import net.minecraft.block.Block;
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
        int i1 = pos.getX();
        int j1 = pos.getY();
        int k1 = pos.getZ();

        if (par1World.isAirBlock(new BlockPos(i1, j1, k1)) && (!par1World.provider.getHasNoSky() || j1 < 127) && this.eggBlock.canPlaceBlockAt(par1World, new BlockPos(i1, j1, k1)))
        {
            par1World.setBlockState(new BlockPos(i1, j1, k1), this.eggBlock.getStateFromMeta(par2Random.nextInt(3)), 2);
        }

        return true;
    }
}
