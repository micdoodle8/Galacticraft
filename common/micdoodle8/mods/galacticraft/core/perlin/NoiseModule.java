package micdoodle8.mods.galacticraft.core.perlin;

public abstract class NoiseModule
{

    public double frequency = 1;
    public double amplitude = 1;

    public abstract double getNoise(double i);

    public abstract double getNoise(double i, double j);

    public abstract double getNoise(double i, double j, double k);

}
