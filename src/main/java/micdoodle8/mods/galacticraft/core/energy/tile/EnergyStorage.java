package micdoodle8.mods.galacticraft.core.energy.tile;

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
        this(capacity, 60, 30);
    }

    public EnergyStorage(float capacity, float maxTransfer)
    {
        this(capacity, 2.5F * maxTransfer, maxTransfer);
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
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (this.energy < 0)
        {
            this.energy = 0;
        }

        nbt.setFloat("EnergyF", Math.min(this.energy, this.capacity));
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

    /*
     * Sets the maximum energy transfer rate on input
     * Call this AFTER setMaxExtract().
     */
    public void setMaxReceive(float maxReceive)
    {
        this.maxReceive = maxReceive;
    }

    /*
     * Sets the energy consumption rate in gJ/t
     * (For machines, this is the energy used per tick.)
     * (For energy sources, this is the maximum output.)
     *
     * Also sets the receive rate at a default value
     * of 2 * the energy consumption rate - so the machine's
     * energy store can charge up even while it is working.
     *
     * If that is not required, call setMaxReceive() AFTER
     * calling setMaxExtract().
     */
    public void setMaxExtract(float maxExtract)
    {
        this.maxExtract = maxExtract;
        this.maxReceive = 2.5F * maxExtract;
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

    public float receiveEnergyGC(float maxReceive)
    {
        float energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
        this.energy += energyReceived;
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
