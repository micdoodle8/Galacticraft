package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOilCanister;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import buildcraft.api.power.PowerFramework;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreTileEntityRefinery extends GCCoreTileEntityElectric implements IInventory, ISidedInventory, IFluidHandler
{
    private final int tankCapacity = 24000;
    public FluidTank oilTank = new FluidTank(this.tankCapacity);
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);

    public static final int PROCESS_TIME_REQUIRED = 1000;
    public int processTicks = 0;
    private ItemStack[] containingItems = new ItemStack[3];

    public GCCoreTileEntityRefinery()
    {
        super(600, 130, 1, 1.5D);

        if (PowerFramework.currentFramework != null)
        {
            this.bcPowerProvider = new GCCoreLinkedPowerProvider(this);
            this.bcPowerProvider.configure(20, 15, 100, 25, 1000);
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.containingItems[1] != null)
            {
                FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[1]);

                if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Oil"))
                {
                    if (this.oilTank.getFluid() == null || this.oilTank.getFluid().amount + liquid.amount <= this.oilTank.getCapacity())
                    {
                        this.oilTank.fill(liquid, true);

                        if (this.containingItems[1].getItem() instanceof GCCoreItemOilCanister)
                        {
                            this.containingItems[1] = new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage());
                        }
                        else if (FluidContainerRegistry.isBucket(this.containingItems[1]) && FluidContainerRegistry.isFilledContainer(this.containingItems[1]))
                        {
                            final int amount = this.containingItems[1].stackSize;
                            this.containingItems[1] = new ItemStack(Item.bucketEmpty, amount);
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

            if (this.containingItems[2] != null && FluidContainerRegistry.isContainer(this.containingItems[2]))
            {
                final FluidStack liquid = this.fuelTank.getFluid();

                if (liquid != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid().getName().equalsIgnoreCase("Fuel"))
                {
                    if (FluidContainerRegistry.isEmptyContainer(this.containingItems[2]))
                    {
                        boolean isCanister = this.containingItems[2].isItemEqual(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()));
                        final int amountToFill = isCanister ? FluidContainerRegistry.BUCKET_VOLUME * 2 : FluidContainerRegistry.BUCKET_VOLUME;

                        if (isCanister)
                        {
                            this.containingItems[2] = new ItemStack(GCCoreItems.fuelCanister, 1, GCCoreItems.fuelCanister.getMaxDamage() - liquid.amount);
                        }
                        else
                        {
                            this.containingItems[2] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[2]);
                        }

                        this.fuelTank.drain(amountToFill, true);
                    }
                }
            }

            if (this.canProcess())
            {
                if (this.processTicks == 0)
                {
                    this.processTicks = GCCoreTileEntityRefinery.PROCESS_TIME_REQUIRED;
                }
                else if (this.processTicks > 0)
                {
                    this.processTicks--;

                    if (this.processTicks < 1)
                    {
                        this.smeltItem();
                        this.processTicks = 0;
                    }
                }
                else
                {
                    this.processTicks = 0;
                }
            }
            else
            {
                this.processTicks = 0;
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

    @Override
    public void openChest()
    {
        if (!this.worldObj.isRemote)
        {
            PacketManager.sendPacketToClients(this.getPacket(), this.worldObj, new Vector3(this), 15);
        }
    }

    @Override
    public void closeChest()
    {
    }

    public boolean canProcess()
    {
        if (this.oilTank.getFluid() == null || this.oilTank.getFluid().amount <= 0)
        {
            return false;
        }

        if (this.getDisabled())
        {
            return false;
        }

        if (this.ic2Energy == 0 && (this.getPowerProvider() == null || this.getPowerProvider().getEnergyStored() == 0) && this.ueWattsReceived == 0)
        {
            return false;
        }

        return true;
    }

    public void smeltItem()
    {
        if (this.canProcess())
        {
            final int oilAmount = this.oilTank.getFluid().amount;
            final int fuelSpace = this.fuelTank.getCapacity() - (this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);

            final int amountToDrain = Math.min(oilAmount, fuelSpace);

            this.oilTank.drain(amountToDrain, true);
            this.fuelTank.fill(FluidRegistry.getFluidStack("fuel", amountToDrain), true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("smeltingTicks");
        final NBTTagList var2 = nbt.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            final byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

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
        final NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);

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
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            final ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName()
    {
        return LanguageRegistry.instance().getStringLocalization("container.refinery.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isStackValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 ? itemstack.getItem() instanceof IItemElectric : true;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return side == 1 ? new int[] { 1 } : side == 0 ? new int[] { 0 } : new int[] { 2 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        return this.isStackValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        return slotID == 2;
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return !this.getDisabled() && this.oilTank.getFluid() != null && this.oilTank.getFluid().amount > 0;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        if (this.worldObj.isRemote)
        {
            this.ueWattsReceived = data.readDouble();
            this.processTicks = data.readInt();
            this.ic2Energy = data.readDouble();
            this.oilTank.setFluid(new FluidStack(GalacticraftCore.CRUDEOIL, data.readInt()));
            this.fuelTank.setFluid(new FluidStack(GalacticraftCore.FUEL, data.readInt()));
            this.disabled = data.readBoolean();
            this.bcEnergy = data.readDouble();
        }
    }

    @Override
    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.ueWattsReceived, this.processTicks, this.ic2Energy, this.oilTank.getFluid() == null ? 0 : this.oilTank.getFluid().amount, this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount, this.disabled, this.getPowerProvider() != null ? (double) this.getPowerProvider().getEnergyStored() : 0.0D);
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.UP;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
        {
            return this.fuelTank.getFluid() != null && this.fuelTank.getFluidAmount() > 0;
        }
        
        return false;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
        {
            return this.fuelTank.drain(resource.amount, doDrain);
        }
        
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
        {
            return this.drain(from, new FluidStack(GalacticraftCore.FUEL, maxDrain), doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()))
        {
            return this.oilTank.getFluid() == null || (this.oilTank.getFluidAmount() < this.oilTank.getCapacity());
        }
        
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int used = 0;
        
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()))
        {
            final String liquidName = FluidRegistry.getFluidName(resource);

            if (liquidName != null && liquidName.equalsIgnoreCase("Oil"))
            {
                used = this.oilTank.fill(resource, doFill);
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
            tankInfo = new FluidTankInfo[] {new FluidTankInfo(this.oilTank)};
        }
        else if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
        {
            tankInfo = new FluidTankInfo[] {new FluidTankInfo(this.fuelTank)};
        }
        
        return tankInfo;
    }
}
