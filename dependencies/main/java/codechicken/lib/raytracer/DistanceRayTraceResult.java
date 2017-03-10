package codechicken.lib.raytracer;

import codechicken.lib.vec.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

//TODO Copyable.
public class DistanceRayTraceResult extends RayTraceResult implements Comparable<DistanceRayTraceResult> {

    /**
     * The square distance from the start of the raytrace.
     */
    public double dist;

    public DistanceRayTraceResult(Entity entity, Vector3 hit, Object data, double dist) {
        super(entity, hit.vec3());
        setData(data);
        this.dist = dist;
    }

    public DistanceRayTraceResult(Vector3 hit, EnumFacing side, Object data, double dist) {
        super(hit.vec3(), side);
        this.dist = dist;
        setData(data);
    }

    public DistanceRayTraceResult(Vector3 hit, BlockPos pos, EnumFacing side, Object data, double dist) {
        super(hit.vec3(), side, pos);
        setData(data);
        this.dist = dist;
    }

    public void setData(Object data) {
        if (data instanceof Integer) {
            subHit = (Integer) data;
        }
        hitInfo = data;
    }

    @Deprecated//TODO Is this actually needed.. Can remove an AT line if not needed.
    public void setPos(BlockPos pos) {
        this.blockPos = pos;
    }

    public void offsetHit(BlockPos pos) {
        hitVec = hitVec.addVector(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public int compareTo(DistanceRayTraceResult o) {
        return dist == o.dist ? 0 : dist < o.dist ? -1 : 1;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", subHit=" + subHit + ", sqDist: " + dist + "}";
    }
}
