package micdoodle8.mods.galacticraft.planets.deepspace.world;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.deepspace.DeepSpaceBlocks;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
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
        
        if ((pos.getZ() >> 4) % 6 == 0)
        {
            // Axial strut
            for (int y = 7; y < 57; y++)
            {
                this.placeBlock(world, pos.add(6, y, 5), tinDeco, 2);
                this.placeBlock(world, pos.add(6, y, 6), tinDeco, 2);
                this.placeBlock(world, pos.add(6, y, 7), tinDeco, 2);
                this.placeBlock(world, pos.add(6, y, 8), tinDeco, 2);
                this.placeBlock(world, pos.add(6, y, 9), tinDeco, 2);
                this.placeBlock(world, pos.add(6, y, 10), tinDeco, 2);
                
                this.placeBlock(world, pos.add(7, y, 4), tinDeco, 2);
                this.placeBlock(world, pos.add(8, y, 4), tinDeco, 2);
                this.placeBlock(world, pos.add(9, y, 4), tinDeco, 2);

                this.placeBlock(world, pos.add(7, y, 11), tinDeco, 2);
                this.placeBlock(world, pos.add(8, y, 11), tinDeco, 2);
                this.placeBlock(world, pos.add(9, y, 11), tinDeco, 2);

                this.placeBlock(world, pos.add(10, y, 5), tinDeco, 2);
                this.placeBlock(world, pos.add(10, y, 6), tinDeco, 2);
                this.placeBlock(world, pos.add(10, y, 7), tinDeco, 2);
                this.placeBlock(world, pos.add(10, y, 8), tinDeco, 2);
                this.placeBlock(world, pos.add(10, y, 9), tinDeco, 2);
                this.placeBlock(world, pos.add(10, y, 10), tinDeco, 2);
            }
            
            // Base of axial strut
            int y = 7;
            this.placeBlock(world, pos.add(5, y, 5), tinDeco, 2);
            this.placeBlock(world, pos.add(5, y, 6), tinDeco, 2);
            this.placeBlock(world, pos.add(5, y, 7), tinDeco, 2);
            this.placeBlock(world, pos.add(5, y, 8), tinDeco, 2);
            this.placeBlock(world, pos.add(5, y, 9), tinDeco, 2);
            this.placeBlock(world, pos.add(5, y, 10), tinDeco, 2);

            this.placeBlock(world, pos.add(6, y, 4), tinDeco, 2);
            this.placeBlock(world, pos.add(6, y, 11), tinDeco, 2);
            this.placeBlock(world, pos.add(10, y, 4), tinDeco, 2);
            this.placeBlock(world, pos.add(10, y, 11), tinDeco, 2);
            
            this.placeBlock(world, pos.add(7, y, 3), tinDeco, 2);
            this.placeBlock(world, pos.add(8, y, 3), tinDeco, 2);
            this.placeBlock(world, pos.add(9, y, 3), tinDeco, 2);

            this.placeBlock(world, pos.add(7, y, 12), tinDeco, 2);
            this.placeBlock(world, pos.add(8, y, 12), tinDeco, 2);
            this.placeBlock(world, pos.add(9, y, 12), tinDeco, 2);

            this.placeBlock(world, pos.add(11, y, 5), tinDeco, 2);
            this.placeBlock(world, pos.add(11, y, 6), tinDeco, 2);
            this.placeBlock(world, pos.add(11, y, 7), tinDeco, 2);
            this.placeBlock(world, pos.add(11, y, 8), tinDeco, 2);
            this.placeBlock(world, pos.add(11, y, 9), tinDeco, 2);
            this.placeBlock(world, pos.add(11, y, 10), tinDeco, 2);
        }
        
        return true;
    }
    
    private void slice(World world, BlockPos pos, int z)
    {
        IBlockState glass = (pos.getZ() >> 4) % 6 == 0 ? tinDeco : DeepSpaceBlocks.glassProtective.getDefaultState();

        //External platform
        this.placeBlock(world, pos.add(1, 1, z), walkway, 2);
        this.placeBlock(world, pos.add(14, 1, z), walkway, 2);

        //Wall
        this.placeBlock(world, pos.add(2, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(2, 2, z), tinDeco, 2);
        this.placeBlock(world, pos.add(2, 3, z), tinDeco, 2);
        this.placeBlock(world, pos.add(2, 4, z), tinDeco, 2);
        this.placeBlock(world, pos.add(2, 5, z), tinDeco, 2);
        //
        this.placeBlock(world, pos.add(3, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(3, 2, z), tinDeco, 2);
        this.placeBlock(world, pos.add(3, 3, z), tinDeco, 2);
        this.placeBlock(world, pos.add(3, 4, z), tinDeco, 2);
        this.placeBlock(world, pos.add(3, 5, z), tinDeco, 2);
        this.placeBlock(world, pos.add(3, 6, z), tinDeco, 2);
        //
        this.placeBlock(world, pos.add(4, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(4, 2, z), tinDeco, 2);
        if (z % 2 == 0)
            this.placeBlock(world, pos.add(4, 3, z), glassPane, 3);
        this.placeBlock(world, pos.add(4, 4, z), tinDeco, 2);
        this.placeBlock(world, pos.add(4, 5, z), tinDeco, 2);
        this.placeBlock(world, pos.add(4, 6, z), tinDeco, 2);
        //
        this.placeBlock(world, pos.add(5, 0, z), tinDeco, 2);
        this.placeBlock(world, pos.add(5, 1, z), grating, 2);
        this.placeBlock(world, pos.add(5, 5, z), tinDeco, 2);
        this.placeBlock(world, pos.add(5, 6, z), tinDeco, 2);
        //
        this.placeBlock(world, pos.add(6, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(6, 5, z), glass, 2);
        this.placeBlock(world, pos.add(6, 6, z), glass, 2);
        //
        this.placeBlock(world, pos.add(7, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(7, 5, z), glass, 2);
        this.placeBlock(world, pos.add(7, 6, z), glass, 2);
        //
        this.placeBlock(world, pos.add(8, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(8, 5, z), glass, 2);
        this.placeBlock(world, pos.add(8, 6, z), glass, 2);
        //
        this.placeBlock(world, pos.add(9, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(9, 5, z), glass, 2);
        this.placeBlock(world, pos.add(9, 6, z), glass, 2);
        //
        this.placeBlock(world, pos.add(10, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(10, 5, z), glass, 2);
        this.placeBlock(world, pos.add(10, 6, z), glass, 2);
        //
        this.placeBlock(world, pos.add(11, 0, z), tinDeco, 2);
        this.placeBlock(world, pos.add(11, 1, z), grating, 2);
        this.placeBlock(world, pos.add(11, 5, z), tinDeco, 2);
        this.placeBlock(world, pos.add(11, 6, z), tinDeco, 2);
        //
        this.placeBlock(world, pos.add(12, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(12, 2, z), tinDeco, 2);
        if (z % 2 == 0)
            this.placeBlock(world, pos.add(12, 3, z), glassPane, 3);
        this.placeBlock(world, pos.add(12, 4, z), tinDeco, 2);
        this.placeBlock(world, pos.add(12, 5, z), tinDeco, 2);
        this.placeBlock(world, pos.add(12, 6, z), tinDeco, 2);
        //
        this.placeBlock(world, pos.add(13, 1, z), tinDeco, 2);
        this.placeBlock(world, pos.add(13, 2, z), tinDeco, 2);
        this.placeBlock(world, pos.add(13, 3, z), tinDeco, 2);
        this.placeBlock(world, pos.add(13, 4, z), tinDeco, 2);
        this.placeBlock(world, pos.add(13, 5, z), tinDeco, 2);
    }
    
    private void placeBlock(World world, BlockPos pos, IBlockState bs, int flag)
    {
        int z = pos.getZ() % 16;
        if (z < 0) z += 16;
        
        if ((z == 0 || z == 15) && pos.getY() % 8 < 7)
        {
            int meta = (pos.getY() % 8);
            if (z >= 8) meta += 8;
            bs = DeepSpaceBlocks.deepWall.getDefaultState().withProperty(BlockDeepStructure.H, 15 - meta);
        }
        
        world.setBlockState(pos, bs, flag);
    }
}
