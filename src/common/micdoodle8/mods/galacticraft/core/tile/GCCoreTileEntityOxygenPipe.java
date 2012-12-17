package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockOxygenDistributor;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class GCCoreTileEntityOxygenPipe extends TileEntity
{
	private double oxygenInPipe;
	private int indexFromCollector;
	
	private GCCoreTileEntityOxygenCollector source;
	
	public void setOxygenInPipe(double d)
	{
		this.oxygenInPipe = d;
	}
	
	public double getOxygenInPipe()
	{
		return this.oxygenInPipe;
	}
	
	public void setSourceCollector(GCCoreTileEntityOxygenCollector collector)
	{
		this.source = collector;
	}
	
	public GCCoreTileEntityOxygenCollector getSourceCollector()
	{
		return this.source;
	}
	
	public void setIndexFromCollector(int i)
	{
		this.indexFromCollector = i;
	}
	
	public int getIndexFromCollector()
	{
		return this.indexFromCollector;
	}
	
	@Override
	public void updateEntity() 
	{
		if (this.source == null)
		{
			this.oxygenInPipe = 0.0D;
		}
		else
		{
			this.source = (GCCoreTileEntityOxygenCollector) this.worldObj.getBlockTileEntity(this.source.xCoord, this.source.yCoord, this.source.zCoord);
			
//			if (!this.worldObj.isRemote)
			{
				final int[] idSet = new int[6];
				
				idSet[0] = this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord);
				idSet[1] = this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord);
				idSet[2] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1);
				idSet[3] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1);
				idSet[4] = this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord);
				idSet[5] = this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord);
				
				TileEntity tile;

				for (int i = 0; i < idSet.length; i++)
				{
					if (this.oxygenInPipe > 0)
					{
						if (idSet[0] == GCCoreBlocks.oxygenPipe.blockID)
						{
							tile = this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
							if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
							{
								if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0 || ((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() > this.getIndexFromCollector())
								{
									((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
									((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
									
									if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0)
										((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(this.indexFromCollector + 1);
								}
							}
						}
						if (idSet[1] == GCCoreBlocks.oxygenPipe.blockID)
						{
							tile = this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
							if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
							{
								if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0 || ((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() > this.getIndexFromCollector())
								{
									((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
									((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
									
									if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0)
										((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(this.indexFromCollector + 1);
								}
							}
						}
						if (idSet[2] == GCCoreBlocks.oxygenPipe.blockID)
						{
							tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
							if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
							{
								if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0 || ((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() > this.getIndexFromCollector())
								{
									((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
									((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
									
									if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0)
										((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(this.indexFromCollector + 1);
								}
							}
						}
						if (idSet[3] == GCCoreBlocks.oxygenPipe.blockID)
						{
							tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
							if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
							{
								if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0 || ((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() > this.getIndexFromCollector())
								{
									((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
									((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
									
									if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0)
										((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(this.indexFromCollector + 1);
								}
							}
						}
						if (idSet[4] == GCCoreBlocks.oxygenPipe.blockID)
						{
							tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
							if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
							{
								if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0 || ((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() > this.getIndexFromCollector())
								{
									((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
									((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
									
									if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0)
										((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(this.indexFromCollector + 1);
								}
							}
						}
						if (idSet[5] == GCCoreBlocks.oxygenPipe.blockID)
						{
							tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
							if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
							{
								if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0 || ((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() > this.getIndexFromCollector())
								{
									((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
									((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
									
									if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0)
										((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(this.indexFromCollector + 1);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void setZeroOxygen()
	{
		this.setIndexFromCollector(0);
		
		this.setOxygenInPipe(0D);
		
		for (int i = 0; i < ForgeDirection.values().length; i++)
    	{
    		final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + ForgeDirection.getOrientation(i).offsetX, this.yCoord + ForgeDirection.getOrientation(i).offsetY, this.zCoord + ForgeDirection.getOrientation(i).offsetZ);
    		
    		if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
    		{
    			final GCCoreTileEntityOxygenPipe pipe = (GCCoreTileEntityOxygenPipe)tile;
    			
    			if (pipe.getIndexFromCollector() > this.getIndexFromCollector())
    			{
    				pipe.setZeroOxygen();
    			}
    		}
    		else if (tile != null && tile instanceof GCCoreTileEntityOxygenDistributor)
    		{
    			final GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor)tile;
    			distributor.currentPower = 0D;
    			GCCoreBlockOxygenDistributor.updateDistributorState(false, this.worldObj, distributor.xCoord, distributor.yCoord, distributor.zCoord);
    		}
    	}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.readFromNBT(par1NBTTagCompound);
		
		this.oxygenInPipe = par1NBTTagCompound.getDouble("oxygenInPipe");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.writeToNBT(par1NBTTagCompound);
		
        par1NBTTagCompound.setDouble("oxygenInPipe", this.oxygenInPipe);
        
        if (this.source != null)
        {
            par1NBTTagCompound.setInteger("SourceX", this.source.xCoord);
            par1NBTTagCompound.setInteger("SourceY", this.source.yCoord);
            par1NBTTagCompound.setInteger("SourceZ", this.source.zCoord);
        }
	}
}
