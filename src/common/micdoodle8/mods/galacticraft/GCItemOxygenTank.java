package micdoodle8.mods.galacticraft;

import java.util.List;

import net.minecraft.src.ItemStack;

public class GCItemOxygenTank extends GCItem
{
	public GCItemOxygenTank(int par1) 
	{
		super(par1);
	}

	@Override
    public void addInformation(ItemStack par1ItemStack, List par2List) 
	{
		par2List.add("Air Remaining: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
	}
}
