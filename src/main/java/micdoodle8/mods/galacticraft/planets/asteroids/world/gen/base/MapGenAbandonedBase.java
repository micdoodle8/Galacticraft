package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenAbandonedBase extends MapGenStructure
{
    private static boolean initialized;

    static
    {
        try
        {
            MapGenAbandonedBase.initiateStructures();
        }
        catch (Throwable e)
        {

        }
    }

    public MapGenAbandonedBase()
    {
    }

    public static void initiateStructures() throws Throwable
    {
        if (!MapGenAbandonedBase.initialized)
        {
              MapGenStructureIO.registerStructure(MapGenAbandonedBase.Start.class, "AbandonedBase");
              MapGenStructureIO.registerStructureComponent(BaseStart.class, "AbandonedBaseStart");
              MapGenStructureIO.registerStructureComponent(BaseLinking.class, "AbandonedBaseCorridor");
              MapGenStructureIO.registerStructureComponent(BaseRoom.class, "AbandonedBaseRoom");
              MapGenStructureIO.registerStructureComponent(BaseHangar.class, "AbandonedBaseHangar");
//            MapGenStructureIO.registerStructureComponent(RoomBoss.class, "MoonDungeonBossRoom");
//            MapGenStructureIO.registerStructureComponent(RoomTreasure.class, "MoonDungeonTreasureRoom");
//            MapGenStructureIO.registerStructureComponent(RoomSpawner.class, "MoonDungeonSpawnerRoom");
//            MapGenStructureIO.registerStructureComponent(RoomChest.class, "MoonDungeonChestRoom");
        }

        MapGenAbandonedBase.initialized = true;
    }


    @Override
    public String getStructureName()
    {
        return "GC_AbandonedBase";
    }

//    @Override
//    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
//    {
//        final byte numChunks = 44;
//        int i = chunkX;
//        int j = chunkZ;
//
//        if (chunkX < 0)
//        {
//            chunkX -= numChunks - 1;
//        }
//
//        if (chunkZ < 0)
//        {
//            chunkZ -= numChunks - 1;
//        }
//
//        int k = chunkX / numChunks;
//        int l = chunkZ / numChunks;
//        Random random = this.worldObj.setRandomSeed(k, l, 10387312);
//        k = k * numChunks;
//        l = l * numChunks;
//        k = k + random.nextInt(numChunks);
//        l = l + random.nextInt(numChunks);
//
//        return i == k && j == l;
//    }

    //TEMP for testing
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        return chunkX == -1 && chunkZ == 15;
    }

    @Override
    public void generate(IChunkProvider chunkProviderIn, World worldIn, int x, int z, ChunkPrimer chunkPrimerIn)
    {
        int i = this.range;
        this.worldObj = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        long j = this.rand.nextLong();
        long k = this.rand.nextLong();

        for (int l = x - i; l <= x + i; ++l)
        {
            for (int i1 = z - i; i1 <= z + i; ++i1)
            {
                long j1 = (long)l * j;
                long k1 = (long)i1 * k;
                this.rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
                this.recursiveGenerate(worldIn, l, i1, x, z, chunkPrimerIn);
            }
        }
    }
    
    public void reset()
    {
        this.structureMap.clear();
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        //TODO random y position, maybe search for nearby asteroids and match
        return new MapGenAbandonedBase.Start(this.worldObj, this.rand, chunkX, chunkZ, new BaseConfiguration(220, this.rand));
    }

    public static class Start extends StructureStart
    {
        private BaseConfiguration configuration;

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ, BaseConfiguration configuration)
        {
            super(chunkX, chunkZ);
            this.configuration = configuration;
            BaseStart startPiece = new BaseStart(worldIn, configuration, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            startPiece.buildComponent(startPiece, this.components, rand);
            List<StructureComponent> list = startPiece.attachedComponents;

            while (!list.isEmpty())
            {
                int i = rand.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(startPiece, this.components, rand);
            }

            this.updateBoundingBox();
        }
    }
 }
