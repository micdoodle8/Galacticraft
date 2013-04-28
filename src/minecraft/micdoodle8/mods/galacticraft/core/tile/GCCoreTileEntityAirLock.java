package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityAirLock extends TileEntityAdvanced
{
	public boolean active;
	public boolean lastActive;
	public ArrayList<GCCoreTileEntityAirLock> otherAirLocks;
	public ArrayList<GCCoreTileEntityAirLock> lastOtherAirLocks;
	private AirLockProtocol protocol;
	private AirLockProtocol lastProtocol = protocol;

    @Override
    public boolean canUpdate()
    {
        return true;
    }
    
    @Override
	public void updateEntity()
    {
    	super.updateEntity();
    	
    	if (this.protocol == null)
    	{
    		protocol = lastProtocol = new AirLockProtocol(this, 40);
    	}
    	
    	if (this.ticks % 10 == 0 && !this.worldObj.isRemote)
    	{
    		otherAirLocks = protocol.calculate();
    		
    		if (this.active && ((this.otherAirLocks != null) || (this.otherAirLocks != null && this.lastOtherAirLocks != null && this.otherAirLocks != this.lastOtherAirLocks) ||(this.otherAirLocks != null && this.lastOtherAirLocks != null && this.otherAirLocks.size() != this.lastOtherAirLocks.size())))
    		{
        		if (protocol.minX != protocol.maxX)
        		{
        			for (int x = protocol.minX + 1; x <= protocol.maxX - 1; x++)
        			{
            			for (int y = protocol.minY + 1; y <= protocol.maxY - 1; y++)
            			{
            				this.worldObj.setBlock(x, y, protocol.minZ, GCCoreBlocks.airLockSeal.blockID, 0, 3);
            			}
        			}
        		}
        		else if (protocol.minZ != protocol.maxZ)
        		{
        			for (int z = protocol.minZ + 1; z <= protocol.maxZ - 1; z++)
        			{
            			for (int y = protocol.minY + 1; y <= protocol.maxY - 1; y++)
            			{
            				this.worldObj.setBlock(protocol.minX, y, z, GCCoreBlocks.airLockSeal.blockID, 0, 3);
            			}
        			}
        		}
    		}
    		else if ((!this.active && this.lastActive) || (this.otherAirLocks == null && this.lastOtherAirLocks != null))
    		{
        		if (lastProtocol.minX != lastProtocol.maxX)
        		{
        			for (int x = lastProtocol.minX + 1; x <= lastProtocol.maxX - 1; x++)
        			{
            			for (int y = lastProtocol.minY + 1; y <= lastProtocol.maxY - 1; y++)
            			{
            				this.worldObj.setBlockToAir(x, y, lastProtocol.minZ);
            			}
        			}
        		}
        		else if (lastProtocol.minZ != lastProtocol.maxZ)
        		{
        			for (int z = lastProtocol.minZ + 1; z <= lastProtocol.maxZ - 1; z++)
        			{
            			for (int y = lastProtocol.minY + 1; y <= lastProtocol.maxY - 1; y++)
            			{
            				this.worldObj.setBlockToAir(lastProtocol.minX, y, z);
            			}
        			}
        		}
    		}
    		
        	this.lastActive = this.active;
        	this.lastOtherAirLocks = this.otherAirLocks;
        	this.lastProtocol = this.protocol;
    	}
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	this.active = par1NBTTagCompound.getBoolean("active");
    	this.lastActive = par1NBTTagCompound.getBoolean("lastActive");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	nbt.setBoolean("active", this.active);
    	nbt.setBoolean("lastActive", this.lastActive);
    }
}
