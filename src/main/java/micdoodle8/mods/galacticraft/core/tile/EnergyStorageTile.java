package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBeamReceiver.ReceiverMode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class EnergyStorageTile extends TileEntityAdvanced
{
	protected EnergyStorage storage = new EnergyStorage(32000);

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

	public int receiveEnergyAdjacent(ILaserNode from, int amount, boolean simulate)
	{
		return storage.receiveEnergyGC(amount, simulate);
	}

	public int extractEnergyAdjacent(ILaserNode from, int amount, boolean simulate)
	{
		return storage.extractEnergyGC(amount, simulate);
	}

	public boolean canConnectToReceiver() 
	{
		return true;
	}
	
	public abstract ReceiverMode getModeFromDirection(ForgeDirection direction);
}