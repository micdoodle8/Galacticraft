package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.gas.EnumGas;
import mekanism.api.gas.GasTransmission;
import mekanism.api.gas.IGasAcceptor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreTileEntityOxygenDecompressor extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
    private ItemStack[] containingItems = new ItemStack[2];

    public static final float WATTS_PER_TICK = 0.2F;
    
    public static final int OUTPUT_PER_TICK = 100;

    public GCCoreTileEntityOxygenDecompressor()
    {
        super(GCCoreTileEntityOxygenDecompressor.WATTS_PER_TICK, 50, 1200, 0);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.containingItems[0] != null && this.getEnergyStored() > 0.0F && this.storedOxygen < this.maxOxygen)
            {
                ItemStack tank1 = this.containingItems[0];
                
                if (tank1.getItem() instanceof GCCoreItemOxygenTank && tank1.getItemDamage() < tank1.getMaxDamage())
                {
                    tank1.setItemDamage(tank1.getItemDamage() + 1);
                    this.storedOxygen += 1;
                }
            }
            
            int gasToSend = (int) Math.min(this.storedOxygen, OUTPUT_PER_TICK);
            
            this.storedOxygen -= gasToSend - GasTransmission.emitGasToNetwork(EnumGas.OXYGEN, gasToSend, this, this.getOxygenOutputDirection());

            final TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), this.getOxygenOutputDirection());

            if (tileEntity instanceof IGasAcceptor)
            {
                if (((IGasAcceptor) tileEntity).canReceiveGas(this.getOxygenOutputDirection().getOpposite(), EnumGas.OXYGEN))
                {
                    double sendingGas = 0;

                    if (this.storedOxygen >= OUTPUT_PER_TICK)
                    {
                        sendingGas = OUTPUT_PER_TICK;
                    }
                    else
                    {
                        sendingGas = this.storedOxygen;
                    }

                    this.storedOxygen -= sendingGas - ((IGasAcceptor) tileEntity).transferGasToAcceptor(MathHelper.floor_double(sendingGas), EnumGas.OXYGEN);
                }
            }
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
        return StatCollector.translateToLocal("container.oxygendecompressor.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
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
                return itemstack.getItemDamage() > 1;
            case 1:
                return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) > 0;
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
                return itemstack.getItemDamage() == 0;
            case 1:
                return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || !this.shouldPullEnergy();
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
            return itemstack.getItem() instanceof GCCoreItemOxygenTank;
        case 1:
            return itemstack.getItem() instanceof IItemElectric;
        }

        return false;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0 && this.getStackInSlot(0) != null;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        if (this.worldObj.isRemote)
        {
            this.storedOxygen = data.readInt();
            this.setEnergyStored(data.readFloat());
            this.disabled = data.readBoolean();
        }
    }

    @Override
    public Packet getPacket()
    {
        return PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.storedOxygen, this.getEnergyStored(), this.disabled);
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(1);
    }

    @Override
    public ForgeDirection getOxygenInputDirection()
    {
        return null;
    }

    @Override
    public boolean canTubeConnect(ForgeDirection direction)
    {
        return direction == this.getOxygenOutputDirection();
    }
    
    public ForgeDirection getOxygenOutputDirection()
    {
        return this.getElectricInputDirection().getOpposite();
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return false;
    }
}
