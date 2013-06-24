package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase.EnumRocketType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GCCoreContainerRocketRefill extends Container
{
    private final IInventory lowerChestInventory;
    private final IInventory spaceshipInv;
    private final EnumRocketType rocketType;

    public GCCoreContainerRocketRefill(IInventory par1IInventory, IInventory par2IInventory, EnumRocketType rocketType)
    {
        this.lowerChestInventory = par1IInventory;
        this.spaceshipInv = par2IInventory;
        this.rocketType = rocketType;
        par2IInventory.openChest();

        switch (rocketType.getInventorySpace())
        {
        case 0:
            this.addSlotsNoInventory();
            break;
        case 18:
            this.addSlotsWithInventory(rocketType.getInventorySpace());
            break;
        case 36:
            this.addSlotsWithInventory(rocketType.getInventorySpace());
            break;
        case 54:
            this.addSlotsWithInventory(rocketType.getInventorySpace());
            break;
        }
    }

    private void addSlotsNoInventory()
    {
        int var4;
        int var5;

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(this.lowerChestInventory, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18 - 34));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(this.lowerChestInventory, var4, 8 + var4 * 18, 142 - 34));
        }
    }

    private void addSlotsWithInventory(int slotCount)
    {
        int var4;
        int var5;
        int lastRow = slotCount / 9 - 1;
        int ySize = 145 + rocketType.getInventorySpace() * 2;

        for (var4 = 0; var4 < lastRow + 1; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                if (var4 != lastRow || var5 < 6)
                {
                    int offset = 0;

                    if (var4 == lastRow)
                    {
                        offset = 28;
                    }

                    this.addSlotToContainer(new Slot(this.spaceshipInv, var5 + var4 * 9, 8 + var5 * 18 + offset, 50 + var4 * 18));
                }
            }
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(this.lowerChestInventory, var5 + var4 * 9 + 9, 8 + var5 * 18, ySize - 82 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(this.lowerChestInventory, var4, 8 + var4 * 18, ySize - 24));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.lowerChestInventory.isUseableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        final Slot var4 = (Slot) this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            final ItemStack var5 = var4.getStack();
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
                var4.putStack((ItemStack) null);
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
    @Override
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
