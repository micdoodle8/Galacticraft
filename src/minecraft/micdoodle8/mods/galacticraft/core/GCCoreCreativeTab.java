package micdoodle8.mods.galacticraft.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreCreativeTab extends CreativeTabs
{
	private int itemForTab;
	private int metaForTab;
	
	public GCCoreCreativeTab(int par1, String par2Str, int itemForTab, int metaForTab)
	{
		super(par1, par2Str);
		this.itemForTab = itemForTab;
		this.metaForTab = metaForTab;
	}
    
    public ItemStack getIconItemStack()
    {
        return new ItemStack(this.itemForTab, 1, this.metaForTab);
    }
}
