package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

class AirLockProtocol
{
	private ArrayList<TileEntityAirLock> adjacentAirLocks;
	private final World worldObj;
	private final TileEntity head;
	private final int maxLoops;

	private int airLocksVerticalMin = 0;
	private int airLocksVerticalMax = 0;
	private int airLocksHorizontalMin = 0;
	private int airLocksHorizontalMax = 0;
	private boolean horizontal;

	public int minX = 6000000;
	public int maxX = -6000000;
	public int minY = 6000000;
	public int maxY = -6000000;
	public int minZ = 6000000;
	public int maxZ = -6000000;

	public AirLockProtocol(TileEntity head)
	{
		this.adjacentAirLocks = new ArrayList<TileEntityAirLock>();
		this.worldObj = head.getWorldObj();
		this.head = head;
		this.maxLoops = 26;
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
						if (!(x == 0 && y == 0 && z == 0))
						{
							if (tile2.xCoord + x == this.head.xCoord || tile2.zCoord + z == this.head.zCoord)
							{
								final TileEntity tile = this.worldObj.getTileEntity(tile2.xCoord + x, tile2.yCoord + y, tile2.zCoord + z);
								if (tile instanceof TileEntityAirLock && !this.adjacentAirLocks.contains(tile))
								{
									this.adjacentAirLocks.add((TileEntityAirLock) tile);
									this.loopThrough(tile, loops - 1);
								}
							}
						}
					}
				}
			}
		}
	}

	public void loopThroughHorizontal(TileEntity tile2, int loops)
	{
		if (loops > 0)
		{
			for (int y = -1; y <= 1; y++)
			{
				for (int x = -1; x <= 1; x++)
				{
					for (int z = -1; z <= 1; z++)
					{
						if (!(x == 0 && y == 0 && z == 0))
						{
							if (tile2.yCoord + y == this.head.yCoord)
							{
								final TileEntity tile = this.worldObj.getTileEntity(tile2.xCoord + x, tile2.yCoord + y, tile2.zCoord + z);
								if (tile instanceof TileEntityAirLock && !this.adjacentAirLocks.contains(tile))
								{
									this.adjacentAirLocks.add((TileEntityAirLock) tile);
									this.loopThroughHorizontal(tile, loops - 1);
								}
							}
						}
					}
				}
			}
		}
	}

	@Deprecated
	public ArrayList<TileEntityAirLock> calculate()
	{
		return this.calculate(false);
	}

	public ArrayList<TileEntityAirLock> calculate(boolean horizontal)
	{
		if (this.worldObj.isRemote)
		{
			return null;
		}

		this.adjacentAirLocks = new ArrayList<TileEntityAirLock>();

		this.horizontal = horizontal;
		
		if (horizontal)
			this.loopThroughHorizontal(this.head, this.maxLoops);
		else 
			this.loopThrough(this.head, this.maxLoops);
			
		for (final TileEntityAirLock airLock : this.adjacentAirLocks)
		{
			if (airLock.xCoord < this.minX)
			{
				this.minX = airLock.xCoord;
			}

			if (airLock.xCoord > this.maxX)
			{
				this.maxX = airLock.xCoord;
			}

			if (airLock.yCoord < this.minY)
			{
				this.minY = airLock.yCoord;
			}

			if (airLock.yCoord > this.maxY)
			{
				this.maxY = airLock.yCoord;
			}

			if (airLock.zCoord < this.minZ)
			{
				this.minZ = airLock.zCoord;
			}

			if (airLock.zCoord > this.maxZ)
			{
				this.maxZ = airLock.zCoord;
			}
		}

		final int count = this.maxX - this.minX + this.maxZ - this.minZ + this.maxY - this.minY;

		if (count > 24 || this.maxX - this.minX <= 1 && this.maxZ - this.minZ <= 1 || !horizontal && this.maxY - this.minY <=1)
		{
			return null;
		}
		
		if (horizontal && (this.maxX - this.minX <= 1 || this.maxZ - this.minZ <= 1))
		{
			return null;
		}

		this.airLocksVerticalMin = 0;
		this.airLocksVerticalMax = 0;
		this.airLocksHorizontalMin = 0;
		this.airLocksHorizontalMax = 0;

		for (int y = this.minY; y <= this.maxY; y++)
		{
			final TileEntity tileAt = this.worldObj.getTileEntity(this.minX, y, this.minZ);

			if (tileAt instanceof TileEntityAirLock)
			{
				this.airLocksVerticalMin++;
			}
		}

		for (int y = this.minY; y <= this.maxY; y++)
		{
			final TileEntity tileAt = this.worldObj.getTileEntity(this.maxX, y, this.maxZ);

			if (tileAt instanceof TileEntityAirLock)
			{
				this.airLocksVerticalMax++;
			}
		}

		if (this.minX != this.maxX)
		{
			for (int x = this.minX; x <= this.maxX; x++)
			{
				final TileEntity tileAt = this.worldObj.getTileEntity(x, this.maxY, this.maxZ);

				if (tileAt instanceof TileEntityAirLock)
				{
					this.airLocksHorizontalMax++;
				}
			}

			for (int x = this.minX; x <= this.maxX; x++)
			{
				final TileEntity tileAt = this.worldObj.getTileEntity(x, this.minY, this.maxZ);

				if (tileAt instanceof TileEntityAirLock)
				{
					this.airLocksHorizontalMin++;
				}
			}
		}
		else if (this.minZ != this.maxZ)
		{
			for (int z = this.minZ; z <= this.maxZ; z++)
			{
				final TileEntity tileAt = this.worldObj.getTileEntity(this.maxX, this.maxY, z);

				if (tileAt instanceof TileEntityAirLock)
				{
					this.airLocksHorizontalMax++;
				}
			}

			for (int z = this.minZ; z <= this.maxZ; z++)
			{
				final TileEntity tileAt = this.worldObj.getTileEntity(this.maxX, this.minY, z);

				if (tileAt instanceof TileEntityAirLock)
				{
					this.airLocksHorizontalMin++;
				}
			}
		}

		if (this.airLocksHorizontalMax == 0 || this.airLocksHorizontalMin == 0 || (!this.horizontal && (this.airLocksVerticalMin == 0 || this.airLocksVerticalMax == 0)) || this.airLocksHorizontalMax != this.airLocksHorizontalMin || this.airLocksVerticalMax != this.airLocksVerticalMin)
		{
			return null;
		}

		return this.adjacentAirLocks;
	}
}
