package gregtechmod.api.gui;

import gregtechmod.api.enums.GT_Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Slot_DataOrb extends Slot {
	public GT_Slot_DataOrb(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
    @Override
	public boolean isItemValid(ItemStack aStack) {
        return GT_Items.Tool_DataOrb.isStackEqual(aStack, true, true);
    }
}