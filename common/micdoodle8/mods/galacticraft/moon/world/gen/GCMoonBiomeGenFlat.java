package micdoodle8.mods.galacticraft.moon.world.gen;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCMoonBiomeGenFlat extends GCMoonBiomeGenBase
{
    public GCMoonBiomeGenFlat(int par1)
    {
        super(par1);
        this.setBiomeName("moonFlat");
        this.setColor(11111111);
        this.minHeight = 1.5F;
        this.maxHeight = 0.4F;
    }
}
