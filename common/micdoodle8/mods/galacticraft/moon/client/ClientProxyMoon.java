package micdoodle8.mods.galacticraft.moon.client;

import java.util.EnumSet;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.CommonProxyMoon;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxyMoon extends CommonProxyMoon implements IGalacticraftSubModClient
{
    @Override
    public void init(FMLInitializationEvent event)
    {
        GalacticraftCore.registerClientSubMod(this);
        TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
    }

    @Override
    public void registerRenderInformation()
    {
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
    }

    public static class TickHandlerClient implements ITickHandler
    {
        @Override
        public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
            final Minecraft minecraft = FMLClientHandler.instance().getClient();

            final WorldClient world = minecraft.theWorld;

            if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
                if (world != null && world.provider instanceof GCMoonWorldProvider)
                {
                    if (world.provider.getSkyRenderer() == null)
                    {
                        world.provider.setSkyRenderer(new GCMoonSkyProvider());
                    }
                }
            }
        }

        @Override
        public void tickEnd(EnumSet<TickType> type, Object... tickData)
        {
        }

        @Override
        public String getLabel()
        {
            return "Galacticraft Moon Client";
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.CLIENT);
        }
    }

    @Override
    public String getDimensionName()
    {
        return "Moon";
    }

    @Override
    public String getPlanetSpriteDirectory()
    {
        return "/micdoodle8/mods/galacticraft/moon/client/planets/";
    }

    @Override
    public IPlanetSlotRenderer getSlotRenderer()
    {
        return new GCMoonSlotRenderer();
    }

    @Override
    public IMapPlanet getPlanetForMap()
    {
        return null;
    }

    @Override
    public IMapPlanet[] getChildMapPlanets()
    {
        return null;
    }

    @Override
    public String getPathToMusicFile()
    {
        return null;
    }
}
