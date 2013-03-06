package micdoodle8.mods.galacticraft.core.perlin.generator;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.FishyNoise;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;

public class RidgedMulti extends NoiseModule {

	private FishyNoise noiseGen;
	private double offsetX;
	private double offsetY;
	private double offsetZ;
	private int numOctaves;
	private double[] spectralWeights = new double[32];
	
	public RidgedMulti(long seed, int nOctaves)
	{
		numOctaves = nOctaves;
		Random rand = new Random(seed);
		offsetX = (rand.nextDouble() / 2) + 0.01D;
		offsetY = (rand.nextDouble() / 2) + 0.01D;
		offsetZ = (rand.nextDouble() / 2) + 0.01D;
		noiseGen = new FishyNoise(seed);
		double h = 1.0;
		for (int i = 0; i < 32; i++) 
		{
			spectralWeights[i] = Math.pow(frequency, -h);
			frequency *= 2;
		}
		frequency = 1.0;
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
		double weight = 1.0;
		double offset = 1.0;
		double gain = 2.0;
		for(int n = 0; n < numOctaves; n++)
		{
			double noise = absolute(noiseGen.noise2d(i + offsetX, j + offsetY));
			noise = offset - noise;
			noise *= noise;
			noise *= weight;
			
			weight = noise * gain;
			
			if(weight > 1D)
			{
				weight = 1D;
			}
			
			if(weight < 0D)
			{
				weight = 0D;
			}
			
			val += noise * spectralWeights[n];
			
			i *= 2;
			j *= 2;
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
		double weight = 1.0;
		double offset = 1.0;
		double gain = 2.0;
		for(int n = 0; n < numOctaves; n++)
		{
			double noise = absolute(noiseGen.noise3d(i + offsetX, j + offsetY, k + offsetZ));
			noise = offset - noise;
			noise *= noise;
			noise *= weight;
			
			weight = noise * gain;
			
			if(weight > 1D)
			{
				weight = 1D;
			}
			
			if(weight < 0D)
			{
				weight = 0D;
			}
			
			val += noise * spectralWeights[n];
			
			i *= 2;
			j *= 2;
			k *= 2;
		}
		return val;
	}
	
	private double absolute(double d)
	{
		if(d < 0)
		{
			d = -d;
		}
		return d;
	}
	
}
