package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.GCCoreEntityZombie;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.SpawnListEntry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsBiomeGenBase extends BiomeGenBase
{
    public static final BiomeGenBase marsFlat = (new GCMarsBiomeGenFlat(103)).setBiomeName("marsFlat");

    public GCMarsBiomeGenBase(int var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(GCCoreEntityZombie.class, 10, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(GCCoreEntitySpider.class, 10, 4, 4));
        this.rainfall = 0F;
    }
    
    @Override
	public GCMarsBiomeGenBase setColor(int var1)
    {
        return (GCMarsBiomeGenBase)super.setColor(var1);
    }

    @Override
	public float getSpawningChance()
    {
        return 0.01F;
    }
}
