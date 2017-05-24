package micdoodle8.mods.galacticraft.api.world;

import java.util.LinkedList;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * This extension of BiomeGenBase contains the default initialiseMobLists()
 * called on CelestialBody registration to register mob spawn data
 */
public abstract class BiomeGenBaseGC extends BiomeGenBase implements IMobSpawnBiome
{
    protected BiomeGenBaseGC(int var1)
    {
        super(var1);
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
