package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBucketGC;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.GCPlanetDimensions;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSludge;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.*;
import micdoodle8.mods.galacticraft.planets.mars.inventory.*;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemSchematicTier2;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.recipe.RecipeManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.BiomeMars;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Method;
import java.util.List;

public class MarsModule implements IPlanetsModule
{
    public static Fluid sludge;
    public static Fluid sludgeGC;
    public static Material sludgeMaterial = new MaterialLiquid(MapColor.FOLIAGE);

    public static Planet planetMars;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHandlerMars());

        if (!FluidRegistry.isFluidRegistered("bacterialsludge"))
        {
            ResourceLocation stillIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sludge_still");
            ResourceLocation flowingIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sludge_flow");
            sludgeGC = new Fluid("bacterialsludge", stillIcon, flowingIcon).setDensity(800).setViscosity(1500);
            FluidRegistry.registerFluid(sludgeGC);
        }
        else
        {
            GCLog.info("Galacticraft sludge is not default, issues may occur.");
        }

        sludge = FluidRegistry.getFluid("bacterialsludge");

        if (sludge.getBlock() == null)
        {
            MarsBlocks.blockSludge = new BlockSludge("sludge");
            ((BlockSludge) MarsBlocks.blockSludge).setQuantaPerBlock(3);
            MarsBlocks.registerBlock(MarsBlocks.blockSludge, ItemBlockDesc.class);
            sludge.setBlock(MarsBlocks.blockSludge);
        }
        else
        {
            MarsBlocks.blockSludge = sludge.getBlock();
        }

        if (MarsBlocks.blockSludge != null)
        {
        	FluidRegistry.addBucketForFluid(sludge);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull fluids out of other mods tanks
            MarsItems.bucketSludge = new ItemBucketGC(MarsBlocks.blockSludge, sludge).setUnlocalizedName("bucket_sludge");
            MarsItems.registerItem(MarsItems.bucketSludge);
            EventHandlerGC.bucketList.put(MarsBlocks.blockSludge, MarsItems.bucketSludge);
        }

        MarsBlocks.initBlocks();

        MarsItems.initItems();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        MarsBlocks.oreDictRegistration();
        this.registerMicroBlocks();
        SchematicRegistry.registerSchematicRecipe(new SchematicTier2Rocket());
        SchematicRegistry.registerSchematicRecipe(new SchematicCargoRocket());

        GalacticraftCore.packetPipeline.addDiscriminator(6, PacketSimpleMars.class);

        this.registerTileEntities();
        this.registerCreatures();
        this.registerOtherEntities();

        MarsModule.planetMars = (Planet) new Planet("mars").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.25F, 1.25F)).setRelativeOrbitTime(1.8811610076670317634173055859803F);
        MarsModule.planetMars.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/mars.png"));
        MarsModule.planetMars.setDimensionInfo(ConfigManagerMars.dimensionIDMars, WorldProviderMars.class).setTierRequired(2);
        MarsModule.planetMars.setAtmosphere(new AtmosphereInfo(false, false, false, -1.0F, 0.3F, 0.1F));
        MarsModule.planetMars.atmosphereComponent(EnumAtmosphericGas.CO2).atmosphereComponent(EnumAtmosphericGas.ARGON).atmosphereComponent(EnumAtmosphericGas.NITROGEN);
        MarsModule.planetMars.setBiomeInfo(BiomeMars.marsFlat);
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
        MarsModule.planetMars.addChecklistKeys("equipOxygenSuit");

        GalaxyRegistry.registerPlanet(MarsModule.planetMars);
        GalacticraftRegistry.registerTeleportType(WorldProviderMars.class, new TeleportTypeMars());
        GalacticraftRegistry.registerRocketGui(WorldProviderMars.class, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/mars_rocket_gui.png"));
        GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 0));
        GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 1));
        GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematic, 1, 2));

        CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(GCItems.heavyPlatingTier1), new ItemStack(GCItems.itemBasicMoon, 1, 1));
        CompressorRecipes.addShapelessRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 5), ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(MarsItems.marsItemBasic, 1, 2) : "ingotDesh");

        GalacticraftCore.proxy.registerFluidTexture(MarsModule.sludge, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/underbecterial.png"));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        RecipeManagerMars.loadRecipes();
        GCPlanetDimensions.MARS = WorldUtil.getDimensionTypeById(ConfigManagerMars.dimensionIDMars);
        ItemSchematicTier2.registerSchematicItems();
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
        try
        {
            Class clazz = Class.forName("codechicken.microblock.MicroMaterialRegistry");
            if (clazz != null)
            {
                Method registerMethod = null;
                Method[] methodz = clazz.getMethods();
                for (Method m : methodz)
                {
                    if (m.getName().equals("registerMaterial"))
                    {
                        registerMethod = m;
                        break;
                    }
                }
                Class clazzbm = Class.forName("codechicken.microblock.BlockMicroMaterial");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 4), "tile.mars.marscobblestone");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 5), "tile.mars.marsgrass");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 6), "tile.mars.marsdirt");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 7), "tile.mars.marsdungeon");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 8), "tile.mars.marsdeco");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 9), "tile.mars.marsstone");
            }
        }
        catch (Exception e)
        {
        }
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntitySlimelingEgg.class, "GC Slimeling Egg");
        GameRegistry.registerTileEntity(TileEntityTreasureChestMars.class, "GC Tier 2 Treasure Chest");
        GameRegistry.registerTileEntity(TileEntityTerraformer.class, "GC Planet Terraformer");
        GameRegistry.registerTileEntity(TileEntityCryogenicChamber.class, "GC Cryogenic Chamber");
        GameRegistry.registerTileEntity(TileEntityGasLiquefier.class, "GC Gas Liquefier");
        GameRegistry.registerTileEntity(TileEntityMethaneSynthesizer.class, "GC Methane Synthesizer");
        GameRegistry.registerTileEntity(TileEntityElectrolyzer.class, "GC Water Electrolyzer");
        GameRegistry.registerTileEntity(TileEntityDungeonSpawnerMars.class, "GC Mars Dungeon Spawner");
        GameRegistry.registerTileEntity(TileEntityLaunchController.class, "GC Launch Controller");
    }

    public void registerCreatures()
    {
        this.registerGalacticraftCreature(EntitySludgeling.class, "sludgeling", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
        this.registerGalacticraftCreature(EntitySlimeling.class, "slimeling", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
        this.registerGalacticraftCreature(EntityCreeperBoss.class, "creeper_boss", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
    }

    public void registerOtherEntities()
    {
        MarsModule.registerGalacticraftNonMobEntity(EntityTier2Rocket.class, "rocket_t2", 150, 1, false);
        MarsModule.registerGalacticraftNonMobEntity(EntityProjectileTNT.class, "projectile_tnt", 150, 1, true);
        MarsModule.registerGalacticraftNonMobEntity(EntityLandingBalloons.class, "landing_balloons", 150, 5, true);
        MarsModule.registerGalacticraftNonMobEntity(EntityCargoRocket.class, "rocket_cargo", 150, 1, false);
    }

    public void registerGalacticraftCreature(Class<? extends Entity> clazz, String name, int back, int fore)
    {
        MarsModule.registerGalacticraftNonMobEntity(clazz, name, 80, 3, true);
        int nextEggID = GCCoreUtil.getNextValidID();
        if (nextEggID < 65536)
        {
            ResourceLocation resourcelocation = new ResourceLocation(Constants.MOD_ID_PLANETS, name);
//            name = Constants.MOD_ID_PLANETS + "." + name;
//            net.minecraftforge.fml.common.registry.EntityEntry entry = new net.minecraftforge.fml.common.registry.EntityEntry(clazz, name);
//            net.minecraftforge.fml.common.registry.GameData.getEntityRegistry().register(nextEggID, resourcelocation, entry);
            EntityList.ENTITY_EGGS.put(resourcelocation, new EntityList.EntityEggInfo(resourcelocation, back, fore));
        }
    }

    public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
        ResourceLocation registryName = new ResourceLocation(Constants.MOD_ID_PLANETS, var1);
        EntityRegistry.registerModEntity(registryName, var0, var1, GCCoreUtil.nextInternalID(), GalacticraftPlanets.instance, trackingDistance, updateFreq, sendVel);
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
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity tile = world.getTileEntity(pos);

            if (ID == GuiIdsPlanets.MACHINE_MARS)
            {
                if (tile instanceof TileEntityTerraformer)
                {
                    return new ContainerTerraformer(player.inventory, (TileEntityTerraformer) tile, player);
                }
                else if (tile instanceof TileEntityLaunchController)
                {
                    return new ContainerLaunchController(player.inventory, (TileEntityLaunchController) tile, player);
                }
                else if (tile instanceof TileEntityElectrolyzer)
                {
                    return new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer) tile, player);
                }
                else if (tile instanceof TileEntityGasLiquefier)
                {
                    return new ContainerGasLiquefier(player.inventory, (TileEntityGasLiquefier) tile, player);
                }
                else if (tile instanceof TileEntityMethaneSynthesizer)
                {
                    return new ContainerMethaneSynthesizer(player.inventory, (TileEntityMethaneSynthesizer) tile, player);
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
        ConfigManagerMars.syncConfig(false, false);
    }
}
