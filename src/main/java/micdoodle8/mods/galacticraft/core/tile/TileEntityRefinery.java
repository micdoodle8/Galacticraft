package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.fluid.GCFluidRegistry;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class TileEntityRefinery extends TileBaseElectricBlockWithInventory implements ISidedInventory, IFluidHandlerWrapper
{
    private final int tankCapacity = 24000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank oilTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);

    public static final int PROCESS_TIME_REQUIRED = 2;
    public static final int OUTPUT_PER_SECOND = 1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;

    public TileEntityRefinery()
    {
        super("container.refinery.name");
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 60);
        this.oilTank.setFluid(new FluidStack(GCFluidRegistry.fluidOil, 0));
        this.fuelTank.setFluid(new FluidStack(GCFluidRegistry.fluidFuel, 0));
        this.inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            final FluidStack liquid = FluidUtil.getFluidContained(this.getInventory().get(1));
            if (FluidUtil.isOil(liquid))
            {
                FluidUtil.loadFromContainer(this.oilTank, GCFluidRegistry.fluidOil, this.getInventory(), 1, liquid.amount);
            }

            checkFluidTankTransfer(2, this.fuelTank);

            if (this.canProcess() && this.hasEnoughEnergyToRun)
            {
                if (this.processTicks == 0)
                {
                    this.processTicks = this.getProcessTimeRequired();
                }
                else
                {
                    if (--this.processTicks <= 0)
                    {
                        this.smeltItem();
                        this.processTicks = this.canProcess() ? this.getProcessTimeRequired() : 0;
                    }
                }
            }
            else
            {
                this.processTicks = 0;
            }
        }
    }
    
    private int getProcessTimeRequired()
    {
        return (this.poweredByTierGC > 1) ? 1 : TileEntityRefinery.PROCESS_TIME_REQUIRED;
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        FluidUtil.tryFillContainerFuel(tank, this.getInventory(), slot);
    }

    public int getScaledOilLevel(int i)
    {
        return this.oilTank.getFluidAmount() * i / this.oilTank.getCapacity();
    }

    public int getScaledFuelLevel(int i)
    {
        return this.fuelTank.getFluidAmount() * i / this.fuelTank.getCapacity();
    }

    public boolean canProcess()
    {
        if (this.oilTank.getFluidAmount() <= 0)
        {
            return false;
        }

        if (this.fuelTank.getFluidAmount() >= this.fuelTank.getCapacity())
        {
            return false;
        }

        return !this.getDisabled(0);

    }

    public void smeltItem()
    {
        if (this.canProcess())
        {
            final int oilAmount = this.oilTank.getFluidAmount();
            final int fuelSpace = this.fuelTank.getCapacity() - this.fuelTank.getFluidAmount();

            final int amountToDrain = Math.min(Math.min(oilAmount, fuelSpace), TileEntityRefinery.OUTPUT_PER_SECOND);

            this.oilTank.drain(amountToDrain, true);
            this.fuelTank.fill(FluidRegistry.getFluidStack(ConfigManagerCore.useOldFuelFluidID ? "fuelgc" : "fuel", amountToDrain), true);
        }
    }

    @Override
    public void readFromNBT(CompoundNBT nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInt("smeltingTicks");

        if (nbt.contains("oilTank"))
        {
            this.oilTank.readFromNBT(nbt.getCompoundTag("oilTank"));
        }
        if (this.oilTank.getFluid() != null && this.oilTank.getFluid().getFluid() != GCFluidRegistry.fluidOil)
        {
            this.oilTank.setFluid(new FluidStack(GCFluidRegistry.fluidOil, this.oilTank.getFluidAmount()));
        }


        if (nbt.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }
        if (this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid() != GCFluidRegistry.fluidFuel)
        {
            this.fuelTank.setFluid(new FluidStack(GCFluidRegistry.fluidFuel, this.fuelTank.getFluidAmount()));
        }
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt)
    {
        super.writeToNBT(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);

        if (this.oilTank.getFluid() != null)
        {
            nbt.put("oilTank", this.oilTank.writeToNBT(new CompoundNBT()));
        }

        if (this.fuelTank.getFluid() != null)
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[] { 0, 1, 2 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (!itemstack.isEmpty() && this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 1:
                return FluidUtil.isOilContainerAny(itemstack);
            case 2:
                return FluidUtil.isPartialContainer(itemstack, GCItems.fuelCanister);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (!itemstack.isEmpty() && this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemEmpty(itemstack) || !this.shouldPullEnergy();
            case 1:
                return FluidUtil.isEmptyContainer(itemstack);
            case 2:
                return FluidUtil.isFullContainer(itemstack);
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
            return !itemstack.isEmpty() && ItemElectricBase.isElectricItem(itemstack.getItem());
        case 1:
        case 2:
            return FluidUtil.isValidContainer(itemstack);
        }

        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.canProcess();
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return Direction.UP;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockRefinery)
        {
            return state.get(BlockRefinery.FACING);
        }
        return Direction.NORTH;
    }

    private Direction getOilPipe()
    {
        return getFront().rotateY();
    }
    
    private Direction getFuelPipe()
    {
        return getFront().rotateYCCW();
    }
    
    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        if (from == getFuelPipe())
        {
            return this.fuelTank.getFluid() != null && this.fuelTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain)
    {
        if (from == getFuelPipe() && resource != null)
        {
            return this.fuelTank.drain(resource.amount, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, boolean doDrain)
    {
        if (from == getFuelPipe())
        {
            return this.drain(from, new FluidStack(GCFluidRegistry.fluidFuel, maxDrain), doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        if (from == getOilPipe())
        {
            return this.oilTank.getFluid() == null || this.oilTank.getFluidAmount() < this.oilTank.getCapacity();
        }

        return false;
    }

    @Override
    public int fill(Direction from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (from == getOilPipe() && resource != null)
        {
            final String liquidName = FluidRegistry.getFluidName(resource);

            if (liquidName != null && FluidUtil.testOil(liquidName))
            {
                if (liquidName.equals(GCFluidRegistry.fluidOil.getName()))
                {
                    used = this.oilTank.fill(resource, doFill);
                }
                else
                {
                    used = this.oilTank.fill(new FluidStack(GCFluidRegistry.fluidOil, resource.amount), doFill);
                }
            }
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(Direction from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};

        if (from == getOilPipe())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.oilTank) };
        }
        else if (from == getFuelPipe())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
        }

        return tankInfo;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) new FluidHandlerWrapper(this, facing);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        } 
        if (type == NetworkType.POWER)
        {
            return direction == this.getElectricInputDirection();
        }
        if (type == NetworkType.FLUID)
        {
            Direction pipeSide = getFuelPipe();
            return direction == pipeSide || direction == pipeSide.getOpposite();
        }
        return false;
    }
}
