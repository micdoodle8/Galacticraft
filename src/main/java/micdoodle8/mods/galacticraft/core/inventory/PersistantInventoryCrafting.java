package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class PersistantInventoryCrafting extends InventoryCrafting
{
    /**
     * List of the stacks in the crafting matrix.
     */
    private ItemStack[] stackList;

    /**
     * the width of the crafting inventory
     */
    private int inventoryWidth;
    private int inventoryHeight;

    /**
     * Class containing the callbacks for the events on_GUIClosed and
     * on_CraftMaxtrixChanged.
     */
    public Container eventHandler;

    public PersistantInventoryCrafting()
    {
        super(null, 3, 3);
        int k = 9;
        this.stackList = new ItemStack[k];
        this.inventoryWidth = 3;
        this.inventoryHeight = 3;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        return this.stackList.length;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? null : this.stackList[par1];
    }

    /**
     * Returns the itemstack in the slot specified (Top left is 0, 0). Args:
     * row, column
     */
    @Override
    public ItemStack getStackInRowAndColumn(int par1, int par2)
    {
        if (par1 >= 0 && par1 < this.inventoryWidth && par2 >= 0 && par2 < this.inventoryHeight)
        {
            int k = par1 + par2 * this.inventoryWidth;
            return this.getStackInSlot(k);
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the name of the inventory.
     */
    @Override
    public String getName()
    {
        return "container.crafting";
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized
     * name, and translated into the player's language. Otherwise it will be
     * used directly.
     */
    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    /**
     * When some containers are closed they call this on each slot, then drop
     * whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     */
    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (this.stackList[par1] != null)
        {
            ItemStack itemstack = this.stackList[par1];
            this.stackList[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.stackList[par1] != null)
        {
            ItemStack itemstack;

            if (this.stackList[par1].stackSize <= par2)
            {
                itemstack = this.stackList[par1];
                this.stackList[par1] = null;

                if (this.eventHandler != null)
                {
                    this.eventHandler.onCraftMatrixChanged(this);
                }

                return itemstack;
            }
            else
            {
                itemstack = this.stackList[par1].splitStack(par2);

                if (this.stackList[par1].stackSize == 0)
                {
                    this.stackList[par1] = null;
                }

                if (this.eventHandler != null)
                {
                    this.eventHandler.onCraftMatrixChanged(this);
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the crafting inventory
     * Updates recipe matching in the containing machine.
     */
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.stackList[par1] = par2ItemStack;

        if (this.eventHandler != null)
        {
            this.eventHandler.onCraftMatrixChanged(this);
        }
    }

    /**
     * Sets the given item stack to the specified slot in the crafting inventory.
     * No update to the containing machine.
     */
    public void setInventorySlotContentsNoUpdate(int par1, ItemStack par2ItemStack)
    {
        this.stackList[par1] = par2ItemStack;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    @Override
    public void markDirty()
    {
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
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

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
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
        return null;
    }
}
