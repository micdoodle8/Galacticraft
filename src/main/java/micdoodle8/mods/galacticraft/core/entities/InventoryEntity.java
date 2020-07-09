package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public abstract class InventoryEntity extends NetworkedEntity implements IInventory
{
    protected NonNullList<ItemStack> stacks = NonNullList.withSize(0, ItemStack.EMPTY);

    public InventoryEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        ItemStackHelper.loadAllItems(nbt, this.stacks);
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt)
    {
        ItemStackHelper.saveAllItems(nbt, this.stacks);
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return this.stacks.get(var1);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.stacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
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
    public boolean isUsableByPlayer(PlayerEntity entityplayer)
    {
        return true;
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
        for (int i = 0; i < this.stacks.size(); ++i)
        {
            this.stacks.set(i, ItemStack.EMPTY);
        }
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }
}
