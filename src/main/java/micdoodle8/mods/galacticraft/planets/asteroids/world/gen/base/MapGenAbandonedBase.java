package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import com.mojang.datafixers.Dynamic;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidFeatures;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.MapGenDungeonMars;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MapGenAbandonedBase extends Structure<BaseConfiguration>
{
//    @Override
//    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean p_180706_3_)
//    {
//        return null;
//    }


    public MapGenAbandonedBase(Function<Dynamic<?>, ? extends BaseConfiguration> deserializer)
    {
        super(deserializer);
    }

    @Override
    public String getStructureName()
    {
        return "GC_AbandonedBase";
    }

    @Override
    public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn)
    {
        long dungeonPos = MapGenDungeonMars.getDungeonPosForCoords(generatorIn, chunkX, chunkZ, ((IGalacticraftDimension) generatorIn.world.getDimension()).getDungeonSpacing());
        int i = (int) (dungeonPos >> 32);
        int j = (int) dungeonPos;  //Java automatically gives the 32 least significant bits
        return i == chunkX && j == chunkZ;
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BaseConfiguration config)
    {
        return super.place(worldIn, generator, rand, pos, config);
    }

//    @Override
//    public void generate(World worldIn, int x, int z, ChunkPrimer chunkPrimerIn)
//    {
//        int i = this.range;
//        this.world = worldIn;
//        this.rand.setSeed(worldIn.getSeed());
//        long j = this.rand.nextLong();
//        long k = this.rand.nextLong();
//
//        for (int l = x - i; l <= x + i; ++l)
//        {
//            for (int i1 = z - i; i1 <= z + i; ++i1)
//            {
//                long j1 = (long)l * j;
//                long k1 = (long)i1 * k;
//                this.rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
//                this.recursiveGenerate(worldIn, l, i1, x, z, chunkPrimerIn);
//            }
//        }
//    }

    @Override
    public IStartFactory getStartFactory()
    {
        return MapGenAbandonedBase.Start::new;
    }

    @Override
    public int getSize()
    {
        return 4;
    }

//    @Override
//    protected StructureStart getStructureStart(int chunkX, int chunkZ)
//    {
//        BlockVec3 asteroid = ((DimensionAsteroids) this.world.getDimension()).getClosestAsteroidXZ((chunkX << 4) + 8, 0, (chunkZ << 4) + 8, false);
//        if (asteroid == null)
//        {
//            return new MapGenAbandonedBase.Start(this.world, this.rand, (chunkX << 4) + 8, (chunkZ << 4) + 8, 15, new BaseConfiguration(148, this.rand));
//        }
//        return new MapGenAbandonedBase.Start(this.world, this.rand, asteroid.x, asteroid.z, asteroid.sideDoneBits - 5, new BaseConfiguration(asteroid.y - 10, this.rand));
//    }

    public static class Start extends StructureStart
    {
//        private BaseConfiguration configuration;

        public Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed)
        {
            super(structure, chunkX, chunkZ, boundsIn, referenceIn, seed);
//            this.configuration = configuration;
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            int size;
            BlockVec3 asteroidVec = ((DimensionAsteroids) generator.world.getDimension()).getClosestAsteroidXZ((chunkX << 4) + 8, 0, (chunkZ << 4) + 8, false);
            if (asteroidVec == null)
            {
                size = 15;
            }
            else
            {
                size = asteroidVec.sideDoneBits - 5;
            }
            BaseConfiguration config = generator.getStructureConfig(biomeIn, AsteroidFeatures.ASTEROID_BASE.get());
            // configuration, rand, posX + xoffset, posZ + zoffset, direction
            int xoffset = 0;
            int zoffset = 0;
            Direction direction = Direction.Plane.HORIZONTAL.random(rand);
            switch (direction)
            {
            case NORTH:
                zoffset = -size;
                break;
            case SOUTH:
                zoffset = size;
                break;
            case WEST:
                xoffset = -size;
                break;
            case EAST:
                xoffset = size;
                break;
            }
            BaseStart startPiece = new BaseStart(config, rand, (chunkX << 4) + xoffset, (chunkZ << 4) + zoffset, direction);
            startPiece.buildComponent(startPiece, this.components, rand);
            List<StructurePiece> list = startPiece.attachedComponents;

            while (!list.isEmpty())
            {
                int i = rand.nextInt(list.size());
                StructurePiece structurecomponent = list.remove(i);
                structurecomponent.buildComponent(startPiece, this.components, rand);
            }

            this.recalculateStructureSize();
        }

//        public Start(World worldIn, Random rand, int posX, int posZ, int size, BaseConfiguration configuration)
//        {
//            super(posX >> 4, posZ >> 4);
////            this.configuration = configuration;
//            if (size < 1) size = 1;
//            size = size * (int) MathHelper.sqrt(size) / 4;
//            if (configuration.isHangarDeck()) size -= 6;
//            int xoffset = 0;
//            int zoffset = 0;
//            Direction direction = Direction.Plane.HORIZONTAL.random(rand);
//            switch (direction)
//            {
//            case NORTH:
//                zoffset = -size;
//                break;
//            case SOUTH:
//                zoffset = size;
//                break;
//            case WEST:
//                xoffset = -size;
//                break;
//            case EAST:
//                xoffset = size;
//                break;
//            }
//            BaseStart startPiece = new BaseStart(configuration, rand, posX + xoffset, posZ + zoffset, direction);
//            startPiece.buildComponent(startPiece, this.components, rand);
//            List<StructurePiece> list = startPiece.attachedComponents;
//
//            while (!list.isEmpty())
//            {
//                int i = rand.nextInt(list.size());
//                StructurePiece structurecomponent = list.remove(i);
//                structurecomponent.buildComponent(startPiece, this.components, rand);
//            }
//
//            this.recalculateStructureSize();
//        }
    }
}
