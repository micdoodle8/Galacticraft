package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockLandingPad extends Block
{
	public GCCoreBlockLandingPad(int i, int j) 
	{
		super(i, j, Material.iron);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
	}

//    @Override
//    public void onBlockAdded(World par1World, int par2, int par3, int par4) 
//    {
//    	int amountOfCorrectBlocks = 0;
//    	
//    	for (int i = -2; i < 3; i++)
//		{
//			for (int j = -2; j < 3; j++)
//    		{
//				if (par1World.getBlockId(par2 + i, par3, par4 + j) == GCCoreBlocks.landingPad.blockID)
//				{
//					amountOfCorrectBlocks++;
//				}
//    		}
//		}
//    	
//    	if (amountOfCorrectBlocks == 9)
//    	{
//			int id;
//			Block block;
//			
//			id = par1World.getBlockId(par2 + 1, par3, par4);
//			
//			block = Block.blocksList[id];
//			
//			if (block != null)
//			{
//		        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9F, 1.0F);
//			}
//
//			id = par1World.getBlockId(par2 - 1, par3, par4);
//			
//			block = Block.blocksList[id];
//			
//			if (block != null)
//			{
//		        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9F, 1.0F);
//			}
//
//			id = par1World.getBlockId(par2, par3, par4 + 1);
//			
//			block = Block.blocksList[id];
//			
//			if (block != null)
//			{
//		        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9F, 1.0F);
//			}
//
//			id = par1World.getBlockId(par2, par3, par4 - 1);
//			
//			block = Block.blocksList[id];
//			
//			if (block != null)
//			{
//		        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9F, 1.0F);
//			}
//    	}
//    	
//    	super.onBlockAdded(par1World, par2, par3, par4);
//    }

    @Override
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
    	final int id = GCCoreBlocks.landingPad.blockID;

		if (par1World.getBlockId(par2 + 1, par3, par4) == id && par1World.getBlockId(par2 + 2, par3, par4) == id && par1World.getBlockId(par2 + 3, par3, par4) == id)
		{
			return false;
		}

		if (par1World.getBlockId(par2 - 1, par3, par4) == id && par1World.getBlockId(par2 - 2, par3, par4) == id && par1World.getBlockId(par2 - 3, par3, par4) == id)
		{
			return false;
		}

		if (par1World.getBlockId(par2, par3, par4 + 1) == id && par1World.getBlockId(par2, par3, par4 + 2) == id && par1World.getBlockId(par2, par3, par4 + 3) == id)
		{
			return false;
		}

		if (par1World.getBlockId(par2, par3, par4 - 1) == id && par1World.getBlockId(par2, par3, par4 - 2) == id && par1World.getBlockId(par2, par3, par4 - 3) == id)
		{
			return false;
		}
    	
    	if (par1World.getBlockId(par2, par3 - 1, par4) == GCCoreBlocks.landingPad.blockID && par5 == 1)
    	{
    		return false;
    	}
    	else
    	{
            return this.canPlaceBlockAt(par1World, par2, par3, par4);
    	}
    }
	
    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
