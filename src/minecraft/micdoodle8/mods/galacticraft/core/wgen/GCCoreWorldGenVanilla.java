package micdoodle8.mods.galacticraft.core.wgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class GCCoreWorldGenVanilla implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		final List<Integer> wordList = new ArrayList<Integer>();

		for (final Integer i : GCCoreConfigManager.oreGenDimensions)
		{
			wordList.add(i);
		}

		for (final int dimID : wordList)
		{
			if (world.provider.dimensionId == dimID)
			{
				if (!GCCoreConfigManager.disableCopperEarth)
				{
					this.generateSurface(10 * GCCoreConfigManager.oreGenFactor, GCCoreBlocks.blockOres.blockID, 8, random, 0, chunkX, chunkZ, world, 120);
				}

				if (!GCCoreConfigManager.disableAluminiumEarth)
				{
					this.generateSurface(5 * GCCoreConfigManager.oreGenFactor, GCCoreBlocks.blockOres.blockID, 12, random, 1, chunkX, chunkZ, world, 30);
				}

				if (!GCCoreConfigManager.disableTitaniumEarth)
				{
					this.generateSurface(3 * GCCoreConfigManager.oreGenFactor, GCCoreBlocks.blockOres.blockID, 7, random, 2, chunkX, chunkZ, world, 20);
				}
			}
		}
	}

	private void generateSurface(int amount, int blockID, int amountOfBlocks, Random random, int metadata, int chunkX, int chunkZ, World world, int maxYCoord)
	{
        for (int var5 = 0; var5 < amount; ++var5)
        {
    		final int Xcoord = chunkX * 16 + random.nextInt(16);
    		final int Ycoord = random.nextInt(maxYCoord);
    		final int Zcoord = chunkZ * 16 + random.nextInt(16);

    		new GCCoreWorldGenMinableMeta(blockID, amountOfBlocks, metadata, true, Block.stone.blockID, 0).generate(world, random, Xcoord, Ycoord, Zcoord);
        }
	}
}
