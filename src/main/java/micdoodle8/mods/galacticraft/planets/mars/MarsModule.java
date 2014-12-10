package micdoodle8.mods.galacticraft.planets.mars;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.*;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.recipe.RecipeManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

import java.io.File;
import java.util.List;

public class MarsModule implements IPlanetsModule
{
    public static final String ASSET_PREFIX = "galacticraftmars";
    public static final String TEXTURE_PREFIX = MarsModule.ASSET_PREFIX + ":";

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
        MarsBlocks.oreDictRegistration();

        MarsModule.SLUDGE.setBlock(MarsBlocks.blockSludge);

        MarsItems.initItems();

		FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(MarsModule.SLUDGE, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(MarsItems.bucketSludge), new ItemStack(Items.bucket)));
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        SchematicRegistry.registerSchematicRecipe(new SchematicTier2Rocket());
        SchematicRegistry.registerSchematicRecipe(new SchematicCargoRocket());

        GalacticraftCore.packetPipeline.addDiscriminator(6, PacketSimpleMars.class);

        this.registerTileEntities();
        this.registerCreatures();
        this.registerOtherEntities();

        MarsModule.planetMars = (Planet) new Planet("mars").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.25F, 1.25F)).setRelativeOrbitTime(1.8811610076670317634173055859803F);
        MarsModule.planetMars.setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/mars.png"));
        MarsModule.planetMars.setDimensionInfo(ConfigManagerMars.dimensionIDMars, WorldProviderMars.class).setTierRequired(2);
        MarsModule.planetMars.atmosphereComponent(IAtmosphericGas.CO2).atmosphereComponent(IAtmosphericGas.ARGON).atmosphereComponent(IAtmosphericGas.NITROGEN);

        GalaxyRegistry.registerPlanet(MarsModule.planetMars);
        GalacticraftRegistry.registerTeleportType(WorldProviderMars.class, new TeleportTypeMars());
        GalacticraftRegistry.registerRocketGui(WorldProviderMars.class, new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/gui/marsRocketGui.png"));
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
    }

    @Override
    public void serverInit(FMLServerStartedEvent event)
    {

    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntitySlimelingEgg.class, "Slimeling Egg");
        GameRegistry.registerTileEntity(TileEntityTreasureChestMars.class, "Tier 2 Treasure Chest");
        GameRegistry.registerTileEntity(TileEntityTerraformer.class, "Planet Terraformer");
        GameRegistry.registerTileEntity(TileEntityCryogenicChamber.class, "Cryogenic Chamber");
        GameRegistry.registerTileEntity(TileEntityGasLiquefier.class, "Gas Liquefier");
        GameRegistry.registerTileEntity(TileEntityMethaneSynthesizer.class, "Methane Synthesizer");
        GameRegistry.registerTileEntity(TileEntityElectrolyzer.class, "Water Electrolyzer");
        GameRegistry.registerTileEntity(TileEntityDungeonSpawnerMars.class, "Mars Dungeon Spawner");
        GameRegistry.registerTileEntity(TileEntityLaunchController.class, "Launch Controller");
        GameRegistry.registerTileEntity(TileEntityHydrogenPipe.class, "Hydrogen Pipe");
    }

    public void registerCreatures()
    {
        this.registerGalacticraftCreature(EntitySludgeling.class, "Sludgeling", GCCoreUtil.to32BitColor(255, 0, 50, 0), GCCoreUtil.to32BitColor(255, 0, 150, 0));
        this.registerGalacticraftCreature(EntitySlimeling.class, "Slimeling", GCCoreUtil.to32BitColor(255, 0, 50, 0), GCCoreUtil.to32BitColor(255, 0, 150, 0));
        this.registerGalacticraftCreature(EntityCreeperBoss.class, "CreeperBoss", GCCoreUtil.to32BitColor(255, 0, 50, 0), GCCoreUtil.to32BitColor(255, 0, 150, 0));
    }

    public void registerOtherEntities()
    {
        MarsModule.registerGalacticraftNonMobEntity(EntityTier2Rocket.class, "SpaceshipT2", 150, 1, false);
        MarsModule.registerGalacticraftNonMobEntity(EntityTerraformBubble.class, "TerraformBubble", 150, 20, false);
        MarsModule.registerGalacticraftNonMobEntity(EntityProjectileTNT.class, "ProjectileTNT", 150, 1, true);
        MarsModule.registerGalacticraftNonMobEntity(EntityLandingBalloons.class, "LandingBalloons", 150, 5, true);
        MarsModule.registerGalacticraftNonMobEntity(EntityCargoRocket.class, "CargoRocket", 150, 1, false);
    }

    public void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int back, int fore)
    {
        int newID = EntityRegistry.instance().findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(var0, var1, newID, back, fore);
        EntityRegistry.registerModEntity(var0, var1, GCCoreUtil.nextInternalID(), GalacticraftPlanets.instance, 80, 3, true);
    }

    public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
        EntityRegistry.registerModEntity(var0, var1, GCCoreUtil.nextInternalID(), GalacticraftPlanets.instance, trackingDistance, updateFreq, sendVel);
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
        idList.add(GuiIdsPlanets.MACHINE_MARS);
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (side == Side.SERVER)
        {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (ID == GuiIdsPlanets.MACHINE_MARS)
            {
                if (tile instanceof TileEntityTerraformer)
                {
                    return new ContainerTerraformer(player.inventory, (TileEntityTerraformer) tile);
                }
                else if (tile instanceof TileEntityLaunchController)
                {
                    return new ContainerLaunchController(player.inventory, (TileEntityLaunchController) tile);
                }
                else if (tile instanceof TileEntityElectrolyzer)
                {
                    return new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer) tile);
                }
                else if (tile instanceof TileEntityGasLiquefier)
                {
                    return new ContainerGasLiquefier(player.inventory, (TileEntityGasLiquefier) tile);
                }
                else if (tile instanceof TileEntityMethaneSynthesizer)
                {
                    return new ContainerMethaneSynthesizer(player.inventory, (TileEntityMethaneSynthesizer) tile);
                }
            }
        }

        return null;
    }

    @Override
    public Configuration getConfiguration()
    {
        return ConfigManagerMars.config;
    }

    @Override
    public void syncConfig()
    {
        ConfigManagerMars.syncConfig(false);
    }
}
