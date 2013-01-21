package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.creativetab.CreativeTabs;

public class GCCoreCreativeTab extends CreativeTabs
{
	public GCCoreCreativeTab(int par1, String par2Str)
	{
		super(par1, par2Str);
	}
	
    @Override
	public int getTabIconItemIndex()
    {
        return GCCoreItems.spaceship.itemID;
    }
}
