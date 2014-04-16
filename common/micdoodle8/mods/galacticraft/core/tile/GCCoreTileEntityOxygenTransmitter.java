package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.IOxygenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.OxygenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class GCCoreTileEntityOxygenTransmitter extends GCCoreTileEntityAdvanced implements ITransmitter
{
	private IGridNetwork network;

	public TileEntity[] adjacentConnections = null;

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
	public boolean canUpdate()
	{
		return false;
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

			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
			{
				TileEntity tileEntity = new Vector3(this).modifyPositionFromSide(side).getTileEntity(this.worldObj);

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
			this.adjacentConnections = WorldUtil.getAdjacentOxygenConnections(this);
			// this.adjacentConnections = new TileEntity[6];
			//
			// for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
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
	public boolean canConnect(ForgeDirection direction, NetworkType type)
	{
		return type == NetworkType.OXYGEN;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
	}

	@Override
	public NetworkType getNetworkType()
	{
		return NetworkType.OXYGEN;
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public int receiveGas(ForgeDirection side, GasStack stack)
	{
		return stack.amount - (int) Math.floor(((IOxygenNetwork) this.getNetwork()).produce(stack.amount, this));
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

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasAcceptor", modID = "Mekanism")
	public int receiveGas(GasStack stack)
	{
		return (int) Math.floor(((IOxygenNetwork) this.getNetwork()).produce(stack.amount, this));
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasAcceptor", altClasses = { "mekanism.api.gas.IGasHandler" }, modID = "Mekanism")
	public boolean canReceiveGas(ForgeDirection side, Gas type)
	{
		return type.getName().equals("oxygen");
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
	public boolean canTubeConnect(ForgeDirection side)
	{
		return this.canConnect(side, NetworkType.OXYGEN);
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasStorage", modID = "Mekanism")
	public GasStack getGas(Object... data)
	{
		return new GasStack((Gas) NetworkConfigHandler.gasOxygen, 0);
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasStorage", modID = "Mekanism")
	public void setGas(GasStack stack, Object... data)
	{
		;
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasStorage", modID = "Mekanism")
	public int getMaxGas(Object... data)
	{
		return 1;
	}
}
