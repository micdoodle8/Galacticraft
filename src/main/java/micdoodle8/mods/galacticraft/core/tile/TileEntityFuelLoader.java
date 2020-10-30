package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockFuelLoader;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.inventory.ContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRefinery;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFuelLoader extends TileBaseElectricBlockWithInventory implements ISidedInventory, IFluidHandlerWrapper, ILandingPadAttachable, IMachineSides, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.fuelLoader)
    public static TileEntityType<TileEntityFuelLoader> TYPE;

    private final int tankCapacity = 12000;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);
    public IFuelable attachedFuelable;
    private boolean loadedFuelLastTick = false;

    public TileEntityFuelLoader()
    {
        super(TYPE);
        this.storage.setMaxExtract(30);
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            this.loadedFuelLastTick = false;

            final FluidStack liquidContained = FluidUtil.getFluidContained(this.getInventory().get(1));
            if (FluidUtil.isFuel(liquidContained))
            {
                FluidUtil.loadFromContainer(this.fuelTank, GCFluids.FUEL.getFluid(), this.getInventory(), 1, liquidContained.getAmount());
            }

            if (this.ticks % 100 == 0)
            {
                this.attachedFuelable = null;

                BlockVec3 thisVec = new BlockVec3(this);
                for (final Direction dir : Direction.values())
                {
                    final TileEntity pad = thisVec.getTileEntityOnSide(this.world, dir);

                    if (pad instanceof TileEntityFake)
                    {
                        final TileEntity mainTile = ((TileEntityFake) pad).getMainBlockTile();

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

            if (this.fuelTank != null && this.fuelTank.getFluid() != FluidStack.EMPTY && this.fuelTank.getFluid().getAmount() > 0)
            {
                final FluidStack liquid = new FluidStack(GCFluids.FUEL.getFluid(), 2);

                if (this.attachedFuelable != null && this.hasEnoughEnergyToRun && !this.disabled)
                {
                    int filled = this.attachedFuelable.addFuel(liquid, IFluidHandler.FluidAction.EXECUTE);
                    this.loadedFuelLastTick = filled > 0;
                    this.fuelTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    @Override
    public void read(CompoundNBT par1NBTTagCompound)
    {
        super.read(par1NBTTagCompound);

        if (par1NBTTagCompound.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(par1NBTTagCompound.getCompound("fuelTank"));
        }

        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        if (this.fuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundNBT()));
        }

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides

        return nbt;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (slotID == 1 && !itemstack.isEmpty())
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        return false;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return (slotID == 1 && itemstack != null && itemstack.getItem() == GCItems.fuelCanister) || (slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem()));
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction action)
    {
        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        if (this.getPipeInputDirection().equals(from))
        {
            return this.fuelTank.getFluid() == FluidStack.EMPTY || this.fuelTank.getFluidAmount() < this.fuelTank.getCapacity();
        }
        return false;
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        int used = 0;

        if (this.getPipeInputDirection().equals(from) && resource != null)
        {
            if (FluidUtil.testFuel(resource.getFluid().getRegistryName()))
            {
                used = this.fuelTank.fill(resource, action);
            }
        }

        return used;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        if (this.getPipeInputDirection().equals(from))
//        {
//            return new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
//        }
//        return null;
//    }


    @Override
    public int getTanks()
    {
        return this.fuelTank.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return this.fuelTank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return this.fuelTank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        return this.fuelTank.isFluidValid(tank, stack);
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.fuelTank.getFluid() != FluidStack.EMPTY && this.fuelTank.getFluid().getAmount() > 0 && !this.getDisabled(0) && loadedFuelLastTick;
    }

    @Override
    public boolean canAttachToLandingPad(IWorldReader world, BlockPos pos)
    {
        return true;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockFuelLoader)
        {
            return state.get(BlockFuelLoader.FACING);
        }
        return Direction.NORTH;
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
//    {
//        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }

    private LazyOptional<IFluidHandler> holder = null;

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (holder == null)
            {
                holder = LazyOptional.of(new NonNullSupplier<IFluidHandler>()
                {
                    @Nonnull
                    @Override
                    public IFluidHandler get()
                    {
                        return new FluidHandlerWrapper(TileEntityFuelLoader.this, facing);
                    }
                });
            }
            return holder.cast();
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
            return direction == this.getPipeInputDirection();
        }
        return false;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
        default:
            return getFront().rotateY();
        }
    }

    @Override
    public Direction getPipeInputDirection()
    {
        switch (this.getSide(MachineSide.PIPE_IN))
        {
        case RIGHT:
        default:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
            return getFront().rotateY();
        }
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[]{MachineSide.ELECTRIC_IN, MachineSide.PIPE_IN};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.LEFT, Face.RIGHT};
    }

    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return IMachineSidesProperties.TWOFACES_HORIZ;
    }
    //------------------END OF IMachineSides implementation

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerFuelLoader(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.fuel_loader");
    }
}
