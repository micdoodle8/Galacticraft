package micdoodle8.mods.galacticraft.api.world;

import java.util.LinkedList;

import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

/**
 * Implement this on any Galacticraft World Provider biome registered for a Celestial Body
 */
public interface IMobSpawnBiome
{
    public void initialiseMobLists(LinkedList<SpawnListEntry> mobInfo);
}
