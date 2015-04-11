package micdoodle8.mods.galacticraft.core.energy.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileBaseElectricBlockWithInventory extends TileBaseElectricBlock implements IInventory
{
    public ItemStack[] readStandardItemsFromNBT(NBTTagCompound nbt)
    {
        final NBTTagList var2 = nbt.getTagList("Items", 10);
        int length = this.getSizeInventory();
        ItemStack[] result = new ItemStack[length];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < length)
            {
                result[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        return result;
    }

    public void writeStandardItemsToNBT(NBTTagCompound nbt)
    {
        final NBTTagList list = new NBTTagList();
        int length = this.getSizeInventory();
        ItemStack containingItems[] = this.getContainingItems();

        for (int var3 = 0; var3 < length; ++var3)
        {
            if (containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        nbt.setTag("Items", list);
    }

    @Override
    public int getSizeInventory()
    {
        return this.getContainingItems().length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.getContainingItems()[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        ItemStack containingItems[] = this.getContainingItems();
        if (containingItems[par1] != null)
        {
            ItemStack var3;

            if (containingItems[par1].stackSize <= par2)
            {
                var3 = containingItems[par1];
                containingItems[par1] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = containingItems[par1].splitStack(par2);

                if (containingItems[par1].stackSize == 0)
                {
                    containingItems[par1] = null;
                }

                this.markDirty();
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        ItemStack containingItems[] = this.getContainingItems();
        if (containingItems[par1] != null)
        {
            final ItemStack var2 = containingItems[par1];
            containingItems[par1] = null;
            this.markDirty();
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        ItemStack containingItems[] = this.getContainingItems();
        containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation((this.getBlockMetadata() & 3) + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    /*
     * Must be overridden by identical code, to get the actual containingItems
     */
    abstract protected ItemStack[] getContainingItems();
}
