package micdoodle8.mods.galacticraft.planets.mars.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.NetworkHelper;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMarsT2;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.miccore.Annotations;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

public class TileEntityMethaneSynthesizer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandlerWrapper
{
    private final int tankCapacity = 4000;

    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank gasTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank gasTank2 = new FluidTank(this.tankCapacity / 2);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank liquidTank = new FluidTank(this.tankCapacity / 2);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = -8;
    private int hasCO2 = -1;
    private boolean noCoal = true;
    private int coalPartial = 0;

    public TileEntityMethaneSynthesizer()
    {
        super("tile.mars_machine.5.name");
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 45);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
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
    public void update()
    {
        super.update();

        if (this.hasCO2 == -1)
        {
            this.hasCO2 = this.getAirProducts();
        }

        if (!this.world.isRemote)
        {
            //If somehow it has CO2 in a CO2-free dimension, flush it out
            if (this.hasCO2 == 0 && this.gasTank2.getFluidAmount() > 0)
            {
                this.gasTank2.drain(this.gasTank2.getFluidAmount(), true);
            }

            //First, see if any gas needs to be put into the hydogen storage
            //TODO - in 1.7.10 implement support for Mekanism internal hydrogen tanks
            //TODO add support for hydrogen atmospheres

            //Now check the CO2 storage
            ItemStack inputCanister = this.getInventory().get(2);
            if (!inputCanister.isEmpty())
            {
                if (inputCanister.getItem() instanceof ItemAtmosphericValve && this.hasCO2 > 0)
                {
                    //CO2 -> CO2 tank
                    if (this.gasTank2.getFluidAmount() < this.gasTank2.getCapacity())
                    {
                        IBlockState stateAbove = this.world.getBlockState(this.getPos().up());
                        Block blockAbove = stateAbove.getBlock();
                        if (blockAbove.getMaterial(stateAbove) == Material.AIR && blockAbove != GCBlocks.breatheableAir && blockAbove != GCBlocks.brightBreatheableAir)
                        {
                            if (!OxygenUtil.inOxygenBubble(this.world, this.getPos().getX() + 0.5D, this.getPos().getY() + 1D, this.getPos().getZ() + 0.5D))
                            {
                                FluidStack gcAtmosphere = FluidRegistry.getFluidStack("carbondioxide", 4);
                                this.gasTank2.fill(gcAtmosphere, true);
                            }
                        }
                    }
                }
            }


            //Now see if any methane from the methane tank needs to be put into the output slot
            checkFluidTankTransfer(4, this.liquidTank);

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
                else if (--this.processTicks <= -8)
                {
                    this.processTicks = -8;
                }
            }
            
            this.produceOutput(this.getHydrogenInputDirection().getOpposite());
        }
    }

    private void produceOutput()
    {
        // TODO Auto-generated method stub
        
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        if (FluidUtil.isValidContainer(this.getInventory().get(slot)))
        {
            final FluidStack liquid = tank.getFluid();

            if (liquid != null)
            {
                FluidUtil.tryFillContainer(tank, liquid, this.getInventory(), slot, AsteroidsItems.methaneCanister);
            }
        }
        else if (!this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot).getItem() instanceof ItemAtmosphericValve)
        {
            tank.drain(4, true);
        }
    }

    public int getScaledGasLevel(int i)
    {
        return this.gasTank.getFluid() != null ? this.gasTank.getFluid().amount * i / this.gasTank.getCapacity() : 0;
    }

    public int getScaledGasLevel2(int i)
    {
        return this.gasTank2.getFluid() != null ? this.gasTank2.getFluid().amount * i / this.gasTank2.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.liquidTank.getFluid() != null ? this.liquidTank.getFluid().amount * i / this.liquidTank.getCapacity() : 0;
    }

    public boolean canProcess()
    {
        if (this.gasTank.getFluid() == null || this.gasTank.getFluid().amount <= 0 || this.getDisabled(0))
        {
            return false;
        }

        this.noCoal = this.getInventory().get(3).isEmpty() || this.getInventory().get(3).getItem() != MarsItems.carbonFragments;

        if (this.noCoal && this.coalPartial == 0 && (this.gasTank2.getFluid() == null || this.gasTank2.getFluidAmount() <= 0))
        {
            return false;
        }

        return this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
    }

    public int getAirProducts()
    {
        WorldProvider WP = this.world.provider;
        if (WP instanceof WorldProviderSpace)
        {
            ArrayList<EnumAtmosphericGas> atmos = ((WorldProviderSpace) WP).getCelestialBody().atmosphere.composition;
            if (atmos.size() > 0)
            {
                if (atmos.get(0) == EnumAtmosphericGas.CO2)
                {
                    return 1;
                }
            }
            if (atmos.size() > 1)
            {
                if (atmos.get(1) == EnumAtmosphericGas.CO2)
                {
                    return 1;
                }
            }
            if (atmos.size() > 2)
            {
                if (atmos.get(2) == EnumAtmosphericGas.CO2)
                {
                    return 1;
                }
            }

            return 0;
        }

        return 0;
    }

    public void doLiquefaction()
    {
        if (this.noCoal && this.coalPartial == 0)
        {
            if (this.gasTank2.getFluid() == null || this.gasTank2.drain(1, true).amount < 1)
            {
                return;
            }
        }
        else
        {
            if (this.coalPartial == 0)
            {
                this.decrStackSize(3, 1);
            }
            this.coalPartial++;
            if (this.coalPartial == 40)
            {
                this.coalPartial = 0;
            }
        }
        this.gasTank.drain(this.placeIntoFluidTanks(2) * 8, true);
    }

    private int placeIntoFluidTanks(int amountToDrain)
    {
        final int fuelSpace = this.liquidTank.getCapacity() - this.liquidTank.getFluidAmount();

        if (fuelSpace > 0)
        {
            if (amountToDrain > fuelSpace)
            {
                amountToDrain = fuelSpace;
            }
            this.liquidTank.fill(FluidRegistry.getFluidStack("methane", amountToDrain), true);
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

        if (nbt.hasKey("gasTank2"))
        {
            this.gasTank2.readFromNBT(nbt.getCompoundTag("gasTank2"));
        }

        if (nbt.hasKey("liquidTank"))
        {
            this.liquidTank.readFromNBT(nbt.getCompoundTag("liquidTank"));
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

        if (this.gasTank2.getFluid() != null)
        {
            nbt.setTag("gasTank2", this.gasTank2.writeToNBT(new NBTTagCompound()));
        }

        if (this.liquidTank.getFluid() != null)
        {
            nbt.setTag("liquidTank", this.liquidTank.writeToNBT(new NBTTagCompound()));
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
        return new int[] { 0, 1, 2, 3, 4 };
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
            case 3:
                return itemstack.getItem() == MarsItems.carbonFragments;
            case 4:
                return FluidUtil.isPartialContainer(itemstack, AsteroidsItems.methaneCanister);
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
                return ItemElectricBase.isElectricItemEmpty(itemstack) || !this.shouldPullEnergy();
            case 4:
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
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        case 1:
            return false;
        case 2:
            return itemstack.getItem() instanceof ItemAtmosphericValve;
        case 3:
            return itemstack.getItem() == MarsItems.carbonFragments;
        case 4:
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
        if (from == this.getHydrogenInputDirection().getOpposite())
        {
            return this.liquidTank.getFluid() != null && this.liquidTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        if (from == this.getHydrogenInputDirection().getOpposite())
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
        if (from == getHydrogenInputDirection().getOpposite())
        {
            return this.liquidTank.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        if (from == this.getHydrogenInputDirection())
        {
            return fluid == null || "hydrogen".equals(fluid.getName());
        }

        return false;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
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

        if (from == this.getHydrogenInputDirection())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
        }
        else if (from == this.getHydrogenInputDirection().getOpposite())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
        }

        return tankInfo;
    }

    @Override
    public int getBlockMetadata()
    {
        return this.getBlockType().getMetaFromState(this.world.getBlockState(getPos()));
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
    {
        if (!stack.getGas().getName().equals("hydrogen"))
        {
            return 0;
        }
        int used = 0;
        //System.out.println("Giving gas amount "+stack.amount);
        if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
        {
            used = this.gasTank.fill(FluidRegistry.getFluidStack("hydrogen", stack.amount), doTransfer);
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
        //System.out.println("Testing receipt of gas "+type.getName());
        return type.getName().equals("hydrogen") && side.equals(this.getHydrogenInputDirection());
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canDrawGas(EnumFacing side, Gas type)
    {
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
    public boolean canTubeConnect(EnumFacing side)
    {
        return side.equals(this.getHydrogenInputDirection());
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        }

        if (type == NetworkType.POWER)
        {
            return direction == this.getElectricInputDirection();
        }

        //Hydrogen pipe
        if (type == NetworkType.FLUID)
        {
            return direction == this.getHydrogenInputDirection() || direction == this.getHydrogenInputDirection().getOpposite();
        }

        return false;
    }

    public int getHydrogenRequest(EnumFacing direction)
    {
        return this.receiveHydrogen(direction, 1000000F, false);
    }

    public boolean shouldPullHydrogen()
    {
        return this.gasTank.getFluidAmount() < this.gasTank.getCapacity();
    }

    public int receiveHydrogen(EnumFacing from, float receive, boolean doReceive)
    {
        if (from == this.getHydrogenInputDirection() && this.shouldPullHydrogen())
        {
            FluidStack fluidToFill = FluidRegistry.getFluidStack("hydrogen", (int) (receive));
            return this.gasTank.fill(fluidToFill, doReceive);
        }

        return 0;
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

    public EnumFacing getHydrogenInputDirection()
    {
        return this.getFront().rotateY();
    }
    
    private boolean produceOutput(EnumFacing outputDirection)
    {
        int provide = this.getMethaneProvide();

        if (provide > 0)
        {
            TileEntity outputTile = new BlockVec3(this).getTileEntityOnSide(this.world, outputDirection);
            FluidNetwork outputNetwork = NetworkHelper.getFluidNetworkFromTile(outputTile, outputDirection);

            if (outputNetwork != null)
            {
                int gasRequested = outputNetwork.getRequest();

                if (gasRequested > 0)
                {
                    int usedGas = outputNetwork.emitToBuffer(new FluidStack(AsteroidsModule.fluidMethaneGas, Math.min(gasRequested, provide)), true);
                    this.liquidTank.drain(usedGas, true);
                    return true;
                }
            }
        }

        return false;
    }

    private int getMethaneProvide()
    {
        return Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.liquidTank.getFluidAmount());
    }
}