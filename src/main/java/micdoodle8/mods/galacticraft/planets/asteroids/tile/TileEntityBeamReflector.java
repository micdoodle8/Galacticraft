package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBeamReflector extends TileEntityBeamOutput implements ILaserNode
{
	public Vector3 color = new Vector3(0, 1, 0);
	private EnergyStorage storage = new EnergyStorage(10, 1);
	private BlockVec3 preLoadTarget = null;
	private BlockVec3 lastTargetVec = BlockVec3.INVALID_VECTOR;

	@Override
	public void updateEntity()
	{
		if (this.preLoadTarget != null)
		{
			TileEntity tileAtTarget = this.worldObj.getTileEntity(preLoadTarget.x, preLoadTarget.y, preLoadTarget.z);
			
			if (tileAtTarget != null && tileAtTarget instanceof ILaserNode)
			{
				this.setTarget((ILaserNode) tileAtTarget);
				this.preLoadTarget = null;
			}
		}
		
		super.updateEntity();
		
		if (!this.targetVec.equals(this.lastTargetVec))
		{
			this.markDirty();
		}
		
		this.lastTargetVec = this.targetVec;
	}
	
	@Override
	public Vector3 getInputPoint() 
	{
		float distance = 0.15F;
		Vector3 deviation = new Vector3(Math.sin(Math.toRadians(this.yaw - 180)) * distance, 0, Math.cos(Math.toRadians(this.yaw - 180)) * distance);
		Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 1.13228 / 2.0, this.zCoord + 0.5);
		headVec.translate(deviation.clone().invert());
		return headVec;
	}

	@Override
	public Vector3 getOutputPoint(boolean offset) 
	{
		return new Vector3(this.xCoord + 0.5, this.yCoord + 1.13228 / 2.0, this.zCoord + 0.5);
	}

	@Override
	public double getPacketRange() 
	{
		return 24.0D;
	}

	@Override
	public int getPacketCooldown() 
	{
		return 3;
	}

	@Override
	public boolean isNetworkedTile() 
	{
		return true;
	}

	@Override
	public Vector3 getColor() 
	{
		return this.color;
	}

	@Override
	public boolean canConnectTo(ILaserNode laserNode) 
	{
		return this.color.equals(laserNode.getColor());
	}

	@Override
	public int receiveEnergyGC(EnergySource from, int amount, boolean simulate) 
	{
		if (this.getTarget() != null)
		{
			if (from instanceof EnergySourceWireless)
			{
				if (((EnergySourceWireless) from).nodes.contains(this.getTarget()))
				{
					return 0;
				}
				
				((EnergySourceWireless) from).nodes.add(this);
				
				return this.getTarget().receiveEnergyGC(from, amount, simulate);
			}
			else
			{
				return 0;
			}
		}
		
		return this.storage.receiveEnergyGC(amount, simulate);
	}

	@Override
	public int extractEnergyGC(EnergySource from, int amount, boolean simulate) 
	{
		return 0;
	}

	@Override
	public boolean nodeAvailable(EnergySource from) 
	{
		return from instanceof EnergySourceWireless;
	}

	@Override
	public int getEnergyStoredGC(EnergySource from) 
	{
		return this.storage.getEnergyStoredGC();
	}

	@Override
	public int getMaxEnergyStoredGC(EnergySource from) 
	{
		return this.storage.getCapacityGC();
	}

    public void readFromNBT(NBTTagCompound nbt)
    {
    	super.readFromNBT(nbt);
    	
    	if (nbt.getBoolean("HasTarget"))
    	{
        	this.preLoadTarget = new BlockVec3(nbt.getInteger("TargetX"), nbt.getInteger("TargetY"), nbt.getInteger("TargetZ"));
    	}
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	
    	nbt.setBoolean("HasTarget", this.getTarget() != null);
    	
    	if (this.getTarget() != null)
    	{
    		nbt.setInteger("TargetX", this.getTarget().getTile().xCoord);
    		nbt.setInteger("TargetY", this.getTarget().getTile().yCoord);
    		nbt.setInteger("TargetZ", this.getTarget().getTile().zCoord);
    	}
    }
	
    @Override
	public void setTarget(ILaserNode target)
    {
    	super.setTarget(target);
	}
}
