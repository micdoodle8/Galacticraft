package micdoodle8.mods.galacticraft.mars;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCEntitySpider;
import micdoodle8.mods.galacticraft.core.GCEntityZombie;
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
public class GCMarsBiomeGenBase extends BiomeGenBase
{
    public static final BiomeGenBase marsFlat = (new GCMarsBiomeGenFlat(103)).setColor(255).setBiomeName("Mars Flat");

    public GCMarsBiomeGenBase(int var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(GCEntityZombie.class, 10, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(GCEntitySpider.class, 10, 4, 4));
    }
    
    protected GCMarsBiomeGenBase setColor(int var1)
    {
        return (GCMarsBiomeGenBase)super.setColor(var1);
    }

    public float getSpawningChance()
    {
        return 0.12F;
    }
}
