package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import java.util.HashMap;

public abstract class TileEntityInventory extends TileEntity implements ISidedInventory
{
    public NonNullList<ItemStack> inventory;
    private final HashMap<Direction, LazyOptional<IItemHandlerModifiable>> itemHandlers = new HashMap<>();

    public TileEntityInventory(TileEntityType<?> type)
    {
        super(type);
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
    public void read(CompoundNBT tags)
    {
        super.read(tags);

        if (handleInventory())
        {
            NonNullList<ItemStack> stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tags, stacks);
            inventory = stacks;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tags)
    {
        super.write(tags);

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
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return !isRemoved() && this.world.isBlockLoaded(this.pos);
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
    public boolean canInsertItem(int slot, ItemStack stack, Direction side)
    {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, Direction side)
    {
        return true;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side)
    {
        if (!this.removed && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (!this.itemHandlers.containsKey(side))
            {
                this.itemHandlers.put(side, LazyOptional.of(new NonNullSupplier<IItemHandlerModifiable>()
                {
                    @Nonnull
                    @Override
                    public IItemHandlerModifiable get()
                    {
                        return new SidedInvWrapper(TileEntityInventory.this, side);
                    }
                }));
            }
            else
            {
                return this.itemHandlers.get(side).cast();
            }
        }
        return super.getCapability(cap, side);
    }

    //We don't use these because we use forge containers
    @Override
    public void openInventory(PlayerEntity player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void closeInventory(PlayerEntity player)
    {
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < this.getInventory().size(); ++i)
        {
            this.getInventory().set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public synchronized void handleUpdateTag(CompoundNBT tag)
    {
        this.read(tag);
    }
}
