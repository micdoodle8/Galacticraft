package micdoodle8.mods.galacticraft.api.transmission.tile;

import micdoodle8.mods.galacticraft.api.transmission.grid.IElectricityNetwork;

public interface IConductor extends ITransmitter
{
    /**
     * @return The tier of this conductor - must be 1 or 2
     */
    public int getTierGC();

    /**
     * @return This conductor's electricity network.
     */
    @Override
    public IElectricityNetwork getNetwork();
}
