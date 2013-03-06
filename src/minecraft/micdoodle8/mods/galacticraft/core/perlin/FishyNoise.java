package micdoodle8.mods.galacticraft.core.perlin;

import java.util.Random;

public class FishyNoise {

	int[] perm = new int[512];
	
	public double[][] grad2d = new double[][] {{1, 0}, {.9239, .3827}, {.707107, 0.707107}, {.3827, .9239}, 
			{0, 1}, {-.3827, .9239}, {-.707107, 0.707107},{-.9239, .3827}, 
			{-1, 0}, {-.9239, -.3827}, {-.707107, -0.707107}, {-.3827, -.9239},
			{0, -1}, {.3827, -.9239}, {.707107, -0.707107},{.9239, -.3827}};
	
	public int[][] grad3d = new int[][] {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, 
			{1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, 
			{0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1},
			{1, 1, 0}, {-1, 1, 0}, {0, -1, 1}, {0, -1, -1}};
	
	public FishyNoise(long seed)
	{
		Random rand = new Random(seed);
		for(int i = 0; i < 256; i++)
		{
			perm[i] = i; //Fill up the random array with numbers 0-256
		}
		
		for(int i = 0; i < 256; i++) //Shuffle those numbers for the random effect
		{
			int j = rand.nextInt(256);
			perm[i] = perm[i] ^ perm[j];
			perm[j] = perm[i] ^ perm[j];
			perm[i] = perm[i] ^ perm[j];
		}
		
		for(int i = 0; i < 256; i++)
		{
			perm[i + 256] = perm[i];
		}
	}
	
	private static double lerp(double x, double y, double n)
	{
		return x + n * (y - x);
	}
	
	private static int fastFloor(double x)
	{
		return x > 0 ? (int)x : (int)x - 1;
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
		return (grad3[0] * x) + (grad3[1] * y) + (grad3[2] * z);
	}
	
	public double noise2d(double x, double y)
	{
		int largeX = fastFloor(x);
		int largeY = fastFloor(y);
		x -= largeX;
		y -= largeY;
		largeX = largeX & 255;
		largeY = largeY & 255;
		
		double u = fade(x);
		double v = fade(y);
		
		double grad00 = dot2(grad2d[(perm[largeX + perm[largeY]]) & 15], x, y);
		double grad01 = dot2(grad2d[(perm[largeX + perm[largeY + 1]]) & 15], x, y - 1);
		double grad11 = dot2(grad2d[(perm[largeX + 1 + perm[largeY + 1]]) & 15], x - 1, y - 1);
		double grad10 = dot2(grad2d[(perm[largeX + 1 + perm[largeY]])  & 15], x - 1, y);

		double lerpX0 = lerp(grad00, grad10, u);
		double lerpX1 = lerp(grad01, grad11, u);
		return lerp(lerpX0, lerpX1, v);
	}
	
	public double noise3d(double x, double y, double z)
	{
		int unitX = fastFloor(x);
		int unitY = fastFloor(y);
		int unitZ = fastFloor(z);
		
		x -= unitX;
		y -= unitY;
		z -= unitZ;
		
		unitX = unitX & 255;
		unitY = unitY & 255;
		unitZ = unitZ & 255;
		
		double u = fade(x);
		double v = fade(y);
		double w = fade(z);
		
		double grad000 = dot3(grad3d[perm[unitX + perm[unitY + perm[unitZ]]] & 15], x, y, z);
		double grad100 = dot3(grad3d[perm[unitX + 1 + perm[unitY + perm[unitZ]]] & 15], x - 1, y, z);
		double grad010 = dot3(grad3d[perm[unitX + perm[unitY + 1 + perm[unitZ]]] & 15], x, y - 1, z);
		double grad110 = dot3(grad3d[perm[unitX + 1 + perm[unitY + 1 + perm[unitZ]]] & 15], x - 1, y - 1, z);
		double grad001 = dot3(grad3d[perm[unitX + perm[unitY + perm[unitZ + 1]]] & 15], x, y, z - 1);
		double grad101 = dot3(grad3d[perm[unitX + 1 + perm[unitY + perm[unitZ + 1]]] & 15], x - 1, y, z - 1);
		double grad011 = dot3(grad3d[perm[unitX + perm[unitY + 1 + perm[unitZ + 1]]] & 15], x, y - 1, z - 1);
		double grad111 = dot3(grad3d[perm[unitX + 1 + perm[unitY + 1 + perm[unitZ + 1]]] & 15], x - 1, y - 1, z - 1);
		
		return lerp(
				lerp(
				  lerp(grad000, grad100, u), 
				  lerp(grad010, grad110, u), v), 
				lerp(
				  lerp(grad001, grad101, u), 
				  lerp(grad011, grad111, u), v), w);
	}
	
}
