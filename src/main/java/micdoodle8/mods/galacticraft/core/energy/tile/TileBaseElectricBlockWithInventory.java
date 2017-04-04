package micdoodle8.mods.galacticraft.core.energy.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public abstract class TileBaseElectricBlockWithInventory extends TileBaseElectricBlock implements IInventory
{
    public NonNullList<ItemStack> readStandardItemsFromNBT(NBTTagCompound nbt)
    {
        NonNullList<ItemStack> stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, stacks);
        return stacks;
    }

    public void writeStandardItemsToNBT(NBTTagCompound nbt, NonNullList<ItemStack> stacks)
    {
        ItemStackHelper.saveAllItems(nbt, stacks);
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.getContainingItems())
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return this.getContainingItems().size();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.getContainingItems().get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.getContainingItems(), index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.getContainingItems(), index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.getContainingItems().set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.getWorld().getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.getHorizontal(((this.getBlockMetadata() & 3) + 1) % 4);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    /*
     * Must be overridden by identical code, to get the actual containingItems
     */
    abstract protected NonNullList<ItemStack> getContainingItems();

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

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
}
