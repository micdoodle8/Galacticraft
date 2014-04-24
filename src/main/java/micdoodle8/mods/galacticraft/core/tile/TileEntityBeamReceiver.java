package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Lists;

public class TileEntityBeamReceiver extends TileEntityBeamOutput implements IEnergyHandlerGC, ILaserNode
{
	public enum ReceiverMode
	{
		EXTRACT,
		RECEIVE,
		UNDEFINED
	}
	
	public ForgeDirection facing = ForgeDirection.UNKNOWN;
	private EnergyStorage storage = new EnergyStorage(Integer.MAX_VALUE, 1);
	public ReceiverMode modeReceive = ReceiverMode.UNDEFINED;
	public Vector3 color = new Vector3(0, 1, 0);
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (facing == ForgeDirection.UNKNOWN)
		{
			Vector3 thisVec = new Vector3(this);
			
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				Vector3 sideVec = thisVec.clone().translate(new Vector3(dir));
				TileEntity tile = sideVec.getTileEntity(this.worldObj);
				
				if (tile != null && tile instanceof TileEntityUniversalElectrical)
				{
					this.setFacing(dir);
					break;
				}
			}
		}
		
		if (this.target != null && this.modeReceive == ReceiverMode.EXTRACT && this.facing != ForgeDirection.UNKNOWN)
		{
			TileEntity tile = this.getAttachedTile();
			
			if (tile instanceof TileEntityUniversalElectrical)
			{
				TileEntityUniversalElectrical electricalTile = (TileEntityUniversalElectrical) tile;
				EnergySourceAdjacent source = new EnergySourceAdjacent(this.facing.getOpposite());
				int toSend = electricalTile.storage.getMaxExtract();
				electricalTile.extractEnergyGC(source, this.target.receiveEnergyGC(new EnergySourceWireless(Lists.newArrayList((ILaserNode)this)), toSend, false), false);
			}
		}
		
		if (this.modeReceive == ReceiverMode.RECEIVE && this.storage.getEnergyStoredGC() > 0)
		{
			TileEntity tile = this.getAttachedTile();
			
			if (tile instanceof TileEntityUniversalElectrical)
			{
				TileEntityUniversalElectrical electricalTile = (TileEntityUniversalElectrical) tile;
				EnergySourceAdjacent source = new EnergySourceAdjacent(this.facing.getOpposite());
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
		return headVec;
	}

	@Override
	public Vector3 getOutputPoint(boolean offset) 
	{
		Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5);
		return headVec;
	}

	@Override
	public TileEntity getTile()
	{
		return this;
	}
	
	public TileEntity getAttachedTile()
	{
		if (facing == ForgeDirection.UNKNOWN)
		{
			return null;
		}
		
		TileEntity tile = new Vector3(this).translate(new Vector3(facing)).getTileEntity(this.worldObj);
		
		if (tile == null || tile.isInvalid())
		{
			this.setFacing(ForgeDirection.UNKNOWN);
		}

		if (tile instanceof EnergyStorageTile)
		{
			this.storage.setCapacity(((EnergyStorageTile) tile).storage.getCapacityGC());
			this.storage.setMaxReceive(((EnergyStorageTile) tile).storage.getMaxReceive());
			this.storage.setMaxExtract(((EnergyStorageTile) tile).storage.getMaxExtract());
		}
		
		return tile;
	}

	@Override
	public int receiveEnergyGC(EnergySource from, int amount, boolean simulate) 
	{
		if (this.modeReceive != ReceiverMode.RECEIVE)
		{
			return 0;
		}
		
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN)
		{
			return 0;
		}
		
		int received = this.storage.receiveEnergyGC(amount, simulate);
		
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
	public int extractEnergyGC(EnergySource from, int amount, boolean simulate) 
	{
		if (this.modeReceive != ReceiverMode.EXTRACT)
		{
			return 0;
		}
		
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN)
		{
			return 0;
		}
		
		int extracted = this.storage.extractEnergyGC(amount, simulate);
		
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
	public int getEnergyStoredGC(EnergySource from) 
	{
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN)
		{
			return 0;
		}
		
		return this.storage.getEnergyStoredGC();
	}

	@Override
	public int getMaxEnergyStoredGC(EnergySource from) 
	{
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN)
		{
			return 0;
		}
		
		return this.storage.getCapacityGC();
	}

	@Override
	public boolean nodeAvailable(EnergySource from)
	{
		TileEntity tile = this.getAttachedTile();
		
		if (this.facing == ForgeDirection.UNKNOWN)
		{
			return false;
		}
		
		return true;
	}
	
	private void setFacing(ForgeDirection newDirection)
	{
		if (newDirection != this.facing)
		{
			if (newDirection == ForgeDirection.UNKNOWN)
			{
				this.modeReceive = ReceiverMode.UNDEFINED;
			}
			else
			{
				TileEntity tile = new Vector3(this).translate(new Vector3(newDirection)).getTileEntity(this.worldObj);
				
				if (tile == null)
				{
					this.modeReceive = ReceiverMode.UNDEFINED;
				}
				else if (tile instanceof EnergyStorageTile)
				{
					ReceiverMode mode = ((EnergyStorageTile) tile).getModeFromDirection(newDirection.getOpposite());
					
					if (mode != null)
					{
						this.modeReceive = mode;
					}
					else
					{
						this.modeReceive = ReceiverMode.UNDEFINED;
					}
				}
			}
		}
		
		this.facing = newDirection;
	}

	@Override
	public boolean canConnectTo(ILaserNode laserNode) 
	{
		return this.modeReceive != ReceiverMode.UNDEFINED && this.color.equals(laserNode.getColor());
	}

	@Override
	public Vector3 getColor() 
	{
		return new Vector3(0, 1, 0);
	}

	@Override
	public ILaserNode getTarget() 
	{
		if (this.modeReceive == ReceiverMode.EXTRACT)
		{
			return super.getTarget();
		}
		
		return null;
	}
}
