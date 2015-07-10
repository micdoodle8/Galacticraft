package micdoodle8.mods.galacticraft.planets.mars.tile;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.IHydrogenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityHydrogenPipe extends TileEntity implements ITransmitter
{
    private IGridNetwork network;

    public TileEntity[] adjacentConnections = null;

//    @Override
//    public boolean canUpdate()
//    {
//        return this.worldObj == null || !this.worldObj.isRemote;
//    }

    @Override
    public void validate()
    {
        super.validate();
    	if (!this.worldObj.isRemote)
    	{
    		TickHandlerServer.hydrogenTransmitterUpdates.add(this);
    	}

        if (this.worldObj != null && this.worldObj.isRemote)
        {
            this.worldObj.notifyLightSet(this.getPos());
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

    @Override
    public IGridNetwork getNetwork()
    {
        if (this.network == null)
        {
            this.resetNetwork();
        }

        return this.network;
    }

    @Override
    public void onNetworkChanged()
    {

    }

    protected void resetNetwork()
    {
        HydrogenNetwork network = new HydrogenNetwork();
        network.getTransmitters().add(this);
        this.setNetwork(network);
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

            for (EnumFacing side : EnumFacing.values())
            {
                TileEntity tileEntity = new BlockVec3(this).getTileEntityOnSide(this.worldObj, side);

                if (tileEntity != null)
                {
                    if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider && !this.getNetwork().equals(((INetworkProvider) tileEntity).getNetwork()))
                    {
                        this.setNetwork((IGridNetwork) this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork()));
                    }
                }
            }

            this.getNetwork().refresh();
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
            this.adjacentConnections = this.getAdjacentHydrogenConnections(this);
        }

        return this.adjacentConnections;
    }

    public static TileEntity[] getAdjacentHydrogenConnections(TileEntity tile)
    {
        TileEntity[] adjacentConnections = new TileEntity[EnumFacing.values().length];

        boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();

        BlockVec3 thisVec = new BlockVec3(tile);
        for (EnumFacing direction : EnumFacing.values())
        {
            TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorld(), direction);

            if (tileEntity instanceof IConnector)
            {
                if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.HYDROGEN))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
            }
//            else if (isMekLoaded)
//            {
//                if (tileEntity instanceof ITubeConnection && (!(tileEntity instanceof IGasTransmitter) || TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, tileEntity)))
//                {
//                    if (((ITubeConnection) tileEntity).canTubeConnect(direction))
//                    {
//                        adjacentConnections[direction.ordinal()] = tileEntity;
//                    }
//                }
//            }
        }

        return adjacentConnections;
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return type == NetworkType.HYDROGEN;
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
        return NetworkType.HYDROGEN;
    }

//    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
//    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
//    {
//    	if (!stack.getGas().getName().equals("hydrogen")) return 0;
//        return stack.amount - (int) Math.floor(((IHydrogenNetwork) this.getNetwork()).produce(stack.amount, this));
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
//    public int receiveGas(EnumFacing side, GasStack stack)
//    {
//    	return this.receiveGas(side, stack, true);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
//    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer)
//    {
//        return null;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
//    public GasStack drawGas(EnumFacing side, int amount)
//    {
//        return null;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
//    public boolean canDrawGas(EnumFacing side, Gas type)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
//    public boolean canReceiveGas(EnumFacing side, Gas type)
//    {
//        return type.getName().equals("hydrogen");
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
//    public boolean canTubeConnect(EnumFacing side)
//    {
//        return this.canConnect(side, NetworkType.HYDROGEN);
//    }
}
