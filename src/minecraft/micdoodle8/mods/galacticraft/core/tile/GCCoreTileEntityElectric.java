package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import micdoodle8.mods.galacticraft.API.IDisableableMachine;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

public abstract class GCCoreTileEntityElectric extends TileEntityElectricityRunnable implements IEnergySink, IPacketReceiver, IDisableableMachine
{
	public int ueWattsPerTick;
	public double ic2MaxEnergy;
	public double ic2Energy;
	public double ic2EnergyPerTick;

	public boolean addedToEnergyNet = false;

	public boolean disabled = true;
	public int disableCooldown = 0;
	
	public abstract boolean shouldPullEnergy();
	
	public abstract void readPacket(ByteArrayDataInput data);
	
	public abstract Packet getPacket();
	
	public abstract ForgeDirection getInputDirection();
	
	public abstract ItemStack getBatteryInSlot();
	
	public GCCoreTileEntityElectric(int ueWattsPerTick, double ic2MaxEnergy, double ic2EnergyPerTick)
	{
		this.ueWattsPerTick = ueWattsPerTick;
		this.ic2MaxEnergy = ic2MaxEnergy;
		this.ic2EnergyPerTick = ic2EnergyPerTick;
	}

	@Override
    public void invalidate()
    {
    	this.unloadIC2();
		super.invalidate();
    }
	
	@Override
    public void onChunkUnload()
    {
    	this.unloadIC2();
    	super.onChunkUnload();
    }
	
	private void unloadIC2()
	{
    	if (this.addedToEnergyNet && this.worldObj != null)
    	{
			if(GalacticraftCore.modIC2Loaded)
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			}
			
			this.addedToEnergyNet = false;
    	}
	}

	@Override
	public ElectricityPack getRequest()
	{
		if (this.shouldPullEnergy())
		{
			return new ElectricityPack(this.ueWattsPerTick / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!this.addedToEnergyNet && this.worldObj != null)
		{
			if(GalacticraftCore.modIC2Loaded)
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			}

			this.addedToEnergyNet = true;
		}
		
		if (!this.worldObj.isRemote)
		{
			this.ic2Energy = Math.max(this.ic2Energy - ic2EnergyPerTick, 0);
			
			if (this.shouldPullEnergy())
			{
				this.wattsReceived += ElectricItemHelper.dechargeItem(this.getBatteryInSlot(), this.ueWattsPerTick, this.getVoltage());
			}
			
			if (this.disableCooldown > 0)
			{
				this.disableCooldown--;
			}
			
			if (this.ticks % 3 == 0)
			{
				PacketManager.sendPacketToClients(this.getPacket(), this.worldObj, new Vector3(this), 12);
			}
			
			this.wattsReceived = Math.max(this.wattsReceived - this.ueWattsPerTick / 4, 0);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setDouble("ic2Energy", this.ic2Energy);
		nbt.setBoolean("isDisabled", this.getDisabled());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		this.ic2Energy = nbt.getDouble("ic2Energy");
		this.setDisabled(nbt.getBoolean("isDisabled"));
	}
	
	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.readPacket(dataStream);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount)
	{
	    this.ic2Energy += amount;
	    
	    int rejects = 0;
	    
	    if (this.ic2Energy > this.ic2MaxEnergy)
	    {
	    	rejects = (int) (this.ic2Energy - this.ic2MaxEnergy);
	    	this.ic2Energy = this.ic2MaxEnergy;
	    }
	    
	    return rejects;
	}

	@Override
	public int demandsEnergy()
	{
		return (int) (shouldPullEnergy() ? this.ic2MaxEnergy - this.ic2Energy : 0);
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) 
	{
		return direction.toForgeDirection() == this.getInputDirection();
	}

	@Override
	public boolean isAddedToEnergyNet() 
	{
		return this.addedToEnergyNet;
	}

	@Override
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == this.getInputDirection();
	}

	@Override
	public void setDisabled(boolean disabled)
	{
		if (this.disableCooldown == 0)
		{
			this.disabled = disabled;
			this.disableCooldown = 20;
		}
	}

	@Override
	public boolean getDisabled()
	{
		return this.disabled;
	}
}
