package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockFuelLoader;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public class TileEntityFuelLoader extends TileBaseElectricBlockWithInventory implements ISidedInventory, IFluidHandlerWrapper, ILandingPadAttachable
{
    private final int tankCapacity = 12000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);
    private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    public IFuelable attachedFuelable;
    private boolean loadedFuelLastTick = false;

    public TileEntityFuelLoader()
    {
        this.storage.setMaxExtract(30);
    }

    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount;

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            this.loadedFuelLastTick = false;

            if (!this.stacks.get(1).isEmpty())
            {
                if (this.stacks.get(1).getItem() instanceof ItemCanisterGeneric)
                {
                    if (this.stacks.get(1).getItem() == GCItems.fuelCanister)
                    {
                        int originalDamage = this.stacks.get(1).getItemDamage();
                        int used = this.fuelTank.fill(new FluidStack(GCFluids.fluidFuel, ItemCanisterGeneric.EMPTY - originalDamage), true);
                        if (originalDamage + used == ItemCanisterGeneric.EMPTY)
                        {
                            this.stacks.set(1, new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY));
                        }
                        else
                        {
                            this.stacks.set(1, new ItemStack(GCItems.fuelCanister, 1, originalDamage + used));
                        }
                    }
                }
                else
                {
                    FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(this.stacks.get(1));

                    if (liquid != null)
                    {
                        boolean isFuel = FluidUtil.testFuel(FluidRegistry.getFluidName(liquid));

                        if (isFuel)
                        {
                            if (this.fuelTank.getFluid() == null || this.fuelTank.getFluid().amount + liquid.amount <= this.fuelTank.getCapacity())
                            {
                                this.fuelTank.fill(new FluidStack(GCFluids.fluidFuel, liquid.amount), true);

                                if (FluidUtil.isBucket(this.stacks.get(1)) && net.minecraftforge.fluids.FluidUtil.getFluidHandler(this.stacks.get(1)) != null)
                                {
                                    final int amount = this.stacks.get(1).getCount();
                                    if (amount > 1)
                                    {
                                        this.fuelTank.fill(new FluidStack(GCFluids.fluidFuel, (amount - 1) * Fluid.BUCKET_VOLUME), true);
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

            if (this.ticks % 100 == 0)
            {
                this.attachedFuelable = null;

                for (final EnumFacing dir : EnumFacing.values())
                {
                    final TileEntity pad = new BlockVec3(this).getTileEntityOnSide(this.world, dir);

                    if (pad instanceof TileEntityMulti)
                    {
                        final TileEntity mainTile = ((TileEntityMulti) pad).getMainBlockTile();

                        if (mainTile instanceof IFuelable)
                        {
                            this.attachedFuelable = (IFuelable) mainTile;
                            break;
                        }
                    }
                    else if (pad instanceof IFuelable)
                    {
                        this.attachedFuelable = (IFuelable) pad;
                        break;
                    }
                }

            }

            if (this.fuelTank != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().amount > 0)
            {
                final FluidStack liquid = new FluidStack(GCFluids.fluidFuel, 2);

                if (this.attachedFuelable != null && this.hasEnoughEnergyToRun && !this.disabled)
                {
                    int filled = this.attachedFuelable.addFuel(liquid, true);
                    this.loadedFuelLastTick = filled > 0;
                    this.fuelTank.drain(filled, true);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.stacks = this.readStandardItemsFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(par1NBTTagCompound.getCompoundTag("fuelTank"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt, this.stacks);

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
        return GCCoreUtil.translate("container.fuelloader.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
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
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 1 && itemstack != null)
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        return false;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

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
        return (slotID == 1 && itemstack != null && itemstack.getItem() == GCItems.fuelCanister) || (slotID == 0 ? ItemElectricBase.isElectricItem(itemstack.getItem()) : false);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return this.fuelTank.getFluid() == null || this.fuelTank.getFluidAmount() < this.fuelTank.getCapacity();
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int used = 0;

//        if (from.equals(EnumFacing.getFront(this.getBlockMetadata() + 2).getOpposite()))
        if (from.equals(this.world.getBlockState(getPos()).getValue(BlockFuelLoader.FACING)))
        {
            if (FluidUtil.testFuel(FluidRegistry.getFluidName(resource)))
            {
                used = this.fuelTank.fill(resource, doFill);
            }
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.fuelTank.getFluid() != null && this.fuelTank.getFluid().amount > 0 && !this.getDisabled(0) && loadedFuelLastTick;
    }

    @Override
    public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    @Override
    public EnumFacing getFront()
    {
        return this.world.getBlockState(getPos()).getValue(BlockFuelLoader.FACING);
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront().rotateY();
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
