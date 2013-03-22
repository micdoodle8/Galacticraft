package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
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
public class GCCoreTileEntityOxygenCompressor extends TileEntityElectricityRunnable implements IInventory, IPacketReceiver, IGasAcceptor, ITubeConnection
{
    public int currentPower;
    
    public boolean active;

	private ItemStack[] containingItems = new ItemStack[2];
   	
	public static final double WATTS_PER_TICK = 300;

	private final int playersUsing = 0;
	
	public static int timeSinceOxygenRequest;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.wattsReceived += ElectricItemHelper.dechargeItem(this.containingItems[1], GCCoreTileEntityOxygenCompressor.WATTS_PER_TICK, this.getVoltage());
		
		if (!this.worldObj.isRemote)
		{
			if (this.wattsReceived >= 0.05)
			{
				this.wattsReceived -= 0.05;
			}
			
			if (this.timeSinceOxygenRequest > 0)
			{
				GCCoreTileEntityOxygenCompressor.timeSinceOxygenRequest--;
			}
			
			this.wattsReceived = Math.max(this.wattsReceived - GCCoreTileEntityOxygenCompressor.WATTS_PER_TICK / 4, 0);
			
			if (this.currentPower < 1 && this.wattsReceived > 0)
			{
				this.active = false;
			}
			else
			{
				this.active = true;
			}

			if (this.active)
			{
				if (!this.worldObj.isRemote && GalacticraftCore.tick % ((31 - Math.min(Math.floor(this.currentPower), 30)) * 5) == 0)
				{
					final ItemStack stack = this.getStackInSlot(0);

					if (stack != null && stack.getItemDamage() > 0)
					{
						stack.setItemDamage(stack.getItemDamage() - 1);
					}
					else if (stack != null)
					{
						stack.setItemDamage(0);
					}
				}
			}

			if (this.ticks % 3 == 0)
			{
				Packet packet = this.getDescriptionPacket();
				
				if (packet != null)
				{
					PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 6);
				}
			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		Packet p = PacketManager.getPacket(BasicComponents.CHANNEL, this, this.currentPower, this.wattsReceived, this.disabledTicks);
		return p;
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.currentPower = dataStream.readInt();
				this.wattsReceived = dataStream.readDouble();
				this.disabledTicks = dataStream.readInt();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public ElectricityPack getRequest()
	{
		if (this.getStackInSlot(0) != null)
		{
			return new ElectricityPack(GCCoreTileEntityOxygenCompressor.WATTS_PER_TICK / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
		}
	}

	@Override
	public boolean canReceiveGas(ForgeDirection side, EnumGas type) 
	{
		return side == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite() && type == EnumGas.OXYGEN;
	}

	@Override
	public boolean canTubeConnect(ForgeDirection direction) 
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.currentPower = par1NBTTagCompound.getInteger("currentPower");

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
		par1NBTTagCompound.setInteger("currentPower", this.currentPower);

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

	@Override
	public int transferGasToAcceptor(int amount, EnumGas type)
	{
		this.timeSinceOxygenRequest = 20;
		
		if (type == EnumGas.OXYGEN && this.getStackInSlot(0) != null)
		{
			int rejects = 0;
			int neededOxygen = this.getStackInSlot(0).getItemDamage();
			
			if (amount <= neededOxygen)
			{
				this.currentPower = amount;
			}
			else if (amount > neededOxygen)
			{
				this.currentPower = neededOxygen;
				rejects = amount - neededOxygen;
			}
			
			return rejects;
		}
		else
		{
			return amount;
		}
	}

	@Override
	public boolean func_94042_c() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean func_94041_b(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}
}