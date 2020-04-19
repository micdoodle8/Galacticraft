package micdoodle8.mods.galacticraft.core.proxy;

import api.player.client.ClientPlayerAPI;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import micdoodle8.mods.galacticraft.api.client.IItemMeshDefinitionCustom;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockConcealedDetector;
import micdoodle8.mods.galacticraft.core.blocks.BlockConcealedRedstone;
import micdoodle8.mods.galacticraft.core.blocks.BlockConcealedRepeater;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import micdoodle8.mods.galacticraft.core.blocks.BlockFallenMeteor;
import micdoodle8.mods.galacticraft.core.blocks.BlockGrating;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenDetector;
import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpaceGlass;
import micdoodle8.mods.galacticraft.core.client.DynamicTextureProper;
import micdoodle8.mods.galacticraft.core.client.EventHandlerClient;
import micdoodle8.mods.galacticraft.core.client.fx.EffectHandler;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.client.model.ModelRocketTier1;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelGrating;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelPanelLightBase;
import micdoodle8.mods.galacticraft.core.client.render.entities.*;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemModelBuggy;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemModelFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemModelRocket;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemModelWorkbench;
import micdoodle8.mods.galacticraft.core.client.render.item.TextureDungeonFinder;
import micdoodle8.mods.galacticraft.core.client.render.tile.*;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.client.sounds.MusicTickerGC;
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerBaseSP;
import micdoodle8.mods.galacticraft.core.entities.player.IPlayerClient;
import micdoodle8.mods.galacticraft.core.entities.player.PlayerClient;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.items.ItemFood;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.*;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class ClientProxyCore extends CommonProxyCore implements IResourceManagerReloadListener
{
    public static List<String> flagRequestsSent = new ArrayList<>();
    public static Set<BlockVec3> valueableBlocks = Sets.newHashSet();
    public static HashSet<BlockMetaList> detectableBlocks = Sets.newHashSet();
    public static List<BlockVec3> leakTrace = null;
    public static Map<String, PlayerGearData> playerItemData = Maps.newHashMap();
    public static double playerPosX;
    public static double playerPosY;
    public static double playerPosZ;
    public static float playerRotationYaw;
    public static float playerRotationPitch;
    public static boolean lastSpacebarDown;

    public static HashMap<Integer, Integer> clientSpaceStationID = Maps.newHashMap();
    public static MusicTicker.MusicType MUSIC_TYPE_MARS;
    public static EnumRarity galacticraftItem = EnumHelper.addRarity("GCRarity", TextFormatting.BLUE, "Space");
    public static Map<String, ResourceLocation> capeMap = new HashMap<>();
    public static InventoryExtended dummyInventory = new InventoryExtended();
    public static Map<Fluid, ResourceLocation> submergedTextures = Maps.newHashMap();
    public static IPlayerClient playerClientHandler = new PlayerClient();
    public static Minecraft mc = FMLClientHandler.instance().getClient();
    public static List<UUID> gearDataRequests = Lists.newArrayList();
    public static DynamicTextureProper overworldTextureClient;
    public static DynamicTextureProper overworldTextureWide;
    public static DynamicTextureProper overworldTextureLarge;
    public static boolean overworldTextureRequestSent;
    public static boolean overworldTexturesValid;
    public static float PLAYER_Y_OFFSET = 1.6200000047683716F;
    public static ResourceLocation playerHead = null;
    public static final ResourceLocation saturnRingTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/saturn_rings.png");
    public static final ResourceLocation uranusRingTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/uranus_rings.png");
    private static List<Item> itemsToRegisterJson = Lists.newArrayList();
    private static ModelResourceLocation fuelLocation = new ModelResourceLocation(Constants.TEXTURE_PREFIX + "fuel", "fluid");
    private static ModelResourceLocation oilLocation = new ModelResourceLocation(Constants.TEXTURE_PREFIX + "oil", "fluid");
    private static List<PartialCanister> canisters = Lists.newArrayList();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ClientProxyCore.registerEntityRenderers();
        ModelLoaderRegistry.registerLoader(OBJLoaderGC.instance);
        OBJLoaderGC.instance.addDomain(Constants.ASSET_PREFIX);

        if (CompatibilityManager.PlayerAPILoaded)
        {
            ClientPlayerAPI.register(Constants.MOD_ID_CORE, GCPlayerBaseSP.class);
        }
        MinecraftForge.EVENT_BUS.register(this);
        ClientProxyCore.registerHandlers();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        MUSIC_TYPE_MARS = EnumHelper.addEnum(MusicTicker.MusicType.class, "MARS_JC", new Class[] { SoundEvent.class, Integer.TYPE, Integer.TYPE }, GCSounds.music, 12000, 24000);
        ClientProxyCore.registerTileEntityRenderers();
        ClientProxyCore.updateCapeList();
        ClientProxyCore.registerInventoryJsons();

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
            (state, world, pos, tintIndex) -> {
                return BlockFallenMeteor.colorMultiplier(world, pos);
            }, GCBlocks.fallenMeteor);

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
            (state, world, pos, tintIndex) -> {
                Block b = state.getBlock();
                return (b instanceof BlockSpaceGlass) ? ((BlockSpaceGlass)b).color : 0xFFFFFF;
            }, new Block[] { GCBlocks.spaceGlassVanilla, GCBlocks.spaceGlassClear, GCBlocks.spaceGlassStrong });

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
            (state, world, pos, tintIndex) -> {
                if (world != null && pos != null)
                {
                    TileEntity tile = world.getTileEntity(pos);
                    if (tile instanceof TileEntityPanelLight)
                    {
                        IBlockState baseState = ((TileEntityPanelLight)tile).getBaseBlock();
                        return Minecraft.getMinecraft().getBlockColors().colorMultiplier(baseState, world, pos, tintIndex);
                    }
                }
                return 0xFFFFFF;
            }, GCBlocks.panelLighting);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        ClientProxyCore.registerInventoryTabs();
        ItemSchematic.registerTextures();

        MinecraftForge.EVENT_BUS.register(new TabRegistry());

        if (!CompatibilityManager.RenderPlayerAPILoaded)
        {
            try
            {
                Field field = RenderManager.class.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "playerRenderer" : "field_178637_m");
                field.setAccessible(true);
                field.set(FMLClientHandler.instance().getClient().getRenderManager(), new RenderPlayerGC());

                field = RenderManager.class.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "skinMap" : "field_178636_l");
                field.setAccessible(true);
                Map<String, RenderPlayer> skinMap = (Map<String, RenderPlayer>) field.get(FMLClientHandler.instance().getClient().getRenderManager());
                skinMap.put("default", new RenderPlayerGC(skinMap.get("default"), false));
                skinMap.put("slim", new RenderPlayerGC(skinMap.get("slim"), true));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);

        try {
            Field ftc = Minecraft.getMinecraft().getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "mcMusicTicker" : "field_147126_aw");
            ftc.setAccessible(true);
            ftc.set(Minecraft.getMinecraft(), new MusicTickerGC(Minecraft.getMinecraft()));
        } catch (Exception e) {e.printStackTrace();} 
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        String lang = net.minecraft.client.Minecraft.getMinecraft().gameSettings.language;
        GCLog.debug("Reloading entity names for language " + lang);
        if (lang == null)
        {
            lang = "en_US";
        }
        GalacticraftCore.instance.loadLanguageCore(lang);
        if (GalacticraftCore.isPlanetsLoaded && !GCCoreUtil.langDisable)
        {
            GalacticraftPlanets.instance.loadLanguagePlanets(lang);
        }
    }
    
    @Override
    public void postRegisterItem(Item item)
    {
        if (!item.getHasSubtypes())
        {
            ClientProxyCore.itemsToRegisterJson.add(item);
        }
    }

    @Override
    public void registerVariants()
    {
        ClientProxyCore.addVariants();

        Item fuel = Item.getItemFromBlock(GCBlocks.fuel);
        ModelBakery.registerItemVariants(fuel, new ResourceLocation("galacticraftcore:fuel"));
        ModelLoader.setCustomMeshDefinition(fuel, IItemMeshDefinitionCustom.create((ItemStack stack) -> fuelLocation));
        ModelLoader.setCustomStateMapper(GCBlocks.fuel, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return fuelLocation;
            }
        });
        Item oil = Item.getItemFromBlock(GCBlocks.crudeOil);
        ModelBakery.registerItemVariants(oil, new ResourceLocation("galacticraftcore:oil"));
        ModelLoader.setCustomMeshDefinition(oil, IItemMeshDefinitionCustom.create((ItemStack stack) -> oilLocation));
        ModelLoader.setCustomStateMapper(GCBlocks.crudeOil, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return oilLocation;
            }
        });

        Item nasaWorkbench = Item.getItemFromBlock(GCBlocks.nasaWorkbench);
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation("galacticraftcore:rocket_workbench", "inventory");
        ModelLoader.setCustomModelResourceLocation(nasaWorkbench, 0, modelResourceLocation);

        modelResourceLocation = new ModelResourceLocation("galacticraftcore:rocket_t1", "inventory");
        for (int i = 0; i < 5; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(GCItems.rocketTier1, i, modelResourceLocation);
        }

        for (int i = 0; i < 4; ++i)
        {
            modelResourceLocation = new ModelResourceLocation("galacticraftcore:buggy" + (i > 0 ? "_" + i : ""), "inventory");
            ModelLoader.setCustomModelResourceLocation(GCItems.buggy, i, modelResourceLocation);
        }

//        for (PartialCanister container : ClientProxyCore.canisters)
//        {
//            modelResourceLocation = new ModelResourceLocation(container.getModID() + ":" + container.getBaseName() + "_0", "inventory");
//            for (int i = 0; i < container.getItem().getMaxDamage(); ++i)
//            {
//                ModelLoader.setCustomModelResourceLocation(container.getItem(), i, modelResourceLocation);
//            }
//        }

        modelResourceLocation = new ModelResourceLocation("galacticraftcore:flag", "inventory");
        ModelLoader.setCustomModelResourceLocation(GCItems.flag, 0, modelResourceLocation);
        ModelLoader.setCustomStateMapper(GCBlocks.oxygenDetector, new StateMap.Builder().ignore(BlockOxygenDetector.ACTIVE).build());
        ModelLoader.setCustomStateMapper(GCBlocks.panelLighting, new StateMap.Builder().ignore(BlockPanelLighting.TYPE).build());
        ModelLoader.setCustomStateMapper(GCBlocks.grating, new StateMap.Builder().ignore(BlockLiquid.LEVEL).ignore(BlockFluidBase.LEVEL).build());
        BlockGrating.remapVariant(GCBlocks.gratingWater);
        BlockGrating.remapVariant(GCBlocks.gratingLava);
        BlockGrating.remapForgeVariants();
        ModelLoader.setCustomStateMapper(GCBlocks.concealedRedstone, new StateMap.Builder().ignore(BlockConcealedRedstone.POWER).build());
        ModelLoader.setCustomStateMapper(GCBlocks.concealedRepeater_Powered, new StateMap.Builder().ignore(BlockConcealedRepeater.FACING, BlockConcealedRepeater.DELAY, BlockConcealedRepeater.LOCKED).build());
        ModelLoader.setCustomStateMapper(GCBlocks.concealedRepeater_Unpowered, new StateMap.Builder().ignore(BlockConcealedRepeater.FACING, BlockConcealedRepeater.DELAY, BlockConcealedRepeater.LOCKED).build());
        ModelLoader.setCustomStateMapper(GCBlocks.concealedDetector, new StateMap.Builder().ignore(BlockConcealedDetector.FACING, BlockConcealedDetector.DETECTED).build());
    }

    @Override
    public World getClientWorld()
    {
        return ClientProxyCore.mc.world;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object[] otherInfo)
    {
        EffectHandler.spawnParticle(particleID, position, motion, otherInfo);
    }

    @Override
    public World getWorldForID(int dimensionID)
    {
        if (GCCoreUtil.getEffectiveSide() == Side.SERVER)
        {
            return WorldUtil.getWorldForDimensionServer(dimensionID);
        }

        World world = ClientProxyCore.mc.world;

        if (world != null && GCCoreUtil.getDimensionID(world) == dimensionID)
        {
            return world;
        }

        return null;
    }

    @Override
    public EntityPlayer getPlayerFromNetHandler(INetHandler handler)
    {
        if (handler instanceof NetHandlerPlayServer)
        {
            return ((NetHandlerPlayServer) handler).player;
        }
        else
        {
            return FMLClientHandler.instance().getClientPlayerEntity();
        }
    }

    @Override
    public void unregisterNetwork(FluidNetwork fluidNetwork)
    {
        super.unregisterNetwork(fluidNetwork);

        if (!GCCoreUtil.getEffectiveSide().isServer())
        {
            TickHandlerClient.removeFluidNetwork(fluidNetwork);
        }
    }

    @Override
    public void registerNetwork(FluidNetwork fluidNetwork)
    {
        super.registerNetwork(fluidNetwork);

        if (!GCCoreUtil.getEffectiveSide().isServer())
        {
            TickHandlerClient.addFluidNetwork(fluidNetwork);
        }
    }

    @Override
    public boolean isPaused()
    {
        if (FMLClientHandler.instance().getClient().isSingleplayer() && !FMLClientHandler.instance().getClient().getIntegratedServer().getPublic())
        {
            GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;

            if (screen != null)
            {
                return screen.doesGuiPauseGame();
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onTextureStitchedPre(TextureStitchEvent.Pre event)
    {
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:blocks/assembly"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:model/rocket_t1"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:model/buggy_main"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:model/buggy_storage"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:model/buggy_wheels"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:model/flag0"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:model/frequency_module"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:blocks/fluids/oxygen_gas"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:blocks/fluids/hydrogen_gas"));
        event.getMap().registerSprite(new ResourceLocation("galacticraftcore:blocks/bubble"));
        new TextureDungeonFinder("galacticraftcore:items/dungeonfinder").register(event.getMap());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        //Specified transformations only take effect on the "inventory" variant, not other variants.
        Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 225, 0));
        replaceModelDefault(event, "rocket_workbench", "block/workbench.obj", ImmutableList.of("Cube"), ItemModelWorkbench.class, new TRSRTransformation(new javax.vecmath.Vector3f(0.7F, 0.1F, 0.0F), rot, new javax.vecmath.Vector3f(0.2604F, 0.2604F, 0.2604F), new javax.vecmath.Quat4f()), "inventory", "normal");
        replaceModelDefault(event, "rocket_t1", "rocket_t1.obj", ImmutableList.of("Rocket"), ItemModelRocket.class, TRSRTransformation.identity());

        for (int i = 0; i < 4; ++i)
        {
            ImmutableList<String> objects = ImmutableList.of("MainBody", "RadarDish_Dish", "Wheel_Back_Left", "Wheel_Back_Right", "Wheel_Front_Left", "Wheel_Front_Right");
            switch (i)
            {
            case 0:
                break;
            case 1:
                objects = ImmutableList.of("MainBody", "RadarDish_Dish", "Wheel_Back_Left", "Wheel_Back_Right", "Wheel_Front_Left", "Wheel_Front_Right", "CargoLeft");
                break;
            case 2:
                objects = ImmutableList.of("MainBody", "RadarDish_Dish", "Wheel_Back_Left", "Wheel_Back_Right", "Wheel_Front_Left", "Wheel_Front_Right", "CargoLeft", "CargoMid");
                break;
            case 3:
                objects = ImmutableList.of("MainBody", "RadarDish_Dish", "Wheel_Back_Left", "Wheel_Back_Right", "Wheel_Front_Left", "Wheel_Front_Right", "CargoLeft", "CargoMid", "CargoRight");
                break;
            }
            replaceModelDefault(event, "buggy" + (i > 0 ? "_" + i : ""), "buggy_inv.obj", objects, ItemModelBuggy.class, TRSRTransformation.identity());
        }

        replaceModelDefault(event, "flag", "flag.obj", ImmutableList.of("Flag", "Pole"), ItemModelFlag.class, TRSRTransformation.identity());
        ModelResourceLocation blockLoc = new ModelResourceLocation(Constants.ASSET_PREFIX + ":panel_lighting", "normal");
        ModelResourceLocation defaultLoc;
        if (GalacticraftCore.isPlanetsLoaded)
        {
            defaultLoc = new ModelResourceLocation(GalacticraftPlanets.ASSET_PREFIX + ":asteroids_block", "basictypeasteroids=asteroid_deco");
        }
        else
        {
            defaultLoc = new ModelResourceLocation(Constants.ASSET_PREFIX + ":basic_block_core", "basictype=deco_block_1");
        }
        event.getModelRegistry().putObject(blockLoc, new ModelPanelLightBase(defaultLoc));
        defaultLoc = new ModelResourceLocation(Constants.ASSET_PREFIX + ":grating", "normal");
        event.getModelRegistry().putObject(defaultLoc, new ModelGrating(defaultLoc, event.getModelManager()));
        for (int i = 1; i < BlockGrating.number; i++)
        {
            blockLoc = new ModelResourceLocation(Constants.ASSET_PREFIX + ":grating" + i, "normal");
            event.getModelRegistry().putObject(blockLoc, new ModelGrating(defaultLoc, event.getModelManager()));
        }
//
//        for (PartialCanister container : ClientProxyCore.canisters)
//        {
//            for (int i = 0; i < container.getTextureCount(); ++i)
//            {
//                ModelResourceLocation modelResourceLocation = new ModelResourceLocation(container.getModID() + ":" + container.getBaseName() + "_" + i, "inventory");
//                IBakedModel object = event.modelRegistry.getObject(modelResourceLocation);
//                ItemLiquidCanisterModel modelFinal = new ItemLiquidCanisterModel(object);
//                event.modelRegistry.putObject(modelResourceLocation, modelFinal);
//            }
//        }
    }

    /**
     * Specified parentState transformations only take effect on the "inventory" variant, not other variants
     * Make sure that identity gives the correct model for other variants!
     * Used for example by the NASA Workbench: transform the model for inventory but not for normal model
     */
    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
    {
        ClientUtil.replaceModel(Constants.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
    }

    public static void registerEntityRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityTier1Rocket.class, (RenderManager manager) -> new RenderTier1Rocket(manager, new ModelRocketTier1(), Constants.ASSET_PREFIX, "rocket_t1"));
        RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedSpider.class, (RenderManager manager) -> new RenderEvolvedSpider(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedZombie.class, (RenderManager manager) -> new RenderEvolvedZombie(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedCreeper.class, (RenderManager manager) -> new RenderEvolvedCreeper(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedSkeleton.class, (RenderManager manager) -> new RenderEvolvedSkeleton(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonBoss.class, (RenderManager manager) -> new RenderEvolvedSkeletonBoss(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, (RenderManager manager) -> new RenderMeteor(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityFlag.class, (RenderManager manager) -> new RenderFlag(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityParachest.class, (RenderManager manager) -> new RenderParaChest(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityAlienVillager.class, (RenderManager manager) -> new RenderAlienVillager(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityLander.class, (RenderManager manager) -> new RenderLander(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityCelestialFake.class, (RenderManager manager) -> new RenderEntityFake(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityBuggy.class, (RenderManager manager) -> new RenderBuggy(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityMeteorChunk.class, (RenderManager manager) -> new RenderMeteorChunk(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityHangingSchematic.class, (RenderManager manager) -> new RenderSchematic(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedEnderman.class, (RenderManager manager) -> new RenderEvolvedEnderman(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedWitch.class, (RenderManager manager) -> new RenderEvolvedWitch(manager));
    }

    private static void registerHandlers()
    {
        TickHandlerClient tickHandlerClient = new TickHandlerClient();
        MinecraftForge.EVENT_BUS.register(tickHandlerClient);
        MinecraftForge.EVENT_BUS.register(new KeyHandlerClient());
        ClientRegistry.registerKeyBinding(KeyHandlerClient.galaxyMap);
        ClientRegistry.registerKeyBinding(KeyHandlerClient.openFuelGui);
        ClientRegistry.registerKeyBinding(KeyHandlerClient.toggleAdvGoggles);
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }

    private static void registerTileEntityRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChest.class, new TileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolar.class, new TileEntitySolarPanelRenderer());
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOxygenDistributor.class, new TileEntityBubbleProviderRenderer<>(0.25F, 0.25F, 1.0F));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScreen.class, new TileEntityScreenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTank.class, new TileEntityFluidTankRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidPipe.class, new TileEntityFluidPipeRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDish.class, new TileEntityDishRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThruster.class, new TileEntityThrusterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArclamp.class, new TileEntityArclampRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPanelLight.class, new TileEntityPanelLightRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlatform.class, new TileEntityPlatformRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEmergencyBox.class, new TileEntityEmergencyBoxRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidPipe.class, new TileEntityOxygenPipeRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOxygenStorageModule.class, new TileEntityMachineRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCircuitFabricator.class, new TileEntityMachineRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectricIngotCompressor.class, new TileEntityMachineRenderer());
    }

    private static void registerInventoryJsons()
    {
        for (Item toReg : ClientProxyCore.itemsToRegisterJson)
        {
            ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, toReg);
        }

        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.canister, 0, "canister");  //This was canister_tin
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.canister, 1, "canister_copper");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.rocketEngine, 0, "engine");  //This was tier1engine
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.rocketEngine, 1, "tier1booster");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 0, "parachute");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 1, "parachute_black");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 2, "parachute_blue");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 3, "parachute_lime");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 4, "parachute_brown");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 5, "parachute_darkblue");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 6, "parachute_darkgray");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 7, "parachute_darkgreen");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 8, "parachute_gray");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 9, "parachute_magenta");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 10, "parachute_orange");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 11, "parachute_pink");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 12, "parachute_purple");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 13, "parachute_red");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 14, "parachute_teal");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.parachute, 15, "parachute_yellow");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.schematic, 0, "schematic");   //This was schematic_buggy
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.schematic, 1, "schematic_rocket_t2");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.key, 0, "key");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.partBuggy, 0, "buggymat");  //This was wheel
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.partBuggy, 1, "seat");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.partBuggy, 2, "storage");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 0, "basic_item");  //This was solar_module_0
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 1, "solar_module_1");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 2, "raw_silicon");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 3, "ingot_copper");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 4, "ingot_tin");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 5, "ingot_aluminum");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 6, "compressed_copper");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 7, "compressed_tin");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 8, "compressed_aluminum");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 9, "compressed_steel");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 10, "compressed_bronze");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 11, "compressed_iron");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 12, "wafer_solar");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 13, "wafer_basic");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 14, "wafer_advanced");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 15, "food");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 16, "dehydrated_carrot");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 17, "dehydrated_melon");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 18, "dehydrated_potato");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 19, "frequency_module");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.basicItem, 20, "ambient_thermal_controller");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.foodItem, 0, "food");
        for (int j = 1; j < ItemFood.names.length; j++)
        {
            ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.foodItem, j, ItemFood.names[j]);
        }
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.itemBasicMoon, 0, "item_basic_moon");  //This was meteoric_iron_ingot
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.itemBasicMoon, 1, "compressed_meteoric_iron");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.itemBasicMoon, 2, "lunar_sapphire");
        if (CompatibilityManager.isIc2Loaded())
        {
            ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.ic2compat, 0, "ic2compat");
            ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.ic2compat, 1, "ic2_ore_purified_alu");
            ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.ic2compat, 2, "ic2_ore_crushed_alu");
            ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.ic2compat, 7, "ic2_dust_small_titanium");
        }

        for (PartialCanister container : ClientProxyCore.canisters)
        {
            for (int i = 0; i <= container.getItem().getMaxDamage(); ++i)
            {
                final int levels = container.getTextureCount() - 1;
                final int fillLevel = levels - levels * i / container.getItem().getMaxDamage();
                ClientUtil.registerItemJson(container.getModID() + ":", container.getItem(), i, container.getBaseName() + (fillLevel > 0 ? "_" + fillLevel : ""));
            }
        }

        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.meteorChunk, 0, "meteor_chunk");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.meteorChunk, 1, "meteor_chunk_hot");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.buggy, 0, "buggy");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.buggy, 1, "buggy_1");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.buggy, 2, "buggy_2");
        ClientUtil.registerItemJson(Constants.TEXTURE_PREFIX, GCItems.buggy, 3, "buggy_3");

        // Blocks
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.breatheableAir);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.brightAir);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.brightBreatheableAir);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.brightLamp);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.treasureChestTier1);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.landingPad, 0, "landing_pad");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.landingPad, 1, "buggy_pad");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.unlitTorch);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.unlitTorchLit);
        for (Block torch : GCBlocks.otherModTorchesLit)
        {
            ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, torch);
        }
        for (Block torch : GCBlocks.otherModTorchesUnlit)
        {
            ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, torch);
        }
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenDistributor);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenPipe);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenPipePull);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenCollector);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenCompressor, 0, "oxygen_compressor");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenCompressor, 4, "oxygen_decompressor");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenSealer);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.oxygenDetector);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.nasaWorkbench);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.radioTelescope);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.fallenMeteor);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 3, "deco_block_0");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 4, "deco_block_1");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 5, "ore_copper_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 6, "ore_tin_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 7, "ore_aluminum_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 8, "ore_silicon");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 9, "block_copper_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 10, "block_tin_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 11, "block_aluminum_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 12, "block_meteoric_iron_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.basicBlock, 13, "block_silicon_gc");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.airLockFrame, 0, "air_lock_frame");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.airLockFrame, 1, "air_lock_controller");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.airLockSeal, 0, "air_lock_seal");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spaceGlassClear, 0, "space_glass_clear");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spaceGlassVanilla, 0, "space_glass_vanilla");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spaceGlassStrong, 0, "space_glass_strong");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spaceGlassClear, 1, "space_glass_tin_clear");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spaceGlassVanilla, 1, "space_glass_tin_vanilla");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spaceGlassStrong, 1, "space_glass_tin_strong");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.crafting);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.refinery);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.fuelLoader);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.landingPadFull, 0, "landing_pad_full");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.landingPadFull, 1, "buggy_pad_full");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spaceStationBase);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.fakeBlock);
        for (BlockEnclosed.EnumEnclosedBlockType type : BlockEnclosed.EnumEnclosedBlockType.values())
        {
            ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.sealableBlock, type.getMeta(), type == BlockEnclosed.EnumEnclosedBlockType.ALUMINUM_WIRE ? "enclosed" : type.getName());
        }
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.cargoLoader, 0, "cargo");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.cargoLoader, 4, "cargo_unloader");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.parachest);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.solarPanel, 0, "solar");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.solarPanel, 4, "advanced_solar");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase, 0, "coal_generator");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase, 12, "ingot_compressor");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase2, 0, "electric_ingot_compressor");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase2, 4, "circuit_fabricator");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase2, 8, "oxygen_storage_module");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase2, 12, "deconstructor");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineTiered, 0, "energy_storage");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineTiered, 4, "electric_furnace");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineTiered, 8, "cluster_storage");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineTiered, 12, "arc_furnace");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase3, 0, "painter");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.machineBase4, 0, "advanced_compressor");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.aluminumWire, 0, "aluminum_wire");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.aluminumWire, 1, "aluminum_wire_heavy");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.aluminumWire, 2, "aluminum_wire_switch");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.aluminumWire, 3, "aluminum_wire_switch_heavy");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.panelLighting, 0, "panel_lighting");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.panelLighting, 1, "panel_lighting_1");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.panelLighting, 2, "panel_lighting_2");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.panelLighting, 3, "panel_lighting_3");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.panelLighting, 4, "panel_lighting_4");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.platform);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.emergencyBox, 0, "emergency_box");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.emergencyBox, 1, "emergency_box_full");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.grating);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.glowstoneTorch);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 0, "ore_copper_moon");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 1, "ore_tin_moon");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 2, "ore_cheese_moon");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 3, "moon_dirt_moon");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 4, "basic_block_moon");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 5, "moon_turf");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 6, "ore_sapphire_moon");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.blockMoon, 14, "moon_dungeon_brick");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.cheeseBlock);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.spinThruster);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.screen);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.telemetry);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.fluidTank);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.slabGCHalf, 0, "slab_tin_1");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.slabGCHalf, 1, "slab_tin_2");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.slabGCHalf, 2, "slab_moon_stone");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.slabGCHalf, 3, "slab_moon_dungeon_brick");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.slabGCHalf, 4, "slab_mars_cobblestone");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.slabGCHalf, 5, "slab_mars_dungeon_brick");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.slabGCHalf, 6, "slab_asteroid");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.tinStairs1);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.tinStairs2);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.moonStoneStairs);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.moonBricksStairs);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.wallGC, 0, "wall_tin_1");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.wallGC, 1, "wall_tin_2");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.wallGC, 2, "wall_moon_stone");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.wallGC, 3, "wall_moon_dungeon_brick");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.wallGC, 4, "wall_mars_cobblestone");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.wallGC, 5, "wall_mars_dungeon_brick");
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.bossSpawner);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.concealedRedstone);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.concealedRepeater_Powered);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.concealedRepeater_Unpowered);
        ClientUtil.registerBlockJson(Constants.TEXTURE_PREFIX, GCBlocks.concealedDetector);
        //TODO: doubleslabs, fluids - and all the remaining meta-dependent block models (e.g. machine, machine2) have no 'inventory' variant for the meta-less block...
    }

    private static void addVariants()
    {
        //BlockItem variants: 
        addCoreVariant("air_lock_frame", "air_lock_frame", "air_lock_controller");
        addCoreVariant("basic_block_core", "deco_block_0", "deco_block_1", "ore_copper_gc", "ore_tin_gc", "ore_aluminum_gc", "ore_silicon", "block_copper_gc", "block_tin_gc", "block_aluminum_gc", "block_meteoric_iron_gc", "block_silicon_gc");
        addCoreVariant("air_lock_frame", "air_lock_frame", "air_lock_controller");
        addCoreVariant("landing_pad", "landing_pad", "buggy_pad");
        addCoreVariant("oxygen_compressor", "oxygen_compressor", "oxygen_decompressor");
        addCoreVariant("cargo", "cargo", "cargo_unloader");
        addCoreVariant("enclosed", "enclosed_hv_cable", "enclosed_fluid_pipe", "enclosed_copper_cable", "enclosed_gold_cable", "enclosed_te_conduit", "enclosed_glass_fibre_cable", "enclosed_lv_cable", "enclosed_pipe_items_stone", "enclosed_pipe_items_cobblestone", "enclosed_pipe_fluids_stone", "enclosed_pipe_fluids_cobblestone", "enclosed_pipe_power_stone", "enclosed_pipe_power_gold", "enclosed_me_cable", "enclosed", "enclosed_heavy_aluminum_wire");
        addCoreVariant("solar", "advanced_solar", "solar");
        addCoreVariant("machine", "coal_generator", "ingot_compressor");
        addCoreVariant("machine2", "circuit_fabricator", "oxygen_storage_module", "electric_ingot_compressor", "deconstructor");
        addCoreVariant("machine3", "painter");
        addCoreVariant("machine4", "advanced_compressor");
        addCoreVariant("machine_tiered", "energy_storage", "electric_furnace", "cluster_storage", "arc_furnace");
        addCoreVariant("basic_block_moon", "ore_copper_moon", "ore_tin_moon", "ore_cheese_moon", "moon_dirt_moon", "basic_block_moon", "moon_turf", "ore_sapphire_moon", "moon_dungeon_brick");
        addCoreVariant("aluminum_wire", "aluminum_wire", "aluminum_wire_heavy", "aluminum_wire_switch", "aluminum_wire_switch_heavy");
        addCoreVariant("slab_gc_half", "slab_tin_1", "slab_tin_2", "slab_moon_stone", "slab_moon_dungeon_brick", "slab_mars_cobblestone", "slab_mars_dungeon_brick", "slab_asteroid");
        addCoreVariant("wall_gc", "wall_tin_1", "wall_tin_2", "wall_moon_stone", "wall_moon_dungeon_brick", "wall_mars_cobblestone", "wall_mars_dungeon_brick");
        addCoreVariant("space_glass_clear", "space_glass_clear", "space_glass_tin_clear");
        addCoreVariant("space_glass_vanilla", "space_glass_vanilla", "space_glass_tin_vanilla");
        addCoreVariant("space_glass_strong", "space_glass_strong", "space_glass_tin_strong");
        addCoreVariant("panel_lighting", "panel_lighting", "panel_lighting_1", "panel_lighting_2", "panel_lighting_3", "panel_lighting_4");
        addCoreVariant("emergency_box", "emergency_box", "emergency_box_full");

        //Item variants: best if the damage=0 variant has the registered item name, to avoid ModelLoader errors for the #inventory variant
        addCoreVariant("canister", "canister", "canister_copper");
        addCoreVariant("engine", "engine", "tier1booster");
        addCoreVariant("parachute", "parachute", "parachute_black", "parachute_blue", "parachute_lime", "parachute_brown", "parachute_darkblue", "parachute_darkgray", "parachute_darkgreen", "parachute_gray", "parachute_magenta", "parachute_orange", "parachute_pink", "parachute_purple", "parachute_red", "parachute_teal", "parachute_yellow");
        addCoreVariant("schematic", "schematic", "schematic_rocket_t2");
        addCoreVariant("key", "key");
        addCoreVariant("buggymat", "buggymat", "seat", "storage");
        addCoreVariant("basic_item", "basic_item", "solar_module_1", "raw_silicon", "ingot_copper", "ingot_tin", "ingot_aluminum", "compressed_copper", "compressed_tin", "compressed_aluminum", "compressed_steel", "compressed_bronze", "compressed_iron", "wafer_solar", "wafer_basic", "wafer_advanced", "food", "dehydrated_carrot", "dehydrated_melon", "dehydrated_potato", "frequency_module", "ambient_thermal_controller");
        addCoreVariant("food", "food", "dehydrated_carrot", "dehydrated_melon", "dehydrated_potato", ItemFood.names[4], ItemFood.names[5], ItemFood.names[6], ItemFood.names[7], ItemFood.names[8], ItemFood.names[9]);
        addCoreVariant("item_basic_moon", "item_basic_moon", "compressed_meteoric_iron", "lunar_sapphire");
        addCoreVariant("meteor_chunk", "meteor_chunk", "meteor_chunk_hot");
        addCoreVariant("buggy", "buggy", "buggy_1", "buggy_2", "buggy_3");
        if (CompatibilityManager.isIc2Loaded()) addCoreVariant("ic2compat", "ic2compat", "ic2_ore_purified_alu", "ic2_ore_crushed_alu", "ic2_dust_small_titanium");

        for (PartialCanister container : ClientProxyCore.canisters)
        {
            String[] variants = new String[container.getTextureCount()];
            for (int i = 0; i < container.getTextureCount(); ++i)
            {
                variants[i] = container.getBaseName() + (i > 0 ? "_" + i : "");
            }
            ClientUtil.addVariant(container.getModID(), container.getBaseName(), variants);
        }
    }

    private static void addCoreVariant(String name, String... variants)
    {
        ClientUtil.addVariant(Constants.MOD_ID_CORE, name, variants);
    }

    private static void updateCapeList()
    {
        int timeout = 10000;
        URL capeListUrl;

        try
        {
            capeListUrl = new URL("https://raw.github.com/micdoodle8/Galacticraft/master/capes-uuid.txt");
        }
        catch (IOException e)
        {
            GCLog.severe("Error getting capes list URL");
            if (ConfigManagerCore.enableDebug) e.printStackTrace();
            return;
        }

        URLConnection connection;

        try
        {
            connection = capeListUrl.openConnection();
        }
        catch (IOException e)
        {
            if (ConfigManagerCore.enableDebug) e.printStackTrace();
            return;
        }

        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        InputStream stream;

        try
        {
            stream = connection.getInputStream();
        }
        catch (IOException e)
        {
            if (ConfigManagerCore.enableDebug) e.printStackTrace();
            return;
        }

        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                if (line.contains(":"))
                {
                    capeMap.put(line.split(":")[0], new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/capes/cape_" + line.split(":")[1].split(" ")[0].substring(4).toLowerCase() + ".png"));
                }
            }
        }
        catch (IOException e)
        {
            if (ConfigManagerCore.enableDebug)e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                if (ConfigManagerCore.enableDebug) e.printStackTrace();
            }
        }
    }

    public static void registerInventoryTabs()
    {
        if (TabRegistry.getTabList().size() == 0)
        {
            TabRegistry.registerTab(new InventoryTabVanilla());
        }

        TabRegistry.registerTab(new InventoryTabGalacticraft());
    }

    @Override
    public void registerCanister(PartialCanister container)
    {
        ClientProxyCore.canisters.add(container);
    }

    @Override
    public void registerFluidTexture(Fluid fluid, ResourceLocation submergedTexture)
    {
        ClientProxyCore.submergedTextures.put(fluid, submergedTexture);
    }

    public static class EventSpecialRender extends Event
    {
        public final float partialTicks;

        public EventSpecialRender(float partialTicks)
        {
            this.partialTicks = partialTicks;
        }
    }
    
    @Override
    public PlayerGearData getGearData(EntityPlayer player)
    {
        PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getName());

        if (gearData == null)
        {
            UUID id = player.getUniqueID();

            if (!ClientProxyCore.gearDataRequests.contains(id))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA2, GCCoreUtil.getDimensionID(player.world), new Object[] { id }));
                ClientProxyCore.gearDataRequests.add(id);
            }
        }

        return gearData;
    }
}
