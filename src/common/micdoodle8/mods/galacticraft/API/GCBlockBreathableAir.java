package micdoodle8.mods.galacticraft.API;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreBlock;
import micdoodle8.mods.galacticraft.core.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.GCCoreTileEntityOxygenDistributor;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockBreathableAir extends GCCoreBlock
{
	public GCBlockBreathableAir(int id, int texIndex) 
	{
		super(id, texIndex, Material.air);
        this.setTickRandomly(true);
	}

	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}

	@Override
	public boolean isCollidable() 
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) 
	{
		return null;
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) 
	{
		return false;
	}

	@Override
    public int tickRate()
    {
        return 30;
    }

	@Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		FMLLog.info("" + activeDistributorNearby(par1World, par2, par3, par4, false));
		
		if (!activeDistributorNearby(par1World, par2, par3, par4, false))
		{
			return;
		}
		else
		{
			for (int j = -1; j < 2; j++)
			{
				for (int i = -1; i < 2; i++)
				{
					for (int k = -1; k < 2; k++)
					{
						if (par1World.isAirBlock(par2 + i, par3 + j, par4 + k))
						{
							par1World.setBlockWithNotify(par2 + i, par3 + j, par4 + k, this.blockID);
						}
					}
				}
			}
		}
//		
//		
//		if (par1World.isAirBlock(par2 + 1, par3, par4))
//		{
//			par1World.setBlockWithNotify(par2 + 1, par3, par4, this.blockID);
//		}
//		
//		if (par1World.isAirBlock(par2 - 1, par3, par4))
//		{
//			par1World.setBlockWithNotify(par2 - 1, par3, par4, this.blockID);
//		}
//		
//		if (par1World.isAirBlock(par2, par3, par4 + 1))
//		{
//			par1World.setBlockWithNotify(par2, par3, par4 + 1, this.blockID);
//		}
//		
//		if (par1World.isAirBlock(par2, par3, par4 - 1))
//		{
//			par1World.setBlockWithNotify(par2, par3, par4 - 1, this.blockID);
//		}
//		
//		if (par1World.isAirBlock(par2, par3 + 1, par4))
//		{
//			par1World.setBlockWithNotify(par2, par3 + 1, par4, this.blockID);
//		}
//		
//		if (par1World.isAirBlock(par2, par3 - 1, par4))
//		{
//			par1World.setBlockWithNotify(par2, par3 - 1, par4, this.blockID);
//		}
    }

    public int getRenderBlockPass()
    {
        return 1;
    }
	
	public static boolean activeDistributorNearby(World world, int x, int y, int z, boolean extraBlock)
	{
//		if (!extraBlock)
		{
			for (int j = -8; j <= 8; j++)
			{
				for (int i = -8; i < 8; i++)
				{
					for (int k = -8; k < 8; k++)
					{
						TileEntity tile = world.getBlockTileEntity(x + i, y + j, z + k);

						if (tile != null && tile instanceof GCCoreTileEntityOxygenDistributor)
						{
							int power = Math.min((int) Math.floor(((GCCoreTileEntityOxygenDistributor)tile).currentPower / 3), 8);
							
							for (int j2 = -power; j2 <= power; j2++)
							{
								for (int i2 = -power; i2 <= power; i2++)
								{
									for (int k2 = -power; k2 <= power; k2++)
									{
										if (world.getBlockId(x + i2, y + j2, z + k2) == GCCoreBlocks.airDistributorActive.blockID)
										{
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par1IBlockAccess.getBlockId(par2, par3, par4) == this.blockID)
        {
            return false;
        }
        else
        {
        	int i = par1IBlockAccess.getBlockId(par2, par3, par4);
        	boolean var6 = false;
        	if (Block.blocksList[i] != null)
        	{
                var6 = !Block.blocksList[i].isOpaqueCube();
        	}
        	boolean var7 = i == 0;

            if ((var6 || var7) && par5 == 3 && !var6)
            {
            	return true;
            }
            else if ((var6 || var7) && par5 == 4 && !var6)
            {
            	return true;
            }
            else if ((var6 || var7) && par5 == 5 && !var6)
            {
            	return true;
            }
            else if ((var6 || var7) && par5 == 2 && !var6)
            {
            	return true;
            }
            else if ((var6 || var7) && par5 == 0 && !var6)
            {
            	return true;
            }
            else if ((var6 || var7) && par5 == 1 && !var6)
            {
            	return true;
            }
            else
            {
            	return false;
            }
        }
    }
}
