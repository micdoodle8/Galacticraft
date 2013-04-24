package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.ITubeConnection;

public abstract class GCCoreTileEntityOxygen extends GCCoreTileEntityElectric implements IGasAcceptor, ITubeConnection
{
	public int maxOxygen;
	public int oxygenPerTick;
	public int storedOxygen;
	public static int timeSinceOxygenRequest;
	
	public GCCoreTileEntityOxygen(int ueWattsPerTick, double ic2MaxEnergy, double bcEnergyPerTick, double ic2EnergyPerTick, int maxOxygen, int oxygenPerTick) 
	{
		super(ueWattsPerTick, ic2MaxEnergy, ic2EnergyPerTick, bcEnergyPerTick);
		this.maxOxygen = maxOxygen;
		this.oxygenPerTick = oxygenPerTick;
	}
	
	public abstract ForgeDirection getOxygenInputDirection();
	
	public abstract boolean shouldPullOxygen();
	
	public int getCappedScaledOxygenLevel(int scale)
	{
		return (int) Math.max(Math.min((Math.floor((double)this.storedOxygen / (double)this.maxOxygen * scale)), scale), 0);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (GCCoreTileEntityOxygenCompressor.timeSinceOxygenRequest > 0)
			{
				GCCoreTileEntityOxygenCompressor.timeSinceOxygenRequest--;
			}
			
			this.storedOxygen = (int) Math.max(this.storedOxygen - this.oxygenPerTick, 0);
			
			if (GCCoreTileEntityOxygenCompressor.timeSinceOxygenRequest > 0)
			{
				GCCoreTileEntityOxygenCompressor.timeSinceOxygenRequest--;
			}
		}
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
		this.timeSinceOxygenRequest = 20;

		if (this.shouldPullOxygen() && type == EnumGas.OXYGEN)
		{
			int rejectedOxygen = 0;
			int requiredOxygen = this.maxOxygen - this.storedOxygen;
			
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
