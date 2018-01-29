package codechicken.lib.raytracer;

import codechicken.lib.vec.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by covers1624 on 8/9/2016.
 * This class is kind of special as if you return this in Block.collisionRayTrace, when the blocks HitBox is rendered it will cancel and use the cuboid provided here.
 * This is to get around the fact that it is currently impossible to determine from Block.getSelectedBoundingBox what sub box to render as you have absolutely no player context to do a trace.
 */
public class CuboidRayTraceResult extends DistanceRayTraceResult {

    public IndexedCuboid6 cuboid6;

    /**
     * Optional flag that disables auto rendering of this trace result. Useful when
     * you wish to use this class to retain the Cuboid6 box but would like to render
     * it yourself.
     */
    public boolean disableAutoHitboxRender = false;

    public CuboidRayTraceResult(Entity entity, Vector3 hit, IndexedCuboid6 cuboid, double dist) {
        super(entity, hit, cuboid.data, dist);
        this.cuboid6 = cuboid;
    }

    public CuboidRayTraceResult(Vector3 hit, BlockPos pos, EnumFacing side, IndexedCuboid6 cuboid, double dist) {
        super(hit, pos, side, cuboid.data, dist);
        this.cuboid6 = cuboid;
    }

    public CuboidRayTraceResult(Vector3 hit, EnumFacing side, IndexedCuboid6 cuboid6, double dist) {
        super(hit, side, cuboid6.data, dist);
        this.cuboid6 = cuboid6;
    }

    /**
     * This will create a new instance of a DistanceRayTraceResult.
     * You will not be able to TypeCast this back to a CuboidRayTraceResult.
     * This can be used to avoid CCL's HitBox render taking over down the line.
     *
     * @return
     */
    public DistanceRayTraceResult getAsTraceResult() {
        return new DistanceRayTraceResult(new Vector3(hitVec), sideHit, hitInfo, dist);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", "") + ", cuboid=" + cuboid6.toString() + "}";
    }
}
