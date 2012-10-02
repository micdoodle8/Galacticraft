package micdoodle8.mods.galacticraft;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCComponentCreeperPitRoom extends StructureComponent
{
	public int corridorCount;
	public int originalFourCorridorLength;
	public int bossEntryCorridor;
	public int bossEntryCount;
	
    public GCComponentCreeperPitRoom(World world, int par1, Random par2Random, StructureBoundingBox bb, int par3, int par4)
    {
        super(par1);
        this.boundingBox = bb;
        this.coordBaseMode = 0;
        this.corridorCount = 0;
        this.bossEntryCount = 0;
        this.bossEntryCorridor = par2Random.nextInt(7) + 4;
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        int var1 = this.getComponentType();
        int[] bb = {this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ};
        
        int length = 40;
        
        for (int var2 = 0; var2 < 4; var2++)
        {
        	GCComponentCreeperPitCorridor pit = new GCComponentCreeperPitCorridor(this, var1 + var2 + 1, par3Random, bb[0], bb[1], bb[2], length, var2, false);
        	this.originalFourCorridorLength = length;
        	par2List.add(pit);
        	pit.buildComponent(this, par2List, par3Random);
        	this.corridorCount++;
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
		this.getBoundingBox().maxY = this.getGroungHeightAt(par1World, this.getBoundingBox().getCenterX(), this.getBoundingBox().getCenterZ());
		
		int x1 = this.getBoundingBox().minX;
		int y1 = this.getBoundingBox().minY;
		int z1 = this.getBoundingBox().minZ;
		int x2 = this.getBoundingBox().maxX;
		int y2 = this.getBoundingBox().maxY;
		int z2 = this.getBoundingBox().maxZ;
		int xSize = this.getBoundingBox().getXSize();
		int ySize = this.getBoundingBox().getYSize();
		int zSize = this.getBoundingBox().getZSize();
		
		this.fillBlocks2(par1World, this.getBoundingBox(), x1, y1 + 7, z1, x2, y2, z2, GCBlocks.creeperDungeonWall.blockID, 0);
		this.fillBlocks2(par1World, this.getBoundingBox(), x1 + 1, y1, z1 + 1, x2 - 1, y2, z2 - 1, 0, 0);
		this.fillBlocks2(par1World, this.getBoundingBox(), x1, y1, z1, x2, y1, z2, GCBlocks.creeperDungeonWall.blockID, 0);
		
		for (int height = y2; height > y1; height--)
		{
			for (int width1 = x2; width1 > x1; width1--)
			{
				for (int width2 = z2; width2 > z1; width2--)
				{
					// Adds some space between and lowers chance
					
					if (height % 5 == 0 && par2Random.nextInt(20) == 0 && height != y2 && height > y1 + 10)
					{
						// Randomize numbers for offset from minX/minZ						
						int x = par2Random.nextInt(8);
						int z = par2Random.nextInt(8);
						
						if (par1World.getBlockId(x1 + x, height, z1 + z) == GCBlocks.creeperDungeonWall.blockID);
						{
							for (int i = -2; i < 2; i++)
							{
								for (int j = -2; j < 2; j++)
								{
									// Creates a platform
									
									if (par1World.getBlockId(x1 + x + i, height, z1 + z + j) == 0)
									{
										GCUtil.checkAndSetBlock(par1World, x1 + x + i, height, z1 + z + j, GCBlocks.creeperDungeonWall.blockID, 0, false, this.getBoundingBox());
									}

									if (height > 20)
									{
										// Sets the corners of the platforms to air, to make them appear more natural
										GCUtil.checkAndSetBlock(par1World, x1 + x - 2, height, z1 + z - 2, 0, 0, false, this.getBoundingBox());
										GCUtil.checkAndSetBlock(par1World, x1 + x + 1, height, z1 + z - 2, 0, 0, false, this.getBoundingBox());
			                    		GCUtil.checkAndSetBlock(par1World, x1 + x - 2, height, z1 + z + 1, 0, 0, false, this.getBoundingBox());
			                    		GCUtil.checkAndSetBlock(par1World, x1 + x + 1, height, z1 + z + 1, 0, 0, false, this.getBoundingBox());
									}
		                    		
		                    		if (par2Random.nextInt(5) == 0 && par1World.getBlockId(x1 + x + i, height + 1, z1 + z + j) == 0 && par1World.getBlockId(x1 + x + i, height, z1 + z + j) == GCBlocks.creeperDungeonWall.blockID)
		                    		{
		                    			// Creates random creeper eggs
		                    			
		                    			GCUtil.checkAndSetBlock(par1World, x1 + x + i, height + 1, z1 + z + j, GCBlocks.creeperEgg.blockID, 0, false, this.getBoundingBox());
		                    		}
								}
							}
							
							if (par2Random.nextInt(7) == 0)
							{
								// Sets mob spawner with creeper egg on top of it.
								
								if (x > 0 && x < 7 && z > 0 && z < 7)
								{
									if (GCUtil.checkAndSetBlock(par1World, x1 + x, height, z1 + z, Block.mobSpawner.blockID, 0, true, this.getBoundingBox()))
									{
										if (par1World.getBlockId(x1 + x, height + 1, z1 + z) == 0)
										{
											GCUtil.checkAndSetBlock(par1World, x1 + x, height + 1, z1 + z, GCBlocks.creeperEgg.blockID, 0, false, this.getBoundingBox());
										}
										
						                TileEntityMobSpawner var7 = (TileEntityMobSpawner)par1World.getBlockTileEntity(x1 + x, height, z1 + z);

						                if (var7 != null)
						                {
						                    var7.setMobID("Evolved Creeper");
						                }
									}
								}
							}
						}
					}
				}
			}
		}
		
		this.fillWithAir(par1World, par3StructureBoundingBox, x1, y1, z1, x2, y1 + 5, z2);
		
		return true;
    }
	
	public void fillBlocks2(World world, StructureBoundingBox bb, int x1, int y1, int z1, int x2, int y2, int z2, int blockID, int metadata)
	{
		for (int var12 = y1; var12 <= y2; ++var12)
        {
            for (int var13 = x1; var13 <= x2; ++var13)
            {
                for (int var14 = z1; var14 <= z2; ++var14)
                {
                    if (var12 > 20 && var12 != y1 && var12 != y2 && var13 != x1 && var13 != x2 && var14 != y1 && var14 != y2)
                    {
                    	if (bb.isVecInside(var13, var12, var14))
                        {
                			world.setBlockAndMetadata(var13, var12, var14, blockID, metadata);
                        }
                    }
                    else if (var12 > 20)
                    {
                    	if (bb.isVecInside(var13, var12, var14))
                        {
                			world.setBlockAndMetadata(var13, var12, var14, blockID, metadata);
                        }
                    }
                    else if (var12 == 20 && blockID == GCBlocks.creeperDungeonWall.blockID)
                    {
                    	if (bb.isVecInside(var13, var12, var14))
                        {
                			world.setBlockAndMetadata(var13, var12, var14, blockID, metadata);
                        }
                    }
                }
            }
        }
	}
	
	public int getGroungHeightAt(World world, int x, int z)
	{
		for (int i = 0; i < world.getActualHeight(); i++)
		{
			if (world.canBlockSeeTheSky(x, i, z))
			{
				return i;
			}
		}
		
		return -1;
	}
}
