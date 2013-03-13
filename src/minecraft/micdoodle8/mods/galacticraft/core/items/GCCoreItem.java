package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreItem extends Item
{
	private String iconName;
	
	public GCCoreItem(int par1, String iconName)
	{
		super(par1);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
		this.iconName = iconName;
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void func_94581_a(IconRegister par1IconRegister)
    {
        this.iconIndex = par1IconRegister.func_94245_a("galacticraftcore:" + this.iconName);
    }
}
