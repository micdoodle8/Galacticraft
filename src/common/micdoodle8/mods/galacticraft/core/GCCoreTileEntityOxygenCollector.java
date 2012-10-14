package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLeaves;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityOxygenCollector extends TileEntity
{
	public double currentPower;
    
	@Override
	public void updateEntity() 
	{
		super.updateEntity();
		
		double power = 0;
		
		for (int y = this.yCoord - 10; y <= this.yCoord + 10; y++)
		{
			for (int x = this.xCoord - 10; x <= this.xCoord + 10; x++)
			{
				for (int z = this.zCoord - 10; z <= this.zCoord + 10; z++)
				{
					Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];

					if (block != null && block instanceof BlockLeaves)
					{
						power++;
					}
				}
			}
		}
		
		this.currentPower = power / 10.0D;

		int[] idSet = new int[6];
		
		idSet[0] = this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord);
		idSet[1] = this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord);
		idSet[2] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1);
		idSet[3] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1);
		idSet[4] = this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord);
		idSet[5] = this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord);

		for (int i = 0; i < idSet.length; i++)
		{
			if (this.currentPower > 0)
			{
				if (idSet[0] == GCCoreBlocks.oxygenPipe.blockID)
				{
					FMLLog.info("0");
					this.worldObj.setBlockMetadata(this.xCoord + 1, this.yCoord, this.zCoord, 1);
				}
				if (idSet[1] == GCCoreBlocks.oxygenPipe.blockID)
				{
					FMLLog.info("1");
					this.worldObj.setBlockMetadata(this.xCoord - 1, this.yCoord, this.zCoord, 1);
				}
				if (idSet[2] == GCCoreBlocks.oxygenPipe.blockID)
				{
					FMLLog.info("2");
					this.worldObj.setBlockMetadata(this.xCoord, this.yCoord, this.zCoord + 1, 1);
				}
				if (idSet[3] == GCCoreBlocks.oxygenPipe.blockID)
				{
//					FMLLog.info("3");
					this.worldObj.setBlockMetadata(this.xCoord, this.yCoord, this.zCoord - 1, 1);
				}
				if (idSet[4] == GCCoreBlocks.oxygenPipe.blockID)
				{
					FMLLog.info("4");
					this.worldObj.setBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord, 1);
				}
				if (idSet[5] == GCCoreBlocks.oxygenPipe.blockID)
				{
					FMLLog.info("5");
					this.worldObj.setBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord, 1);
				}
			}
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
}
