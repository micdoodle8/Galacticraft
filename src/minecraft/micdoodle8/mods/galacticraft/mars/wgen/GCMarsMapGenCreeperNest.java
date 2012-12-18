package micdoodle8.mods.galacticraft.mars.wgen;

import micdoodle8.mods.galacticraft.core.wgen.GCCoreMapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsMapGenCreeperNest extends GCCoreMapGenStructure
{
    @Override
	protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
    	if (this.rand.nextInt(50) != 0)
    	{
    		return false;
    	}
    	
    	return true;
    }

    @Override
	protected StructureStart getStructureStart(int par1, int par2)
    {
        return new GCMarsStructureStart(this.worldObj, this.rand, par1, par2);
    }
}
