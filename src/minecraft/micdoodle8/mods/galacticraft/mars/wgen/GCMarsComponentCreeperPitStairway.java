package micdoodle8.mods.galacticraft.mars.wgen;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.wgen.GCCoreStructureComponent;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsComponentCreeperPitStairway extends GCCoreStructureComponent
{
    private int[] orig = new int[3];
    private final GCMarsComponentCreeperPitRoom originalRoom;
    
	protected GCMarsComponentCreeperPitStairway(GCMarsComponentCreeperPitRoom origRoom, int type, Random rand, int x, int y, int z)
	{
		super(type);
        this.orig = new int[] {x, y, z};
        this.originalRoom = origRoom;
        this.boundingBox = new StructureBoundingBox(Math.min(x - 5, x - 2), Math.min(y - 20, y - 20), Math.min(z - 5, z - 2), Math.max(x - 5, x - 2), Math.max(y, y), Math.max(z - 5, z - 2));
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
		
    }
	
	@Override
	public boolean addComponentParts(World par1World, Random var2, StructureBoundingBox var3)
	{
		final int x1 = this.getBoundingBox().minX;
		final int y1 = this.getBoundingBox().minY;
		final int z1 = this.getBoundingBox().minZ;
		final int x2 = this.getBoundingBox().maxX;
		final int y2 = this.getBoundingBox().maxY;
		final int z2 = this.getBoundingBox().maxZ;
		
//		this.fillBlocks2(par1World, this.getBoundingBox(), x1, y1, z1, x2, y2, z2, GCBlocks.creeperDungeonWall.blockID, 0);
//		this.fillBlocks2(par1World, this.getBoundingBox(), x1 + 1, y1, z1 + 1, x2 - 1, y2, z2 - 1, 0, 0);
		this.fillWithBlocks(par1World, var3, x1, y1, z1, x2, y2, z2, GCMarsBlocks.creeperDungeonWall.blockID, 0, false);
		this.fillWithBlocks(par1World, var3, x1 + 1, y1, z1 + 1, x2 - 1, y2, z2 - 1, 0, 0, false);
		
		return true;
	}
}
