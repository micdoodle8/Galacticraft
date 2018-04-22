package micdoodle8.mods.galacticraft.core.event;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootHandlerGC
{
    public static ResourceLocation TABLE_CRASHED_PROBE;

    public static void registerAll()
    {
        if (GalacticraftCore.isPlanetsLoaded) TABLE_CRASHED_PROBE = register("crashed_probe");
    }

    private static ResourceLocation register(String table)
    {
        return LootTableList.register(new ResourceLocation(Constants.MOD_ID_CORE, table));
    }
}
