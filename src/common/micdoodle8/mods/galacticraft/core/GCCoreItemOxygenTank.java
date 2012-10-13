package micdoodle8.mods.galacticraft.core;

import java.util.List;

import micdoodle8.mods.galacticraft.mars.GCMarsItem;
import net.minecraft.src.ItemStack;

public class GCCoreItemOxygenTank extends GCMarsItem
{
	public GCCoreItemOxygenTank(int par1) 
	{
		super(par1);
	}

	@Override
    public void addInformation(ItemStack par1ItemStack, List par2List) 
	{
		par2List.add("Air Remaining: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
	}
}
