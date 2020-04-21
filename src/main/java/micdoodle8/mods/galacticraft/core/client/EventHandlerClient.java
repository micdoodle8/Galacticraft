package micdoodle8.mods.galacticraft.core.client;

import com.google.common.base.Function;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

public class EventHandlerClient
{
    public static Minecraft mc = FMLClientHandler.instance().getClient();
    public static boolean sneakRenderOverride;
    public static IBakedModel[] fluidPipeModels = new IBakedModel[6];

    @SubscribeEvent(priority = EventPriority.LOWEST)  //Lowest priority to do the PushMatrix last, just before vanilla RenderPlayer - this also means if it gets cancelled first by another mod, this will never be called
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event)
    {
        GL11.glPushMatrix();

        final EntityPlayer player = event.getEntityPlayer();

        if (player.getRidingEntity() instanceof ICameraZoomEntity && player == Minecraft.getMinecraft().player
                && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
        {
            Entity entity = player.getRidingEntity();
            float rotateOffset = ((ICameraZoomEntity)entity).getRotateOffset();
            if (rotateOffset > -10F)
            {
                rotateOffset += ClientProxyCore.PLAYER_Y_OFFSET;
                GL11.glTranslatef(0, -rotateOffset, 0);
                float anglePitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * event.getPartialRenderTick();
                float angleYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * event.getPartialRenderTick();
            GL11.glRotatef(-angleYaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(anglePitch, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0, rotateOffset, 0);
            }
        }

        if (player instanceof EntityPlayerSP)
        {
            sneakRenderOverride = true;
        }

        //Gravity - freefall - jetpack changes in player model orientation can go here
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)  //Highest priority to do the PushMatrix first, just after vanilla RenderPlayer
    public void onRenderPlayerPost(RenderPlayerEvent.Post event)
    {
        GL11.glPopMatrix();

        if (event.getEntityPlayer() instanceof EntityPlayerSP)
            sneakRenderOverride = false;
    }

    @SubscribeEvent
    public void onRenderPlanetPre(CelestialBodyRenderEvent.Pre event)
    {
        if (event.celestialBody == GalacticraftCore.planetOverworld)
        {
            if (!ClientProxyCore.overworldTextureRequestSent)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionID(mc.world), new Object[] {}));
                ClientProxyCore.overworldTextureRequestSent = true;
            }

            if (ClientProxyCore.overworldTexturesValid)
            {
                event.celestialBodyTexture = null;
                GlStateManager.bindTexture(ClientProxyCore.overworldTextureClient.getGlTextureId());
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlanetPost(CelestialBodyRenderEvent.Post event)
    {
        if (mc.currentScreen instanceof GuiCelestialSelection)
        {
            if (event.celestialBody == GalacticraftCore.planetSaturn)
            {
                mc.renderEngine.bindTexture(ClientProxyCore.saturnRingTexture);
                float size = ((GuiCelestialSelection)mc.currentScreen).getWidthForCelestialBody(event.celestialBody) / 6.0F;
                ((GuiCelestialSelection) mc.currentScreen).drawTexturedModalRect(-7.5F * size, -1.75F * size, 15.0F * size, 3.5F * size, 0, 0, 30, 7, false, false, 32, 32);
            }
            else if (event.celestialBody == GalacticraftCore.planetUranus)
            {
                mc.renderEngine.bindTexture(ClientProxyCore.uranusRingTexture);
                float size = ((GuiCelestialSelection)mc.currentScreen).getWidthForCelestialBody(event.celestialBody) / 6.0F;
                ((GuiCelestialSelection) mc.currentScreen).drawTexturedModalRect(-1.75F * size, -7.0F * size, 3.5F * size, 14.0F * size, 0, 0, 7, 28, false, false, 32, 32);
            }
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event)
    {
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            try
            {
                // Get the first character of the direction name (n/e/s/w/u/d)
                Character c = Character.toLowerCase(facing.getName().charAt(0));
                IModel model;
                synchronized (ModelLoaderRegistry.class)
                {
                    model = ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "block/fluid_pipe_pull_" + c));
                }
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = (ResourceLocation location) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                fluidPipeModels[facing.ordinal()] = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
