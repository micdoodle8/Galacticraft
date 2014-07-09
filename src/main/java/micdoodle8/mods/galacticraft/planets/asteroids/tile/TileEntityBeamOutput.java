package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.LinkedList;

public abstract class TileEntityBeamOutput extends TileEntityAdvanced implements ILaserNode
{
	public LinkedList<ILaserNode> nodeList = new LinkedList<ILaserNode>();
	@NetworkedField(targetSide = Side.CLIENT)
	public BlockVec3 targetVec = BlockVec3.INVALID_VECTOR;
	public float pitch;
	public float yaw;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && this.targetVec.equals(BlockVec3.INVALID_VECTOR))
		{
			this.initiateReflector();
		}

		if (this.worldObj.isRemote)
		{
			this.updateOrientation();
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		this.invalidateReflector();
	}

	@Override
	public void validate()
	{
		super.validate();
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidateReflector();
	}

	public void invalidateReflector()
	{
		for (ILaserNode node : this.nodeList)
		{
			node.removeNode(this);
		}

		this.nodeList.clear();
	}

	public void initiateReflector()
	{
		this.nodeList.clear();

		int chunkXMin = this.xCoord - 15 >> 4;
		int chunkZMin = this.zCoord - 15 >> 4;
		int chunkXMax = this.xCoord + 15 >> 4;
		int chunkZMax = this.zCoord + 15 >> 4;

		for (int cX = chunkXMin; cX <= chunkXMax; cX++)
		{
			for (int cZ = chunkZMin; cZ <= chunkZMax; cZ++)
			{
				if (this.worldObj.getChunkProvider().chunkExists(cX, cZ))
				{
					Chunk chunk = this.worldObj.getChunkFromChunkCoords(cX, cZ);

					for (Object obj : chunk.chunkTileEntityMap.values())
					{
						if (obj != this && obj instanceof ILaserNode)
						{
							BlockVec3 deltaPos = new BlockVec3(this).subtract(new BlockVec3(((ILaserNode) obj).getTile()));

							if (deltaPos.x < 16 && deltaPos.y < 16 && deltaPos.z < 16)
							{
								ILaserNode laserNode = (ILaserNode) obj;

								if (this.canConnectTo(laserNode) && laserNode.canConnectTo(this))
								{
									this.addNode(laserNode);
									laserNode.addNode(this);
								}
							}
						}
					}
				}
			}
		}

		this.setTarget(this.nodeList.peekFirst());
	}

	@Override
	public void addNode(ILaserNode node)
	{
		int index = -1;

		for (int i = 0; i < this.nodeList.size(); i++)
		{
			if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(node.getTile())))
			{
				index = i;
				break;
			}
		}

		if (index != -1)
		{
			this.nodeList.set(index, node);
			return;
		}

		if (this.nodeList.isEmpty())
		{
			this.nodeList.add(node);
		}
		else
		{
			int nodeCompare = this.nodeList.get(0).compareTo(node, new BlockVec3(this));

			if (nodeCompare <= 0)
			{
				this.nodeList.addFirst(node);
				return;
			}

			nodeCompare = this.nodeList.get(this.nodeList.size() - 1).compareTo(node, new BlockVec3(this));

			if (nodeCompare >= 0)
			{
				this.nodeList.addLast(node);
				return;
			}

			index = 1;
			nodeCompare = 0;

			while (index < this.nodeList.size() && (nodeCompare = this.nodeList.get(index).compareTo(node, new BlockVec3(this))) > 0)
			{
				index++;
			}

			this.nodeList.add(index, node);
		}
	}

	@Override
	public void removeNode(ILaserNode node)
	{
		int index = -1;

		for (int i = 0; i < this.nodeList.size(); i++)
		{
			if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(node.getTile())))
			{
				index = i;
				break;
			}
		}

		if (new BlockVec3(node.getTile()).equals(this.targetVec))
		{
			if (index == 0)
			{
				if (this.nodeList.size() > 1)
				{
					this.setTarget(this.nodeList.get(index + 1));
				}
				else
				{
					this.setTarget(null);
				}
			}
			else
			{
				this.setTarget(this.nodeList.get(index - 1));
			}
		}

		if (index != -1)
		{
			this.nodeList.remove(index);
		}
	}

	public void updateOrientation()
	{
		if (this.getTarget() != null)
		{
			Vector3 direction = Vector3.subtract(this.getOutputPoint(false), this.getTarget().getInputPoint()).normalize();
			this.pitch = (float) -Vector3.getAngle(new Vector3(-direction.x, -direction.y, -direction.z), new Vector3(0, 1, 0)) * (float) (180.0F / Math.PI) + 90;
			this.yaw = (float) -(Math.atan2(direction.z, direction.x) * (float) (180.0F / Math.PI)) + 90;
		}
	}

	@Override
	public TileEntity getTile()
	{
		return this;
	}

	@Override
	public int compareTo(ILaserNode otherNode, BlockVec3 origin)
	{
		int thisDistance = new BlockVec3(this).subtract(origin).getMagnitudeSquared();
		int otherDistance = new BlockVec3(otherNode.getTile()).subtract(origin).getMagnitudeSquared();

		if (thisDistance < otherDistance)
		{
			return 1;
		}
		else if (thisDistance > otherDistance)
		{
			return -1;
		}

		return 0;
	}

	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (this.nodeList.size() > 1)
		{
			int index = -1;

			for (int i = 0; i < this.nodeList.size(); i++)
			{
				if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(this.getTarget().getTile())))
				{
					index = i;
					break;
				}
			}

			if (index == -1)
			{
				// This shouldn't happen, but just in case...
				this.initiateReflector();
			}
			else
			{
				index++;
				index %= this.nodeList.size();
				this.setTarget(this.nodeList.get(index));
				return true;
			}
		}

		return false;
	}

	@Override
	public ILaserNode getTarget()
	{
		if (!this.targetVec.equals(BlockVec3.INVALID_VECTOR))
		{
			TileEntity tileAtTarget = this.worldObj.getTileEntity(this.targetVec.x, this.targetVec.y, this.targetVec.z);

			if (tileAtTarget != null && tileAtTarget instanceof ILaserNode)
			{
				return (ILaserNode) tileAtTarget;
			}

			return null;
		}

		return null;
	}

	public void setTarget(ILaserNode target)
	{
		if (target != null)
		{
			this.targetVec = new BlockVec3(target.getTile());
		}
		else
		{
			this.targetVec = BlockVec3.INVALID_VECTOR;
		}
	}
}
