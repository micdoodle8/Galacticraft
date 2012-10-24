package micdoodle8.mods.galacticraft.API;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreTileEntityBreathableAir;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockBreathableAir extends BlockContainer
{
	public GCBlockBreathableAir(int id, int texIndex) 
	{
		super(id, texIndex, Material.air);
        this.setTickRandomly(true);
	}
	
	public void setMinYMaxY(double minY, double maxY)
	{
		this.minY = minY;
		this.maxY = maxY;
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

    public int getRenderBlockPass()
    {
        return 1;
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

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return new GCCoreTileEntityBreathableAir();
	}
    
	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
