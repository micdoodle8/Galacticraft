package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityGasLiquefier extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandlerWrapper, IOxygenReceiver
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.gasLiquefier)
    public static TileEntityType<TileEntityGasLiquefier> TYPE;

    private final int tankCapacity = 2000;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public final FluidTank gasTank = new FluidTank(this.tankCapacity * 2);
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public final FluidTank liquidTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public final FluidTank liquidTank2 = new FluidTank(this.tankCapacity);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int processTicks = -10;
    private int airProducts = -1;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int gasTankType = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int fluidTankType = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int fluidTank2Type = -1;

    // TODO Support other mods' fluids?
    public enum TankGases
    {
        METHANE(0, PlanetFluids.GAS_METHANE.getFluid(), GCFluids.FUEL.getFluid()),
        OXYGEN(1, GCFluids.OXYGEN.getFluid(), PlanetFluids.LIQUID_OXYGEN.getFluid()),
        NITROGEN(2, PlanetFluids.GAS_NITROGEN.getFluid(), PlanetFluids.LIQUID_NITROGEN.getFluid()),
        ARGON(3, PlanetFluids.GAS_ARGON.getFluid(), PlanetFluids.LIQUID_ARGON.getFluid()),
        AIR(4, PlanetFluids.GAS_ATMOSPHERIC.getFluid(), null);

        final int index;
        final Fluid gas;
        final Fluid liquid;

        TankGases(int id, Fluid fluidname, Fluid outputname)
        {
            this.index = id;
            this.gas = fluidname;
            this.liquid = outputname;
        }
    }


    public TileEntityGasLiquefier()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 60);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//            return true;
//
//        if (EnergyUtil.checkMekGasHandler(capability))
//        	return true;
//
//        return super.hasCapability(capability, facing);
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
                        return new FluidHandlerWrapper(TileEntityGasLiquefier.this, facing);
                    }
                });
            }
            return holder.cast();
        }
//        if (EnergyUtil.checkMekGasHandler(capability))
//        {
//            return (T) this;
//        }  TODO Mek support
        return super.getCapability(capability, facing);
    }

    @Override
    public void remove()
    {
        super.remove();
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.airProducts == -1)
        {
            this.airProducts = this.getAirProducts();
        }

        if (!this.world.isRemote)
        {
            FluidStack currentgas = this.gasTank.getFluid();
            if (currentgas == FluidStack.EMPTY || currentgas.getAmount() <= 0)
            {
                this.gasTankType = -1;
            }
            else
            {
                this.gasTankType = this.getIdFromName(currentgas.getFluid().getRegistryName().getPath());
            }

            //If somehow it has air in an airless dimension, flush it out
            if (this.airProducts == 0 && this.gasTankType == TankGases.AIR.index)
            {
                this.gasTank.drain(this.gasTank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            }

            FluidStack currentLiquid = this.liquidTank.getFluid();
            if (currentLiquid == FluidStack.EMPTY || currentLiquid.getAmount() == 0)
            {
                this.fluidTankType = -1;
            }
            else
            {
                this.fluidTankType = this.getProductIdFromName(currentLiquid.getFluid().getRegistryName().getPath());
            }

            currentLiquid = this.liquidTank2.getFluid();
            if (currentLiquid == FluidStack.EMPTY || currentLiquid.getAmount() == 0)
            {
                this.fluidTank2Type = -1;
            }
            else
            {
                this.fluidTank2Type = this.getProductIdFromName(currentLiquid.getFluid().getRegistryName().getPath());
            }

            //First, see if any gas needs to be put into the gas storage
            ItemStack inputCanister = getInventory().get(1);
            if (!inputCanister.isEmpty())
            {
                if (inputCanister.getItem() == AsteroidsItems.atmosphericValve && this.airProducts > 0)
                {
                    //Air -> Air tank
                    if (this.gasTankType == -1 || (this.gasTankType == TankGases.AIR.index && this.gasTank.getFluid().getAmount() < this.gasTank.getCapacity()))
                    {
                        BlockState stateAbove = this.world.getBlockState(getPos().up());
                        if (stateAbove.getMaterial() == Material.AIR && stateAbove.getBlock() != GCBlocks.breatheableAir && stateAbove.getBlock() != GCBlocks.brightBreatheableAir)
                        {
                            FluidStack gcAtmosphere = new FluidStack(TankGases.AIR.gas, 4);
                            this.gasTank.fill(gcAtmosphere, IFluidHandler.FluidAction.EXECUTE);
                            this.gasTankType = TankGases.AIR.index;
                        }
                    }
                }
                else if (inputCanister.getItem() instanceof ItemCanisterGeneric)
                {
                    int amount = ItemCanisterGeneric.EMPTY - inputCanister.getDamage();
                    if (amount > 0)
                    {
                        Item canisterType = inputCanister.getItem();
                        FluidStack canisterGas = null;
                        int factor = 1;
                        if (this.gasTankType <= 0 && canisterType == AsteroidsItems.methaneCanister)
                        {
                            this.gasTankType = TankGases.METHANE.index;
                            canisterGas = new FluidStack(TankGases.METHANE.gas, amount);
                        }
                        if ((this.gasTankType == TankGases.OXYGEN.index || this.gasTankType == -1) && canisterType == AsteroidsItems.canisterLOX)
                        {
                            this.gasTankType = TankGases.OXYGEN.index;
                            canisterGas = new FluidStack(TankGases.OXYGEN.gas, amount * 2);
                            factor = 2;
                        }
                        if ((this.gasTankType == TankGases.NITROGEN.index || this.gasTankType == -1) && canisterType == AsteroidsItems.canisterLN2)
                        {
                            this.gasTankType = TankGases.NITROGEN.index;
                            canisterGas = new FluidStack(TankGases.NITROGEN.gas, amount * 2);
                            factor = 2;
                        }

                        if (canisterGas != null)
                        {
                            int used = this.gasTank.fill(canisterGas, IFluidHandler.FluidAction.EXECUTE) / factor;
                            if (used == amount)
                            {
                                ItemStack stack = new ItemStack(GCItems.oilCanister, 1);
                                stack.setDamage(ItemCanisterGeneric.EMPTY);
                                getInventory().set(1, stack);
                            }
                            else
                            {
                                ItemStack stack = new ItemStack(GCItems.oilCanister, 1);
                                stack.setDamage(ItemCanisterGeneric.EMPTY - amount + used);
                                getInventory().set(1, stack);
                            }
                        }
                    }
                }
                else
                {
                    LazyOptional<FluidStack> holder = net.minecraftforge.fluids.FluidUtil.getFluidContained(inputCanister);
                    FluidStack fluid = holder.orElse(FluidStack.EMPTY);
                    if (fluid != FluidStack.EMPTY && fluid.getAmount() > 0)
                    {
                        String inputName = fluid.getFluid().getRegistryName().getPath();
                        //Methane -> Methane tank
                        if (this.gasTankType <= 0 && inputName.contains("methane"))
                        {
                            if (currentgas == FluidStack.EMPTY || currentgas.getAmount() + fluid.getAmount() <= this.gasTank.getCapacity())
                            {
                                FluidStack gcMethane = new FluidStack(TankGases.METHANE.gas, fluid.getAmount());
                                this.gasTank.fill(gcMethane, IFluidHandler.FluidAction.EXECUTE);
                                this.gasTankType = 0;
                                ItemStack stack = FluidUtil.getUsedContainer(inputCanister);
                                getInventory().set(1, stack);
                            }
                        }
                        else                        //Oxygen -> Oxygen tank
                            if ((this.gasTankType == TankGases.OXYGEN.index || this.gasTankType == -1) && inputName.contains("oxygen"))
                            {
                                int tankedAmount = fluid.getAmount() * (inputName.contains("fluid") ? 2 : 1);
                                if (currentgas == FluidStack.EMPTY || currentgas.getAmount() + tankedAmount <= this.gasTank.getCapacity())
                                {
                                    FluidStack gcgas = new FluidStack(TankGases.OXYGEN.gas, tankedAmount);
                                    this.gasTank.fill(gcgas, IFluidHandler.FluidAction.EXECUTE);
                                    this.gasTankType = TankGases.OXYGEN.index;
                                    ItemStack stack = FluidUtil.getUsedContainer(inputCanister);
                                    getInventory().set(1, stack);
                                }
                            }
                            else                        //Nitrogen -> Nitrogen tank
                                if ((this.gasTankType == TankGases.NITROGEN.index || this.gasTankType == -1) && inputName.contains("nitrogen"))
                                {
                                    int tankedAmount = fluid.getAmount() * (inputName.contains("fluid") ? 2 : 1);
                                    if (currentgas == FluidStack.EMPTY || currentgas.getAmount() + tankedAmount <= this.gasTank.getCapacity())
                                    {
                                        FluidStack gcgas = new FluidStack(TankGases.NITROGEN.gas, tankedAmount);
                                        this.gasTank.fill(gcgas, IFluidHandler.FluidAction.EXECUTE);
                                        this.gasTankType = TankGases.NITROGEN.index;
                                        ItemStack stack = FluidUtil.getUsedContainer(inputCanister);
                                        getInventory().set(1, stack);
                                    }
                                }
                    }
                }
            }

            //Now see if any liquids from the output tanks need to be put into the output slot
            checkFluidTankTransfer(2, this.liquidTank);
            checkFluidTankTransfer(3, this.liquidTank2);

            if (this.hasEnoughEnergyToRun && this.canProcess())
            {
                //50% extra speed boost for Tier 2 machine if powered by Tier 2 power
                if (this.tierGC == 2)
                {
                    this.processTimeRequired = Math.max(1, 4 - this.poweredByTierGC);
                }

                if (this.processTicks <= 0)
                {
                    this.processTicks = this.processTimeRequired;
                }
                else
                {
                    if (--this.processTicks <= 0)
                    {
                        this.doLiquefaction();
                        this.processTicks = this.canProcess() ? this.processTimeRequired : 0;
                    }
                }
            }
            else
            {
                if (this.processTicks > 0)
                {
                    this.processTicks = 0;
                }
                else if (--this.processTicks <= -10)
                {
                    this.processTicks = -10;
                }
            }
        }
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        if (FluidUtil.isValidContainer(this.getInventory().get(slot)))
        {
            final FluidStack liquid = tank.getFluid();

            if (liquid.getAmount() > 0)
            {
                String liquidname = liquid.getFluid().getRegistryName().getPath();
                if (liquidname.startsWith("fuel"))
                {
                    FluidUtil.tryFillContainerFuel(tank, this.getInventory(), slot);
                }
            }
        }
        else if (!this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot).getItem() == AsteroidsItems.atmosphericValve)
        {
            tank.drain(4, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public int getIdFromName(String gasname)
    {
        for (TankGases type : TankGases.values())
        {
            if (type.gas.equals(gasname))
            {
                return type.index;
            }
        }

        return -1;
    }

    public int getProductIdFromName(String gasname)
    {
        for (TankGases type : TankGases.values())
        {
            if (type.liquid.equals(gasname))
            {
                return type.index;
            }
        }

        return -1;
    }

    public int getScaledGasLevel(int i)
    {
        return this.gasTank.getFluid() != FluidStack.EMPTY ? this.gasTank.getFluid().getAmount() * i / this.gasTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.liquidTank.getFluid() != FluidStack.EMPTY ? this.liquidTank.getFluid().getAmount() * i / this.liquidTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel2(int i)
    {
        return this.liquidTank2.getFluid() != FluidStack.EMPTY ? this.liquidTank2.getFluid().getAmount() * i / this.liquidTank2.getCapacity() : 0;
    }

    public boolean canProcess()
    {
        if (this.gasTank.getFluid() == FluidStack.EMPTY || this.gasTank.getFluid().getAmount() <= 0 || this.getDisabled(0))
        {
            return false;
        }

        if (this.fluidTankType == -1 || this.fluidTank2Type == -1)
        {
            return true;
        }

        boolean tank1HasSpace = this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
        boolean tank2HasSpace = this.liquidTank2.getFluidAmount() < this.liquidTank2.getCapacity();

        if (this.gasTankType == TankGases.AIR.index)
        {
            int airProducts = this.airProducts;
            do
            {
                int thisProduct = (airProducts & 15) - 1;
                if ((thisProduct == this.fluidTankType && tank1HasSpace) || (thisProduct == this.fluidTank2Type && tank2HasSpace))
                {
                    return true;
                }
                airProducts = airProducts >> 4;
            }
            while (airProducts > 0);
            return false;
        }

        return (this.gasTankType == this.fluidTankType && tank1HasSpace) || (this.gasTankType == this.fluidTank2Type && tank2HasSpace);
    }

    public int getAirProducts()
    {
        Dimension WP = this.world.getDimension();
        if (WP instanceof DimensionSpace)
        {
            int result = 0;
            ArrayList<EnumAtmosphericGas> atmos = ((DimensionSpace) WP).getCelestialBody().atmosphere.composition;
            if (atmos.size() > 0)
            {
                result = this.getIdFromName(atmos.get(0).name().toLowerCase()) + 1;
            }
            if (atmos.size() > 1)
            {
                result += 16 * (this.getIdFromName(atmos.get(1).name().toLowerCase()) + 1);
            }
            if (atmos.size() > 2)
            {
                result += 256 * (this.getIdFromName(atmos.get(2).name().toLowerCase()) + 1);
            }

            return result;
        }

        return 35;
    }

    public void doLiquefaction()
    {
        //Can't be called if the gasTank fluid is null
        final int gasAmount = this.gasTank.getFluid().getAmount();
        if (gasAmount == 0)
        {
            return;
        }

        if (this.gasTankType == TankGases.AIR.index)
        {
            int airProducts = this.airProducts;
            int amountToDrain = Math.min(gasAmount / 2, (airProducts > 15) ? 2 : 3);
            if (amountToDrain == 0)
            {
                amountToDrain = 1;
            }

            do
            {
                int thisProduct = (airProducts & 15) - 1;
                //-1 indicates a gas which can't be liquefied (e.g. Carbon Dioxide)
                if (thisProduct >= 0)
                {
                    this.gasTank.drain(this.placeIntoFluidTanks(thisProduct, amountToDrain) * 2, IFluidHandler.FluidAction.EXECUTE);
                }
                airProducts = airProducts >> 4;
                amountToDrain = amountToDrain >> 1;
                if (amountToDrain == 0)
                {
                    amountToDrain = 1;
                }
            }
            while (airProducts > 0);
        }
        else
        {
            if (gasAmount == 1)
            {
                this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, 1), IFluidHandler.FluidAction.EXECUTE);
            }
            else
            {
                this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, Math.min(gasAmount / 2, 3)) * 2, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    private int placeIntoFluidTanks(int thisProduct, int amountToDrain)
    {
        final int fuelSpace = this.liquidTank.getCapacity() - this.liquidTank.getFluidAmount();
        final int fuelSpace2 = this.liquidTank2.getCapacity() - this.liquidTank2.getFluidAmount();

        if ((thisProduct == this.fluidTank2Type || this.fluidTank2Type == -1) && fuelSpace2 > 0)
        {
            if (amountToDrain > fuelSpace2)
            {
                amountToDrain = fuelSpace2;
            }
            this.liquidTank2.fill(new FluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), IFluidHandler.FluidAction.EXECUTE);
            this.fluidTank2Type = thisProduct;
        }
        else if ((thisProduct == this.fluidTankType || this.fluidTankType == -1) && fuelSpace > 0)
        {
            if (amountToDrain > fuelSpace)
            {
                amountToDrain = fuelSpace;
            }
            this.liquidTank.fill(new FluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), IFluidHandler.FluidAction.EXECUTE);
            this.fluidTankType = thisProduct;
        }
        else
        {
            amountToDrain = 0;
        }

        return amountToDrain;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.processTicks = nbt.getInt("smeltingTicks");

        if (nbt.contains("gasTank"))
        {
            this.gasTank.readFromNBT(nbt.getCompound("gasTank"));
        }

        if (nbt.contains("liquidTank"))
        {
            this.liquidTank.readFromNBT(nbt.getCompound("liquidTank"));
        }
        if (nbt.contains("liquidTank2"))
        {
            this.liquidTank2.readFromNBT(nbt.getCompound("liquidTank2"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);

        if (this.gasTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("gasTank", this.gasTank.writeToNBT(new CompoundNBT()));
        }

        if (this.liquidTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("liquidTank", this.liquidTank.writeToNBT(new CompoundNBT()));
        }
        if (this.liquidTank2.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("liquidTank2", this.liquidTank2.writeToNBT(new CompoundNBT()));
        }

        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side == Direction.DOWN)
        {
            return new int[]{0, 1, 2, 3};
        }

        if (side == Direction.UP)
        {
            return new int[]{0};
        }

        return new int[]{1, 2, 3};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
                case 0:
                    return ItemElectricBase.isElectricItemCharged(itemstack);
                case 1:
                    return FluidUtil.isMethaneContainerAny(itemstack);
                case 2:
                    return FluidUtil.isEmptyContainerFor(itemstack, this.liquidTank.getFluid());
                case 3:
                    return FluidUtil.isEmptyContainerFor(itemstack, this.liquidTank2.getFluid());
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        switch (slotID)
        {
            case 0:
                return ItemElectricBase.isElectricItemEmpty(itemstack);
            case 1:
                return FluidUtil.isEmptyContainer(itemstack);
            case 2:
                return FluidUtil.isFullContainer(itemstack);
            case 3:
                return FluidUtil.isFullContainer(itemstack);
            default:
                return false;
        }
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
            case 0:
                return ItemElectricBase.isElectricItem(itemstack.getItem());
            case 1:
            case 2:
            case 3:
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
    public double getPacketRange()
    {
        return 320.0D;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return Direction.DOWN;
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        if (from == getGasInputDirection().getOpposite())
        {
            return this.liquidTank2.getFluid() != FluidStack.EMPTY && this.liquidTank2.getFluidAmount() > 0;
        }

        //2->5 3->4 4->2 5->3
        if (getGasInputDirection().rotateY() == from)
        {
            return this.liquidTank.getFluid() != FluidStack.EMPTY && this.liquidTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (from == getGasInputDirection().getOpposite())
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank2.getFluid()))
            {
                return this.liquidTank2.drain(resource.getAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        }

        //2->5 3->4 4->2 5->3
        if (getGasInputDirection().rotateY() == from)
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank.getFluid()))
            {
                return this.liquidTank.drain(resource.getAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction action)
    {
        if (from == getGasInputDirection().getOpposite())
        {
            return this.liquidTank2.drain(maxDrain, IFluidHandler.FluidAction.EXECUTE);
        }

        //2->5 3->4 4->2 5->3
        if (getGasInputDirection().rotateY() == from)
        {
            return this.liquidTank.drain(maxDrain, IFluidHandler.FluidAction.EXECUTE);
        }

        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        if (from.equals(this.getGasInputDirection()))
        {
            //Can fill with gases
            return fluid == null || this.getIdFromName(fluid.getRegistryName().getPath()) > -1;
        }

        return false;
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            int type = this.getIdFromName(resource.getFluid().getRegistryName().getPath());

            if (this.gasTankType == -1 || (this.gasTankType == type && this.gasTank.getFluidAmount() < this.gasTank.getCapacity()))
            {
                used = this.gasTank.fill(resource, action);
            }
        }

        return used;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
//
//        if (from == this.getGasInputDirection())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
//        }
//        else if (from == this.getGasInputDirection().getOpposite())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank2) };
//        }
//        if (getGasInputDirection().rotateY() == from)
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
//        }
//
//        return tankInfo;
//    }

    @Override
    public int getTanks()
    {
        return 3;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        switch (tank)
        {
            case 0:
                return this.gasTank.getFluid();
            case 1:
                return this.liquidTank2.getFluid();
            case 2:
                return this.liquidTank.getFluid();
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank)
    {
        switch (tank)
        {
            case 0:
                return this.gasTank.getCapacity();
            case 1:
                return this.liquidTank2.getCapacity();
            case 2:
                return this.liquidTank.getCapacity();
        }
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        switch (tank)
        {
            case 0:
                return this.gasTank.isFluidValid(stack);
            case 1:
                return this.liquidTank2.isFluidValid(stack);
            case 2:
                return this.liquidTank.isFluidValid(stack);
        }
        return false;
    }

//    @Override
//    public int getBlockMetadata()
//    {
//        return getBlockType().getMetaFromState(this.world.getBlockState(getPos())) & 3;
//    }

    @Override
    public boolean shouldPullOxygen()
    {
        return this.gasTankType == -1 || (this.gasTankType == 1 && this.gasTank.getFluidAmount() < this.gasTank.getCapacity());
    }

    @Override
    public int receiveOxygen(Direction from, int receive, IFluidHandler.FluidAction action)
    {
        if (from == this.getGasInputDirection() && this.shouldPullOxygen())
        {
            float conversion = 2F * Constants.LOX_GAS_RATIO;  //Special conversion ratio for breathable air
            FluidStack fluidToFill = new FluidStack(GCFluids.OXYGEN.getFluid(), (int) (receive * conversion));
            int used = MathHelper.ceil(this.gasTank.fill(fluidToFill, action) / conversion);
            return used;
        }

        return 0;
    }

    @Override
    public int provideOxygen(Direction from, int request, IFluidHandler.FluidAction action)
    {
        return 0;
    }

    @Override
    public int getOxygenRequest(Direction direction)
    {
        return this.receiveOxygen(direction, 1000000, IFluidHandler.FluidAction.SIMULATE);
    }

    @Override
    public int getOxygenProvide(Direction direction)
    {
        return 0;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        }

        if (type == NetworkType.FLUID)
        {
            return direction == getGasInputDirection() || direction == this.getGasInputDirection().getOpposite() || direction == this.getGasInputDirection().rotateY();
        }

        if (type == NetworkType.POWER)
        {
            return direction == Direction.DOWN;
        }

        return false;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
//        if (state.getBlock() instanceof BlockMachineMarsT2)
//        {
//            return state.get(BlockMachineMarsT2.FACING);
//        }
        return state.get(BlockGasLiquefier.FACING);
    }

    public Direction getGasInputDirection()
    {
        return this.getFront().rotateY();
    }

//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack, boolean doTransfer)
//    {
//        if (!stack.getGas().getName().equals("oxygen") || !this.shouldPullOxygen())
//        {
//            return 0;
//        }
//        int used = 0;
//        if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
//        {
//            used = this.gasTank.fill(new FluidStack("oxygen", stack.amount), doTransfer);
//        }
//        return used;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack)
//    {
//        return this.receiveGas(side, stack, true);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount, boolean doTransfer)
//    {
//        return null;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount)
//    {
//        return null;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveGas(Direction side, Gas type)
//    {
//        return this.shouldPullOxygen() && type.getName().equals("oxygen") && side.equals(this.getGasInputDirection());
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canDrawGas(Direction side, Gas type)
//    {
//        return false;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
//    public boolean canTubeConnect(Direction side)
//    {
//        return side.equals(this.getGasInputDirection());
//    }
}