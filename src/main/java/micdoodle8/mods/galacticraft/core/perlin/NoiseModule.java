package micdoodle8.mods.galacticraft.core.perlin;

public abstract class NoiseModule
{
    public float frequencyX = 1;
    public float frequencyY = 1;
    public float frequencyZ = 1;
    public float amplitude = 1;

    public abstract float getNoise(float i);

    public abstract float getNoise(float i, float j);

    public abstract float getNoise(float i, float j, float k);

    public void setFrequency(float frequency)
    {
        this.frequencyX = frequency;
        this.frequencyY = frequency;
        this.frequencyZ = frequency;
    }

}
