package micdoodle8.mods.galacticraft.core.perlin.generator;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.FishyNoise;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;

public class Billowed extends NoiseModule
{
	private final FishyNoise noiseGen;
	private final float offsetX;
	private final float offsetY;
	private final float offsetZ;
	private final int numOctaves;
	private final float persistance;

	public Billowed(long seed, int nOctaves, float p)
	{
		this.numOctaves = nOctaves;
		this.persistance = p;
		final Random rand = new Random(seed);
		this.offsetX = rand.nextFloat() / 2 + 0.01F;
		this.offsetY = rand.nextFloat() / 2 + 0.01F;
		this.offsetZ = rand.nextFloat() / 2 + 0.01F;
		this.noiseGen = new FishyNoise(seed);
	}

	@Override
	public float getNoise(float i)
	{
		i *= this.frequencyX;
		float val = 0;
		float curAmplitude = this.amplitude;
		for (int n = 0; n < this.numOctaves; n++)
		{
			val += Math.abs(this.noiseGen.noise2d(i + this.offsetX, this.offsetY) * curAmplitude);
			i *= 2;
			curAmplitude *= this.persistance;
		}
		return val;
	}

	@Override
	public float getNoise(float i, float j)
	{
		i *= this.frequencyX;
		j *= this.frequencyY;
		float val = 0;
		float curAmplitude = this.amplitude;
		for (int n = 0; n < this.numOctaves; n++)
		{
			val += Math.abs(this.noiseGen.noise2d(i + this.offsetX, j + this.offsetY) * curAmplitude);
			i *= 2;
			j *= 2;
			curAmplitude *= this.persistance;
		}
		return val;
	}

	@Override
	public float getNoise(float i, float j, float k)
	{
		i *= this.frequencyX;
		j *= this.frequencyY;
		k *= this.frequencyZ;
		float val = 0;
		for (int n = 0; n < this.numOctaves; n++)
		{
			val += Math.abs(this.noiseGen.noise3d(i + this.offsetX, j + this.offsetY, k + this.offsetZ) * this.amplitude);
			i *= 2;
			j *= 2;
			k *= 2;
		}
		return val;
	}

}
