package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.venus.client.TickHandlerClientVenus;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.EntityAcidVaporFX;
import micdoodle8.mods.galacticraft.planets.venus.client.render.RenderJuicer;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class VenusModuleClient implements IPlanetsModuleClient
{
    private static ModelResourceLocation sludgeLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sludge", "fluid");

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        addPlanetVariants("venus", "venus_rock_0", "venus_rock_1", "venus_rock_2", "venus_rock_3", "dungeon_brick_venus_1", "dungeon_brick_venus_2");
        MinecraftForge.EVENT_BUS.register(this);
        RenderingRegistry.registerEntityRenderingHandler(EntityJuicer.class, (RenderManager manager) -> new RenderJuicer(manager));
    }

    private void addPlanetVariants(String name, String... variants)
    {
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
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
        registerTexture(event, "rocketT2");
        registerTexture(event, "cargoRocket");
        registerTexture(event, "landingBalloon");
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.map.registerSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
    }

    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState)
    {
        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    public static void registerBlockRenderers()
    {
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 0, "venus_rock_0");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 1, "venus_rock_1");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 2, "venus_rock_2");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 3, "venus_rock_3");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 4, "dungeon_brick_venus_1");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 5, "dungeon_brick_venus_2");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.spout, 0, "spout");
    }

    private void addVariants(String name, ResourceLocation... variants)
    {
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
        ModelBakery.registerItemVariants(itemBlockVariants, variants);
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
//            final double dPosX = mc.getRenderViewEntity().posX - position.x;
//            final double dPosY = mc.getRenderViewEntity().posY - position.y;
//            final double dPosZ = mc.getRenderViewEntity().posZ - position.z;
            EntityFX particle = null;
//            final double maxDistSqrd = 64.0D;

            if (particleID.equals("acidVapor"))
            {
                particle = new EntityAcidVaporFX(mc.theWorld, position.x, position.y, position.z, motion.x, motion.y, motion.z, 2.5F);
            }

            if (particle != null)
            {
                particle.prevPosX = particle.posX;
                particle.prevPosY = particle.posY;
                particle.prevPosZ = particle.posZ;
                mc.effectRenderer.addEffect(particle);
            }
        }
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
    }
}
