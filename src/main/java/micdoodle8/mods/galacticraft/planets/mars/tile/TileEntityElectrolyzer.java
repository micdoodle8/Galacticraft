package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.relauncher.Side;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityElectrolyzer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandler
{
    private final int tankCapacity = 4000;

    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank waterTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank liquidTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank liquidTank2 = new FluidTank(this.tankCapacity);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    private ItemStack[] containingItems = new ItemStack[4];
    
    public TileEntityElectrolyzer()
    {
        this.storage.setMaxExtract(120);
        this.setTierGC(2);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.containingItems[1] != null)
            {
                final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[1]);

                if (liquid != null && liquid.getFluid().getName().equals(FluidRegistry.WATER.getName()))
                {
                    if (this.waterTank.getFluid() == null || this.waterTank.getFluid().amount + liquid.amount <= this.waterTank.getCapacity())
                    {
                        this.waterTank.fill(liquid, true);

                        if (FluidContainerRegistry.isBucket(this.containingItems[1]) && FluidContainerRegistry.isFilledContainer(this.containingItems[1]))
                        {
                            this.containingItems[1] = new ItemStack(Items.bucket, this.containingItems[1].stackSize);
                        }
                        else
                        {
                            this.containingItems[1].stackSize--;

                            if (this.containingItems[1].stackSize == 0)
                            {
                                this.containingItems[1] = null;
                            }
                        }
                    }
                }
            }

            //Only drain with atmospheric valve
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
                        this.doElectrolysis();
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

    private void doElectrolysis()
    {
        //Can't be called if the gasTank fluid is null
        final int waterAmount = this.waterTank.getFluid().amount;
        if (waterAmount == 0) return;

        this.placeIntoFluidTanks(2);
        this.waterTank.drain(1, true);
    }

    private int placeIntoFluidTanks(int amountToDrain)
    {
        final int fuelSpace = this.liquidTank.getCapacity() - this.liquidTank.getFluidAmount();
        final int fuelSpace2 = this.liquidTank2.getCapacity() - this.liquidTank2.getFluidAmount();
        int amountToDrain2 = amountToDrain * 2;
        
        if (amountToDrain > fuelSpace) amountToDrain = fuelSpace;
        this.liquidTank.fill(FluidRegistry.getFluidStack("oxygen", amountToDrain), true);

        if (amountToDrain2 > fuelSpace2) amountToDrain2 = fuelSpace2;
        this.liquidTank2.fill(FluidRegistry.getFluidStack("hydrogen", amountToDrain2), true);

        return amountToDrain;
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        if (this.containingItems[slot] != null && this.containingItems[slot].getItem() instanceof ItemAtmosphericValve)
        {
            tank.drain(4, true);
        }
    }

    public int getScaledGasLevel(int i)
    {
        return this.waterTank.getFluid() != null ? this.waterTank.getFluid().amount * i / this.waterTank.getCapacity() : 0;
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
        if (this.waterTank.getFluid() == null || this.waterTank.getFluid().amount <= 0 || this.getDisabled(0))
        {
            return false;
        }

        boolean tank1HasSpace = this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
        boolean tank2HasSpace = this.liquidTank2.getFluidAmount() < this.liquidTank2.getCapacity();

        return tank1HasSpace || tank2HasSpace;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("processTicks");
        this.containingItems = this.readStandardItemsFromNBT(nbt);

        if (nbt.hasKey("waterTank"))
        {
            this.waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
        }

        if (nbt.hasKey("gasTank"))
        {
            this.liquidTank.readFromNBT(nbt.getCompoundTag("gasTank"));
        }
        if (nbt.hasKey("gasTank2"))
        {
            this.liquidTank2.readFromNBT(nbt.getCompoundTag("gasTank2"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("processTicks", this.processTicks);
        this.writeStandardItemsToNBT(nbt);

        if (this.waterTank.getFluid() != null)
        {
            nbt.setTag("waterTank", this.waterTank.writeToNBT(new NBTTagCompound()));
        }

        if (this.liquidTank.getFluid() != null)
        {
            nbt.setTag("gasTank", this.liquidTank.writeToNBT(new NBTTagCompound()));
        }
        if (this.liquidTank2.getFluid() != null)
        {
            nbt.setTag("gasTank2", this.liquidTank2.writeToNBT(new NBTTagCompound()));
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
        return GCCoreUtil.translate("tile.marsMachine.6.name");
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItem(itemstack.getItem());
            case 1:
            	return itemstack.getItem() == Items.water_bucket;
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
                return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0;
            case 1:
            	return itemstack.getItem() == Items.bucket;
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        Item item = itemstack.getItem(); 
        switch (slotID)
        {
        case 0:
            return ItemElectricBase.isElectricItem(item);
        case 1:
        	return item == Items.bucket || item == Items.water_bucket;
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
            if (resource != null && resource.isFluidEqual(this.liquidTank2.getFluid()))
                return this.liquidTank2.drain(resource.amount, doDrain);
        }

        //2->5 3->4 4->2 5->3
        if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank.getFluid()))
                return this.liquidTank.drain(resource.amount, doDrain);
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
            return this.liquidTank2.drain(maxDrain, doDrain);
        }

        //2->5 3->4 4->2 5->3
        if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
            return this.liquidTank.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (from.ordinal() == this.getBlockMetadata() + 2)
        {
            //Can fill with water
            return fluid != null && fluid.getName().equals(FluidRegistry.WATER.getName());
        }

        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            used = this.waterTank.fill(resource, doFill);
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
        int metaside = this.getBlockMetadata() + 2;
        int side = from.ordinal();

        if (metaside == side)
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.waterTank) };
        }
        else if (metaside == (side ^ 1))
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank2) };
        }
        else if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
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

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public int receiveGas(ForgeDirection side, GasStack stack)
    {
        return 0;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public GasStack drawGas(ForgeDirection from, int amount)
    {
        int metaside = this.getBlockMetadata() + 2;
        int side = from.ordinal();
    	if (metaside == (side ^ 1))
        {
    		int amountH = Math.min(8, this.liquidTank2.getFluidAmount());
    		amountH = this.liquidTank2.drain(amountH, true).amount;
    		return new GasStack((Gas) EnergyConfigHandler.gasHydrogen, amountH);
        }
        else if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
    		int amountO = Math.min(8, this.liquidTank.getFluidAmount());
    		amountO = this.liquidTank.drain(amountO, true).amount;
    		return new GasStack((Gas) EnergyConfigHandler.gasOxygen, amountO);
        }
        return null;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public boolean canReceiveGas(ForgeDirection side, Gas type)
    {
    	return false;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public boolean canDrawGas(ForgeDirection from, Gas type)
    {
        int metaside = this.getBlockMetadata() + 2;
        int side = from.ordinal();
    	if (metaside == (side ^ 1))
        {
    		return type.getName().equals("hydrogen");
    	}
        else if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
        	return type.getName().equals("oxygen");
        }
        return false;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
    public boolean canTubeConnect(ForgeDirection from)
    {
        int metaside = this.getBlockMetadata() + 2;
        int side = from.ordinal();
    	if (metaside == (side ^ 1))
        {
    		return true;
    	}
        else if (7 - (metaside ^ (metaside > 3 ? 0 : 1)) == (side ^ 1))
        {
        	return true;
        }
        return false;
    }
}