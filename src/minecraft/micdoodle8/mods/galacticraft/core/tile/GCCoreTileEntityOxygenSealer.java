package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.API.IDisableableMachine;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenSealer extends GCCoreTileEntityElectric implements IInventory, IGasAcceptor, ITubeConnection, ISidedInventory
{
	public boolean sealed;

	public boolean lastDisabled = false;

    public boolean active;
	private ItemStack[] containingItems = new ItemStack[1];

	public static int timeSinceOxygenRequest;

	public static final double OXYGEN_PER_TICK = 500;
	
	public static final int MAX_OXYGEN = 18000;
	
	public int storedOxygen;
	
	public GCCoreTileEntityOxygenSealer()
	{
		super(300, 130);
	}
	
    public double getPower()
    {
    	return this.storedOxygen / 600.0D;
    }

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (GCCoreTileEntityOxygenSealer.timeSinceOxygenRequest > 0)
			{
				GCCoreTileEntityOxygenSealer.timeSinceOxygenRequest--;
			}

			this.wattsReceived = Math.max(this.wattsReceived - this.ueWattsPerTick / 4, 0);
			this.storedOxygen = (int) Math.max(this.storedOxygen - this.storedOxygen / 40, 0);

			if (this.getPower() >= 1 && (this.wattsReceived > 0 || this.ic2Energy > 0) && !this.disabled)
			{
				this.active = true;
			}
			else
			{
				this.active = false;
			}

			if (this.ticks % 50 == 0 || this.lastDisabled != this.disabled)
			{
				if (this.active)
				{
		            this.clean(this.worldObj, this.xCoord, this.yCoord, this.zCoord, (int) (this.storedOxygen / 10.0D));
		            this.sealed = this.isOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				}
				else
				{
		            this.spread(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				}
			}

			if (this.ticks % 3 == 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 6);
			}

			this.lastDisabled = this.disabled;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.storedOxygen = par1NBTTagCompound.getInteger("storedOxygen");

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
		par1NBTTagCompound.setInteger("storedOxygen", this.storedOxygen);

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
	public int transferGasToAcceptor(int amount, EnumGas type)
	{
		GCCoreTileEntityOxygenSealer.timeSinceOxygenRequest = 20;

		if (this.wattsReceived > 0 && type == EnumGas.OXYGEN)
		{
			int rejectedOxygen = 0;
			int requiredOxygen = MAX_OXYGEN - storedOxygen;
			
			if (amount <= requiredOxygen)
			{
				this.storedOxygen += amount;
			}
			else
			{
				this.storedOxygen += requiredOxygen;
				rejectedOxygen = amount - requiredOxygen;
			}
			
			return rejectedOxygen;
		}
		
		return 0;
	}

	@Override
	public boolean canReceiveGas(ForgeDirection side, EnumGas type)
	{
		return side == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}

	@Override
	public boolean canTubeConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
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

    public boolean isOn(World var1, int var2, int var3, int var4)
    {
    	final OxygenPressureProtocol var5 = new OxygenPressureProtocol();
        final boolean on = var5.checkSeal(var1, var2, var3, var4, 3);
        return on;
    }

    public void clean(World var1, int var2, int var3, int var4, int var5)
    {
    	final OxygenPressureProtocol var6 = new OxygenPressureProtocol();
        var6.seal(var1, var2, var3, var4, var5);
    }

    private void spread(World var1, int var2, int var3, int var4)
    {
    	final OxygenPressureProtocol var5 = new OxygenPressureProtocol();
        var5.unSeal(var1, var2, var3, var4);
    }

	@Override
	public boolean shouldPullEnergy() 
	{
		return this.timeSinceOxygenRequest > 0 && !this.disabled;
	}

	@Override
	public void readPacket(ByteArrayDataInput data)
	{
		if (this.worldObj.isRemote)
		{
			this.storedOxygen = data.readInt();
			this.wattsReceived = data.readDouble();
			this.disabled = data.readBoolean();
			this.ic2Energy = data.readDouble();
			this.sealed = data.readBoolean();
		}
	}

	@Override
	public Packet getPacket()
	{
		return PacketManager.getPacket(BasicComponents.CHANNEL, this, this.storedOxygen, this.wattsReceived, this.disabled, this.ic2Energy, this.sealed);
	}

	@Override
	public ForgeDirection getInputDirection() 
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ItemStack getBatteryInSlot() 
	{
		return this.getStackInSlot(0);
	}
}