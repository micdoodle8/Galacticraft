package micdoodle8.mods.galacticraft.core.tile;

import universalelectricity.compatibility.TileEntityUniversalConductor;

public class GCCoreTileEntityCopperWire extends TileEntityUniversalConductor
{
    /**
     * Changed this if your mod wants to nerf Basic Component's copper wire.
     */
    public static float RESISTANCE = 0.05F;
    public static float MAX_AMPS = 200;

    @Override
    public float getResistance()
    {
        return GCCoreTileEntityCopperWire.RESISTANCE;
    }

    @Override
    public float getCurrentCapacity()
    {
        return GCCoreTileEntityCopperWire.MAX_AMPS;
    }
}
