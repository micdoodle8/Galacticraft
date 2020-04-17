package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayRocket extends Overlay
{
    /**
     * Render the GUI when player is in inventory
     */
    public static void renderSpaceshipOverlay(Minecraft mc, ResourceLocation guiTexture)
    {
        if (guiTexture == null)
        {
            return;
        }

        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(mc, mc.displayWidth, mc.displayHeight);
        scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(guiTexture);

        float var1 = 0F;
        float var2 = height / 2 - 170 / 2;
        float var3 = 0.0F;
        float var3b = 0.0F;
        float var4 = 0.0F;
        float var5 = 1.0F;
        float var6 = 1.0F;
        float var7 = 1.0F;
        float var8 = 1.0F;
        float sizeScale = 0.65F;

        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(var1 + 0, var2 + 242.0F * sizeScale, 0.0).tex((var3 + 0) * var7, (var4 + var6) * var8).endVertex();
        worldRenderer.pos(var1 + 20.0F * sizeScale, var2 + 242.0F * sizeScale, 0.0).tex((var3 + var5) * var7, (var4 + var6) * var8).endVertex();
        worldRenderer.pos(var1 + 20.0F * sizeScale, var2 + 0, 0.0).tex((var3 + var5) * var7, (var4 + 0) * var8).endVertex();
        worldRenderer.pos(var1 + 0, var2 + 0, 0.0).tex((var3 + 0) * var7, (var4 + 0) * var8).endVertex();
        tess.draw();

        GlStateManager.color(1.0F, 1.0F, 1.0F);

        Entity rocket = mc.player.getRidingEntity();
        float headOffset = 0;
        if (rocket instanceof EntityTier1Rocket)
        {
            headOffset = 5F;
        }
        Render spaceshipRender = (Render) mc.getRenderManager().entityRenderMap.get(rocket.getClass());

        final int y1 = height / 2 + 60 - (int) Math.floor(Overlay.getPlayerPositionY(mc.player) / 10.5F);
        var1 = 2.5F;
        var2 = y1;
        var3 = 8;
        var3b = 40;
        var4 = 8;
        var5 = 8;
        var6 = 8;
        var7 = 1.0F / 64.0F;
        var8 = 1.0F / 64.0F;

        GlStateManager.pushMatrix();
        final int i = rocket.getBrightnessForRender();
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableColorMaterial();
        GlStateManager.translate(var1 + 4, var2 + 6, 50F);
        GlStateManager.scale(5F, 5F, 5F);
        GlStateManager.rotate(180F, 1, 0, 0);
        GlStateManager.rotate(90F, 0, 1, 0);

        try
        {
            spaceshipRender.doRender(rocket.getClass().getConstructor(World.class).newInstance(mc.player.world), 0, 0, 0, 0, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        GlStateManager.popMatrix();

        mc.renderEngine.bindTexture(ClientProxyCore.playerHead);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.translate(0F, -12F + headOffset, 60F);

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(var1 + 0, var2 + var6, 0.0).tex((var3 + 0) * var7, (var4 + var6) * var8).endVertex();
        worldRenderer.pos(var1 + var5, var2 + var6, 0.0).tex((var3 + var5) * var7, (var4 + var6) * var8).endVertex();
        worldRenderer.pos(var1 + var5, var2 + 0, 0.0).tex((var3 + var5) * var7, (var4 + 0) * var8).endVertex();
        worldRenderer.pos(var1 + 0, var2 + 0, 0.0).tex((var3 + 0) * var7, (var4 + 0) * var8).endVertex();
        tess.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(var1 + 0, var2 + var6, 0.0).tex((var3b + 0) * var7, (var4 + var6) * var8).endVertex();
        worldRenderer.pos(var1 + var5, var2 + var6, 0.0).tex((var3b + var5) * var7, (var4 + var6) * var8).endVertex();
        worldRenderer.pos(var1 + var5, var2 + 0, 0.0).tex((var3b + var5) * var7, (var4 + 0) * var8).endVertex();
        worldRenderer.pos(var1 + 0, var2 + 0, 0.0).tex((var3b + 0) * var7, (var4 + 0) * var8).endVertex();
        tess.draw();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }
}
