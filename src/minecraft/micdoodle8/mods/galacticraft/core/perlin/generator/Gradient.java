package micdoodle8.mods.galacticraft.core.perlin.generator;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.FishyNoise;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;

public class Gradient extends NoiseModule {

	private FishyNoise noiseGen;
	private double offsetX;
	private double offsetY;
	private double offsetZ;
	private int numOctaves;
	private double persistance;
	
	public Gradient(long seed, int nOctaves, double p)
	{
		numOctaves = nOctaves;
		persistance = p;
		Random rand = new Random(seed);
		offsetX = (rand.nextDouble() / 2) + 0.01D;
		offsetY = (rand.nextDouble() / 2) + 0.01D;
		offsetZ = (rand.nextDouble() / 2) + 0.01D;
		noiseGen = new FishyNoise(seed);
	}

	public double getNoise(double i)
	{
		return getNoise(i, 0.0D);
	}
	
	public double getNoise(double i, double j)
	{
		i *= frequency;
		j *= frequency;
		double val = 0;
		double curAmplitude = amplitude;
		for(int n = 0; n < numOctaves; n++)
		{
			val += noiseGen.noise2d(i + offsetX, j + offsetY) * curAmplitude;
			i *= 2;
			j *= 2;
			curAmplitude *= persistance;
		}
		return val;
	}
	
	@Override
	public double getNoise(double i, double j, double k) 
	{
		i *= frequency;
		j *= frequency;
		k *= frequency;
		double val = 0;
		double curAmplitude = amplitude;
		//for(int n = 0; n < numOctaves; n++)
		{
			val += noiseGen.noise3d(i + offsetX, j + offsetY, k + offsetZ) * curAmplitude;
			i *= 2;
			j *= 2;
			k *= 2;
			curAmplitude *= persistance;
		}
		return val;
	}

}
