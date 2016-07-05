package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenNetwork;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityOxygenTransmitter extends TileEntityAdvanced implements ITransmitter
{
    private IGridNetwork network;

    public TileEntity[] adjacentConnections = null;

    @Override
    public void validate()
    {
    	super.validate();
    	if (!this.worldObj.isRemote)
    	{
    		TickHandlerServer.oxygenTransmitterUpdates.add(this);
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
    public void onChunkUnload()
    {
        super.invalidate();
    	super.onChunkUnload();
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

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
        this.worldObj.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
    }

    protected void resetNetwork()
    {
        OxygenNetwork network = new OxygenNetwork();
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
            this.adjacentConnections = OxygenUtil.getAdjacentOxygenConnections(this);
            // this.adjacentConnections = new TileEntity[6];
            //
            // for (EnumFacing side : EnumFacing.VALID_DIRECTIONS)
            // {
            // Vector3 thisVec = new Vector3(this);
            // TileEntity tileEntity =
            // thisVec.modifyPositionFromSide(side).getTileEntity(worldObj);
            //
            // if (tileEntity instanceof IConnector)
            // {
            // if (((IConnector) tileEntity).canConnect(side.getOpposite(),
            // NetworkType.OXYGEN))
            // {
            // this.adjacentConnections[side.ordinal()] = tileEntity;
            // }
            // }
            // }
        }

        return this.adjacentConnections;
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return type == NetworkType.OXYGEN;
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
        return NetworkType.OXYGEN;
    }

    @Override
    public double getPacketRange() {
        return 0;
    }

    @Override
    public int getPacketCooldown() {
        return 0;
    }

    @Override
    public boolean isNetworkedTile() {
        return false;
    }

    //    @RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
//    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
//    {
//    	if (!stack.getGas().getName().equals("oxygen")) return 0;
//        return stack.amount - (int) Math.floor(((IOxygenNetwork) this.getNetwork()).produce(stack.amount, this));
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
//        return type.getName().equals("oxygen");
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
//    public boolean canTubeConnect(EnumFacing side)
//    {
//        return this.canConnect(side, NetworkType.OXYGEN);
//    }
}
