package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.*;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;

public class TileEntityGasLiquefier extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandler
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
    public int processTicks = 0;
    private ItemStack[] containingItems = new ItemStack[4];
    private int airProducts = -1;

    @NetworkedField(targetSide = Side.CLIENT)
    public int gasTankType = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int fluidTankType = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int fluidTank2Type = -1;

    public enum TankGases
    {
        METHANE(0, "methane", "fuel"),
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
            this.gas = new String(fluidname);
            this.liquid = new String(outputname);
        }
    }


    public TileEntityGasLiquefier()
    {
        this.storage.setMaxExtract(60);
        this.setTierGC(2);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (this.airProducts == -1)
        {
            this.airProducts = this.getAirProducts();
        }

        if (!this.worldObj.isRemote)
        {
            FluidStack currentgas = this.gasTank.getFluid();
            if (currentgas == null || currentgas.amount <= 0) this.gasTankType = -1;
            else this.gasTankType = this.getIdFromName(currentgas.getFluid().getName());

            //If somehow it has air in an airless dimension, flush it out
            if (this.airProducts == 0 && this.gasTankType == TankGases.AIR.index)
            {
                this.gasTank.drain(this.gasTank.getFluidAmount(), true);
            }

            FluidStack currentLiquid = this.liquidTank.getFluid();
            if (currentLiquid == null || currentLiquid.amount == 0) this.fluidTankType = -1;
            else this.fluidTankType = this.getProductIdFromName(currentLiquid.getFluid().getName());

            currentLiquid = this.liquidTank2.getFluid();
            if (currentLiquid == null || currentLiquid.amount == 0) this.fluidTank2Type = -1;
            else this.fluidTank2Type = this.getProductIdFromName(currentLiquid.getFluid().getName());

            //First, see if any gas needs to be put into the gas storage
            ItemStack inputCanister = this.containingItems[1];
            if (inputCanister != null)
            {
                if (inputCanister.getItem() instanceof ItemAtmosphericValve && this.airProducts > 0)
                {
                    //Air -> Air tank
                    if (this.gasTankType == -1 || (this.gasTankType == TankGases.AIR.index && this.gasTank.getFluid().amount < this.gasTank.getCapacity()))
                    {
                        Block blockAbove = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord);
                        if (blockAbove != null && blockAbove.getMaterial() == Material.air && blockAbove!=GCBlocks.breatheableAir && blockAbove!=GCBlocks.brightBreatheableAir)
                        {
                            FluidStack gcAtmosphere = FluidRegistry.getFluidStack(TankGases.AIR.gas, 4);
                            this.gasTank.fill(gcAtmosphere, true);
                            this.gasTankType = TankGases.AIR.index;
                        }
                    }
                }
                else
                {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(inputCanister);
                    if (liquid != null && liquid.amount > 0)
                    {
                        String inputName = FluidRegistry.getFluidName(liquid);
                        //Methane -> Methane tank
                        if (this.gasTankType <= 0 && inputName.contains("methane"))
                        {
                            if (currentgas == null || currentgas.amount + liquid.amount * 2  <= this.gasTank.getCapacity())
                            {
                                FluidStack gcMethane = FluidRegistry.getFluidStack(TankGases.METHANE.gas, liquid.amount);
                                this.gasTank.fill(gcMethane, true);
                                this.gasTankType = 0;

                                if (inputCanister.getItem() instanceof ItemCanisterMethane)
                                {
                                    this.containingItems[1] = new ItemStack(AsteroidsItems.methaneCanister, 1, AsteroidsItems.methaneCanister.getMaxDamage());
                                }
                                else if (FluidContainerRegistry.isBucket(inputCanister) && FluidContainerRegistry.isFilledContainer(inputCanister))
                                {
                                    final int amount = inputCanister.stackSize;
                                    this.containingItems[1] = new ItemStack(Items.bucket, amount);
                                }
                                else
                                {
                                    inputCanister.stackSize--;

                                    if (inputCanister.stackSize == 0)
                                    {
                                        this.containingItems[1] = null;
                                    }
                                }
                            }
                        }
                        else 						//Oxygen -> Oxygen tank
                            if ((this.gasTankType == TankGases.OXYGEN.index || this.gasTankType == -1) && inputName.contains("oxygen"))
                            {
                                if (currentgas == null || currentgas.amount + liquid.amount * 2 <= this.gasTank.getCapacity())
                                {
                                    FluidStack gcgas = FluidRegistry.getFluidStack(TankGases.OXYGEN.gas, liquid.amount * (inputName.contains("liquid") ? 2 : 1));
                                    this.gasTank.fill(gcgas, true);
                                    this.gasTankType = TankGases.OXYGEN.index;

                                    if (inputCanister.getItem() instanceof ItemCanisterLiquidOxygen)
                                    {
                                        this.containingItems[1] = new ItemStack(AsteroidsItems.canisterLOX, 1, AsteroidsItems.canisterLOX.getMaxDamage());
                                    }
                                    else if (FluidContainerRegistry.isBucket(inputCanister) && FluidContainerRegistry.isFilledContainer(inputCanister))
                                    {
                                        final int amount = inputCanister.stackSize;
                                        this.containingItems[1] = new ItemStack(Items.bucket, amount);
                                    }
                                    else
                                    {
                                        inputCanister.stackSize--;

                                        if (inputCanister.stackSize == 0)
                                        {
                                            this.containingItems[1] = null;
                                        }
                                    }
                                }
                            }
                            else 						//Nitrogen -> Nitrogen tank
                                if ((this.gasTankType == TankGases.NITROGEN.index || this.gasTankType == -1) && inputName.contains("nitrogen"))
                                {
                                    if (currentgas == null || currentgas.amount + liquid.amount * 2  <= this.gasTank.getCapacity())
                                    {
                                        FluidStack gcgas = FluidRegistry.getFluidStack(TankGases.NITROGEN.gas, liquid.amount * (inputName.contains("liquid") ? 2 : 1));
                                        this.gasTank.fill(gcgas, true);
                                        this.gasTankType = TankGases.NITROGEN.index;

                                        if (inputCanister.getItem() instanceof ItemCanisterLiquidNitrogen)
                                        {
                                            this.containingItems[1] = new ItemStack(AsteroidsItems.canisterLN2, 1, AsteroidsItems.canisterLN2.getMaxDamage());
                                        }
                                        else if (FluidContainerRegistry.isBucket(inputCanister) && FluidContainerRegistry.isFilledContainer(inputCanister))
                                        {
                                            final int amount = inputCanister.stackSize;
                                            this.containingItems[1] = new ItemStack(Items.bucket, amount);
                                        }
                                        else
                                        {
                                            inputCanister.stackSize--;

                                            if (inputCanister.stackSize == 0)
                                            {
                                                this.containingItems[1] = null;
                                            }
                                        }
                                    }
                                }
                    }
                }
            }

            //Now see if any fuel from the fuel tank needs to be put into the output slot
            checkFluidTankTransfer(2, this.liquidTank);
            checkFluidTankTransfer(3, this.liquidTank2);

            if (this.hasEnoughEnergyToRun && this.canProcess())
            {
                //50% extra speed boost for Tier 2 machine if powered by Tier 2 power
                if (this.tierGC == 2) this.processTimeRequired = (this.poweredByTierGC == 2) ? 2 : 3;

                if (this.processTicks == 0)
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
                this.processTicks = 0;
            }
        }
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        if (this.containingItems[slot] != null && FluidContainerRegistry.isContainer(this.containingItems[slot]))
        {
            final FluidStack liquid = tank.getFluid();

            if (liquid != null && liquid.amount > 0)
            {
                String liquidname = liquid.getFluid().getName();
                if (liquidname.equals(TankGases.METHANE.liquid))
                {
                    tryFillContainer(tank, liquid, slot, GCItems.fuelCanister);
                }
                else if (liquidname.equals(TankGases.OXYGEN.liquid))
                {
                    tryFillContainer(tank, liquid, slot, AsteroidsItems.canisterLOX);
                }
                else if (liquidname.equals(TankGases.NITROGEN.liquid))
                {
                    tryFillContainer(tank, liquid, slot, AsteroidsItems.canisterLN2);
                }
            }
        }
        else if (this.containingItems[slot] != null && this.containingItems[slot].getItem() instanceof ItemAtmosphericValve)
        {
            tank.drain(4, true);
        }
    }

    private void tryFillContainer(FluidTank tank, FluidStack liquid, int slot, Item canister)
    {
        ItemStack slotItem = this.containingItems[slot];
        boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() > 1;
        final int amountToFill = Math.min(liquid.amount, isCanister ? slotItem.getItemDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

        if (isCanister)
        {
            this.containingItems[slot] = new ItemStack(canister, 1, slotItem.getItemDamage() - amountToFill);
            tank.drain(amountToFill, true);
        }
        else if (amountToFill == FluidContainerRegistry.BUCKET_VOLUME)
        {
            this.containingItems[slot] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[slot]);
            if (this.containingItems[slot] == null) this.containingItems[slot] = slotItem;
            else tank.drain(amountToFill, true);
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
            return true;

        boolean tank1HasSpace = this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
        boolean tank2HasSpace = this.liquidTank2.getFluidAmount() < this.liquidTank2.getCapacity();

        if (this.gasTankType == TankGases.AIR.index)
        {
            int airProducts = this.airProducts;
            do
            {
                int thisProduct = (airProducts & 15) - 1;
                if ((thisProduct == this.fluidTankType && tank1HasSpace) || (thisProduct == this.fluidTank2Type && tank2HasSpace))
                    return true;
                airProducts = airProducts >> 4;
            }
            while (airProducts > 0);
            return false;
        }

        if ((this.gasTankType == this.fluidTankType && tank1HasSpace) || (this.gasTankType == this.fluidTank2Type && tank2HasSpace))
            return true;

        return false;
    }

    public int getAirProducts()
    {
        WorldProvider WP = this.worldObj.provider;
        if (WP instanceof WorldProviderSpace)
        {
            int result = 0;
            ArrayList<IAtmosphericGas> atmos = ((WorldProviderSpace)WP).getCelestialBody().atmosphere;
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
        if (gasAmount == 0) return;

        if (this.gasTankType == TankGases.AIR.index)
        {
            int airProducts = this.airProducts;
            int amountToDrain = Math.min(gasAmount / 2, (airProducts > 15) ? 2 : 3);
            if (amountToDrain == 0) amountToDrain = 1;

            do
            {
                int thisProduct = (airProducts & 15) - 1;
                //-1 indicates a gas which can't be liquefied (e.g. Carbon Dioxide)
                if (thisProduct >= 0)
                    this.gasTank.drain(this.placeIntoFluidTanks(thisProduct, amountToDrain) * 2, true);
                airProducts = airProducts >> 4;
                amountToDrain = amountToDrain >> 1;
                if (amountToDrain == 0)
                {
                    if (thisProduct == -1)
                        amountToDrain = 1;
                    else
                        break;
                }
            }
            while (airProducts > 0);
        }
        else
        {
            if (gasAmount == 1)
                this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, 1), true);
            else
                this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, Math.min(gasAmount / 2, 3)) * 2, true);
        }
    }

    private int placeIntoFluidTanks(int thisProduct, int amountToDrain)
    {
        final int fuelSpace = this.liquidTank.getCapacity() - this.liquidTank.getFluidAmount();
        final int fuelSpace2 = this.liquidTank2.getCapacity() - this.liquidTank2.getFluidAmount();

        if ((thisProduct == this.fluidTankType || this.fluidTankType == -1) && fuelSpace > 0)
        {
            if (amountToDrain > fuelSpace) amountToDrain = fuelSpace;
            this.liquidTank.fill(FluidRegistry.getFluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), true);
            this.fluidTankType = thisProduct;
        } else
        if ((thisProduct == this.fluidTank2Type || this.fluidTank2Type == -1) && fuelSpace2 > 0)
        {
            if (amountToDrain > fuelSpace2) amountToDrain = fuelSpace2;
            this.gasTank.drain(amountToDrain, true);
            this.liquidTank2.fill(FluidRegistry.getFluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), true);
            this.fluidTank2Type = thisProduct;
        } else
            amountToDrain = 0;

        return amountToDrain;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("smeltingTicks");
        this.containingItems = this.readStandardItemsFromNBT(nbt);

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
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("smeltingTicks", this.processTicks);
        this.writeStandardItemsToNBT(nbt);

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
    }

    @Override
    protected ItemStack[] getContainingItems()
    {
        return this.containingItems;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public String getInventoryName()
    {
        return GCCoreUtil.translate("tile.marsMachine.4.name");
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        int metaside = this.getBlockMetadata() + 2;
        if (side == (metaside ^ 1))
        {
            return new int [] { 2 };
        }

        //2->5 3->4 4->2 5->3
        if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
            return new int [] { 3 };
        }

        if (side == metaside)
        {
            return new int [] { 1 };
        }

        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
            case 1:
                FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
                return stack != null && stack.getFluid() != null && stack.getFluid().getName().toLowerCase().contains("methane");
            case 2:
                return FluidContainerRegistry.isEmptyContainer(itemstack);
            case 3:
                return FluidContainerRegistry.isEmptyContainer(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || !this.shouldPullEnergy();
            case 1:
                return FluidContainerRegistry.isEmptyContainer(itemstack);
            case 2:
                return FluidContainerRegistry.isFilledContainer(itemstack);
            case 3:
                return FluidContainerRegistry.isFilledContainer(itemstack);
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
            FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
            return stack != null && stack.getFluid() != null && this.getIdFromName(stack.getFluid().getName()) > -1;
        case 2:
            return FluidContainerRegistry.isEmptyContainer(itemstack);
        case 3:
            return FluidContainerRegistry.isEmptyContainer(itemstack);
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
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.DOWN;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        int metaside = this.getBlockMetadata() + 2;
        int side = from.ordinal();
        if (side == (metaside ^ 1))
            return this.liquidTank.getFluid() != null && this.liquidTank.getFluidAmount() > 0;

        //2->5 3->4 4->2 5->3
        if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
            return this.liquidTank2.getFluid() != null && this.liquidTank2.getFluidAmount() > 0;

        return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        int metaside = this.getBlockMetadata() + 2;
        int side = from.ordinal();
        if (side == (metaside ^ 1))
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank.getFluid()))
                return this.liquidTank.drain(resource.amount, doDrain);
        }

        //2->5 3->4 4->2 5->3
        if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank2.getFluid()))
                return this.liquidTank2.drain(resource.amount, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        int metaside = this.getBlockMetadata() + 2;
        int side = from.ordinal();
        if (side == (metaside ^ 1))
        {
            return this.liquidTank.drain(maxDrain, doDrain);
        }

        //2->5 3->4 4->2 5->3
        if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
            return this.liquidTank2.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
        {
            //Can fill with gases
            return fluid != null && this.getIdFromName(fluid.getName()) > -1;
        }

        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
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
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};

        if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
        }
        else if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
        }

        return tankInfo;
    }

    @Override
    public int getBlockMetadata()
    {
        if (this.blockMetadata == -1)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata & 3;
    }
}