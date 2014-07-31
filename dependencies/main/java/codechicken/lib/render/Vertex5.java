package codechicken.lib.render;

import codechicken.lib.render.uv.UV;
import codechicken.lib.render.uv.UVTransformation;
import codechicken.lib.util.Copyable;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Vector3;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Vertex5 implements Copyable<Vertex5>
{
    public Vector3 vec;
    public UV uv;
    
    public Vertex5()
    {
        this(new Vector3(), new UV());
    }
    
    public Vertex5(Vector3 vert, UV uv)
    {
        this.vec = vert;
        this.uv = uv;
    }
    
    public Vertex5(Vector3 vert, double u, double v)
    {
        this(vert, new UV(u, v));
    }

    public Vertex5(double x, double y, double z, double u, double v)
    {
        this(x, y, z, u, v, 0);
    }

    public Vertex5(double x, double y, double z, double u, double v, int tex)
    {
        this(new Vector3(x, y, z), new UV(u, v));
    }

    public Vertex5 set(double x, double y, double z, double u, double v)
    {
        vec.set(x, y, z);
        uv.set(u, v);
        return this;
    }

    public Vertex5 set(double x, double y, double z, double u, double v, int tex)
    {
        vec.set(x, y, z);
        uv.set(u, v, tex);
        return this;
    }

    public Vertex5 set(Vertex5 vert) {
        vec.set(vert.vec);
        uv.set(vert.uv);
        return this;
    }

    public Vertex5(Vertex5 vertex5)
    {
        this(vertex5.vec.copy(), vertex5.uv.copy());
    }

    public Vertex5 copy()
    {
        return new Vertex5(this);
    }
    
    public String toString()
    {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vertex: ("+new BigDecimal(vec.x, cont)+", "+new BigDecimal(vec.y, cont)+", "+new BigDecimal(vec.z, cont)+") ("+new BigDecimal(uv.u, cont)+", "+new BigDecimal(uv.v, cont)+")";
    }

    public Vertex5 apply(Transformation t)
    {
        vec.apply(t);
        return this;
    }
    
    public Vertex5 apply(UVTransformation t)
    {
        uv.apply(t);
        return this;
    }
}
