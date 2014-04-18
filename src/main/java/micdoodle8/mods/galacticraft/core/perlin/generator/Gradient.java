package micdoodle8.mods.galacticraft.core.perlin.generator;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.FishyNoise;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;

/**
 * Gradient.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class Gradient extends NoiseModule
{
	private final FishyNoise noiseGen;
	private final double offsetX;
	private final double offsetY;
	private final double offsetZ;
	private final int numOctaves;
	private final double persistance;

	public Gradient(long seed, int nOctaves, double p)
	{
		this.numOctaves = nOctaves;
		this.persistance = p;
		final Random rand = new Random(seed);
		this.offsetX = rand.nextDouble() / 2 + 0.01D;
		this.offsetY = rand.nextDouble() / 2 + 0.01D;
		this.offsetZ = rand.nextDouble() / 2 + 0.01D;
		this.noiseGen = new FishyNoise(seed);
	}

	@Override
	public double getNoise(double i)
	{
		return this.getNoise(i, 0.0D);
	}

	@Override
	public double getNoise(double i, double j)
	{
		i *= this.frequency;
		j *= this.frequency;
		double val = 0;
		double curAmplitude = this.amplitude;
		for (int n = 0; n < this.numOctaves; n++)
		{
			val += this.noiseGen.noise2d(i + this.offsetX, j + this.offsetY) * curAmplitude;
			i *= 2;
			j *= 2;
			curAmplitude *= this.persistance;
		}
		return val;
	}

	@Override
	public double getNoise(double i, double j, double k)
	{
		i *= this.frequency;
		j *= this.frequency;
		k *= this.frequency;
		double val = 0;
		double curAmplitude = this.amplitude;
		// for(int n = 0; n < numOctaves; n++)
		{
			val += this.noiseGen.noise3d(i + this.offsetX, j + this.offsetY, k + this.offsetZ) * curAmplitude;
			i *= 2;
			j *= 2;
			k *= 2;
			curAmplitude *= this.persistance;
		}
		return val;
	}

}
