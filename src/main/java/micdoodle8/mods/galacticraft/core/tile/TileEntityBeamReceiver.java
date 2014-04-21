package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.core.grid.IReflectorNode;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityBeamReceiver extends TileEntityAdvanced implements IReflectorNode
{
	public TileEntity attachedTile;
	public ForgeDirection facing = ForgeDirection.UNKNOWN;
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (attachedTile == null)
		{
			Vector3 thisVec = new Vector3(this);
			
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				Vector3 sideVec = thisVec.clone().translate(new Vector3(dir));
				TileEntity tile = sideVec.getTileEntity(this.worldObj);
				
				if (tile != null && tile instanceof TileEntityUniversalElectrical)
				{
					this.attachedTile = tile;
					this.facing = dir;
					break;
				}
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
}
