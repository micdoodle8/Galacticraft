package micdoodle8.mods.galacticraft.api.world;

import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.BiMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome.SpawnListEntry;

/**
 * Implement this on any Galacticraft World dimension biome registered for a Celestial Body
 */
public interface IMobSpawnBiome
{
    void initialiseMobLists(Map<SpawnListEntry, EntityClassification> mobInfo);
}
