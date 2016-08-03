package codechicken.lib.raytracer;

import codechicken.lib.math.MathHelper;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class RayTracer {
    private Vector3 vec = new Vector3();
    private Vector3 vec2 = new Vector3();

    private Vector3 s_vec = new Vector3();
    private double s_dist;
    private int s_side;
    private IndexedCuboid6 c_cuboid;

    private static ThreadLocal<RayTracer> t_inst = new ThreadLocal<RayTracer>();

    public static RayTracer instance() {
        RayTracer inst = t_inst.get();
        if (inst == null) {
            t_inst.set(inst = new RayTracer());
        }
        return inst;
    }

    private void traceSide(int side, Vector3 start, Vector3 end, Cuboid6 cuboid) {
        vec.set(start);
        Vector3 hit = null;
        switch (side) {
        case 0:
            hit = vec.XZintercept(end, cuboid.min.y);
            break;
        case 1:
            hit = vec.XZintercept(end, cuboid.max.y);
            break;
        case 2:
            hit = vec.XYintercept(end, cuboid.min.z);
            break;
        case 3:
            hit = vec.XYintercept(end, cuboid.max.z);
            break;
        case 4:
            hit = vec.YZintercept(end, cuboid.min.x);
            break;
        case 5:
            hit = vec.YZintercept(end, cuboid.max.x);
            break;
        }
        if (hit == null) {
            return;
        }

        switch (side) {
        case 0:
        case 1:
            if (!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) {
                return;
            }
            break;
        case 2:
        case 3:
            if (!MathHelper.between(cuboid.min.x, hit.x, cuboid.max.x) || !MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y)) {
                return;
            }
            break;
        case 4:
        case 5:
            if (!MathHelper.between(cuboid.min.y, hit.y, cuboid.max.y) || !MathHelper.between(cuboid.min.z, hit.z, cuboid.max.z)) {
                return;
            }
            break;
        }

        double dist = vec2.set(hit).subtract(start).magSquared();
        if (dist < s_dist) {
            s_side = side;
            s_dist = dist;
            s_vec.set(vec);
        }
    }

    private boolean rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid) {
        s_dist = Double.MAX_VALUE;
        s_side = -1;

        for (int i = 0; i < 6; i++) {
            traceSide(i, start, end, cuboid);
        }

        return s_side >= 0;
    }

    public ExtendedMOP rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, BlockCoord pos, Object data) {
        return rayTraceCuboid(start, end, cuboid) ? new ExtendedMOP(s_vec, s_side, pos, data, s_dist) : null;
    }

    public ExtendedMOP rayTraceCuboid(Vector3 start, Vector3 end, Cuboid6 cuboid, Entity entity, Object data) {
        return rayTraceCuboid(start, end, cuboid) ? new ExtendedMOP(entity, s_vec, data, s_dist) : null;
    }

    public void rayTraceCuboids(Vector3 start, Vector3 end, List<IndexedCuboid6> cuboids, BlockCoord pos, Block block, List<ExtendedMOP> hitList) {
        for (IndexedCuboid6 cuboid : cuboids) {
            ExtendedMOP mop = rayTraceCuboid(start, end, cuboid, pos, cuboid.data);
            if (mop != null) {
                hitList.add(mop);
            }
        }
    }

    public static MovingObjectPosition retraceBlock(World world, EntityPlayer player, BlockPos pos) {
        IBlockState b = world.getBlockState(pos);
        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1.0F);
        double reach = getBlockReachDistance(player);
        Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        return b.getBlock().collisionRayTrace(world, pos, headVec, endVec);
    }

    private static double getBlockReachDistance_server(EntityPlayerMP player) {
        return player.theItemInWorldManager.getBlockReachDistance();
    }

    @SideOnly(Side.CLIENT)
    private static double getBlockReachDistance_client() {
        return Minecraft.getMinecraft().playerController.getBlockReachDistance();
    }

    public static MovingObjectPosition retrace(EntityPlayer player) {
        return retrace(player, getBlockReachDistance(player));
    }

    public static MovingObjectPosition retrace(EntityPlayer player, double reach) {
        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1);
        Vec3 endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        return player.worldObj.rayTraceBlocks(headVec, endVec, true, false, true);
    }

    public static Vec3 getCorrectedHeadVec(EntityPlayer player) {
        Vector3 v = Vector3.fromEntity(player);
        if (player.worldObj.isRemote) {
            v.y += player.getEyeHeight() - player.getDefaultEyeHeight();//compatibility with eye height changing mods
        } else {
            v.y += player.getEyeHeight();
            if (player instanceof EntityPlayerMP && player.isSneaking()) {
                v.y -= 0.08;
            }
        }
        return v.vec3();
    }

    public static Vec3 getStartVec(EntityPlayer player) {
        return getCorrectedHeadVec(player);
    }

    public static double getBlockReachDistance(EntityPlayer player) {
        return player.worldObj.isRemote ? getBlockReachDistance_client() : player instanceof EntityPlayerMP ? getBlockReachDistance_server((EntityPlayerMP) player) : 5D;
    }

    public static Vec3 getEndVec(EntityPlayer player) {
        Vec3 headVec = getCorrectedHeadVec(player);
        Vec3 lookVec = player.getLook(1.0F);
        double reach = getBlockReachDistance(player);
        return headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
    }
}
