package micdoodle8.mods.galacticraft.moon.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonBlockStone extends Block
{
	public GCMoonBlockStone(int par1, int par2)
	{
		super(par1, par2, Material.rock);
	}
	
	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/moon/client/blocks/moon.png";
	}
}
