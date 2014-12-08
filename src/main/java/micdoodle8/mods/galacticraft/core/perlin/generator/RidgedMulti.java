package micdoodle8.mods.galacticraft.core.perlin.generator;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.FishyNoise;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;

public class RidgedMulti extends NoiseModule
{
    private final FishyNoise noiseGen;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final int numOctaves;

    public RidgedMulti(long seed, int nOctaves)
    {
        this.numOctaves = nOctaves;
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
        float weight = 1.0F;
        final float offset = 1.0F;
        final float gain = 2.0F;
        for (int n = 0; n < this.numOctaves; n++)
        {
            float noise = this.absolute(this.noiseGen.noise2d(i + this.offsetX, this.offsetY));
            noise = offset - noise;
            noise *= noise;
            noise *= weight;

            weight = noise * gain;

            if (weight > 1F)
            {
                weight = 1F;
            }

            if (weight < 0F)
            {
                weight = 0F;
            }

            val += noise;

            i *= 2;
        }
        return val;
    }

    @Override
    public float getNoise(float i, float j)
    {
        i *= this.frequencyX;
        j *= this.frequencyY;
        float val = 0;
        float weight = 1.0F;
        final float offset = 1.0F;
        final float gain = 2.0F;
        for (int n = 0; n < this.numOctaves; n++)
        {
            float noise = this.absolute(this.noiseGen.noise2d(i + this.offsetX, j + this.offsetY));
            noise = offset - noise;
            noise *= noise;
            noise *= weight;

            weight = noise * gain;

            if (weight > 1F)
            {
                weight = 1F;
            }

            if (weight < 0F)
            {
                weight = 0F;
            }

            val += noise;

            i *= 2;
            j *= 2;
        }
        return val;
    }

    @Override
    public float getNoise(float i, float j, float k)
    {
        i *= this.frequencyX;
        j *= this.frequencyY;
        k *= this.frequencyZ;
        float val = 0F;
        float weight = 1.0F;
        final float offset = 1.0F;
        final float gain = 2.0F;
        for (int n = 0; n < this.numOctaves; n++)
        {
            float noise = this.absolute(this.noiseGen.noise3d(i + this.offsetX, j + this.offsetY, k + this.offsetZ));
            noise = offset - noise;
            noise *= noise;
            noise *= weight;

            weight = noise * gain;

            if (weight > 1F)
            {
                weight = 1F;
            }

            if (weight < 0F)
            {
                weight = 0F;
            }

            val += noise;

            i *= 2;
            j *= 2;
            k *= 2;
        }
        return val;
    }

    private float absolute(float d)
    {
        if (d < 0)
        {
            d = -d;
        }
        return d;
    }

}
