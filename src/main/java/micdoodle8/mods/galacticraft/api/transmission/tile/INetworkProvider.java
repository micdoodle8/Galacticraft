package micdoodle8.mods.galacticraft.api.transmission.tile;

import micdoodle8.mods.galacticraft.api.transmission.core.grid.IGridNetwork;

@SuppressWarnings("rawtypes")
public interface INetworkProvider
{
	public IGridNetwork getNetwork();

	public void setNetwork(IGridNetwork network);
}
