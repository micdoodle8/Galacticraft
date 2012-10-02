package micdoodle8.mods.galacticraft;

import java.util.Random;

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
public class GCBiomeGenBase extends BiomeGenBase
{
    public static final BiomeGenBase marsFlat = (new GCBiomeGenFlat(103)).setColor(255).setBiomeName("Mars Flat");

    public GCBiomeGenBase(int var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(GCEntityZombie.class, 10, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(GCEntitySpider.class, 10, 4, 4));
    }
    
    protected GCBiomeGenBase setColor(int var1)
    {
        return (GCBiomeGenBase)super.setColor(var1);
    }

    public float getSpawningChance()
    {
        return 0.12F;
    }
}
