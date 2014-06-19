package micdoodle8.mods.galacticraft.planets.asteroids;

import java.io.File;
import java.util.List;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.CreativeTabGC;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.TeleportTypeAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.player.AsteroidsPlayerHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.util.AsteroidsUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class AsteroidsModule implements IPlanetsModule
{
	public static Planet planetAsteroids;

	public static final String ASSET_PREFIX = "galacticraftasteroids";
	public static final String TEXTURE_DOMAIN = AsteroidsModule.ASSET_PREFIX + ":";
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		new ConfigManagerAsteroids(new File(event.getModConfigurationDirectory(), "Galacticraft/asteroids.conf"));
		AsteroidsPlayerHandler playerHandler = new AsteroidsPlayerHandler();
		MinecraftForge.EVENT_BUS.register(playerHandler);
		FMLCommonHandler.instance().bus().register(playerHandler);
		AsteroidBlocks.initBlocks();
		AsteroidBlocks.registerBlocks();
		AsteroidsItems.initItems();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		this.registerEntities();

		AsteroidsModule.planetAsteroids = new Planet("asteroids").setParentGalaxy(GalacticraftCore.galaxyBlockyWay);
		AsteroidsModule.planetAsteroids.setDimensionInfo(ConfigManagerAsteroids.dimensionIDAsteroids, WorldProviderAsteroids.class);
		
		GalaxyRegistry.registerPlanet(AsteroidsModule.planetAsteroids);
		GalacticraftRegistry.registerTeleportType(WorldProviderAsteroids.class, new TeleportTypeAsteroids());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
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
		AsteroidsUtil.registerAsteroidsNonMobEntity(EntitySmallAsteroid.class, "SmallAsteroidGC", ConfigManagerAsteroids.idEntitySmallAsteroid, 150, 1, true);
		AsteroidsUtil.registerAsteroidsNonMobEntity(EntityGrapple.class, "GrappleHookGC", ConfigManagerAsteroids.idEntityGrappleHook, 150, 1, true);
		AsteroidsUtil.registerAsteroidsNonMobEntity(EntityTier3Rocket.class, "Tier3RocketGC", ConfigManagerAsteroids.idEntityTier3Rocket, 150, 1, false);
	}
	
	private void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityBeamReflector.class, "Beam Reflector");
		GameRegistry.registerTileEntity(TileEntityBeamReceiver.class, "Beam Receiver");
		GameRegistry.registerTileEntity(TileEntityShortRangeTelepad.class, "Short Range Telepad");
	}
}
