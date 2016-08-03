package codechicken.lib.raytracer;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

public class ExtendedMOP extends MovingObjectPosition implements Comparable<ExtendedMOP> {
    /**
     * The square distance from the start of the raytrace.
     */
    public double dist;

    public ExtendedMOP(Entity entity, Vector3 hit, Object data, double dist) {
        super(entity, hit.vec3());
        setData(data);
        this.dist = dist;
    }

    public ExtendedMOP(Vector3 hit, int side, BlockCoord pos, Object data, double dist) {
        super(hit.vec3(), EnumFacing.values()[side], pos.pos());
        setData(data);
        this.dist = dist;
    }

    public void setData(Object data) {
        if (data instanceof Integer) {
            subHit = ((Integer) data).intValue();
        }
        hitInfo = data;
    }

    @Override
    public int compareTo(ExtendedMOP o) {
        return dist == o.dist ? 0 : dist < o.dist ? -1 : 1;
    }
}
