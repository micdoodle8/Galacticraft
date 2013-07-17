package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityOxygenBubble;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreTileEntityOxygenDistributor extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
    public boolean active;
    public boolean lastActive;

    private ItemStack[] containingItems = new ItemStack[1];

    public GCCoreEntityOxygenBubble oxygenBubble;

    public GCCoreTileEntityOxygenDistributor()
    {
        super(300, 130, 1, 1.0D, 6000, 12);
    }

    @Override
    public void invalidate()
    {
        if (this.oxygenBubble != null)
        {
            for (int x = (int) Math.floor(this.xCoord - this.oxygenBubble.getSize()); x < Math.ceil(this.xCoord + this.oxygenBubble.getSize()); x++)
            {
                for (int y = (int) Math.floor(this.yCoord - this.oxygenBubble.getSize()); y < Math.ceil(this.yCoord + this.oxygenBubble.getSize()); y++)
                {
                    for (int z = (int) Math.floor(this.zCoord - this.oxygenBubble.getSize()); z < Math.ceil(this.zCoord + this.oxygenBubble.getSize()); z++)
                    {
                        int blockID = this.worldObj.getBlockId(x, y, z);
                        
                        if (blockID > 0)
                        {
                            Block block = Block.blocksList[blockID];
                            
                            if (block instanceof IOxygenReliantBlock)
                            {
                                ((IOxygenReliantBlock) block).onOxygenRemoved(this.worldObj, x, y, z);
                            }
                        }
                    }
                }
            }
        }

        super.invalidate();
    }

    public double getDistanceFromServer(double par1, double par3, double par5)
    {
        final double d3 = this.xCoord + 0.5D - par1;
        final double d4 = this.yCoord + 0.5D - par3;
        final double d5 = this.zCoord + 0.5D - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (this.oxygenBubble == null)
        {
            this.oxygenBubble = new GCCoreEntityOxygenBubble(this.worldObj, new Vector3(this), this);

            if (!this.worldObj.isRemote)
            {
                this.worldObj.spawnEntityInWorld(this.oxygenBubble);
            }
        }

        if (!this.worldObj.isRemote)
        {
            if (this.oxygenBubble.getSize() >= 1 && (this.ueWattsReceived > 0 || this.ic2Energy > 0 || (this.getPowerReceiver(this.getElectricInputDirection()) != null && this.getPowerReceiver(this.getElectricInputDirection()).getEnergyStored() > 0)))
            {
                this.active = true;
            }
            else
            {
                this.active = false;
            }
        }
        
        if (!this.worldObj.isRemote && (this.active != this.lastActive || this.ticks % 20 == 0))
        {
            if (this.active)
            {
                for (int x = (int) Math.floor(this.xCoord - this.oxygenBubble.getSize() - 4); x < Math.ceil(this.xCoord + this.oxygenBubble.getSize() + 4); x++)
                {
                    for (int y = (int) Math.floor(this.yCoord - this.oxygenBubble.getSize() - 4); y < Math.ceil(this.yCoord + this.oxygenBubble.getSize() + 4); y++)
                    {
                        for (int z = (int) Math.floor(this.zCoord - this.oxygenBubble.getSize() - 4); z < Math.ceil(this.zCoord + this.oxygenBubble.getSize() + 4); z++)
                        {
                            int blockID = this.worldObj.getBlockId(x, y, z);
                            
                            if (blockID > 0)
                            {
                                Block block = Block.blocksList[blockID];
                                
                                if (block instanceof IOxygenReliantBlock)
                                {
                                    if (this.getDistanceFromServer(x, y, z) < Math.pow(this.oxygenBubble.getSize() - 0.5D, 2))
                                    {
                                        ((IOxygenReliantBlock) block).onOxygenAdded(this.worldObj, x, y, z);
                                    }
                                    else
                                    {
                                        ((IOxygenReliantBlock) block).onOxygenRemoved(this.worldObj, x, y, z);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        this.lastActive = this.active;
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
        return LanguageRegistry.instance().getStringLocalization("container.oxygendistributor.name");
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
    public void openChest()
    {

    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public boolean shouldPullEnergy()
    {
        return GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0;
    }

    @Override
    public void readPacket(ByteArrayDataInput data)
    {
        if (this.worldObj.isRemote)
        {
            this.storedOxygen = data.readInt();
            this.ueWattsReceived = data.readDouble();
            this.ic2Energy = data.readDouble();
            this.disabled = data.readBoolean();
            this.bcEnergy = data.readDouble();
        }
    }

    @Override
    public Packet getPacket()
    {
        return GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.storedOxygen, this.ueWattsReceived, this.ic2Energy, this.disabled, this.getPowerReceiver(this.getElectricInputDirection()) != null ? (double) this.getPowerReceiver(this.getElectricInputDirection()).getEnergyStored() : 0.0D);
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
        return this.ic2Energy > 0 || this.ueWattsReceived > 0 || this.getPowerReceiver(this.getElectricInputDirection()) != null && this.getPowerReceiver(this.getElectricInputDirection()).getEnergyStored() > 0;
    }
}
