package codechicken.lib.raytracer;

import codechicken.lib.math.MathHelper;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class IndexedCuboid6 extends Cuboid6 {

    public Object data;

    public IndexedCuboid6(Object data, Cuboid6 cuboid) {
        super(cuboid);
        this.data = data;
    }

    public IndexedCuboid6(Object data, AxisAlignedBB box) {
        super(box);
        this.data = data;
    }

    public CuboidRayTraceResult calculateIntercept(Vector3 start, Vector3 end) {
        Vector3 hit = null;
        EnumFacing sideHit = null;
        double dist = Double.MAX_VALUE;

        for (EnumFacing face : EnumFacing.values()) {
            Vector3 suspectHit = null;
            switch (face) {
                case DOWN:
                    suspectHit = start.copy().XZintercept(end, min.y);
                    break;
                case UP:
                    suspectHit = start.copy().XZintercept(end, max.y);
                    break;
                case NORTH:
                    suspectHit = start.copy().XYintercept(end, min.z);
                    break;
                case SOUTH:
                    suspectHit = start.copy().XYintercept(end, max.z);
                    break;
                case WEST:
                    suspectHit = start.copy().YZintercept(end, min.x);
                    break;
                case EAST:
                    suspectHit = start.copy().YZintercept(end, max.x);
                    break;
            }

            if (suspectHit == null) {
                continue;
            }

            switch (face) {

                case DOWN:
                case UP:
                    if (!MathHelper.between(min.x, suspectHit.x, max.x) || !MathHelper.between(min.z, suspectHit.z, max.z)) {
                        continue;
                    }
                    break;
                case NORTH:
                case SOUTH:
                    if (!MathHelper.between(min.x, suspectHit.x, max.x) || !MathHelper.between(min.y, suspectHit.y, max.y)) {
                        continue;
                    }
                    break;
                case WEST:
                case EAST:
                    if (!MathHelper.between(min.y, suspectHit.y, max.y) || !MathHelper.between(min.z, suspectHit.z, max.z)) {
                        continue;
                    }
                    break;
            }
            double suspectDist = suspectHit.copy().subtract(start).magSquared();
            if (suspectDist < dist) {
                sideHit = face;
                dist = suspectDist;
                hit = suspectHit;
            }
        }

        if (sideHit != null && hit != null) {
            return new CuboidRayTraceResult(hit, sideHit, this, dist);
        }
        return null;
    }

    @Override
    public IndexedCuboid6 copy() {
        return new IndexedCuboid6(data, this);
    }
}
