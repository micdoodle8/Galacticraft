package micdoodle8.mods.galacticraft.planets.venus.tick;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.tile.SolarModuleNetwork;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySolarTransmitter;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VenusTickHandlerServer
{
    private static final List<SolarModuleNetwork> solarModuleNetworks = Lists.newArrayList();
    public static LinkedList<TileEntitySolarTransmitter> solarTransmitterUpdates = new LinkedList<>();

    public static void addSolarNetwork(SolarModuleNetwork network)
    {
        solarModuleNetworks.add(network);
    }

    public static void removeSolarNetwork(SolarModuleNetwork network)
    {
        solarModuleNetworks.remove(network);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        MinecraftServer server = GCCoreUtil.getServer();
        //Prevent issues when clients switch to LAN servers
        if (server == null)
        {
            return;
        }

        if (event.phase == TickEvent.Phase.END)
        {
            for (SolarModuleNetwork network : new ArrayList<>(solarModuleNetworks))
            {
                if (!network.getTransmitters().isEmpty())
                {
//                    network.tickEnd();
                }
                else
                {
                    solarModuleNetworks.remove(network);
                }
            }

            int maxPasses = 10;
            while (!solarTransmitterUpdates.isEmpty())
            {
                LinkedList<TileEntitySolarTransmitter> pass = new LinkedList<>();
                pass.addAll(solarTransmitterUpdates);
                solarTransmitterUpdates.clear();
                for (TileEntitySolarTransmitter newTile : pass)
                {
                    if (!newTile.isRemoved())
                    {
                        newTile.refresh();
                    }
                }

                if (--maxPasses <= 0)
                {
                    break;
                }
            }
        }
    }
}
