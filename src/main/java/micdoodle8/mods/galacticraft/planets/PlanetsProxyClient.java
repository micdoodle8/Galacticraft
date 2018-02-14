package micdoodle8.mods.galacticraft.planets;

import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.MarsModuleClient;
import micdoodle8.mods.galacticraft.planets.venus.VenusModuleClient;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.List;

public class PlanetsProxyClient extends PlanetsProxy
{
    private List<Item> itemsToRegisterJson = Lists.newArrayList();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        OBJLoaderGC.instance.addDomain(GalacticraftPlanets.ASSET_PREFIX);
        GalacticraftPlanets.clientModules.add(new MarsModuleClient());
        GalacticraftPlanets.clientModules.add(new AsteroidsModuleClient());
        GalacticraftPlanets.clientModules.add(new VenusModuleClient());

        super.preInit(event);

        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules)
        {
            module.preInit(event);
        }
    }

    @Override
    public void registerVariants()
    {
        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules)
        {
            module.registerVariants();
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);

        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules)
        {
            module.init(event);
        }

        for (Item toReg : itemsToRegisterJson)
        {
            ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, toReg);
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);

        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules)
        {
            module.postInit(event);
        }
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
        super.serverStarting(event);
    }

    @Override
    public void postRegisterItem(Item item)
    {
        if (!item.getHasSubtypes())
        {
            itemsToRegisterJson.add(item);
        }
    }
}
