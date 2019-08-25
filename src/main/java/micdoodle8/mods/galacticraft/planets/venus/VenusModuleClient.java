package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.api.client.IItemMeshDefinitionCustom;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.venus.client.TickHandlerClientVenus;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.ParticleAcidExhaust;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.ParticleAcidVapor;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiGeothermal;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiSolarArrayController;
import micdoodle8.mods.galacticraft.planets.venus.client.render.entity.RenderEntryPodVenus;
import micdoodle8.mods.galacticraft.planets.venus.client.render.entity.RenderJuicer;
import micdoodle8.mods.galacticraft.planets.venus.client.render.entity.RenderSpiderQueen;
import micdoodle8.mods.galacticraft.planets.venus.client.render.entity.RenderWebShot;
import micdoodle8.mods.galacticraft.planets.venus.client.render.tile.TileEntityLaserTurretRenderer;
import micdoodle8.mods.galacticraft.planets.venus.client.render.tile.TileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntitySpiderQueen;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityWebShot;
import micdoodle8.mods.galacticraft.planets.venus.tile.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class VenusModuleClient implements IPlanetsModuleClient
{
    private static ModelResourceLocation sulphuricAcidLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sulphuric_acid", "fluid");

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        RenderingRegistry.registerEntityRenderingHandler(EntityJuicer.class, (RenderManager manager) -> new RenderJuicer(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEntryPodVenus.class, (RenderManager manager) -> new RenderEntryPodVenus(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiderQueen.class, (RenderManager manager) -> new RenderSpiderQueen(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityWebShot.class, (RenderManager manager) -> new RenderWebShot(manager));
    }

    private void addPlanetVariants(String name, String... variants)
    {
        Item itemBlockVariants = Item.REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID_PLANETS, name));
        ResourceLocation[] variants0 = new ResourceLocation[variants.length];
        for (int i = 0; i < variants.length; ++i)
        {
            variants0[i] = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + variants[i]);
        }
        ModelBakery.registerItemVariants(itemBlockVariants, variants0);
    }

    @Override
    public void registerVariants()
    {
        addPlanetVariants("venus", "venus_rock_0", "venus_rock_1", "venus_rock_2", "venus_rock_3", "dungeon_brick_venus_1", "dungeon_brick_venus_2", "venus_ore_aluminum", "venus_ore_copper", "venus_ore_galena", "venus_ore_quartz", "venus_ore_silicon", "venus_ore_tin", "lead_block", "venus_ore_solar");
        addPlanetVariants("thermal_padding_t2", "thermal_helm_t2", "thermal_chestplate_t2", "thermal_leggings_t2", "thermal_boots_t2");
        addPlanetVariants("basic_item_venus", "shield_controller", "ingot_lead", "radioisotope_core", "thermal_fabric", "solar_dust", "solar_module_2", "thin_solar_wafer");
        addPlanetVariants("web_torch", "web_torch_0", "web_torch_1");

        Item sludge = Item.getItemFromBlock(VenusBlocks.sulphuricAcid);
        ModelBakery.registerItemVariants(sludge, new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sulphuric_acid"));
        ModelLoader.setCustomMeshDefinition(sludge, IItemMeshDefinitionCustom.create((ItemStack stack) -> sulphuricAcidLocation));
        ModelLoader.setCustomStateMapper(VenusBlocks.sulphuricAcid, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return sulphuricAcidLocation;
            }
        });
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new TickHandlerClientVenus());
        VenusModuleClient.registerBlockRenderers();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void loadTextures(TextureStitchEvent.Pre event)
    {
        registerTexture(event, "pod_flame");
        registerTexture(event, "web");
        registerTexture(event, "laser");
        registerTexture(event, "laser_off");
        registerTexture(event, "orb");
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.getMap().registerSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
    }

    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
    {
        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestVenus.class, new TileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserTurret.class, new TileEntityLaserTurretRenderer());
    }

    public static void registerBlockRenderers()
    {
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 0, "venus_rock_0");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 1, "venus_rock_1");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 2, "venus_rock_2");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 3, "venus_rock_3");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 4, "dungeon_brick_venus_1");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 5, "dungeon_brick_venus_2");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 6, "venus_ore_aluminum");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 7, "venus_ore_copper");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 8, "venus_ore_galena");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 9, "venus_ore_quartz");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 10, "venus_ore_silicon");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 11, "venus_ore_tin");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 12, "lead_block");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 13, "venus_ore_solar");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.spout, 0, "spout");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.treasureChestTier3, 0, "treasure_t3");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.torchWeb, 0, "web_torch_0");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.torchWeb, 1, "web_torch_1");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.geothermalGenerator, 0, "geothermal_generator");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.crashedProbe);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.scorchedRock);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.bossSpawner);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.solarArrayModule);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.solarArrayController, 0, "solar_array_controller");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.laserTurret);
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 0, "thermal_helm_t2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 1, "thermal_chestplate_t2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 2, "thermal_leggings_t2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 3, "thermal_boots_t2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 0, "shield_controller");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 1, "ingot_lead");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 2, "radioisotope_core");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 3, "thermal_fabric");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 4, "solar_dust");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 5, "solar_module_2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 6, "thin_solar_wafer");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.key, 0, "key_t3");
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (side == Side.CLIENT)
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

            if (ID == GuiIdsPlanets.MACHINE_VENUS)
            {
                if (tile instanceof TileEntityGeothermalGenerator)
                {
                    return new GuiGeothermal(player.inventory, (TileEntityGeothermalGenerator) tile);
                }
                else if (tile instanceof TileEntityCrashedProbe)
                {
                    return new GuiCrashedProbe(player.inventory, (TileEntityCrashedProbe) tile);
                }
                else if (tile instanceof TileEntitySolarArrayController)
                {
                    return new GuiSolarArrayController(player.inventory, (TileEntitySolarArrayController) tile);
                }
                else if (tile instanceof TileEntityLaserTurret)
                {
                    return new GuiLaserTurret(player.inventory, (TileEntityLaserTurret) tile);
                }
            }
        }

        return null;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            double dX = mc.getRenderViewEntity().posX - position.x;
            double dY = mc.getRenderViewEntity().posY - position.y;
            double dZ = mc.getRenderViewEntity().posZ - position.z;
            Particle particle = null;
            double viewDistance = 64.0D;

            if (particleID.equals("acidVapor"))
            {
                particle = new ParticleAcidVapor(mc.world, position.x, position.y, position.z, motion.x, motion.y, motion.z, 2.5F);
            }

            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
            {
                if (particleID.equals("acidExhaust"))
                {
                    particle = new ParticleAcidExhaust(mc.world, position.x, position.y, position.z, motion.x, motion.y, motion.z, 0.5F);
                }
            }

            if (particle != null)
            {
                mc.effectRenderer.addEffect(particle);
            }
        }
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
        idList.add(GuiIdsPlanets.MACHINE_VENUS);
    }
}
