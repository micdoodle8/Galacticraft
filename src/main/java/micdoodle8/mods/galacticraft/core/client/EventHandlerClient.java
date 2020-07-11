package micdoodle8.mods.galacticraft.core.client;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class EventHandlerClient
{
    public static Minecraft mc = Minecraft.getInstance();
    public static boolean sneakRenderOverride;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    //Lowest priority to do the PushMatrix last, just before vanilla RenderPlayer - this also means if it gets cancelled first by another mod, this will never be called
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event)
    {
        GL11.glPushMatrix();

        final PlayerEntity player = event.getEntityPlayer();

        if (player.getRidingEntity() instanceof ICameraZoomEntity && player == Minecraft.getInstance().player
                && Minecraft.getInstance().gameSettings.thirdPersonView == 0)
        {
            Entity entity = player.getRidingEntity();
            float rotateOffset = ((ICameraZoomEntity) entity).getRotateOffset();
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

        if (player instanceof ClientPlayerEntity)
        {
            sneakRenderOverride = true;
        }

        //Gravity - freefall - jetpack changes in player model orientation can go here
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    //Highest priority to do the PushMatrix first, just after vanilla RenderPlayer
    public void onRenderPlayerPost(RenderPlayerEvent.Post event)
    {
        GL11.glPopMatrix();

        if (event.getEntityPlayer() instanceof ClientPlayerEntity)
        {
            sneakRenderOverride = false;
        }
    }

    @SubscribeEvent
    public void onRenderPlanetPre(CelestialBodyRenderEvent.Pre event)
    {
        if (event.celestialBody == GalacticraftCore.planetOverworld)
        {
            if (!ClientProxyCore.overworldTextureRequestSent)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionType(mc.world), new Object[]{}));
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
                mc.textureManager.bindTexture(ClientProxyCore.saturnRingTexture);
                float size = ((GuiCelestialSelection) mc.currentScreen).getWidthForCelestialBody(event.celestialBody) / 6.0F;
                ((GuiCelestialSelection) mc.currentScreen).blit(-7.5F * size, -1.75F * size, 15.0F * size, 3.5F * size, 0, 0, 30, 7, false, false, 32, 32);
            }
            else if (event.celestialBody == GalacticraftCore.planetUranus)
            {
                mc.textureManager.bindTexture(ClientProxyCore.uranusRingTexture);
                float size = ((GuiCelestialSelection) mc.currentScreen).getWidthForCelestialBody(event.celestialBody) / 6.0F;
                ((GuiCelestialSelection) mc.currentScreen).blit(-1.75F * size, -7.0F * size, 3.5F * size, 14.0F * size, 0, 0, 7, 28, false, false, 32, 32);
            }
        }
    }
}
