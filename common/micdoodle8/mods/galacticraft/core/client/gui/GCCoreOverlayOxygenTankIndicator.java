package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreOverlayOxygenTankIndicator extends GCCoreOverlay
{
    private final static ResourceLocation guiTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/gui.png");

    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    /**
     * Render the GUI that displays oxygen level in tanks
     */
    public static void renderOxygenTankIndicatorRight(int oxygenInTank1, int oxygenInTank2)
    {
        final ScaledResolution scaledresolution = new ScaledResolution(GCCoreOverlayOxygenTankIndicator.minecraft.gameSettings, GCCoreOverlayOxygenTankIndicator.minecraft.displayWidth, GCCoreOverlayOxygenTankIndicator.minecraft.displayHeight);
        final int i = scaledresolution.getScaledWidth();
        scaledresolution.getScaledHeight();
        GCCoreOverlayOxygenTankIndicator.minecraft.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCCoreOverlayOxygenTankIndicator.guiTexture);
        final Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(i - 29, 33.5 + 23.5, -90D, 85 * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(i - 10, 33.5 + 23.5, -90D, (85 + 19) * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(i - 10, 33.5 - 23.5, -90D, (85 + 19) * 0.00390625F, 0 * 0.00390625F);
        tessellator.addVertexWithUV(i - 29, 33.5 - 23.5, -90D, 85 * 0.00390625F, 0 * 0.00390625F);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(i - 49, 33.5 + 23.5, -90D, 85 * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(i - 30, 33.5 + 23.5, -90D, (85 + 19) * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(i - 30, 33.5 - 23.5, -90D, (85 + 19) * 0.00390625F, 0 * 0.00390625F);
        tessellator.addVertexWithUV(i - 49, 33.5 - 23.5, -90D, 85 * 0.00390625F, 0 * 0.00390625F);
        tessellator.draw();
        GL11.glDepthMask(true);

        if (oxygenInTank1 > 0 || oxygenInTank1 <= 0)
        {
            final Tessellator tessellator2 = Tessellator.instance;

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(i - 48, 34.5 - 23.5 + oxygenInTank1 / 2, 0, 105 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 31, 34.5 - 23.5 + oxygenInTank1 / 2, 0, (105 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 31, 34.5 - 23.5, 0, (105 + 17) * 0.00390625F, 1 * 0.00390625F);
            tessellator.addVertexWithUV(i - 48, 34.5 - 23.5, 0, 105 * 0.00390625F, 1 * 0.00390625F);
            tessellator2.draw();

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(i - 49, 34.5 - 23.5 + oxygenInTank1 / 2, 0, 66 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 31, 34.5 - 23.5 + oxygenInTank1 / 2, 0, (66 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 31, 34.5 - 23.5 + oxygenInTank1 / 2 - 1, 0, (66 + 17) * 0.00390625F, (oxygenInTank1 / 2 - 1) * 0.00390625F);
            tessellator.addVertexWithUV(i - 49, 34.5 - 23.5 + oxygenInTank1 / 2 - 1, 0, 66 * 0.00390625F, (oxygenInTank1 / 2 - 1) * 0.00390625F);
            tessellator2.draw();
        }

        if (oxygenInTank2 > 0 || oxygenInTank2 <= 0)
        {
            final Tessellator tessellator2 = Tessellator.instance;

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(i - 28, 34.5 - 23.5 + oxygenInTank2 / 2, 0, 105 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 11, 34.5 - 23.5 + oxygenInTank2 / 2, 0, (105 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 11, 34.5 - 23.5, 0, (105 + 17) * 0.00390625F, 1 * 0.00390625F);
            tessellator.addVertexWithUV(i - 28, 34.5 - 23.5, 0, 105 * 0.00390625F, 1 * 0.00390625F);
            tessellator2.draw();

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(i - 29, 34.5 - 23.5 + oxygenInTank2 / 2, 0, 66 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 11, 34.5 - 23.5 + oxygenInTank2 / 2, 0, (66 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(i - 11, 34.5 - 23.5 + oxygenInTank2 / 2 - 1, 0, (66 + 17) * 0.00390625F, (oxygenInTank2 / 2 - 1) * 0.00390625F);
            tessellator.addVertexWithUV(i - 29, 34.5 - 23.5 + oxygenInTank2 / 2 - 1, 0, 66 * 0.00390625F, (oxygenInTank2 / 2 - 1) * 0.00390625F);
            tessellator2.draw();
        }
    }

    /**
     * Render the GUI that displays oxygen level in tanks
     */
    public static void renderOxygenTankIndicatorLeft(int oxygenInTank1, int oxygenInTank2)
    {
        final ScaledResolution scaledresolution = new ScaledResolution(GCCoreOverlayOxygenTankIndicator.minecraft.gameSettings, GCCoreOverlayOxygenTankIndicator.minecraft.displayWidth, GCCoreOverlayOxygenTankIndicator.minecraft.displayHeight);
        scaledresolution.getScaledWidth();
        scaledresolution.getScaledHeight();
        GCCoreOverlayOxygenTankIndicator.minecraft.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCCoreOverlayOxygenTankIndicator.guiTexture);
        final Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0 + 10, 33.5 + 23.5, -90D, 85 * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(0 + 29, 33.5 + 23.5, -90D, (85 + 19) * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(0 + 29, 33.5 - 23.5, -90D, (85 + 19) * 0.00390625F, 0 * 0.00390625F);
        tessellator.addVertexWithUV(0 + 10, 33.5 - 23.5, -90D, 85 * 0.00390625F, 0 * 0.00390625F);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0 + 30, 33.5 + 23.5, -90D, 85 * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(0 + 49, 33.5 + 23.5, -90D, (85 + 19) * 0.00390625F, 47 * 0.00390625F);
        tessellator.addVertexWithUV(0 + 49, 33.5 - 23.5, -90D, (85 + 19) * 0.00390625F, 0 * 0.00390625F);
        tessellator.addVertexWithUV(0 + 30, 33.5 - 23.5, -90D, 85 * 0.00390625F, 0 * 0.00390625F);
        tessellator.draw();
        GL11.glDepthMask(true);

        if (oxygenInTank1 > 0 || oxygenInTank1 <= 0)
        {
            final Tessellator tessellator2 = Tessellator.instance;

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(0 + 31, 34.5 - 23.5 + oxygenInTank1 / 2, 0, 105 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 48, 34.5 - 23.5 + oxygenInTank1 / 2, 0, (105 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 48, 34.5 - 23.5, 0, (105 + 17) * 0.00390625F, 1 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 31, 34.5 - 23.5, 0, 105 * 0.00390625F, 1 * 0.00390625F);
            tessellator2.draw();

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(0 + 30, 34.5 - 23.5 + oxygenInTank1 / 2, 0, 66 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 48, 34.5 - 23.5 + oxygenInTank1 / 2, 0, (66 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 48, 34.5 - 23.5 + oxygenInTank1 / 2 - 1, 0, (66 + 17) * 0.00390625F, (oxygenInTank1 / 2 - 1) * 0.00390625F);
            tessellator.addVertexWithUV(0 + 30, 34.5 - 23.5 + oxygenInTank1 / 2 - 1, 0, 66 * 0.00390625F, (oxygenInTank1 / 2 - 1) * 0.00390625F);
            tessellator2.draw();
        }

        if (oxygenInTank2 > 0 || oxygenInTank2 <= 0)
        {
            final Tessellator tessellator2 = Tessellator.instance;

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(0 + 11, 34.5 - 23.5 + oxygenInTank2 / 2, 0, 105 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 28, 34.5 - 23.5 + oxygenInTank2 / 2, 0, (105 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 28, 34.5 - 23.5, 0, (105 + 17) * 0.00390625F, 1 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 11, 34.5 - 23.5, 0, 105 * 0.00390625F, 1 * 0.00390625F);
            tessellator2.draw();

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(0 + 10.0, 34.5 - 23.5 + oxygenInTank2 / 2, 0, 66 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 28, 34.5 - 23.5 + oxygenInTank2 / 2, 0, (66 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(0 + 28, 34.5 - 23.5 + oxygenInTank2 / 2 - 1, 0, (66 + 17) * 0.00390625F, (oxygenInTank2 / 2 - 1) * 0.00390625F);
            tessellator.addVertexWithUV(0 + 10.00, 34.5 - 23.5 + oxygenInTank2 / 2 - 1, 0, 66 * 0.00390625F, (oxygenInTank2 / 2 - 1) * 0.00390625F);
            tessellator2.draw();
        }
    }
}
