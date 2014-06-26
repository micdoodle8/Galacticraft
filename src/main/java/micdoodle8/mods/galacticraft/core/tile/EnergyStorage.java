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

		if (this.energy > this.capacity)
		{
			this.energy = this.capacity;
		}

		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (this.energy < 0)
		{
			this.energy = 0;
		}

		nbt.setFloat("EnergyF", this.energy);
		return nbt;
	}

	public void setCapacity(float capacity)
	{
		this.capacity = capacity;

		if (this.energy > capacity)
		{
			this.energy = capacity;
		}
	}

	public void setMaxTransfer(float maxTransfer)
	{
		this.setMaxReceive(maxTransfer);
		this.setMaxExtract(maxTransfer);
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
		this.energy = Math.max(0, Math.min(energy, this.capacity));
	}

	public float getMaxReceive()
	{
		return this.maxReceive;
	}

	public float getMaxExtract()
	{
		return this.maxExtract;
	}

	@Override
	public float receiveEnergyGC(float maxReceive, boolean simulate)
	{
		float energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate)
		{
			this.energy += energyReceived;
		}

		return energyReceived;
	}

	@Override
	public float extractEnergyGC(float maxExtract, boolean simulate)
	{
		float energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate)
		{
			this.energy -= energyExtracted;
		}

		return energyExtracted;
	}

	@Override
	public float getEnergyStoredGC()
	{
		return this.energy;
	}

	@Override
	public float getCapacityGC()
	{
		return this.capacity;
	}
}
