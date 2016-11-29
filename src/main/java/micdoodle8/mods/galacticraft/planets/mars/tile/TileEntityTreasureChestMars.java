package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTreasureChestMars extends TileEntityTreasureChest
{
    public TileEntityTreasureChestMars()
    {
        super(2);
    }

    public TileEntityTreasureChestMars(int tier)
    {
        super(tier);
    }

    public static TileEntityTreasureChestMars findClosest(Entity entity)
    {
        double distance = Double.MAX_VALUE;
        TileEntityTreasureChestMars chest = null;
        for (final TileEntity tile : entity.worldObj.loadedTileEntityList)
        {
            if (tile instanceof TileEntityTreasureChestMars)
            {
                double dist = entity.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5);
                if (dist < distance)
                {
                    distance = dist;
                    chest = (TileEntityTreasureChestMars) tile;
                }
            }
        }

        return chest;
    }
}