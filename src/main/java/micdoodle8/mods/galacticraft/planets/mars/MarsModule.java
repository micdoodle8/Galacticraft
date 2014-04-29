package micdoodle8.mods.galacticraft.planets.mars;

import java.io.File;
import java.util.List;

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
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityProjectileTNT;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.recipe.RecipeManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityDungeonSpawnerMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * GalacticraftMars.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MarsModule implements IPlanetsModule
{
	public static final String LANGUAGE_PATH = "/assets/galacticraftmars/lang/";

	public static CreativeTabGC galacticraftMarsTab;

	public static final String TEXTURE_DOMAIN = "galacticraftmars";
	public static final String TEXTURE_PREFIX = MarsModule.TEXTURE_DOMAIN + ":";

	public static Fluid SLUDGE;

	public static Planet planetMars;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new EventHandlerMars());
		new ConfigManagerMars(new File(event.getModConfigurationDirectory(), "Galacticraft/mars.conf"));

		MarsModule.SLUDGE = new Fluid("bacterialsludge").setViscosity(3000);
		if (!FluidRegistry.registerFluid(MarsModule.SLUDGE))
		{
			GCLog.info("\"bacterialsludge\" has already been registered as a fluid, ignoring...");
		}

		MarsBlocks.initBlocks();
		MarsBlocks.registerBlocks();
		MarsBlocks.setHarvestLevels();
		
		MarsModule.SLUDGE.setBlock(MarsBlocks.blockSludge);

		MarsItems.initItems();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		SchematicRegistry.registerSchematicRecipe(new SchematicTier2Rocket());
		SchematicRegistry.registerSchematicRecipe(new SchematicCargoRocket());

		GalacticraftCore.packetPipeline.registerPacket(PacketSimpleMars.class);
		
		MarsModule.galacticraftMarsTab = new CreativeTabGC(CreativeTabs.getNextID(), GalacticraftPlanets.MODID, MarsItems.spaceship, 5);
		
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();

		MarsModule.planetMars = (Planet) new Planet("mars").setParentGalaxy(GalacticraftCore.galaxyBlockyWay).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(1.52F).setRelativeOrbitTime(1.88F);
		MarsModule.planetMars.setPlanetIcon(new ResourceLocation(MarsModule.TEXTURE_DOMAIN, "textures/gui/planets/mars.png"));
		MarsModule.planetMars.setDimensionInfo(ConfigManagerMars.dimensionIDMars, WorldProviderMars.class);

		GalaxyRegistry.registerPlanet(MarsModule.planetMars);
		GalacticraftRegistry.registerTeleportType(WorldProviderMars.class, new TeleportTypeMars());
		GalacticraftRegistry.registerRocketGui(WorldProviderMars.class, new ResourceLocation(MarsModule.TEXTURE_DOMAIN, "textures/gui/marsRocketGui.png"));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 0));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 1));

		CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(GCItems.heavyPlatingTier1), new ItemStack(GCItems.meteoricIronIngot, 1, 1));
		CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 5), new ItemStack(MarsItems.marsItemBasic, 1, 2));
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		RecipeManagerMars.loadRecipes();
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
		this.registerGalacticraftCreature(EntitySludgeling.class, "Sludgeling", ConfigManagerMars.idEntitySludgeling, GCCoreUtil.convertTo32BitColor(255, 0, 0, 50), GCCoreUtil.convertTo32BitColor(255, 0, 0, 150));
		this.registerGalacticraftCreature(EntitySlimeling.class, "Slimeling", ConfigManagerMars.idEntitySlimeling, GCCoreUtil.convertTo32BitColor(255, 0, 0, 50), GCCoreUtil.convertTo32BitColor(255, 0, 0, 150));
		this.registerGalacticraftCreature(EntityCreeperBoss.class, "CreeperBoss", ConfigManagerMars.idEntityCreeperBoss, GCCoreUtil.convertTo32BitColor(255, 0, 0, 50), GCCoreUtil.convertTo32BitColor(255, 0, 0, 150));
	}

	public void registerOtherEntities()
	{
		this.registerGalacticraftNonMobEntity(EntityTier2Rocket.class, "SpaceshipT2", ConfigManagerMars.idEntitySpaceshipTier2, 150, 1, false);
		this.registerGalacticraftNonMobEntity(EntityTerraformBubble.class, "TerraformBubble", ConfigManagerMars.idEntityTerraformBubble, 150, 20, false);
		this.registerGalacticraftNonMobEntity(EntityProjectileTNT.class, "ProjectileTNT", ConfigManagerMars.idEntityProjectileTNT, 150, 1, true);
		this.registerGalacticraftNonMobEntity(EntityLandingBalloons.class, "LandingBalloons", ConfigManagerMars.idEntityLandingBalloons, 150, 5, true);
		this.registerGalacticraftNonMobEntity(EntityCargoRocket.class, "CargoRocket", ConfigManagerMars.idEntityCargoRocket, 150, 1, false);
	}

	public void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int id, int back, int fore)
	{
		EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
		EntityRegistry.registerModEntity(var0, var1, id, GalacticraftPlanets.instance, 80, 3, true);
	}

	public void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
	{
		EntityList.addMapping(var0, var1, id);
		EntityRegistry.registerModEntity(var0, var1, id, GalacticraftPlanets.instance, trackingDistance, updateFreq, sendVel);
	}

	@Override
	public void getGuiIDs(List<Integer> idList) 
	{
		idList.add(ConfigManagerMars.idGuiCargoRocketCraftingBench);
		idList.add(ConfigManagerMars.idGuiMachine);
		idList.add(ConfigManagerMars.idGuiRocketCraftingBenchT2);
	}

	@Override
	public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if (side == Side.SERVER)
		{
			TileEntity tile = world.getTileEntity(x, y, z);

			if (ID == ConfigManagerMars.idGuiMachine)
			{
				if (tile instanceof TileEntityTerraformer)
				{
					return new ContainerTerraformer(player.inventory, (TileEntityTerraformer) tile);
				}
				else if (tile instanceof TileEntityLaunchController)
				{
					return new ContainerLaunchController(player.inventory, (TileEntityLaunchController) tile);
				}
			}
		}
		
		return null;
	}
}
