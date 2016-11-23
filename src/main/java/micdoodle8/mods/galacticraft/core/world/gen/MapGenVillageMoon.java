package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenMoonDungeon;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.StructureMoonDungeonPieces;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MapGenVillageMoon extends MapGenStructure
{
    public static List<BiomeGenBase> villageSpawnBiomes = Arrays.asList(new BiomeGenBase[] { BiomeGenBaseMoon.moonFlat });
    private final int terrainType;
    private static boolean initialized;

    static
    {
        try
        {
            MapGenVillageMoon.initiateStructures();
        }
        catch (Throwable e)
        {

        }
    }

    public static void initiateStructures() throws Throwable
    {
        if (!MapGenVillageMoon.initialized)
        {
            MapGenStructureIO.registerStructure(StructureVillageStartMoon.class, "MoonVillage");
            MapGenStructureIO.registerStructure(MapGenMoonDungeon.Start.class, "MoonDungeon");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillageField.class, "MoonField1");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillageField2.class, "MoonField2");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillageHouse.class, "MoonHouse");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillageRoadPiece.class, "MoonRoadPiece");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillagePathGen.class, "MoonPath");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillageTorch.class, "MoonTorch");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillageStartPiece.class, "MoonWell");
            MapGenStructureIO.registerStructureComponent(StructureComponentVillageWoodHut.class, "MoonWoodHut");
            MapGenStructureIO.registerStructureComponent(StructureMoonDungeonPieces.Start.class, "MoonDungeonStart");
            MapGenStructureIO.registerStructureComponent(StructureMoonDungeonPieces.Corridor.class, "MoonDungeonCorridor");
            MapGenStructureIO.registerStructureComponent(StructureMoonDungeonPieces.RoomEmpty.class, "MoonDungeonEmptyRoom");
            MapGenStructureIO.registerStructureComponent(StructureMoonDungeonPieces.TreasureCorridor.class, "MoonDungeonTreasureCorridor");
            MapGenStructureIO.registerStructureComponent(StructureMoonDungeonPieces.RoomBoss.class, "MoonDungeonBossRoom");
            MapGenStructureIO.registerStructureComponent(StructureMoonDungeonPieces.RoomTreasure.class, "MoonDungeonTreasureRoom");
        }

        MapGenVillageMoon.initialized = true;
    }

    public MapGenVillageMoon()
    {
        this.terrainType = 0;
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int i, int j)
    {
        final byte numChunks = 32;
        final byte offsetChunks = 8;
        final int oldi = i;
        final int oldj = j;

        if (i < 0)
        {
            i -= numChunks - 1;
        }

        if (j < 0)
        {
            j -= numChunks - 1;
        }

        int randX = i / numChunks;
        int randZ = j / numChunks;
        final Random var7 = this.worldObj.setRandomSeed(i, j, 10387312);
        randX *= numChunks;
        randZ *= numChunks;
        randX += var7.nextInt(numChunks - offsetChunks);
        randZ += var7.nextInt(numChunks - offsetChunks);

        return oldi == randX && oldj == randZ;

    }

    @Override
    protected StructureStart getStructureStart(int par1, int par2)
    {
        FMLLog.info("Generating Moon Village at x" + par1 * 16 + " z" + par2 * 16);
        return new StructureVillageStartMoon(this.worldObj, this.rand, par1, par2, this.terrainType);
    }

    @Override
    public String getStructureName()
    {
        return "MoonVillage";
    }
}
