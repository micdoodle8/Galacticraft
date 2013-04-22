package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityOxygenBubble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenDistributor extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
	public boolean active;
	
	private ItemStack[] containingItems = new ItemStack[1];

	public GCCoreEntityOxygenBubble oxygenBubble;

    public GCCoreTileEntityOxygenDistributor()
    {
		super(300, 130, 1, 6000, 12);
	}
	
    @Override
  	public void invalidate()
  	{
    	for (int x = (int) Math.floor(this.xCoord - (this.storedOxygen / 600.0D) * 1.5); x < Math.ceil(this.xCoord + (this.storedOxygen / 600.0D) * 1.5); x++)
    	{
        	for (int y = (int) Math.floor(this.yCoord - (this.storedOxygen / 600.0D) * 1.5); y < Math.ceil(this.yCoord + (this.storedOxygen / 600.0D) * 1.5); y++)
        	{
            	for (int z = (int) Math.floor(this.zCoord - (this.storedOxygen / 600.0D) * 1.5); z < Math.ceil(this.zCoord + (this.storedOxygen / 600.0D) * 1.5); z++)
            	{
            		final TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);
            		
            		if (tile != null && tile instanceof GCCoreTileEntityUnlitTorch)
            		{
            			tile.worldObj.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, GCCoreBlocks.unlitTorch.blockID, 0, 3);
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
			if ((this.storedOxygen / 600.0D) >= 1 && (this.wattsReceived > 0 || this.ic2Energy > 0))
			{
				this.active = true;
			}
			else
			{
				this.active = false;

		    	for (int x = (int) Math.floor(this.xCoord - (this.storedOxygen / 600.0D) * 1.5); x < Math.ceil(this.xCoord + (this.storedOxygen / 600.0D) * 1.5); x++)
		    	{
		        	for (int y = (int) Math.floor(this.yCoord - (this.storedOxygen / 600.0D) * 1.5); y < Math.ceil(this.yCoord + (this.storedOxygen / 600.0D) * 1.5); y++)
		        	{
		            	for (int z = (int) Math.floor(this.zCoord - (this.storedOxygen / 600.0D) * 1.5); z < Math.ceil(this.zCoord + (this.storedOxygen / 600.0D) * 1.5); z++)
		            	{
		            		final TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

		            		if (tile != null && tile instanceof GCCoreTileEntityUnlitTorch)
		            		{
		            			tile.worldObj.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, GCCoreBlocks.unlitTorch.blockID, 0, 3);
		            		}
		            	}
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
		return "Oxygen Distributor";
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
	public int[] getSizeInventorySide(int side)
	{
		return new int[] {0};
	}

	@Override
	public boolean func_102007_a(int slotID, ItemStack itemstack, int side)
	{
		return this.isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean func_102008_b(int slotID, ItemStack itemstack, int side)
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
		return this.timeSinceOxygenRequest > 0;
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
		}
	}

	@Override
	public Packet getPacket() 
	{
		return PacketManager.getPacket(BasicComponents.CHANNEL, this, this.storedOxygen, this.wattsReceived, this.ic2Energy, this.disabled);
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
		return true;
	}
}