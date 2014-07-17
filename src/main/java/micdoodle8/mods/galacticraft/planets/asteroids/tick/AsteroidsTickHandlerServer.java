package micdoodle8.mods.galacticraft.planets.asteroids.tick;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import micdoodle8.mods.galacticraft.core.dimension.WorldDataSpaceRaces;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
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
        if (event.phase == TickEvent.Phase.START)
        {
            if (AsteroidsTickHandlerServer.spaceRaceData == null)
            {
                World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
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
