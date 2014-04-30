package codechicken.lib.vec;

import codechicken.lib.math.MathHelper;
import codechicken.lib.util.Copyable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Quat implements Copyable<Quat>
{
    public double x;
    public double y;
    public double z;
    public double s;

    public Quat()
    {
        s = 1;
        x = 0;
        y = 0;
        z = 0;
    }

    public Quat(Quat quat)
    {
        x = quat.x;
        y = quat.y;
        z = quat.z;
        s = quat.s;
    }

    public Quat(double d, double d1, double d2, double d3)
    {
        x = d1;
        y = d2;
        z = d3;
        s = d;
    }

    public Quat set(Quat quat)
    {
        x = quat.x;
        y = quat.y;
        z = quat.z;
        s = quat.s;
        
        return this;
    }

    public Quat set(double d, double d1, double d2, double d3)
    {
        x = d1;
        y = d2;
        z = d3;
        s = d;
        
        return this;
    }
    
    public static Quat aroundAxis(double ax, double ay, double az, double angle)
    {
        return new Quat().setAroundAxis(ax, ay, az, angle);
    }

    public static Quat aroundAxis(Vector3 axis, double angle)
    {
        return aroundAxis(axis.x, axis.y, axis.z, angle);
    }
    
    public Quat setAroundAxis(double ax, double ay, double az, double angle)
    {
        angle *= 0.5;
        double d4 = MathHelper.sin(angle);
        return set(MathHelper.cos(angle), ax * d4, ay * d4, az * d4);
    }
    
    public Quat setAroundAxis(Vector3 axis, double angle)
    {
        return setAroundAxis(axis.x, axis.y, axis.z, angle);
    }

    public Quat multiply(Quat quat)
    {
        double d = s * quat.s - x * quat.x - y * quat.y - z * quat.z;
        double d1 = s * quat.x + x * quat.s - y * quat.z + z * quat.y;
        double d2 = s * quat.y + x * quat.z + y * quat.s - z * quat.x;
        double d3 = s * quat.z - x * quat.y + y * quat.x + z * quat.s;
        s = d;
        x = d1;
        y = d2;
        z = d3;
        
        return this;
    }

    public Quat rightMultiply(Quat quat)
    {
        double d = s * quat.s - x * quat.x - y * quat.y - z * quat.z;
        double d1 = s * quat.x + x * quat.s + y * quat.z - z * quat.y;
        double d2 = s * quat.y - x * quat.z + y * quat.s + z * quat.x;
        double d3 = s * quat.z + x * quat.y - y * quat.x + z * quat.s;
        s = d;
        x = d1;
        y = d2;
        z = d3;
        
        return this;
    }

    public double mag()
    {
        return Math.sqrt(x * x + y * y + z * z + s * s);
    }

    public Quat normalize()
    {
        double d = mag();
        if(d != 0)
        {
            d = 1 / d;
            x *= d;
            y *= d;
            z *= d;
            s *= d;
        }
        
        return this;
    }

    public Quat copy()
    {
        return new Quat(this);
    }

    public void rotate(Vector3 vec)
    {
        double d = -x * vec.x - y * vec.y - z * vec.z;
        double d1 = s * vec.x + y * vec.z - z * vec.y;
        double d2 = s * vec.y - x * vec.z + z * vec.x;
        double d3 = s * vec.z + x * vec.y - y * vec.x;
        vec.x = d1 * s - d * x - d2 * z + d3 * y;
        vec.y = d2 * s - d * y + d1 * z - d3 * x;
        vec.z = d3 * s - d * z - d1 * y + d2 * x;
    }

    public String toString()
    {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Quat("+new BigDecimal(s, cont)+", "+new BigDecimal(x, cont)+", "+new BigDecimal(y, cont)+", "+new BigDecimal(z, cont)+")";
    }

    public Rotation rotation()
    {
        return new Rotation(this);
    }
}
