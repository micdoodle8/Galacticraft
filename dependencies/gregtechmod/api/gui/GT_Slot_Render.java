package gregtechmod.api.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class GT_Slot_Render extends GT_Slot_Holo {
	public GT_Slot_Render(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4, false, false, 0);
	}
	
	/**
	 * NEI has a nice and "useful" Delete-All Function, which would delete the Content of this Slot. This is here to prevent that.
	 */
    @Override
	public void putStack(ItemStack aStack) {
    	if (inventory instanceof TileEntity && ((TileEntity)inventory).worldObj.isRemote) {
            inventory.setInventorySlotContents(getSlotIndex(), aStack);
    	}
        onSlotChanged();
    }
}