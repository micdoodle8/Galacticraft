package micdoodle8.mods.galacticraft.planets.asteroids.tick;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class AsteroidsTickHandlerServer
{
    public static ShortRangeTelepadHandler spaceRaceData = null;

    public static void restart()
    {
        spaceRaceData = null;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
    	MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    	//Prevent issues when clients switch to LAN servers
    	if (server == null) return;

    	if (event.phase == TickEvent.Phase.START)
        {
            if (AsteroidsTickHandlerServer.spaceRaceData == null)
            {
                World world = server.worldServerForDimension(0);
                AsteroidsTickHandlerServer.spaceRaceData = (ShortRangeTelepadHandler) world.mapStorage.loadData(ShortRangeTelepadHandler.class, ShortRangeTelepadHandler.saveDataID);

                if (AsteroidsTickHandlerServer.spaceRaceData == null)
                {
                    AsteroidsTickHandlerServer.spaceRaceData = new ShortRangeTelepadHandler(ShortRangeTelepadHandler.saveDataID);
                    world.mapStorage.setData(ShortRangeTelepadHandler.saveDataID, AsteroidsTickHandlerServer.spaceRaceData);
                }
            }
        }
    }
}
