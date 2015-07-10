package micdoodle8.mods.galacticraft.planets;

import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PlanetsProxy implements IGuiHandler
{
    public void preInit(FMLPreInitializationEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            module.preInit(event);
        }
    }

    public void init(FMLInitializationEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            module.init(event);
        }
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            module.postInit(event);
        }
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            module.serverStarting(event);
        }
    }

    public void serverInit(FMLServerStartedEvent event)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            module.serverInit(event);
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            List<Integer> guiIDs = new ArrayList<Integer>();
            module.getGuiIDs(guiIDs);
            if (guiIDs.contains(ID))
            {
                return module.getGuiElement(Side.SERVER, ID, player, world, x, y, z);
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules.values())
        {
            List<Integer> guiIDs = new ArrayList<Integer>();
            module.getGuiIDs(guiIDs);
            if (guiIDs.contains(ID))
            {
                return module.getGuiElement(Side.CLIENT, ID, player, world, x, y, z);
            }
        }

        return null;
    }
}
