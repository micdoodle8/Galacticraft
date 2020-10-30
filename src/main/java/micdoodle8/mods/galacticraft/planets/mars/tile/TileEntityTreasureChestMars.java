package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityTreasureChestMars extends TileEntityTreasureChest
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.treasureChestTier2)
    public static TileEntityType<TileEntityTreasureChestMars> TYPE;

    public TileEntityTreasureChestMars()
    {
        super(TYPE, 2);
    }
}