package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileEntityCargoBase extends TileBaseElectricBlockWithInventory
{
    public TileEntityCargoBase(TileEntityType<?> type)
    {
        super(type);
    }
}
