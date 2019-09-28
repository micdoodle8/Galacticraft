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

public class InventoryRocketBench implements IInventory
{
    public NonNullList<ItemStack> stacks;
    private final int inventoryWidth;
    private final Container eventHandler;

    public InventoryRocketBench(Container par1Container)
    {
        final int size = 18;
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.eventHandler = par1Container;
        this.inventoryWidth = 5;
    }

    public ItemStack getStackInRowAndColumn(int par1, int par2)
    {
        if (par1 >= 0 && par1 < this.inventoryWidth)
        {
            final int var3 = par1 + par2 * this.inventoryWidth;
            if (var3 >= 18)
            {
                return ItemStack.EMPTY;
            }
            return this.getStackInSlot(var3);
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public int getSizeInventory()
    {
        return this.stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.stacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
            this.eventHandler.onCraftMatrixChanged(this);
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack oldstack = ItemStackHelper.getAndRemove(this.stacks, index);
        if (!oldstack.isEmpty())
        {
            this.markDirty();
            this.eventHandler.onCraftMatrixChanged(this);
        }
    	return oldstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.stacks.set(index, stack);
        this.markDirty();
        this.eventHandler.onCraftMatrixChanged(this);
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getName()
    {
        return "container.crafting";
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
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    //We don't use these because we use forge containers
    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void closeInventory(EntityPlayer player)
    {
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
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
}
