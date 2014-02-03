package micdoodle8.mods.galacticraft.mars.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.core.world.gen.GCCoreWorldGenMinableMeta;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

/**
 * GCMarsBiomeDecorator.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsBiomeDecorator
{
	protected World currentWorld;
	protected Random rand;

	protected int chunkX;
	protected int chunkZ;

	protected WorldGenerator dirtGen;
	protected WorldGenerator deshGen;
	protected WorldGenerator tinGen;
	protected WorldGenerator copperGen;
	protected WorldGenerator ironGen;

	public GCMarsBiomeDecorator()
	{
		this.copperGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.marsBlock.blockID, 4, 0, true, GCMarsBlocks.marsBlock.blockID, 9);
		this.tinGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.marsBlock.blockID, 4, 1, true, GCMarsBlocks.marsBlock.blockID, 9);
		this.deshGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.marsBlock.blockID, 4, 2, true, GCMarsBlocks.marsBlock.blockID, 9);
		this.ironGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.marsBlock.blockID, 8, 3, true, GCMarsBlocks.marsBlock.blockID, 9);
		this.dirtGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.marsBlock.blockID, 32, 6, true, GCMarsBlocks.marsBlock.blockID, 9);
	}

	public void decorate(World world, Random random, int chunkX, int chunkZ)
	{
		if (this.currentWorld != null)
		{
			throw new RuntimeException("Already decorating!!");
		}
		else
		{
			this.currentWorld = world;
			this.rand = random;
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
			this.generateMars();
			this.currentWorld = null;
			this.rand = null;
		}
	}

	protected void generateOre(int amountPerChunk, WorldGenerator worldGenerator, int minY, int maxY)
	{
		for (int var5 = 0; var5 < amountPerChunk; ++var5)
		{
			final int var6 = this.chunkX + this.rand.nextInt(16);
			final int var7 = this.rand.nextInt(maxY - minY) + minY;
			final int var8 = this.chunkZ + this.rand.nextInt(16);
			worldGenerator.generate(this.currentWorld, this.rand, var6, var7, var8);
		}
	}

	protected void generateMars()
	{
		MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(this.currentWorld, this.rand, this.chunkX, this.chunkZ));
		this.generateOre(20, this.dirtGen, 0, 200);
		this.generateOre(15, this.deshGen, 0, 128);
		this.generateOre(26, this.copperGen, 0, 60);
		this.generateOre(23, this.tinGen, 0, 60);
		this.generateOre(20, this.ironGen, 0, 64);
		MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(this.currentWorld, this.rand, this.chunkX, this.chunkZ));
	}
}
