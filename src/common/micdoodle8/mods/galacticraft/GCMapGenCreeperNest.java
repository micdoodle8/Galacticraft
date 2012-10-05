package micdoodle8.mods.galacticraft;

import net.minecraft.src.MapGenStructure;
import net.minecraft.src.StructureMineshaftStart;
import net.minecraft.src.StructureStart;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMapGenCreeperNest extends MapGenStructure
{
    protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
    	if (this.rand.nextInt(100) != 0)
    	{
    		return false;
    	}
    	
    	return true;
    }

    protected StructureStart getStructureStart(int par1, int par2)
    {
        return new GCStructureStart(this.worldObj, this.rand, par1, par2);
    }
}
