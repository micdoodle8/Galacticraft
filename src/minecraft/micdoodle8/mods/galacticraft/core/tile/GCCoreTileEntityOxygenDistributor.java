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

	@Override
	public void removeSource(TileEntityOxygenSource source)
	{
		this.sources.remove(source);
	}

	@Override
	public void setSourceCollectors(Set<TileEntityOxygenSource> sources)
	{
		this.sources = sources;
	}

	@Override
	public Set<TileEntityOxygenSource> getSourceCollectors()
	{
		return this.sources;
	}

	@Override
	public void setIndexFromSource(int index)
	{
		this.indexFromCollector = index;
	}

	@Override
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

		if (this.preLoadSourceCoords != null)
		{
			for (final GCCoreBlockLocation location : this.preLoadSourceCoords)
			{
	            final TileEntity tile = this.worldObj.getBlockTileEntity(location.chunkZPos, location.chunkZPos, location.chunkZPos);

	            if (tile != null && tile instanceof TileEntityOxygenSource)
	            {
	                this.sources.add((TileEntityOxygenSource) tile);
	            }
			}

			this.preLoadSourceCoords = null;
		}

		this.updateSourceList();

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

					this.updateAdjacentOxygenAdd(ForgeDirection.getOrientation(i).offsetX, ForgeDirection.getOrientation(i).offsetY, ForgeDirection.getOrientation(i).offsetZ);
			    }
				else if (!this.active)
				{
					this.updateAdjacentOxygenRemove(x, y, z);
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
		final ArrayList<TileEntityOxygenSource> sources = new ArrayList<TileEntityOxygenSource>();

		for (final TileEntityOxygenSource source : this.sources)
		{
			sources.add(source);
		}

		final ListIterator li = sources.listIterator();

		while (li.hasNext())
		{
			final TileEntityOxygenSource source = (TileEntityOxygenSource) li.next();

			if (!(this.worldObj.getBlockTileEntity(source.xCoord, source.yCoord, source.zCoord) instanceof TileEntityOxygenSource))
			{
				this.sources.remove(source);
			}
		}
	}

	public void updateAdjacentOxygenAdd(int xOffset, int yOffset, int zOffset)
	{
		final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);

		if (tile != null && tile instanceof GCCoreTileEntityBreathableAir)
		{
			final GCCoreTileEntityBreathableAir air = (GCCoreTileEntityBreathableAir) tile;

			air.addDistributor(this);
		}
	}

	public void updateAdjacentOxygenRemove(int xOffset, int yOffset, int zOffset)
	{
		final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);

		if (tile != null && tile instanceof GCCoreTileEntityBreathableAir)
		{
			final GCCoreTileEntityBreathableAir air = (GCCoreTileEntityBreathableAir) tile;

			air.removeDistributor(this);
		}
	}

	public void updateOxygenFromAdjacentPipe(int xOffset, int yOffset, int zOffset)
	{
		final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);

		if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
		{
			final GCCoreTileEntityOxygenPipe pipe = (GCCoreTileEntityOxygenPipe) tile;

			this.currentPower = pipe.getOxygenInPipe();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.setActive(par1NBTTagCompound.getBoolean("active"));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("active", this.getActive());
	}
}