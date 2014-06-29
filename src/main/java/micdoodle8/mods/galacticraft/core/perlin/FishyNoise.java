package micdoodle8.mods.galacticraft.core.perlin;

import java.util.Random;

public class FishyNoise
{

	int[] perm = new int[512];

	public float[][] grad2d = new float[][] { { 1, 0 }, { .9239F, .3827F }, { .707107F, 0.707107F }, { .3827F, .9239F }, { 0, 1 }, { -.3827F, .9239F }, { -.707107F, 0.707107F }, { -.9239F, .3827F }, { -1, 0 }, { -.9239F, -.3827F }, { -.707107F, -0.707107F }, { -.3827F, -.9239F }, { 0, -1 }, { .3827F, -.9239F }, { .707107F, -0.707107F }, { .9239F, -.3827F } };

	public int[][] grad3d = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { -1, 1, 0 }, { 0, -1, 1 }, { 0, -1, -1 } };

	public FishyNoise(long seed)
	{
		final Random rand = new Random(seed);
		for (int i = 0; i < 256; i++)
		{
			this.perm[i] = i; // Fill up the random array with numbers 0-256
		}

		for (int i = 0; i < 256; i++) // Shuffle those numbers for the random
										// effect
		{
			final int j = rand.nextInt(256);
			this.perm[i] = this.perm[i] ^ this.perm[j];
			this.perm[j] = this.perm[i] ^ this.perm[j];
			this.perm[i] = this.perm[i] ^ this.perm[j];
		}

        System.arraycopy(this.perm, 0, this.perm, 256, 256);
	}

	private static float lerp(float x, float y, float n)
	{
		return x + n * (y - x);
	}

	private static int fastFloor(float x)
	{
		return x > 0 ? (int) x : (int) x - 1;
	}

	private static float fade(float n)
	{
		return n * n * n * (n * (n * 6 - 15) + 10);
	}

	private static float dot2(float[] grad2, float x, float y)
	{
		return grad2[0] * x + grad2[1] * y;
	}

	private static float dot3(int[] grad3, float x, float y, float z)
	{
		return grad3[0] * x + grad3[1] * y + grad3[2] * z;
	}

	public float noise2d(float x, float y)
	{
		int largeX = FishyNoise.fastFloor(x);
		int largeY = FishyNoise.fastFloor(y);
		x -= largeX;
		y -= largeY;
		largeX = largeX & 255;
		largeY = largeY & 255;

		final float u = FishyNoise.fade(x);
		final float v = FishyNoise.fade(y);

		final float grad00 = FishyNoise.dot2(this.grad2d[this.perm[largeX + this.perm[largeY]] & 15], x, y);
		final float grad01 = FishyNoise.dot2(this.grad2d[this.perm[largeX + this.perm[largeY + 1]] & 15], x, y - 1);
		final float grad11 = FishyNoise.dot2(this.grad2d[this.perm[largeX + 1 + this.perm[largeY + 1]] & 15], x - 1, y - 1);
		final float grad10 = FishyNoise.dot2(this.grad2d[this.perm[largeX + 1 + this.perm[largeY]] & 15], x - 1, y);

		final float lerpX0 = FishyNoise.lerp(grad00, grad10, u);
		final float lerpX1 = FishyNoise.lerp(grad01, grad11, u);
		return FishyNoise.lerp(lerpX0, lerpX1, v);
	}

	public float noise3d(float x, float y, float z)
	{
		int unitX = FishyNoise.fastFloor(x);
		int unitY = FishyNoise.fastFloor(y);
		int unitZ = FishyNoise.fastFloor(z);

		x -= unitX;
		y -= unitY;
		z -= unitZ;

		unitX = unitX & 255;
		unitY = unitY & 255;
		unitZ = unitZ & 255;

		final float u = FishyNoise.fade(x);
		final float v = FishyNoise.fade(y);
		final float w = FishyNoise.fade(z);

		final float grad000 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + this.perm[unitZ]]] & 15], x, y, z);
		final float grad100 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + this.perm[unitZ]]] & 15], x - 1, y, z);
		final float grad010 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + 1 + this.perm[unitZ]]] & 15], x, y - 1, z);
		final float grad110 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + 1 + this.perm[unitZ]]] & 15], x - 1, y - 1, z);
		final float grad001 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + this.perm[unitZ + 1]]] & 15], x, y, z - 1);
		final float grad101 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + this.perm[unitZ + 1]]] & 15], x - 1, y, z - 1);
		final float grad011 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + 1 + this.perm[unitZ + 1]]] & 15], x, y - 1, z - 1);
		final float grad111 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + 1 + this.perm[unitZ + 1]]] & 15], x - 1, y - 1, z - 1);

		return FishyNoise.lerp(FishyNoise.lerp(FishyNoise.lerp(grad000, grad100, u), FishyNoise.lerp(grad010, grad110, u), v), FishyNoise.lerp(FishyNoise.lerp(grad001, grad101, u), FishyNoise.lerp(grad011, grad111, u), v), w);
	}

}
