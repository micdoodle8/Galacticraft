package micdoodle8.mods.galacticraft.api.transmission.tile;

import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;

@SuppressWarnings("rawtypes")
public interface INetworkProvider
{
    IGridNetwork getNetwork();

    boolean hasNetwork();

    void setNetwork(IGridNetwork network);
}
