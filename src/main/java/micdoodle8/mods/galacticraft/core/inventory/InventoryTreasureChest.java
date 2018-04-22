package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryTreasureChest implements IInventory
{
    private String name;
    private IInventory upperChest;
    private IInventory lowerChest;

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


    @Override
    public String getName()
    {
        return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
    }

    @Override
    public boolean hasCustomName()
    {
        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return p_70301_1_ >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(p_70301_1_ - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        return p_70298_1_ >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(p_70298_1_ - this.upperChest.getSizeInventory(), p_70298_2_) : this.upperChest.decrStackSize(p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack removeStackFromSlot(int p_70304_1_)
    {
        return p_70304_1_ >= this.upperChest.getSizeInventory() ? this.lowerChest.removeStackFromSlot(p_70304_1_ - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
        if (p_70299_1_ >= this.upperChest.getSizeInventory())
        {
            this.lowerChest.setInventorySlotContents(p_70299_1_ - this.upperChest.getSizeInventory(), p_70299_2_);
        }
        else
        {
            this.upperChest.setInventorySlotContents(p_70299_1_, p_70299_2_);
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
    public boolean isUsableByPlayer(EntityPlayer p_70300_1_)
    {
        return this.upperChest.isUsableByPlayer(p_70300_1_) && this.lowerChest.isUsableByPlayer(p_70300_1_);
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        this.upperChest.openInventory(player);
        this.lowerChest.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        this.upperChest.closeInventory(player);
        this.lowerChest.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
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

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
}
