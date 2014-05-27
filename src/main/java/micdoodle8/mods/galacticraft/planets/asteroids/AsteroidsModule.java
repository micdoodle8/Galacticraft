package micdoodle8.mods.galacticraft.planets.asteroids;

import java.io.File;
import java.util.List;

import micdoodle8.mods.galacticraft.core.util.CreativeTabGC;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.util.AsteroidsUtil;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;

public class AsteroidsModule implements IPlanetsModule
{
	public static CreativeTabGC asteroidsTab;

	public static final String ASSET_PREFIX = "galacticraftasteroids";
	public static final String TEXTURE_DOMAIN = AsteroidsModule.ASSET_PREFIX + ":";
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		new ConfigManagerAsteroids(new File(event.getModConfigurationDirectory(), "Galacticraft/asteroids.conf"));
		AsteroidBlocks.initBlocks();
		AsteroidBlocks.registerBlocks();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		AsteroidsModule.asteroidsTab = new CreativeTabGC(CreativeTabs.getNextID(), "GalacticraftAsteroids", Item.getItemFromBlock(AsteroidBlocks.blockWalkway), 0);
		this.registerEntities();
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
		AsteroidsUtil.registerAsteroidsNonMobEntity(EntitySmallAsteroid.class, "SmallAsteroid", ConfigManagerAsteroids.idEntitySmallAsteroid, 150, 1, true);
	}
	
	private void registerTileEntities()
	{
		
	}
}
