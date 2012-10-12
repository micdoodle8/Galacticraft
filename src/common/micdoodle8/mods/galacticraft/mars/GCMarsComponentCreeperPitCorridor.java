package micdoodle8.mods.galacticraft.mars;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCStructureComponent;
import net.minecraft.src.Block;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.World;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsComponentCreeperPitCorridor extends GCStructureComponent
{
    private int height;
    private int width;
    private int length;
    
	public GCMarsComponentCreeperPitCorridor(int type, int x, int y, int z, int height, int width, int length, int cbm)
	{
        super(type);
        this.setCoordBaseMode(cbm);
        this.height = height;
        this.width = width;
        this.boundingBox = this.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, length, this.height, width, cbm);
	}

	@Override
	public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) 
	{
		int cbm = this.getCoordBaseMode();
		
		for (int y = 0; y < this.height; y++)
		{
			for (int x = 0; x < this.width; x++)
			{
				for (int z = 0; z < this.width; z++)
				{
					this.placeBlockAtCurrentPosition(var1, cbm == 0 ? 0 : (cbm == 1 ? 1 : (cbm == 2 ? 2 : (cbm == 3 ? 3 : Block.obsidian.blockID))), 0, x, y, z, getBoundingBox());
				}
			}
		}
		
		return true;
	}
	
//    private boolean spawnerPlaced;
//    private int[] orig = new int[3];
//    private int[] newbb = new int[6];
//    private int length;
//    private int direction;
//    private int corridorCount;
//    private GCComponentCreeperPitRoom originalRoom;
//    private boolean shouldCapEnds;
//    private boolean isBossEntryCorridor;
//
//    public GCComponentCreeperPitCorridor(GCComponentCreeperPitRoom origRoom, int type, Random rand, int x, int y, int z, int length, int direction, boolean capEnds)
//    {
//        super(type);
//        this.orig = new int[] {x, y, z};
//        this.length = length;
//        this.direction = direction;
//        this.originalRoom = origRoom;
//        this.shouldCapEnds = capEnds;
//        
//        if (origRoom.corridorCount > 3)
//        {
//        	this.isBossEntryCorridor = true;
//        }
//        
//        switch (this.direction)
//        {
//        case 0:
//        	this.boundingBox = new StructureBoundingBox(Math.min(x, x + 7), Math.min(y, y + 5), Math.min(z, z - length), Math.max(x, x + 7), Math.max(y, y + 5), Math.max(z, z - length));
//         	this.corridorCount = (Math.min(z, z - length) + Math.max(z, z - length)) / 6;
//            break;
//        case 1:
//        	this.boundingBox = new StructureBoundingBox(Math.min(x + 7, x + 7 + length), Math.min(y, y + 5), Math.min(z, z + 7), Math.max(x + 7, x + 7 + length), Math.max(y, y + 5), Math.max(z, z + 7));
//        	this.corridorCount = (Math.min(x + 7, x + 7 + length) + Math.max(x + 7, x + 7 + length)) / 6;
//            break;
//        case 2:
//        	this.boundingBox = new StructureBoundingBox(Math.min(x + 7, x), Math.min(y, y + 5), Math.min(z + 7, z + 7 + length), Math.max(x + 7, x), Math.max(y, y + 5), Math.max(z + 7, z + 7 + length));
//         	this.corridorCount = (Math.min(z + 7, z + 7 + length) + Math.max(z + 7, z + 7 + length)) / 6;
//            break;
//        case 3:
//        	this.boundingBox = new StructureBoundingBox(Math.min(x, x - length), Math.min(y, y + 5), Math.min(z, z + 7), Math.max(x, x - length), Math.max(y, y + 5), Math.max(z, z + 7));
//        	this.corridorCount = (Math.min(x, x - length) + Math.max(x, x - length)) / 6;
//            break;
//        }
//
//        if (this.direction != 2 && this.direction != 0)
//        {
//            this.corridorCount = boundingBox.getXSize() / 5;
//        }
//        else
//        {
//            this.corridorCount = boundingBox.getZSize() / 5;
//        }
//    }
//    
//    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
//    {
////    	if (this.originalRoom.bossEntryCount < 2)
//    	{
////    		if (this.direction == 1 && this.originalRoom.bossEntryCount == 0)
////    		{
////            	int[] bb2 = {this.getBoundingBox().maxX, this.getBoundingBox().minY, this.getBoundingBox().maxZ};
////            	GCComponentCreeperPitStairway pit3 = new GCComponentCreeperPitStairway(originalRoom, 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2]);
////	          	par2List.add(pit3);
////	          	pit3.buildComponent(this, par2List, par3Random);
////	          	this.originalRoom.bossEntryCount++;
////	          	return;
////    		}
////    		if (this.direction == 2 && this.originalRoom.bossEntryCount == 1)
////    		{
////            	int[] bb2 = {this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().minZ};
////            	GCComponentCreeperPitStairway pit3 = new GCComponentCreeperPitStairway(originalRoom, 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2]);
////	          	par2List.add(pit3);
////	          	pit3.buildComponent(this, par2List, par3Random);
////	          	this.originalRoom.bossEntryCount++;
////	          	return;
////    		}
//    		
////    		switch (this.direction)
////            {
////            case 0:
////            	if (this.originalRoom.bossEntryCount < 2)
////            	{
////                	int[] bb2 = {this.getBoundingBox().maxX, this.getBoundingBox().minY, this.getBoundingBox().maxZ};
////                    GCComponentCreeperPitStairway pit3 = new GCComponentCreeperPitStairway(originalRoom, 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2]);
////                	par2List.add(pit3);
////                	pit3.buildComponent(this, par2List, par3Random);
////                	this.originalRoom.bossEntryCount++;
////            	}
////            case 1:
////            	if (this.originalRoom.bossEntryCount < 2)
////            	{
////                	int[] bb2 = {this.getBoundingBox().maxX, this.getBoundingBox().minY, this.getBoundingBox().maxZ};
////                    GCComponentCreeperPitStairway pit3 = new GCComponentCreeperPitStairway(originalRoom, 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2]);
////                	par2List.add(pit3);
////                	pit3.buildComponent(this, par2List, par3Random);
////                	this.originalRoom.bossEntryCount++;
////            	}
////            case 2:
////            	if (this.originalRoom.bossEntryCount > 1 && this.originalRoom.bossEntryCount < 4)
////            	{
////                	int[] bb2 = {this.getBoundingBox().maxX, this.getBoundingBox().minY, this.getBoundingBox().maxZ};
////                    GCComponentCreeperPitStairway pit3 = new GCComponentCreeperPitStairway(originalRoom, 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2]);
////                	par2List.add(pit3);
////                	pit3.buildComponent(this, par2List, par3Random);
////                	this.originalRoom.bossEntryCount++;
////            	}
////            case 3:
////            	if (this.originalRoom.bossEntryCount > 1 && this.originalRoom.bossEntryCount < 4)
////            	{
////                	int[] bb2 = {this.getBoundingBox().maxX, this.getBoundingBox().minY, this.getBoundingBox().maxZ};
////                    GCComponentCreeperPitStairway pit3 = new GCComponentCreeperPitStairway(originalRoom, 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2]);
////                	par2List.add(pit3);
////                	pit3.buildComponent(this, par2List, par3Random);
////                	this.originalRoom.bossEntryCount++;
////            	}
////            case 3:
////            	this.originalRoom.bossEntryCount++;
////            	int[] bb3 = {this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().maxZ};
////                GCComponentCreeperPitStairway pit5 = new GCComponentCreeperPitStairway(originalRoom, 2 + this.originalRoom.corridorCount, par3Random, bb3[0], bb3[1], bb3[2]);
////            	par2List.add(pit5);
////            	pit5.buildComponent(this, par2List, par3Random);
////            case 3:
////            	this.originalRoom.bossEntryCount++;
////            	int[] bb4 = {Math.min(this.getBoundingBox().minX, this.getBoundingBox().maxX), this.getBoundingBox().minY, Math.min(this.getBoundingBox().minZ, this.getBoundingBox().maxZ)};
////                GCComponentCreeperPitStairway pit7 = new GCComponentCreeperPitStairway(originalRoom, 1 + this.originalRoom.corridorCount, par3Random, bb4[0], bb4[1], bb4[2]);
////            	par2List.add(pit7);
////            	pit7.buildComponent(this, par2List, par3Random);
//    	}
//    	
////    	if (this.originalRoom.corridorCount < 9)
////    	{
////            int var1 = this.getComponentType();
////            
////            switch (this.direction)
////            {
////            case 0:
////            	this.originalRoom.corridorCount += 1;
////                int[] bb = {Math.min(this.originalRoom.getBoundingBox().minX, this.originalRoom.getBoundingBox().minX + 7), this.originalRoom.getBoundingBox().minY + 60, Math.min(this.originalRoom.getBoundingBox().minZ - 7, this.originalRoom.getBoundingBox().minZ - this.originalRoom.originalFourCorridorLength - 7)};
////            	GCComponentCreeperPitCorridor pit = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb[0], bb[1], bb[2], 47, 1, true);
////            	par2List.add(pit);
////            	pit.buildComponent(this, par2List, par3Random);
////            	this.originalRoom.corridorCount += 1;
////            	GCComponentCreeperPitCorridor pit2 = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb[0], bb[1], bb[2], 47, 3, true);
////            	par2List.add(pit2);
////            	pit2.buildComponent(this, par2List, par3Random);
////            case 1:
////            	this.originalRoom.corridorCount += 1;
////                int[] bb2 = {Math.max(this.originalRoom.getBoundingBox().minX + 7, this.originalRoom.getBoundingBox().minX + 7 + this.originalRoom.originalFourCorridorLength), this.originalRoom.getBoundingBox().minY + 60, Math.min(this.originalRoom.getBoundingBox().minZ, this.originalRoom.getBoundingBox().minZ + 7)};
////            	GCComponentCreeperPitCorridor pit3 = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2], 40, 0, false);
////            	par2List.add(pit3);
////            	pit3.buildComponent(this, par2List, par3Random);
////            	this.originalRoom.corridorCount += 1;
////            	GCComponentCreeperPitCorridor pit4 = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb2[0], bb2[1], bb2[2], 47, 2, true);
////            	par2List.add(pit4);
////            	pit4.buildComponent(this, par2List, par3Random);
////            case 2:
////            	this.originalRoom.corridorCount += 1;
////                int[] bb3 = {Math.min(this.originalRoom.getBoundingBox().minX, this.originalRoom.getBoundingBox().minX + 7), this.originalRoom.getBoundingBox().minY + 60, Math.max(this.originalRoom.getBoundingBox().minZ + 7, this.originalRoom.getBoundingBox().minZ + 7 + this.originalRoom.originalFourCorridorLength)};
////            	GCComponentCreeperPitCorridor pit5 = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb3[0], bb3[1], bb3[2], 40, 1, false);
////            	par2List.add(pit5);
////            	pit5.buildComponent(this, par2List, par3Random);
////            	this.originalRoom.corridorCount += 1;
////            	GCComponentCreeperPitCorridor pit6 = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb3[0], bb3[1], bb3[2], 47, 3, true);
////            	par2List.add(pit6);
////            	pit6.buildComponent(this, par2List, par3Random);
////            case 3:
////            	this.originalRoom.corridorCount += 1;
////                int[] bb4 = {Math.min(this.originalRoom.getBoundingBox().minX - 7, this.originalRoom.getBoundingBox().minX - this.originalRoom.originalFourCorridorLength - 7), this.originalRoom.getBoundingBox().minY + 60, Math.min(this.originalRoom.getBoundingBox().minZ, this.originalRoom.getBoundingBox().minZ + 7)};
////            	GCComponentCreeperPitCorridor pit7 = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb4[0], bb4[1], bb4[2], 40, 0, false);
////            	par2List.add(pit7);
////            	pit7.buildComponent(this, par2List, par3Random);
////            	this.originalRoom.corridorCount += 1;
////            	GCComponentCreeperPitCorridor pit8 = new GCComponentCreeperPitCorridor(originalRoom, var1 + 1 + this.originalRoom.corridorCount, par3Random, bb4[0], bb4[1], bb4[2], 40, 2, false);
////            	par2List.add(pit8);
////            	pit8.buildComponent(this, par2List, par3Random);
////            }
////    	}
//    }
//
//    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
//    {
//		int x1 = this.getBoundingBox().minX;
//		int y1 = this.getBoundingBox().minY;
//		int z1 = this.getBoundingBox().minZ;
//		int x2 = this.getBoundingBox().maxX;
//		int y2 = this.getBoundingBox().maxY;
//		int z2 = this.getBoundingBox().maxZ;
//
//        this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2), GCBlocks.creeperDungeonWall.blockID, 0, false);
//        
//		switch (this.direction)
//        {
//        case 0:
//        	if (this.shouldCapEnds)
//        	{
//                this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1 + 1, x2 - 1), Math.min(y1 + 1, y2 - 1), Math.min(z1 + 1, z2 - 1), Math.max(x1 + 1, x2 - 1), Math.max(y1 + 1, y2 - 1), Math.max(z1 + 1, z2 - 1), 0, 0, false);
//        	}
//        	else
//        	{
//                this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1 + 1, x2 - 1), Math.min(y1 + 1, y2 - 1), Math.min(z1, z2), Math.max(x1 + 1, x2 - 1), Math.max(y1 + 1, y2 - 1), Math.max(z1, z2), 0, 0, false);
//        	}
//        	
//    		this.fillWithAir(par1World, par3StructureBoundingBox, this.orig[0], this.orig[1], this.orig[2], this.orig[0] + 7, this.orig[1] + 5, this.orig[2] + 7);
//            break;
//        case 1:
//        	if (this.shouldCapEnds)
//        	{
//        		this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1 + 1, x2 - 1), Math.min(y1 + 1, y2 - 1), Math.min(z1 + 1, z2 - 1), Math.max(x1 + 1, x2 - 1), Math.max(y1 + 1, y2 - 1), Math.max(z1 + 1, z2 - 1), 0, 0, false);
//        	}
//        	else
//        	{
//        		this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1, x2), Math.min(y1 + 1, y2 - 1), Math.min(z1 + 1, z2 - 1), Math.max(x1, x2), Math.max(y1 + 1, y2 - 1), Math.max(z1 + 1, z2 - 1), 0, 0, false);
//        	}
//        	
//        	this.fillWithAir(par1World, par3StructureBoundingBox, this.orig[0], this.orig[1], this.orig[2], this.orig[0] + 7, this.orig[1] + 5, this.orig[2] + 7);
//            break;
//        case 2:
//        	if (this.shouldCapEnds)
//        	{
//                this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1 + 1, x2 - 1), Math.min(y1 + 1, y2 - 1), Math.min(z1 + 1, z2 - 1), Math.max(x1 + 1, x2 - 1), Math.max(y1 + 1, y2 - 1), Math.max(z1 + 1, z2 - 1), 0, 0, false);
//        	}
//        	else
//        	{
//                this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1 + 1, x2 - 1), Math.min(y1 + 1, y2 - 1), Math.min(z1, z2), Math.max(x1 + 1, x2 - 1), Math.max(y1 + 1, y2 - 1), Math.max(z1, z2), 0, 0, false);
//        	}
//        	
//    		this.fillWithAir(par1World, par3StructureBoundingBox, this.orig[0], this.orig[1], this.orig[2], this.orig[0] + 7, this.orig[1] + 5, this.orig[2] + 7);
//            break;
//        case 3:
//        	if (this.shouldCapEnds)
//        	{
//        		this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1 + 1, x2 - 1), Math.min(y1 + 1, y2 - 1), Math.min(z1 + 1, z2 - 1), Math.max(x1 + 1, x2 - 1), Math.max(y1 + 1, y2 - 1), Math.max(z1 + 1, z2 - 1), 0, 0, false);
//        	}
//        	else
//        	{
//        		this.fillWithBlocks(par1World, par3StructureBoundingBox, Math.min(x1, x2), Math.min(y1 + 1, y2 - 1), Math.min(z1 + 1, z2 - 1), Math.max(x1, x2), Math.max(y1 + 1, y2 - 1), Math.max(z1 + 1, z2 - 1), 0, 0, false);
//        	}
//        	
//    		this.fillWithAir(par1World, par3StructureBoundingBox, this.orig[0], this.orig[1], this.orig[2], this.orig[0] + 7, this.orig[1] + 5, this.orig[2] + 7);
//            break;
//        }
//		
//		for (int i = 7; i < this.length - 7; i += 5)
//		{
//			if (this.direction == 0 || this.direction == 2)
//			{
//				if (par1World.getBlockId(this.getBoundingBox().minX + 1, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + i) == 0 && par1World.getBlockId(this.getBoundingBox().minX, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + i) == GCBlocks.creeperDungeonWall.blockID)
//	            this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 1F, this.getBoundingBox().minX + 1, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + i, GCBlocks.unlitTorch.blockID, 0);
//				if (par1World.getBlockId(this.getBoundingBox().maxX - 1, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + i) == 0 && par1World.getBlockId(this.getBoundingBox().maxX, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + i) == GCBlocks.creeperDungeonWall.blockID)
//	            this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 1F, this.getBoundingBox().maxX - 1, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + i, GCBlocks.unlitTorch.blockID, 0);
//			}
//			else
//			{
//				if (par1World.getBlockId(this.getBoundingBox().minX + i, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + 1) == 0 && par1World.getBlockId(this.getBoundingBox().minX + i, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ) == GCBlocks.creeperDungeonWall.blockID)
//	            this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 1F, this.getBoundingBox().minX + i, this.getBoundingBox().minY + 3, this.getBoundingBox().minZ + 1, GCBlocks.unlitTorch.blockID, 0);
//				if (par1World.getBlockId(this.getBoundingBox().minX + i, this.getBoundingBox().minY + 3, this.getBoundingBox().maxZ - 1) == 0 && par1World.getBlockId(this.getBoundingBox().minX + i, this.getBoundingBox().minY + 3, this.getBoundingBox().maxZ) == GCBlocks.creeperDungeonWall.blockID)
//	            this.randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 1F, this.getBoundingBox().minX + i, this.getBoundingBox().minY + 3, this.getBoundingBox().maxZ - 1, GCBlocks.unlitTorch.blockID, 0);
//			}
//		}
//		
//        return true;
//    }
}
