package codechicken.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryNBT implements IInventory
{
    protected ItemStack[] items;
    protected NBTTagCompound tag;
    
    public InventoryNBT(int size, NBTTagCompound tag)
    {
        this.tag = tag;
        items = new ItemStack[size];
        readNBT();
    }
    
    private void writeNBT()
    {
        tag.setTag("items", InventoryUtils.writeItemStacksToTag(items, getInventoryStackLimit()));
    }
    
    private void readNBT()
    {
        if(tag.hasKey("items"))
            InventoryUtils.readItemStacksFromTag(items, tag.getTagList("items"));
    }
    
    @Override
    public int getSizeInventory()
    {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return items[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        return InventoryUtils.decrStackSize(this, slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return InventoryUtils.getStackInSlotOnClosing(this, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        items[slot] = stack;
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "NBT";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onInventoryChanged()
    {
        writeNBT();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }
}
