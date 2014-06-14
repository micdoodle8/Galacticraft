package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;

public abstract class EnergyStorageTile extends TileEntityAdvanced implements IEnergyHandlerGC, IElectrical
{
	@NetworkedField(targetSide = Side.CLIENT)
	public EnergyStorage storage = new EnergyStorage(50, 1000000);

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);
		storage.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);
		storage.writeToNBT(nbt);
	}
	
	public abstract ReceiverMode getModeFromDirection(ForgeDirection direction);

	@Override
	public int receiveEnergyGC(EnergySource from, int amount, boolean simulate) 
	{
		return storage.receiveEnergyGC(amount, simulate);
	}

	@Override
	public int extractEnergyGC(EnergySource from, int amount, boolean simulate) 
	{
		return storage.extractEnergyGC(amount, simulate);
	}

	@Override
	public boolean nodeAvailable(EnergySource from) 
	{
		if (!(from instanceof EnergySourceAdjacent))
		{
			return false;
		}
		
		return this.getModeFromDirection(((EnergySourceAdjacent) from).direction) != ReceiverMode.UNDEFINED;
	}

	@Override
	public int getEnergyStoredGC(EnergySource from) 
	{
		return storage.getEnergyStoredGC();
	}
	
	public int getEnergyStoredGC()
	{
		return this.getEnergyStoredGC(null);
	}

	@Override
	public int getMaxEnergyStoredGC(EnergySource from)
	{
		return storage.getCapacityGC();
	}
	
	public int getMaxEnergyStoredGC()
	{
		return this.getMaxEnergyStoredGC(null);
	}

	@Override
	public boolean canConnect(ForgeDirection direction, NetworkType type) {
		return false;
	}

	//Five methods for compatibility with basic electricity
	@Override
	public int receiveElectricity(ForgeDirection from, int receive, boolean doReceive)
	{
		return storage.receiveEnergyGC(receive, !doReceive);
	}

	@Override
	public int provideElectricity(ForgeDirection from, int request, boolean doProvide)
	{
		return storage.extractEnergyGC(request, !doProvide);
	}

	@Override
	public int getRequest(ForgeDirection direction)
	{
		return getMaxEnergyStoredGC() - getEnergyStoredGC();
	}

	@Override
	public int getProvide(ForgeDirection direction)
	{
		return 0;
	}
}