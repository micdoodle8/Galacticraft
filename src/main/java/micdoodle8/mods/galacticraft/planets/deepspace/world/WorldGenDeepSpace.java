package micdoodle8.mods.galacticraft.planets.deepspace.world;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenDeepSpace extends WorldGenerator
{
    IBlockState tinDeco = GCBlocks.basicBlock.getStateFromMeta(4);
    IBlockState glassPane = Blocks.GLASS_PANE.getDefaultState();
    IBlockState walkway = AsteroidBlocks.blockWalkway.getDefaultState();
    IBlockState grating = GCBlocks.grating.getDefaultState();
    IBlockState b1;
    IBlockState b2;
    IBlockState b3;
    IBlockState b4;
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        for (int z = 0; z < 16; z++)
        {
            slice(world, pos, z);
        }
        
        return true;
    }
    
    private void slice(World world, BlockPos pos, int z)
    {
        //External platform
        world.setBlockState(pos.add(1, 1, z), walkway, 2);
        world.setBlockState(pos.add(14, 1, z), walkway, 2);

        //Wall
        world.setBlockState(pos.add(2, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(2, 2, z), tinDeco, 2);
        world.setBlockState(pos.add(2, 3, z), tinDeco, 2);
        world.setBlockState(pos.add(2, 4, z), tinDeco, 2);
        world.setBlockState(pos.add(2, 5, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(3, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(3, 2, z), tinDeco, 2);
        world.setBlockState(pos.add(3, 3, z), tinDeco, 2);
        world.setBlockState(pos.add(3, 4, z), tinDeco, 2);
        world.setBlockState(pos.add(3, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(3, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(4, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(4, 2, z), tinDeco, 2);
        if (z % 2 == 0)
        world.setBlockState(pos.add(4, 3, z), glassPane, 3);
        world.setBlockState(pos.add(4, 4, z), tinDeco, 2);
        world.setBlockState(pos.add(4, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(4, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(5, 0, z), tinDeco, 2);
        world.setBlockState(pos.add(5, 1, z), grating, 2);
        world.setBlockState(pos.add(5, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(5, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(6, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(6, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(6, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(7, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(7, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(7, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(8, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(8, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(8, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(9, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(9, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(9, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(10, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(10, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(10, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(11, 0, z), tinDeco, 2);
        world.setBlockState(pos.add(11, 1, z), grating, 2);
        world.setBlockState(pos.add(11, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(11, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(12, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(12, 2, z), tinDeco, 2);
        if (z % 2 == 0)
        world.setBlockState(pos.add(12, 3, z), glassPane, 3);
        world.setBlockState(pos.add(12, 4, z), tinDeco, 2);
        world.setBlockState(pos.add(12, 5, z), tinDeco, 2);
        world.setBlockState(pos.add(12, 6, z), tinDeco, 2);
        //
        world.setBlockState(pos.add(13, 1, z), tinDeco, 2);
        world.setBlockState(pos.add(13, 2, z), tinDeco, 2);
        world.setBlockState(pos.add(13, 3, z), tinDeco, 2);
        world.setBlockState(pos.add(13, 4, z), tinDeco, 2);
        world.setBlockState(pos.add(13, 5, z), tinDeco, 2);
    }
}
