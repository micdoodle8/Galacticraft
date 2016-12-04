package micdoodle8.mods.galacticraft.planets;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.MarsModuleClient;

import java.util.List;

public class PlanetsProxyClient extends PlanetsProxy
{
    private List<Item> itemsToRegisterJson = Lists.newArrayList();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        OBJLoader.instance.addDomain(GalacticraftPlanets.ASSET_PREFIX);
        GalacticraftPlanets.clientModules.add(new MarsModuleClient());
        GalacticraftPlanets.clientModules.add(new AsteroidsModuleClient());

        super.preInit(event);

        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules)
        {
            module.preInit(event);
        }

        for (Item toReg : itemsToRegisterJson)
        {
            ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, toReg);
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
