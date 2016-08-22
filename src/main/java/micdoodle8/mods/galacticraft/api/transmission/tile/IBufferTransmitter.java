package micdoodle8.mods.galacticraft.api.transmission.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;

public interface IBufferTransmitter<N> extends ITransmitter
{
    public N getBuffer();

    public int getCapacity();
}
