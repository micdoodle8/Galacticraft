package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayRocket extends Overlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    /**
     * Render the GUI when player is in inventory
     */
    public static void renderSpaceshipOverlay(ResourceLocation guiTexture)
    {
        if (guiTexture == null)
        {
            return;
        }

        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(OverlayRocket.minecraft, OverlayRocket.minecraft.displayWidth, OverlayRocket.minecraft.displayHeight);
        scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        OverlayRocket.minecraft.entityRenderer.setupOverlayRendering();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(guiTexture);

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
        WorldRenderer worldRenderer = tess.getWorldRenderer();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV(var1 + 0, var2 + 242.0F * sizeScale, 0.0, (var3 + 0) * var7, (var4 + var6) * var8);
        worldRenderer.addVertexWithUV(var1 + 20.0F * sizeScale, var2 + 242.0F * sizeScale, 0.0, (var3 + var5) * var7, (var4 + var6) * var8);
        worldRenderer.addVertexWithUV(var1 + 20.0F * sizeScale, var2 + 0, 0.0, (var3 + var5) * var7, (var4 + 0) * var8);
        worldRenderer.addVertexWithUV(var1 + 0, var2 + 0, 0.0, (var3 + 0) * var7, (var4 + 0) * var8);
        tess.draw();

        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        Render spaceshipRender = (Render) minecraft.getRenderManager().entityRenderMap.get(OverlayRocket.minecraft.thePlayer.ridingEntity.getClass());

        final int y1 = height / 2 + 60 - (int) Math.floor(Overlay.getPlayerPositionY(OverlayRocket.minecraft.thePlayer) / 10.5F);
        var1 = 2.5F;
        var2 = y1;
        var3 = 8;
        var3b = 40;
        var4 = 8;
        var5 = 8;
        var6 = 8;
        var7 = 1.0F / 64.0F;
        var8 = 1.0F / 32.0F;

        GL11.glPushMatrix();
        final int i = OverlayRocket.minecraft.thePlayer.ridingEntity.getBrightnessForRender(1);
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glTranslatef(var1 + 4, var2 + 6, 50F);
        GL11.glScalef(5F, 5F, 5F);
        GL11.glRotatef(180F, 1, 0, 0);
        GL11.glRotatef(90F, 0, 1, 0);

        try
        {
            spaceshipRender.doRender(OverlayRocket.minecraft.thePlayer.ridingEntity.getClass().getConstructor(World.class).newInstance(OverlayRocket.minecraft.thePlayer.worldObj), 0, 0, 0, 0, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        GL11.glPopMatrix();

        ResourceLocation resourcelocation = AbstractClientPlayer.getLocationSkin(OverlayRocket.minecraft.thePlayer.getGameProfile().getName());
        AbstractClientPlayer.getDownloadImageSkin(resourcelocation, OverlayRocket.minecraft.thePlayer.getGameProfile().getName());

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(resourcelocation);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef(0F, 0F, 60F);

        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV(var1 + 0, var2 + var6, 0.0, (var3 + 0) * var7, (var4 + var6) * var8);
        worldRenderer.addVertexWithUV(var1 + var5, var2 + var6, 0.0, (var3 + var5) * var7, (var4 + var6) * var8);
        worldRenderer.addVertexWithUV(var1 + var5, var2 + 0, 0.0, (var3 + var5) * var7, (var4 + 0) * var8);
        worldRenderer.addVertexWithUV(var1 + 0, var2 + 0, 0.0, (var3 + 0) * var7, (var4 + 0) * var8);
        tess.draw();

        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV(var1 + 0, var2 + var6, 0.0, (var3b + 0) * var7, (var4 + var6) * var8);
        worldRenderer.addVertexWithUV(var1 + var5, var2 + var6, 0.0, (var3b + var5) * var7, (var4 + var6) * var8);
        worldRenderer.addVertexWithUV(var1 + var5, var2 + 0, 0.0, (var3b + var5) * var7, (var4 + 0) * var8);
        worldRenderer.addVertexWithUV(var1 + 0, var2 + 0, 0.0, (var3b + 0) * var7, (var4 + 0) * var8);
        tess.draw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
