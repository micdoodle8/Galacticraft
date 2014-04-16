package micdoodle8.mods.galacticraft.api.transmission.core.grid;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;

import com.google.common.collect.Lists;

public class ChunkPowerHandler
{
	private static boolean initiated = false;

	public static void initiate()
	{
		if (!ChunkPowerHandler.initiated)
		{
			ChunkPowerHandler.initiated = true;
			MinecraftForge.EVENT_BUS.register(new ChunkPowerHandler());
		}
	}

	@SuppressWarnings("unchecked")
	@ForgeSubscribe
	public void onChunkLoad(ChunkEvent.Load event)
	{
		if (!event.world.isRemote && event.getChunk() != null)
		{
			try
			{
				ArrayList<Object> tileList = Lists.newArrayList();
				tileList.addAll(event.getChunk().chunkTileEntityMap.values());

				for (Object o : tileList)
				{
					if (o instanceof TileEntity)
					{
						TileEntity tile = (TileEntity) o;

						if (tile instanceof INetworkConnection)
						{
							((INetworkConnection) tile).refresh();
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
