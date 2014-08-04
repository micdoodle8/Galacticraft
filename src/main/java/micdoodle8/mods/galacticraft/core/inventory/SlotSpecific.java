package micdoodle8.mods.galacticraft.core.inventory;

import mekanism.api.energy.IEnergizedItem;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates a slot with a specific amount of items that matches the slot's
 * requirements. Allows easy shift right clicking management and slot blocking
 * in classes. In your container you can use this.getSlot(i).isItemValid to
 * justify the player's shift clicking actions to match the slot.
 * 
 * @author Calclavia
 * 
 */
public class SlotSpecific extends Slot
{
	public ItemStack[] validItemStacks = new ItemStack[0];
	@SuppressWarnings("rawtypes")
	public Class[] validClasses = new Class[0];

	public boolean isInverted = false;
	public boolean isMetadataSensitive = false;

	public SlotSpecific(IInventory par2IInventory, int par3, int par4, int par5, ItemStack... itemStacks)
	{
		super(par2IInventory, par3, par4, par5);
		this.setItemStacks(itemStacks);
	}

	@SuppressWarnings("rawtypes")
	public SlotSpecific(IInventory par2IInventory, int par3, int par4, int par5, Class... validClasses)
	{
		super(par2IInventory, par3, par4, par5);
		if (validClasses != null && Arrays.asList(validClasses).contains(ItemElectric.class))
		{
			if (NetworkConfigHandler.isIndustrialCraft2Loaded())
			{
				try {
					Class<?> itemElectricIC2a = Class.forName("ic2.api.item.IElectricItem");
					Class<?> itemElectricIC2b = Class.forName("ic2.api.item.ISpecialElectricItem");
					ArrayList<Class> existing = new ArrayList(Arrays.asList(validClasses));
					existing.add(itemElectricIC2a);
					existing.add(itemElectricIC2b);
					validClasses = existing.toArray(new Class[existing.size()]);
				} catch (Exception e) { e.printStackTrace(); }
			}
			if (NetworkConfigHandler.isMekanismLoaded())
			{
				try {
					Class<?> itemElectricMek = Class.forName("mekanism.api.energy.IEnergizedItem");
					ArrayList<Class> existing = new ArrayList(Arrays.asList(validClasses));
					existing.add(itemElectricMek);
					validClasses = existing.toArray(new Class[existing.size()]);
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
		this.setClasses(validClasses);
	}

	public SlotSpecific setMetadataSensitive()
	{
		this.isMetadataSensitive = true;
		return this;
	}

	public SlotSpecific setItemStacks(ItemStack... validItemStacks)
	{
		this.validItemStacks = validItemStacks;
		return this;
	}

	@SuppressWarnings("rawtypes")
	public SlotSpecific setClasses(Class... validClasses)
	{
		this.validClasses = validClasses;
		return this;
	}

	public SlotSpecific toggleInverted()
	{
		this.isInverted = !this.isInverted;
		return this;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean isItemValid(ItemStack compareStack)
	{
		boolean returnValue = false;

		for (ItemStack itemStack : this.validItemStacks)
		{
			if (compareStack.isItemEqual(itemStack) || !this.isMetadataSensitive && compareStack == itemStack)
			{
				returnValue = true;
				break;
			}
		}

		if (!returnValue)
		{
			for (Class clazz : this.validClasses)
			{
				if (clazz.isInstance(compareStack.getItem()))
				{
					returnValue = true;
					break;
				}
			}
		}

		if (this.isInverted)
		{
			return !returnValue;
		}

		return returnValue;
	}
}
