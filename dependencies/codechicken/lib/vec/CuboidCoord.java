package codechicken.lib.vec;

import net.minecraft.util.AxisAlignedBB;

public class CuboidCoord
{
    public BlockCoord min;
    public BlockCoord max;
    
    public CuboidCoord(BlockCoord min, BlockCoord max)
    {
        this.min = min;
        this.max = max;
    }
    
    public CuboidCoord(BlockCoord coord)
    {
        this(coord, coord.copy());
    }

    public CuboidCoord(int[] ia)
    {
        this(ia[0], ia[1], ia[2], ia[3], ia[4], ia[5]);
    }

    public CuboidCoord(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        this(new BlockCoord(x1, y1, z1), new BlockCoord(x2, y2, z2));
    }

    public void expand(int side, int amount)
    {
        if(side%2 == 0)//negative side
            min = min.offset(side, amount);
        else
            max = max.offset(side, amount);
    }
    
    public void shrink(int side, int amount)
    {
        if(side%2 == 0)//negative side
            min = min.inset(side, amount);
        else
            max = max.inset(side, amount);
    }

    public int size(int s)
    {
        switch(s)
        {
            case 0:
            case 1:
                return max.y - min.y+1;
            case 2:
            case 3:
                return max.z - min.z+1;
            case 4:
            case 5:
                return max.x - min.x+1;
            default:
                return 0;
        }
    }
    
    public int getVolume()
    {
        return (max.x-min.x+1)*(max.y-min.y+1)*(max.z-min.z+1);
    }

    public Vector3 getCenterVec()
    {
        return new Vector3(min.x+(max.x-min.x+1)/2D, min.y+(max.y-min.y+1)/2D, min.z+(max.z-min.z+1)/2D);
    }

    public BlockCoord getCenter(BlockCoord store)
    {
        store.set(min.x+(max.x-min.x)/2, min.y+(max.y-min.y)/2, min.z+(max.z-min.z)/2);
        return store;
    }

    public boolean contains(BlockCoord coord)
    {
        return contains(coord.x, coord.y, coord.z);
    }

    public boolean contains(int x, int y, int z)
    {
        return x >= min.x && x <= max.x
                && y >= min.y && y <= max.y
                && z >= min.z && z <= max.z;
    }

    public int[] intArray()
    {
        return new int[]{min.x, min.y, min.z, max.x, max.y, max.z};
    }

    public CuboidCoord copy()
    {
        return new CuboidCoord(min.copy(), max.copy());
    }

    public AxisAlignedBB toAABB()
    {
        return AxisAlignedBB.getBoundingBox(min.x, min.y, min.z, max.x+1, max.y+1, max.z+1);
    }

    public void set(BlockCoord min, BlockCoord max)
    {
        this.min = min;
        this.max = max;
    }
}
