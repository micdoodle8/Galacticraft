package micdoodle8.mods.galacticraft;

import java.util.List;
import java.util.Random;

import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.World;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCComponentCreeperPitStairway extends StructureComponent
{
    private int[] orig = new int[3];
    private GCComponentCreeperPitRoom originalRoom;
    
	protected GCComponentCreeperPitStairway(GCComponentCreeperPitRoom origRoom, int type, Random rand, int x, int y, int z) 
	{
		super(type);
        this.orig = new int[] {x, y, z};
        this.originalRoom = origRoom;
        this.boundingBox = new StructureBoundingBox(Math.min(x, x + 3), Math.min(y - 20, y - 20), Math.min(z, z + 3), Math.max(x, x + 3), Math.max(y, y), Math.max(z, z + 3));
	}
	
	@Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
		
    }
	
	@Override
	public boolean addComponentParts(World par1World, Random var2, StructureBoundingBox var3) 
	{
		int x1 = this.getBoundingBox().minX;
		int y1 = this.getBoundingBox().minY;
		int z1 = this.getBoundingBox().minZ;
		int x2 = this.getBoundingBox().maxX;
		int y2 = this.getBoundingBox().maxY;
		int z2 = this.getBoundingBox().maxZ;
		
//		this.fillBlocks2(par1World, this.getBoundingBox(), x1, y1, z1, x2, y2, z2, GCBlocks.creeperDungeonWall.blockID, 0);
//		this.fillBlocks2(par1World, this.getBoundingBox(), x1 + 1, y1, z1 + 1, x2 - 1, y2, z2 - 1, 0, 0);
		this.fillWithBlocks(par1World, var3, x1, y1, z1, x2, y2, z2, GCBlocks.creeperDungeonWall.blockID, 0, false);
		this.fillWithBlocks(par1World, var3, x1 + 1, y1, z1 + 1, x2 - 1, y2, z2 - 1, 0, 0, false);
		
		return true;
	}
}
