package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TileEntityOxygenStorageModule extends TileEntityOxygen implements IInventoryDefaults, ISidedInventory
{
    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    public int scaledOxygenLevel;
    private int lastScaledOxygenLevel;

    public static final int OUTPUT_PER_TICK = 500;
    public static final int OXYGEN_CAPACITY = 60000;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityOxygenStorageModule()
    {
        super(OXYGEN_CAPACITY, 40);
        this.storage.setCapacity(0);
        this.storage.setMaxExtract(0);
    }

    @Override
    public void update()
    {
        if (!this.world.isRemote)
        {
            ItemStack oxygenItemStack = this.getStackInSlot(0);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(this.oxygenPerTick * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }
        }

        super.update();

        this.scaledOxygenLevel = this.getScaledOxygenLevel(16);

        if (this.scaledOxygenLevel != this.lastScaledOxygenLevel)
        {
            this.world.notifyLightSet(this.getPos());
        }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;

        this.produceOxygen(getFront().rotateY().getOpposite());

        // if (!this.world.isRemote)
        // {
        // int gasToSend = Math.min(this.storedOxygen,
        // GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK);
        // GasStack toSend = new GasStack(GalacticraftCore.gasOxygen,
        // gasToSend);
        // this.storedOxygen -= GasTransmission.emitGasToNetwork(toSend, this,
        // this.getOxygenOutputDirection());
        //
        // Vector3 thisVec = new Vector3(this);
        // TileEntity tileEntity =
        // thisVec.modifyPositionFromSide(this.getOxygenOutputDirection()).getTileEntity(this.world);
        //
        // if (tileEntity instanceof IGasAcceptor)
        // {
        // if (((IGasAcceptor)
        // tileEntity).canReceiveGas(this.getOxygenInputDirection(),
        // GalacticraftCore.gasOxygen))
        // {
        // double sendingGas = 0;
        //
        // if (this.storedOxygen >=
        // GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK)
        // {
        // sendingGas = GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK;
        // }
        // else
        // {
        // sendingGas = this.storedOxygen;
        // }
        //
        // this.storedOxygen -= sendingGas - ((IGasAcceptor)
        // tileEntity).receiveGas(new GasStack(GalacticraftCore.gasOxygen, (int)
        // Math.floor(sendingGas)));
        // }
        // }
        // }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;
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
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return null;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public int getOxygenProvide(EnumFacing direction)
    {
        return this.getOxygenOutputDirections().contains(direction) ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(this.getPos());
        if (state.getBlock() != GCBlocks.machineBase2)
        {
            return EnumFacing.NORTH;
        }
        return this.world.getBlockState(getPos()).getValue(BlockMachine2.FACING);
    }

    @Override
    public EnumSet<EnumFacing> getOxygenInputDirections()
    {
        return EnumSet.of(getFront().rotateY());
    }

    @Override
    public EnumSet<EnumFacing> getOxygenOutputDirections()
    {
        return EnumSet.of(getFront().rotateY().getOpposite());
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
        return GCCoreUtil.translate("tile.machine2.6.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.world.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && itemstack != null && itemstack.getItem() instanceof IItemOxygenSupply;
    }

    //ISidedInventory
    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 0 && this.isItemValidForSlot(slotID, itemstack))
        {
            return itemstack.getItemDamage() < itemstack.getItem().getMaxDamage();
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 0 && itemstack != null)
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        return false;
    }

    //IFluidHandler methods - to allow this to accept Liquid Oxygen
    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return super.canDrain(from, fluid);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return super.drain(from, resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return super.drain(from, maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
//        if (from.ordinal() == this.getBlockMetadata() - BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 2 && GalacticraftCore.isPlanetsLoaded)
//        {
//            //Can fill with LOX only
//            return fluid != null && fluid.getName().equals(AsteroidsModule.fluidLiquidOxygen.getName());
//        }

        return super.canFill(from, fluid);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
//        int used = 0;
//
//        if (resource != null && this.canFill(from, resource.getFluid()))
//        {
//            used = (int) (this.receiveOxygen((int) Math.floor(resource.amount / Constants.LOX_GAS_RATIO), doFill) * Constants.LOX_GAS_RATIO);
//        }

        return super.fill(from, resource, doFill);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
//        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
//        int metaside = this.getBlockMetadata() - BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 2;
//        int side = from.ordinal();
//
//        if (metaside == side && GalacticraftCore.isPlanetsLoaded)
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(new FluidStack(AsteroidsModule.fluidLiquidOxygen, (int) (this.getOxygenStored() * Constants.LOX_GAS_RATIO)), (int) (OXYGEN_CAPACITY * Constants.LOX_GAS_RATIO)) };
//        }
//        return tankInfo;
        return super.getTankInfo(from);
    }
}
