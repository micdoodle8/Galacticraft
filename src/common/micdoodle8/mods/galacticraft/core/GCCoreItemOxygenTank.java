package micdoodle8.mods.galacticraft.core;

import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public class GCCoreItemOxygenTank extends GCCoreItem
{
	public GCCoreItemOxygenTank(int par1) 
	{
		super(par1);
	}

	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b) 
	{
		par2List.add("Air Remaining: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
	}
}
