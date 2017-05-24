package micdoodle8.mods.galacticraft.api.transmission.tile;

public interface IBufferTransmitter<N> extends ITransmitter
{
    N getBuffer();

    int getCapacity();
}
