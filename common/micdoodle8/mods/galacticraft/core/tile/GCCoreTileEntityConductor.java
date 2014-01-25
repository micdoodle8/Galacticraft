package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.power.NetworkLoader;
import micdoodle8.mods.galacticraft.power.core.grid.IElectricityNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This tile entity pre-fabricated for all conductors.
 * 
 * @author Calclavia
 * 
 */
public abstract class GCCoreTileEntityConductor extends GCCoreTileEntityAdvanced implements IConductor
{
	private IElectricityNetwork network;

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
	public IElectricityNetwork getNetwork()
	{
		if (this.network == null)
		{
			this.setNetwork(NetworkLoader.getNewNetwork(this));
		}

		return this.network;
	}

	@Override
	public void setNetwork(IElectricityNetwork network)
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
				Vector3 thisVec = new Vector3(this);
				TileEntity tileEntity = thisVec.modifyPositionFromSide(side).getTileEntity(this.worldObj);

				if (tileEntity != null)
				{
					if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider)
					{
						this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork());
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
			this.adjacentConnections = new TileEntity[6];

			for (byte i = 0; i < 6; i++)
			{
				ForgeDirection side = ForgeDirection.getOrientation(i);
				Vector3 thisVec = new Vector3(this);
				TileEntity tileEntity = thisVec.modifyPositionFromSide(side).getTileEntity(worldObj);

				if (tileEntity instanceof IConnector)
				{
					if (((IConnector) tileEntity).canConnect(side.getOpposite()))
					{
						this.adjacentConnections[i] = tileEntity;
					}
				}
			}
		}

		return this.adjacentConnections;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
	}
}
