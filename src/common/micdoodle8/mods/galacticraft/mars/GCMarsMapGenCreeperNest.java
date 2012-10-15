package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GCCoreMapGenStructure;
import net.minecraft.src.MapGenStructure;
import net.minecraft.src.StructureMineshaftStart;
import net.minecraft.src.StructureStart;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsMapGenCreeperNest extends GCCoreMapGenStructure
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
        return new GCMarsStructureStart(this.worldObj, this.rand, par1, par2);
    }
}
