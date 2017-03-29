package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.List;
import java.util.Random;

public class MapGenDungeon extends MapGenStructure
{
    private static boolean initialized;
    private DungeonConfiguration configuration;

    static
    {
        try
        {
            MapGenDungeon.initiateStructures();
        }
        catch (Throwable e)
        {

        }
    }

    public MapGenDungeon(DungeonConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public static void initiateStructures() throws Throwable
    {
        if (!MapGenDungeon.initialized)
        {
            MapGenStructureIO.registerStructure(MapGenDungeon.Start.class, "MoonDungeon");
            MapGenStructureIO.registerStructureComponent(DungeonStart.class, "MoonDungeonStart");
            MapGenStructureIO.registerStructureComponent(Corridor.class, "MoonDungeonCorridor");
            MapGenStructureIO.registerStructureComponent(RoomEmpty.class, "MoonDungeonEmptyRoom");
            MapGenStructureIO.registerStructureComponent(RoomBoss.class, "MoonDungeonBossRoom");
            MapGenStructureIO.registerStructureComponent(RoomTreasure.class, "MoonDungeonTreasureRoom");
            MapGenStructureIO.registerStructureComponent(RoomSpawner.class, "MoonDungeonSpawnerRoom");
            MapGenStructureIO.registerStructureComponent(RoomChest.class, "MoonDungeonChestRoom");
        }

        MapGenDungeon.initialized = true;
    }

    @Override
    public String getStructureName()
    {
        return "GC_Dungeon";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        final byte numChunks = 44;
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= numChunks - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= numChunks - 1;
        }

        int k = chunkX / numChunks;
        int l = chunkZ / numChunks;
        Random random = this.world.setRandomSeed(k, l, 10387312);
        k = k * numChunks;
        l = l * numChunks;
        k = k + random.nextInt(numChunks);
        l = l + random.nextInt(numChunks);

        return i == k && j == l;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenDungeon.Start(this.world, this.rand, chunkX, chunkZ, this.configuration);
    }

    @Override
    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_)
    {
        return null;
    }

    public static class Start extends StructureStart
    {
        private DungeonConfiguration configuration;

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ, DungeonConfiguration configuration)
        {
            super(chunkX, chunkZ);
            this.configuration = configuration;
            DungeonStart startPiece = new DungeonStart(worldIn, configuration, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
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
