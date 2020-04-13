package micdoodle8.mods.galacticraft.planets.mars.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMarsT2;
import micdoodle8.mods.miccore.Annotations;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

public class TileEntityGasLiquefier extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandlerWrapper, IOxygenReceiver
{
    private final int tankCapacity = 2000;

    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank gasTank = new FluidTank(this.tankCapacity * 2);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank liquidTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank liquidTank2 = new FluidTank(this.tankCapacity);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = -10;
    private int airProducts = -1;

    @NetworkedField(targetSide = Side.CLIENT)
    public int gasTankType = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int fluidTankType = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int fluidTank2Type = -1;

    public enum TankGases
    {
        METHANE(0, "methane", ConfigManagerCore.useOldFuelFluidID ? "fuelgc" : "fuel"),
        OXYGEN(1, "oxygen", "liquidoxygen"),
        NITROGEN(2, "nitrogen", "liquidnitrogen"),
        ARGON(3, "argon", "liquidargon"),
        AIR(4, "atmosphericgases", "xxyyzz");

        int index;
        String gas;
        String liquid;

        TankGases(int id, String fluidname, String outputname)
        {
            this.index = id;
            this.gas = fluidname;
            this.liquid = outputname;
        }
    }


    public TileEntityGasLiquefier()
    {
        super("tile.mars_machine.4.name");
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 60);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return true;

        if (EnergyUtil.checkMekGasHandler(capability))
        	return true;
       
        return super.hasCapability(capability, facing);  
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) new FluidHandlerWrapper(this, facing);
        }

        if (EnergyUtil.checkMekGasHandler(capability))
        {
            return (T) this;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
    }

    @Override
    public void update()
    {
        super.update();

        if (this.airProducts == -1)
        {
            this.airProducts = this.getAirProducts();
        }

        if (!this.world.isRemote)
        {
            FluidStack currentgas = this.gasTank.getFluid();
            if (currentgas == null || currentgas.amount <= 0)
            {
                this.gasTankType = -1;
            }
            else
            {
                this.gasTankType = this.getIdFromName(currentgas.getFluid().getName());
            }

            //If somehow it has air in an airless dimension, flush it out
            if (this.airProducts == 0 && this.gasTankType == TankGases.AIR.index)
            {
                this.gasTank.drain(this.gasTank.getFluidAmount(), true);
            }

            FluidStack currentLiquid = this.liquidTank.getFluid();
            if (currentLiquid == null || currentLiquid.amount == 0)
            {
                this.fluidTankType = -1;
            }
            else
            {
                this.fluidTankType = this.getProductIdFromName(currentLiquid.getFluid().getName());
            }

            currentLiquid = this.liquidTank2.getFluid();
            if (currentLiquid == null || currentLiquid.amount == 0)
            {
                this.fluidTank2Type = -1;
            }
            else
            {
                this.fluidTank2Type = this.getProductIdFromName(currentLiquid.getFluid().getName());
            }

            //First, see if any gas needs to be put into the gas storage
            ItemStack inputCanister = getInventory().get(1);
            if (!inputCanister.isEmpty())
            {
                if (inputCanister.getItem() instanceof ItemAtmosphericValve && this.airProducts > 0)
                {
                    //Air -> Air tank
                    if (this.gasTankType == -1 || (this.gasTankType == TankGases.AIR.index && this.gasTank.getFluid().amount < this.gasTank.getCapacity()))
                    {
                        IBlockState stateAbove = this.world.getBlockState(getPos().up());
                        if (stateAbove.getMaterial() == Material.AIR && stateAbove.getBlock() != GCBlocks.breatheableAir && stateAbove.getBlock() != GCBlocks.brightBreatheableAir)
                        {
                            FluidStack gcAtmosphere = FluidRegistry.getFluidStack(TankGases.AIR.gas, 4);
                            this.gasTank.fill(gcAtmosphere, true);
                            this.gasTankType = TankGases.AIR.index;
                        }
                    }
                }
                else if (inputCanister.getItem() instanceof ItemCanisterGeneric)
                {
                    int amount = ItemCanisterGeneric.EMPTY - inputCanister.getItemDamage();
                    if (amount > 0)
                    {
                        Item canisterType = inputCanister.getItem();
                        FluidStack canisterGas = null;
                        int factor = 1;
                        if (this.gasTankType <= 0 && canisterType == AsteroidsItems.methaneCanister)
                        {
                            this.gasTankType = TankGases.METHANE.index;
                            canisterGas = FluidRegistry.getFluidStack(TankGases.METHANE.gas, amount);
                        }
                        if ((this.gasTankType == TankGases.OXYGEN.index || this.gasTankType == -1) && canisterType == AsteroidsItems.canisterLOX)
                        {
                            this.gasTankType = TankGases.OXYGEN.index;
                            canisterGas = FluidRegistry.getFluidStack(TankGases.OXYGEN.gas, amount * 2);
                            factor = 2;
                        }
                        if ((this.gasTankType == TankGases.NITROGEN.index || this.gasTankType == -1) && canisterType == AsteroidsItems.canisterLN2)
                        {
                            this.gasTankType = TankGases.NITROGEN.index;
                            canisterGas = FluidRegistry.getFluidStack(TankGases.NITROGEN.gas, amount * 2);
                            factor = 2;
                        }

                        if (canisterGas != null)
                        {
                            int used = this.gasTank.fill(canisterGas, true) / factor;
                            if (used == amount)
                            {
                                getInventory().set(1, new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY));
                            }
                            else
                            {
                                getInventory().set(1, new ItemStack(canisterType, 1, ItemCanisterGeneric.EMPTY - amount + used));
                            }
                        }
                    }
                }
                else
                {
                    FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(inputCanister);
                    if (liquid != null && liquid.amount > 0)
                    {
                        String inputName = FluidRegistry.getFluidName(liquid);
                        //Methane -> Methane tank
                        if (this.gasTankType <= 0 && inputName.contains("methane"))
                        {
                            if (currentgas == null || currentgas.amount + liquid.amount <= this.gasTank.getCapacity())
                            {
                                FluidStack gcMethane = FluidRegistry.getFluidStack(TankGases.METHANE.gas, liquid.amount);
                                this.gasTank.fill(gcMethane, true);
                                this.gasTankType = 0;
                                ItemStack stack = FluidUtil.getUsedContainer(inputCanister);
                                getInventory().set(1, stack == null ? ItemStack.EMPTY : stack);
                            }
                        }
                        else                        //Oxygen -> Oxygen tank
                            if ((this.gasTankType == TankGases.OXYGEN.index || this.gasTankType == -1) && inputName.contains("oxygen"))
                            {
                                int tankedAmount = liquid.amount * (inputName.contains("liquid") ? 2 : 1);
                                if (currentgas == null || currentgas.amount + tankedAmount <= this.gasTank.getCapacity())
                                {
                                    FluidStack gcgas = FluidRegistry.getFluidStack(TankGases.OXYGEN.gas, tankedAmount);
                                    this.gasTank.fill(gcgas, true);
                                    this.gasTankType = TankGases.OXYGEN.index;
                                    ItemStack stack = FluidUtil.getUsedContainer(inputCanister);
                                    getInventory().set(1, stack == null ? ItemStack.EMPTY : stack);
                                }
                            }
                            else                        //Nitrogen -> Nitrogen tank
                                if ((this.gasTankType == TankGases.NITROGEN.index || this.gasTankType == -1) && inputName.contains("nitrogen"))
                                {
                                    int tankedAmount = liquid.amount * (inputName.contains("liquid") ? 2 : 1);
                                    if (currentgas == null || currentgas.amount + tankedAmount <= this.gasTank.getCapacity())
                                    {
                                        FluidStack gcgas = FluidRegistry.getFluidStack(TankGases.NITROGEN.gas, tankedAmount);
                                        this.gasTank.fill(gcgas, true);
                                        this.gasTankType = TankGases.NITROGEN.index;
                                        ItemStack stack = FluidUtil.getUsedContainer(inputCanister);
                                        getInventory().set(1, stack == null ? ItemStack.EMPTY : stack);
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

            if (liquid != null && liquid.amount > 0)
            {
                String liquidname = liquid.getFluid().getName();
                if (liquidname.startsWith("fuel"))
                {
                    FluidUtil.tryFillContainerFuel(tank, this.getInventory(), slot);
                }
                else if (liquidname.equals(TankGases.OXYGEN.liquid))
                {
                    FluidUtil.tryFillContainer(tank, liquid, this.getInventory(), slot, AsteroidsItems.canisterLOX);
                }
                else if (liquidname.equals(TankGases.NITROGEN.liquid))
                {
                    FluidUtil.tryFillContainer(tank, liquid, this.getInventory(), slot, AsteroidsItems.canisterLN2);
                }
            }
        }
        else if (!this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot).getItem() instanceof ItemAtmosphericValve)
        {
            tank.drain(4, true);
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
        return this.gasTank.getFluid() != null ? this.gasTank.getFluid().amount * i / this.gasTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.liquidTank.getFluid() != null ? this.liquidTank.getFluid().amount * i / this.liquidTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel2(int i)
    {
        return this.liquidTank2.getFluid() != null ? this.liquidTank2.getFluid().amount * i / this.liquidTank2.getCapacity() : 0;
    }

    public boolean canProcess()
    {
        if (this.gasTank.getFluid() == null || this.gasTank.getFluid().amount <= 0 || this.getDisabled(0))
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

        if ((this.gasTankType == this.fluidTankType && tank1HasSpace) || (this.gasTankType == this.fluidTank2Type && tank2HasSpace))
        {
            return true;
        }

        return false;
    }

    public int getAirProducts()
    {
        WorldProvider WP = this.world.provider;
        if (WP instanceof WorldProviderSpace)
        {
            int result = 0;
            ArrayList<EnumAtmosphericGas> atmos = ((WorldProviderSpace) WP).getCelestialBody().atmosphere.composition;
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
        final int gasAmount = this.gasTank.getFluid().amount;
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
                    this.gasTank.drain(this.placeIntoFluidTanks(thisProduct, amountToDrain) * 2, true);
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
                this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, 1), true);
            }
            else
            {
                this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, Math.min(gasAmount / 2, 3)) * 2, true);
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
            this.liquidTank2.fill(FluidRegistry.getFluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), true);
            this.fluidTank2Type = thisProduct;
        }
        else if ((thisProduct == this.fluidTankType || this.fluidTankType == -1) && fuelSpace > 0)
        {
            if (amountToDrain > fuelSpace)
            {
                amountToDrain = fuelSpace;
            }
            this.liquidTank.fill(FluidRegistry.getFluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), true);
            this.fluidTankType = thisProduct;
        }
        else
        {
            amountToDrain = 0;
        }

        return amountToDrain;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("smeltingTicks");

        if (nbt.hasKey("gasTank"))
        {
            this.gasTank.readFromNBT(nbt.getCompoundTag("gasTank"));
        }

        if (nbt.hasKey("liquidTank"))
        {
            this.liquidTank.readFromNBT(nbt.getCompoundTag("liquidTank"));
        }
        if (nbt.hasKey("liquidTank2"))
        {
            this.liquidTank2.readFromNBT(nbt.getCompoundTag("liquidTank2"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("smeltingTicks", this.processTicks);

        if (this.gasTank.getFluid() != null)
        {
            nbt.setTag("gasTank", this.gasTank.writeToNBT(new NBTTagCompound()));
        }

        if (this.liquidTank.getFluid() != null)
        {
            nbt.setTag("liquidTank", this.liquidTank.writeToNBT(new NBTTagCompound()));
        }
        if (this.liquidTank2.getFluid() != null)
        {
            nbt.setTag("liquidTank2", this.liquidTank2.writeToNBT(new NBTTagCompound()));
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
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.DOWN)
        {
            return new int[] { 0, 1, 2, 3 };
        }

        if (side == EnumFacing.UP)
        {
            return new int[] { 0 };
        }

        return new int[] { 1, 2, 3 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
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
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
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
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.DOWN;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        if (from == getGasInputDirection().getOpposite())
        {
            return this.liquidTank2.getFluid() != null && this.liquidTank2.getFluidAmount() > 0;
        }

        //2->5 3->4 4->2 5->3
        if (getGasInputDirection().rotateY() == from)
        {
            return this.liquidTank.getFluid() != null && this.liquidTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        if (from == getGasInputDirection().getOpposite())
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank2.getFluid()))
            {
                return this.liquidTank2.drain(resource.amount, doDrain);
            }
        }

        //2->5 3->4 4->2 5->3
        if (getGasInputDirection().rotateY() == from)
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank.getFluid()))
            {
                return this.liquidTank.drain(resource.amount, doDrain);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        if (from == getGasInputDirection().getOpposite())
        {
            return this.liquidTank2.drain(maxDrain, doDrain);
        }

        //2->5 3->4 4->2 5->3
        if (getGasInputDirection().rotateY() == from)
        {
            return this.liquidTank.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        if (from.equals(this.getGasInputDirection()))
        {
            //Can fill with gases
            return fluid == null || this.getIdFromName(fluid.getName()) > -1;
        }

        return false;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            int type = this.getIdFromName(FluidRegistry.getFluidName(resource));

            if (this.gasTankType == -1 || (this.gasTankType == type && this.gasTank.getFluidAmount() < this.gasTank.getCapacity()))
            {
                used = this.gasTank.fill(resource, doFill);
            }
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};

        if (from == this.getGasInputDirection())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
        }
        else if (from == this.getGasInputDirection().getOpposite())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank2) };
        }
        if (getGasInputDirection().rotateY() == from)
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
        }

        return tankInfo;
    }

    @Override
    public int getBlockMetadata()
    {
        return getBlockType().getMetaFromState(this.world.getBlockState(getPos())) & 3;
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return this.gasTankType == -1 || (this.gasTankType == 1 && this.gasTank.getFluidAmount() < this.gasTank.getCapacity());
    }

    @Override
    public int receiveOxygen(EnumFacing from, int receive, boolean doReceive)
    {
        if (from == this.getGasInputDirection() && this.shouldPullOxygen())
        {
            float conversion = 2F * Constants.LOX_GAS_RATIO;  //Special conversion ratio for breathable air
            FluidStack fluidToFill = new FluidStack(AsteroidsModule.fluidOxygenGas, (int) (receive * conversion));
            int used = MathHelper.ceil(this.gasTank.fill(fluidToFill, doReceive) / conversion);
            return used;
        }

        return 0;
    }

    @Override
    public int provideOxygen(EnumFacing from, int request, boolean doProvide)
    {
        return 0;
    }

    @Override
    public int getOxygenRequest(EnumFacing direction)
    {
        return this.receiveOxygen(direction, 1000000, false);
    }

    @Override
    public int getOxygenProvide(EnumFacing direction)
    {
        return 0;
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
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
            return direction == EnumFacing.DOWN;
        }

        return false;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockMachineMarsT2)
        {
            return state.getValue(BlockMachineMarsT2.FACING);
        }
        return EnumFacing.NORTH;
    }

    public EnumFacing getGasInputDirection()
    {
        return this.getFront().rotateY();
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
    {
        if (!stack.getGas().getName().equals("oxygen") || !this.shouldPullOxygen())
        {
            return 0;
        }
        int used = 0;
        if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
        {
            used = this.gasTank.fill(FluidRegistry.getFluidStack("oxygen", stack.amount), doTransfer);
        }
        return used;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack)
    {
        return this.receiveGas(side, stack, true);
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer)
    {
        return null;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount)
    {
        return null;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canReceiveGas(EnumFacing side, Gas type)
    {
        return this.shouldPullOxygen() && type.getName().equals("oxygen") && side.equals(this.getGasInputDirection());
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canDrawGas(EnumFacing side, Gas type)
    {
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
    public boolean canTubeConnect(EnumFacing side)
    {
        return side.equals(this.getGasInputDirection());
    }
}