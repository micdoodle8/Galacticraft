package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineTiered;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TileEntityEnergyStorageModule extends TileBaseUniversalElectricalSource implements IPacketReceiver, ISidedInventory, IConnector
{
    private final static float BASE_CAPACITY = 500000;
    private final static float TIER2_CAPACITY = 2500000;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);

    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    public int scaledEnergyLevel;
    public int lastScaledEnergyLevel;
    private float lastEnergy = 0;

    private boolean initialised = false;

    public TileEntityEnergyStorageModule()
    {
        this(1);
    }

    /*
     * @param tier: 1 = Electric Furnace  2 = Electric Arc Furnace
     */
    public TileEntityEnergyStorageModule(int tier)
    {
        this.initialised = true;
        if (tier == 1)
        {
            //Designed so that Tier 1 Energy Storage can power up to 10 Tier 1 machines
            this.storage.setCapacity(BASE_CAPACITY);
            this.storage.setMaxExtract(300);
            return;
        }

        this.setTier2();
    }

    private void setTier2()
    {
        this.storage.setCapacity(TIER2_CAPACITY);
        this.storage.setMaxExtract(1800);
        this.setTierGC(2);
    }

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            int metadata = this.getBlockMetadata();

            //for version update compatibility
            Block b = this.world.getBlockState(this.getPos()).getBlock();
            if (b == GCBlocks.machineBase)
            {
                this.world.setBlockState(this.getPos(), GCBlocks.machineTiered.getDefaultState(), 2);
            }
            else if (metadata >= 8)
            {
                this.setTier2();
            }
            this.initialised = true;
        }

        float energy = this.storage.getEnergyStoredGC();
        if (this.getTierGC() == 1 && !this.world.isRemote)
        {
            if (this.lastEnergy - energy > this.storage.getMaxExtract() - 1)
            {
                //Deplete faster if being drained at maximum output
                this.storage.extractEnergyGC(25, false);
            }
        }
        this.lastEnergy = energy;

        super.update();

        this.scaledEnergyLevel = (int) Math.floor((this.getEnergyStoredGC() + 49) * 16 / this.getMaxEnergyStoredGC());

        if (this.scaledEnergyLevel != this.lastScaledEnergyLevel)
        {
            this.world.notifyLightSet(this.getPos());
        }

        if (!this.world.isRemote)
        {
            this.recharge(this.stacks.get(0));
            this.discharge(this.stacks.get(1));
        }

        if (!this.world.isRemote)
        {
            this.produce();
        }

        this.lastScaledEnergyLevel = this.scaledEnergyLevel;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.setTier2();
            this.initialised = true;
        }
        else
        {
            this.initialised = false;
        }

        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.stacks);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (this.tierGC == 1 && this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.storage.setEnergyStored(BASE_CAPACITY);
        }

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
    public String getName()
    {
        return GCCoreUtil.translate(this.tierGC == 1 ? "tile.machine.1.name" : "tile.machine.8.name");
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

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }


    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
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
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return ItemElectricBase.isElectricItem(itemstack.getItem());
    }

//    @Override
//    public int[] getAccessibleSlotsFromSide(int slotID)
//    {
//        return new int[] { 0, 1 };
//    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (itemstack.getItem() instanceof ItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((ItemElectricBase) itemstack.getItem()).getTransfer(itemstack) > 0;
            }
            else if (slotID == 1)
            {
                return ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (itemstack.getItem() instanceof ItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((ItemElectricBase) itemstack.getItem()).getTransfer(itemstack) <= 0;
            }
            else if (slotID == 1)
            {
                return ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || this.getEnergyStoredGC() >= this.getMaxEnergyStoredGC();
            }
        }

        return false;

    }

    public EnumFacing getFront()
    {
        if (getBlockType() instanceof BlockMachine)
        {
            return (this.world.getBlockState(getPos()).getValue(BlockMachine.FACING));
        }
        else if (getBlockType() instanceof BlockMachineTiered)
        {
            return (this.world.getBlockState(getPos()).getValue(BlockMachineTiered.FACING));
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.of(getFront().rotateY().getOpposite());
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.of(getFront().rotateY());
    }

    @Override
    public EnumFacing getElectricalOutputDirectionMain()
    {
        return getFront().rotateY();
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return getElectricalInputDirections().contains(direction) || getElectricalOutputDirections().contains(direction);
    }
}
