package gregtechmod.api.gui;

import gregtechmod.api.GregTech_API;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Slot_DataOrb extends Slot {
	public GT_Slot_DataOrb(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
    public boolean isItemValid(ItemStack aStack) {
        return aStack != null && aStack.itemID == GregTech_API.getGregTechItem(43, 1, 0).itemID;
    }
}