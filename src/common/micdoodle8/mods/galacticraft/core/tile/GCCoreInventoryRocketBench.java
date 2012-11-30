package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class GCCoreInventoryRocketBench implements IInventory
{
    private ItemStack[] stackList;
    private int inventoryWidth;
    private Container eventHandler;

    public GCCoreInventoryRocketBench(Container par1Container)
    {
        int var4 = 20;
        this.stackList = new ItemStack[var4];
        this.eventHandler = par1Container;
        this.inventoryWidth = 5;
    }

    @Override
	public int getSizeInventory()
    {
        return this.stackList.length;
    }

    @Override
	public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? null : this.stackList[par1];
    }

    public ItemStack getStackInRowAndColumn(int par1, int par2)
    {
        if (par1 >= 0 && par1 < this.inventoryWidth)
        {
            int var3 = par1 + par2 * this.inventoryWidth;
            return this.getStackInSlot(var3);
        }
        else
        {
            return null;
        }
    }

    @Override
	public String getInvName()
    {
        return "container.crafting";
    }

    @Override
	public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.stackList[par1] != null)
        {
            ItemStack var2 = this.stackList[par1];
            this.stackList[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
	public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.stackList[par1] != null)
        {
            ItemStack var3;

            if (this.stackList[par1].stackSize <= par2)
            {
                var3 = this.stackList[par1];
                this.stackList[par1] = null;
                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
            else
            {
                var3 = this.stackList[par1].splitStack(par2);

                if (this.stackList[par1].stackSize == 0)
                {
                    this.stackList[par1] = null;
                }

                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.stackList[par1] = par2ItemStack;
        this.eventHandler.onCraftMatrixChanged(this);
    }

    @Override
	public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
	public void onInventoryChanged() {}

    @Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
	public void openChest() {}

    @Override
	public void closeChest() {}
}