package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelFan;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityOxygenCollector extends TileEntity
{
	public double currentPower;

    public GCCoreModelFan fanModel = new GCCoreModelFan();

    @Override
  	public void validate()
  	{
   		super.validate();

   		if (!this.isInvalid() && this.worldObj != null)
      	{
   		   	this.fanModel = new GCCoreModelFan();
      	}
  	}
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		double power = 0;
		
		for (int y = this.yCoord - 5; y <= this.yCoord + 5; y++)
		{
			for (int x = this.xCoord - 5; x <= this.xCoord + 5; x++)
			{
				for (int z = this.zCoord - 5; z <= this.zCoord + 5; z++)
				{
					final Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];

					if (block != null && block instanceof BlockLeaves)
					{
						if (this.worldObj.rand.nextInt(100000) == 0)
						{
							this.worldObj.setBlock(x, y, z, 0);
						}
						
						power++;
					}
				}
			}
		}
		
		this.currentPower = power / 5.0D;
		
		for (int i = 0; i < ForgeDirection.values().length; i++)
    	{
			this.updateAdjacentPipe(ForgeDirection.getOrientation(i).offsetX, ForgeDirection.getOrientation(i).offsetY, ForgeDirection.getOrientation(i).offsetZ);
    	}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		
		this.currentPower = par1NBTTagCompound.getDouble("power");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		
        par1NBTTagCompound.setDouble("power", this.currentPower);
	}

	public void updateAdjacentPipe(int xOffset, int yOffset, int zOffset)
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);
		
		if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
		{
			((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.currentPower);
			((GCCoreTileEntityOxygenPipe)tile).addSource(this);
			((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(1);
		}
	}
}
