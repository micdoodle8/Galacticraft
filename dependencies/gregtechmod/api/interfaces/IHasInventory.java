package gregtechmod.api.interfaces;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public interface IHasInventory extends ISidedInventory, IHasWorldObjectAndCoords {
	
	/**
	 * if the Inventory of this TileEntity got modified this tick
	 */
	public boolean hasInventoryBeenModified();

	/**
	 * if this is just a Holoslot
	 */
	public boolean isValidSlot(int aIndex);
	
	/**
	 * Tries to add a Stack to the Slot.
	 * It doesn't matter if the Slot is valid or invalid as described at the Function above.
	 * 
	 * @return true if aStack == null, then false if aIndex is out of bounds, then false if aStack cannot be added, and then true if aStack has been added
	 */
	public boolean addStackToSlot(int aIndex, ItemStack aStack);
	
	/**
	 * Tries to add X Items of a Stack to the Slot.
	 * It doesn't matter if the Slot is valid or invalid as described at the Function above.
	 * 
	 * @return true if aStack == null, then false if aIndex is out of bounds, then false if aStack cannot be added, and then true if aStack has been added
	 */
	public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount);
}