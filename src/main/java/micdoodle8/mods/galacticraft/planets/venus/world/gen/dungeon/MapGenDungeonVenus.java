package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Structure;
import net.minecraft.world.gen.feature.StructureIO;
import net.minecraft.world.gen.feature.StructurePiece;
import net.minecraft.world.gen.feature.StructureStart;

import java.util.List;
import java.util.Random;

public class MapGenDungeonVenus extends Structure
{
    private static boolean initialized;
    private DungeonConfigurationVenus configuration;

    static
    {
        try
        {
            MapGenDungeonVenus.initiateStructures();
        }
        catch (Throwable e)
        {

        }
    }

    public MapGenDungeonVenus(DungeonConfigurationVenus configuration)
    {
        this.configuration = configuration;
    }

    public static void initiateStructures() throws Throwable
    {
        if (!MapGenDungeonVenus.initialized)
        {
            StructureIO.registerStructure(MapGenDungeonVenus.Start.class, "VenusDungeon");
            StructureIO.registerStructureComponent(DungeonStartVenus.class, "VenusDungeonStart");
            StructureIO.registerStructureComponent(CorridorVenus.class, "VenusDungeonCorridor");
            StructureIO.registerStructureComponent(RoomEmptyVenus.class, "VenusDungeonEmptyRoom");
            StructureIO.registerStructureComponent(RoomBossVenus.class, "VenusDungeonBossRoom");
            StructureIO.registerStructureComponent(RoomTreasureVenus.class, "VenusDungeonTreasureRoom");
            StructureIO.registerStructureComponent(RoomSpawnerVenus.class, "VenusDungeonSpawnerRoom");
            StructureIO.registerStructureComponent(RoomChestVenus.class, "VenusDungeonChestRoom");
            StructureIO.registerStructureComponent(RoomEntranceVenus.class, "VenusDungeonEntranceRoom");
        }

        MapGenDungeonVenus.initialized = true;
    }

    @Override
    public String getStructureName()
    {
        return "GC_Dungeon_Venus";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        long dungeonPos = MapGenDungeon.getDungeonPosForCoords(this.world, chunkX, chunkZ, ((IGalacticraftWorldProvider) this.world.provider).getDungeonSpacing());
        int i = (int) (dungeonPos >> 32);
        int j = (int) dungeonPos;
        return i == chunkX && j == chunkZ;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenDungeonVenus.Start(this.world, this.rand, chunkX, chunkZ, this.configuration);
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean p_180706_3_)
    {
        return null;
    }

    public static class Start extends StructureStart
    {
        private DungeonConfigurationVenus configuration;

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ, DungeonConfigurationVenus configuration)
        {
            super(chunkX, chunkZ);
            this.configuration = configuration;
            DungeonStartVenus startPiece = new DungeonStartVenus(worldIn, configuration, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            startPiece.buildComponent(startPiece, this.components, rand);
            List<StructurePiece> list = startPiece.attachedComponents;

            while (!list.isEmpty())
            {
                int i = rand.nextInt(list.size());
                StructurePiece structurecomponent = list.remove(i);
                structurecomponent.buildComponent(startPiece, this.components, rand);
            }

            this.updateBoundingBox();
        }
    }
}
