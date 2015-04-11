package micdoodle8.mods.galacticraft.core.energy.item;

import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.miccore.Annotations;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/*
 * Interface between Galacticraft electric items (batteries) and IC2.
 * 
 * Galactricraft items implemented as Tier 1 items when recharging
 * and for discharging purposes (so can power anything)
 */
public class ElectricItemManagerIC2
{
    @Annotations.RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
    public int charge(ItemStack itemStack, int amount, int tier,
                      boolean ignoreTransferLimit, boolean simulate)
    {
        if (itemStack.getItem() instanceof ItemElectricBase)
        {
            ItemElectricBase item = (ItemElectricBase) itemStack.getItem();
            float energy = amount * EnergyConfigHandler.IC2_RATIO;
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

            return (int) (energyToReceive / EnergyConfigHandler.IC2_RATIO);
        }
        return 0;
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
    public int discharge(ItemStack itemStack, int amount, int tier,
                         boolean ignoreTransferLimit, boolean simulate)
    {
        if (itemStack.getItem() instanceof ItemElectricBase)
        {
            ItemElectricBase item = (ItemElectricBase) itemStack.getItem();
            float energy = amount / EnergyConfigHandler.TO_IC2_RATIO;
            float energyToTransfer = Math.min(item.getElectricityStored(itemStack), energy);
            if (!ignoreTransferLimit)
            {
                energyToTransfer = Math.min(energyToTransfer, item.transferMax);
            }

            if (!simulate)
            {
                item.setElectricity(itemStack, item.getElectricityStored(itemStack) - energyToTransfer);
            }

            return (int) (energyToTransfer * EnergyConfigHandler.TO_IC2_RATIO);
        }
        return 0;
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
    public int getCharge(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof ItemElectricBase)
        {
            ItemElectricBase item = (ItemElectricBase) itemStack.getItem();
            return (int) (item.getElectricityStored(itemStack) * EnergyConfigHandler.TO_IC2_RATIO);
        }
        return 0;
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
    public boolean canUse(ItemStack itemStack, int amount)
    {
        if (itemStack.getItem() instanceof ItemElectricBase)
        {
            return this.getCharge(itemStack) >= amount;
        }
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
    public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity)
    {
        if (itemStack.getItem() instanceof ItemElectricBase)
        {
            return this.discharge(itemStack, amount, 1, true, false) >= amount - 1;
        }
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity)
    {
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.item.IElectricItemManager", modID = "IC2")
    public String getToolTip(ItemStack itemStack)
    {
        return null;
    }

}
