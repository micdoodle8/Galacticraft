package micdoodle8.mods.galacticraft.core.perlin;

public abstract class NoiseModule
{
	public double frequencyX = 1;
	public double frequencyY = 1;
	public double frequencyZ = 1;
	public double amplitude = 1;

	public abstract double getNoise(double i);

	public abstract double getNoise(double i, double j);

	public abstract double getNoise(double i, double j, double k);
	
	public void setFrequency(double frequency) {
		this.frequencyX = frequency;
		this.frequencyY = frequency;
		this.frequencyZ = frequency;
	}

}
