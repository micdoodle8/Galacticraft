package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreTileEntityOxygenSealer extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
    public boolean sealed;
    public boolean lastSealed = false;

    public boolean lastDisabled = false;

    public boolean active;
    private ItemStack[] containingItems = new ItemStack[1];

    public GCCoreTileEntityOxygenSealer()
    {
        super(300, 130, 1, 1.0D, 10000, 12);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.ticks % 10 == 0)
            {
                this.sealed = this.checkSeal(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }

            if (this.storedOxygen >= 1 && (this.ueWattsReceived > 0 || this.ic2Energy > 0 || this.getPowerProvider() != null && this.getPowerProvider().getEnergyStored() > 0) && !this.disabled)
            {
                this.active = true;
            }
            else
            {
                this.active = false;
            }

            if (this.lastSealed != this.sealed || this.lastDisabled != this.disabled || this.ticks % 200 == 0)
            {
                if (this.active)
                {
                    this.sealArea(this.worldObj, this.xCoord, this.yCoord, this.zCoord, (int) (this.storedOxygen / 10.0D));
                }
                else
                {
                    this.unSealArea(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                }
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
        return LanguageRegistry.instance().getStringLocalization("container.oxygensealer.name");
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
        return this.isStackValidForSlot(slotID, itemstack);
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
    public boolean isStackValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 ? itemstack.getItem() instanceof IItemElectric : false;
    }

    private boolean checkSeal(World var1, int var2, int var3, int var4)
    {
        final OxygenPressureProtocol protocol = new OxygenPressureProtocol();
        return protocol.checkSeal(var1, var2, var3, var4, 3);
    }

    private void sealArea(World var1, int var2, int var3, int var4, int var5)
    {
        final OxygenPressureProtocol protocol = new OxygenPressureProtocol();
        protocol.seal(var1, var2, var3, var4, 2);
    }

    private void unSealArea(World var1, int var2, int var3, int var4)
    {
        final OxygenPressureProtocol protocol = new OxygenPressureProtocol();
        protocol.unSeal(var1, var2, var3, var4);
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0 && !this.disabled;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        if (this.worldObj.isRemote)
        {
            this.storedOxygen = data.readInt();
            this.ueWattsReceived = data.readDouble();
            this.disabled = data.readBoolean();
            this.ic2Energy = data.readDouble();
            this.sealed = data.readBoolean();
            this.bcEnergy = data.readDouble();
        }
    }

    @Override
    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.storedOxygen, this.ueWattsReceived, this.disabled, this.ic2Energy, this.sealed, this.getPowerProvider() != null ? (double) this.getPowerProvider().getEnergyStored() : 0.0D);
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
        return this.ic2Energy > 0 || this.ueWattsReceived > 0 || this.getPowerProvider() != null && this.getPowerProvider().getEnergyStored() > 0;
    }
}
