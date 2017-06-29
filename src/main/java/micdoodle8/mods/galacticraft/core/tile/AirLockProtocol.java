package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;

class AirLockProtocol
{
    private ArrayList<BlockPos> adjacentAirLocks;
    private HashSet<BlockPos> checked;
    private final World world;
    private final TileEntity head;
    private final int maxLoops;

    private int airLocksDimension1Min = 0;
    private int airLocksDimension1Max = 0;
    private int airLocksDimension2Min = 0;
    private int airLocksDimension2Max = 0;

    public int minX = 6000000;
    public int maxX = -6000000;
    public int minY = 6000000;
    public int maxY = -6000000;
    public int minZ = 6000000;
    public int maxZ = -6000000;

    public AirLockProtocol(TileEntity head)
    {
        this.adjacentAirLocks = new ArrayList<BlockPos>();
        this.checked = new HashSet<BlockPos>();
        this.world = head.getWorld();
        this.head = head;
        this.maxLoops = 26;
    }

    private void loopThrough(BlockPos pos, int loops)
    {
        int xAligned = this.head.getPos().getX();
        int zAligned = this.head.getPos().getZ();
        for (int x = -1; x <= 1; x++)
        {
            int xTest = pos.getX() + x;
            for (int z = -1; z <= 1; z++)
            {
                int zTest = pos.getZ() + z;

                if ((xTest == xAligned || zTest == zAligned))
                {
                    for (int y = -1; y <= 1; y++)
                    {
                        if (!(x == 0 && y == 0 && z == 0))
                        {
                            final BlockPos testPos = new BlockPos(xTest, pos.getY() + y, zTest);
                            if (!this.checked.contains(testPos))
                            {
                                this.checked.add(testPos);
                                if (this.world.getTileEntity(testPos) instanceof TileEntityAirLock)
                                {
                                    this.adjacentAirLocks.add(testPos);
                                    if (loops > 1)
                                    {
                                        this.loopThrough(testPos, loops - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void loopThroughHorizontal(BlockPos pos, int loops)
    {
        int yTest = pos.getY();
        for (int x = -1; x <= 1; x++)
        {
            int xTest = pos.getX() + x;
            for (int z = -1; z <= 1; z++)
            {
                if (!(x == 0 && z == 0))
                {
                    final BlockPos testPos = new BlockPos(xTest, yTest, pos.getZ() + z);
                    if (!this.checked.contains(testPos))
                    {
                        this.checked.add(testPos);
                        if (this.world.getTileEntity(testPos) instanceof TileEntityAirLock)
                        {
                            this.adjacentAirLocks.add(testPos);
                            if (loops > 1)
                            {
                                this.loopThroughHorizontal(testPos, loops - 1);
                            }
                        }
                    }
                }
            }
        }
    }

    public int calculate(boolean horizontal)
    {
        if (this.world.isRemote)
        {
            return -1;
        }

        this.minX = 6000000;
        this.maxX = -6000000;
        this.minY = 6000000;
        this.maxY = -6000000;
        this.minZ = 6000000;
        this.maxZ = -6000000;

        this.adjacentAirLocks = new ArrayList<BlockPos>();
        this.checked.clear();
        final BlockPos headPos = this.head.getPos();
        this.checked.add(headPos);
        this.adjacentAirLocks.add(headPos);

        if (horizontal)
        {
            this.loopThroughHorizontal(headPos, this.maxLoops);
        }
        else
        {
            this.loopThrough(headPos, this.maxLoops);
        }

        for (final BlockPos airLock : this.adjacentAirLocks)
        {
            if (airLock.getX() < this.minX)
            {
                this.minX = airLock.getX();
            }

            if (airLock.getX() > this.maxX)
            {
                this.maxX = airLock.getX();
            }

            if (airLock.getY() < this.minY)
            {
                this.minY = airLock.getY();
            }

            if (airLock.getY() > this.maxY)
            {
                this.maxY = airLock.getY();
            }

            if (airLock.getZ() < this.minZ)
            {
                this.minZ = airLock.getZ();
            }

            if (airLock.getZ() > this.maxZ)
            {
                this.maxZ = airLock.getZ();
            }
        }

        final int count = this.maxX - this.minX + this.maxZ - this.minZ + this.maxY - this.minY;

        if (count > 24 || this.maxX - this.minX <= 1 && this.maxZ - this.minZ <= 1 || !horizontal && this.maxY - this.minY <= 1)
        {
            return -1;
        }

        if (horizontal && (this.maxX - this.minX <= 1 || this.maxZ - this.minZ <= 1))
        {
            return -1;
        }

        this.airLocksDimension1Min = 0;
        this.airLocksDimension1Max = 0;
        this.airLocksDimension2Min = 0;
        this.airLocksDimension2Max = 0;

        if (horizontal)
        {
            this.checkDimensionsHorizontal();
        }
        else
        {
            this.checkDimensions();
        }

        if (this.airLocksDimension2Max == 0 || this.airLocksDimension2Min == 0 || (this.airLocksDimension1Min == 0 || this.airLocksDimension1Max == 0) || this.airLocksDimension2Max != this.airLocksDimension2Min || this.airLocksDimension1Max != this.airLocksDimension1Min)
        {
            return -1;
        }

        return this.adjacentAirLocks.size();
    }

    private void checkDimensions()
    {
        for (int y = this.minY; y <= this.maxY; y++)
        {
            final TileEntity tileAt = this.world.getTileEntity(new BlockPos(this.minX, y, this.minZ));

            if (tileAt instanceof TileEntityAirLock)
            {
                this.airLocksDimension1Min++;
            }
        }

        for (int y = this.minY; y <= this.maxY; y++)
        {
            final TileEntity tileAt = this.world.getTileEntity(new BlockPos(this.maxX, y, this.maxZ));

            if (tileAt instanceof TileEntityAirLock)
            {
                this.airLocksDimension1Max++;
            }
        }

        if (this.minX != this.maxX)
        {
            for (int x = this.minX; x <= this.maxX; x++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(x, this.maxY, this.maxZ));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension2Max++;
                }
            }

            for (int x = this.minX; x <= this.maxX; x++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(x, this.minY, this.maxZ));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension2Min++;
                }
            }
        }
        else if (this.minZ != this.maxZ)
        {
            for (int z = this.minZ; z <= this.maxZ; z++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(this.maxX, this.maxY, z));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension2Max++;
                }
            }

            for (int z = this.minZ; z <= this.maxZ; z++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(this.maxX, this.minY, z));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension2Min++;
                }
            }
        }
    }

    private void checkDimensionsHorizontal()
    {
        if (this.minX != this.maxX)
        {
            for (int x = this.minX; x <= this.maxX; x++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(x, this.minY, this.maxZ));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension1Max++;
                }
            }

            for (int x = this.minX; x <= this.maxX; x++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(x, this.minY, this.minZ));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension1Min++;
                }
            }
        }

        if (this.minZ != this.maxZ)
        {
            for (int z = this.minZ; z <= this.maxZ; z++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(this.maxX, this.minY, z));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension2Max++;
                }
            }

            for (int z = this.minZ; z <= this.maxZ; z++)
            {
                final TileEntity tileAt = this.world.getTileEntity(new BlockPos(this.minX, this.minY, z));

                if (tileAt instanceof TileEntityAirLock)
                {
                    this.airLocksDimension2Min++;
                }
            }
        }
    }
}
