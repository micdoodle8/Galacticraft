package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_HANGAR;
import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures.CBASE_PLATE;

public class BaseHangar extends SizedPiece
{
    public static final int HANGARWIDTH = 26;
    public static final int HANGARLENGTH = 42;
    public static final int HANGARHEIGHT = 15;

    public BaseHangar(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CBASE_HANGAR, nbt);
    }

    public BaseHangar(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosZ, Direction direction)
    {
        super(CBASE_HANGAR, configuration, HANGARWIDTH, HANGARHEIGHT, HANGARLENGTH, direction);
        if (direction.getAxis() == Direction.Axis.X)
        {
            int w = this.sizeX;
            this.sizeX = this.sizeZ;
            this.sizeZ = w;
            this.setCoordBaseMode(direction.getOpposite()); //Maybe a bug in vanilla here?
        }
        else
        {
            this.setCoordBaseMode(direction);
        }
        int yPos = configuration.getYPosition();
        this.boundingBox = new MutableBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
        //TODO check save nbt
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        BlockState blockAir = Blocks.AIR.getDefaultState();
        BlockState blockPlain = GCBlocks.decoBlock1.getDefaultState();
        BlockState blockPattern = GCBlocks.decoBlock0.getDefaultState();
        BlockState blockGrid = AsteroidBlocks.blockWalkway.getDefaultState();
//        BlockState blockSlab = GCBlocks.slabGCHalf.getStateFromMeta(1);
//        BlockState upsideSlab = GCBlocks.slabGCHalf.getStateFromMeta(9);
//        BlockState blockWall = GCBlocks.wallGC.getStateFromMeta(1);
//        BlockState decoWall = GCBlocks.wallGC.getStateFromMeta(0);
//        BlockState moonWall = GCBlocks.wallGC.getStateFromMeta(2); TODO Slabs and walls
        BlockState blockBars = Blocks.IRON_BARS.getDefaultState();
        BlockState blockDesh = MarsBlocks.deshBlock.getDefaultState();
        BlockState blockRedstone = GCBlocks.concealedRedstone.getDefaultState();
        Block blockStair = GCBlocks.tinStairs2;
        Block arcLamp = GCBlocks.arcLamp;
        int stairmeta = 1;
        int stairmetaB = 2;
        int lampmeta = 3;
        int lampmeta2 = 5;
        if (direction == Direction.SOUTH)
        {
            stairmeta ^= 1;
            stairmetaB ^= 1;
            lampmeta ^= 1;
            lampmeta2 ^= 1;
        }
        else if (direction == Direction.EAST)
        {
            stairmeta ^= 2;
            stairmetaB ^= 3;
            lampmeta = 4;
            lampmeta2 = 3;
        }
        else if (direction == Direction.WEST)
        {
            stairmeta ^= 3;
            stairmetaB ^= 2;
            lampmeta = 5;
            lampmeta2 = 2;
        }


        int maxX = HANGARWIDTH;
        int maxZ = HANGARLENGTH;
        int maxY = this.sizeY;
        int midPoint = HANGARLENGTH - 22;

        //AIR
        for (int zz = HANGARLENGTH; zz >= 0; zz--)
        {
            for (int xx = 0; xx <= maxX; xx++)
            {
                for (int y = 0; y <= maxY; y++)
                {
                    if (y <= 1 && (xx == 0 || xx == maxX))
                    {
                        continue;
                    }
                    if (y == maxY && (xx < 4 || xx > maxX - 4))
                    {
                        continue;
                    }
                    if (y == maxY - 1 && (xx < 2 || xx > maxX - 2))
                    {
                        continue;
                    }
                    this.setBlockState(worldIn, blockAir, xx, y, zz, mutableBoundingBoxIn);
                }
            }
        }

        //endwall
//        for (int y = 7; y <= 8; y++)
//        {
//            this.setBlockState(worldIn, blockWall, 1, y, HANGARLENGTH, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockWall, 2, y, HANGARLENGTH, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockWall, 3, y, HANGARLENGTH, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockWall, 23, y, HANGARLENGTH, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockWall, 24, y, HANGARLENGTH, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockWall, 25, y, HANGARLENGTH, mutableBoundingBoxIn);
//
//            for (int x = 5; x < 22; x++)
//            {
//                this.setBlockState(worldIn, this.configuration.getWallBlock(), x, y, HANGARLENGTH, mutableBoundingBoxIn);
//                if (y == 7 && (x < 9 || x > 17))
//                    this.setBlockState(worldIn, this.configuration.getWallBlock(), x, maxY, HANGARLENGTH, mutableBoundingBoxIn);
//            }
//        } TODO Slabs and walls

        for (int y = 7; y <= maxY; y++)
        {
            this.setBlockState(worldIn, blockPlain, 4, y, HANGARLENGTH, mutableBoundingBoxIn);
            this.setBlockState(worldIn, blockPlain, 22, y, HANGARLENGTH, mutableBoundingBoxIn);
        }
        for (int y = 9; y <= maxY; y++)
        {
            this.setBlockState(worldIn, this.configuration.getWallBlock(), 6, y, HANGARLENGTH, mutableBoundingBoxIn);
            this.setBlockState(worldIn, this.configuration.getWallBlock(), 20, y, HANGARLENGTH, mutableBoundingBoxIn);
        }

//        for (int xx = 0; xx <= 3; xx++)
//            this.setBlockState(worldIn, moonWall, xx, 1, HANGARLENGTH, mutableBoundingBoxIn);
//        for (int xx = maxX - 3; xx <= maxX; xx++)
//            this.setBlockState(worldIn, moonWall, xx, 1, HANGARLENGTH, mutableBoundingBoxIn);
//
//        this.setBlockState(worldIn, blockDesh, 9, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockRedstone, 11, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockPattern, 13, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockRedstone, 15, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockDesh, 17, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, upsideSlab, 10, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, upsideSlab, 12, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, upsideSlab, 14, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, upsideSlab, 16, maxY, HANGARLENGTH - 1, mutableBoundingBoxIn);
//
//        //Ceiling struts
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB + 4), 4, maxY - 1, HANGARLENGTH - 3, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB + 4), 4, maxY - 2, HANGARLENGTH - 2, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB + 4), 4, maxY - 3, HANGARLENGTH - 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB + 4), 22, maxY - 1, HANGARLENGTH - 3, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB + 4), 22, maxY - 2, HANGARLENGTH - 2, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB + 4), 22, maxY - 3, HANGARLENGTH - 1, mutableBoundingBoxIn);
        // TODO Slabs, stairs and walls ^^^

//        this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta), 5, maxY - 3, HANGARLENGTH, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta), 5, maxY - 6, HANGARLENGTH, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta), 21, maxY - 3, HANGARLENGTH, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta), 21, maxY - 6, HANGARLENGTH, mutableBoundingBoxIn);

        //FIRST SECTION
        for (int zz = HANGARLENGTH; zz > HANGARLENGTH - 5; zz--)
        {
//            for (int xx = 0; xx <= maxX; xx+= maxX)
//            {
//                this.setBlockState(worldIn, moonWall, xx, 1, zz, mutableBoundingBoxIn);
//                for (int y = 2; y < 7; y++)
//                {
//                    this.setBlockState(worldIn, blockAir, xx, y, zz, mutableBoundingBoxIn);
//                }
//                for (int y = 9; y < maxY - 1; y++)
//                {
//                    this.setBlockState(worldIn, blockAir, xx, y, zz, mutableBoundingBoxIn);
//                }
//                this.setBlockState(worldIn, blockWall, xx, 7, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockWall, xx, 8, zz, mutableBoundingBoxIn);
//            } TODO Slabs, stairs and walls ^^^

            if (zz % 3 == 1)
            {
                this.setBlockState(worldIn, blockPlain, 4, maxY, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockPlain, 22, maxY, zz, mutableBoundingBoxIn);
            }
            else
            {
                this.setBlockState(worldIn, blockPattern, 4, maxY, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockPattern, 22, maxY, zz, mutableBoundingBoxIn);
            }

            //Top middle
            if (zz < HANGARLENGTH - 1)
            {
                this.setBlockState(worldIn, blockGrid, 9, maxY, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockRedstone, 11, maxY, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockGrid, 13, maxY, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockRedstone, 15, maxY, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockGrid, 17, maxY, zz, mutableBoundingBoxIn);
            }

            //Floor
            this.setBlockState(worldIn, blockPattern, 7, 0, zz, mutableBoundingBoxIn);
            this.setBlockState(worldIn, blockPattern, 9, 0, zz, mutableBoundingBoxIn);
            this.setBlockState(worldIn, blockPattern, 19, 0, zz, mutableBoundingBoxIn);
            this.setBlockState(worldIn, blockPattern, 17, 0, zz, mutableBoundingBoxIn);
            this.setBlockState(worldIn, blockGrid, 8, 0, zz, mutableBoundingBoxIn);
            this.setBlockState(worldIn, blockGrid, 18, 0, zz, mutableBoundingBoxIn);

            if (zz > HANGARLENGTH - 5)
            {
                this.setBlockState(worldIn, blockPlain, 10, 0, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockPlain, 16, 0, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockPlain, 11, 0, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockPlain, 15, 0, zz, mutableBoundingBoxIn);
            }

            if (zz > HANGARLENGTH - 4)
            {
                this.setBlockState(worldIn, blockPlain, 12, 0, zz, mutableBoundingBoxIn);
                this.setBlockState(worldIn, blockPlain, 14, 0, zz, mutableBoundingBoxIn);
            }
            if (zz > HANGARLENGTH - 3)
            {
                this.setBlockState(worldIn, blockPlain, 13, 0, zz, mutableBoundingBoxIn);
            }
        }

        //Floor end
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB), 10, 0, HANGARLENGTH - 6, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB), 16, 0, HANGARLENGTH - 6, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB), 11, 0, HANGARLENGTH - 5, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB), 15, 0, HANGARLENGTH - 5, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB), 12, 0, HANGARLENGTH - 4, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB), 14, 0, HANGARLENGTH - 4, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmetaB), 13, 0, HANGARLENGTH - 3, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockPlain, 10, 0, HANGARLENGTH - 5, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockPlain, 16, 0, HANGARLENGTH - 5, mutableBoundingBoxIn);
//
//        //Join first and second sections
//        this.setBlockState(worldIn, moonWall, 0, 1, HANGARLENGTH - 5, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, moonWall, maxX, 1, HANGARLENGTH - 5, mutableBoundingBoxIn);
//
//        this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta2 ^ 1), 12, maxY, HANGARLENGTH - 3, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta2), 14, maxY, HANGARLENGTH - 3, mutableBoundingBoxIn);
//
//        //SECOND SECTION
//        for (int zz = HANGARLENGTH - 5; zz > midPoint; zz--)
//        {
//            //Top sides
//            if (zz % 3 == 1)
//            {
//                this.setBlockState(worldIn, blockSlab, 3, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPlain, 4, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPlain, 22, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockSlab, 23, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmeta + 4), 2, maxY - 1, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmeta + 4), 1, maxY - 2, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockStair.getStateFromMeta((stairmeta ^ 1) + 4), 24, maxY - 1, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockStair.getStateFromMeta((stairmeta ^ 1) + 4), 25, maxY - 2, zz, mutableBoundingBoxIn);
//                if (zz != HANGARLENGTH - 11)
//                {
//                    this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmeta), 1, 1, zz, mutableBoundingBoxIn);
//                    this.setBlockState(worldIn, blockStair.getStateFromMeta(stairmeta ^ 1), 25, 1, zz, mutableBoundingBoxIn);
//                    floorStrut(worldIn, blockWall, decoWall, zz, mutableBoundingBoxIn);
//                }
//            }
//            else
//            {
//                this.setBlockState(worldIn, blockPattern, 22, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPattern, 4, maxY, zz, mutableBoundingBoxIn);
//            }
//
//            //Top middle
//            if (zz == midPoint + 1)
//            {
//                this.setBlockState(worldIn, blockDesh, 9, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPattern, 11, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockDesh, 13, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPattern, 15, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockDesh, 17, maxY, zz, mutableBoundingBoxIn);
//            }
//            else
//            {
//                this.setBlockState(worldIn, blockGrid, 9, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockGrid, 13, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockGrid, 17, maxY, zz, mutableBoundingBoxIn);
//                if (zz == HANGARLENGTH - 12)
//                {
//                    BlockState repeater = GCBlocks.concealedRepeater_Powered.getStateFromMeta(direction.getAxis() == Direction.Axis.Z ? 0 : 2);  //Rotation will be taken care of by getRotation() but seems to be bugged in vanilla
//                    this.setBlockState(worldIn, repeater, 11, maxY, zz, mutableBoundingBoxIn);
//                    this.setBlockState(worldIn, repeater, 15, maxY, zz, mutableBoundingBoxIn);
//                }
//                else
//                {
//                    this.setBlockState(worldIn, blockRedstone, 11, maxY, zz, mutableBoundingBoxIn);
//                    this.setBlockState(worldIn, blockRedstone, 15, maxY, zz, mutableBoundingBoxIn);
//                }
//            }
//            if ((zz - midPoint - 1) % 6 == 0)
//            {
//                this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta2), 10, maxY, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, arcLamp.getStateFromMeta(lampmeta2 ^ 1), 16, maxY, zz, mutableBoundingBoxIn);
//            }
//
//            //Walls
//            for (int xx = 0; xx <= maxX; xx+= maxX)
//            {
//                this.setBlockState(worldIn, blockPlain, xx, maxY - 2, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPlain, xx, maxY - 4, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPlain, xx, 2, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockPlain, xx, 4, zz, mutableBoundingBoxIn);
//                if (zz % 3 == 1)
//                {
//                    this.setBlockState(worldIn, blockPattern, xx, maxY - 3, zz, mutableBoundingBoxIn);
//                    this.setBlockState(worldIn, blockPlain, xx, 3, zz, mutableBoundingBoxIn);
//
//                    this.setBlockState(worldIn, blockWall, xx, 5, zz, mutableBoundingBoxIn);
//                    this.setBlockState(worldIn, blockWall, xx, 6, zz, mutableBoundingBoxIn);
//                    this.setBlockState(worldIn, blockWall, xx, 9, zz, mutableBoundingBoxIn);
//                    this.setBlockState(worldIn, blockWall, xx, 10, zz, mutableBoundingBoxIn);
//                }
//                this.setBlockState(worldIn, blockWall, xx, 7, zz, mutableBoundingBoxIn);
//                this.setBlockState(worldIn, blockWall, xx, 8, zz, mutableBoundingBoxIn);
//            }
//
//            //Floor
//            BlockState blockBeam = (zz % 2 == 0) ? blockPattern : blockPlain;
//            this.setBlockState(worldIn, blockBeam, 7, 0, zz, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockBeam, 9, 0, zz, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockBeam, 19, 0, zz, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockBeam, 17, 0, zz, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockGrid, 8, 0, zz, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockGrid, 18, 0, zz, mutableBoundingBoxIn);
//        }
//
//        //THIRD SECTION
//        int zz = midPoint;
//
//        //Top sides
//        extrudeTrioOff(worldIn, randomIn, 2, blockPlain, blockPattern, 4, maxY, zz, mutableBoundingBoxIn);
//        extrudeTrioOff(worldIn, randomIn, 2, blockPlain, blockPattern, 22, maxY, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 2, blockGrid, 23, maxY, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 2, blockGrid, 3, maxY, zz, mutableBoundingBoxIn);
//
//        //Walls
//        for (int xx = 0; xx <= maxX; xx+= maxX)
//        {
//            extrudeTrio(worldIn, randomIn, 1, blockPlain, blockPattern, xx, maxY - 2, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 1, blockPlain, blockPattern, xx, maxY - 4, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 1, blockPlain, blockPattern, xx, 2, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 1, blockPlain, blockPattern, xx, 4, zz, mutableBoundingBoxIn);
//
//            extrudeTrio(worldIn, randomIn, 0, blockBars, blockAir, xx, 5, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 0, blockBars, blockAir, xx, 6, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 0, blockBars, blockAir, xx, 7, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 0, blockBars, blockAir, xx, 8, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 0, blockBars, blockAir, xx, 9, zz, mutableBoundingBoxIn);
//            extrudeTrio(worldIn, randomIn, 0, blockBars, blockAir, xx, 10, zz, mutableBoundingBoxIn);
//            extrude(worldIn, randomIn, 2, blockBars, xx, maxY - 3, zz, mutableBoundingBoxIn);
//            extrude(worldIn, randomIn, 2, blockBars, xx, 3, zz, mutableBoundingBoxIn);
//        }
//
//        //Floor
//        extrude(worldIn, randomIn, 2, blockGrid, 1, 1, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 2, blockGrid, 2, 1, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 2, blockGrid, 24, 1, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 2, blockGrid, 25, 1, zz, mutableBoundingBoxIn);
//        extrudeDuo(worldIn, randomIn, 3, blockPlain, blockPattern, 7, 0, zz, mutableBoundingBoxIn);
//        extrudeDuo(worldIn, randomIn, 3, blockPlain, blockPattern, 9, 0, zz, mutableBoundingBoxIn);
//        extrudeDuo(worldIn, randomIn, 3, blockPlain, blockPattern, 19, 0, zz, mutableBoundingBoxIn);
//        extrudeDuo(worldIn, randomIn, 3, blockPlain, blockPattern, 17, 0, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 3, blockGrid, 8, 0, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 3, blockGrid, 18, 0, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 3, blockGrid, 5, 0, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 3, blockGrid, 21, 0, zz, mutableBoundingBoxIn);
//
//        this.setBlockState(worldIn, moonWall, 10, 0, zz, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, moonWall, 11, 0, zz, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, moonWall, 12, 0, zz, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, moonWall, 13, 0, zz, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, moonWall, 14, 0, zz, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, moonWall, 15, 0, zz, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, moonWall, 16, 0, zz, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 2, blockGrid, 10, 0, zz - 1, mutableBoundingBoxIn);
//        extrude(worldIn, randomIn, 2, blockGrid, 16, 0, zz - 1, mutableBoundingBoxIn);
//
//        //WHERE SECOND AND THIRD SECTION MEET
//        //Extend the mid-LogicalSide walls a little way past the midPoint
//        for (int xx = 0; xx <= maxX; xx+= maxX)
//        {
//            this.setBlockState(worldIn, blockWall, xx, 7, midPoint, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockWall, xx, 8, midPoint, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockBars, xx, 3, midPoint + 1, mutableBoundingBoxIn);
//            this.setBlockState(worldIn, blockBars, xx, maxY - 3, midPoint + 1, mutableBoundingBoxIn);
//        }
//        //Extend the walkways a little way also
//        this.setBlockState(worldIn, blockGrid, 1, 1, midPoint + 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockGrid, 2, 1, midPoint + 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockGrid, 5, 0, midPoint + 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockGrid, 21, 0, midPoint + 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockGrid, 24, 1, midPoint + 1, mutableBoundingBoxIn);
//        this.setBlockState(worldIn, blockGrid, 25, 1, midPoint + 1, mutableBoundingBoxIn); TODO Hangar gen

        return true;
    }

    private void floorStrut(World worldIn, BlockState blockWall, BlockState decoWall, int zz, MutableBoundingBox mutableBoundingBoxIn)
    {
        this.setBlockState(worldIn, blockWall, 1, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, decoWall, 2, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, blockWall, 3, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, decoWall, 4, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, blockWall, 5, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, decoWall, 6, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, decoWall, 20, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, blockWall, 21, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, decoWall, 22, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, blockWall, 23, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, decoWall, 24, 0, zz, mutableBoundingBoxIn);
        this.setBlockState(worldIn, blockWall, 25, 0, zz, mutableBoundingBoxIn);
    }

    private void extrude(World worldIn, Random rand, int solid, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox boundingBoxIn)
    {
        for (int zz = z; zz >= rand.nextInt(4 * (4 - solid)); zz--)
        {
            this.setBlockState(worldIn, blockstateIn, x, y, zz, boundingBoxIn);
        }
    }

    private void extrudeDuo(World worldIn, Random rand, int solid, BlockState blockA, BlockState blockB, int x, int y, int z, MutableBoundingBox boundingBoxIn)
    {
        for (int zz = z; zz >= rand.nextInt(4 * (4 - solid)); zz--)
        {
            this.setBlockState(worldIn, (zz % 2 == 0) ? blockA : blockB, x, y, zz, boundingBoxIn);
        }
    }

    private void extrudeTrio(World worldIn, Random rand, int solid, BlockState blockA, BlockState blockB, int x, int y, int z, MutableBoundingBox boundingBoxIn)
    {
        for (int zz = z; zz >= rand.nextInt(4 * (4 - solid)); zz--)
        {
            this.setBlockState(worldIn, (zz % 3 == 0) ? blockA : blockB, x, y, zz, boundingBoxIn);
        }
    }

    private void extrudeTrioOff(World worldIn, Random rand, int solid, BlockState blockA, BlockState blockB, int x, int y, int z, MutableBoundingBox boundingBoxIn)
    {
        for (int zz = z; zz >= rand.nextInt(4 * (4 - solid)); zz--)
        {
            this.setBlockState(worldIn, (zz % 3 == 1) ? blockA : blockB, x, y, zz, boundingBoxIn);
        }
    }
}
