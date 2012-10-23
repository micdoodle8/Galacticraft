package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.CreativeTabs;

public class GCCoreCreativeTab extends CreativeTabs
{
	public GCCoreCreativeTab(int par1, String par2Str)
	{
		super(par1, par2Str);
	}
	
    public int getTabIconItemIndex()
    {
        return GCCoreItems.spaceship.shiftedIndex;
    }
}
