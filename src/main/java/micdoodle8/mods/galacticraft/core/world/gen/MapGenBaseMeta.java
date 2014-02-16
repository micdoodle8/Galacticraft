package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * GCCoreMapGenBaseMeta.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MapGenBaseMeta
{
	protected int range = 8;
	protected Random rand = new Random();
	protected World worldObj;

	public void generate(IChunkProvider par1IChunkProvider, World par2World, int par3, int par4, Block[] par5ArrayOfByte, byte[] metaArray)
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

	protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, Block[] par6ArrayOfByte, byte[] metaArray)
	{
	}
}
