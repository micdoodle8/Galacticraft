package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.power.core.grid.IGridNetwork;

public interface INetworkProvider
{
	public IGridNetwork getNetwork();

	public void setNetwork(IGridNetwork network);
}
