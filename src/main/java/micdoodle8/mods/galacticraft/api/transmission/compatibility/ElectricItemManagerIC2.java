package micdoodle8.mods.galacticraft.api.transmission.compatibility;

import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import ic2.api.item.IElectricItemManager;

/*
 * Interface between Galacticraft electric items (batteries) and IC2.
 * 
 * Galactricraft items implemented as Tier 1 items when recharging
 * and for discharging purposes (so can power anything)
 */
public class ElectricItemManagerIC2 implements IElectricItemManager
{

	@Override
	public int charge(ItemStack itemStack, int amount, int tier,
			boolean ignoreTransferLimit, boolean simulate)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			ItemElectric item = (ItemElectric)itemStack.getItem();
			float energy = amount * NetworkConfigHandler.IC2_RATIO;
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

			return (int) (energyToReceive * NetworkConfigHandler.TO_IC2_RATIO);
		}
		return 0;
	}

	@Override
	public int discharge(ItemStack itemStack, int amount, int tier,
			boolean ignoreTransferLimit, boolean simulate)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			ItemElectric item = (ItemElectric)itemStack.getItem();
			float energy = amount * NetworkConfigHandler.IC2_RATIO;
			float energyToTransfer = Math.min(item.getElectricityStored(itemStack), energy);
			if (!ignoreTransferLimit) energyToTransfer = Math.min(energyToTransfer, item.transferMax);
			
			if (!simulate)
			{
				item.setElectricity(itemStack, item.getElectricityStored(itemStack) - energyToTransfer);
			}
	
			return (int) (energyToTransfer * NetworkConfigHandler.TO_IC2_RATIO);
		}
		return 0;
	}

	@Override
	public int getCharge(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			ItemElectric item = (ItemElectric)itemStack.getItem();
			return (int) (item.getElectricityStored(itemStack) * NetworkConfigHandler.TO_IC2_RATIO);
		}
		return 0;
	}

	@Override
	public boolean canUse(ItemStack itemStack, int amount)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			return this.getCharge(itemStack) >= amount;
		}
		return false;
	}

	@Override
	public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity)
	{
		if (itemStack.getItem() instanceof ItemElectric)
		{
			return this.discharge(itemStack, amount, 1, true, false) >= amount - 1;
		}
		return false;
	}

	@Override
	public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity)
	{
	}

	@Override
	public String getToolTip(ItemStack itemStack)
	{
		return null;
	}

}
