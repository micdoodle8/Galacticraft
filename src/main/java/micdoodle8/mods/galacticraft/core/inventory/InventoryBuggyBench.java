package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryBuggyBench implements IInventory
{
    private final NonNullList<ItemStack> stackList;
    private final int inventoryWidth;
    private final Container eventHandler;

    public InventoryBuggyBench(Container par1Container)
    {
        final int size = 32;
        this.stackList = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
        this.eventHandler = par1Container;
        this.inventoryWidth = 5;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stackList.size();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? ItemStack.EMPTY : this.stackList.get(par1);
    }

    public ItemStack getStackInRowAndColumn(int par1, int par2)
    {
        if (par1 >= 0 && par1 < this.inventoryWidth)
        {
            final int var3 = par1 + par2 * this.inventoryWidth;
            return this.getStackInSlot(var3);
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public String getName()
    {
        return "container.crafting";
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack oldstack = ItemStackHelper.getAndRemove(this.stackList, index);
        if (!oldstack.isEmpty())
        {
            this.markDirty();
            this.eventHandler.onCraftMatrixChanged(this);
        }
    	return oldstack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
            this.eventHandler.onCraftMatrixChanged(this);
        }

        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.stackList.set(index, stack);
        this.markDirty();
        this.eventHandler.onCraftMatrixChanged(this);
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
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stackList)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
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
    public void clear()
    {

    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
}
