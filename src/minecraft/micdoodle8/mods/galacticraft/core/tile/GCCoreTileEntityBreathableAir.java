package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.tileentity.TileEntity;

public class GCCoreTileEntityBreathableAir extends TileEntity
{
	GCCoreTileEntityOxygenDistributor closestDistributor;
	private HashSet<GCCoreTileEntityOxygenDistributor> distributors = new HashSet<GCCoreTileEntityOxygenDistributor>();
	public HashSet<GCCoreTileEntityBreathableAir> connectedAir = new HashSet<GCCoreTileEntityBreathableAir>();

	@Override
	public void updateEntity()
	{
		if (this.worldObj.isRemote)
		{
			return;
		}

		this.closestDistributor = this.getClosestDistributor();

		if (this.closestDistributor != null)
		{
			final TileEntity distributorTile = this.worldObj.getBlockTileEntity(this.closestDistributor.xCoord, this.closestDistributor.yCoord, this.closestDistributor.zCoord);

			if (distributorTile == null || !(distributorTile instanceof GCCoreTileEntityOxygenDistributor) || ((GCCoreTileEntityOxygenDistributor)distributorTile).currentPower < 1.0D)
			{
				this.closestDistributor = null;
			}
		}

		if (this.closestDistributor == null || this.closestDistributor.currentPower < 1.0D)
		{
			if (!this.worldObj.isRemote)
			{
				this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, 0);
				this.invalidate();
			}
		}
		else
		{
			final double distanceFromDistributor = this.closestDistributor.getDistanceFrom2(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D);

			if (distanceFromDistributor <= this.closestDistributor.currentPower)
			{
				for (int j = -1; j <= 1; j++)
				{
					for (int i = -1; i <= 1; i++)
					{
						for (int k = -1; k <= 1; k++)
						{
							if (this.worldObj.isAirBlock(this.xCoord + i, this.yCoord + j, this.zCoord + k))
							{
								this.worldObj.setBlockWithNotify(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCCoreBlocks.breatheableAir.blockID);

								final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + i, this.yCoord + j, this.zCoord + k);

								if (tile != null && tile instanceof GCCoreTileEntityBreathableAir)
								{
									this.connectedAir.add((GCCoreTileEntityBreathableAir) tile);
									((GCCoreTileEntityBreathableAir) tile).setDistributors(this.getDistributors());
								}
							}
						}
					}
				}
			}
		}
	}

	public void setDistributors(HashSet<GCCoreTileEntityOxygenDistributor> distributors)
	{
		this.distributors = distributors;
	}

	public HashSet<GCCoreTileEntityOxygenDistributor> getDistributors()
	{
		return this.distributors;
	}

	private GCCoreTileEntityOxygenDistributor getClosestDistributor()
	{
		double distance = 5000;
		GCCoreTileEntityOxygenDistributor closestDistributor = null;

		for (final GCCoreTileEntityOxygenDistributor distributor : this.getDistributors())
		{
			final double distanceFromDistributor = distributor.getDistanceFrom2(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D);

			if (distanceFromDistributor < distance && distributor.getActive())
			{
				distance = distanceFromDistributor;
				closestDistributor = distributor;
			}
		}

		return closestDistributor;
	}

	public void removeDistributor(GCCoreTileEntityOxygenDistributor distributor)
	{
		this.getDistributors().remove(distributor);

		if (this.connectedAir != null)
		{
			for (final GCCoreTileEntityBreathableAir air : this.connectedAir)
			{
				air.getDistributors().remove(distributor);
			}
		}

		this.closestDistributor = this.getClosestDistributor();
	}

	public void addDistributor(GCCoreTileEntityOxygenDistributor distributor)
	{
		this.getDistributors().add(distributor);

		if (this.connectedAir != null)
		{
			for (final GCCoreTileEntityBreathableAir air : this.connectedAir)
			{
				air.getDistributors().add(distributor);
			}
		}

		this.closestDistributor = this.getClosestDistributor();
	}
}
