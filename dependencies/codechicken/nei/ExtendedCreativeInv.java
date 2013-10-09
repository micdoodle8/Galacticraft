package codechicken.nei;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ExtendedCreativeInv implements IInventory
{
    PlayerSave playerSave;
    Side side;
    public ExtendedCreativeInv(PlayerSave playerSave, Side side)
    {
        this.playerSave = playerSave;
        this.side = side;
    }

    @Override
    public int getSizeInventory()
    {
        return 54;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if(side.isClient())
            return NEIClientConfig.creativeInv[slot];
        return playerSave.creativeInv[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        ItemStack item = getStackInSlot(slot);        
        
        if(item != null)
        {
            if(item.stackSize <= size)
            {
                ItemStack itemstack = item;
                setInventorySlotContents(slot, null);
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = item.splitStack(size);
            if(item.stackSize == 0)
            {
                setInventorySlotContents(slot, null);
            }
            onInventoryChanged();
            return itemstack1;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        synchronized(this)
        {
            ItemStack stack = getStackInSlot(slot);
            setInventorySlotContents(slot, null);
            return stack;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        if(side.isClient())
            NEIClientConfig.creativeInv[slot] = stack;
        else 
            playerSave.creativeInv[slot] = stack;
        
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Extended Creative";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onInventoryChanged()
    {
        if(side.isServer())
            playerSave.setCreativeDirty();
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
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

}
