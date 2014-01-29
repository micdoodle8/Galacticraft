package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.power.NetworkType;

public interface ITransmitter extends INetworkProvider, INetworkConnection
{
	public NetworkType getNetworkType();
}
