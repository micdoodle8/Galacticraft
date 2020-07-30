package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import com.mojang.blaze3d.platform.GLX;
import micdoodle8.mods.galacticraft.api.item.ISensorGlassesArmor;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class OverlaySensorGlasses extends Overlay
{
    private static final ResourceLocation hudTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/hud.png");
    private static final ResourceLocation indicatorTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/indicator.png");
    public static final ResourceLocation altTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/sensor_mobs.png");

    private static final Minecraft minecraft = Minecraft.getInstance();

    private static int zoom = 0;

    /**
     * Render the GUI that displays sensor glasses
     */
    public static void renderSensorGlassesMain(ItemStack stack, PlayerEntity player, float partialTicks)
    {
        OverlaySensorGlasses.zoom++;

        final float f = MathHelper.sin(OverlaySensorGlasses.zoom / 80.0F) * 0.1F + 0.1F;

//        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(OverlaySensorGlasses.minecraft, OverlaySensorGlasses.minecraft.displayWidth, OverlaySensorGlasses.minecraft.displayHeight);
//        final int i = scaledresolution.getScaledWidth();
//        final int k = scaledresolution.getScaledHeight();
//        OverlaySensorGlasses.minecraft.entityRenderer.setupOverlayRendering();
        Minecraft mc = Minecraft.getInstance();
        int width = (int) (mc.mouseHelper.getMouseX() * (double) mc.getMainWindow().getScaledWidth() / (double) mc.getMainWindow().getWidth());
        int height = (int) (mc.mouseHelper.getMouseY() * (double) mc.getMainWindow().getScaledHeight() / (double) mc.getMainWindow().getHeight());
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.textureManager.bindTexture(OverlaySensorGlasses.hudTexture);
        final Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(width / 2 - height - f * 80, height + f * 40, -90D).tex(0.0F, 1.0F).endVertex();
        worldRenderer.pos(width / 2 + height + f * 80, height + f * 40, -90D).tex(1.0F, 1.0F).endVertex();
        worldRenderer.pos(width / 2 + height + f * 80, 0.0D - f * 40, -90D).tex(1.0F, 0.0F).endVertex();
        worldRenderer.pos(width / 2 - height - f * 80, 0.0D - f * 40, -90D).tex(0.0F, 0.0F).endVertex();
        tessellator.draw();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderSensorGlassesValueableBlocks(ItemStack stack, PlayerEntity player, float partialTicks)
    {
        final Iterator<BlockVec3> var51 = ClientProxyCore.valueableBlocks.iterator();
        double var52;
        double var58;
        double var59;
        double var20;
        double var21;
        float var60;

        while (var51.hasNext())
        {
            BlockVec3 coords = var51.next();

            var52 = ClientProxyCore.playerPosX - coords.x - 0.5D;
            var58 = ClientProxyCore.playerPosY - coords.y - 0.5D;
            var59 = ClientProxyCore.playerPosZ - coords.z - 0.5D;
            var60 = (float) Math.toDegrees(Math.atan2(var52, var59));
            var20 = Math.sqrt(var52 * var52 + var58 * var58 + var59 * var59) * 0.5D;
            var21 = Math.sqrt(var52 * var52 + var59 * var59) * 0.5D;

            Minecraft mc = Minecraft.getInstance();
            int width = (int) (mc.mouseHelper.getMouseX() * (double) mc.getMainWindow().getScaledWidth() / (double) mc.getMainWindow().getWidth());
            int height = (int) (mc.mouseHelper.getMouseY() * (double) mc.getMainWindow().getScaledHeight() / (double) mc.getMainWindow().getHeight());
//            final ScaledResolution var5 = ClientUtil.getScaledRes(OverlaySensorGlasses.minecraft, OverlaySensorGlasses.minecraft.displayWidth, OverlaySensorGlasses.minecraft.displayHeight);
//            final int var6 = var5.getScaledWidth();
//            final int var7 = var5.getScaledHeight();

            boolean var2 = false;

            final ClientPlayerEntity client = PlayerUtil.getPlayerBaseClientFromPlayer(OverlaySensorGlasses.minecraft.player, false);

            if (client != null)
            {
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(client);
                var2 = stats.isUsingAdvancedGoggles();
            }

            OverlaySensorGlasses.minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.sensor.advanced") + ": " + (var2 ? GCCoreUtil.translate("gui.sensor.advancedon") : GCCoreUtil.translate("gui.sensor.advancedoff")), width / 2 - 50, 4, 0x03b88f);

            try
            {
                GL11.glPushMatrix();

                if (var20 < 4.0D)
                {
                    GL11.glColor4f(0.0F, 255F / 255F, 198F / 255F, (float) Math.min(1.0D, Math.max(0.2D, (var20 - 1.0D) * 0.1D)));
                    Minecraft.getInstance().textureManager.bindTexture(OverlaySensorGlasses.indicatorTexture);
                    GL11.glRotatef(-var60 - ClientProxyCore.playerRotationYaw + 180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslated(0.0D, var2 ? -var20 * 16 : -var21 * 16, 0.0D);
                    GL11.glRotatef(-(-var60 - ClientProxyCore.playerRotationYaw + 180.0F), 0.0F, 0.0F, 1.0F);
                    Overlay.drawCenteringRectangle(width / 2, height / 2, 1.0D, 8.0D, 8.0D);
                }
            }
            finally
            {
                GL11.glPopMatrix();
            }
        }
    }

    public static void preRenderMobs()
    {
        GL11.glEnable(GL11.GL_BLEND);
//TODO  Enable these to see the entity through solid blocks - but would need a postRenderCallback to switch this off again otherwise everything is changed
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
//            GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        int i = 15728880;
        int j = i % 65536;
        int k = i / 65536;
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glTranslatef(0.0F, 0.045F, 0.0F);
        GL11.glScalef(1.07F, 1.035F, 1.07F);
    }

    public static void postRenderMobs()
    {
        GL11.glEnable(GL11.GL_ALPHA_TEST);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glDepthMask(true);
    }

    public static boolean overrideMobTexture()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        return (player != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() instanceof ISensorGlassesArmor);
    }
}
