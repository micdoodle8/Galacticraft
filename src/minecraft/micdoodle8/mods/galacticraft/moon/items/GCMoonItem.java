package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.Item;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonItem extends Item
{
	public GCMoonItem(int par1) 
	{
		super(par1);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/moon/client/items/moon.png";
	}
}
