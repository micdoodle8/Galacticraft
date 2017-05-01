package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventorySchematicTier2Rocket implements IInventoryDefaults
{
    private final NonNullList<ItemStack> stacks;
    private final int inventoryWidth;
    private final Container eventHandler;

    public InventorySchematicTier2Rocket(Container par1Container)
    {
        this.stacks = NonNullList.withSize(22, ItemStack.EMPTY);
        this.eventHandler = par1Container;
        this.inventoryWidth = 5;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? ItemStack.EMPTY : this.stacks.get(par1);
    }

    @Override
    public String getName()
    {
        return "container.crafting";
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (!this.stacks.get(par1).isEmpty())
        {
            ItemStack var2 = this.stacks.get(par1);
            this.stacks.set(par1, ItemStack.EMPTY);
            return var2;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (!this.stacks.get(par1).isEmpty())
        {
            ItemStack var3;

            if (this.stacks.get(par1).getCount() <= par2)
            {
                var3 = this.stacks.get(par1);
                this.stacks.set(par1, ItemStack.EMPTY);
                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
            else
            {
                var3 = this.stacks.get(par1).splitStack(par2);

                if (this.stacks.get(par1).isEmpty())
                {
                    this.stacks.set(par1, ItemStack.EMPTY);
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.stacks.set(par1, par2ItemStack);
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
}
