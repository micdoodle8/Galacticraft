package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.tick.VenusTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.venus.tile.SolarModuleNetwork;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class PlanetsProxy {
    public void init(FMLCommonSetupEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules)
        {
            module.init(event);
        }
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules)
        {
            module.serverStarting(event);
        }
    }

    public void serverInit(FMLServerStartedEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules)
        {
            module.serverInit(event);
        }
    }

    public void enqueueIMC(InterModEnqueueEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules) {
            module.enqueueIMC(event);
        }
    }


//    @Override
//    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        for (IPlanetsModule module : GalacticraftPlanets.commonModules)
//        {
//            List<Integer> guiIDs = new ArrayList<Integer>();
//            module.getGuiIDs(guiIDs);
//            if (guiIDs.contains(ID))
//            {
//                return module.getGuiElement(LogicalSide.SERVER, ID, player, world, x, y, z);
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules)
//        {
//            List<Integer> guiIDs = new ArrayList<Integer>();
//            module.getGuiIDs(guiIDs);
//            if (guiIDs.contains(ID))
//            {
//                return module.getGuiElement(LogicalSide.CLIENT, ID, player, world, x, y, z);
//            }
//        }
//
//        return null;
//    }

//    public void postRegisterItem(Item item)
//    {
//    }

    public void unregisterNetwork(SolarModuleNetwork solarNetwork)
    {
        if (GCCoreUtil.getEffectiveSide().isServer())
        {
            VenusTickHandlerServer.removeSolarNetwork(solarNetwork);
        }
    }

    public void registerNetwork(SolarModuleNetwork solarNetwork)
    {
        if (GCCoreUtil.getEffectiveSide().isServer())
        {
            VenusTickHandlerServer.addSolarNetwork(solarNetwork);
        }
    }
}
