package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.item.ItemReed;

public class GCMoonItemReed extends ItemReed
{
	public GCMoonItemReed(int par1, Block par2Block)
	{
		super(par1, par2Block);
		this.setMaxStackSize(1);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}
}
