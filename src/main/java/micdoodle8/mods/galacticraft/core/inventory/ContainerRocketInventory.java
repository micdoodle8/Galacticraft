package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRocketInventory extends Container
{
    private final IInventory lowerChestInventory;
    private final IInventory spaceshipInv;
    private final EnumRocketType rocketType;

    public ContainerRocketInventory(IInventory par1IInventory, IInventory par2IInventory, EnumRocketType rocketType, EntityPlayer player)
    {
        this.lowerChestInventory = par1IInventory;
        this.spaceshipInv = par2IInventory;
        this.rocketType = rocketType;
        par2IInventory.openInventory(player);

        switch (rocketType.getInventorySpace() - 2)
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
        int lastRow = slotCount / 9;
        int ySize = 145 + (this.rocketType.getInventorySpace() - 2) * 2;

        for (var4 = 0; var4 < lastRow; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(this.spaceshipInv, var5 + var4 * 9, 8 + var5 * 18, 50 + var4 * 18));
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
        return this.spaceshipInv.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot var4 = this.inventorySlots.get(par2);
        final int b = this.inventorySlots.size() - 36;

        if (var4 != null && var4.getHasStack())
        {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 < b)
            {
                if (!this.mergeItemStack(var5, b, b + 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(var5, 0, b, false))
            {
                return ItemStack.EMPTY;
            }

            if (var5.getCount() == 0)
            {
                var4.putStack(ItemStack.EMPTY);
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
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.lowerChestInventory.closeInventory(par1EntityPlayer);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getLowerChestInventory()
    {
        return this.lowerChestInventory;
    }
}
