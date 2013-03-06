package micdoodle8.mods.galacticraft.core.perlin;

import java.util.Random;

public class SimplexNoise {
int[] perm = new int[512];
	
	public int[][] grad2d = new int[][] {{0, 0}, {0, 1}, {1, 1}, {1, 0}};
	
	public SimplexNoise(long seed)
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
	
	private static double dot2(int[] grad2, double x, double y)
	{
		return grad2[0] * x + grad2[1] * y;
	}
	
}
