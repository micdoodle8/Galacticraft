package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.StructureStart;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCStructureStart extends StructureStart
{
	public GCStructureStart(World world, Random random, int x, int z)
	{
        int var5 = (x << 4) + 8;
        int var6 = (z << 4) + 8;
        GCComponentCreeperPitRoom room = new GCComponentCreeperPitRoom(0, world, random, var5, 90, var6, 40, 7, 0);
        this.components.add(room);
        room.buildComponent(room, this.components, random);
        this.updateBoundingBox();
	}
}
