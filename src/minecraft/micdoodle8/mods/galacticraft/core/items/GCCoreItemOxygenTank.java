package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GCCoreItemOxygenTank extends GCCoreItem
{
	public EnumOxygenTankTier tier;
	
	public GCCoreItemOxygenTank(int par1) 
	{
		super(par1);
	}
	
	public GCCoreItemOxygenTank setTankTier(EnumOxygenTankTier tier)
	{
		this.tier = tier;
		return this;
	}

	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b) 
	{
		par2List.add("Air Remaining: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
	}
}
