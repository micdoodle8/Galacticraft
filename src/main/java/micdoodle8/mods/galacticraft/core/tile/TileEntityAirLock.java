package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.util.EnumFacing;

public class TileEntityAirLock extends TileEntityAdvanced
{
    public TileEntityAirLock()
    {
        super("tile.air_lock_seal.name");
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }
}
