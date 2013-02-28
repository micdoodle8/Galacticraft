package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemOilCanister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

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
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 == 3)
            {
                if (!this.mergeItemStack(var5, 3, 39, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (par2 != 2 && par2 != 1 && par2 != 0)
            {
                if (var5.getItem() instanceof GCCoreItemOilCanister && (var5.getMaxDamage() - var5.getItemDamage()) > 0)
                {
                    if (!this.mergeItemStack(var5, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(var5))
                {
                    if (!this.mergeItemStack(var5, 1, 2, false) && !this.mergeItemStack(var5, 2, 3, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 4 && par2 < 30)
                {
                    if (!this.mergeItemStack(var5, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(var5, 4, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 4, 39, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }
}
