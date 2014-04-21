package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.core.grid.IReflectorNode;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TileEntityBeamReflector extends TileEntityAdvanced implements IReflectorNode
{
	public IReflectorNode target;
	public float pitch;
	public float yaw;
	
	public TileEntityBeamReflector()
	{
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (/*this.target == null && */FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			boolean foundThis = false;
			for (int i = 0; i < worldObj.loadedTileEntityList.size(); i++)
			{
				TileEntity tile = (TileEntity) worldObj.loadedTileEntityList.get(i);
				
				if (tile instanceof IReflectorNode)
				{
					if (tile == this)
					{
						foundThis = true;
						continue;
					}
					else if (foundThis)
					{
						this.setTarget((IReflectorNode) tile);
						break;
					}
				}
			}
			
//			if (this.target == null)
//			{
//				for (int i = 0; i < worldObj.loadedTileEntityList.size(); i++)
//				{
//					TileEntity tile = (TileEntity) worldObj.loadedTileEntityList.get(i);
//					
//					if (tile instanceof IReflectorNode)
//					{
//						this.setTarget((IReflectorNode) tile);
//						break;
//					}
//				}
//			}
		}
	}
	
	public void setTarget(IReflectorNode tile)
	{
//		if (tile != this.target)
		{
			Vector3 direction = Vector3.subtract(this.getOutputPoint(false), tile.getInputPoint()).normalize();
			this.pitch = (float) -Vector3.getAngle(new Vector3(-direction.x, -direction.y, -direction.z), new Vector3(0, 1, 0)) * (float)(180.0F / Math.PI) + 90;
			this.yaw = (float) -(Math.atan2(direction.z, direction.x) * (float)(180.0F / Math.PI)) + 90;
		}
		
		this.target = tile;
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
		float distance = 0.15F;
		Vector3 deviation = new Vector3(Math.sin(Math.toRadians(this.yaw - 180)) * distance, 0, Math.cos(Math.toRadians(this.yaw - 180)) * distance);
		Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 1.13228 / 2.0, this.zCoord + 0.5);
		headVec.translate(deviation.clone().invert());
		return headVec;
	}

	@Override
	public Vector3 getOutputPoint(boolean offset) 
	{
		float distance = 0.15F;
		Vector3 deviation = new Vector3(Math.sin(Math.toRadians(this.yaw)) * distance, 0, Math.cos(Math.toRadians(this.yaw)) * distance);
		Vector3 headVec = new Vector3(this.xCoord + 0.5, this.yCoord + 1.13228 / 2.0, this.zCoord + 0.5);
		if (offset)
		{
			headVec.translate(deviation.clone().invert());
		}
		return headVec;
	}
}
