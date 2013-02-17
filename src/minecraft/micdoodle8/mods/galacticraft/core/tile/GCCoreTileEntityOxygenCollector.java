package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

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
					final Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];

					if (block != null && block instanceof BlockLeaves)
					{
						power++;
					}
				}
			}
		}
		
		this.currentPower = power / 5.0D;

//		if (!this.worldObj.isRemote)
		{
			final int[] idSet = new int[6];
			
			idSet[0] = this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord);
			idSet[1] = this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord);
			idSet[2] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1);
			idSet[3] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1);
			idSet[4] = this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord);
			idSet[5] = this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord);
			
			TileEntity tile;

			for (final int element : idSet) {
				if (idSet[0] == GCCoreBlocks.oxygenPipe.blockID)
				{
					tile = this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
					if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
					{
						((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.currentPower);
						((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this);
						((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(1);
					}
				}
				if (idSet[1] == GCCoreBlocks.oxygenPipe.blockID)
				{
					tile = this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
					if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
					{
						((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.currentPower);
						((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this);
						((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(1);
					}
				}
				if (idSet[2] == GCCoreBlocks.oxygenPipe.blockID)
				{
					tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
					if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
					{
						((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.currentPower);
						((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this);
						((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(1);
					}
				}
				if (idSet[3] == GCCoreBlocks.oxygenPipe.blockID)
				{
					tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
					if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
					{
						((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.currentPower);
						((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this);
						((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(1);
					}
				}
				if (idSet[4] == GCCoreBlocks.oxygenPipe.blockID)
				{
					tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
					if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
					{
						((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.currentPower);
						((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this);
						((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(1);
					}
				}
				if (idSet[5] == GCCoreBlocks.oxygenPipe.blockID)
				{
					tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
					if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
					{
						((GCCoreTileEntityOxygenPipe)tile).setOxygenInPipe(this.currentPower);
						((GCCoreTileEntityOxygenPipe)tile).setSourceCollector(this);
						((GCCoreTileEntityOxygenPipe)tile).setIndexFromCollector(1);
					}
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
