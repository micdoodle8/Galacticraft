package micdoodle8.mods.galacticraft.core;

import java.lang.reflect.Field;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.world.WorldEvent;

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
	public void onBucketFill(FillBucketEvent event)
	{
		final ItemStack result = this.fillCustomBucket(event.world, event.target);

		if (result == null)
		{
			return;
		}

		event.result = result;
		event.setResult(Result.ALLOW);
	}

	public ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
	{
		Class buildCraftClass = null;

		int bcOilID1 = -1;
		int bcOilID2 = -1;
		Item bcOilBucket = null;

		try
		{
			if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
			{
				for (final Field f : buildCraftClass.getFields())
				{
					if (f.getName().equals("oilMoving"))
					{
						final Block block = (Block) f.get(null);

						bcOilID1 = block.blockID;
					}
					else if (f.getName().equals("oilStill"))
					{
						final Block block = (Block) f.get(null);

						bcOilID2 = block.blockID;
					}
					else if (f.getName().equals("bucketOil"))
					{
						final Item item = (Item) f.get(null);

						bcOilBucket = item;
					}
				}
			}
		}
		catch (final Throwable cnfe)
		{

		}

		final int blockID = world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);

		if ((blockID == bcOilID1 || blockID == bcOilID2 || blockID == GCCoreBlocks.crudeOilMoving.blockID || blockID == GCCoreBlocks.crudeOilStill.blockID) && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0 && bcOilBucket != null)
		{
			world.func_94571_i(pos.blockX, pos.blockY, pos.blockZ);

			return new ItemStack(bcOilBucket);
		}
		else
		{
			return null;
		}
	}

	@ForgeSubscribe
	public void populate(PopulateChunkEvent.Post event)
	{
		final boolean doGen = TerrainGen.populate(event.chunkProvider, event.world, event.rand, event.chunkX, event.chunkX, event.hasVillageGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM);

		if (!doGen || GCCoreConfigManager.disableOilGen)
		{
			return;
		}

		final int worldX = event.chunkX << 4;
		final int worldZ = event.chunkZ << 4;

		this.doPopulate(event.world, event.rand, worldX, worldZ);
	}

	public static void doPopulate(World world, Random rand, int x, int z)
	{
		final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x + 16, z + 16);

		if (biomegenbase.biomeID == BiomeGenBase.sky.biomeID || biomegenbase.biomeID == BiomeGenBase.hell.biomeID)
		{
			return;
		}

		final boolean flag1 = rand.nextDouble() <= 0.08 * GCCoreConfigManager.oilGenFactor;
		final boolean flag2 = rand.nextDouble() <= 0.08 * GCCoreConfigManager.oilGenFactor;

		if (flag1 || flag2)
		{
			final int cx = x, cy = 20 + rand.nextInt(10), cz = z;

			final int r = 1 + rand.nextInt(2);

			final int r2 = r * r;

			for (int bx = -r; bx <= r; bx++)
			{
				for (int by = -r; by <= r; by++)
				{
					for (int bz = -r; bz <= r; bz++)
					{
						final int d2 = bx * bx + by * by + bz * bz;

						if (d2 <= r2)
						{
							world.func_94575_c(bx + cx, by + cy, bz + cz, GCCoreBlocks.crudeOilStill.blockID);
						}
					}
				}
			}
		}
	}
}
