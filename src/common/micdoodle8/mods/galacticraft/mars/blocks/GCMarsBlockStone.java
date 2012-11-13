package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsBlockStone extends Block
{
	public GCMarsBlockStone(int par1, int par2) 
	{
		super(par1, par2, Material.rock);
	}

	@Override
	public int idDropped(int i, Random random, int par3) 
	{
		return GCMarsBlocks.marsCobblestone.blockID;
	}
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mars/client/blocks/mars.png";
	}
}
