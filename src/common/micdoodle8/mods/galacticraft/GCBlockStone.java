package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockStone extends Block
{
	public GCBlockStone(int par1, int par2) 
	{
		super(par1, par2, Material.rock);
	}

	@Override
	public int idDropped(int i, Random random, int par3) 
	{
		return GCBlocks.marsCobblestone.blockID;
	}
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/client/blocks/mars.png";
	}
}
