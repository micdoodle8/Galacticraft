package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLeaves;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import cpw.mods.fml.common.FMLLog;

public class GCTileEntityOxygenCollector extends TileEntity
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
