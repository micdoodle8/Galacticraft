package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GCCoreContainerRocketRefill extends Container
{
    private IInventory lowerChestInventory;
    private IInventory spaceshipInv;
    private int type;

    public GCCoreContainerRocketRefill(IInventory par1IInventory, IInventory par2IInventory, int type)
    {
        this.lowerChestInventory = par1IInventory;
        this.spaceshipInv = par2IInventory;
        this.type = type;
        par2IInventory.openChest();
        
        switch (type)
        {
        case 0:
        	this.addSlotsForType1();
        	break;
        case 1:
        	this.addSlotsForType2();
        	break;
        }
    }
    
    private void addSlotsForType1()
    {
        int var4;
        int var5;

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(lowerChestInventory, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18 - 34));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(lowerChestInventory, var4, 8 + var4 * 18, 142 - 34));
        }

        this.addSlotToContainer(new Slot(this.spaceshipInv, 27, 80, 18));
    }
    
    private void addSlotsForType2()
    {
        int var4;
        int var5;
        
        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
            	if ((var4 == 2 && var5 == 8) || (var4 == 2  && var5 == 7) || (var4 == 2  && var5 == 6))
            	{
            	}
            	else
            	{
                    this.addSlotToContainer(new Slot(this.spaceshipInv, var5 + var4 * 9, 8 + var5 * 18, 50 + var4 * 18));
            	}
            }
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(this.lowerChestInventory, var5 + var4 * 9 + 9, 8 + var5 * 18, 103 + var4 * 18 + 14));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(this.lowerChestInventory, var4, 8 + var4 * 18, 161 + 14));
        }

        this.addSlotToContainer(new Slot(this.spaceshipInv, 27, 80, 18));
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.lowerChestInventory.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 < 27)
            {
                if (!this.mergeItemStack(var5, 27, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 27, false))
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
        }

        return var3;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);
        this.lowerChestInventory.closeChest();
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getLowerChestInventory()
    {
        return this.lowerChestInventory;
    }
}
