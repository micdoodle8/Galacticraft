/*
 * (C) 2014-2017 Team CoFH / CoFH / Cult of the Full Hub
 * http://www.teamcofh.com
 */
package cofh.redstoneflux.impl;

import cofh.redstoneflux.util.EnergyContainerItemWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * This class is a simple extension of {@link ItemEnergyContainer}. It exposes the Forge Energy Capability via the {@link EnergyContainerItemWrapper} class.
 *
 * Use/extend this or implement your own. This particular implementation allows for an item to both send and receive energy.
 *
 * @author King Lemming
 */
public class ItemEnergyContainerCap extends ItemEnergyContainer {

	/* CAPABILITIES */
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

		return new EnergyContainerItemWrapper(stack, this);
	}

}
