package micdoodle8.mods.galacticraft.moon;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.GCCoreEntityZombie;
import net.minecraft.src.BiomeDecorator;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.WorldGenTallGrass;
import net.minecraft.src.WorldGenerator;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonBiomeGenBase extends BiomeGenBase
{
    public static final BiomeGenBase moonFlat = (new GCMoonBiomeGenFlat(102)).setBiomeName("moon").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);

    public GCMoonBiomeGenBase(int var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(GCCoreEntityZombie.class, 10, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(GCCoreEntitySpider.class, 10, 4, 4));
        this.rainfall = 0F;
    }
    
    protected GCMoonBiomeGenBase setColor(int var1)
    {
        return (GCMoonBiomeGenBase)super.setColor(var1);
    }

    public float getSpawningChance()
    {
        return 0.01F;
    }
}
