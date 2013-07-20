package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.ITubeConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;

public abstract class GCCoreTileEntityOxygen extends GCCoreTileEntityElectric implements IGasAcceptor, ITubeConnection
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

            this.oxygenPerTick = (int) (this.storedOxygen / 250.0F);

            if (this.storedOxygen == this.lastStoredOxygen && this.storedOxygen < 250)
            {
                this.oxygenPerTick = 1;
            }

            this.storedOxygen = Math.max(this.storedOxygen - this.oxygenPerTick, 0);
        }

        this.lastStoredOxygen = this.storedOxygen;
    }

    @Override
    public boolean canReceiveGas(ForgeDirection side, EnumGas type)
    {
        return side == this.getOxygenInputDirection() && type == EnumGas.OXYGEN;
    }

    @Override
    public boolean canTubeConnect(ForgeDirection direction)
    {
        return direction == this.getOxygenInputDirection();
    }

    @Override
    public int transferGasToAcceptor(int amount, EnumGas type)
    {
        GCCoreTileEntityOxygen.timeSinceOxygenRequest = 20;

        if (this.shouldPullOxygen() && type == EnumGas.OXYGEN)
        {
            int rejectedOxygen = 0;
            final int requiredOxygen = this.maxOxygen - this.storedOxygen;

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

        return amount;
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
}
