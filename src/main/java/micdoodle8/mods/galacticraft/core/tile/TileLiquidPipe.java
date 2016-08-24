package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileLiquidPipe extends TileEntityAdvanced implements ITransmitter
{
    @Override
    public IGridNetwork getNetwork()
    {
        return null;
    }

    @Override
    public NetworkType getNetworkType()
    {
        return null;
    }

    @Override
    public void setNetwork(IGridNetwork network)
    {

    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return false;
    }

    @Override
    public TileEntity[] getAdjacentConnections()
    {
        return new TileEntity[0];
    }

    @Override
    public void refresh()
    {

    }

    @Override
    public void onNetworkChanged()
    {

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
    public boolean hasNetwork()
    {
        return false;
    }
}
