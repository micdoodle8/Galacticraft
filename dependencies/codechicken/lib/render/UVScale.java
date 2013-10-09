package codechicken.lib.render;

public class UVScale implements IUVTransformation
{
    double su;
    double sv;
    
    public UVScale(double scaleu, double scalev)
    {
        su = scaleu;
        sv = scalev;
    }
    
    public UVScale(double d)
    {
        this(d, d);
    }

    @Override
    public void transform(UV uv)
    {
        uv.u*=su;
        uv.v*=sv;
    }
}
