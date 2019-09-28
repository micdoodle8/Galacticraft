package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.HashMap;

public abstract class TileEntityInventory extends TileEntity implements ISidedInventory
{
    public NonNullList<ItemStack> inventory;
    private HashMap<EnumFacing, IItemHandler> itemHandlers = new HashMap<>();
    private String tileName;

    public TileEntityInventory(String tileName)
    {
        this.tileName = tileName;
    }

    public NonNullList<ItemStack> getInventory()
    {
        return inventory;
    }

    protected boolean handleInventory()
    {
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : getInventory())
        {
            if (!stack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        super.readFromNBT(tags);

        if (handleInventory())
        {
            NonNullList<ItemStack> stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tags, stacks);
            inventory = stacks;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);

        if (handleInventory())
        {
            ItemStackHelper.saveAllItems(tags, getInventory());
        }

        return tags;
    }

    @Override
    public int getSizeInventory()
    {
        return getInventory() == null ? 0 : getInventory().size();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return getInventory() == null ? ItemStack.EMPTY : getInventory().get(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        if (getInventory() == null)
        {
            return ItemStack.EMPTY;
        }

        return ItemStackHelper.getAndSplit(getInventory(), slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        if (getInventory() == null)
        {
            return ItemStack.EMPTY;
        }

        return ItemStackHelper.getAndRemove(getInventory(), slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        getInventory().set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return !isInvalid() && this.world.isBlockLoaded(this.pos);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side)
    {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing side)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing side)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            IItemHandler handler = itemHandlers.get(side);
            if (handler == null)
            {
                handler = new SidedInvWrapper(this, side);
                itemHandlers.put(side, handler);
            }
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler);
        }

        return super.getCapability(capability, side);
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
        for (int i = 0; i < this.getInventory().size(); ++i)
        {
            this.getInventory().set(i, ItemStack.EMPTY);
        }
    }

    /**
     * Override this and return true IF the inventory .getName() is
     * ALREADY a localized name e.g. by GCCoreUtil.translate()
     *
     **/
    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate(tileName);
    }
    
    @Override
    public synchronized void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
}
