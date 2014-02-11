package micdoodle8.mods.galacticraft.core.perlin;

import java.util.Random;

/**
 * FishyNoise.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class FishyNoise
{

	int[] perm = new int[512];

	public double[][] grad2d = new double[][] { { 1, 0 }, { .9239, .3827 }, { .707107, 0.707107 }, { .3827, .9239 }, { 0, 1 }, { -.3827, .9239 }, { -.707107, 0.707107 }, { -.9239, .3827 }, { -1, 0 }, { -.9239, -.3827 }, { -.707107, -0.707107 }, { -.3827, -.9239 }, { 0, -1 }, { .3827, -.9239 }, { .707107, -0.707107 }, { .9239, -.3827 } };

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

		for (int i = 0; i < 256; i++)
		{
			this.perm[i + 256] = this.perm[i];
		}
	}

	private static double lerp(double x, double y, double n)
	{
		return x + n * (y - x);
	}

	private static int fastFloor(double x)
	{
		return x > 0 ? (int) x : (int) x - 1;
	}

	private static double fade(double n)
	{
		return n * n * n * (n * (n * 6 - 15) + 10);
	}

	private static double dot2(double[] grad2, double x, double y)
	{
		return grad2[0] * x + grad2[1] * y;
	}

	private static double dot3(int[] grad3, double x, double y, double z)
	{
		return grad3[0] * x + grad3[1] * y + grad3[2] * z;
	}

	public double noise2d(double x, double y)
	{
		int largeX = FishyNoise.fastFloor(x);
		int largeY = FishyNoise.fastFloor(y);
		x -= largeX;
		y -= largeY;
		largeX = largeX & 255;
		largeY = largeY & 255;

		final double u = FishyNoise.fade(x);
		final double v = FishyNoise.fade(y);

		final double grad00 = FishyNoise.dot2(this.grad2d[this.perm[largeX + this.perm[largeY]] & 15], x, y);
		final double grad01 = FishyNoise.dot2(this.grad2d[this.perm[largeX + this.perm[largeY + 1]] & 15], x, y - 1);
		final double grad11 = FishyNoise.dot2(this.grad2d[this.perm[largeX + 1 + this.perm[largeY + 1]] & 15], x - 1, y - 1);
		final double grad10 = FishyNoise.dot2(this.grad2d[this.perm[largeX + 1 + this.perm[largeY]] & 15], x - 1, y);

		final double lerpX0 = FishyNoise.lerp(grad00, grad10, u);
		final double lerpX1 = FishyNoise.lerp(grad01, grad11, u);
		return FishyNoise.lerp(lerpX0, lerpX1, v);
	}

	public double noise3d(double x, double y, double z)
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

		final double u = FishyNoise.fade(x);
		final double v = FishyNoise.fade(y);
		final double w = FishyNoise.fade(z);

		final double grad000 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + this.perm[unitZ]]] & 15], x, y, z);
		final double grad100 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + this.perm[unitZ]]] & 15], x - 1, y, z);
		final double grad010 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + 1 + this.perm[unitZ]]] & 15], x, y - 1, z);
		final double grad110 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + 1 + this.perm[unitZ]]] & 15], x - 1, y - 1, z);
		final double grad001 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + this.perm[unitZ + 1]]] & 15], x, y, z - 1);
		final double grad101 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + this.perm[unitZ + 1]]] & 15], x - 1, y, z - 1);
		final double grad011 = FishyNoise.dot3(this.grad3d[this.perm[unitX + this.perm[unitY + 1 + this.perm[unitZ + 1]]] & 15], x, y - 1, z - 1);
		final double grad111 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + this.perm[unitY + 1 + this.perm[unitZ + 1]]] & 15], x - 1, y - 1, z - 1);

		return FishyNoise.lerp(FishyNoise.lerp(FishyNoise.lerp(grad000, grad100, u), FishyNoise.lerp(grad010, grad110, u), v), FishyNoise.lerp(FishyNoise.lerp(grad001, grad101, u), FishyNoise.lerp(grad011, grad111, u), v), w);
	}

}
