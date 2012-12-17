package micdoodle8.mods.galacticraft.enceladus.wgen;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.SpawnListEntry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCEnceladusBiomeGenBase extends BiomeGenBase
{
    public static final BiomeGenBase enceladusFlat = new GCEnceladusBiomeGenFlat(103).setBiomeName("enceladusFlat");

    public GCEnceladusBiomeGenBase(int var1)
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
	public GCEnceladusBiomeGenBase setColor(int var1)
    {
        return (GCEnceladusBiomeGenBase)super.setColor(var1);
    }

    @Override
	public float getSpawningChance()
    {
        return 0.01F;
    }
}
