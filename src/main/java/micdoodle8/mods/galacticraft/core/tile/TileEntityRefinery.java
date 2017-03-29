package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockRefinery;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

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
    private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);

    public TileEntityRefinery()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 60);
        this.oilTank.setFluid(new FluidStack(GCFluids.fluidOil, 0));
        this.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, 0));
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            if (!this.stacks.get(1).isEmpty())
            {
                if (this.stacks.get(1).getItem() instanceof ItemCanisterGeneric)
                {
                    if (this.stacks.get(1).getItem() == GCItems.oilCanister)
                    {
                        int originalDamage = this.stacks.get(1).getItemDamage();
                        int used = this.oilTank.fill(new FluidStack(GCFluids.fluidOil, ItemCanisterGeneric.EMPTY - originalDamage), true);
                        this.stacks.set(1, new ItemStack(GCItems.oilCanister, 1, originalDamage + used));
                    }
                }
                else
                {
                    FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(this.stacks.get(1));

                    if (liquid != null)
                    {
                        boolean isOil = FluidRegistry.getFluidName(liquid).startsWith("oil");

                        if (isOil)
                        {
                            if (this.oilTank.getFluid() == null || this.oilTank.getFluid().amount + liquid.amount <= this.oilTank.getCapacity())
                            {
                                this.oilTank.fill(new FluidStack(GCFluids.fluidOil, liquid.amount), true);

                                if (FluidUtil.isBucket(this.stacks.get(1)) && net.minecraftforge.fluids.FluidUtil.getFluidHandler(this.stacks.get(1)) != null)
                                {
                                    final int amount = this.stacks.get(1).getCount();
                                    if (amount > 1)
                                    {
                                        this.oilTank.fill(new FluidStack(GCFluids.fluidOil, (amount - 1) * Fluid.BUCKET_VOLUME), true);
                                    }
                                    this.stacks.set(1, new ItemStack(Items.BUCKET, amount));
                                }
                                else
                                {
                                    this.stacks.get(1).shrink(1);
                                }
                            }
                        }
                    }
                }
            }

            checkFluidTankTransfer(2, this.fuelTank);

            if (this.canProcess() && this.hasEnoughEnergyToRun)
            {
                if (this.processTicks == 0)
                {
                    this.processTicks = TileEntityRefinery.PROCESS_TIME_REQUIRED;
                }
                else
                {
                    if (--this.processTicks <= 0)
                    {
                        this.smeltItem();
                        this.processTicks = this.canProcess() ? TileEntityRefinery.PROCESS_TIME_REQUIRED : 0;
                    }
                }
            }
            else
            {
                this.processTicks = 0;
            }
        }
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        FluidUtil.tryFillContainerFuel(tank, this.stacks, slot);
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
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("smeltingTicks");
        this.stacks = this.readStandardItemsFromNBT(nbt);

        if (nbt.hasKey("oilTank"))
        {
            this.oilTank.readFromNBT(nbt.getCompoundTag("oilTank"));
        }
        if (this.oilTank.getFluid() != null && this.oilTank.getFluid().getFluid() != GCFluids.fluidOil)
        {
            this.oilTank.setFluid(new FluidStack(GCFluids.fluidOil, this.oilTank.getFluidAmount()));
        }


        if (nbt.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }
        if (this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid() != GCFluids.fluidFuel)
        {
            this.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, this.fuelTank.getFluidAmount()));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("smeltingTicks", this.processTicks);
        this.writeStandardItemsToNBT(nbt, this.stacks);

        if (this.oilTank.getFluid() != null)
        {
            nbt.setTag("oilTank", this.oilTank.writeToNBT(new NBTTagCompound()));
        }

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }


    @Override
    protected NonNullList<ItemStack> getContainingItems()
    {
        return this.stacks;
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.refinery.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1, 2 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (itemstack != null && this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
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
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (itemstack != null && this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || !this.shouldPullEnergy();
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
            return itemstack != null && ItemElectricBase.isElectricItem(itemstack.getItem());
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
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.UP;
    }

    @Override
    public EnumFacing getFront()
    {
        return (this.world.getBlockState(getPos()).getValue(BlockRefinery.FACING));
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        if (from.equals(getFront()))
//        if (from.equals(EnumFacing.getFront((this.getBlockMetadata() + 2) ^ 1)))
        {
            return this.fuelTank.getFluid() != null && this.fuelTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        if (from.equals(getFront()))
//        if (from.equals(EnumFacing.getFront((this.getBlockMetadata() + 2) ^ 1)))
        {
            return this.fuelTank.drain(resource.amount, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        if (from.equals(getFront()))
//        if (from.equals(EnumFacing.getFront((this.getBlockMetadata() + 2) ^ 1)))
        {
            return this.drain(from, new FluidStack(GCFluids.fluidFuel, maxDrain), doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        if (from.equals(getFront()))
//        if (from.equals(EnumFacing.getFront(this.getBlockMetadata() + 2)))
        {
            return this.oilTank.getFluid() == null || this.oilTank.getFluidAmount() < this.oilTank.getCapacity();
        }

        return false;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (from.equals(getFront()))
//        if (from.equals(EnumFacing.getFront(this.getBlockMetadata() + 2)))
        {
            final String liquidName = FluidRegistry.getFluidName(resource);

            if (liquidName != null && liquidName.startsWith("oil"))
            {
                if (liquidName.equals(GCFluids.fluidOil.getName()))
                {
                    used = this.oilTank.fill(resource, doFill);
                }
                else
                {
                    used = this.oilTank.fill(new FluidStack(GCFluids.fluidOil, resource.amount), doFill);
                }
            }
//            else if (liquidName != null && liquidName.equalsIgnoreCase("oilgc"))
//            {
//                used = this.oilTank.fill(new FluidStack(GalacticraftCore.fluidOil, resource.amount), doFill);
//            }
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};

        if (from.equals(getFront()))
//        if (from == EnumFacing.getFront(this.getBlockMetadata() + 2))
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.oilTank) };
        }
        else if (from.equals(getFront()))
//        else if (from == EnumFacing.getFront((this.getBlockMetadata() + 2) ^ 1))
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
        }

        return tankInfo;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) new FluidHandlerWrapper(this, facing);
        }
        return super.getCapability(capability, facing);
    }
}
