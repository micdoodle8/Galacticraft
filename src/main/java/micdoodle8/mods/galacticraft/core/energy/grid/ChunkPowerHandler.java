package micdoodle8.mods.galacticraft.core.energy.grid;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

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

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event)
    {
        if (!event.getWorld().isRemote && event.getChunk() != null)
        {
            try
            {
                ArrayList<Object> tileList = Lists.newArrayList();
                tileList.addAll(event.getChunk().getTileEntityMap().values());

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

            if (ConfigManagerCore.retrogenOil && GCCoreUtil.getDimensionID(event.getWorld()) == 0)
            {
                EventHandlerGC.retrogenOil(event.getWorld(), event.getChunk());
            }
        }
    }
}
