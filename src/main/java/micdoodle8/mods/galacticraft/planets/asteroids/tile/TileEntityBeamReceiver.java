package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.EnergyStorage;
import micdoodle8.mods.galacticraft.core.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.galacticraft.core.tile.TileEntityUniversalElectrical;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;

public class TileEntityBeamReceiver extends TileEntityBeamOutput implements IEnergyHandlerGC, ILaserNode
{
	@NetworkedField(targetSide = Side.CLIENT)
	public int facing = ForgeDirection.UNKNOWN.ordinal();
	private int preLoadFacing = -1;
	private EnergyStorage storage = new EnergyStorage(Integer.MAX_VALUE, 1);
	@NetworkedField(targetSide = Side.CLIENT)
	public int modeReceive = ReceiverMode.UNDEFINED.ordinal();
	public Vector3 color = new Vector3(0, 1, 0);
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.preLoadFacing != -1)
		{
			this.setFacing(ForgeDirection.getOrientation(this.preLoadFacing));
			this.preLoadFacing = -1;
		}
		
		if (this.getTarget() != null && this.modeReceive == ReceiverMode.EXTRACT.ordinal() && this.facing != ForgeDirection.UNKNOWN.ordinal())
		{
			TileEntity tile = this.getAttachedTile();
			
			if (tile instanceof TileEntityUniversalElectrical)
			{
				TileEntityUniversalElectrical electricalTile = (TileEntityUniversalElectrical) tile;
				EnergySourceAdjacent source = new EnergySourceAdjacent(ForgeDirection.getOrientation(this.facing).getOpposite());
				float toSend = electricalTile.storage.getMaxExtract();
				electricalTile.extractEnergyGC(source, this.getTarget().receiveEnergyGC(new EnergySourceWireless(Lists.newArrayList((ILaserNode)this)), toSend, false), false);
			}
		}
		
		if (this.modeReceive == ReceiverMode.RECEIVE.ordinal() && this.storage.getEnergyStoredGC() > 0)
		{
			TileEntity tile = this.getAttachedTile();
			
			if (tile instanceof TileEntityUniversalElectrical)
			{
				TileEntityUniversalElectrical electricalTile = (TileEntityUniversalElectrical) tile;
				EnergySourceAdjacent source = new EnergySourceAdjacent(ForgeDirection.getOrientation(this.facing).getOpposite());
				this.storage.extractEnergyGC((int) electricalTile.receiveEnergyGC(source, this.storage.getEnergyStoredGC(), false), false);
			}
		}
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
	public Vector3 getInputPoint() 
	{
		Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
		ForgeDirection facingDir = ForgeDirection.getOrientation(this.facing);
		headVec.x += facingDir.offsetX * 0.1F;
		headVec.y += facingDir.offsetY * 0.1F;
		headVec.z += facingDir.offsetZ * 0.1F;
		return headVec;
	}

	@Override
	public Vector3 getOutputPoint(boolean offset) 
	{
		Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
		ForgeDirection facingDir = ForgeDirection.getOrientation(this.facing);
		headVec.x += facingDir.offsetX * 0.1F;
		headVec.y += facingDir.offsetY * 0.1F;
		headVec.z += facingDir.offsetZ * 0.1F;
		return headVec;
	}

	@Override
	public TileEntity getTile()
	{
		return this;
	}
	
	public TileEntity getAttachedTile()
	{
		if (facing == ForgeDirection.UNKNOWN.ordinal())
		{
			return null;
		}
		
		TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, facing);
		
		if (tile == null || tile.isInvalid())
		{
			this.setFacing(ForgeDirection.UNKNOWN);
		}

		if (tile instanceof EnergyStorageTile)
		{
			EnergyStorage attachedStorage = ((EnergyStorageTile) tile).storage; 
			this.storage.setCapacity(attachedStorage.getCapacityGC());
			this.storage.setMaxReceive(attachedStorage.getMaxReceive());
			this.storage.setMaxExtract(attachedStorage.getMaxExtract());
		}
		
		return tile;
	}

	@Override
	public float receiveEnergyGC(EnergySource from, float amount, boolean simulate) 
	{
		if (this.modeReceive != ReceiverMode.RECEIVE.ordinal())
		{
			return 0;
		}
		
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN.ordinal())
		{
			return 0;
		}
		
		float received = this.storage.receiveEnergyGC(amount, simulate);
		
//		if (received < amount)
//		{
//			if (tile instanceof EnergyStorageTile)
//			{
//				received += ((EnergyStorageTile) tile).storage.receiveEnergyGC(amount - received, simulate);
//			}
//		}
		
		return received;
	}

	@Override
	public float extractEnergyGC(EnergySource from, float amount, boolean simulate) 
	{
		if (this.modeReceive != ReceiverMode.EXTRACT.ordinal())
		{
			return 0;
		}
		
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN.ordinal())
		{
			return 0;
		}
		
		float extracted = this.storage.extractEnergyGC(amount, simulate);
		
		if (extracted < amount)
		{
			if (tile instanceof EnergyStorageTile)
			{
				extracted += ((EnergyStorageTile) tile).storage.extractEnergyGC(amount - extracted, simulate);
			}
		}
		
		return extracted;
	}

	@Override
	public float getEnergyStoredGC(EnergySource from) 
	{
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN.ordinal())
		{
			return 0;
		}
		
		return this.storage.getEnergyStoredGC();
	}

	@Override
	public float getMaxEnergyStoredGC(EnergySource from) 
	{
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN.ordinal())
		{
			return 0;
		}
		
		return this.storage.getCapacityGC();
	}

	@Override
	public boolean nodeAvailable(EnergySource from)
	{
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN.ordinal())
		{
			return false;
		}
		
		return true;
	}
	
	public void setFacing(ForgeDirection newDirection)
	{
		if (newDirection.ordinal() != this.facing)
		{
			if (newDirection == ForgeDirection.UNKNOWN)
			{
				this.modeReceive = ReceiverMode.UNDEFINED.ordinal();
			}
			else
			{
				TileEntity tile = new Vector3(this).translate(new Vector3(newDirection)).getTileEntity(this.worldObj);
				
				if (tile == null)
				{
					this.modeReceive = ReceiverMode.UNDEFINED.ordinal();
				}
				else if (tile instanceof EnergyStorageTile)
				{
					ReceiverMode mode = ((EnergyStorageTile) tile).getModeFromDirection(newDirection.getOpposite());
					
					if (mode != null)
					{
						this.modeReceive = mode.ordinal();
					}
					else
					{
						this.modeReceive = ReceiverMode.UNDEFINED.ordinal();
					}
				}
			}
		}
		
		this.facing = newDirection.ordinal();
	}

	@Override
	public boolean canConnectTo(ILaserNode laserNode) 
	{
		return this.modeReceive != ReceiverMode.UNDEFINED.ordinal() && this.color.equals(laserNode.getColor());
	}

	@Override
	public Vector3 getColor() 
	{
		return new Vector3(0, 1, 0);
	}

	@Override
	public ILaserNode getTarget() 
	{
		if (this.modeReceive == ReceiverMode.EXTRACT.ordinal())
		{
			return super.getTarget();
		}
		
		return null;
	}

    public void readFromNBT(NBTTagCompound nbt)
    {
    	super.readFromNBT(nbt);
    	this.preLoadFacing = nbt.getInteger("FacingSide");
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	nbt.setInteger("FacingSide", this.facing);
    }
}
