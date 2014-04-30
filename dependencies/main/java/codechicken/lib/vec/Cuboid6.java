package codechicken.lib.vec;

import codechicken.lib.util.Copyable;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Cuboid6 implements Copyable<Cuboid6>
{
    public static Cuboid6 full = new Cuboid6(0, 0, 0, 1, 1, 1);
    
    public Vector3 min;
    public Vector3 max;
    
    public Cuboid6(Vector3 min, Vector3 max)
    {
        this.min = min;
        this.max = max;
    }

    public Cuboid6(AxisAlignedBB aabb)
    {
        min = new Vector3(aabb.minX, aabb.minY, aabb.minZ);
        max = new Vector3(aabb.maxX, aabb.maxY, aabb.maxZ);        
    }
    
    public Cuboid6(Cuboid6 cuboid)
    {
        min = cuboid.min.copy();
        max = cuboid.max.copy();
    }

    public Cuboid6(double minx, double miny, double minz, double maxx, double maxy, double maxz)
    {
        min = new Vector3(minx, miny, minz);
        max = new Vector3(maxx, maxy, maxz);
    }

    public AxisAlignedBB toAABB()
    {
        return AxisAlignedBB.getBoundingBox(min.x, min.y, min.z, max.x, max.y, max.z);
    }
    
    public Cuboid6 copy()
    {
        return new Cuboid6(this);
    }

    public Cuboid6 set(Cuboid6 c) {
        return set(c.min, c.max);
    }

    public Cuboid6 set(Vector3 min, Vector3 max) {
        this.min.set(min);
        this.max.set(max);
        return this;
    }

    public Cuboid6 set(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
        min.set(minx, miny, minz);
        max.set(maxx, maxy, maxz);
        return this;
    }

    public Cuboid6 add(Vector3 vec)
    {
        min.add(vec);
        max.add(vec);
        return this;
    }
    
    public Cuboid6 sub(Vector3 vec)
    {
        min.subtract(vec);
        max.subtract(vec);
        return this;
    }
    
    public Cuboid6 expand(double d)
    {
        return expand(new Vector3(d, d, d));
    }
    
    public Cuboid6 expand(Vector3 vec)
    {
        min.sub(vec);
        max.add(vec);
        return this;
    }

    public void setBlockBounds(Block block)
    {
        block.setBlockBounds((float)min.x, (float)min.y, (float)min.z, (float)max.x, (float)max.y, (float)max.z);
    }

    public boolean intersects(Cuboid6 b)
    {
        return max.x-1E-5 > b.min.x &&
                b.max.x-1E-5 > min.x &&
                max.y-1E-5 > b.min.y &&
                b.max.y-1E-5 > min.y &&
                max.z-1E-5 > b.min.z &&
                b.max.z-1E-5 > min.z;
    }
    
    public Cuboid6 offset(Cuboid6 o)
    {
        min.add(o.min);
        max.add(o.max);
        return this;
    }
    
    public Vector3 center()
    {
        return min.copy().add(max).multiply(0.5);
    }
    
    public static boolean intersects(Cuboid6 a, Cuboid6 b)
    {
        return a != null && b != null && a.intersects(b);
    }
    
    public String toString()
    {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Cuboid: ("+new BigDecimal(min.x, cont)+", "+new BigDecimal(min.y, cont)+", "+new BigDecimal(min.z, cont)+") -> ("+
            new BigDecimal(max.x, cont)+", "+new BigDecimal(max.y, cont)+", "+new BigDecimal(max.z, cont)+")";
    }

    public Cuboid6 enclose(Vector3 vec)
    {
        if(min.x > vec.x) min.x = vec.x;
        if(min.y > vec.y) min.y = vec.y;
        if(min.z > vec.z) min.z = vec.z;
        if(max.x < vec.x) max.x = vec.x;
        if(max.y < vec.y) max.y = vec.y;
        if(max.z < vec.z) max.z = vec.z;
        return this;
    }
    
    public Cuboid6 enclose(Cuboid6 c)
    {
        if(min.x > c.min.x) min.x = c.min.x;
        if(min.y > c.min.y) min.y = c.min.y;
        if(min.z > c.min.z) min.z = c.min.z;
        if(max.x < c.max.x) max.x = c.max.x;
        if(max.y < c.max.y) max.y = c.max.y;
        if(max.z < c.max.z) max.z = c.max.z;
        return this;
    }
    
    public Cuboid6 apply(Transformation t)
    {
        t.apply(min);
        t.apply(max);
        double temp;
        if(min.x > max.x) {temp = min.x; min.x = max.x; max.x = temp;}
        if(min.y > max.y) {temp = min.y; min.y = max.y; max.y = temp;}
        if(min.z > max.z) {temp = min.z; min.z = max.z; max.z = temp;}
        return this;
    }
    
    public double getSide(int s)
    {
        switch(s) {
            case 0: return min.y;
            case 1: return max.y;
            case 2: return min.z;
            case 3: return max.z;
            case 4: return min.x;
            case 5: return max.x;
        }
        throw new IndexOutOfBoundsException("Switch Falloff");
    }
    
    public Cuboid6 setSide(int s, double d)
    {
        switch(s) {
            case 0: min.y = d; break;
            case 1: max.y = d; break;
            case 2: min.z = d; break;
            case 3: max.z = d; break;
            case 4: min.x = d; break;
            case 5: max.x = d; break;
            default: throw new IndexOutOfBoundsException("Switch Falloff");
        }
        return this;
    }
}
