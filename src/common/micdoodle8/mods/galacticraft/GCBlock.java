package micdoodle8.mods.galacticraft;

import java.util.ArrayList;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlock extends Block
{
	protected GCBlock(int i, int j, Material material) 
	{
		super(i, j, material);
	}

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/client/blocks/mars.png";
    }
}
