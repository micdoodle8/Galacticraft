package micdoodle8.mods.galacticraft.moon.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCMoonItem extends Item
{
	private String iconName;
	
	public GCMoonItem(int par1, String iconName)
	{
		super(par1);
		this.iconName = iconName;
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		this.iconIndex = iconRegister.func_94245_a("galacticraftmoon:" + this.iconName);
	}

	@Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftMoon.galacticraftMoonTab;
    }
}
