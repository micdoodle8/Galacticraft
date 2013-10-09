package codechicken.lib.render;

public class UVTranslation implements IUVTransformation
{
    public double du;
    public double dv;
    
    public UVTranslation(double u, double v)
    {
        du = u;
        dv = v;
    }

    @Override
    public void transform(UV texcoord)
    {
        texcoord.u+=du;
        texcoord.v+=dv;
    }
}
