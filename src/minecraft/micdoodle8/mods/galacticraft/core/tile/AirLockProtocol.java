package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class AirLockProtocol 
{
	ArrayList<GCCoreTileEntityAirLock> adjacentAirLocks;
	private final World worldObj;
	private final TileEntity head;
	private final int maxLoops;
	
	public int airLocksVerticalMin = 0;
	public int airLocksVerticalMax = 0;
	public int airLocksHorizontalMin = 0;
	public int airLocksHorizontalMax = 0;
	
	public int minX = 6000000;
	public int maxX = -6000000;
	public int minY = 6000000;
	public int maxY = -6000000;
	public int minZ = 6000000;
	public int maxZ = -6000000;

	public AirLockProtocol(TileEntity head, int maxLoops)
	{
		adjacentAirLocks = new ArrayList<GCCoreTileEntityAirLock>();
		this.worldObj = head.worldObj;
		this.head = head;
		this.maxLoops = maxLoops;
	}
	
	public void loopThrough(TileEntity tile2, int loops)
	{
		if (loops > 0)
		{
			for (int y = -1; y <= 1; y++)
			{
				for (int x = -1; x <= 1; x++)
				{
					for (int z = -1; z <= 1; z++)
					{
						TileEntity tile = this.worldObj.getBlockTileEntity(tile2.xCoord + x, tile2.yCoord + y, tile2.zCoord + z);
						Vector3 vec = new Vector3(this.head).add(new Vector3(x, y, z));
						
						if (!(x == 0 && y == 0 && z == 0))
						{
							if (tile != null && tile instanceof GCCoreTileEntityAirLock && !this.adjacentAirLocks.contains((GCCoreTileEntityAirLock) tile))
							{
								adjacentAirLocks.add((GCCoreTileEntityAirLock) tile);
								this.loopThrough(tile, loops - 1);
							}
						}
					}
				}
			}
		}
	}

	public ArrayList<GCCoreTileEntityAirLock> calculate()
	{
		if (this.worldObj.isRemote)
		{
			return null;
		}
		
		adjacentAirLocks = new ArrayList<GCCoreTileEntityAirLock>();
		
		this.loopThrough(this.head, this.maxLoops);
		
		for (GCCoreTileEntityAirLock airLock : this.adjacentAirLocks)
		{
			Vector3 vecAt = new Vector3(airLock);
			
			if (vecAt.intX() < minX)
			{
				minX = vecAt.intX();
			}
			
			if (vecAt.intX() > maxX)
			{
				maxX = vecAt.intX();
			}
			
			if (vecAt.intY() < minY)
			{
				minY = vecAt.intY();
			}
			
			if (vecAt.intY() > maxY)
			{
				maxY = vecAt.intY();
			}
			
			if (vecAt.intZ() < minZ)
			{
				minZ = vecAt.intZ();
			}
			
			if (vecAt.intZ() > maxZ)
			{
				maxZ = vecAt.intZ();
			}
		}
		
		int count = (maxX - minX) + (maxZ - minZ) + (maxY - minY);
		
		if (count > 16 || ((maxX - minX) == 0 && (maxZ - minZ) == 0))
		{
			return null;
		}
		
		airLocksVerticalMin = 0;
		airLocksVerticalMax = 0;
		airLocksHorizontalMin = 0;
		airLocksHorizontalMax = 0;
		
		for (int y = minY; y <= maxY; y++)
		{
			TileEntity tileAt = new Vector3(minX, y, minZ).getTileEntity(this.worldObj);
			
			if (tileAt instanceof GCCoreTileEntityAirLock)
			{
				airLocksVerticalMin++;
			}
		}
		
		for (int y = minY; y <= maxY; y++)
		{
			TileEntity tileAt = new Vector3(maxX, y, maxZ).getTileEntity(this.worldObj);
			
			if (tileAt instanceof GCCoreTileEntityAirLock)
			{
				airLocksVerticalMax++;
			}
		}
		
		if (minX != maxX)
		{
			for (int x = minX; x <= maxX; x++)
			{
				TileEntity tileAt = new Vector3(x, maxY, maxZ).getTileEntity(this.worldObj);
				
				if (tileAt instanceof GCCoreTileEntityAirLock)
				{
					airLocksHorizontalMax++;
				}
			}

			for (int x = minX; x <= maxX; x++)
			{
				TileEntity tileAt = new Vector3(x, minY, maxZ).getTileEntity(this.worldObj);
				
				if (tileAt instanceof GCCoreTileEntityAirLock)
				{
					airLocksHorizontalMin++;
				}
			}
		}
		else if (minZ != maxZ)
		{
			for (int z = minZ; z <= maxZ; z++)
			{
				TileEntity tileAt = new Vector3(maxX, maxY, z).getTileEntity(this.worldObj);
				
				if (tileAt instanceof GCCoreTileEntityAirLock)
				{
					airLocksHorizontalMax++;
				}
			}

			for (int z = minZ; z <= maxZ; z++)
			{
				TileEntity tileAt = new Vector3(maxX, minY, z).getTileEntity(this.worldObj);
				
				if (tileAt instanceof GCCoreTileEntityAirLock)
				{
					airLocksHorizontalMin++;
				}
			}
		}
		
		if (airLocksHorizontalMax == 0 || airLocksHorizontalMin == 0 || airLocksVerticalMin == 0 || airLocksVerticalMax == 0 || (airLocksHorizontalMax != airLocksHorizontalMin) || (airLocksVerticalMax != airLocksVerticalMin))
		{
			return null;
		}
			
		return this.adjacentAirLocks;
	}
}
