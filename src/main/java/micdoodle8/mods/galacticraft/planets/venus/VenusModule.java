package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.planets.venus.dimension.WorldProviderVenus;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySpout;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class VenusModule implements IPlanetsModule
{
    public static Planet planetVenus;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
//        MinecraftForge.EVENT_BUS.register(new EventHandlerVenus());

        VenusBlocks.initBlocks();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        this.registerMicroBlocks();

//        GalacticraftCore.packetPipeline.addDiscriminator(8, PacketSimpleVenus.class);

        this.registerTileEntities();
        this.registerCreatures();
        this.registerOtherEntities();

        VenusModule.planetVenus = (Planet) new Planet("venus").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(2.0F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.75F, 0.75F)).setRelativeOrbitTime(0.61527929901423877327491785323111F);
        VenusModule.planetVenus.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/venus.png"));
        VenusModule.planetVenus.setDimensionInfo(ConfigManagerVenus.dimensionIDVenus, WorldProviderVenus.class).setTierRequired(4);

        GalaxyRegistry.registerPlanet(VenusModule.planetVenus);
        GalacticraftRegistry.registerTeleportType(WorldProviderVenus.class, new TeleportTypeMars());
//        GalacticraftRegistry.registerRocketGui(WorldProviderMars.class, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/marsRocketGui.png"));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
//        RecipeManagerVenus.loadRecipes();
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
    }

    @Override
    public void serverInit(FMLServerStartedEvent event)
    {

    }

    private void registerMicroBlocks()
    {
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntitySpout.class, "Venus Spout");
    }

    public void registerCreatures()
    {
        this.registerGalacticraftCreature(EntityJuicer.class, "juicer", ColorUtil.to32BitColor(180, 180, 50, 0), ColorUtil.to32BitColor(255, 0, 2, 0));
    }

    public void registerOtherEntities()
    {
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public Configuration getConfiguration()
    {
        return ConfigManagerVenus.config;
    }

    @Override
    public void syncConfig()
    {
        ConfigManagerVenus.syncConfig(false, false);
    }

    public void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int back, int fore)
    {
        VenusModule.registerGalacticraftNonMobEntity(var0, var1, 80, 3, true);
        int nextEggID = GCCoreUtil.getNextValidEggID();
        if (nextEggID < 65536)
        {
            EntityList.idToClassMapping.put(nextEggID, var0);
            EntityList.classToIDMapping.put(var0, nextEggID);
            EntityList.entityEggs.put(nextEggID, new EntityList.EntityEggInfo(nextEggID, back, fore));
        }
    }

    public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            LanguageRegistry.instance().addStringLocalization("entity.galacticraftplanets." + var1 + ".name", GCCoreUtil.translate("entity." + var1 + ".name"));
            LanguageRegistry.instance().addStringLocalization("entity.GalacticraftPlanets." + var1 + ".name", GCCoreUtil.translate("entity." + var1 + ".name"));
        }
        EntityRegistry.registerModEntity(var0, var1, GCCoreUtil.nextInternalID(), GalacticraftPlanets.instance, trackingDistance, updateFreq, sendVel);
    }
}
