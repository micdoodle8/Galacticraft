package micdoodle8.mods.galacticraft.api.transmission.compatibility;

import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/*
 * Interface between Galacticraft electric items (batteries) and IC2.
 * 
 * Galactricraft items implemented as Tier 1 items when recharging
 * and for discharging purposes (so can power anything)
 */
public class ElectricItemManagerIC2_1710
{
	@RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
	public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			ItemElectric item = (ItemElectric)itemStack.getItem();
			float energy = (float) amount * NetworkConfigHandler.IC2_RATIO;
			float rejectedElectricity = Math.max(item.getElectricityStored(itemStack) + energy - item.getMaxElectricityStored(itemStack), 0);
			float energyToReceive = energy - rejectedElectricity;
			if (!ignoreTransferLimit && energyToReceive > item.transferMax)
			{
				rejectedElectricity += energyToReceive - item.transferMax;
				energyToReceive = item.transferMax;
			}

			if (!simulate)
			{
				item.setElectricity(itemStack, item.getElectricityStored(itemStack) + energyToReceive);
			}

			return energyToReceive * NetworkConfigHandler.TO_IC2_RATIO;
		}
		return 0D;
	}

	@RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
	public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			ItemElectric item = (ItemElectric)itemStack.getItem();
			float energy = (float) amount * NetworkConfigHandler.IC2_RATIO;
			float energyToTransfer = Math.min(item.getElectricityStored(itemStack), energy);
			if (!ignoreTransferLimit) energyToTransfer = Math.min(energyToTransfer, item.transferMax);
			
			if (!simulate)
			{
				item.setElectricity(itemStack, item.getElectricityStored(itemStack) - energyToTransfer);
			}
	
			return energyToTransfer * NetworkConfigHandler.TO_IC2_RATIO;
		}
		return 0D;
	}

	@RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
	public double getCharge(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			ItemElectric item = (ItemElectric)itemStack.getItem();
			return item.getElectricityStored(itemStack) * NetworkConfigHandler.TO_IC2_RATIO;
		}
		return 0D;
	}

	@RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
	public boolean canUse(ItemStack itemStack, double amount)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			return this.getCharge(itemStack) >= amount;
		}
		return false;
	}

	@RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
	public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			return this.discharge(itemStack, amount, 1, true, false, false) >= amount - 1;
		}
		return false;
	}

	@RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
	public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity)
	{
	}

	@RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
	public String getToolTip(ItemStack itemStack)
	{
		return null;
	}

}
