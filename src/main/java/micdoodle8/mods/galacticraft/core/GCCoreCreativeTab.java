package micdoodle8.mods.galacticraft.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * GCCoreCreativeTab.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreCreativeTab extends CreativeTabs
{
	private final int itemForTab;
	private final int metaForTab;

	public GCCoreCreativeTab(int par1, String par2Str, int itemForTab, int metaForTab)
	{
		super(par1, par2Str);
		this.itemForTab = itemForTab;
		this.metaForTab = metaForTab;
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(this.itemForTab, 1, this.metaForTab);
	}
}
