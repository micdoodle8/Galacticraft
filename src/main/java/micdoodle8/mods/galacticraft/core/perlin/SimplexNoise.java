package micdoodle8.mods.galacticraft.core.perlin;

import java.util.Random;

/**
 * SimplexNoise.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class SimplexNoise
{
	int[] perm = new int[512];

	public int[][] grad2d = new int[][] { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 0 } };

	public SimplexNoise(long seed)
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

}
