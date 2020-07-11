package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class PathNavigateCeiling extends PathNavigator
{
    protected WalkNodeProcessorCeiling nodeProcessor;

    public PathNavigateCeiling(EntityJuicer entity, World worldIn)
    {
        super(entity, worldIn);
    }

    @Override
    protected PathFinder getPathFinder(int val)
    {
        this.nodeProcessor = new WalkNodeProcessorCeiling();
        return new PathFinder(this.nodeProcessor, val);
    }

    @Override
    protected boolean canNavigate()
    {
        return this.entity.onGround || this.entity.getRidingEntity() != null && this.entity instanceof ZombieEntity && this.entity.getRidingEntity() instanceof ChickenEntity;
    }

    @Override
    protected Vec3d getEntityPosition()
    {
        return new Vec3d(this.entity.getPosX(), this.getPathablePosY(), this.entity.getPosZ());
    }

    private int getPathablePosY()
    {
        return (int) (this.entity.getBoundingBox().minY + 0.5D);
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3d current, Vec3d target, int sizeX, int sizeY, int sizeZ)
    {
        int i = MathHelper.floor(current.x);
        int j = MathHelper.floor(current.z);
        double d0 = target.x - current.x;
        double d1 = target.z - current.z;
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

            if (!this.isSafeToStandAt(i, (int) current.y, j, sizeX, sizeY, sizeZ, current, d0, d1))
            {
                return false;
            }
            else
            {
                sizeX = sizeX - 2;
                sizeZ = sizeZ - 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double) (i) - current.x;
                double d7 = (double) (j) - current.z;

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
                int i1 = MathHelper.floor(target.x);
                int j1 = MathHelper.floor(target.z);
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

                    if (!this.isSafeToStandAt(i, (int) current.y, j, sizeX, sizeY, sizeZ, current, d0, d1))
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
                    double d0 = (double) k + 0.5D - currentPos.x;
                    double d1 = (double) l + 0.5D - currentPos.z;

                    if (d0 * distanceX + d1 * distanceZ >= 0.0D)
                    {
                        BlockState state = this.world.getBlockState(new BlockPos(k, y + 1, l));
                        Material material = state.getMaterial();

                        if (material == Material.AIR)
                        {
                            return false;
                        }

                        if (material == Material.WATER && !this.entity.isInWater())
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
        Stream<BlockPos> stream = BlockPos.getAllInBox(new BlockPos(minX, minY, minZ), new BlockPos(minX + sizeX - 1, minY + sizeY - 1, minZ + sizeZ - 1));
        stream = stream.filter((pos) -> ((double) pos.getX() + 0.5D - currentPos.x) * distanceX + ((double) pos.getZ() + 0.5D - currentPos.z) * distanceZ >= 0.0D);
        return stream.allMatch((pos) ->
        {
            BlockState state = this.world.getBlockState(pos);

            return state.allowsMovement(world, pos, PathType.LAND);
        });
    }
}