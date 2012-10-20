package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.core.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.moon.client.GCMoonColorizerGrass;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.MathHelper;
import net.minecraft.src.SpawnListEntry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

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

    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor()
    {
        double var3 = (double)MathHelper.clamp_float(this.maxHeight, 0.0F, 1.0F);
        return GCMoonColorizerGrass.getGrassColor(var3, var3);
    }
}
