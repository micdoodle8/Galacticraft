package micdoodle8.mods.galacticraft.core.energy.tile;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.grid.EnergyNetwork;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * This tile entity pre-fabricated for all conductors.
 *
 * @author Calclavia
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class TileBaseConductor extends TileEntityAdvanced implements IConductor
{
    private IGridNetwork network;

    public TileEntity[] adjacentConnections = null;

    @Override
    public void validate()
    {
    	super.validate();
    	if (!this.worldObj.isRemote)
    	{
    		TickHandlerServer.energyTransmitterUpdates.add(this);
    	}
    }
    
    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
        {
            this.getNetwork().split(this);
        }

        super.invalidate();
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

    @Override
    public IElectricityNetwork getNetwork()
    {
        if (this.network == null)
        {
            EnergyNetwork network = new EnergyNetwork();
            network.getTransmitters().add(this);
            this.setNetwork(network);
        }

        return (IElectricityNetwork) this.network;
    }

    @Override
    public void setNetwork(IGridNetwork network)
    {
        this.network = network;
    }

    @Override
    public void refresh()
    {
        if (!this.worldObj.isRemote)
        {
            this.adjacentConnections = null;

            this.getNetwork().refresh();

            BlockVec3 thisVec = new BlockVec3(this);
            for (EnumFacing side : EnumFacing.values())
            {
                TileEntity tileEntity = thisVec.getTileEntityOnSide(this.worldObj, side);

                if (tileEntity != null)
                {
                    if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider && !this.getNetwork().equals(((INetworkProvider) tileEntity).getNetwork()))
                    {
                    	((INetworkProvider) tileEntity).getNetwork().merge(this.getNetwork());
                    }
                }
            }
        }
    }

    @Override
    public TileEntity[] getAdjacentConnections()
    {
        /**
         * Cache the adjacentConnections.
         */
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = new TileEntity[6];

            BlockVec3 thisVec = new BlockVec3(this);
            for (int i = 0; i < 6; i++)
            {
                EnumFacing side = EnumFacing.getFront(i);
                TileEntity tileEntity = thisVec.getTileEntityOnSide(this.worldObj, side);

                if (tileEntity instanceof IConnector)
                {
                    if (((IConnector) tileEntity).canConnect(side.getOpposite(), NetworkType.POWER))
                    {
                        this.adjacentConnections[i] = tileEntity;
                    }
                }
            }
        }

        return this.adjacentConnections;
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return type == NetworkType.POWER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.fromBounds(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1);
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.POWER;
    }
}
