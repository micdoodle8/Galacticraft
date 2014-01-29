package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.ASMHelper.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.power.NetworkType;
import micdoodle8.mods.galacticraft.power.core.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.power.core.grid.IOxygenNetwork;
import micdoodle8.mods.galacticraft.power.core.grid.OxygenNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
			OxygenNetwork network = new OxygenNetwork();
			network.getTransmitters().add(this);
			this.setNetwork(network);
		}

		return this.network;
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
					if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider)
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
//			this.adjacentConnections = new TileEntity[6];
//
//			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
//			{
//				Vector3 thisVec = new Vector3(this);
//				TileEntity tileEntity = thisVec.modifyPositionFromSide(side).getTileEntity(worldObj);
//
//				if (tileEntity instanceof IConnector)
//				{
//					if (((IConnector) tileEntity).canConnect(side.getOpposite(), NetworkType.OXYGEN))
//					{
//						this.adjacentConnections[side.ordinal()] = tileEntity;
//					}
//				}
//			}
		}

		return this.adjacentConnections;
	}

	@Override
	public boolean canConnect(ForgeDirection direction, NetworkType type)
	{
		return type == NetworkType.POWER;
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

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasAcceptor", modID = "Mekanism")
	public int receiveGas(GasStack stack)
	{
		return (int) Math.floor(((IOxygenNetwork) this.getNetwork()).produce(stack.amount, this));
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasAcceptor", modID = "Mekanism")
	public boolean canReceiveGas(ForgeDirection side, Gas type)
	{
		return type.getName().equals("oxygen");
	}
}
