package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.tileentity.TileEntity;

public class GCCoreTileEntityBreathableAir extends TileEntity
{
	GCCoreTileEntityOxygenDistributor distributor;
	
	@Override
	public void updateEntity() 
	{
		if (this.distributor != null)
		{
			final TileEntity distributorTile = this.worldObj.getBlockTileEntity(this.distributor.xCoord, this.distributor.yCoord, this.distributor.zCoord);
			
			if (distributorTile == null || ((GCCoreTileEntityOxygenDistributor)distributorTile).currentPower < 1.0D)
			{
				this.distributor = null;
			}
		}
		
		if (this.distributor == null || this.distributor.currentPower < 1.0D)
		{
			if (!this.worldObj.isRemote)
			{
				this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, 0);
				this.invalidate();
			}
		}
		else
		{
			final double distanceFromDistributor = this.distributor.getDistanceFrom2(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D);
			
			if (distanceFromDistributor < this.distributor.currentPower)
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
									((GCCoreTileEntityBreathableAir)tile).setDistributor(this.distributor);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void setDistributor(GCCoreTileEntityOxygenDistributor distributor)
	{
		this.distributor = distributor;
	}
	
	public GCCoreTileEntityOxygenDistributor getDistributor()
	{
		return this.distributor;
	}
}
