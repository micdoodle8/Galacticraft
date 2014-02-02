package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Do not include this prefab class in your released mod download.
 */
public class GCCoreMapGenBaseMeta
{
	/** The number of Chunks to gen-check in any given direction. */
	protected int range = 8;

	/** The RNG used by the MapGen classes. */
	protected Random rand = new Random();

	/** This world object. */
	protected World worldObj;

	public void generate(IChunkProvider par1IChunkProvider, World par2World, int par3, int par4, short[] par5ArrayOfByte, byte[] metaArray)
	{
		final int var6 = this.range;
		this.worldObj = par2World;
		this.rand.setSeed(par2World.getSeed());
		final long var7 = this.rand.nextLong();
		final long var9 = this.rand.nextLong();

		for (int var11 = par3 - var6; var11 <= par3 + var6; ++var11)
		{
			for (int var12 = par4 - var6; var12 <= par4 + var6; ++var12)
			{
				final long var13 = var11 * var7;
				final long var15 = var12 * var9;
				this.rand.setSeed(var13 ^ var15 ^ par2World.getSeed());
				this.recursiveGenerate(par2World, var11, var12, par3, par4, par5ArrayOfByte, metaArray);
			}
		}
	}

	/**
	 * Recursively called by generate() (generate) and optionally by itself.
	 */
	protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, short[] par6ArrayOfByte, byte[] metaArray)
	{
	}
}
