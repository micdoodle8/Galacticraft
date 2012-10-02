package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.ComponentMineshaftRoom;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.StructureStart;
import net.minecraft.src.World;

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
		StructureBoundingBox bb = new StructureBoundingBox((x << 4) + 2, 25, (z << 4) + 2, ((x << 4) + 2) + 7, 200, ((z << 4) + 2) + 7);
        GCComponentCreeperPitRoom var5 = new GCComponentCreeperPitRoom(world, 0, random, bb, (x << 4) + 2, (z << 4) + 2);
        this.components.add(var5);
        if (var5.getBoundingBox() != null)
        {
            var5.buildComponent(var5, this.components, random);
            this.updateBoundingBox();
        }
	}
}
