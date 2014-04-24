package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.power.IEnergyStorageGC;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage implements IEnergyStorageGC 
{
	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public EnergyStorage(int capacity) 
	{
		this(capacity, capacity, capacity);
	}

	public EnergyStorage(int capacity, int maxTransfer) 
	{
		this(capacity, maxTransfer, maxTransfer);
	}

	public EnergyStorage(int capacity, int maxReceive, int maxExtract) 
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public EnergyStorage readFromNBT(NBTTagCompound nbt) 
	{
		this.energy = nbt.getInteger("Energy");

		if (energy > capacity) 
		{
			energy = capacity;
		}
		
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
	{
		if (energy < 0) 
		{
			energy = 0;
		}
		
		nbt.setInteger("Energy", energy);
		return nbt;
	}

	public void setCapacity(int capacity) 
	{
		this.capacity = capacity;

		if (energy > capacity)
		{
			energy = capacity;
		}
	}

	public void setMaxTransfer(int maxTransfer) 
	{
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive) 
	{
		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) 
	{
		this.maxExtract = maxExtract;
	}
	
	public void setEnergyStored(int energy)
	{
		this.energy = Math.max(0, Math.min(energy, capacity));
	}

	public int getMaxReceive() 
	{
		return maxReceive;
	}

	public int getMaxExtract() 
	{
		return maxExtract;
	}

	@Override
	public int receiveEnergyGC(int maxReceive, boolean simulate) 
	{
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) 
		{
			energy += energyReceived;
		}
		
		return energyReceived;
	}

	@Override
	public int extractEnergyGC(int maxExtract, boolean simulate) 
	{
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) 
		{
			energy -= energyExtracted;
		}
		
		return energyExtracted;
	}

	@Override
	public int getEnergyStoredGC() 
	{
		return energy;
	}

	@Override
	public int getCapacityGC() 
	{
		return capacity;
	}
}
