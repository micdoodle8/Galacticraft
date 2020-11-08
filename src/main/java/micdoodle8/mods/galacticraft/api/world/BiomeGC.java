package micdoodle8.mods.galacticraft.api.world;

import java.util.LinkedList;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.world.biome.Biome;

/**
 * This extension of BiomeGenBase contains the default initialiseMobLists()
 * called on CelestialBody registration to register mob spawn data
 */
public abstract class BiomeGC extends Biome implements IMobSpawnBiome
{
//    public final boolean isAdaptiveBiome;

    protected BiomeGC(Biome.Builder biomeBuilder)
    {
        super(biomeBuilder);
        GalacticraftCore.biomesList.add(this);
//        this.isAdaptiveBiome = false;
    }

    protected BiomeGC(Biome.Builder biomeBuilder, boolean adaptive)
    {
        super(biomeBuilder);
//        this.isAdaptiveBiome = adaptive;
    }

    /**
     * Override this in your biomes
     * <br>
     * (Note: if adaptive biomes, only the FIRST to register the adaptive biome will have its
     * types registered in the BiomeDictionary - sorry, that's a Forge limitation.)
     */
    public void registerTypes(Biome registering)
    {
    }

    /**
     * The default implementation in BiomeGenBaseGC will attempt to allocate each
     * SpawnListEntry in the CelestialBody's mobInfo to this biome's
     * Water, Cave, Monster or Creature lists according to whether the
     * spawnable entity's class is a subclass of EntityWaterMob, EntityAmbientCreature,
     * EntityMob or anything else (passive mobs or plain old EntityLiving).
     * <p>
     * Override this if different behaviour is required.
     */
    @Override
    public void initialiseMobLists(Map<SpawnListEntry, EntityClassification> mobInfo)
    {
        for (EntityClassification classification : EntityClassification.values())
        {
            this.getSpawns(classification).clear();
        }
        for (Map.Entry<SpawnListEntry, EntityClassification> entry : mobInfo.entrySet())
        {
            getSpawns(entry.getValue()).add(entry.getKey());
        }
    }
}
