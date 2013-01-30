package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;

public class GCCoreSlotRocketBench extends Slot
{
    private final int index;

    public GCCoreSlotRocketBench(IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.index = par3;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
	public boolean isItemValid(ItemStack par1ItemStack)
    {
    	switch (this.index)
    	{
    	case 1:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketNoseCone.itemID ? true : false;
    	case 2:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 3:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 4:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 5:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 6:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 7:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 8:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 9:
    		return par1ItemStack.getItem().itemID == GCCoreItems.heavyPlating.itemID ? true : false;
    	case 10:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 11:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 12:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketEngine.itemID ? true : false;
    	case 13:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 14:
    		return par1ItemStack.getItem().itemID == GCCoreItems.rocketFins.itemID ? true : false;
    	case 15:
    		return true;
    	case 16:
    		return true;
    	case 17:
    		return true;
    	}
    	
    	return false;
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    @Override
	public int getSlotStackLimit()
    {
        return 64;
    }
}
