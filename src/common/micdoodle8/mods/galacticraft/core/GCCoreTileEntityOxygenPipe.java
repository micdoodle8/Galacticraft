package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityOxygenPipe extends TileEntity
{
	private double oxygenInPipe;
	
	private GCCoreTileEntityOxygenCollector source;
	
	public GCCoreTileEntityOxygenPipe()
	{
		
	}
	
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
			
			int[] idSet = new int[6];
			
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
							((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
							((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
						}
					}
					if (idSet[1] == GCCoreBlocks.oxygenPipe.blockID)
					{
						tile = this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
						if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
						{
							((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
							((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
						}
					}
					if (idSet[2] == GCCoreBlocks.oxygenPipe.blockID)
					{
						tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
						if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
						{
							((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
							((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
						}
					}
					if (idSet[3] == GCCoreBlocks.oxygenPipe.blockID)
					{
						tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
						if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
						{
							((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
							((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
						}
					}
					if (idSet[4] == GCCoreBlocks.oxygenPipe.blockID)
					{
						tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
						if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
						{
							((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
							((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
						}
					}
					if (idSet[5] == GCCoreBlocks.oxygenPipe.blockID)
					{
						tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
						if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
						{
							((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.oxygenInPipe);
							((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this.source);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.readFromNBT(par1NBTTagCompound);
		
		this.oxygenInPipe = par1NBTTagCompound.getDouble("oxygenInPipe");
		
		this.source = (GCCoreTileEntityOxygenCollector) this.worldObj.getBlockTileEntity(par1NBTTagCompound.getInteger("SourceX"), par1NBTTagCompound.getInteger("SourceY"), par1NBTTagCompound.getInteger("SourceZ"));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.writeToNBT(par1NBTTagCompound);
		
        par1NBTTagCompound.setDouble("oxygenInPipe", this.oxygenInPipe);
        par1NBTTagCompound.setInteger("SourceX", this.source.xCoord);
        par1NBTTagCompound.setInteger("SourceY", this.source.yCoord);
        par1NBTTagCompound.setInteger("SourceZ", this.source.zCoord);
	}
}
