package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasAcceptor;
import mekanism.api.gas.IGasStorage;
import mekanism.api.gas.ITubeConnection;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public abstract class GCCoreTileEntityOxygen extends GCCoreTileEntityElectric implements IGasAcceptor, ITubeConnection, IGasStorage
{
    public int maxOxygen;
    public int oxygenPerTick;
    public int storedOxygen;
    public int lastStoredOxygen;
    public static int timeSinceOxygenRequest;

    public GCCoreTileEntityOxygen(float wattsPerTick, float maxEnergy, int maxOxygen, int oxygenPerTick)
    {
        super(wattsPerTick, maxEnergy);
        this.maxOxygen = maxOxygen;
        this.oxygenPerTick = oxygenPerTick;
    }

    public abstract ForgeDirection getOxygenInputDirection();

    public abstract boolean shouldPullOxygen();

    public abstract boolean shouldUseOxygen();

    public int getCappedScaledOxygenLevel(int scale)
    {
        return (int) Math.max(Math.min(Math.floor((double) this.storedOxygen / (double) this.maxOxygen * scale), scale), 0);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0)
            {
                GCCoreTileEntityOxygen.timeSinceOxygenRequest--;
            }

            if (this.shouldUseOxygen())
            {
                this.storedOxygen = Math.max(this.storedOxygen - this.oxygenPerTick, 0);
            }
        }

        this.lastStoredOxygen = this.storedOxygen;
    }

    @Override
    public boolean canReceiveGas(ForgeDirection side, Gas type)
    {
        return this.getOxygenInputDirection() != null && side == this.getOxygenInputDirection() && type.getName().equals("oxygen");
    }

    @Override
    public boolean canTubeConnect(ForgeDirection direction)
    {
        return this.getOxygenInputDirection() != null && direction == this.getOxygenInputDirection();
    }

    @Override
    public int receiveGas(GasStack stack)
    {
        GCCoreTileEntityOxygen.timeSinceOxygenRequest = 20;

        if (this.shouldPullOxygen() && stack.getGas().equals(GalacticraftCore.gasOxygen))
        {
            int rejectedOxygen = 0;
            final int requiredOxygen = this.maxOxygen - this.storedOxygen;

            if (stack.amount <= requiredOxygen)
            {
                this.storedOxygen += stack.amount;
            }
            else
            {
                this.storedOxygen += requiredOxygen;
                rejectedOxygen = stack.amount - requiredOxygen;
            }

            return rejectedOxygen;
        }

        return stack.amount;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.storedOxygen = nbt.getInteger("storedOxygen");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("storedOxygen", this.storedOxygen);
    }

    @Override
    public GasStack getGas(Object... data)
    {
        return new GasStack(GalacticraftCore.gasOxygen, this.storedOxygen);
    }

    @Override
    public void setGas(GasStack stack, Object... data)
    {
        if (stack == null)
        {
            this.storedOxygen = 0;
            return;
        }
        
        this.storedOxygen = stack.amount;
    }

    @Override
    public int getMaxGas(Object... data)
    {
        return this.maxOxygen;
    }
}
