package codechicken.lib.raytracer;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ExtendedMOP extends MovingObjectPosition implements Comparable<ExtendedMOP>
{
    public Object data;
    /**
     * The square distance from the start of the raytrace.
     */
    public double dist;
    
    public ExtendedMOP(Entity entity, Object data)
    {
        super(entity);
        setData(data);
    }
    
    public ExtendedMOP(int x, int y, int z, int side, Vec3 hit, Object data)
    {
        super(x, y, z, side, hit);
        setData(data);
    }
    
    public ExtendedMOP(MovingObjectPosition mop, Object data, double dist)
    {
        super(0, 0, 0, 0, mop.hitVec);
        typeOfHit = mop.typeOfHit;
        blockX = mop.blockX;
        blockY = mop.blockY;
        blockZ = mop.blockZ;
        sideHit = mop.sideHit;
        subHit = mop.subHit;
        setData(data);
        this.dist = dist;
    }

    public void setData(Object data)
    {
        if(data instanceof Integer)
            subHit = ((Integer) data).intValue();
        this.data = data;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getData(MovingObjectPosition mop)
    {
        if(mop instanceof ExtendedMOP)
            return (T)((ExtendedMOP)mop).data;
        
        return (T)Integer.valueOf(mop.subHit);
    }
    
    @Override
    public int compareTo(ExtendedMOP o)
    {
        return dist == o.dist ? 0 : dist < o.dist ? -1 : 1;
    }
}
