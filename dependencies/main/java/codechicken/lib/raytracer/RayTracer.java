package codechicken.lib.raytracer;

import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RayTracer {

    /**
     * @param start   The vector to start RayTracing from.
     * @param end     The Vector to stop RayTracing at.
     * @param cuboids The cuboids to check for a hit.
     * @param pos     The position offset for the start and end vector.
     * @return The closest hit to the start vector.
     * //TODO 1.11, Re arrange args to (start, end, pos, cuboids)
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vec3d start, Vec3d end, List<IndexedCuboid6> cuboids, BlockPos pos) {
        return rayTraceCuboidsClosest(new Vector3(start), new Vector3(end), cuboids, pos);
    }

    /**
     * @param start   The vector to start RayTracing from.
     * @param end     The Vector to stop RayTracing at.
     * @param cuboids The cuboids to check for a hit.
     * @param pos     The position offset for the start and end vector.
     * @return The closest hit to the start vector.
     * //TODO 1.11, Re arrange args to (start, end, pos, cuboids)
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockPos pos) {
        List<CuboidRayTraceResult> results = new ArrayList<CuboidRayTraceResult>();
        for (IndexedCuboid6 cuboid6 : cuboids) {
            CuboidRayTraceResult hit = rayTrace(pos, start, end, cuboid6);
            results.add(hit);
        }
        CuboidRayTraceResult closestHit = null;
        double curClosest = Double.MAX_VALUE;
        for (CuboidRayTraceResult hit : results) {
            if (hit != null) {
                if (curClosest > hit.dist) {
                    closestHit = hit;
                    curClosest = hit.dist;
                }
            }
        }
        return closestHit;
    }

    /**
     * This method goes by the assumption you don't care about IndexedCuboids and their extra data.
     * Useful for adding hitboxes to blocks that don't actually do anything but visuals.
     *
     * @param start The vector to start RayTracing from.
     * @param end   The vector to stop RayTracing at.
     * @param pos   The position offset for the start and enc vector.
     * @param boxes The cuboids to trace.
     * @return The closest hit to the start vector.
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vector3 start, Vector3 end, BlockPos pos, AxisAlignedBB... boxes) {
        List<IndexedCuboid6> cuboidList = new LinkedList<IndexedCuboid6>();
        if (boxes != null) {
            for (AxisAlignedBB box : boxes) {
                cuboidList.add(new IndexedCuboid6(0, box));
            }
        }
        return rayTraceCuboidsClosest(start, end, cuboidList, pos);
    }

    /**
     * This method goes by the assumption you don't care about IndexedCuboids and their extra data.
     * Useful for adding hitboxes to blocks that don't actually do anything but visuals.
     *
     * @param start The vector to start RayTracing from.
     * @param end   The vector to stop RayTracing at.
     * @param pos   The position offset for the start and enc vector.
     * @param boxes The cuboids to trace.
     * @return The closest hit to the start vector.
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vec3d start, Vec3d end, BlockPos pos, AxisAlignedBB... boxes) {
        List<IndexedCuboid6> cuboidList = new LinkedList<IndexedCuboid6>();
        if (boxes != null) {
            for (AxisAlignedBB box : boxes) {
                cuboidList.add(new IndexedCuboid6(0, box));
            }
        }
        return rayTraceCuboidsClosest(start, end, cuboidList, pos);
    }

    /**
     * This method goes by the assumption you don't care about IndexedCuboids and their extra data.
     * Useful for adding hitboxes to blocks that don't actually do anything but visuals.
     *
     * @param start   The vector to start RayTracing from.
     * @param end     The vector to stop RayTracing at.
     * @param pos     The position offset for the start and enc vector.
     * @param cuboids The cuboids to trace.
     * @return The closest hit to the start vector.
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vector3 start, Vector3 end, BlockPos pos, Cuboid6... cuboids) {
        List<IndexedCuboid6> cuboidList = new LinkedList<IndexedCuboid6>();
        if (cuboids != null) {
            for (Cuboid6 cuboid : cuboids) {
                cuboidList.add(new IndexedCuboid6(0, cuboid));
            }
        }
        return rayTraceCuboidsClosest(start, end, cuboidList, pos);
    }

    /**
     * This method goes by the assumption you don't care about IndexedCuboids and their extra data.
     * Useful for adding hitboxes to blocks that don't actually do anything but visuals.
     *
     * @param start   The vector to start RayTracing from.
     * @param end     The vector to stop RayTracing at.
     * @param pos     The position offset for the start and enc vector.
     * @param cuboids The cuboids to trace.
     * @return The closest hit to the start vector.
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vec3d start, Vec3d end, BlockPos pos, Cuboid6... cuboids) {
        List<IndexedCuboid6> cuboidList = new LinkedList<IndexedCuboid6>();
        if (cuboids != null) {
            for (Cuboid6 cuboid : cuboids) {
                cuboidList.add(new IndexedCuboid6(0, cuboid));
            }
        }
        return rayTraceCuboidsClosest(start, end, cuboidList, pos);
    }

    /**
     * @param start   The vector to start RayTracing from.
     * @param end     The Vector to stop RayTracing at.
     * @param pos     The position offset for the start and end vector.
     * @param cuboids The cuboids to check for a hit.
     * @return The closest hit to the start vector.
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vector3 start, Vector3 end, BlockPos pos, IndexedCuboid6... cuboids) {
        List<IndexedCuboid6> cuboidList = new LinkedList<IndexedCuboid6>();
        if (cuboids != null) {
            Collections.addAll(cuboidList, cuboids);
        }
        return rayTraceCuboidsClosest(start, end, cuboidList, pos);
    }

    /**
     * @param start   The vector to start RayTracing from.
     * @param end     The Vector to stop RayTracing at.
     * @param pos     The position offset for the start and end vector.
     * @param cuboids The cuboids to check for a hit.
     * @return The closest hit to the start vector.
     */
    public static CuboidRayTraceResult rayTraceCuboidsClosest(Vec3d start, Vec3d end, BlockPos pos, IndexedCuboid6... cuboids) {
        List<IndexedCuboid6> cuboidList = new LinkedList<IndexedCuboid6>();
        if (cuboids != null) {
            Collections.addAll(cuboidList, cuboids);
        }
        return rayTraceCuboidsClosest(start, end, cuboidList, pos);
    }

    /**
     * Ray traces from start to end, if the ray intercepts the cuboid it returns a new CuboidRayTraceResult.
     *
     * @param pos    The BlockPosition to subtract from the start and end vector.
     * @param start  The vector to start RayTracing from.
     * @param end    The vector to end RayTracing at.
     * @param cuboid The cuboid to check for an intercept.
     * @return A new CuboidRayTraceResult if successful, null if fail.
     */
    public static CuboidRayTraceResult rayTrace(BlockPos pos, Vector3 start, Vector3 end, IndexedCuboid6 cuboid) {
        Vector3 startRay = start.copy().subtract(pos);
        Vector3 endRay = end.copy().subtract(pos);
        RayTraceResult bbResult = cuboid.aabb().calculateIntercept(startRay.vec3(), endRay.vec3());

        if (bbResult != null) {
            Vector3 hitVec = new Vector3(bbResult.hitVec).add(pos);
            EnumFacing sideHit = bbResult.sideHit;
            double dist = hitVec.copy().subtract(start).magSquared();
            return new CuboidRayTraceResult(hitVec, pos, sideHit, cuboid, dist);
        }
        return null;
    }

    public static RayTraceResult retraceBlock(World world, EntityPlayer player, BlockPos pos) {
        Vec3d startVec = getStartVec(player);
        Vec3d endVec = getEndVec(player);
        return world.getBlockState(pos).collisionRayTrace(world, pos, startVec, endVec);
    }

    private static double getBlockReachDistance_server(EntityPlayerMP player) {
        return player.interactionManager.getBlockReachDistance();
    }

    @SideOnly (Side.CLIENT)
    private static double getBlockReachDistance_client() {
        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }

    public static RayTraceResult retrace(EntityPlayer player) {
        return retrace(player, getBlockReachDistance(player));
    }

    public static RayTraceResult retrace(EntityPlayer player, boolean stopOnFluid) {
        Vec3d startVec = getStartVec(player);
        Vec3d endVec = getEndVec(player);
        return player.worldObj.rayTraceBlocks(startVec, endVec, stopOnFluid, false, true);
    }

    public static RayTraceResult retrace(EntityPlayer player, double reach, boolean stopOnFluids) {
        Vec3d startVec = getStartVec(player);
        Vec3d endVec = getEndVec(player, reach);
        return player.worldObj.rayTraceBlocks(startVec, endVec, stopOnFluids, false, true);
    }

    public static RayTraceResult retrace(EntityPlayer player, double reach) {
        return retrace(player, reach, true);
    }

    public static Vec3d getCorrectedHeadVec(EntityPlayer player) {
        Vector3 v = Vector3.fromEntity(player).add(0, player.getEyeHeight(), 0);
        return v.vec3();
    }

    public static Vec3d getStartVec(EntityPlayer player) {
        return getCorrectedHeadVec(player);
    }

    public static double getBlockReachDistance(EntityPlayer player) {
        return player.worldObj.isRemote ? getBlockReachDistance_client() : player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP) player) : 5D;
    }

    public static Vec3d getEndVec(EntityPlayer player) {
        Vec3d headVec = getCorrectedHeadVec(player);
        Vec3d lookVec = player.getLook(1.0F);
        double reach = getBlockReachDistance(player);
        return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
    }

    public static Vec3d getEndVec(EntityPlayer player, double reach) {
        Vec3d headVec = getCorrectedHeadVec(player);
        Vec3d lookVec = player.getLook(1.0F);
        return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
    }
}
