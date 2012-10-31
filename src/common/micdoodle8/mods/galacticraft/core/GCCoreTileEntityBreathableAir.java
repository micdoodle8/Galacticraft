package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.TileEntity;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityBreathableAir extends TileEntity
{
	GCCoreTileEntityOxygenDistributor distributor;
	
	@Override
	public void updateEntity() 
	{
		if (this.distributor != null)
		{
			TileEntity distributorTile = this.worldObj.getBlockTileEntity(distributor.xCoord, distributor.yCoord, distributor.zCoord);
			
			if (distributorTile == null || ((GCCoreTileEntityOxygenDistributor)distributorTile).currentPower < 1.0D)
			{
				distributor = null;
			}
		}
		
		if (this.distributor == null || this.distributor.currentPower < 1.0D)
		{
			if (!this.worldObj.isRemote)
			{
				this.worldObj.setBlock(xCoord, yCoord, zCoord, 0);
				this.invalidate();
			}
		}
		else
		{
			double distanceFromDistributor = distributor.getDistanceFrom(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D);
			
			if (distanceFromDistributor < distributor.currentPower)
			{
				for (int j = -1; j <= 1; j++)
				{
					for (int i = -1; i <= 1; i++)
					{
						for (int k = -1; k <= 1; k++)
						{
							if (this.worldObj.isAirBlock(xCoord + i, yCoord + j, zCoord + k))
							{
								this.worldObj.setBlockWithNotify(xCoord + i, yCoord + j, zCoord + k, GCCoreBlocks.breatheableAir.blockID);
								
								TileEntity tile = this.worldObj.getBlockTileEntity(xCoord + i, yCoord + j, zCoord + k);
								
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
