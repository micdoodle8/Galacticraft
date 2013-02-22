package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import micdoodle8.mods.galacticraft.API.TileEntityOxygenAcceptor;
import micdoodle8.mods.galacticraft.API.TileEntityOxygenSource;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLocation;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelFan;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenDistributor extends TileEntityOxygenAcceptor
{
    protected double currentPower;
    protected double lastPower;
    protected boolean active;
	private int indexFromCollector;
	
	private Set<TileEntityOxygenSource> sources = new HashSet<TileEntityOxygenSource>();
	private List<GCCoreBlockLocation> preLoadSourceCoords;

    public GCCoreModelFan fanModel1 = new GCCoreModelFan();
    public GCCoreModelFan fanModel2 = new GCCoreModelFan();
    public GCCoreModelFan fanModel3 = new GCCoreModelFan();
   	public GCCoreModelFan fanModel4 = new GCCoreModelFan();

	@Override
	public void addSource(TileEntityOxygenSource source) 
	{
		this.sources.add(source);
	}
	
	public void setSourceCollectors(Set<TileEntityOxygenSource> sources)
	{
		this.sources = sources;
	}
	
	public Set<TileEntityOxygenSource> getSourceCollectors()
	{
		return this.sources;
	}

	@Override
	public void setIndexFromSource(int index) 
	{
		this.indexFromCollector = index;
	}

	public int getIndexFromSource() 
	{
		return this.indexFromCollector;
	}
	
    @Override
	public boolean getActive()
	{
		return this.active;
	}

    @Override
	public void setActive(boolean active)
	{
		this.active = active;
	}

	@Override
	public double getPower() 
	{
		return this.currentPower;
	}

	@Override
	public void setPower(double power) 
	{
		this.currentPower = this.lastPower = power;
	}

    @Override
  	public void validate()
  	{
   		super.validate();

   		if (!this.isInvalid() && this.worldObj != null)
      	{
   		   	this.fanModel1 = new GCCoreModelFan();
   		    this.fanModel2 = new GCCoreModelFan();
   		    this.fanModel3 = new GCCoreModelFan();
   		   	this.fanModel4 = new GCCoreModelFan();
      	}
  	}
    
    public double getDistanceFrom2(double par1, double par3, double par5)
    {
        final double var7 = this.xCoord + 0.5D - par1;
        final double var9 = this.yCoord + 0.5D - par3;
        final double var11 = this.zCoord + 0.5D - par5;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();

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
		
		updateSourceList();

		for (int i = 0; i < ForgeDirection.values().length; i++)
    	{
			this.updateOxygenFromAdjacentPipe(ForgeDirection.getOrientation(i).offsetX, ForgeDirection.getOrientation(i).offsetY, ForgeDirection.getOrientation(i).offsetZ);
    	}
		
		if (this.currentPower < 1.0D)
		{
			this.active = false;
		}
		else
		{
			this.active = true;
		}
		
		if (!this.worldObj.isRemote)
		{
			for (int i = 0; i < ForgeDirection.values().length; i++)
	    	{
				int x, y, z;
				x = ForgeDirection.getOrientation(i).offsetX + this.xCoord;
				y = ForgeDirection.getOrientation(i).offsetY + this.yCoord;
				z = ForgeDirection.getOrientation(i).offsetZ + this.zCoord;
				
				if (this.active)
				{
					if (this.worldObj.getBlockId(x, y, z) == 0)
					{
						this.worldObj.setBlockWithNotify(x, y, z, GCCoreBlocks.breatheableAir.blockID);
					}
					
					this.updateAdjacentOxygenAdd(x - this.xCoord, y - this.yCoord, z - this.zCoord);
			    }
				else if (!active)
				{
					this.updateAdjacentOxygenRemove(x - this.xCoord, y - this.yCoord, z - this.zCoord);
				}
	    	}
		}
			
		if (this.active)
		{
			final int power = Math.min((int) Math.floor(this.currentPower / 3), 8);
			
			for (int j = -power; j <= power; j++)
			{
				for (int i = -power; i <= power; i++)
				{
					for (int k = -power; k <= power; k++)
					{
						if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCCoreBlocks.breatheableAir.blockID)
						{
							this.worldObj.scheduleBlockUpdate(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCCoreBlocks.breatheableAir.blockID, GCCoreBlocks.breatheableAir.tickRate());
						}
						else if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCCoreBlocks.unlitTorch.blockID)
						{
							final int meta = this.worldObj.getBlockMetadata(this.xCoord + i, this.yCoord + j, this.zCoord + k);
							this.worldObj.setBlockAndMetadataWithNotify(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCCoreBlocks.unlitTorchLit.blockID, meta);
						}
					}
				}
			}
		}
		
		this.lastPower = this.currentPower;
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
	
	public void updateAdjacentOxygenAdd(int xOffset, int yOffset, int zOffset)
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);
		
		if (tile != null && tile instanceof GCCoreTileEntityBreathableAir)
		{
			GCCoreTileEntityBreathableAir air = (GCCoreTileEntityBreathableAir) tile;
			
			// TODO add to list, not just set it as only distributor
			air.setDistributor(this);
		}
	}
	
	public void updateAdjacentOxygenRemove(int xOffset, int yOffset, int zOffset)
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);
		
		if (tile != null && tile instanceof GCCoreTileEntityBreathableAir)
		{
			GCCoreTileEntityBreathableAir air = (GCCoreTileEntityBreathableAir) tile;
			
			// TODO remove from list, not just set it as only distributor
//			air.setDistributor(this);
		}
	}
	
	public void updateOxygenFromAdjacentPipe(int xOffset, int yOffset, int zOffset)
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);
		
		if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
		{
			GCCoreTileEntityOxygenPipe pipe = (GCCoreTileEntityOxygenPipe) tile;
			
			this.currentPower = pipe.getOxygenInPipe();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.setActive(par1NBTTagCompound.getBoolean("active"));
        final NBTTagList var2 = par1NBTTagCompound.getTagList("sources");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("active", this.getActive());
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
}