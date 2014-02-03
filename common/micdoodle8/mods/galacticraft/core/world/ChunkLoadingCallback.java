package micdoodle8.mods.galacticraft.core.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.core.GCLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

/**
 * ChunkLoadingCallback.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ChunkLoadingCallback implements LoadingCallback
{
	private static boolean loaded;
	private static HashMap<String, HashMap<Integer, HashSet<ChunkCoordinates>>> chunkLoaderList = new HashMap<String, HashMap<Integer, HashSet<ChunkCoordinates>>>();
	// private static HashMap<Integer, HashSet<IChunkLoader>> loadedChunks = new
	// HashMap<Integer, HashSet<IChunkLoader>>();

	private static boolean configLoaded;
	private static Configuration config;
	// private static boolean keepLoadedOffline;
	private static boolean loadOnLogin;

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for (Ticket ticket : tickets)
		{
			NBTTagCompound nbt = ticket.getModData();

			if (nbt != null)
			{
				int tileX = nbt.getInteger("ChunkLoaderTileX");
				int tileY = nbt.getInteger("ChunkLoaderTileY");
				int tileZ = nbt.getInteger("ChunkLoaderTileZ");
				TileEntity tile = world.getBlockTileEntity(tileX, tileY, tileZ);

				if (tile instanceof IChunkLoader)
				{
					((IChunkLoader) tile).onTicketLoaded(ticket, false);
				}
			}
		}
	}

	public static void loadConfig(File file)
	{
		if (!ChunkLoadingCallback.configLoaded)
		{
			ChunkLoadingCallback.config = new Configuration(file);
		}

		try
		{
			// keepLoadedOffline = config.get("CHUNKLOADING",
			// "OfflineKeepLoaded", true,
			// "Set to false if you want each player's chunk loaders to unload when they log out.").getBoolean(true);
			ChunkLoadingCallback.loadOnLogin = ChunkLoadingCallback.config.get("CHUNKLOADING", "LoadOnLogin", true, "If you don't want each player's chunks to load when they log in, set to false.").getBoolean(true);
		}
		catch (final Exception e)
		{
			GCLog.severe("Problem loading chunkloading config (\"core.conf\")");
		}
		finally
		{
			if (ChunkLoadingCallback.config.hasChanged())
			{
				ChunkLoadingCallback.config.save();
			}

			ChunkLoadingCallback.configLoaded = true;
		}
	}

	public static void addToList(World world, int x, int y, int z, String playerName)
	{
		HashMap<Integer, HashSet<ChunkCoordinates>> dimensionMap = ChunkLoadingCallback.chunkLoaderList.get(playerName);

		if (dimensionMap == null)
		{
			dimensionMap = new HashMap<Integer, HashSet<ChunkCoordinates>>();
			ChunkLoadingCallback.chunkLoaderList.put(playerName, dimensionMap);
		}

		HashSet<ChunkCoordinates> chunkLoaders = dimensionMap.get(world.provider.dimensionId);

		if (chunkLoaders == null)
		{
			chunkLoaders = new HashSet<ChunkCoordinates>();
		}

		chunkLoaders.add(new ChunkCoordinates(x, y, z));
		dimensionMap.put(world.provider.dimensionId, chunkLoaders);
		ChunkLoadingCallback.chunkLoaderList.put(playerName, dimensionMap);
	}

	public static void forceChunk(Ticket ticket, World world, int x, int y, int z, String playerName)
	{
		ChunkLoadingCallback.addToList(world, x, y, z, playerName);
		ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(x >> 4, z >> 4);
		ForgeChunkManager.forceChunk(ticket, chunkPos);
		//
		// TileEntity tile = world.getBlockTileEntity(x, y, z);
		//
		// if (tile instanceof IChunkLoader)
		// {
		// IChunkLoader chunkLoader = (IChunkLoader) tile;
		// int dimID = world.provider.dimensionId;
		//
		// HashSet<IChunkLoader> chunkList = loadedChunks.get(dimID);
		//
		// if (chunkList == null)
		// {
		// chunkList = new HashSet<IChunkLoader>();
		// }
		//
		// ForgeChunkManager.forceChunk(ticket, chunkPos);
		// chunkList.add(chunkLoader);
		// loadedChunks.put(dimID, chunkList);
		// }
	}

	public static void save(WorldServer world)
	{
		try
		{
			File saveDir = ChunkLoadingCallback.getSaveDir();

			if (saveDir != null)
			{
				File saveFile = new File(saveDir, "chunkloaders.dat");

				if (!saveFile.exists())
				{
					saveFile.createNewFile();
				}

				DataOutputStream dataStream = new DataOutputStream(new FileOutputStream(saveFile));
				dataStream.writeInt(ChunkLoadingCallback.chunkLoaderList.size());

				for (Entry<String, HashMap<Integer, HashSet<ChunkCoordinates>>> playerEntry : ChunkLoadingCallback.chunkLoaderList.entrySet())
				{
					dataStream.writeUTF(playerEntry.getKey());
					dataStream.writeInt(playerEntry.getValue().size());

					for (Entry<Integer, HashSet<ChunkCoordinates>> dimensionEntry : playerEntry.getValue().entrySet())
					{
						dataStream.writeInt(dimensionEntry.getKey());
						dataStream.writeInt(dimensionEntry.getValue().size());

						for (ChunkCoordinates coords : dimensionEntry.getValue())
						{
							dataStream.writeInt(coords.posX);
							dataStream.writeInt(coords.posY);
							dataStream.writeInt(coords.posZ);
						}
					}
				}

				dataStream.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static File getSaveDir()
	{
		if (DimensionManager.getWorld(0) != null)
		{
			File saveDir = new File(DimensionManager.getCurrentSaveRootDirectory(), "galacticraft");

			if (!saveDir.exists())
			{
				saveDir.mkdirs();
			}

			return saveDir;
		}

		return null;
	}

	public static void load(WorldServer world)
	{
		if (ChunkLoadingCallback.loaded)
		{
			return;
		}

		try
		{
			File saveDir = ChunkLoadingCallback.getSaveDir();

			if (saveDir != null)
			{
				if (!saveDir.exists())
				{
					saveDir.mkdirs();
				}

				File saveFile = new File(saveDir, "chunkloaders.dat");

				if (saveFile.exists())
				{
					DataInputStream dataStream = new DataInputStream(new FileInputStream(saveFile));

					int playerCount = dataStream.readInt();

					for (int l = 0; l < playerCount; l++)
					{
						String ownerName = dataStream.readUTF();

						int mapSize = dataStream.readInt();
						HashMap<Integer, HashSet<ChunkCoordinates>> dimensionMap = new HashMap<Integer, HashSet<ChunkCoordinates>>();

						for (int i = 0; i < mapSize; i++)
						{
							int dimensionID = dataStream.readInt();
							HashSet<ChunkCoordinates> coords = new HashSet<ChunkCoordinates>();
							dimensionMap.put(dimensionID, coords);
							int coordSetSize = dataStream.readInt();

							for (int j = 0; j < coordSetSize; j++)
							{
								coords.add(new ChunkCoordinates(dataStream.readInt(), dataStream.readInt(), dataStream.readInt()));
							}
						}

						ChunkLoadingCallback.chunkLoaderList.put(ownerName, dimensionMap);
					}

					dataStream.close();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ChunkLoadingCallback.loaded = true;
	}

	public static void onPlayerLogin(EntityPlayer player)
	{
		for (Entry<String, HashMap<Integer, HashSet<ChunkCoordinates>>> playerEntry : ChunkLoadingCallback.chunkLoaderList.entrySet())
		{
			if (player.username.equals(playerEntry.getKey()))
			{
				for (Entry<Integer, HashSet<ChunkCoordinates>> dimensionEntry : playerEntry.getValue().entrySet())
				{
					int dimID = dimensionEntry.getKey();

					if (ChunkLoadingCallback.loadOnLogin)
					{
						MinecraftServer.getServer().worldServerForDimension(dimID);
					}
				}
			}
		}
	}

	public static void onPlayerLogout(EntityPlayer player)
	{
		// if (!keepLoadedOffline)
		// {
		// for (Entry<Integer, HashSet<IChunkLoader>> dimEntry :
		// loadedChunks.entrySet())
		// {
		// int dimID = dimEntry.getKey();
		//
		// for (IChunkLoader loader : new
		// ArrayList<IChunkLoader>(dimEntry.getValue()))
		// {
		// World world = loader.getWorldObj();
		//
		// ChunkCoordinates coords = loader.getCoords();
		// TileEntity tile = world.getBlockTileEntity(coords.posX, coords.posY,
		// coords.posZ);
		//
		// if (tile != null && tile.equals(loader))
		// {
		// Chunk chunkAt = world.getChunkFromChunkCoords(coords.posX >> 4,
		// coords.posZ >> 4);
		// boolean foundOtherLoader = false;
		//
		// for (Object o : chunkAt.chunkTileEntityMap.values())
		// {
		// TileEntity otherTile = (TileEntity) o;
		//
		// if (otherTile != null && !otherTile.equals(tile))
		// {
		// if (otherTile instanceof IChunkLoader)
		// {
		// IChunkLoader otherLoader = (IChunkLoader) otherTile;
		//
		// if (!otherLoader.getOwnerName().equals(loader.getOwnerName()))
		// {
		// HashMap<Integer, HashSet<ChunkCoordinates>> otherDimMap =
		// chunkLoaderList.get(loader.getOwnerName());
		//
		// if (otherDimMap != null)
		// {
		// HashSet<ChunkCoordinates> otherLoaders = otherDimMap.get(dimID);
		//
		// if (otherLoaders != null && otherLoaders.contains(otherLoader))
		// {
		// foundOtherLoader = true;
		// break;
		// }
		// }
		// }
		// }
		// }
		// }
		//
		// if (!foundOtherLoader)
		// {
		// ForgeChunkManager.unforceChunk(loader.getTicket(), new
		// ChunkCoordIntPair(coords.posX >> 4, coords.posZ >> 4));
		// dimEntry.getValue().remove(loader);
		// }
		// }
		// else
		// {
		// dimEntry.getValue().remove(loader);
		//
		// HashMap<Integer, HashSet<ChunkCoordinates>> dimMap =
		// chunkLoaderList.get(player.username);
		//
		// if (dimMap != null)
		// {
		// HashSet<ChunkCoordinates> coordSet = dimMap.get(dimID);
		//
		// if (coordSet != null)
		// {
		// coordSet.remove(loader.getCoords());
		// }
		//
		// dimm
		// chunkLoaderList.put(player.username, dimMap);
		// }
		// }
		// }
		// }
		// }
	}
}
