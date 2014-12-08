package micdoodle8.mods.galacticraft.planets.mars.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasTransmitter;
import mekanism.api.gas.ITubeConnection;
import mekanism.api.transmitters.TransmissionType;
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
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityHydrogenPipe extends TileEntity implements ITransmitter
{
    private IGridNetwork network;

    public TileEntity[] adjacentConnections = null;

    @Override
    public boolean canUpdate()
    {
        return this.worldObj == null || !this.worldObj.isRemote;

    }

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
            this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
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

            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
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
        TileEntity[] adjacentConnections = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];

        boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();

        BlockVec3 thisVec = new BlockVec3(tile);
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorldObj(), direction);

            if (tileEntity instanceof IConnector)
            {
                if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.HYDROGEN))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
            }
            else if (isMekLoaded)
            {
                if (tileEntity instanceof ITubeConnection && (!(tileEntity instanceof IGasTransmitter) || TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, tileEntity)))
                {
                    if (((ITubeConnection) tileEntity).canTubeConnect(direction))
                    {
                        adjacentConnections[direction.ordinal()] = tileEntity;
                    }
                }
            }
        }

        return adjacentConnections;
    }

    @Override
    public boolean canConnect(ForgeDirection direction, NetworkType type)
    {
        return type == NetworkType.HYDROGEN;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.HYDROGEN;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public int receiveGas(ForgeDirection side, GasStack stack)
    {
    	if (!stack.getGas().getName().equals("hydrogen")) return 0;  
        return stack.amount - (int) Math.floor(((IHydrogenNetwork) this.getNetwork()).produce(stack.amount, this));
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public GasStack drawGas(ForgeDirection side, int amount)
    {
        return null;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public boolean canDrawGas(ForgeDirection side, Gas type)
    {
        return false;
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
    public boolean canReceiveGas(ForgeDirection side, Gas type)
    {
        return type.getName().equals("hydrogen");
    }

    @RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
    public boolean canTubeConnect(ForgeDirection side)
    {
        return this.canConnect(side, NetworkType.HYDROGEN);
    }
}
