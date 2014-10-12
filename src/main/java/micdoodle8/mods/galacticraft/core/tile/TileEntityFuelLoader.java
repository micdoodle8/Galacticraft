package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.entity.IFuelable;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemFuelCanister;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityFuelLoader extends TileBaseElectricBlockWithInventory implements ISidedInventory, IFluidHandler, ILandingPadAttachable
{
    private final int tankCapacity = 12000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);

    private ItemStack[] containingItems = new ItemStack[2];

    public IFuelable attachedFuelable;

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
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.containingItems[1] != null)
            {
                final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[1]);

                if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Fuel"))
                {
                    if (this.fuelTank.getFluid() == null || this.fuelTank.getFluid().amount + liquid.amount <= this.fuelTank.getCapacity())
                    {
                        this.fuelTank.fill(liquid, true);

                        if (this.containingItems[1].getItem() instanceof ItemFuelCanister)
                        {
                            this.containingItems[1] = new ItemStack(GCItems.oilCanister, 1, GCItems.oilCanister.getMaxDamage());
                        }
                        else if (FluidContainerRegistry.isBucket(this.containingItems[1]) && FluidContainerRegistry.isFilledContainer(this.containingItems[1]))
                        {
                            final int amount = this.containingItems[1].stackSize;
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

            if (this.ticks % 100 == 0)
            {
                this.attachedFuelable = null;

                for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                {
                    final TileEntity pad = new BlockVec3(this).getTileEntityOnSide(this.worldObj, dir);

                    if (pad instanceof TileEntityMulti)
                    {
                        final TileEntity mainTile = ((TileEntityMulti) pad).mainBlockPosition.getTileEntity(this.worldObj);

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
                final FluidStack liquid = new FluidStack(GalacticraftCore.fluidFuel, 2);

                if (this.attachedFuelable != null && this.hasEnoughEnergyToRun && !this.disabled)
                {
                    if (liquid != null)
                    {
                        this.fuelTank.drain(this.attachedFuelable.addFuel(liquid, true), true);
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.containingItems = this.readStandardItemsFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(par1NBTTagCompound.getCompoundTag("fuelTank"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        this.writeStandardItemsToNBT(par1NBTTagCompound);

        if (this.fuelTank.getFluid() != null)
        {
            par1NBTTagCompound.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
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
        return GCCoreUtil.translate("container.fuelloader.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
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
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        if (slotID == 1 && itemstack != null)
        {
           	return FluidContainerRegistry.isEmptyContainer(itemstack);
        }
        return false;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return (slotID == 1 && itemstack != null && itemstack.getItem() == GCItems.fuelCanister) || (slotID == 0 ? ItemElectricBase.isElectricItem(itemstack.getItem()) : false);
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
        return this.fuelTank.getFluid() == null || this.fuelTank.getFluidAmount() < this.fuelTank.getCapacity();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()))
        {
            final String liquidName = FluidRegistry.getFluidName(resource);

            if (liquidName != null && liquidName.equalsIgnoreCase("Fuel"))
            {
                used = this.fuelTank.fill(resource, doFill);
            }
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.fuelTank.getFluid() != null && this.fuelTank.getFluid().amount > 0 && !this.getDisabled(0);
    }

    @Override
    public boolean canAttachToLandingPad(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }
}
