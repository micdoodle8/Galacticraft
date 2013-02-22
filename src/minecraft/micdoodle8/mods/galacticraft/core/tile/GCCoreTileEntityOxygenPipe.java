package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import micdoodle8.mods.galacticraft.API.TileEntityOxygenSource;
import micdoodle8.mods.galacticraft.API.TileEntityOxygenTransmitter;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class GCCoreTileEntityOxygenPipe extends TileEntityOxygenTransmitter
{
	private double oxygenInPipe;
	private int indexFromCollector;
	
	private Set<TileEntityOxygenSource> sources = new HashSet<TileEntityOxygenSource>();
	private List<GCCoreBlockLocation> preLoadSourceCoords;

	@Override
	public void setOxygenInTransmitter(double oxygen) 
	{
		this.oxygenInPipe = oxygen;
	}

	@Override
	public double getOxygenInTransmitter() 
	{
		return this.oxygenInPipe;
	}

	@Override
	public void addSource(TileEntityOxygenSource source) 
	{
		this.sources.add(source);
	}

	@Override
	public void setIndexFromSource(int index) 
	{
		this.indexFromCollector = index;
	}
	
	public void setOxygenInPipe(double d)
	{
		this.oxygenInPipe = d;
	}
	
	public double getOxygenInPipe()
	{
		return this.oxygenInPipe;
	}
	
	public void setSourceCollectors(Set<TileEntityOxygenSource> sources)
	{
		this.sources = sources;
	}
	
	public Set<TileEntityOxygenSource> getSourceCollectors()
	{
		return this.sources;
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
		if (preLoadSourceCoords != null)
		{
			for (GCCoreBlockLocation location : this.preLoadSourceCoords)
			{
	            TileEntity tile = this.worldObj.getBlockTileEntity(location.chunkZPos, location.chunkZPos, location.chunkZPos);

	            if (tile != null && tile instanceof TileEntityOxygenSource)
	            {
	                this.sources.add((TileEntityOxygenSource) tile);
	            }
			}
			
			this.preLoadSourceCoords = null;
		}
		
		if (this.sources.size() == 0)
		{
			this.oxygenInPipe = 0.0D;
		}
		
		updateSourceList();
		
		if (this.oxygenInPipe > 0)
		{
			for (int i = 0; i < ForgeDirection.values().length; i++)
	    	{
				this.updateAdjacentBlock(ForgeDirection.getOrientation(i).offsetX, ForgeDirection.getOrientation(i).offsetY, ForgeDirection.getOrientation(i).offsetZ);
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
    			distributor.setActive(false);
    		}
    	}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.oxygenInPipe = par1NBTTagCompound.getDouble("oxygenInPipe");
        final NBTTagList var2 = par1NBTTagCompound.getTagList("sources");
        this.readSourcesFromNBT(var2);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setDouble("oxygenInPipe", this.oxygenInPipe);
        par1NBTTagCompound.setTag("sources", this.writeSourcesToNBT(new NBTTagList()));
	}
    
    public NBTTagList writeSourcesToNBT(NBTTagList par1NBTTagList)
    {
        int var2;
        NBTTagCompound var3;

		for (TileEntityOxygenSource source : this.sources)
		{
            if (source != null)
            {
                var3 = new NBTTagCompound();
                var3.setInteger("X", source.xCoord);
                var3.setInteger("Y", source.yCoord);
                var3.setInteger("Z", source.zCoord);
                par1NBTTagList.appendTag(var3);
            }
        }

        return par1NBTTagList;
    }

    public void readSourcesFromNBT(NBTTagList par1NBTTagList)
    {
        this.sources = new HashSet<TileEntityOxygenSource>();
        this.preLoadSourceCoords = new ArrayList<GCCoreBlockLocation>();

        for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2)
        {
            final NBTTagCompound var3 = (NBTTagCompound)par1NBTTagList.tagAt(var2);
            final int x = var3.getInteger("X");
            final int y = var3.getInteger("Y");
            final int z = var3.getInteger("Z");
            
            this.preLoadSourceCoords.add(new GCCoreBlockLocation(x, y, z));
        }
    }
	
	public void updateSourceList()
	{
		ArrayList<TileEntityOxygenSource> sources = new ArrayList<TileEntityOxygenSource>();
		
		for (TileEntityOxygenSource source : this.sources)
		{
			sources.add(source);
		}
		
		ListIterator li = sources.listIterator();
		
		while (li.hasNext())
		{
			TileEntityOxygenSource source = (TileEntityOxygenSource) li.next();
			
			if (!(this.worldObj.getBlockTileEntity(source.xCoord, source.yCoord, source.zCoord) instanceof TileEntityOxygenSource))
			{
				this.sources.remove(source);
			}
		}
	}
	
	public double getTotalOxygenValue()
	{
		double value = 0;

		for (TileEntityOxygenSource source : this.sources)
		{
			value += source.getPower();
		}
		
		return value;
	}
	
	public void updateAdjacentBlock(int xOffset, int yOffset, int zOffset)
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);
		
		if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
		{
			if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0 || ((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() > this.getIndexFromCollector())
			{
				((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.getTotalOxygenValue());
				this.updateAdjacentSources((GCCoreTileEntityOxygenPipe)tile);
				
				if (((GCCoreTileEntityOxygenPipe)tile).getIndexFromCollector() == 0)
				{
					((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(this.indexFromCollector + 1);
				}
			}
		}

		if (tile != null && tile instanceof GCCoreTileEntityOxygenDistributor)
		{
			if (((GCCoreTileEntityOxygenDistributor)tile).getIndexFromSource() == 0 || ((GCCoreTileEntityOxygenDistributor)tile).getIndexFromSource() > this.getIndexFromCollector())
			{
				this.updateAdjacentSources((GCCoreTileEntityOxygenDistributor)tile);
				
				if (((GCCoreTileEntityOxygenDistributor)tile).getIndexFromSource() == 0)
				{
					((GCCoreTileEntityOxygenDistributor)tile).setIndexFromSource(this.indexFromCollector + 1);
				}
			}
		}
	}
	
	public void updateAdjacentSources(GCCoreTileEntityOxygenPipe tile)
	{
		if (tile.sources == null || tile.sources.size() == 0)
		{
			tile.sources = this.sources;
		}
		else if (tile.sources.size() > 0)
		{
			for (TileEntityOxygenSource source : this.sources)
			{
				tile.addSource(source);
			}
		}
	}
	
	public void updateAdjacentSources(GCCoreTileEntityOxygenDistributor tile)
	{
		if (tile.getSourceCollectors() == null || tile.getSourceCollectors().size() == 0)
		{
			tile.setSourceCollectors(this.sources);
		}
		else if (tile.getSourceCollectors().size() > 0)
		{
			for (TileEntityOxygenSource source : this.sources)
			{
				tile.addSource(source);
			}
		}
	}
}
