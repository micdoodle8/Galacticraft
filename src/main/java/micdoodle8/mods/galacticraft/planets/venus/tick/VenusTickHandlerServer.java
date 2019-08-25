package micdoodle8.mods.galacticraft.planets.venus.tick;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.planets.venus.tile.SolarModuleNetwork;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySolarTransmitter;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VenusTickHandlerServer
{
    private static List<SolarModuleNetwork> solarModuleNetworks = Lists.newArrayList();
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
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        //Prevent issues when clients switch to LAN servers
        if (server == null)
        {
            return;
        }

        if (event.phase == Phase.END)
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
                    if (!newTile.isInvalid())
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
