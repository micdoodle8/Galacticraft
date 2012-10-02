package micdoodle8.mods.galacticraft;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCItem extends Item
{
	public GCItem(int par1) 
	{
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/client/items/core.png";
	}
}
