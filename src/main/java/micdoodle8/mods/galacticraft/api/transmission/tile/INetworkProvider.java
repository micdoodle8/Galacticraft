package micdoodle8.mods.galacticraft.api.transmission.tile;

import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;

@SuppressWarnings("rawtypes")
public interface INetworkProvider
{
    public IGridNetwork getNetwork();

    public boolean hasNetwork();

    public void setNetwork(IGridNetwork network);
}
