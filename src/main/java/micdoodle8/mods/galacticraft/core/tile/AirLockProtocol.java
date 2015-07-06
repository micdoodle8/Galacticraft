package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
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
        this.worldObj = head.getWorld();
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
                            if (tile2.getPos().getX() + x == this.head.getPos().getX() || tile2.getPos().getZ() + z == this.head.getPos().getZ())
                            {
                                final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(tile2.getPos().getX() + x, tile2.getPos().getY() + y, tile2.getPos().getZ() + z));
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
                            if (tile2.getPos().getY() + y == this.head.getPos().getY())
                            {
                                final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(tile2.getPos().getX() + x, tile2.getPos().getY() + y, tile2.getPos().getZ() + z));
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
        {
            this.loopThroughHorizontal(this.head, this.maxLoops);
        }
        else
        {
            this.loopThrough(this.head, this.maxLoops);
        }

        for (final TileEntityAirLock airLock : this.adjacentAirLocks)
        {
            if (airLock.getPos().getX() < this.minX)
            {
                this.minX = airLock.getPos().getX();
            }

            if (airLock.getPos().getX() > this.maxX)
            {
                this.maxX = airLock.getPos().getX();
            }

            if (airLock.getPos().getY() < this.minY)
            {
                this.minY = airLock.getPos().getY();
            }

            if (airLock.getPos().getY() > this.maxY)
            {
                this.maxY = airLock.getPos().getY();
            }

            if (airLock.getPos().getZ() < this.minZ)
            {
                this.minZ = airLock.getPos().getZ();
            }

            if (airLock.getPos().getZ() > this.maxZ)
            {
                this.maxZ = airLock.getPos().getZ();
            }
        }

        final int count = this.maxX - this.minX + this.maxZ - this.minZ + this.maxY - this.minY;

        if (count > 24 || this.maxX - this.minX <= 1 && this.maxZ - this.minZ <= 1 || !horizontal && this.maxY - this.minY <= 1)
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
            final TileEntity tileAt = this.worldObj.getTileEntity(new BlockPos(this.minX, y, this.minZ));

            if (tileAt instanceof TileEntityAirLock)
            {
                this.airLocksVerticalMin++;
            }
        }

        for (int y = this.minY; y <= this.maxY; y++)
        {
            final TileEntity tileAt = this.worldObj.getTileEntity(new BlockPos(this.maxX, y, this.maxZ));

            if (tileAt instanceof TileEntityAirLock)
            {
                this.airLocksVerticalMax++;
            }
        }

        if (this.minX != this.maxX)
        {
            for (int x = this.minX; x <= this.maxX; x++)
            {
                final TileEntity tileAt = this.worldObj.getTileEntity(new BlockPos(x, this.maxY, this.maxZ));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksHorizontalMax++;
                }
            }

            for (int x = this.minX; x <= this.maxX; x++)
            {
                final TileEntity tileAt = this.worldObj.getTileEntity(new BlockPos(x, this.minY, this.maxZ));

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
                final TileEntity tileAt = this.worldObj.getTileEntity(new BlockPos(this.maxX, this.maxY, z));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksHorizontalMax++;
                }
            }

            for (int z = this.minZ; z <= this.maxZ; z++)
            {
                final TileEntity tileAt = this.worldObj.getTileEntity(new BlockPos(this.maxX, this.minY, z));

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
