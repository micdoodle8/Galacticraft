package micdoodle8.mods.galacticraft.mars.wgen;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.wgen.GCCoreStructureComponent;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsComponentCreeperPitRoom extends GCCoreStructureComponent
{
	public int corridorCount;
	public int originalFourCorridorLength;
	public int bossEntryCorridor;
	public int bossEntryCount;
    private int averageGroundLevel = -1;
    private final int height;
    private final int width;
	
    public GCMarsComponentCreeperPitRoom(int type, World world, Random par2Random, int x, int y, int z, int height, int width, int cbm)
    {
        super(type);
        this.setCoordBaseMode(cbm);
        this.height = height;
        this.width = width;
        this.boundingBox = GCCoreStructureComponent.getComponentToAddBoundingBox(x, 78 - this.height, z, 0, 0, 0, 7, this.height, 7, cbm);
    }
    
    @Override
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) 
    {
        int var4;
        
        for (var4 = 0; var4 < 4; ++var4)
        {
            final int[] var5 = this.getValidOpening(par3Random, var4);
            
            this.makeCorridor(par2List, par3Random, 1, var5[0], var5[1], var5[2], this.width, 7, var4);
        }
    }
    
    public int[] getValidOpening(Random var1, int var2)
    {
    	if (var2 == 0)
    	{
    		return new int[] {this.width - 1, 0, 1};
    	}
    	else if (var2 == 1)
    	{
    		return new int[] {1, 0, this.width - 1};
    	}
    	else if (var2 == 2)
    	{
    		return new int[] {0, 0, 1};
    	}
    	else if (var2 == 3)
    	{
    		return new int[] {1, 0, 0};
    	}
    	
    	return new int[] {0, 0, 0};
    }
    
    public boolean makeCorridor(List list, Random random, int type, int x, int y, int z, int width, int height, int cbm)
    {
    	final int var10 = (this.getCoordBaseMode() + cbm) % 4;
        final int[] var11 = this.offsetCorridorCoords(x, y, z, width, var10);
        
    	final GCMarsComponentCreeperPitCorridor var12 = new GCMarsComponentCreeperPitCorridor(10 + var10, var11[0], var11[1], var11[2], height, 7, 30, var10);//new GCComponentCreeperPitCorridor(var3, var11[0], var11[1], var11[2], var7, var8, var10);

    	if (var12 != null)
    	{
            list.add(var12);
            var12.buildComponent(this, list, random);
    	}
        return true;
    }
    
    protected int[] offsetCorridorCoords(int x, int y, int z, int width, int cbm)
    {
        final int var6 = this.getXWithOffset(x, z);
        final int var7 = this.getYWithOffset(y);
        final int var8 = this.getZWithOffset(x, z);
        return cbm == 0 ? new int[] {var6 + 1, var7 - 1, var8 - width / 2}: cbm == 1 ? new int[] {var6 + width / 2, var7 - 1, var8 + 1}: cbm == 2 ? new int[] {var6 - 1, var7 - 1, var8 + width / 2}: cbm == 3 ? new int[] {var6 - width / 2, var7 - 1, var8 - 1}: new int[] {x, y, z};
    }

    @Override
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 3, 0);
        }

    	this.makeWallsDown(par1World);
    	this.makePlatforms(par1World, par2Random);
    	
		return true;
    }
    
    public void makeWallsDown(World world)
    {
    	for (int y = 0; y < this.height; y++)
    	{
    		for (int x = 0; x < 7; x++)
    		{
    			for (int z = 0; z < 7; z++)
    			{
    				if ((x == 0 || x == 6 || z == 0 || z == 6) && (y == 0 || y > 7))
    				{
    					this.placeBlockAtCurrentPosition(world, GCMarsBlocks.creeperDungeonWall.blockID, 0, x, y, z, this.getBoundingBox());
    				}
    				else
    				{
    					this.placeBlockAtCurrentPosition(world, 0, 0, x, y, z, this.getBoundingBox());
    				}
    			}
    		}
    	}
    }
    
    public void makePlatforms(World world, Random rand)
    {
    	for (int y = this.height - 1; y > 10; y--)
    	{
    		for (int x = 0; x < this.width; x++)
    		{
    			for (int z = 0; z < this.width; z++)
    			{
    				if (y % 4 == 0 && rand.nextInt(20) == 0)
					{
						if (world.getBlockId(this.getBoundingBox().minX + x, this.getBoundingBox().minY + y, this.getBoundingBox().minZ + z) == GCMarsBlocks.creeperDungeonWall.blockID);
						{
							for (int i = -2; i < 2; i++)
							{
								for (int j = -2; j < 2; j++)
								{
									// Creates a platform
									
									if (world.getBlockId(this.getBoundingBox().minX + x + i, this.getBoundingBox().minY + y, this.getBoundingBox().minZ + z + j) == 0)
									{
										this.placeBlockAtCurrentPosition(world, GCMarsBlocks.creeperDungeonWall.blockID, 0, x + i, y, z + j, this.getBoundingBox());
									}

									if (y > 10)
									{
										// Sets the corners of the platforms to air, to make them appear more natural
										this.placeBlockAtCurrentPosition(world, 0, 0, x - 2, y, z - 2, this.getBoundingBox());
										this.placeBlockAtCurrentPosition(world, 0, 0, x + 1, y, z - 2, this.getBoundingBox());
										this.placeBlockAtCurrentPosition(world, 0, 0, x - 2, y, z + 1, this.getBoundingBox());
										this.placeBlockAtCurrentPosition(world, 0, 0, x + 1, y, z + 1, this.getBoundingBox());
									}
		                    		
		                    		if (rand.nextInt(5) == 0 && world.getBlockId(this.getBoundingBox().minX + x + i, this.getBoundingBox().minY + y + 1, this.getBoundingBox().minZ + z + j) == 0 && world.getBlockId(this.getBoundingBox().minX + x + i, this.getBoundingBox().minY + y, this.getBoundingBox().minZ + z + j) == GCMarsBlocks.creeperDungeonWall.blockID)
		                    		{
		                    			// Creates random creeper eggs

										this.placeBlockAtCurrentPosition(world, GCMarsBlocks.creeperEgg.blockID, 0, x + i, y + 1, z + j, this.getBoundingBox());
		                    		}
								}
							}
							
							if (rand.nextInt(7) == 0)
							{
								// Sets mob spawner with creeper egg on top of it.
								
								if (x > 0 && x < 7 && z > 0 && z < 7)
								{
									this.placeBlockAtCurrentPosition(world, Block.mobSpawner.blockID, 0, x, y + 1, z, this.getBoundingBox());
									
									if (world.getBlockId(this.getBoundingBox().minX + x, this.getBoundingBox().minY + y + 1, this.getBoundingBox().minZ + z) == 0)
									{
										this.placeBlockAtCurrentPosition(world, GCMarsBlocks.creeperEgg.blockID, 0, x, y + 2, z, this.getBoundingBox());
									}
									
					                final TileEntityMobSpawner var7 = (TileEntityMobSpawner)world.getBlockTileEntity(this.getBoundingBox().minX + x, this.getBoundingBox().minY + y + 1, this.getBoundingBox().minZ + z);

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
    
    protected int getAverageGroundLevel(World par1World, StructureBoundingBox par2StructureBoundingBox)
    {
        int var3 = 0;
        int var4 = 0;

        for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
        {
            for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6)
            {
                if (par2StructureBoundingBox.isVecInside(var6, 64, var5))
                {
                    var3 += Math.max(par1World.getTopSolidOrLiquidBlock(var6, var5), par1World.provider.getAverageGroundLevel());
                    ++var4;
                }
            }
        }

        if (var4 == 0)
        {
            return -1;
        }
        else
        {
            return var3 / var4;
        }
    }
}
