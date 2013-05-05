package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenCompressor extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
	private ItemStack[] containingItems = new ItemStack[2];
	
	public GCCoreTileEntityOxygenCompressor()
	{
		super (300, 130, 1, 0.75D, 1200, 12);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if ((this.storedOxygen / 5.0D) >= 1.0D && (this.wattsReceived > 0 || this.ic2Energy > 0 || this.bcEnergy > 0))
			{
				if (!this.worldObj.isRemote && this.ticks % ((31 - Math.min(Math.floor((this.storedOxygen / 5.0D)), 30)) * 10) == 0)
				{
					final ItemStack stack = this.getStackInSlot(0);

					if (stack != null && stack.getItem() instanceof GCCoreItemOxygenTank && stack.getItemDamage() > 0)
					{
						stack.setItemDamage(stack.getItemDamage() - 1);
					}
					else if (stack != null && stack.getItem() instanceof GCCoreItemOxygenTank)
					{
						stack.setItemDamage(0);
					}
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
            final NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
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
                var4.setByte("Slot", (byte)var3);
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
		return "Oxygen Compressor";
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
	public void openChest() {}

	@Override
	public void closeChest() {}

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 1 || side == 0 ? new int[] {1} : new int[] {0};
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
		return slotID == 0 ? itemstack != null && itemstack.getItem() instanceof GCCoreItemOxygenTank && itemstack.getItemDamage() > 0 : slotID == 1 ? itemstack.getItem() instanceof IItemElectric : false;
	}

	@Override
	public boolean shouldPullEnergy() 
	{
		return this.timeSinceOxygenRequest > 0 && this.getStackInSlot(0) != null;
	}

	@Override
	public void readPacket(ByteArrayDataInput data) 
	{
		if (this.worldObj.isRemote)
		{
			this.storedOxygen = data.readInt();
			this.wattsReceived = data.readDouble();
			this.ic2Energy = data.readDouble();
			this.disabled = data.readBoolean();
			this.bcEnergy = data.readDouble();
		}
	}

	@Override
	public Packet getPacket() 
	{
		return PacketManager.getPacket(GalacticraftCore.CHANNEL, this, this.storedOxygen, this.wattsReceived, this.ic2Energy, this.disabled, this.bcEnergy);
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ItemStack getBatteryInSlot() 
	{
		return this.getStackInSlot(1);
	}

	@Override
	public ForgeDirection getOxygenInputDirection()
	{
		return this.getElectricInputDirection().getOpposite();
	}

	@Override
	public boolean shouldPullOxygen() 
	{
		return this.ic2Energy > 0 || this.wattsReceived > 0 || this.bcEnergy > 0;
	}
}