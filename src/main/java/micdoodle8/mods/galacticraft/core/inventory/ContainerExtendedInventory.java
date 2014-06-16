package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;



public class ContainerExtendedInventory extends Container
{
	public InventoryPlayer inventoryPlayer;
	public InventoryExtended extendedInventory;

	public ContainerExtendedInventory(EntityPlayer thePlayer, InventoryExtended extendedInventory)
	{
		this.inventoryPlayer = thePlayer.inventory;
		this.extendedInventory = extendedInventory;

		int i;
		int j;

		for (i = 0; i < 4; ++i)
		{
			this.addSlotToContainer(new SlotArmorGC(thePlayer, thePlayer.inventory, thePlayer.inventory.getSizeInventory() - 1 - i, 61, 8 + i * 18, i));
		}

		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(thePlayer.inventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(thePlayer.inventory, i, 8 + i * 18, 142));
		}

		this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 0, 106 + 19, 17));
		this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 1, 106 + 19, 35));
		this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 2, 106 + 19 - 9, 53));
		this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 3, 106 + 19 + 9, 53));
		this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 4, 124 + 19, 17));
		this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 5, 106 + 1, 17));

		for (i = 0; i < 4; ++i)
		{
			this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 6 + i, 79, 8 + i * 18));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		return null;
	}
}
