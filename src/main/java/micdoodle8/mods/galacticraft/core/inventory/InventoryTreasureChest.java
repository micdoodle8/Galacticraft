package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryTreasureChest implements IInventory
{
    private final String name;
    private final IInventory upperChest;
    private final IInventory lowerChest;

    public InventoryTreasureChest(String name, IInventory upper, IInventory lower)
    {
        this.name = name;

        if (upper == null)
        {
            upper = lower;
        }

        if (lower == null)
        {
            lower = upper;
        }

        this.upperChest = upper;
        this.lowerChest = lower;
    }

    @Override
    public int getSizeInventory()
    {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }


//    @Override
//    public String getName()
//    {
//        return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
//    }
//
//    @Override
//    public boolean hasCustomName()
//    {
//        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
//    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(slot - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int count)
    {
        return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(slot - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(slot, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.removeStackFromSlot(slot - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        if (slot >= this.upperChest.getSizeInventory())
        {
            this.lowerChest.setInventorySlotContents(slot - this.upperChest.getSizeInventory(), stack);
        }
        else
        {
            this.upperChest.setInventorySlotContents(slot, stack);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return this.upperChest.getInventoryStackLimit();
    }

    @Override
    public void markDirty()
    {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity p_70300_1_)
    {
        return this.upperChest.isUsableByPlayer(p_70300_1_) && this.lowerChest.isUsableByPlayer(p_70300_1_);
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        this.upperChest.openInventory(player);
        this.lowerChest.openInventory(player);
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        this.upperChest.closeInventory(player);
        this.lowerChest.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public void clear()
    {
        this.upperChest.clear();
        this.lowerChest.clear();
    }

    @Override
    public boolean isEmpty()
    {
        return this.upperChest.isEmpty() && this.lowerChest.isEmpty();
    }

//    @Override
//    public int getField(int id)
//    {
//        return 0;
//    }
//
//    @Override
//    public void setField(int id, int value)
//    {
//    }

//    @Override
//    public int getFieldCount()
//    {
//        return 0;
//    }

//    @Override
//    public ITextComponent getDisplayName()
//    {
//        return this.hasCustomName() ? new StringTextComponent(this.getName()) : new TranslationTextComponent(this.getName(), new Object[0]);
//    }
}
