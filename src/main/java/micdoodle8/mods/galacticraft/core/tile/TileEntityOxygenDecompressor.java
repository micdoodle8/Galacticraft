package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterOxygenInfinite;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.EnumSet;

public class TileEntityOxygenDecompressor extends TileEntityOxygen implements IInventoryDefaults, ISidedInventory
{
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);

    public static final int OUTPUT_PER_TICK = 100;
    private boolean usingEnergy = false;

    public TileEntityOxygenDecompressor()
    {
        super(1200, 0);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            this.usingEnergy = false;
            ItemStack tank1 = this.stacks.get(0);

            if (!tank1.isEmpty() && this.hasEnoughEnergyToRun && this.getOxygenStored() < this.getMaxOxygenStored())
            {
                if (tank1.getItem() instanceof ItemOxygenTank && tank1.getItemDamage() < tank1.getMaxDamage())
                {
                    tank1.setItemDamage(tank1.getItemDamage() + 1);
                    this.receiveOxygen(1, true);
                    this.usingEnergy = true;
                }
                else if (tank1.getItem() instanceof ItemCanisterOxygenInfinite)
                {
                    this.receiveOxygen(1, true);
                    this.usingEnergy = true;
                }
            }

            this.produceOxygen();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.stacks);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        ItemStackHelper.saveAllItems(nbt, this.stacks);
        return nbt;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stacks.size();
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
        ItemStack oldstack = ItemStackHelper.getAndRemove(this.stacks, index);
        if (!oldstack.isEmpty())
        {
        	this.markDirty();
        }
    	return oldstack;
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
    public String getName()
    {
        return GCCoreUtil.translate("container.oxygendecompressor.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.world.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getItemDamage() < itemstack.getMaxDamage();
            case 1:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getItemDamage() == itemstack.getMaxDamage();
            case 1:
                return ItemElectricBase.isElectricItemEmpty(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            return itemstack.getItem() instanceof ItemOxygenTank;
        case 1:
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }

        return false;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.usingEnergy;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockOxygenCompressor)
        {
            return state.getValue(BlockOxygenCompressor.FACING).rotateY();
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(1);
    }

    public EnumFacing getOxygenOutputDirection()
    {
        return this.getElectricInputDirection().getOpposite();
    }

    @Override
    public EnumSet<EnumFacing> getOxygenInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getOxygenOutputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return false;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public int getOxygenProvide(EnumFacing direction)
    {
        return this.getOxygenOutputDirections().contains(direction) ? Math.min(TileEntityOxygenDecompressor.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }
}
