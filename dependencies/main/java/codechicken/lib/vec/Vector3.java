package codechicken.lib.vec;

import codechicken.lib.math.MathHelper;
import codechicken.lib.util.Copyable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Vector3 implements Copyable<Vector3>
{
    public static Vector3 zero = new Vector3();
    public static Vector3 one = new Vector3(1, 1, 1);
    public static Vector3 center = new Vector3(0.5, 0.5, 0.5);
    
    public double x;
    public double y;
    public double z;

    public Vector3()
    {
    }

    public Vector3(double d, double d1, double d2)
    {
        x = d;
        y = d1;
        z = d2;
    }

    public Vector3(Vector3 vec)
    {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Vector3(double[] da)
    {
        this(da[0], da[1], da[2]);
    }

    public Vector3(Vec3 vec)
    {
        x = vec.xCoord;
        y = vec.yCoord;
        z = vec.zCoord;
    }

    public Vector3(BlockCoord coord)
    {
        x = coord.x;
        y = coord.y;
        z = coord.z;
    }

    public Vector3 copy()
    {
        return new Vector3(this);
    }
    
    public static Vector3 fromEntity(Entity e)
    {
        return new Vector3(e.posX, e.posY, e.posZ);
    }
    
    public static Vector3 fromEntityCenter(Entity e)
    {
        return new Vector3(e.posX, e.posY - e.yOffset + e.height/2, e.posZ);
    }

    public static Vector3 fromTileEntity(TileEntity e)
    {
        return new Vector3(e.xCoord, e.yCoord, e.zCoord);
    }
    
    public static Vector3 fromTileEntityCenter(TileEntity e)
    {
        return new Vector3(e.xCoord + 0.5, e.yCoord + 0.5, e.zCoord + 0.5);
    }

    public static Vector3 fromAxes(double[] da)
    {
        return new Vector3(da[2], da[0], da[1]);
    }

    public Vector3 set(double d, double d1, double d2)
    {
        x = d;
        y = d1;
        z = d2;
        return this;
    }

    public Vector3 set(Vector3 vec)
    {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        return this;
    }

    public double getSide(int side) {
        switch(side) {
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

    public Vector3 setSide(int s, double v)
    {
        switch(s)
        {
            case 0:
            case 1: y = v; break;
            case 2:
            case 3: z = v; break;
            case 4:
            case 5: x = v; break;
            default: throw new IndexOutOfBoundsException("Switch Falloff");
        }
        return this;
    }

    public double dotProduct(Vector3 vec)
    {
        double d = vec.x * x + vec.y * y + vec.z * z;
        
        if(d > 1 && d < 1.00001)
            d = 1;
        else if(d < -1 && d > -1.00001)
            d = -1;
        return d;
    }

    public double dotProduct(double d, double d1, double d2)
    {
        return d * x + d1 * y + d2 * z;
    }

    public Vector3 crossProduct(Vector3 vec)
    {
        double d = y * vec.z - z * vec.y;
        double d1 = z * vec.x - x * vec.z;
        double d2 = x * vec.y - y * vec.x;
        x = d;
        y = d1;
        z = d2;
        return this;
    }

    public Vector3 add(double d, double d1, double d2)
    {
        x += d;
        y += d1;
        z += d2;
        return this;
    }

    public Vector3 add(Vector3 vec)
    {
        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }
    
    public Vector3 add(double d)
    {
        return add(d, d, d);
    }

    public Vector3 sub(Vector3 vec)
    {
        return subtract(vec);
    }
    
    public Vector3 subtract(Vector3 vec)
    {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    public Vector3 negate(Vector3 vec)
    {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }
    
    public Vector3 multiply(double d)
    {
        x *= d;
        y *= d;
        z *= d;
        return this;
    }

    public Vector3 multiply(Vector3 f)
    {
        x *= f.x;
        y *= f.y;
        z *= f.z;
        return this;
    }
    
    public Vector3 multiply(double fx, double fy, double fz)
    {
        x *= fx;
        y *= fy;
        z *= fz;
        return this;
    }

    public double mag()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double magSquared()
    {
        return x * x + y * y + z * z;
    }

    public Vector3 normalize()
    {
        double d = mag();
        if(d != 0)
        {
            multiply(1 / d);
        }
        return this;
    }

    public String toString()
    {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector3("+new BigDecimal(x, cont)+", "+new BigDecimal(y, cont)+", "+new BigDecimal(z, cont)+")";
    }
    
    public Vector3 perpendicular()
    {
        if(z == 0)
            return zCrossProduct();
        return xCrossProduct();
    }
    
    public Vector3 xCrossProduct()
    {
        double d = z;
        double d1 = -y;
        x = 0;
        y = d;
        z = d1;
        return this;
    }
    
    public Vector3 zCrossProduct()
    {
        double d = y;
        double d1 = -x;
        x = d;
        y = d1;
        z = 0;
        return this;
    }
    
    public Vector3 yCrossProduct()
    {
        double d = -z;
        double d1 = x;
        x = d;
        y = 0;
        z = d1;
        return this;
    }
    
    public Vector3 rotate(double angle, Vector3 axis){
        Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
        return this;
    }
    
    public Vector3 rotate(Quat rotator)
    {
        rotator.rotate(this);
        return this;
    }

    public Vec3 toVec3D()
    {
        return Vec3.createVectorHelper(x, y, z);
    }

    public double angle(Vector3 vec)
    {
        return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
    }

    public boolean isZero()
    {
        return x == 0 && y == 0 && z == 0;
    }

    public boolean isAxial()
    {
        return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
    }

    @SideOnly(Side.CLIENT)
    public Vector3f vector3f()
    {
        return new Vector3f((float)x, (float)y, (float)z);
    }

    @SideOnly(Side.CLIENT)
    public Vector4f vector4f()
    {
        return new Vector4f((float)x, (float)y, (float)z, 1);
    }
    
    @SideOnly(Side.CLIENT)
    public void glVertex()
    {
        GL11.glVertex3d(x, y, z);
    }

    public Vector3 YZintercept(Vector3 end, double px)
    {
        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dx == 0)
            return null;
        
        double d = (px - x) / dx;
        if(MathHelper.between(-1E-5, d, 1E-5))
            return this;
        
        if(!MathHelper.between(0, d, 1))
            return null;
        
        x = px;
        y += d*dy;
        z += d*dz;        
        return this;
    }

    public Vector3 XZintercept(Vector3 end, double py)
    {
        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dy == 0)
            return null;
        
        double d = (py - y) / dy;
        if(MathHelper.between(-1E-5, d, 1E-5))
            return this;
        
        if(!MathHelper.between(0, d, 1))
            return null;
        
        x += d*dx;
        y = py;
        z += d*dz;        
        return this;
    }

    public Vector3 XYintercept(Vector3 end, double pz)
    {
        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dz == 0)
            return null;
        
        double d = (pz - z) / dz;
        if(MathHelper.between(-1E-5, d, 1E-5))
            return this;
        
        if(!MathHelper.between(0, d, 1))
            return null;
        
        x += d*dx;
        y += d*dy;
        z = pz;        
        return this;
    }

    public Vector3 negate()
    {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Translation translation()
    {
        return new Translation(this);
    }

    public double scalarProject(Vector3 b)
    {
        double l = b.mag();
        return l == 0 ? 0 : dotProduct(b)/l;
    }
    
    public Vector3 project(Vector3 b)
    {
        double l = b.magSquared();
        if(l == 0)
        {
            set(0, 0, 0);
            return this;
        }
        double m = dotProduct(b)/l;
        set(b).multiply(m);
        return this;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Vector3))
            return false;
        Vector3 v = (Vector3)o;
        return x == v.x && y == v.y && z == v.z;
    }
    
    /**
     * Equals method with tolerance
     * @return true if this is equal to v within +-1E-5
     */
    public boolean equalsT(Vector3 v)
    {
        return MathHelper.between(x-1E-5, v.x, x+1E-5) &&
                MathHelper.between(y-1E-5, v.y, y+1E-5) &&
                MathHelper.between(z-1E-5, v.z, z+1E-5);
    }

    public Vector3 apply(Transformation t)
    {
        t.apply(this);
        return this;
    }

    public Vector3 $tilde()
    {
        return normalize();
    }

    public Vector3 unary_$tilde()
    {
        return normalize();
    }
    
    public Vector3 $plus(Vector3 v)
    {
        return add(v);
    }
    
    public Vector3 $minus(Vector3 v)
    {
        return subtract(v);
    }
    
    public Vector3 $times(double d)
    {
        return multiply(d);
    }
    
    public Vector3 $div(double d)
    {
        return multiply(1/d);
    }
    
    public Vector3 $times(Vector3 v)
    {
        return crossProduct(v);
    }

    public double $dot$times(Vector3 v)
    {
        return dotProduct(v);
    }
}
