package micdoodle8.mods.galacticraft.mars;

import java.io.File;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluid;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCCreativeTab;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.mars.blocks.BlockSludge;
import micdoodle8.mods.galacticraft.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.mars.entities.EntityProjectileTNT;
import micdoodle8.mods.galacticraft.mars.entities.EntityTier2Rocket;
import micdoodle8.mods.galacticraft.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.mars.entities.EntitySludgeling;
import micdoodle8.mods.galacticraft.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.galacticraft.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.mars.proxy.CommonProxyMars;
import micdoodle8.mods.galacticraft.mars.recipe.RecipeManagerMars;
import micdoodle8.mods.galacticraft.mars.schematic.SchematicCargoRocket;
import micdoodle8.mods.galacticraft.mars.schematic.SchematicTier2Rocket;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityDungeonSpawnerMars;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.TileEntitySlimelingEgg;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityTreasureChestMars;
import micdoodle8.mods.galacticraft.mars.util.ConfigManagerMars;
import micdoodle8.mods.galacticraft.mars.util.EventHandlerMars;
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
@Mod(name = GalacticraftMars.NAME, useMetadata = true, modid = GalacticraftMars.MOD_ID, dependencies = "required-after:" + GalacticraftCore.MOD_ID + ";required-after:Forge@[7.0,);required-after:FML@[5.0.5,)")
public class GalacticraftMars
{
	public static final String NAME = "Galacticraft Mars";
	public static final String MOD_ID = "GalacticraftMars";

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.mars.proxy.ClientProxyMars", serverSide = "micdoodle8.mods.galacticraft.mars.proxy.CommonProxyMars")
	public static CommonProxyMars proxy;

	@Instance(GalacticraftMars.MOD_ID)
	public static GalacticraftMars instance;

	public static CreativeTabs galacticraftMarsTab;

	public static final String ASSET_DOMAIN = "galacticraftmars";
	public static final String TEXTURE_PREFIX = GalacticraftMars.ASSET_DOMAIN + ":";
	
	public static Planet planetMars;

	public static Fluid fluidSludge;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new EventHandlerMars());
		new ConfigManagerMars(new File(event.getModConfigurationDirectory(), "Galacticraft/mars.conf"));

		GalacticraftMars.fluidSludge = new Fluid("bacterialsludge").setViscosity(3000).setDensity(4500);
		FluidRegistry.registerFluid(GalacticraftMars.fluidSludge);
		
		if (fluidSludge.getBlock() == null)
		{
			MarsBlocks.blockSludge = new BlockSludge(GalacticraftMars.fluidSludge, "bacterialsludge");
			((BlockFluid) MarsBlocks.blockSludge).setQuantaPerBlock(9);
			MarsBlocks.blockSludge.setBlockName("bacterialsludge");
			GameRegistry.registerBlock(MarsBlocks.blockSludge, ItemBlockGC.class, MarsBlocks.blockSludge.getUnlocalizedName(), GalacticraftMars.MOD_ID);
			GalacticraftMars.fluidSludge.setBlock(MarsBlocks.blockSludge);
		}
		else
		{
			MarsBlocks.blockSludge = GalacticraftMars.fluidSludge.getBlock();
		}

		MarsBlocks.initBlocks();
		MarsBlocks.registerBlocks();

		MarsItems.initItems();

		GalacticraftMars.proxy.preInit(event);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		SchematicRegistry.registerSchematicRecipe(new SchematicTier2Rocket());
		SchematicRegistry.registerSchematicRecipe(new SchematicCargoRocket());
		
		GalacticraftCore.packetPipeline.registerPacket(PacketSimpleMars.class);

		GalacticraftMars.galacticraftMarsTab = new GCCreativeTab(CreativeTabs.getNextID(), GalacticraftMars.MOD_ID, MarsItems.spaceship, 5);
		NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftMars.instance, GalacticraftMars.proxy);
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		GalacticraftMars.proxy.init(event);

		GalacticraftMars.planetMars = (Planet) new Planet("mars").setParentGalaxy(GalacticraftCore.galaxyBlockyWay).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(1.52F).setRelativeOrbitTime(1.88F);
		GalacticraftMars.planetMars.setPlanetIcon(new ResourceLocation(GalacticraftMars.ASSET_DOMAIN, "textures/gui/planets/mars.png"));
		GalacticraftMars.planetMars.setDimensionInfo(ConfigManagerMars.dimensionIDMars, WorldProviderMars.class);
		GalaxyRegistry.registerPlanet(GalacticraftMars.planetMars);
		
		GalacticraftRegistry.registerTeleportType(WorldProviderMars.class, new TeleportTypeMars());
		GalacticraftRegistry.registerRocketGui(WorldProviderMars.class, new ResourceLocation(GalacticraftMars.ASSET_DOMAIN, "textures/gui/marsRocketGui.png"));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 0));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 1));

		CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(GCItems.heavyPlatingTier1), new ItemStack(GCItems.meteoricIronIngot, 1, 1));
		CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 5), new ItemStack(MarsItems.marsItemBasic, 1, 2));
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	public void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntitySlimelingEgg.class, "Slimeling Egg");
		GameRegistry.registerTileEntity(TileEntityTreasureChestMars.class, "Tier 2 Treasure Chest");
		GameRegistry.registerTileEntity(TileEntityTerraformer.class, "Planet Terraformer");
		GameRegistry.registerTileEntity(TileEntityCryogenicChamber.class, "Cryogenic Chamber");
		GameRegistry.registerTileEntity(TileEntityDungeonSpawnerMars.class, "Mars Dungeon Spawner");
		GameRegistry.registerTileEntity(TileEntityLaunchController.class, "Launch Controller");
	}

	public void registerCreatures()
	{
		this.registerGalacticraftCreature(EntitySludgeling.class, "Sludgeling", ConfigManagerMars.idEntitySludgeling, CoreUtil.to32BitColor(255, 0, 50, 0), CoreUtil.to32BitColor(255, 0, 150, 0));
		this.registerGalacticraftCreature(EntitySlimeling.class, "Slimeling", ConfigManagerMars.idEntitySlimeling, CoreUtil.to32BitColor(255, 0, 50, 0), CoreUtil.to32BitColor(255, 0, 150, 0));
		this.registerGalacticraftCreature(EntityCreeperBoss.class, "CreeperBoss", ConfigManagerMars.idEntityCreeperBoss, CoreUtil.to32BitColor(255, 0, 50, 0), CoreUtil.to32BitColor(255, 0, 150, 0));
	}

	public void registerOtherEntities()
	{
		this.registerGalacticraftNonMobEntity(EntityTier2Rocket.class, "SpaceshipT2", ConfigManagerMars.idEntitySpaceshipTier2, 150, 1, false);
		this.registerGalacticraftNonMobEntity(EntityTerraformBubble.class, "TerraformBubble", ConfigManagerMars.idEntityTerraformBubble, 150, 20, false);
		this.registerGalacticraftNonMobEntity(EntityProjectileTNT.class, "ProjectileTNT", ConfigManagerMars.idEntityProjectileTNT, 150, 1, true);
		this.registerGalacticraftNonMobEntity(EntityLandingBalloons.class, "LandingBalloons", ConfigManagerMars.idEntityLandingBalloons, 150, 5, true);
		this.registerGalacticraftNonMobEntity(EntityCargoRocket.class, "CargoRocket", ConfigManagerMars.idEntityCargoRocket, 150, 1, false);
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		GalacticraftMars.proxy.postInit(event);
		GalacticraftMars.proxy.registerRenderInformation();
		RecipeManagerMars.loadRecipes();
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
