package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
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
		this.maxLoops = 40;
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
						final TileEntity tile = this.worldObj.getTileEntity(tile2.xCoord + x, tile2.yCoord + y, tile2.zCoord + z);
						new Vector3(this.head).translate(new Vector3(x, y, z));

						if (!(x == 0 && y == 0 && z == 0))
						{
							if (tile != null && tile instanceof TileEntityAirLock && !this.adjacentAirLocks.contains(tile))
							{
								if (this.horizontal && tile.yCoord == this.head.yCoord)
								{
									this.adjacentAirLocks.add((TileEntityAirLock) tile);
									this.loopThrough(tile, loops - 1);
								}
								else if (!this.horizontal && tile.xCoord == this.head.xCoord || tile.zCoord == this.head.zCoord)
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
		this.loopThrough(this.head, this.maxLoops);

		for (final TileEntityAirLock airLock : this.adjacentAirLocks)
		{
			final Vector3 vecAt = new Vector3(airLock);

			if (vecAt.intX() < this.minX)
			{
				this.minX = vecAt.intX();
			}

			if (vecAt.intX() > this.maxX)
			{
				this.maxX = vecAt.intX();
			}

			if (vecAt.intY() < this.minY)
			{
				this.minY = vecAt.intY();
			}

			if (vecAt.intY() > this.maxY)
			{
				this.maxY = vecAt.intY();
			}

			if (vecAt.intZ() < this.minZ)
			{
				this.minZ = vecAt.intZ();
			}

			if (vecAt.intZ() > this.maxZ)
			{
				this.maxZ = vecAt.intZ();
			}
		}

		final int count = this.maxX - this.minX + this.maxZ - this.minZ + this.maxY - this.minY;

		if (count > 16 || this.maxX - this.minX == 0 && this.maxZ - this.minZ == 0)
		{
			return null;
		}

		this.airLocksVerticalMin = 0;
		this.airLocksVerticalMax = 0;
		this.airLocksHorizontalMin = 0;
		this.airLocksHorizontalMax = 0;

		for (int y = this.minY; y <= this.maxY; y++)
		{
			final TileEntity tileAt = new Vector3(this.minX, y, this.minZ).getTileEntity(this.worldObj);

			if (tileAt instanceof TileEntityAirLock)
			{
				this.airLocksVerticalMin++;
			}
		}

		for (int y = this.minY; y <= this.maxY; y++)
		{
			final TileEntity tileAt = new Vector3(this.maxX, y, this.maxZ).getTileEntity(this.worldObj);

			if (tileAt instanceof TileEntityAirLock)
			{
				this.airLocksVerticalMax++;
			}
		}

		if (this.minX != this.maxX)
		{
			for (int x = this.minX; x <= this.maxX; x++)
			{
				final TileEntity tileAt = new Vector3(x, this.maxY, this.maxZ).getTileEntity(this.worldObj);

				if (tileAt instanceof TileEntityAirLock)
				{
					this.airLocksHorizontalMax++;
				}
			}

			for (int x = this.minX; x <= this.maxX; x++)
			{
				final TileEntity tileAt = new Vector3(x, this.minY, this.maxZ).getTileEntity(this.worldObj);

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
				final TileEntity tileAt = new Vector3(this.maxX, this.maxY, z).getTileEntity(this.worldObj);

				if (tileAt instanceof TileEntityAirLock)
				{
					this.airLocksHorizontalMax++;
				}
			}

			for (int z = this.minZ; z <= this.maxZ; z++)
			{
				final TileEntity tileAt = new Vector3(this.maxX, this.minY, z).getTileEntity(this.worldObj);

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
