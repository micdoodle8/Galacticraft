package micdoodle8.mods.galacticraft.core.tile;

import buildcraft.api.power.PowerProvider;

public class GCCoreLinkedPowerProvider extends PowerProvider
{
    public GCCoreTileEntityElectric electricTile;

    public GCCoreLinkedPowerProvider(GCCoreTileEntityElectric tile)
    {
        this.electricTile = tile;
    }
}
