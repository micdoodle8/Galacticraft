package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class MapGenDungeonMars extends MapGenDungeon
{
    private static boolean initialized;

    static
    {
        try
        {
            MapGenDungeonMars.initiateStructures();
        }
        catch (Throwable e)
        {

        }
    }

    public MapGenDungeonMars(DungeonConfiguration configuration)
    {
        super(configuration);
    }

    public static void initiateStructures() throws Throwable
    {
        if (!MapGenDungeonMars.initialized)
        {
            MapGenStructureIO.registerStructureComponent(RoomBossMars.class, "MarsDungeonBossRoom");
            MapGenStructureIO.registerStructureComponent(RoomTreasureMars.class, "MarsDungeonTreasureRoom");
        }

        MapGenDungeonMars.initialized = true;
    }
}
