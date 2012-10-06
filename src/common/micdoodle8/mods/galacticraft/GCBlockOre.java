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
public class GCBlockOre extends Block
{
	private boolean glowing;

	public GCBlockOre(int par1, int par2, boolean glowing) 
	{
		this(par1, par2);

		if (glowing) 
		{
			this.setTickRandomly(true);
		}

		this.glowing = glowing;
	}
	
	public GCBlockOre(int i, int j) 
	{
		super(i, j, Material.rock);
	}

	@Override
	public int idDropped(int i, Random random, int par3) 
	{
		if (this.blockID == GCBlocks.marsOreDesh.blockID)
		{
//			return Items.rawDesh.shiftedIndex;
		}

		return this.blockID;
	}
	
	@Override
	public String getTextureFile()
	{
		if (this.blockID != GCConfigManager.idBlockPowerCrystal)
		{
			return "/micdoodle8/mods/galacticraft/client/blocks/mars.png";
		}
		else
		{
			return "/micdoodle8/mods/galacticraft/client/blocks/core.png";
		}
	}
}
