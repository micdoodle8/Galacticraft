package micdoodle8.mods.galacticraft.api.transmission.tile;

public interface IBufferTransmitter<N> extends ITransmitter
{
    public N getBuffer();

    public int getCapacity();
}
