package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.grid.EnergyNetwork;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This tile entity pre-fabricated for all conductors.
 *
 * @author Calclavia
 */
@SuppressWarnings({"rawtypes"})
public abstract class TileBaseConductor extends TileEntity implements IConductor
{
    protected IGridNetwork network;

    public TileEntity[] adjacentConnections = null;

    public TileBaseConductor(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void validate()
    {
        super.validate();
        if (!this.world.isRemote)
        {
            TickHandlerServer.energyTransmitterUpdates.add(this);
        }
    }

    @Override
    public void remove()
    {
        if (!this.world.isRemote)
        {
            this.getNetwork().split(this);
        }

        super.remove();
    }

    @Override
    public void onChunkUnloaded()
    {
        super.remove();
        super.onChunkUnloaded();
    }

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
        if (!this.world.isRemote)
        {
            this.adjacentConnections = null;

            this.getNetwork().refresh();

            BlockVec3 thisVec = new BlockVec3(this);
            for (Direction side : Direction.values())
            {
                TileEntity tileEntity = thisVec.getTileEntityOnSide(this.world, side);

                if (tileEntity instanceof TileBaseConductor && ((TileBaseConductor) tileEntity).canConnect(side.getOpposite(), NetworkType.POWER))
                {
                    IGridNetwork otherNet = ((INetworkProvider) tileEntity).getNetwork();
                    if (!this.getNetwork().equals(otherNet))
                    {
                        if (!otherNet.getTransmitters().isEmpty())
                        {
                            otherNet.merge(this.getNetwork());
                        }
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
                Direction side = Direction.byIndex(i);
                TileEntity tileEntity = thisVec.getTileEntityOnSide(this.world, side);

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
    public boolean hasNetwork()
    {
        return this.network != null;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.POWER;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1);
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.POWER;
    }

    @Override
    public boolean canTransmit()
    {
        return true;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
