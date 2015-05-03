package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityRefinery extends TileBaseElectricBlockWithInventory implements ISidedInventory, IFluidHandler
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
    private ItemStack[] containingItems = new ItemStack[3];

    public TileEntityRefinery()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 90 : 60);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.containingItems[1] != null)
            {
                if (this.containingItems[1].getItem() instanceof ItemCanisterGeneric)
                {
	                if (this.containingItems[1].getItem() == GCItems.oilCanister)
	                {
	                	int originalDamage = this.containingItems[1].getItemDamage();
	                	int used = this.oilTank.fill(new FluidStack(GalacticraftCore.fluidOil, ItemCanisterGeneric.EMPTY - originalDamage), true);
	                	this.containingItems[1] = new ItemStack(GCItems.oilCanister, 1, originalDamage + used);
	                }
            	}
                else
                {
	                FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[1]);
	
	                if (liquid != null)
	                {
	                	boolean isOil = false;
		                if (FluidRegistry.getFluidName(liquid).equalsIgnoreCase("oil")) isOil = true;
		                if (FluidRegistry.getFluidName(liquid).equalsIgnoreCase("oilgc")) isOil = true;
		
		                if (isOil)
		                {
		                    if (this.oilTank.getFluid() == null || this.oilTank.getFluid().amount + liquid.amount <= this.oilTank.getCapacity())
		                    {
		                        this.oilTank.fill(new FluidStack(GalacticraftCore.fluidOil, liquid.amount), true);
		
		                        if (FluidContainerRegistry.isBucket(this.containingItems[1]) && FluidContainerRegistry.isFilledContainer(this.containingItems[1]))
		                        {
		                            final int amount = this.containingItems[1].stackSize;
		                            if (amount > 1) this.oilTank.fill(new FluidStack(GalacticraftCore.fluidOil, (amount - 1) * FluidContainerRegistry.BUCKET_VOLUME), true);
		                            this.containingItems[1] = new ItemStack(Items.bucket, amount);
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
        if (FluidUtil.isValidContainer(this.containingItems[slot]))
        {
            final FluidStack liquid = tank.getFluid();

            if (liquid != null && liquid.amount > 0)
            {
                String liquidname = liquid.getFluid().getName();
                if (liquidname.equals("fuel"))
                {
                    FluidUtil.tryFillContainer(tank, liquid, this.containingItems, slot, GCItems.fuelCanister);
                }
            }
        }
    }

    public int getScaledOilLevel(int i)
    {
        return this.oilTank.getFluid() != null ? this.oilTank.getFluid().amount * i / this.oilTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.fuelTank.getFluid() != null ? this.fuelTank.getFluid().amount * i / this.fuelTank.getCapacity() : 0;
    }

    public boolean canProcess()
    {
        if (this.oilTank.getFluid() == null || this.oilTank.getFluid().amount <= 0)
        {
            return false;
        }

        return !this.getDisabled(0);

    }

    public void smeltItem()
    {
        if (this.canProcess())
        {
            final int oilAmount = this.oilTank.getFluid().amount;
            final int fuelSpace = this.fuelTank.getCapacity() - (this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);

            final int amountToDrain = Math.min(Math.min(oilAmount, fuelSpace), TileEntityRefinery.OUTPUT_PER_SECOND);

            this.oilTank.drain(amountToDrain, true);
            this.fuelTank.fill(FluidRegistry.getFluidStack("fuel", amountToDrain), true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("smeltingTicks");
        this.containingItems = this.readStandardItemsFromNBT(nbt);

        if (nbt.hasKey("oilTank"))
        {
            this.oilTank.readFromNBT(nbt.getCompoundTag("oilTank"));
        }

        if (nbt.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("smeltingTicks", this.processTicks);
        this.writeStandardItemsToNBT(nbt);

        if (this.oilTank.getFluid() != null)
        {
            nbt.setTag("oilTank", this.oilTank.writeToNBT(new NBTTagCompound()));
        }

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }
    }


    @Override
    protected ItemStack[] getContainingItems()
    {
        return this.containingItems;
    }

    @Override
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.refinery.name");
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0, 1, 2 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
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
                return FluidUtil.isEmptyContainer(itemstack, GCItems.fuelCanister);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
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
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.UP;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (from.equals(ForgeDirection.getOrientation((this.getBlockMetadata() + 2) ^ 1)))
        {
            return this.fuelTank.getFluid() != null && this.fuelTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (from.equals(ForgeDirection.getOrientation((this.getBlockMetadata() + 2) ^ 1)))
        {
            return this.fuelTank.drain(resource.amount, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (from.equals(ForgeDirection.getOrientation((this.getBlockMetadata() + 2) ^ 1)))
        {
            return this.drain(from, new FluidStack(GalacticraftCore.fluidFuel, maxDrain), doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
        {
            return this.oilTank.getFluid() == null || this.oilTank.getFluidAmount() < this.oilTank.getCapacity();
        }

        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
        {
            final String liquidName = FluidRegistry.getFluidName(resource);

            if (liquidName != null && liquidName.equalsIgnoreCase("oil"))
            {
                used = this.oilTank.fill(resource, doFill);
            } else
            if (liquidName != null && liquidName.equalsIgnoreCase("oilgc"))
            {
                used = this.oilTank.fill(new FluidStack(GalacticraftCore.fluidOil, resource.amount), doFill);
            }            	
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] { };

        if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.oilTank) };
        }
        else if (from == ForgeDirection.getOrientation((this.getBlockMetadata() + 2) ^ 1))
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
        }

        return tankInfo;
    }
}
