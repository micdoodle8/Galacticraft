package micdoodle8.mods.galacticraft.core.perlin.generator;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.FishyNoise;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;

/**
 * RidgedMulti.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class RidgedMulti extends NoiseModule
{
	private final FishyNoise noiseGen;
	private final double offsetX;
	private final double offsetY;
	private final double offsetZ;
	private final int numOctaves;

	public RidgedMulti(long seed, int nOctaves)
	{
		this.numOctaves = nOctaves;
		final Random rand = new Random(seed);
		this.offsetX = rand.nextDouble() / 2 + 0.01D;
		this.offsetY = rand.nextDouble() / 2 + 0.01D;
		this.offsetZ = rand.nextDouble() / 2 + 0.01D;
		this.noiseGen = new FishyNoise(seed);
	}

	@Override
	public double getNoise(double i)
	{
		i *= this.frequencyX;
		double val = 0;
		double weight = 1.0;
		final double offset = 1.0;
		final double gain = 2.0;
		for(int n = 0; n < this.numOctaves; n++) {
			double noise = this.absolute(this.noiseGen.noise2d(i + this.offsetX, this.offsetY));
			noise = offset - noise;
			noise *= noise;
			noise *= weight;
			
			weight = noise * gain;
			
			if(weight > 1D) {
				weight = 1D;
			}
			
			if(weight < 0D) {
				weight = 0D;
			}
			
			val += noise;
			
			i *= 2;
		}
		return val;
	}

	@Override
	public double getNoise(double i, double j)
	{
		i *= this.frequencyX;
		j *= this.frequencyY;
		double val = 0;
		double weight = 1.0;
		final double offset = 1.0;
		final double gain = 2.0;
		for (int n = 0; n < this.numOctaves; n++)
		{
			double noise = this.absolute(this.noiseGen.noise2d(i + this.offsetX, j + this.offsetY));
			noise = offset - noise;
			noise *= noise;
			noise *= weight;

			weight = noise * gain;

			if (weight > 1D)
			{
				weight = 1D;
			}

			if (weight < 0D)
			{
				weight = 0D;
			}

			val += noise;

			i *= 2;
			j *= 2;
		}
		return val;
	}

	@Override
	public double getNoise(double i, double j, double k)
	{
		i *= this.frequencyX;
		j *= this.frequencyY;
		k *= this.frequencyZ;
		double val = 0;
		double weight = 1.0;
		final double offset = 1.0;
		final double gain = 2.0;
		for (int n = 0; n < this.numOctaves; n++)
		{
			double noise = this.absolute(this.noiseGen.noise3d(i + this.offsetX, j + this.offsetY, k + this.offsetZ));
			noise = offset - noise;
			noise *= noise;
			noise *= weight;

			weight = noise * gain;

			if (weight > 1D)
			{
				weight = 1D;
			}

			if (weight < 0D)
			{
				weight = 0D;
			}

			val += noise;

			i *= 2;
			j *= 2;
			k *= 2;
		}
		return val;
	}

	private double absolute(double d)
	{
		if (d < 0)
		{
			d = -d;
		}
		return d;
	}

}
