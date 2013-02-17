package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.Item;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsItem extends Item
{
	public GCMarsItem(int par1)
	{
		super(par1);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mars/client/items/mars.png";
	}
}
