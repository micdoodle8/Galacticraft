package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class GCCoreContainerRefinery extends Container
{
	private GCCoreTileEntityRefinery refinery;
	
    public GCCoreContainerRefinery(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityRefinery refinery)
    {
    	this.refinery = refinery;
        this.addSlotToContainer(new Slot(refinery, 0, 40, 29));
        this.addSlotToContainer(new Slot(refinery, 1, 72, 61));
        this.addSlotToContainer(new Slot(refinery, 2, 90, 61));
        this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, refinery, 3, 130, 8));
        
        int var6;
        int var7;
        
        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 142));
        }
    }
    
	@Override
	public boolean canInteractWith(EntityPlayer var1) 
	{
		return true;
	}

    @Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        final Slot var3 = (Slot)this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            final ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 == 0)
            {
                if (!this.mergeItemStack(var4, 11, 46, true))
                {
                    return null;
                }

                var3.onSlotChange(var4, var2);
            }
            else if (par1 >= 10 && par1 < 37)
            {
                if (!this.mergeItemStack(var4, 37, 46, false))
                {
                    return null;
                }
            }
            else if (par1 >= 37 && par1 < 46)
            {
                if (!this.mergeItemStack(var4, 11, 37, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 11, 46, false))
            {
                return null;
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.onPickupFromSlot(par1EntityPlayer, var4);
        }

        return var2;
    }
}
