package micdoodle8.mods.galacticraft.europa;

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
public class GCEuropaBiomeGenBase extends BiomeGenBase
{
    public static final BiomeGenBase europaFlat = (new GCEuropaBiomeGenFlat(103)).setBiomeName("europaFlat");

    public GCEuropaBiomeGenBase(int var1)
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
	public GCEuropaBiomeGenBase setColor(int var1)
    {
        return (GCEuropaBiomeGenBase)super.setColor(var1);
    }

    @Override
	public float getSpawningChance()
    {
        return 0.01F;
    }
}
