package micdoodle8.mods.galacticraft.mars;

import java.io.File;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.CreativeTabGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsTeleportType;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsWorldProvider;
import micdoodle8.mods.galacticraft.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.mars.entities.EntityProjectileTNT;
import micdoodle8.mods.galacticraft.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.mars.entities.EntitySludgeling;
import micdoodle8.mods.galacticraft.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.galacticraft.mars.entities.EntityTier2Rocket;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.mars.recipe.GCMarsRecipeManager;
import micdoodle8.mods.galacticraft.mars.schematic.GCMarsSchematicCargoRocket;
import micdoodle8.mods.galacticraft.mars.schematic.GCMarsSchematicRocketT2;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntitySlimelingEgg;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTerraformer;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTreasureChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * GalacticraftMars.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@Mod(name = GalacticraftMars.NAME, version = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION, useMetadata = true, modid = GalacticraftMars.MODID, dependencies = "required-after:" + GalacticraftCore.MODID + ";")
public class GalacticraftMars
{
	public static final String NAME = "Galacticraft Mars";
	public static final String MODID = "GalacticraftMars";
	public static final String CHANNEL = "GalacticraftMars";
	public static final String CHANNELENTITIES = "GCMarsEntities";

	public static final String LANGUAGE_PATH = "/assets/galacticraftmars/lang/";

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.mars.client.ClientProxyMars", serverSide = "micdoodle8.mods.galacticraft.mars.CommonProxyMars")
	public static CommonProxyMars proxy;

	@Instance(GalacticraftMars.MODID)
	public static GalacticraftMars instance;

	public static CreativeTabGC galacticraftMarsTab;

	public static final String TEXTURE_DOMAIN = "galacticraftmars";
	public static final String TEXTURE_PREFIX = GalacticraftMars.TEXTURE_DOMAIN + ":";

	public static Fluid SLUDGE;

	public static Planet planetMars;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new GCMarsEvents());
		new GCMarsConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/mars.conf"));

		GalacticraftMars.SLUDGE = new Fluid("bacterialsludge").setViscosity(3000);
		if (!FluidRegistry.registerFluid(GalacticraftMars.SLUDGE))
		{
			GCLog.info("\"bacterialsludge\" has already been registered as a fluid, ignoring...");
		}

		GCMarsBlocks.initBlocks();
		GCMarsBlocks.registerBlocks();
		GCMarsBlocks.setHarvestLevels();
		
		GalacticraftMars.SLUDGE.setBlock(GCMarsBlocks.blockSludge);

		GCMarsItems.initItems();

		GalacticraftMars.proxy.preInit(event);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		SchematicRegistry.registerSchematicRecipe(new GCMarsSchematicRocketT2());
		SchematicRegistry.registerSchematicRecipe(new GCMarsSchematicCargoRocket());

		GalacticraftMars.galacticraftMarsTab = new CreativeTabGC(CreativeTabs.getNextID(), GalacticraftMars.MODID, GCMarsItems.spaceship, 5);
		NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftMars.instance, GalacticraftMars.proxy);
		
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		
		GalacticraftMars.proxy.init(event);

		GalacticraftMars.planetMars = (Planet) new Planet("mars").setParentGalaxy(GalacticraftCore.galaxyBlockyWay).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(1.52F).setRelativeOrbitTime(1.88F);
		GalacticraftMars.planetMars.setPlanetIcon(new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/gui/planets/mars.png"));
		GalacticraftMars.planetMars.setDimensionInfo(GCMarsConfigManager.dimensionIDMars, GCMarsWorldProvider.class);

		GalaxyRegistry.registerPlanet(GalacticraftMars.planetMars);
		GalacticraftRegistry.registerTeleportType(GCMarsWorldProvider.class, new GCMarsTeleportType());
		GalacticraftRegistry.registerRocketGui(GCMarsWorldProvider.class, new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/gui/marsRocketGui.png"));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(GCMarsItems.schematic, 1, 0));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(GCMarsItems.schematic, 1, 1));

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), new ItemStack(GCItems.heavyPlatingTier1), new ItemStack(GCItems.meteoricIronIngot, 1, 1));
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCMarsItems.marsItemBasic, 1, 5), new ItemStack(GCMarsItems.marsItemBasic, 1, 2));
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	public void registerTileEntities()
	{
		GameRegistry.registerTileEntity(GCMarsTileEntitySlimelingEgg.class, "Slimeling Egg");
		GameRegistry.registerTileEntity(GCMarsTileEntityTreasureChest.class, "Tier 2 Treasure Chest");
		GameRegistry.registerTileEntity(GCMarsTileEntityTerraformer.class, "Planet Terraformer");
		GameRegistry.registerTileEntity(GCMarsTileEntityCryogenicChamber.class, "Cryogenic Chamber");
		GameRegistry.registerTileEntity(GCMarsTileEntityDungeonSpawner.class, "Mars Dungeon Spawner");
		GameRegistry.registerTileEntity(GCMarsTileEntityLaunchController.class, "Launch Controller");
	}

	public void registerCreatures()
	{
		this.registerGalacticraftCreature(EntitySludgeling.class, "Sludgeling", GCMarsConfigManager.idEntitySludgeling, GCCoreUtil.convertTo32BitColor(255, 0, 0, 50), GCCoreUtil.convertTo32BitColor(255, 0, 0, 150));
		this.registerGalacticraftCreature(EntitySlimeling.class, "Slimeling", GCMarsConfigManager.idEntitySlimeling, GCCoreUtil.convertTo32BitColor(255, 0, 0, 50), GCCoreUtil.convertTo32BitColor(255, 0, 0, 150));
		this.registerGalacticraftCreature(EntityCreeperBoss.class, "CreeperBoss", GCMarsConfigManager.idEntityCreeperBoss, GCCoreUtil.convertTo32BitColor(255, 0, 0, 50), GCCoreUtil.convertTo32BitColor(255, 0, 0, 150));
	}

	public void registerOtherEntities()
	{
		this.registerGalacticraftNonMobEntity(EntityTier2Rocket.class, "SpaceshipT2", GCMarsConfigManager.idEntitySpaceshipTier2, 150, 1, false);
		this.registerGalacticraftNonMobEntity(EntityTerraformBubble.class, "TerraformBubble", GCMarsConfigManager.idEntityTerraformBubble, 150, 20, false);
		this.registerGalacticraftNonMobEntity(EntityProjectileTNT.class, "ProjectileTNT", GCMarsConfigManager.idEntityProjectileTNT, 150, 1, true);
		this.registerGalacticraftNonMobEntity(EntityLandingBalloons.class, "LandingBalloons", GCMarsConfigManager.idEntityLandingBalloons, 150, 5, true);
		this.registerGalacticraftNonMobEntity(EntityCargoRocket.class, "CargoRocket", GCMarsConfigManager.idEntityCargoRocket, 150, 1, false);
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		GalacticraftMars.proxy.postInit(event);
		GalacticraftMars.proxy.registerRenderInformation();
		GCMarsRecipeManager.loadRecipes();
	}

	public void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int id, int back, int fore)
	{
		EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
		EntityRegistry.registerModEntity(var0, var1, id, GalacticraftMars.instance, 80, 3, true);
	}

	public void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
	{
		EntityList.addMapping(var0, var1, id);
		EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
	}
}
