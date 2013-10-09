package gregtechmod.api.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Slot_Holo extends Slot {
	public boolean mCanInsertItem, mCanStackItem;
	public int mMaxStacksize = 127;
	
	public GT_Slot_Holo(IInventory par1iInventory, int par2, int par3, int par4, boolean aCanInsertItem, boolean aCanStackItem, int aMaxStacksize) {
		super(par1iInventory, par2, par3, par4);
		mCanInsertItem = aCanInsertItem;
		mCanStackItem = aCanStackItem;
		mMaxStacksize = aMaxStacksize;
	}
	
    public boolean isItemValid(ItemStack par1ItemStack) {
        return mCanInsertItem;
    }
    
    public int getSlotStackLimit() {
        return mMaxStacksize;
    }
    
    public boolean getHasStack() {
        return false;
    }
    
    public ItemStack decrStackSize(int par1) {
    	if (!mCanStackItem) return null;
        return super.decrStackSize(par1);
    }
}
