package micdoodle8.mods.galacticraft.core;

import java.util.Random;

import micdoodle8.mods.galacticraft.mars.GCMarsBlock;
import micdoodle8.mods.galacticraft.mars.GCMarsBlocks;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockBreathableAir extends GCMarsBlock
{
	public GCBlockBreathableAir(int par1) 
	{
		super(par1, 0, Material.air);
        this.setTickRandomly(true);
        this.blockIndexInTexture = 21;
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
		if (!activeDistributorNearby(par1World, par2, par3, par4, false))
		{
			return;
		}
		
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
		
		if (par1World.isAirBlock(par2 + 1, par3, par4))
		{
			par1World.setBlockWithNotify(par2 + 1, par3, par4, this.blockID);
		}
		
		if (par1World.isAirBlock(par2 - 1, par3, par4))
		{
			par1World.setBlockWithNotify(par2 - 1, par3, par4, this.blockID);
		}
		
		if (par1World.isAirBlock(par2, par3, par4 + 1))
		{
			par1World.setBlockWithNotify(par2, par3, par4 + 1, this.blockID);
		}
		
		if (par1World.isAirBlock(par2, par3, par4 - 1))
		{
			par1World.setBlockWithNotify(par2, par3, par4 - 1, this.blockID);
		}
		
		if (par1World.isAirBlock(par2, par3 + 1, par4))
		{
			par1World.setBlockWithNotify(par2, par3 + 1, par4, this.blockID);
		}
		
		if (par1World.isAirBlock(par2, par3 - 1, par4))
		{
			par1World.setBlockWithNotify(par2, par3 - 1, par4, this.blockID);
		}
    }

    public int getRenderBlockPass()
    {
        return 1;
    }
	
	public static boolean activeDistributorNearby(World world, int x, int y, int z, boolean extraBlock)
	{
		if (!extraBlock)
		{
			for (int j = -4; j < 5; j++)
			{
				for (int i = -4; i < 5; i++)
				{
					for (int k = -4; k < 5; k++)
					{
						if (world.getBlockId(x + i, y + j, z + k) == GCMarsBlocks.airDistributorActive.blockID)
						{
							return true;
						}
					}
				}
			}
		}
		else
		{
			for (int j = -5; j < 6; j++)
			{
				for (int i = -5; i < 6; i++)
				{
					for (int k = -5; k < 6; k++)
					{
						if (world.getBlockId(x + i, y + j, z + k) == GCMarsBlocks.airDistributorActive.blockID)
						{
							return true;
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
