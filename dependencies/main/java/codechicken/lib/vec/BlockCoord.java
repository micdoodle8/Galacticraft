package codechicken.lib.vec;

import codechicken.lib.math.MathHelper;
import codechicken.lib.util.Copyable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class BlockCoord implements Comparable<BlockCoord>, Copyable<BlockCoord> {
    public static final BlockCoord[] sideOffsets = new BlockCoord[] { new BlockCoord(0, -1, 0), new BlockCoord(0, 1, 0), new BlockCoord(0, 0, -1), new BlockCoord(0, 0, 1), new BlockCoord(-1, 0, 0), new BlockCoord(1, 0, 0) };

    public int x;
    public int y;
    public int z;

    public BlockCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockCoord(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockCoord(Vector3 v) {
        this(MathHelper.floor_double(v.x), MathHelper.floor_double(v.y), MathHelper.floor_double(v.z));
    }

    public BlockCoord(TileEntity tile) {
        this(tile.getPos());
    }

    public BlockCoord(int[] ia) {
        this(ia[0], ia[1], ia[2]);
    }

    public BlockCoord() {
    }

    /**
     * @param ia int[]{y, z, x}
     */
    public static BlockCoord fromAxes(int[] ia) {
        return new BlockCoord(ia[2], ia[0], ia[1]);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockCoord)) {
            return false;
        }
        BlockCoord o2 = (BlockCoord) obj;
        return x == o2.x && y == o2.y && z == o2.z;
    }

    @Override
    public int hashCode() {
        return (x ^ z) * 31 + y;
    }

    public int compareTo(BlockCoord o) {
        if (x != o.x) {
            return x < o.x ? 1 : -1;
        }
        if (y != o.y) {
            return y < o.y ? 1 : -1;
        }
        if (z != o.z) {
            return z < o.z ? 1 : -1;
        }
        return 0;
    }

    public Vector3 toVector3Centered() {
        return new Vector3(x + 0.5, y + 0.5, z + 0.5);
    }

    public BlockCoord multiply(int i) {
        x *= i;
        y *= i;
        z *= i;
        return this;
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public int mag2() {
        return x * x + y * y + z * z;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public boolean isAxial() {
        return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
    }

    public BlockCoord add(BlockCoord coord2) {
        x += coord2.x;
        y += coord2.y;
        z += coord2.z;
        return this;
    }

    public BlockCoord add(int i, int j, int k) {
        x += i;
        y += j;
        z += k;
        return this;
    }

    public BlockCoord sub(BlockCoord coord2) {
        x -= coord2.x;
        y -= coord2.y;
        z -= coord2.z;
        return this;
    }

    public BlockCoord sub(int i, int j, int k) {
        x -= i;
        y -= j;
        z -= k;
        return this;
    }

    public BlockCoord offset(int side) {
        return offset(side, 1);
    }

    public BlockCoord offset(int side, int amount) {
        BlockCoord offset = sideOffsets[side];
        x += offset.x * amount;
        y += offset.y * amount;
        z += offset.z * amount;
        return this;
    }

    public BlockCoord inset(int side) {
        return inset(side, 1);
    }

    public BlockCoord inset(int side, int amount) {
        return offset(side, -amount);
    }

    public int getSide(int side) {
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

    public BlockCoord setSide(int s, int v) {
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

    public int[] intArray() {
        return new int[] { x, y, z };
    }

    public BlockPos pos() {
        return new BlockPos(x, y, z);
    }

    public BlockCoord copy() {
        return new BlockCoord(x, y, z);
    }

    public BlockCoord set(int i, int j, int k) {
        x = i;
        y = j;
        z = k;
        return this;
    }

    public BlockCoord set(BlockCoord coord) {
        return set(coord.x, coord.y, coord.z);
    }

    public BlockCoord set(BlockPos pos) {
        return set(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockCoord set(int[] ia) {
        return set(ia[0], ia[1], ia[2]);
    }

    public BlockCoord set(TileEntity tile) {
        return set(tile.getPos());
    }

    public int toSide() {
        if (!isAxial()) {
            return -1;
        }
        if (y < 0) {
            return 0;
        }
        if (y > 0) {
            return 1;
        }
        if (z < 0) {
            return 2;
        }
        if (z > 0) {
            return 3;
        }
        if (x < 0) {
            return 4;
        }
        if (x > 0) {
            return 5;
        }

        return -1;
    }

    public int absSum() {
        return (x < 0 ? -x : x) +
                (y < 0 ? -y : y) +
                (z < 0 ? -z : z);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
