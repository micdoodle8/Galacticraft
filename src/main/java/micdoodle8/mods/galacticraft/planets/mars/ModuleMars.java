package micdoodle8.mods.galacticraft.planets.mars;

import java.io.File;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluid;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSludge;
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
import micdoodle8.mods.galacticraft.planets.mars.util.EventHandlerMars;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleMars implements IPlanetsModule
{
	public static Planet planetMars;
	public static Fluid fluidSludge;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new EventHandlerMars());
		ModuleMars.fluidSludge = new Fluid("bacterialsludge").setViscosity(3000).setDensity(4500);
		FluidRegistry.registerFluid(ModuleMars.fluidSludge);
		
		if (fluidSludge.getBlock() == null)
		{
			MarsBlocks.blockSludge = new BlockSludge(ModuleMars.fluidSludge, "bacterialsludge");
			((BlockFluid) MarsBlocks.blockSludge).setQuantaPerBlock(9);
			MarsBlocks.blockSludge.setBlockName("bacterialsludge");
			GameRegistry.registerBlock(MarsBlocks.blockSludge, ItemBlockGC.class, MarsBlocks.blockSludge.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
			ModuleMars.fluidSludge.setBlock(MarsBlocks.blockSludge);
		}
		else
		{
			MarsBlocks.blockSludge = ModuleMars.fluidSludge.getBlock();
		}

		MarsBlocks.initBlocks();
		MarsItems.initItems();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		SchematicRegistry.registerSchematicRecipe(new SchematicTier2Rocket());
		SchematicRegistry.registerSchematicRecipe(new SchematicCargoRocket());
		
		GalacticraftCore.packetPipeline.registerPacket(PacketSimpleMars.class);

		NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftPlanets.instance, GalacticraftPlanets.proxyMars);
		
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();

		ModuleMars.planetMars = (Planet) new Planet("mars").setParentGalaxy(GalacticraftCore.galaxyBlockyWay).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(1.52F).setRelativeOrbitTime(1.88F);
		ModuleMars.planetMars.setPlanetIcon(new ResourceLocation(GalacticraftPlanets.ASSET_DOMAIN, "textures/gui/planets/mars.png"));
		ModuleMars.planetMars.setDimensionInfo(ConfigManagerPlanets.dimensionIDMars, WorldProviderMars.class);
		
		GalaxyRegistry.registerPlanet(ModuleMars.planetMars);
		GalacticraftRegistry.registerTeleportType(WorldProviderMars.class, new TeleportTypeMars());
		GalacticraftRegistry.registerRocketGui(WorldProviderMars.class, new ResourceLocation(GalacticraftPlanets.ASSET_DOMAIN, "textures/gui/marsRocketGui.png"));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 0));
		GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 1));

		CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(GCItems.heavyPlatingTier1), new ItemStack(GCItems.meteoricIronIngot, 1, 1));
		CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 5), new ItemStack(MarsItems.marsItemBasic, 1, 2));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		RecipeManagerMars.loadRecipes();
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
		;
	}

	public void registerTileEntities()
	{
		CoreUtil.registerTileEntity(GalacticraftPlanets.MOD_ID, TileEntitySlimelingEgg.class, "SlimelingEgg");
		CoreUtil.registerTileEntity(GalacticraftPlanets.MOD_ID, TileEntityTreasureChestMars.class, "TreasureChestT2");
		CoreUtil.registerTileEntity(GalacticraftPlanets.MOD_ID, TileEntityTerraformer.class, "TerraformerMars");
		CoreUtil.registerTileEntity(GalacticraftPlanets.MOD_ID, TileEntityCryogenicChamber.class, "CryogenicChamber");
		CoreUtil.registerTileEntity(GalacticraftPlanets.MOD_ID, TileEntityDungeonSpawnerMars.class, "DungeonSpawnerMars");
		CoreUtil.registerTileEntity(GalacticraftPlanets.MOD_ID, TileEntityLaunchController.class, "RocketLaunchController");
	}

	public void registerCreatures()
	{
		CoreUtil.registerCreature(GalacticraftPlanets.instance, EntitySludgeling.class, "Sludgeling", ConfigManagerPlanets.idEntitySludgeling, CoreUtil.to32BitColor(255, 0, 50, 0), CoreUtil.to32BitColor(255, 0, 150, 0));
		CoreUtil.registerCreature(GalacticraftPlanets.instance, EntitySlimeling.class, "Slimeling", ConfigManagerPlanets.idEntitySlimeling, CoreUtil.to32BitColor(255, 0, 50, 0), CoreUtil.to32BitColor(255, 0, 150, 0));
		CoreUtil.registerCreature(GalacticraftPlanets.instance, EntityCreeperBoss.class, "CreeperBoss", ConfigManagerPlanets.idEntityCreeperBoss, CoreUtil.to32BitColor(255, 0, 50, 0), CoreUtil.to32BitColor(255, 0, 150, 0));
	}

	public void registerOtherEntities()
	{
		CoreUtil.registerNonMobEntity(GalacticraftPlanets.instance, EntityTier2Rocket.class, "SpaceshipT2", ConfigManagerPlanets.idEntitySpaceshipTier2, 150, 1, false);
		CoreUtil.registerNonMobEntity(GalacticraftPlanets.instance, EntityTerraformBubble.class, "TerraformBubble", ConfigManagerPlanets.idEntityTerraformBubble, 150, 20, false);
		CoreUtil.registerNonMobEntity(GalacticraftPlanets.instance, EntityProjectileTNT.class, "ProjectileTNT", ConfigManagerPlanets.idEntityProjectileTNT, 150, 1, true);
		CoreUtil.registerNonMobEntity(GalacticraftPlanets.instance, EntityLandingBalloons.class, "LandingBalloons", ConfigManagerPlanets.idEntityLandingBalloons, 150, 5, true);
		CoreUtil.registerNonMobEntity(GalacticraftPlanets.instance, EntityCargoRocket.class, "CargoRocket", ConfigManagerPlanets.idEntityCargoRocket, 150, 1, false);
	}
}
