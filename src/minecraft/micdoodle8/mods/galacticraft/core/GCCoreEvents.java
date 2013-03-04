package micdoodle8.mods.galacticraft.core;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreTeleporter;
import net.minecraft.block.BlockSapling;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;

public class GCCoreEvents
{
	@ForgeSubscribe
	public void growTreeBonemeal(BonemealEvent event)
	{
		if (event.world.getBlockId(event.X, event.Y, event.Z) == GCCoreBlocks.sapling.blockID)
		{
			if (!event.world.isRemote)
			{
                ((BlockSapling)GCCoreBlocks.sapling).growTree(event.world, event.X, event.Y, event.Z, event.world.rand);
                --event.entityPlayer.inventory.getCurrentItem().stackSize;
				event.setResult(Result.DENY);
			}
		}
		
		event.setResult(Result.DENY);
	}
	
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		if (event.world instanceof WorldServer)
		{
			((WorldServer)event.world).customTeleporters.add(new GCCoreTeleporter((WorldServer)event.world));
		}
	}
	
	@ForgeSubscribe
	public void populate(PopulateChunkEvent.Post event) 
	{
		boolean doGen = TerrainGen.populate(event.chunkProvider, event.world, event.rand, event.chunkX, event.chunkX, event.hasVillageGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM);

		if (!doGen || GCCoreConfigManager.disableOilGen) 
		{
			return;
		}

		int worldX = event.chunkX << 4;
		int worldZ = event.chunkZ << 4;

		this.doPopulate(event.world, event.rand, worldX, worldZ);
	}

	public static void doPopulate(World world, Random rand, int x, int z) 
	{
		BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x + 16, z + 16);

		if (biomegenbase.biomeID == BiomeGenBase.sky.biomeID || biomegenbase.biomeID == BiomeGenBase.hell.biomeID) 
		{
			return;
		}

		boolean flag1 = rand.nextDouble() <= (0.08 * GCCoreConfigManager.oilGenFactor);
		boolean flag2 = rand.nextDouble() <= (0.08 * GCCoreConfigManager.oilGenFactor);

		if (flag1 || flag2) 
		{
			int cx = x, cy = 20 + rand.nextInt(10), cz = z;

			int r = 1 + rand.nextInt(2);

			int r2 = r * r;

			for (int bx = -r; bx <= r; bx++) 
			{
				for (int by = -r; by <= r; by++) 
				{
					for (int bz = -r; bz <= r; bz++) 
					{
						int d2 = bx * bx + by * by + bz * bz;

						if (d2 <= r2) 
						{
							world.setBlockWithNotify(bx + cx, by + cy, bz + cz, GCCoreBlocks.crudeOilStill.blockID);
						}
					}
				}
			}
		}
	}
}
