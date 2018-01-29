package codechicken.lib.vec;

import codechicken.lib.math.MathHelper;
import codechicken.lib.util.Copyable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Vector3 implements Copyable<Vector3> {

    public static Vector3 zero = new Vector3();
    public static Vector3 one = new Vector3(1, 1, 1);
    public static Vector3 center = new Vector3(0.5, 0.5, 0.5);

    public double x;
    public double y;
    public double z;

    public Vector3() {
    }

    public Vector3(double d, double d1, double d2) {
        x = d;
        y = d1;
        z = d2;
    }

    public Vector3(Vector3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Vector3(double[] da) {
        this(da[0], da[1], da[2]);
    }

    public Vector3(float[] fa) {
        this(fa[0], fa[1], fa[2]);
    }

    public Vector3(Vec3d vec) {
        x = vec.xCoord;
        y = vec.yCoord;
        z = vec.zCoord;
    }

    @Deprecated // use Vector3.fromBlockPos //TODO, 1.11 move this to Vec3i
    public Vector3(BlockPos pos) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
    }

    public static Vector3 fromBlockPos(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vector3 fromBlockPosCenter(BlockPos pos) {
        return fromBlockPos(pos).add(0.5);
    }

    public static Vector3 fromEntity(Entity e) {
        return new Vector3(e.posX, e.posY, e.posZ);
    }

    public static Vector3 fromEntityCenter(Entity e) {
        return new Vector3(e.posX, e.posY - e.getYOffset() + e.height / 2, e.posZ);
    }

    public static Vector3 fromTile(TileEntity tile) {
        return fromBlockPos(tile.getPos());
    }

    public static Vector3 fromTileCenter(TileEntity tile) {
        return fromTile(tile).add(0.5);
    }

    public static Vector3 fromAxes(double[] da) {
        return new Vector3(da[2], da[0], da[1]);
    }

    public static Vector3 fromAxes(float[] fa) {
        return new Vector3(fa[2], fa[0], fa[1]);
    }

    public static Vector3 fromArray(double[] da) {
        return new Vector3(da[0], da[1], da[2]);
    }

    public static Vector3 fromArray(float[] fa) {
        return new Vector3(fa[0], fa[1], fa[2]);
    }

    public Vec3d vec3() {
        return new Vec3d(x, y, z);
    }

    public BlockPos pos() {
        return new BlockPos(x, y, z);
    }

    @SideOnly (Side.CLIENT)
    public Vector3f vector3f() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    @SideOnly (Side.CLIENT)
    public Vector4f vector4f() {
        return new Vector4f((float) x, (float) y, (float) z, 1);
    }

    @SideOnly (Side.CLIENT)
    public void glVertex() {
        GlStateManager.glVertex3f((float) x, (float) y, (float) z);
    }

    public Vector3 set(double x1, double y1, double z1) {
        x = x1;
        y = y1;
        z = z1;
        return this;
    }

    public Vector3 set(double d) {
        return set(d, d, d);
    }

    public Vector3 set(Vector3 vec) {
        return set(vec.x, vec.y, vec.z);
    }

    public Vector3 set(double[] da) {
        return set(da[0], da[1], da[2]);
    }

    public Vector3 set(float[] fa) {
        return set(fa[0], fa[1], fa[2]);
    }

    public Vector3 add(double dx, double dy, double dz) {
        x += dx;
        y += dy;
        z += dz;
        return this;
    }

    public Vector3 add(double d) {
        return add(d, d, d);
    }

    public Vector3 add(Vector3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(BlockPos pos) {
        return add(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3 subtract(double dx, double dy, double dz) {
        x -= dx;
        y -= dy;
        z -= dz;
        return this;
    }

    public Vector3 subtract(double d) {
        return subtract(d, d, d);
    }

    public Vector3 subtract(Vector3 vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public Vector3 subtract(BlockPos pos) {
        return subtract(pos.getX(), pos.getY(), pos.getZ());
    }

    @Deprecated //Use subtract
    public Vector3 sub(Vector3 vec) {
        return subtract(vec);
    }

    @Deprecated //Use subtract
    public Vector3 sub(BlockPos pos) {
        return subtract(pos);
    }

    public Vector3 multiply(double fx, double fy, double fz) {
        x *= fx;
        y *= fy;
        z *= fz;
        return this;
    }

    public Vector3 multiply(double f) {
        return multiply(f, f, f);
    }

    public Vector3 multiply(Vector3 f) {
        return multiply(f.x, f.y, f.z);
    }

    public Vector3 divide(double fx, double fy, double fz) {
        x /= fx;
        y /= fy;
        z /= fz;
        return this;
    }

    public Vector3 divide(double f) {
        return divide(f, f, f);
    }

    public Vector3 divide(Vector3 vec) {
        return divide(vec.x, vec.y, vec.z);
    }

    public Vector3 divide(BlockPos pos) {
        return divide(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3 floor() {
        x = MathHelper.floor(x);
        y = MathHelper.floor(y);
        z = MathHelper.floor(z);
        return this;
    }

    public Vector3 celi() {
        x = MathHelper.ceil(x);
        y = MathHelper.ceil(y);
        z = MathHelper.ceil(z);
        return this;
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double magSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3 negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector3 normalize() {
        double d = mag();
        if (d != 0) {
            multiply(1 / d);
        }
        return this;
    }

    public double dotProduct(double x1, double y1, double z1) {
        return x1 * x + y1 * y + z1 * z;
    }

    public double dotProduct(Vector3 vec) {
        double d = vec.x * x + vec.y * y + vec.z * z;

        if (d > 1 && d < 1.00001) {
            d = 1;
        } else if (d < -1 && d > -1.00001) {
            d = -1;
        }
        return d;
    }

    public Vector3 crossProduct(Vector3 vec) {
        double d = y * vec.z - z * vec.y;
        double d1 = z * vec.x - x * vec.z;
        double d2 = x * vec.y - y * vec.x;
        x = d;
        y = d1;
        z = d2;
        return this;
    }

    public Vector3 perpendicular() {
        if (z == 0) {
            return zCrossProduct();
        }
        return xCrossProduct();
    }

    public Vector3 xCrossProduct() {
        double d = z;
        double d1 = -y;
        x = 0;
        y = d;
        z = d1;
        return this;
    }

    public Vector3 zCrossProduct() {
        double d = y;
        double d1 = -x;
        x = d;
        y = d1;
        z = 0;
        return this;
    }

    public Vector3 yCrossProduct() {
        double d = -z;
        double d1 = x;
        x = d;
        y = 0;
        z = d1;
        return this;
    }

    public double scalarProject(Vector3 b) {
        double l = b.mag();
        return l == 0 ? 0 : dotProduct(b) / l;
    }

    public Vector3 project(Vector3 b) {
        double l = b.magSquared();
        if (l == 0) {
            set(0, 0, 0);
            return this;
        }
        double m = dotProduct(b) / l;
        set(b).multiply(m);
        return this;
    }

    public Vector3 rotate(double angle, Vector3 axis) {
        Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
        return this;
    }

    public Vector3 rotate(Quat rotator) {
        rotator.rotate(this);
        return this;
    }

    public double angle(Vector3 vec) {
        return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
    }

    public Vector3 YZintercept(Vector3 end, double px) {
        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dx == 0) {
            return null;
        }

        double d = (px - x) / dx;
        if (MathHelper.between(-1E-5, d, 1E-5)) {
            return this;
        }

        if (!MathHelper.between(0, d, 1)) {
            return null;
        }

        x = px;
        y += d * dy;
        z += d * dz;
        return this;
    }

    public Vector3 XZintercept(Vector3 end, double py) {
        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dy == 0) {
            return null;
        }

        double d = (py - y) / dy;
        if (MathHelper.between(-1E-5, d, 1E-5)) {
            return this;
        }

        if (!MathHelper.between(0, d, 1)) {
            return null;
        }

        x += d * dx;
        y = py;
        z += d * dz;
        return this;
    }

    public Vector3 XYintercept(Vector3 end, double pz) {
        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dz == 0) {
            return null;
        }

        double d = (pz - z) / dz;
        if (MathHelper.between(-1E-5, d, 1E-5)) {
            return this;
        }

        if (!MathHelper.between(0, d, 1)) {
            return null;
        }

        x += d * dx;
        y += d * dy;
        z = pz;
        return this;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public boolean isAxial() {
        return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
    }

    public double getSide(int side) {
        switch (side) {
            case 0:
            case 1:
                return y;
            case 2:
            case 3:
                return z;
            case 4:
            case 5:
                return x;
        }
        throw new IndexOutOfBoundsException("Switch Falloff");
    }

    public Vector3 setSide(int s, double v) {
        switch (s) {
            case 0:
            case 1:
                y = v;
                break;
            case 2:
            case 3:
                z = v;
                break;
            case 4:
            case 5:
                x = v;
                break;
            default:
                throw new IndexOutOfBoundsException("Switch Falloff");
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector3)) {
            return false;
        }
        Vector3 v = (Vector3) o;
        return x == v.x && y == v.y && z == v.z;
    }

    /**
     * Equals method with tolerance
     *
     * @return true if this is equal to v within +-1E-5
     */
    public boolean equalsT(Vector3 v) {
        return MathHelper.between(x - 1E-5, v.x, x + 1E-5) && MathHelper.between(y - 1E-5, v.y, y + 1E-5) && MathHelper.between(z - 1E-5, v.z, z + 1E-5);
    }

    public Vector3 copy() {
        return new Vector3(this);
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector3(" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
    }

    public Translation translation() {
        return new Translation(this);
    }

    public Vector3 apply(Transformation t) {
        t.apply(this);
        return this;
    }

    public Vector3 $tilde() {
        return normalize();
    }

    public Vector3 unary_$tilde() {
        return normalize();
    }

    public Vector3 $plus(Vector3 v) {
        return add(v);
    }

    public Vector3 $minus(Vector3 v) {
        return subtract(v);
    }

    public Vector3 $times(double d) {
        return multiply(d);
    }

    public Vector3 $div(double d) {
        return multiply(1 / d);
    }

    public Vector3 $times(Vector3 v) {
        return crossProduct(v);
    }

    public double $dot$times(Vector3 v) {
        return dotProduct(v);
    }
}
