package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBreathableAir;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockBreathableAir extends BlockContainer
{
	public GCBlockBreathableAir(int id, int texIndex)
	{
		super(id, texIndex, Material.air);
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
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
	public int getRenderBlockPass()
    {
        return GCCoreConfigManager.transparentBreathableAir ? 1 : 0;
    }
	
    @Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par1IBlockAccess.getBlockId(par2, par3, par4) == this.blockID)
        {
            return false;
        }
        else
        {
        	final int i = par1IBlockAccess.getBlockId(par2, par3, par4);
        	boolean var6 = false;
        	if (Block.blocksList[i] != null)
        	{
                var6 = !Block.blocksList[i].isOpaqueCube();
        	}
        	final boolean var7 = i == 0;

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
