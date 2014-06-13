package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;



public class SlotExtendedInventory extends Slot
{
	public SlotExtendedInventory(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		switch (this.getSlotIndex())
		{
		case 0:
			return itemstack.getItem() instanceof ItemOxygenMask;
		case 1:
			return itemstack.getItem() == GCItems.oxygenGear;
		case 2:
			return itemstack.getItem() instanceof ItemOxygenTank;
		case 3:
			return itemstack.getItem() instanceof ItemOxygenTank;
		case 4:
			return itemstack.getItem() instanceof ItemParaChute;
		case 5:
			return itemstack.getItem() == GCItems.basicItem && itemstack.getItemDamage() == 19;
		case 6:
			return thermalArmorSlotValid(itemstack, 0);
		case 7:
			return thermalArmorSlotValid(itemstack, 1);
		case 8:
			return thermalArmorSlotValid(itemstack, 2);
		case 9:
			return thermalArmorSlotValid(itemstack, 3);
		}

		return super.isItemValid(itemstack);
	}
	
	public boolean thermalArmorSlotValid(ItemStack stack, int slotIndex)
	{
		if (Loader.isModLoaded("GalacticraftMars"))
		{
			try
			{
				Class<?> clazz = Class.forName("micdoodle8.mods.galacticraft.planets.asteroids.items.ItemThermalPadding");
				
				if (clazz.isInstance(stack.getItem()))
				{
					return stack.getItemDamage() == slotIndex;
				}				
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
		return false;
	}
}
