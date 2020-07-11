package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.Region;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

public class WalkNodeProcessorCeiling extends NodeProcessor
{
    protected float avoidsWater;
    protected MobEntity currentEntity;

    @Override
    public void func_225578_a_(Region region, MobEntity mob) {
        super.func_225578_a_(region, mob);
        this.avoidsWater = mob.getPathPriority(PathNodeType.WATER);
    }

    @Override
    public void postProcess()
    {
        this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
        super.postProcess();
    }

    @Override
    public PathPoint getStart()
    {
        int i;
        if (this.getCanSwim() && this.entity.isInWater())
        {
            i = MathHelper.floor(this.entity.getBoundingBox().minY);
            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(this.entity.getPosX(), i, this.entity.getPosZ());

            for (BlockState blockstate = this.blockaccess.getBlockState(blockpos$mutableblockpos); blockstate.getBlock() == Blocks.WATER || blockstate.getFluidState() == Fluids.WATER.getStillFluidState(false); blockstate = this.blockaccess.getBlockState(blockpos$mutableblockpos))
            {
                ++i;
                blockpos$mutableblockpos.setPos(this.entity.getPosX(), i, this.entity.getPosZ());
            }

            --i;
        }
        else if (this.entity.onGround)
        {
            i = MathHelper.floor(this.entity.getBoundingBox().minY + 0.5D);
        }
        else
        {
            BlockPos blockpos;
            for (blockpos = new BlockPos(this.entity); (this.blockaccess.getBlockState(blockpos).isAir() || this.blockaccess.getBlockState(blockpos).allowsMovement(this.blockaccess, blockpos, PathType.LAND)) && blockpos.getY() > 0; blockpos = blockpos.down())
            {
            }

            i = blockpos.up().getY();
        }

        BlockPos blockpos2 = new BlockPos(this.entity);
        PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, blockpos2.getX(), i, blockpos2.getZ());
        if (this.entity.getPathPriority(pathnodetype1) < 0.0F)
        {
            Set<BlockPos> set = Sets.newHashSet();
            set.add(new BlockPos(this.entity.getBoundingBox().minX, i, this.entity.getBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getBoundingBox().minX, i, this.entity.getBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getBoundingBox().maxX, i, this.entity.getBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getBoundingBox().maxX, i, this.entity.getBoundingBox().maxZ));

            for (BlockPos blockpos1 : set)
            {
                PathNodeType pathnodetype = this.getPathNodeType(this.entity, blockpos1);
                if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
                {
                    return this.openPoint(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                }
            }
        }

        return this.openPoint(blockpos2.getX(), i, blockpos2.getZ());
    }

    @Override
    public FlaggedPathPoint func_224768_a(double p_224768_1_, double p_224768_3_, double p_224768_5_)
    {
        return new FlaggedPathPoint(this.openPoint(MathHelper.floor(p_224768_1_), MathHelper.floor(p_224768_3_), MathHelper.floor(p_224768_5_)));
    }

    @Override
    public int func_222859_a(PathPoint[] p_222859_1_, PathPoint p_222859_2_)
    {
        int i = 0;
        int j = 0;
        PathNodeType pathnodetype = this.getPathNodeType(this.entity, p_222859_2_.x, p_222859_2_.y + 1, p_222859_2_.z);
        if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
        {
            j = MathHelper.floor(Math.max(1.0F, this.entity.stepHeight));
        }

        double d0 = getGroundY(this.blockaccess, new BlockPos(p_222859_2_.x, p_222859_2_.y, p_222859_2_.z));
        PathPoint pathpoint = this.getSafePoint(p_222859_2_.x, p_222859_2_.y, p_222859_2_.z + 1, j, d0, Direction.SOUTH);
        if (pathpoint != null && !pathpoint.visited && pathpoint.costMalus >= 0.0F)
        {
            p_222859_1_[i++] = pathpoint;
        }

        PathPoint pathpoint1 = this.getSafePoint(p_222859_2_.x - 1, p_222859_2_.y, p_222859_2_.z, j, d0, Direction.WEST);
        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.costMalus >= 0.0F)
        {
            p_222859_1_[i++] = pathpoint1;
        }

        PathPoint pathpoint2 = this.getSafePoint(p_222859_2_.x + 1, p_222859_2_.y, p_222859_2_.z, j, d0, Direction.EAST);
        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.costMalus >= 0.0F)
        {
            p_222859_1_[i++] = pathpoint2;
        }

        PathPoint pathpoint3 = this.getSafePoint(p_222859_2_.x, p_222859_2_.y, p_222859_2_.z - 1, j, d0, Direction.NORTH);
        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.costMalus >= 0.0F)
        {
            p_222859_1_[i++] = pathpoint3;
        }

        PathPoint pathpoint4 = this.getSafePoint(p_222859_2_.x - 1, p_222859_2_.y, p_222859_2_.z - 1, j, d0, Direction.NORTH);
        if (this.func_222860_a(p_222859_2_, pathpoint1, pathpoint3, pathpoint4))
        {
            p_222859_1_[i++] = pathpoint4;
        }

        PathPoint pathpoint5 = this.getSafePoint(p_222859_2_.x + 1, p_222859_2_.y, p_222859_2_.z - 1, j, d0, Direction.NORTH);
        if (this.func_222860_a(p_222859_2_, pathpoint2, pathpoint3, pathpoint5))
        {
            p_222859_1_[i++] = pathpoint5;
        }

        PathPoint pathpoint6 = this.getSafePoint(p_222859_2_.x - 1, p_222859_2_.y, p_222859_2_.z + 1, j, d0, Direction.SOUTH);
        if (this.func_222860_a(p_222859_2_, pathpoint1, pathpoint, pathpoint6))
        {
            p_222859_1_[i++] = pathpoint6;
        }

        PathPoint pathpoint7 = this.getSafePoint(p_222859_2_.x + 1, p_222859_2_.y, p_222859_2_.z + 1, j, d0, Direction.SOUTH);
        if (this.func_222860_a(p_222859_2_, pathpoint2, pathpoint, pathpoint7))
        {
            p_222859_1_[i++] = pathpoint7;
        }

        return i;
    }

    private boolean func_222860_a(PathPoint p_222860_1_, @Nullable PathPoint p_222860_2_, @Nullable PathPoint p_222860_3_, @Nullable PathPoint p_222860_4_)
    {
        if (p_222860_4_ != null && p_222860_3_ != null && p_222860_2_ != null)
        {
            if (p_222860_4_.visited)
            {
                return false;
            }
            else if (p_222860_3_.y <= p_222860_1_.y && p_222860_2_.y <= p_222860_1_.y)
            {
                return p_222860_4_.costMalus >= 0.0F && (p_222860_3_.y < p_222860_1_.y || p_222860_3_.costMalus >= 0.0F) && (p_222860_2_.y < p_222860_1_.y || p_222860_2_.costMalus >= 0.0F);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public static double getGroundY(IBlockReader p_197682_0_, BlockPos pos)
    {
        BlockPos blockpos = pos.down();
        VoxelShape voxelshape = p_197682_0_.getBlockState(blockpos).getCollisionShape(p_197682_0_, blockpos);
        return (double) blockpos.getY() + (voxelshape.isEmpty() ? 0.0D : voxelshape.getEnd(Direction.Axis.Y));
    }

    /**
     * Returns a point that the entity can safely move to
     */
    @Nullable
    private PathPoint getSafePoint(int x, int y, int z, int stepHeight, double groundYIn, Direction facing)
    {
        PathPoint pathpoint = null;
        BlockPos blockpos = new BlockPos(x, y, z);
        double d0 = getGroundY(this.blockaccess, blockpos);
        if (d0 - groundYIn > 1.125D)
        {
            return null;
        }
        else
        {
            PathNodeType pathnodetype = this.getPathNodeType(this.entity, x, y, z);
            float f = this.entity.getPathPriority(pathnodetype);
            double d1 = (double) this.entity.getWidth() / 2.0D;
            if (f >= 0.0F)
            {
                pathpoint = this.openPoint(x, y, z);
                pathpoint.nodeType = pathnodetype;
                pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
            }

            if (pathnodetype == PathNodeType.WALKABLE)
            {
                return pathpoint;
            }
            else
            {
                if ((pathpoint == null || pathpoint.costMalus < 0.0F) && stepHeight > 0 && pathnodetype != PathNodeType.FENCE && pathnodetype != PathNodeType.TRAPDOOR)
                {
                    pathpoint = this.getSafePoint(x, y + 1, z, stepHeight - 1, groundYIn, facing);
                    if (pathpoint != null && (pathpoint.nodeType == PathNodeType.OPEN || pathpoint.nodeType == PathNodeType.WALKABLE) && this.entity.getWidth() < 1.0F)
                    {
                        double d2 = (double) (x - facing.getXOffset()) + 0.5D;
                        double d3 = (double) (z - facing.getZOffset()) + 0.5D;
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(d2 - d1, getGroundY(this.blockaccess, new BlockPos(d2, y + 1, d3)) + 0.001D, d3 - d1, d2 + d1, (double) this.entity.getHeight() + getGroundY(this.blockaccess, new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z)) - 0.002D, d3 + d1);
                        if (!this.blockaccess.hasNoCollisions(this.entity, axisalignedbb))
                        {
                            pathpoint = null;
                        }
                    }
                }

                if (pathnodetype == PathNodeType.WATER && !this.getCanSwim())
                {
                    if (this.getPathNodeType(this.entity, x, y - 1, z) != PathNodeType.WATER)
                    {
                        return pathpoint;
                    }

                    while (y > 0)
                    {
                        --y;
                        pathnodetype = this.getPathNodeType(this.entity, x, y, z);
                        if (pathnodetype != PathNodeType.WATER)
                        {
                            return pathpoint;
                        }

                        pathpoint = this.openPoint(x, y, z);
                        pathpoint.nodeType = pathnodetype;
                        pathpoint.costMalus = Math.max(pathpoint.costMalus, this.entity.getPathPriority(pathnodetype));
                    }
                }

                if (pathnodetype == PathNodeType.OPEN)
                {
                    AxisAlignedBB axisalignedbb1 = new AxisAlignedBB((double) x - d1 + 0.5D, (double) y + 0.001D, (double) z - d1 + 0.5D, (double) x + d1 + 0.5D, (float) y + this.entity.getHeight(), (double) z + d1 + 0.5D);
                    if (!this.blockaccess.hasNoCollisions(this.entity, axisalignedbb1))
                    {
                        return null;
                    }

                    if (this.entity.getWidth() >= 1.0F)
                    {
                        PathNodeType pathnodetype1 = this.getPathNodeType(this.entity, x, y - 1, z);
                        if (pathnodetype1 == PathNodeType.BLOCKED)
                        {
                            pathpoint = this.openPoint(x, y, z);
                            pathpoint.nodeType = PathNodeType.WALKABLE;
                            pathpoint.costMalus = Math.max(pathpoint.costMalus, f);
                            return pathpoint;
                        }
                    }

                    int i = 0;
                    int j = y;

                    while (pathnodetype == PathNodeType.OPEN)
                    {
                        --y;
                        if (y < 0)
                        {
                            PathPoint pathpoint2 = this.openPoint(x, j, z);
                            pathpoint2.nodeType = PathNodeType.BLOCKED;
                            pathpoint2.costMalus = -1.0F;
                            return pathpoint2;
                        }

                        PathPoint pathpoint1 = this.openPoint(x, y, z);
                        if (i++ >= this.entity.getMaxFallHeight())
                        {
                            pathpoint1.nodeType = PathNodeType.BLOCKED;
                            pathpoint1.costMalus = -1.0F;
                            return pathpoint1;
                        }

                        pathnodetype = this.getPathNodeType(this.entity, x, y, z);
                        f = this.entity.getPathPriority(pathnodetype);
                        if (pathnodetype != PathNodeType.OPEN && f >= 0.0F)
                        {
                            pathpoint = pathpoint1;
                            pathpoint1.nodeType = pathnodetype;
                            pathpoint1.costMalus = Math.max(pathpoint1.costMalus, f);
                            break;
                        }

                        if (f < 0.0F)
                        {
                            pathpoint1.nodeType = PathNodeType.BLOCKED;
                            pathpoint1.costMalus = -1.0F;
                            return pathpoint1;
                        }
                    }
                }

                return pathpoint;
            }
        }
    }

    @Override
    public PathNodeType getPathNodeType(IBlockReader blockaccessIn, int x, int y, int z, MobEntity entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn)
    {
        EnumSet<PathNodeType> enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        double d0 = (double) entitylivingIn.getWidth() / 2.0D;
        BlockPos blockpos = new BlockPos(entitylivingIn);
        this.currentEntity = entitylivingIn;
        pathnodetype = this.getPathNodeType(blockaccessIn, x, y, z, xSize, ySize, zSize, canBreakDoorsIn, canEnterDoorsIn, enumset, pathnodetype, blockpos);
        this.currentEntity = entitylivingIn;
        if (enumset.contains(PathNodeType.FENCE))
        {
            return PathNodeType.FENCE;
        }
        else
        {
            PathNodeType pathnodetype1 = PathNodeType.BLOCKED;

            for (PathNodeType pathnodetype2 : enumset)
            {
                if (entitylivingIn.getPathPriority(pathnodetype2) < 0.0F)
                {
                    return pathnodetype2;
                }

                if (entitylivingIn.getPathPriority(pathnodetype2) >= entitylivingIn.getPathPriority(pathnodetype1))
                {
                    pathnodetype1 = pathnodetype2;
                }
            }

            if (pathnodetype == PathNodeType.OPEN && entitylivingIn.getPathPriority(pathnodetype1) == 0.0F)
            {
                return PathNodeType.OPEN;
            }
            else
            {
                return pathnodetype1;
            }
        }
    }

    public PathNodeType getPathNodeType(IBlockReader p_193577_1_, int x, int y, int z, int xSize, int ySize, int zSize, boolean canOpenDoorsIn, boolean canEnterDoorsIn, EnumSet<PathNodeType> nodeTypeEnum, PathNodeType nodeType, BlockPos pos)
    {
        for (int i = 0; i < xSize; ++i)
        {
            for (int j = 0; j < ySize; ++j)
            {
                for (int k = 0; k < zSize; ++k)
                {
                    int l = i + x;
                    int i1 = j + y;
                    int j1 = k + z;
                    PathNodeType pathnodetype = this.getPathNodeType(p_193577_1_, l, i1, j1);
                    pathnodetype = this.func_215744_a(p_193577_1_, canOpenDoorsIn, canEnterDoorsIn, pos, pathnodetype);
                    if (i == 0 && j == 0 && k == 0)
                    {
                        nodeType = pathnodetype;
                    }

                    nodeTypeEnum.add(pathnodetype);
                }
            }
        }

        return nodeType;
    }

    protected PathNodeType func_215744_a(IBlockReader p_215744_1_, boolean p_215744_2_, boolean p_215744_3_, BlockPos p_215744_4_, PathNodeType p_215744_5_)
    {
        if (p_215744_5_ == PathNodeType.DOOR_WOOD_CLOSED && p_215744_2_ && p_215744_3_)
        {
            p_215744_5_ = PathNodeType.WALKABLE;
        }

        if (p_215744_5_ == PathNodeType.DOOR_OPEN && !p_215744_3_)
        {
            p_215744_5_ = PathNodeType.BLOCKED;
        }

        if (p_215744_5_ == PathNodeType.RAIL && !(p_215744_1_.getBlockState(p_215744_4_).getBlock() instanceof AbstractRailBlock) && !(p_215744_1_.getBlockState(p_215744_4_.down()).getBlock() instanceof AbstractRailBlock))
        {
            p_215744_5_ = PathNodeType.FENCE;
        }

        if (p_215744_5_ == PathNodeType.LEAVES)
        {
            p_215744_5_ = PathNodeType.BLOCKED;
        }

        return p_215744_5_;
    }

    private PathNodeType getPathNodeType(MobEntity entitylivingIn, BlockPos pos)
    {
        return this.getPathNodeType(entitylivingIn, pos.getX(), pos.getY(), pos.getZ());
    }

    private PathNodeType getPathNodeType(MobEntity entitylivingIn, int x, int y, int z)
    {
        return this.getPathNodeType(this.blockaccess, x, y, z, entitylivingIn, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanOpenDoors(), this.getCanEnterDoors());
    }

    @Override
    public PathNodeType getPathNodeType(IBlockReader blockaccessIn, int x, int y, int z)
    {
        PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);
        if (pathnodetype == PathNodeType.OPEN && y >= 1)
        {
            Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
            PathNodeType pathnodetype1 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
            pathnodetype = pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.WATER && pathnodetype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA_BLOCK || block == Blocks.CAMPFIRE)
            {
                pathnodetype = PathNodeType.DAMAGE_FIRE;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS)
            {
                pathnodetype = PathNodeType.DAMAGE_CACTUS;
            }

            if (pathnodetype1 == PathNodeType.DAMAGE_OTHER)
            {
                pathnodetype = PathNodeType.DAMAGE_OTHER;
            }
            if (pathnodetype1 == PathNodeType.DAMAGE_OTHER)
            {
                pathnodetype = PathNodeType.DAMAGE_OTHER; // Forge: consider modded damage types
            }
        }

        pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
        return pathnodetype;
    }

    public PathNodeType checkNeighborBlocks(IBlockReader blockaccessIn, int x, int y, int z, PathNodeType nodeType)
    {
        if (nodeType == PathNodeType.WALKABLE)
        {
            try (BlockPos.PooledMutable blockpos$pooledmutableblockpos = BlockPos.PooledMutable.retain())
            {
                for (int i = -1; i <= 1; ++i)
                {
                    for (int j = -1; j <= 1; ++j)
                    {
                        if (i != 0 || j != 0)
                        {
                            BlockState state = blockaccessIn.getBlockState(blockpos$pooledmutableblockpos.setPos(i + x, y, j + z));
                            Block block = state.getBlock();
                            PathNodeType type = block.getAiPathNodeType(state, blockaccessIn, blockpos$pooledmutableblockpos, this.currentEntity);
                            if (block == Blocks.CACTUS || type == PathNodeType.DAMAGE_CACTUS)
                            {
                                nodeType = PathNodeType.DANGER_CACTUS;
                            }
                            else if (block == Blocks.FIRE || type == PathNodeType.DAMAGE_FIRE)
                            {
                                nodeType = PathNodeType.DANGER_FIRE;
                            }
                            else if (block == Blocks.SWEET_BERRY_BUSH || type == PathNodeType.DAMAGE_OTHER)
                            {
                                nodeType = PathNodeType.DANGER_OTHER;
                            }
                        }
                    }
                }
            }
        }

        return nodeType;
    }

    protected PathNodeType getPathNodeTypeRaw(IBlockReader blockaccessIn, int x, int y, int z)
    {
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockState blockstate = blockaccessIn.getBlockState(blockpos);
        PathNodeType type = blockstate.getAiPathNodeType(blockaccessIn, blockpos, this.currentEntity);
        if (type != null)
        {
            return type;
        }
        Block block = blockstate.getBlock();
        Material material = blockstate.getMaterial();
        if (blockstate.isAir(blockaccessIn, blockpos))
        {
            return PathNodeType.OPEN;
        }
        else if (!block.isIn(BlockTags.TRAPDOORS) && block != Blocks.LILY_PAD)
        {
            if (block == Blocks.FIRE)
            {
                return PathNodeType.DAMAGE_FIRE;
            }
            else if (block == Blocks.CACTUS)
            {
                return PathNodeType.DAMAGE_CACTUS;
            }
            else if (block == Blocks.SWEET_BERRY_BUSH)
            {
                return PathNodeType.DAMAGE_OTHER;
            }
            else if (block instanceof DoorBlock && material == Material.WOOD && !blockstate.get(DoorBlock.OPEN))
            {
                return PathNodeType.DOOR_WOOD_CLOSED;
            }
            else if (block instanceof DoorBlock && material == Material.IRON && !blockstate.get(DoorBlock.OPEN))
            {
                return PathNodeType.DOOR_IRON_CLOSED;
            }
            else if (block instanceof DoorBlock && blockstate.get(DoorBlock.OPEN))
            {
                return PathNodeType.DOOR_OPEN;
            }
            else if (block instanceof AbstractRailBlock)
            {
                return PathNodeType.RAIL;
            }
            else if (block instanceof LeavesBlock)
            {
                return PathNodeType.LEAVES;
            }
            else if (!block.isIn(BlockTags.FENCES) && !block.isIn(BlockTags.WALLS) && (!(block instanceof FenceGateBlock) || blockstate.get(FenceGateBlock.OPEN)))
            {
                IFluidState ifluidstate = blockaccessIn.getFluidState(blockpos);
                if (ifluidstate.isTagged(FluidTags.WATER))
                {
                    return PathNodeType.WATER;
                }
                else if (ifluidstate.isTagged(FluidTags.LAVA))
                {
                    return PathNodeType.LAVA;
                }
                else
                {
                    return blockstate.allowsMovement(blockaccessIn, blockpos, PathType.LAND) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
                }
            }
            else
            {
                return PathNodeType.FENCE;
            }
        }
        else
        {
            return PathNodeType.TRAPDOOR;
        }
    }
}