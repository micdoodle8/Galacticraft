package micdoodle8.mods.galacticraft;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ModLoader;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBiomeGenFlat extends GCBiomeGenBase
{
    public GCBiomeGenFlat(int par1)
    {
        super(par1); 
        this.topBlock = (byte)GCConfigManager.idBlockMarsGrass;
        this.fillerBlock = (byte)GCConfigManager.idBlockMarsStone;
        this.rainfall = 0.0F;
        this.setBiomeName("marsFlat");
        this.setColor(16711680);
        this.setTemperatureRainfall2(2.0F, 0.0F);
        this.minHeight = 2.5F;
        this.maxHeight = 0.4F;
    }
    
    private BiomeGenBase setTemperatureRainfall2(float par1, float par2)
    {
        if (par1 > 0.1F && par1 < 0.2F)
        {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        else
        {
            this.temperature = par1;
            this.rainfall = par2;
            return this;
        }
    }
}
