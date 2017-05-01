package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public abstract class InventoryEntity extends NetworkedEntity implements IInventoryDefaults
{
    public ItemStack[] containedItems = new ItemStack[0];

    public InventoryEntity(World par1World)
    {
        super(par1World);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        NBTTagList itemList = nbt.getTagList("Items", 10);
        this.containedItems = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < itemList.tagCount(); ++i)
        {
            NBTTagCompound itemTag = itemList.getCompoundTagAt(i);
            int slotID = itemTag.getByte("Slot") & 255;

            if (slotID >= 0 && slotID < this.containedItems.length)
            {
                this.containedItems[slotID] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < this.containedItems.length; ++i)
        {
            if (this.containedItems[i] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) i);
                this.containedItems[i].writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }

        nbt.setTag("Items", itemList);
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return this.containedItems[var1];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount)
    {
        if (this.containedItems[slotIndex] != null)
        {
            ItemStack var3;

            if (this.containedItems[slotIndex].stackSize <= amount)
            {
                var3 = this.containedItems[slotIndex];
                this.containedItems[slotIndex] = null;
                return var3;
            }
            else
            {
                var3 = this.containedItems[slotIndex].splitStack(amount);

                if (this.containedItems[slotIndex].stackSize == 0)
                {
                    this.containedItems[slotIndex] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex)
    {
        if (this.containedItems[slotIndex] != null)
        {
            ItemStack stack = this.containedItems[slotIndex];
            this.containedItems[slotIndex] = null;
            return stack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack stack)
    {
        this.containedItems[slotIndex] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }
}
