package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.power.core.grid.IElectricityNetwork;

public interface INetworkProvider
{
	public IElectricityNetwork getNetwork();

	public void setNetwork(IElectricityNetwork network);
}
