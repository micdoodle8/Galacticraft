package micdoodle8.mods.galacticraft.titan.wgen;

import net.minecraft.world.biome.BiomeGenBase;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCTitanBiomeGenFlat extends GCTitanBiomeGenBase
{
    public GCTitanBiomeGenFlat(int par1)
    {
        super(par1); 
        this.setBiomeName("titanFlat");
        this.setColor(16711680);
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
