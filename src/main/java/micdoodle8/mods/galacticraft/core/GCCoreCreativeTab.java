package micdoodle8.mods.galacticraft.core;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	private final Item itemForTab;
	private final int metaForTab;

	public GCCoreCreativeTab(int par1, String par2Str, Item itemForTab, int metaForTab)
	{
		super(par1, par2Str);
		this.itemForTab = itemForTab;
		this.metaForTab = metaForTab;
	}

	public GCCoreCreativeTab(int par1, String par2Str, Block itemForTab, int metaForTab)
	{
		this(par1, par2Str, Item.getItemFromBlock(itemForTab), metaForTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() 
	{
		return this.itemForTab;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public int func_151243_f()
    {
        return this.metaForTab;
    }
}
