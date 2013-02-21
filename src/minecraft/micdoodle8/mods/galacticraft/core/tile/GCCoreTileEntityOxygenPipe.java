package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockOxygenDistributor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class GCCoreTileEntityOxygenPipe extends TileEntity
{
	private double oxygenInPipe;
	private int indexFromCollector;
	
	private Set<GCCoreTileEntityOxygenCollector> sources = new HashSet<GCCoreTileEntityOxygenCollector>();
	
	public void setOxygenInPipe(double d)
	{
		this.oxygenInPipe = d;
	}
	
	public double getOxygenInPipe()
	{
		return this.oxygenInPipe;
	}
	
	public void addSource(GCCoreTileEntityOxygenCollector collector)
	{
		this.sources.add(collector);
	}
	
	public void setSourceCollectors(Set<GCCoreTileEntityOxygenCollector> sources)
	{
		this.sources = sources;
	}
	
	public Set<GCCoreTileEntityOxygenCollector> getSourceCollectors()
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
		if (this.sources.size() == 0)
		{
			this.oxygenInPipe = 0.0D;
		}
		
		updateSourceList();
		
		if (this.oxygenInPipe > 0)
		{
			for (int i = 0; i < ForgeDirection.values().length; i++)
	    	{
				this.updateAdjacentPipe(ForgeDirection.getOrientation(i).offsetX, ForgeDirection.getOrientation(i).offsetY, ForgeDirection.getOrientation(i).offsetZ);
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

		for (GCCoreTileEntityOxygenCollector source : this.sources)
		{
            if (source != null)
            {
                var3 = new NBTTagCompound();
                var3.setByte("X", (byte)source.xCoord);
                var3.setByte("Y", (byte)source.yCoord);
                var3.setByte("Z", (byte)source.zCoord);
                par1NBTTagList.appendTag(var3);
            }
        }

        return par1NBTTagList;
    }

    public synchronized void readSourcesFromNBT(NBTTagList par1NBTTagList)
    {
        this.sources = new HashSet<GCCoreTileEntityOxygenCollector>();

        for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2)
        {
            final NBTTagCompound var3 = (NBTTagCompound)par1NBTTagList.tagAt(var2);
            final int x = var3.getByte("X") & 255;
            final int y = var3.getByte("Y") & 255;
            final int z = var3.getByte("Z") & 255;
            
            TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

            if (tile != null && tile instanceof GCCoreTileEntityOxygenCollector)
            {
                this.sources.add((GCCoreTileEntityOxygenCollector) tile);
            }
        }
    }
	
	public synchronized void updateSourceList()
	{
		ArrayList<GCCoreTileEntityOxygenCollector> sources = new ArrayList<GCCoreTileEntityOxygenCollector>();
		
		for (GCCoreTileEntityOxygenCollector source : this.sources)
		{
			sources.add(source);
		}
		
		ListIterator li = sources.listIterator();
		
		while (li.hasNext())
		{
			GCCoreTileEntityOxygenCollector source = (GCCoreTileEntityOxygenCollector) li.next();
			
			if (!(this.worldObj.getBlockTileEntity(source.xCoord, source.yCoord, source.zCoord) instanceof GCCoreTileEntityOxygenCollector))
			{
				this.sources.remove(source);
			}
		}
	}
	
	public double getTotalOxygenValue()
	{
		double value = 0;

		for (GCCoreTileEntityOxygenCollector source : this.sources)
		{
			value += source.currentPower;
		}
		
		return value;
	}
	
	public void updateAdjacentPipe(int xOffset, int yOffset, int zOffset)
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
	}
	
	public void updateAdjacentSources(GCCoreTileEntityOxygenPipe tile)
	{
		if (tile.sources == null || tile.sources.size() == 0)
		{
			tile.sources = this.sources;
		}
		else if (tile.sources.size() > 0)
		{
			for (GCCoreTileEntityOxygenCollector source : this.sources)
			{
				tile.addSource(source);
			}
		}
	}
}
