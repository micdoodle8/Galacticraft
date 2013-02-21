package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelFan;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenDistributor extends TileEntity
{
    public double currentPower;
    
    public double lastPower;
    
    private boolean active;

    public GCCoreModelFan fanModel1 = new GCCoreModelFan();
    public GCCoreModelFan fanModel2 = new GCCoreModelFan();
    public GCCoreModelFan fanModel3 = new GCCoreModelFan();
   	public GCCoreModelFan fanModel4 = new GCCoreModelFan();

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
	
	public boolean getActive()
	{
		return this.active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
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
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("active", this.getActive());
	}
}