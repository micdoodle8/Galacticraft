package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.oxygen.ThreadFindSeal;
import micdoodle8.mods.galacticraft.power.core.item.IItemElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;

/**
 * GCCoreTileEntityOxygenSealer.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityOxygenSealer extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
    public boolean sealed;
    public boolean lastSealed = false;

    public static final float WATTS_PER_TICK = 0.2F;
    public boolean lastDisabled = false;

    public boolean active;
    private ItemStack[] containingItems = new ItemStack[1];
    public ThreadFindSeal threadSeal;
    public int stopSealThreadCooldown;
    public boolean calculatingSealed;

    public GCCoreTileEntityOxygenSealer()
    {
        super(GCCoreTileEntityOxygenSealer.WATTS_PER_TICK, 50, 10000, 8);
    }

    public int getFindSealChecks()
    {
        if (!this.active || this.storedOxygen < this.oxygenPerTick || this.getEnergyStored() <= 0.0F)
        {
            return 0;
        }

        return 1250;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.stopSealThreadCooldown > 0)
            {
                this.stopSealThreadCooldown--;
            }

            if (this.threadSeal != null)
            {
                this.sealed = this.threadSeal.sealed;
                this.calculatingSealed = this.threadSeal.looping;
            }

            if (this.storedOxygen >= 1 && this.getEnergyStored() > 0 && !this.disabled)
            {
                this.active = true;
            }
            else
            {
                this.active = false;
            }

            if (this.ticks % 100 == 0 && this.stopSealThreadCooldown <= 0)
            {
                OxygenPressureProtocol.updateSealerStatus(this);
            }

            this.lastDisabled = this.disabled;
            this.lastSealed = this.sealed;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
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
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", list);
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
        return StatCollector.translateToLocal("container.oxygensealer.name");
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
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
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
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        return slotID == 0;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 ? itemstack.getItem() instanceof IItemElectric : false;
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0 && !this.getDisabled(0);
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        if (this.worldObj.isRemote)
        {
            this.storedOxygen = data.readInt();
            this.setEnergyStored(data.readFloat());
            this.disabled = data.readBoolean();
            this.sealed = data.readBoolean();
            this.calculatingSealed = data.readBoolean();
        }
    }

    @Override
    public Packet getPacket()
    {
        return GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.storedOxygen, this.getEnergyStored(), this.disabled, this.sealed, this.calculatingSealed);
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public ForgeDirection getOxygenInputDirection()
    {
        return this.getElectricInputDirection().getOpposite();
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return this.getEnergyStored() > 0;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return this.active && this.sealed;
    }
}
