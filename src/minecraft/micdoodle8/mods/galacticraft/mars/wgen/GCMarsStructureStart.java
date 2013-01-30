package micdoodle8.mods.galacticraft.mars.wgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsStructureStart extends StructureStart
{
	public GCMarsStructureStart(World world, Random random, int x, int z)
	{
        final int var5 = (x << 4) + 8;
        final int var6 = (z << 4) + 8;
        final GCMarsComponentCreeperPitRoom room = new GCMarsComponentCreeperPitRoom(0, world, random, var5, 90, var6, 40, 7, 0);
        
        if (room != null)
        {
            this.components.add(room);
            room.buildComponent(room, this.components, random);
        }
        
        this.updateBoundingBox();
	}
}
