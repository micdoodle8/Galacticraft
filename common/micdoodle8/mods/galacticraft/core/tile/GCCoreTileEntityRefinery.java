package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.API.IRefinableItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
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
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import buildcraft.api.power.PowerFramework;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreTileEntityRefinery extends GCCoreTileEntityElectric implements IInventory, ISidedInventory, ITankContainer
{
    private final int tankCapacity = 24000;
    public LiquidTank oilTank = new LiquidTank(this.tankCapacity);
    public LiquidTank fuelTank = new LiquidTank(this.tankCapacity);

    public static final int PROCESS_TIME_REQUIRED = 1000;
    public int processTicks = 0;
    private ItemStack[] containingItems = new ItemStack[3];

    private boolean checkOilNextTick = false;
    private boolean lastCheckOilNextTick = false;

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
                final LiquidStack liquid = LiquidContainerRegistry.getLiquidForFilledItem(this.containingItems[1]);

                if (liquid != null && LiquidDictionary.findLiquidName(liquid).equals("Oil"))
                {
                    if (this.oilTank.getLiquid() == null || this.oilTank.getLiquid().amount + liquid.amount <= this.oilTank.getCapacity())
                    {
                        this.oilTank.fill(liquid, true);

                        if (this.containingItems[1].getItem() instanceof GCCoreItemOilCanister)
                        {
                            this.containingItems[1] = new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage());
                        }
                        else if (LiquidContainerRegistry.isBucket(this.containingItems[1]) && LiquidContainerRegistry.isFilledContainer(this.containingItems[1]))
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

            if (this.containingItems[2] != null && LiquidContainerRegistry.isContainer(this.containingItems[2]))
            {
                final LiquidStack liquid = this.fuelTank.getLiquid();

                if (liquid != null && this.fuelTank.getLiquidName() != null && this.fuelTank.getLiquidName().equals("Fuel"))
                {
                    if (LiquidContainerRegistry.isEmptyContainer(this.containingItems[2]))
                    {
                        boolean isCanister = this.containingItems[2].isItemEqual(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()));
                        final int amountToFill = isCanister ? LiquidContainerRegistry.BUCKET_VOLUME * 2 : LiquidContainerRegistry.BUCKET_VOLUME;

                        if (isCanister)
                        {
                            float f = Float.valueOf(LiquidContainerRegistry.BUCKET_VOLUME * 2.0F) / Float.valueOf(GCCoreItems.fuelCanister.getMaxDamage());
                            int dam = GCCoreItems.fuelCanister.getMaxDamage() - Math.min(Math.max((int) Math.floor(liquid.amount / f), 1), 60);
                            this.containingItems[2] = new ItemStack(GCCoreItems.fuelCanister, 1, dam);
                        }
                        else
                        {
                            this.containingItems[2] = LiquidContainerRegistry.fillLiquidContainer(liquid, this.containingItems[2]);
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

            if (this.checkOilNextTick && this.lastCheckOilNextTick)
            {
                if (!this.getDisabled() && (this.oilTank == null || this.oilTank.getLiquid() == null || this.oilTank.getLiquid().amount == 0))
                {
                    this.setDisabled(true);
                    this.checkOilNextTick = false;
                }
            }

            this.lastCheckOilNextTick = this.checkOilNextTick;
        }
    }

    public int getScaledOilLevel(int i)
    {
        return this.oilTank.getLiquid() != null ? this.oilTank.getLiquid().amount * i / this.oilTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.fuelTank.getLiquid() != null ? this.fuelTank.getLiquid().amount * i / this.fuelTank.getCapacity() : 0;
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
        if (this.oilTank.getLiquid() == null || this.oilTank.getLiquid().amount <= 0)
        {
            return false;
        }

        if (this.getDisabled())
        {
            return false;
        }

        if (this.ic2Energy == 0 && (this.getPowerProvider() == null || this.getPowerProvider().getEnergyStored() == 0) && this.wattsReceived == 0)
        {
            return false;
        }

        return true;
    }

    public void smeltItem()
    {
        if (this.canProcess())
        {
            final int oilAmount = this.oilTank.getLiquid().amount;
            final int fuelSpace = this.fuelTank.getCapacity() - (this.fuelTank.getLiquid() == null ? 0 : this.fuelTank.getLiquid().amount);

            final int amountToDrain = Math.min(oilAmount, fuelSpace);

            this.oilTank.drain(amountToDrain, true);
            this.fuelTank.fill(LiquidDictionary.getLiquid("Fuel", amountToDrain), true);

            this.checkOilNextTick = true;
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

        if (this.oilTank.getLiquid() != null)
        {
            nbt.setTag("oilTank", this.oilTank.writeToNBT(new NBTTagCompound()));
        }

        if (this.fuelTank.getLiquid() != null)
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
        return slotID == 1 ? itemstack.getItem() instanceof IRefinableItem && ((IRefinableItem) itemstack.getItem()).canSmeltItem(itemstack) : slotID == 0 ? itemstack.getItem() instanceof IItemElectric : slotID == 2 ? true : false;
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
    public int fill(ForgeDirection from, LiquidStack resource, boolean doFill)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()))
        {
            return this.fill(0, resource, doFill);
        }

        return 0;
    }

    @Override
    public int fill(int tankIndex, LiquidStack resource, boolean doFill)
    {
        int used = 0;
        resource.copy();
        final String liquidName = LiquidDictionary.findLiquidName(resource);

        if (tankIndex == 0 && liquidName != null && liquidName.equals("Oil"))
        {
            used = this.oilTank.fill(resource, doFill);
        }

        return used;
    }

    @Override
    public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
        {
            return this.drain(0, maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain)
    {
        return this.fuelTank.drain(maxDrain, doDrain);
    }

    @Override
    public ILiquidTank[] getTanks(ForgeDirection direction)
    {
        return new ILiquidTank[] { this.oilTank, this.fuelTank };
    }

    @Override
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type)
    {
        if (direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite())
        {
            // OIL
            return this.oilTank;
        }
        else if (direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
        {
            // FUEL
            return this.fuelTank;
        }

        return null;
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return !this.getDisabled() && this.oilTank.getLiquid() != null && this.oilTank.getLiquid().amount > 0;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        if (this.worldObj.isRemote)
        {
            this.wattsReceived = data.readDouble();
            this.processTicks = data.readInt();
            this.ic2Energy = data.readDouble();
            this.oilTank.setLiquid(new LiquidStack(GCCoreBlocks.crudeOilStill.blockID, data.readInt(), 0));
            this.fuelTank.setLiquid(new LiquidStack(GCCoreItems.fuel.itemID, data.readInt(), 0));
            this.disabled = data.readBoolean();
            this.bcEnergy = data.readDouble();
        }
    }

    @Override
    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.wattsReceived, this.processTicks, this.ic2Energy, this.oilTank.getLiquid() == null ? 0 : this.oilTank.getLiquid().amount, this.fuelTank.getLiquid() == null ? 0 : this.fuelTank.getLiquid().amount, this.disabled, this.getPowerProvider() != null ? (double) this.getPowerProvider().getEnergyStored() : 0.0D);
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
}
