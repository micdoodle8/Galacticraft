package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.power.IEnergyStorageGC;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage implements IEnergyStorageGC 
{
	protected float energy;
	protected float capacity;
	protected float maxReceive;
	protected float maxExtract;

	public EnergyStorage(float capacity) 
	{
		this(capacity, capacity, capacity);
	}

	public EnergyStorage(float capacity, float maxTransfer) 
	{
		this(capacity, maxTransfer, maxTransfer);
	}

	public EnergyStorage(float capacity, float maxReceive, float maxExtract) 
	{
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public EnergyStorage readFromNBT(NBTTagCompound nbt) 
	{
		this.energy = nbt.getFloat("EnergyF");

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
		
		nbt.setFloat("EnergyF", energy);
		return nbt;
	}

	public void setCapacity(float capacity) 
	{
		this.capacity = capacity;

		if (energy > capacity)
		{
			energy = capacity;
		}
	}

	public void setMaxTransfer(float maxTransfer) 
	{
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(float maxReceive) 
	{
		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(float maxExtract) 
	{
		this.maxExtract = maxExtract;
	}
	
	public void setEnergyStored(float energy)
	{
		this.energy = Math.max(0, Math.min(energy, capacity));
	}

	public float getMaxReceive() 
	{
		return maxReceive;
	}

	public float getMaxExtract() 
	{
		return maxExtract;
	}

	@Override
	public float receiveEnergyGC(float maxReceive, boolean simulate) 
	{
		float energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) 
		{
			energy += energyReceived;
		}
		
		return energyReceived;
	}

	@Override
	public float extractEnergyGC(float maxExtract, boolean simulate) 
	{
		float energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) 
		{
			energy -= energyExtracted;
		}
		
		return energyExtracted;
	}

	@Override
	public float getEnergyStoredGC() 
	{
		return energy;
	}

	@Override
	public float getCapacityGC() 
	{
		return capacity;
	}
}
