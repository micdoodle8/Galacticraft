package micdoodle8.mods.galacticraft.planets.deepspace.world;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.deepspace.DeepSpaceBlocks;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockBasicSpace.EnumBlockSpace;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockFlooring.EnumBlockFlooring;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockInterior.EnumBlockInterior;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenDeepSpace extends WorldGenerator
{
    private IBlockState exteriorBasic = EnumBlockSpace.DECO_1.getBlock();
    private IBlockState solid = EnumBlockSpace.BASE_METAL.getBlock();
    private IBlockState interiorWall = EnumBlockInterior.DARK_GREY.getBlock();
    private IBlockState interiorFloor = EnumBlockFlooring.GREY.getBlock();
    private IBlockState interiorFloor2 = EnumBlockFlooring.GREY_HANDLE.getBlock();
    private IBlockState glassPane = Blocks.GLASS_PANE.getDefaultState();
    private IBlockState walkway = AsteroidBlocks.blockWalkway.getDefaultState();
    private IBlockState grating = GCBlocks.grating.getDefaultState();
    private IBlockState b1;
    private IBlockState b2;
    private IBlockState b3;
    private IBlockState b4;
    private Random random;
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        this.random = rand;
        BlockPos floor = pos.add(0, -5, 0);
        for (int z = 0; z < 16; z++)
        {
            slice(world, floor, z);
        }
        
        if ((pos.getZ() >> 4) % 6 == 0)
        {
            // Axial strut
            for (int y = 2; y < 57; y++)
            {
                this.placeBlock(world, pos.add(6, y, 5), exteriorBasic, 2);
                this.placeBlock(world, pos.add(6, y, 6), exteriorBasic, 2);
                this.placeBlock(world, pos.add(6, y, 7), exteriorBasic, 2);
                this.placeBlock(world, pos.add(6, y, 8), exteriorBasic, 2);
                this.placeBlock(world, pos.add(6, y, 9), exteriorBasic, 2);
                this.placeBlock(world, pos.add(6, y, 10), exteriorBasic, 2);
                
                this.placeBlock(world, pos.add(7, y, 4), exteriorBasic, 2);
                this.placeBlock(world, pos.add(8, y, 4), exteriorBasic, 2);
                this.placeBlock(world, pos.add(9, y, 4), exteriorBasic, 2);

                this.placeBlock(world, pos.add(7, y, 11), exteriorBasic, 2);
                this.placeBlock(world, pos.add(8, y, 11), exteriorBasic, 2);
                this.placeBlock(world, pos.add(9, y, 11), exteriorBasic, 2);

                this.placeBlock(world, pos.add(10, y, 5), exteriorBasic, 2);
                this.placeBlock(world, pos.add(10, y, 6), exteriorBasic, 2);
                this.placeBlock(world, pos.add(10, y, 7), exteriorBasic, 2);
                this.placeBlock(world, pos.add(10, y, 8), exteriorBasic, 2);
                this.placeBlock(world, pos.add(10, y, 9), exteriorBasic, 2);
                this.placeBlock(world, pos.add(10, y, 10), exteriorBasic, 2);
            }
            
            // Base of axial strut
            int y = 0;
            this.placeBlock(world, pos.add(5, y, 5), exteriorBasic, 2);
            this.placeBlock(world, pos.add(5, y, 6), exteriorBasic, 2);
            this.placeBlock(world, pos.add(5, y, 7), exteriorBasic, 2);
            this.placeBlock(world, pos.add(5, y, 8), exteriorBasic, 2);
            this.placeBlock(world, pos.add(5, y, 9), exteriorBasic, 2);
            this.placeBlock(world, pos.add(5, y, 10), exteriorBasic, 2);

            this.placeBlock(world, pos.add(6, y, 4), exteriorBasic, 2);
            this.placeBlock(world, pos.add(6, y, 11), exteriorBasic, 2);
            this.placeBlock(world, pos.add(10, y, 4), exteriorBasic, 2);
            this.placeBlock(world, pos.add(10, y, 11), exteriorBasic, 2);
            
            this.placeBlock(world, pos.add(7, y, 3), exteriorBasic, 2);
            this.placeBlock(world, pos.add(8, y, 3), exteriorBasic, 2);
            this.placeBlock(world, pos.add(9, y, 3), exteriorBasic, 2);

            this.placeBlock(world, pos.add(7, y, 12), exteriorBasic, 2);
            this.placeBlock(world, pos.add(8, y, 12), exteriorBasic, 2);
            this.placeBlock(world, pos.add(9, y, 12), exteriorBasic, 2);

            this.placeBlock(world, pos.add(11, y, 5), exteriorBasic, 2);
            this.placeBlock(world, pos.add(11, y, 6), exteriorBasic, 2);
            this.placeBlock(world, pos.add(11, y, 7), exteriorBasic, 2);
            this.placeBlock(world, pos.add(11, y, 8), exteriorBasic, 2);
            this.placeBlock(world, pos.add(11, y, 9), exteriorBasic, 2);
            this.placeBlock(world, pos.add(11, y, 10), exteriorBasic, 2);
        }
        
        return true;
    }
    
    private void slice(World world, BlockPos pos, int z)
    {
        IBlockState glass = (pos.getZ() >> 4) % 6 == 0 ? exteriorBasic : DeepSpaceBlocks.glassProtective.getDefaultState();

        //External platform
        this.placeBlock(world, pos.add(1, 1, z), walkway, 2);
        this.placeBlock(world, pos.add(14, 1, z), walkway, 2);

        //Wall
        this.placeExteriorBlock(world, pos.add(2, 1, z));
        this.placeExteriorBlock(world, pos.add(2, 2, z));
        this.placeExteriorBlock(world, pos.add(2, 3, z));
        this.placeExteriorBlock(world, pos.add(2, 4, z));
        this.placeExteriorBlock(world, pos.add(2, 5, z));
        //
        this.placeExteriorBlock(world, pos.add(3, 1, z));
        this.placeBlock(world, pos.add(3, 2, z), solid, 2);
        this.placeBlock(world, pos.add(3, 3, z), solid, 2);
        this.placeBlock(world, pos.add(3, 4, z), solid, 2);
        this.placeBlock(world, pos.add(3, 5, z), solid, 2);
        this.placeExteriorBlock(world, pos.add(3, 6, z));
        //
        this.placeExteriorBlock(world, pos.add(4, 0, z));
        this.placeExteriorBlock(world, pos.add(4, 1, z));
        this.placeBlock(world, pos.add(4, 2, z), interiorWall, 2);
        if (z % 2 == 0 || z % 16 == 15 || z % 16 == -1)
            this.placeBlock(world, pos.add(4, 3, z), glassPane, 3);
        this.placeBlock(world, pos.add(4, 4, z), interiorWall, 2);
        this.placeBlock(world, pos.add(4, 5, z), solid, 2);
        this.placeExteriorBlock(world, pos.add(4, 6, z));
        //
        this.placeExteriorBlock(world, pos.add(5, -1, z));
        this.placeBlock(world, pos.add(5, 1, z), grating, 2);
        this.placeBlock(world, pos.add(5, 5, z), solid, 2);
        this.placeExteriorBlock(world, pos.add(5, 6, z));
        //
        this.placeExteriorBlock(world, pos.add(6, -1, z));
        this.placeBlock(world, pos.add(6, 1, z), interiorFloor, 2);
        this.placeBlock(world, pos.add(6, 5, z), glass, 2);
        this.placeBlock(world, pos.add(6, 6, z), glass, 2);
        //
        this.placeExteriorBlock(world, pos.add(7, -1, z));
        this.placeBlock(world, pos.add(7, 1, z), interiorFloor, 2);
        this.placeBlock(world, pos.add(7, 5, z), glass, 2);
        this.placeBlock(world, pos.add(7, 6, z), glass, 2);
        //
        this.placeExteriorBlock(world, pos.add(8, -1, z));
        this.placeBlock(world, pos.add(8, 1, z), interiorFloor2, 2);
        this.placeBlock(world, pos.add(8, 5, z), glass, 2);
        this.placeBlock(world, pos.add(8, 6, z), glass, 2);
        //
        this.placeExteriorBlock(world, pos.add(9, -1, z));
        this.placeBlock(world, pos.add(9, 1, z), interiorFloor, 2);
        this.placeBlock(world, pos.add(9, 5, z), glass, 2);
        this.placeBlock(world, pos.add(9, 6, z), glass, 2);
        //
        this.placeExteriorBlock(world, pos.add(10, -1, z));
        this.placeBlock(world, pos.add(10, 1, z), interiorFloor, 2);
        this.placeBlock(world, pos.add(10, 5, z), glass, 2);
        this.placeBlock(world, pos.add(10, 6, z), glass, 2);
        //
        this.placeExteriorBlock(world, pos.add(11, -1, z));
        this.placeBlock(world, pos.add(11, 1, z), grating, 2);
        this.placeBlock(world, pos.add(11, 5, z), solid, 2);
        this.placeExteriorBlock(world, pos.add(11, 6, z));
        //
        this.placeExteriorBlock(world, pos.add(12, 0, z));
        this.placeExteriorBlock(world, pos.add(12, 1, z));
        this.placeBlock(world, pos.add(12, 2, z), interiorWall, 2);
        if (z % 2 == 0 || z % 16 == 15 || z % 16 == -1)
            this.placeBlock(world, pos.add(12, 3, z), glassPane, 3);
        this.placeBlock(world, pos.add(12, 4, z), interiorWall, 2);
        this.placeBlock(world, pos.add(12, 5, z), solid, 2);
        this.placeExteriorBlock(world, pos.add(12, 6, z));
        //
        this.placeExteriorBlock(world, pos.add(13, 1, z));
        this.placeBlock(world, pos.add(13, 2, z), solid, 2);
        this.placeBlock(world, pos.add(13, 3, z), solid, 2);
        this.placeBlock(world, pos.add(13, 4, z), solid, 2);
        this.placeBlock(world, pos.add(13, 5, z), solid, 2);
        this.placeExteriorBlock(world, pos.add(13, 6, z));
        //
        this.placeExteriorBlock(world, pos.add(14, 1, z));
        this.placeExteriorBlock(world, pos.add(14, 2, z));
        this.placeExteriorBlock(world, pos.add(14, 3, z));
        this.placeExteriorBlock(world, pos.add(14, 4, z));
        this.placeExteriorBlock(world, pos.add(14, 5, z));
    }
    
    private void placeExteriorBlock(World world, BlockPos pos)
    {
        int r = this.random.nextInt(12);
        if (r > 6)
            this.placeBlock(world, pos, DeepSpaceBlocks.surface.getStateFromMeta(r - 7), 2);
        else
            this.placeBlock(world, pos, DeepSpaceBlocks.spaceBasic.getStateFromMeta(r), 2);
    }
    
    private void placeBlock(World world, BlockPos pos, IBlockState bs, int flag)
    {
        int z = pos.getZ() % 16;
        if (z < 0) z += 16;
        
        int y = pos.getY();
        if ((z == 0 || z == 15) && y % 8 < 7 && y >= 64)
        {
            int meta = (y % 8);
            if (z >= 8) meta += 8;
            bs = DeepSpaceBlocks.deepWall.getDefaultState().withProperty(BlockDeepStructure.H, 15 - meta);
        }
        
        world.setBlockState(pos, bs, flag);
    }
}
