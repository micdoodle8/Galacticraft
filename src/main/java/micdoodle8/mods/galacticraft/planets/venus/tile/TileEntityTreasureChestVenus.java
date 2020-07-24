package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityTreasureChestVenus extends TileEntityTreasureChest
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.treasureChestTier3)
    public static TileEntityType<TileEntityTreasureChestVenus> TYPE;

    public TileEntityTreasureChestVenus()
    {
        super(TYPE, 3);
    }
}