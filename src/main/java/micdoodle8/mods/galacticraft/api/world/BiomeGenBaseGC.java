package micdoodle8.mods.galacticraft.api.world;

import java.util.LinkedList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.biome.Biome;

/**
 * This extension of BiomeGenBase contains the default initialiseMobLists()
 * called on CelestialBody registration to register mob spawn data
 */
public abstract class BiomeGenBaseGC extends Biome implements IMobSpawnBiome
{
    public final boolean isAdaptiveBiome;
    
    protected BiomeGenBaseGC(BiomeProperties var1)
    {
        super(var1);
        this.setRegistryName(var1.biomeName);
        GalacticraftCore.biomesList.add(this);
        this.isAdaptiveBiome = false;
    }

    protected BiomeGenBaseGC(BiomeProperties properties, boolean adaptive)
    {
        super(properties);
        this.setRegistryName(properties.biomeName);
        this.isAdaptiveBiome = adaptive;
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
     * 
     * Override this if different behaviour is required.
     */
    @Override
    public void initialiseMobLists(LinkedList<SpawnListEntry> mobInfo)
    {
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        for (SpawnListEntry entry : mobInfo)
        {
            Class<?> mobClass = entry.entityClass;
            if (EntityWaterMob.class.isAssignableFrom(mobClass))
            {
                this.spawnableWaterCreatureList.add(entry);
            }
            else if (EntityAmbientCreature.class.isAssignableFrom(mobClass))
            {
                this.spawnableCaveCreatureList.add(entry);
            }
            else if (EntityMob.class.isAssignableFrom(mobClass))
            {
                this.spawnableMonsterList.add(entry);
            }
            else
            {
                this.spawnableCreatureList.add(entry);
            }
        }
    }
}
