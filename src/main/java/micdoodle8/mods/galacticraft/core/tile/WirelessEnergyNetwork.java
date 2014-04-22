package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WirelessEnergyNetwork 
{
	public List<TileEntityBeamReceiver> receivers = new ArrayList<TileEntityBeamReceiver>();
	public List<TileEntityBeamReflector> reflectors = new ArrayList<TileEntityBeamReflector>();
	
	public boolean isDirty = false;
	public final World worldObj;
	public final Vector3 color;
	
	public WirelessEnergyNetwork(World worldObj)
	{
		this.worldObj = worldObj;
		this.color = new Vector3(0, 1, 0);
	}
	
	public void recalculate()
	{
		this.receivers.clear();

		Iterator<TileEntityBeamReflector> it = this.reflectors.iterator();

		while (it.hasNext())
		{
			TileEntityBeamReflector reflector = it.next();
			List<ILaserNode> laserNodes = new ArrayList<ILaserNode>();
			List<Chunk> nearbyChunks = new ArrayList<Chunk>();
			
			int chunkXMin = (reflector.xCoord - 15) >> 4;
			int chunkZMin = (reflector.zCoord - 15) >> 4;
			int chunkXMax = (reflector.xCoord + 15) >> 4;
			int chunkZMax = (reflector.zCoord + 15) >> 4;
			
			for (int cX = chunkXMin; cX <= chunkXMax; cX++)
			{
				for (int cZ = chunkZMin; cZ <= chunkZMax; cZ++)
				{
					if (worldObj.getChunkProvider().chunkExists(cX, cZ))
					{
						Chunk chunk = worldObj.getChunkFromChunkCoords(cX, cZ);
						
						for (int x = 0; x < 16; x++)
						{
							for (int z = 0; z < 16; z++)
							{
								for (int y = 0; y < 16; y++)
								{
									TileEntity tile = chunk.func_150806_e(x, y, z);

									if (tile instanceof ILaserNode)
									{
										BlockVec3 deltaPos = new BlockVec3(reflector).subtract(new BlockVec3(tile));
										
										if (deltaPos.x < 16 && deltaPos.y < 16 && deltaPos.z < 16)
										{
											ILaserNode laserNode = (ILaserNode) tile;
											
											if (this.color.equals(laserNode.getColor()))
											{
												laserNodes.add(laserNode);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			for (ILaserNode laserNode : laserNodes)
			{
				if (laserNode instanceof TileEntityBeamReceiver)
				{
					this.receivers.add((TileEntityBeamReceiver) laserNode);
				}
				else if (laserNode instanceof TileEntityBeamReflector)
				{
					this.reflectors.add((TileEntityBeamReflector) laserNode);
				}
			}
		}
	}
}
