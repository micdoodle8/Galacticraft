package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityElectrolyzer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandler
{
    private final int tankCapacity = 4000;

    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank gasTank = new FluidTank(this.tankCapacity);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    private ItemStack[] containingItems = new ItemStack[1];

    public TileEntityElectrolyzer()
    {
        this.storage.setMaxExtract(60);
        this.setTierGC(2);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
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

    public boolean canProcess()
    {
        return false;
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
                return ItemElectricBase.isElectricItem(itemstack.getItem());
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
        return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return 0;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};

        if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
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