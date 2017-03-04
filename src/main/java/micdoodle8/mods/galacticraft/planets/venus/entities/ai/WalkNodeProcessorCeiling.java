package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class WalkNodeProcessorCeiling extends NodeProcessor
{
    @Override
    public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn)
    {
        super.initProcessor(iblockaccessIn, entityIn);
    }

    @Override
    public void postProcess()
    {
        super.postProcess();
    }

    @Override
    public PathPoint getPathPointTo(Entity entityIn)
    {
        int i = MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5D);

        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), i, MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }

    @Override
    public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target)
    {
        return this.openPoint(MathHelper.floor_double(x - (double)(entityIn.width / 2.0F)), MathHelper.floor_double(y), MathHelper.floor_double(target - (double)(entityIn.width / 2.0F)));
    }

    @Override
    public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int i = 0;
        int j = 0;

        if (this.getVerticalOffset(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord) == 1)
        {
            j = 1;
        }

        PathPoint pathpoint = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j);
        PathPoint pathpoint1 = this.getSafePoint(entityIn, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j);
        PathPoint pathpoint2 = this.getSafePoint(entityIn, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j);
        PathPoint pathpoint3 = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j);

        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint;
        }

        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint1;
        }

        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint3;
        }

        return i;
    }

    /**
     * Returns a point that the entity can safely move to
     */
    private PathPoint getSafePoint(Entity entityIn, int x, int y, int z, int offsetY)
    {
        PathPoint pathpoint = null;
        int i = this.getVerticalOffset(entityIn, x, y, z);

        if (i == 2)
        {
            return this.openPoint(x, y, z);
        }
        else
        {
            if (i == 1)
            {
                pathpoint = this.openPoint(x, y, z);
            }

            if (pathpoint == null && offsetY > 0 && i != -3 && i != -4 && this.getVerticalOffset(entityIn, x, y + offsetY, z) == 1)
            {
                pathpoint = this.openPoint(x, y + offsetY, z);
                y += offsetY;
            }

            if (pathpoint != null)
            {
                int j;
                int k;

                for (j = 0; y - j > 0; pathpoint = this.openPoint(x, y, z), j++)
                {
                    k = this.getVerticalOffset(entityIn, x, y - j - 1, z);

                    if (k == -2)
                    {
                        return null;
                    }

                    if (k != 1)
                    {
                        break;
                    }
                }
            }

            return pathpoint;
        }
    }

    /**
     * Checks if an entity collides with blocks at a position.
     * Returns 1 if clear, 0 for colliding with any solid block, -1 for water(if avoids water),
     * -2 for lava, -3 for fence and wall, -4 for closed trapdoor, 2 if otherwise clear except for open trapdoor or
     * water(if not avoiding)
     */
    private int getVerticalOffset(Entity entityIn, int x, int y, int z)
    {
        return getVerticalOffset(this.blockaccess, entityIn, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ);
    }

    public static int getVerticalOffset(IBlockAccess blockaccessIn, Entity entityIn, int x, int y, int z, int sizeX, int sizeY, int sizeZ)
    {
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entityIn);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int i = x; i < x + sizeX; ++i)
        {
            for (int j = y; j < y + sizeY; ++j)
            {
                for (int k = z; k < z + sizeZ; ++k)
                {
                    blockpos$mutableblockpos.set(i, j, k);
                    Block block = blockaccessIn.getBlockState(blockpos$mutableblockpos).getBlock();

                    if (block.getMaterial() != Material.AIR)
                    {
                        if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor)
                        {
                            if (block != Blocks.flowing_water && block != Blocks.water)
                            {
                                if (block instanceof BlockDoor && block.getMaterial() == Material.WOOD)
                                {
                                    return 0;
                                }
                            }
                            else
                            {
                                flag = true;
                            }
                        }
                        else
                        {
                            flag = true;
                        }

                        if (entityIn.worldObj.getBlockState(blockpos$mutableblockpos).getBlock() instanceof BlockRailBase)
                        {
                            if (!(entityIn.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(entityIn.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase))
                            {
                                return -3;
                            }
                        }
                        else if (!block.isPassable(blockaccessIn, blockpos$mutableblockpos))
                        {
                            if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockWall)
                            {
                                return -3;
                            }

                            if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor)
                            {
                                return -4;
                            }

                            Material material = block.getMaterial();

                            if (material != Material.lava)
                            {
                                return 0;
                            }

                            if (!entityIn.isInLava())
                            {
                                return -2;
                            }
                        }
                    }
                }
            }
        }

        return flag ? 2 : 1;
    }
}