package micdoodle8.mods.galacticraft.api.vector;

public class Vector4
{
    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4()
    {
        this(0.0F, 0.0F, 0.0F, 0.0F);
    }
}
