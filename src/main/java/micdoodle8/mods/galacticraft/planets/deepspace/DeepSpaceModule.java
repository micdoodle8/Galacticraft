package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.deepspace.dimension.WorldProviderDeepSpace;
import micdoodle8.mods.galacticraft.planets.deepspace.tile.TileEntityDeepStructure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class DeepSpaceModule implements IPlanetsModule
{
    public static Moon stationDeepSpace;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        DeepSpaceBlocks.initBlocks();
        DeepSpaceBlocks.registerBlocks();
        DeepSpaceBlocks.setHarvestLevels();

//        DeepSpaceItems.initItems();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        this.registerEntities();

//        RecipeManagerDeepSpace.loadRecipes();

        DeepSpaceModule.stationDeepSpace = new Moon("deep_space").setParentPlanet(GalacticraftCore.planetJupiter);
        DeepSpaceModule.stationDeepSpace.setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(23.5F, 23.5F)).setRelativeOrbitTime(1 / 0.02F);
        DeepSpaceModule.stationDeepSpace.setDimensionInfo(ConfigManagerCore.idDimensionOverworldOrbitStatic, WorldProviderDeepSpace.class).setTierRequired(3);
        DeepSpaceModule.stationDeepSpace.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/space_station.png"));
        DeepSpaceModule.stationDeepSpace.setAtmosphere(new AtmosphereInfo(false, false, false, 0.0F, 0.1F, 0.02F));
        GalaxyRegistry.registerMoon(DeepSpaceModule.stationDeepSpace);
//        GalacticraftRegistry.registerTeleportType(WorldProviderAsteroids.class, new TeleportTypeAsteroids());

        GalacticraftRegistry.registerRocketGui(WorldProviderDeepSpace.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/overworld_rocket_gui.png"));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
//        ChunkProviderDeepSpace.reset();
    }

    @Override
    public void serverInit(FMLServerStartedEvent event)
    {
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
//        if (side == Side.SERVER)
//        {
//            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
//
//            switch (ID)
//            {
//            }
//        }
//
        return null;
    }

    private void registerEntities()
    {
        this.registerCreatures();
        this.registerNonMobEntities();
        this.registerTileEntities();
    }

    private void registerCreatures()
    {
    }

    private void registerNonMobEntities()
    {
    }

    private void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityDeepStructure.class, "GC Deep Structural");
    }

    @Override
    public Configuration getConfiguration()
    {
        return ConfigManagerDeepSpace.config;
    }

    @Override
    public void syncConfig()
    {
        ConfigManagerDeepSpace.syncConfig(false, false);
    }
}
