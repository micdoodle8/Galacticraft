package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateCeiling extends PathNavigate
{
    protected WalkNodeProcessorCeiling nodeProcessor;

    public PathNavigateCeiling(EntityJuicer entity, World worldIn)
    {
        super(entity, worldIn);
    }

    @Override
    protected PathFinder getPathFinder()
    {
        this.nodeProcessor = new WalkNodeProcessorCeiling();
        return new PathFinder(this.nodeProcessor);
    }

    @Override
    protected boolean canNavigate()
    {
        return this.theEntity.onGround || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.getRidingEntity() instanceof EntityChicken;
    }

    @Override
    protected Vec3d getEntityPosition()
    {
        return new Vec3d(this.theEntity.posX, (double)this.getPathablePosY(), this.theEntity.posZ);
    }

    private int getPathablePosY()
    {
        return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D);
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3d current, Vec3d target, int sizeX, int sizeY, int sizeZ)
    {
        int i = MathHelper.floor_double(current.xCoord);
        int j = MathHelper.floor_double(current.zCoord);
        double d0 = target.xCoord - current.xCoord;
        double d1 = target.zCoord - current.zCoord;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 < 1.0E-8D)
        {
            return false;
        }
        else
        {
            double d3 = 1.0D / Math.sqrt(d2);
            d0 = d0 * d3;
            d1 = d1 * d3;
            sizeX = sizeX + 2;
            sizeZ = sizeZ + 2;

            if (!this.isSafeToStandAt(i, (int)current.yCoord, j, sizeX, sizeY, sizeZ, current, d0, d1))
            {
                return false;
            }
            else
            {
                sizeX = sizeX - 2;
                sizeZ = sizeZ - 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double)(i) - current.xCoord;
                double d7 = (double)(j) - current.zCoord;

                if (d0 >= 0.0D)
                {
                    ++d6;
                }

                if (d1 >= 0.0D)
                {
                    ++d7;
                }

                d6 = d6 / d0;
                d7 = d7 / d1;
                int k = d0 < 0.0D ? -1 : 1;
                int l = d1 < 0.0D ? -1 : 1;
                int i1 = MathHelper.floor_double(target.xCoord);
                int j1 = MathHelper.floor_double(target.zCoord);
                int k1 = i1 - i;
                int l1 = j1 - j;

                while (k1 * k > 0 || l1 * l > 0)
                {
                    if (d6 < d7)
                    {
                        d6 += d4;
                        i += k;
                        k1 = i1 - i;
                    }
                    else
                    {
                        d7 += d5;
                        j += l;
                        l1 = j1 - j;
                    }

                    if (!this.isSafeToStandAt(i, (int)current.yCoord, j, sizeX, sizeY, sizeZ, current, d0, d1))
                    {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d currentPos, double distanceX, double distanceZ)
    {
        int i = x - sizeX / 2;
        int j = z - sizeZ / 2;

        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, currentPos, distanceX, distanceZ))
        {
            return false;
        }
        else
        {
            for (int k = i; k < i + sizeX; ++k)
            {
                for (int l = j; l < j + sizeZ; ++l)
                {
                    double d0 = (double)k + 0.5D - currentPos.xCoord;
                    double d1 = (double)l + 0.5D - currentPos.zCoord;

                    if (d0 * distanceX + d1 * distanceZ >= 0.0D)
                    {
                        IBlockState state = this.worldObj.getBlockState(new BlockPos(k, y + 1, l));
                        Material material = state.getBlock().getMaterial(state);

                        if (material == Material.AIR)
                        {
                            return false;
                        }

                        if (material == Material.WATER && !this.theEntity.isInWater())
                        {
                            return false;
                        }

                        if (material == Material.LAVA)
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean isPositionClear(int minX, int minY, int minZ, int sizeX, int sizeY, int sizeZ, Vec3d currentPos, double distanceX, double distanceZ)
    {
        for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(minX, minY, minZ), new BlockPos(minX + sizeX - 1, minY + sizeY - 1, minZ + sizeZ - 1)))
        {
            double d0 = (double)blockpos.getX() + 0.5D - currentPos.xCoord;
            double d1 = (double)blockpos.getZ() + 0.5D - currentPos.zCoord;

            if (d0 * distanceX + d1 * distanceZ >= 0.0D)
            {
                Block block = this.worldObj.getBlockState(blockpos).getBlock();

                if (!block.isPassable(this.worldObj, blockpos))
                {
                    return false;
                }
            }
        }

        return true;
    }
}