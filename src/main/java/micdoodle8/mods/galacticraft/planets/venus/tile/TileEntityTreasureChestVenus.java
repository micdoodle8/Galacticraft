package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTreasureChestVenus extends TileEntityTreasureChest
{
    public TileEntityTreasureChestVenus()
    {
        super(3);
    }

    public TileEntityTreasureChestVenus(int tier)
    {
        super(tier);
    }

    public static TileEntityTreasureChestVenus findClosest(Entity entity)
    {
        double distance = Double.MAX_VALUE;
        TileEntityTreasureChestVenus chest = null;
        for (final TileEntity tile : entity.worldObj.loadedTileEntityList)
        {
            if (tile instanceof TileEntityTreasureChestVenus)
            {
                double dist = entity.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5);
                if (dist < distance)
                {
                    distance = dist;
                    chest = (TileEntityTreasureChestVenus) tile;
                }
            }
        }

        return chest;
    }
}