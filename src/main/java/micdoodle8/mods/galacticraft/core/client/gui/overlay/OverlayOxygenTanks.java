package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayOxygenTanks extends Overlay
{
    private final static ResourceLocation guiTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/gui.png");

    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    /**
     * Render the GUI that displays oxygen level in tanks
     */
    public static void renderOxygenTankIndicator(int heatLevel, int oxygenInTank1, int oxygenInTank2, boolean right, boolean top, boolean invalid)
    {
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(OverlayOxygenTanks.minecraft, OverlayOxygenTanks.minecraft.displayWidth, OverlayOxygenTanks.minecraft.displayHeight);
        final int i = scaledresolution.getScaledWidth();
        final int j = scaledresolution.getScaledHeight();
        OverlayOxygenTanks.minecraft.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(OverlayOxygenTanks.guiTexture);
        final Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int minLeftX = 0;
        int maxLeftX = 0;
        int minRightX = 0;
        int maxRightX = 0;
        double bottomY = 0;
        double topY = 0;
        double zLevel = -190.0D;

        if (right)
        {
            minLeftX = i - 59;
            maxLeftX = i - 40;
            minRightX = i - 39;
            maxRightX = i - 20;
        }
        else
        {
            minLeftX = 10;
            maxLeftX = 29;
            minRightX = 30;
            maxRightX = 49;
        }

        if (top)
        {
            topY = 10.5;
        }
        else
        {
            topY = j - 57;
        }

        bottomY = topY + 46.5;

        float texMod = 0.00390625F;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(minLeftX, bottomY, zLevel, 66 * texMod, 47 * texMod);
        tessellator.addVertexWithUV(minLeftX + 9, bottomY, zLevel, (66 + 9) * texMod, 47 * texMod);
        tessellator.addVertexWithUV(minLeftX + 9, topY, zLevel, (66 + 9) * texMod, 47 * 2 * texMod);
        tessellator.addVertexWithUV(minLeftX, topY, zLevel, 66 * texMod, 47 * 2 * texMod);
        tessellator.draw();

        int heatLevelScaled = Math.min(Math.max(heatLevel, 1), 45);
        int heatLeveLScaledMax = Math.min(heatLevelScaled + 2, 45);
        int heatLevelScaledMin = Math.max(heatLeveLScaledMax - 2, 0);

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(minLeftX + 1, bottomY - heatLevelScaledMin, zLevel, 76 * texMod, (48 + 45 - heatLevelScaled) * texMod);
        tessellator.addVertexWithUV(minLeftX + 8, bottomY - heatLevelScaledMin, zLevel, (76 + 7) * texMod, (48 + 45 - heatLevelScaled) * texMod);
        tessellator.addVertexWithUV(minLeftX + 8, bottomY - heatLeveLScaledMax, zLevel, (76 + 7) * texMod, (48 + 45 - heatLevelScaled) * texMod);
        tessellator.addVertexWithUV(minLeftX + 1, bottomY - heatLeveLScaledMax, zLevel, 76 * texMod, (48 + 45 - heatLevelScaled) * texMod);
        tessellator.draw();

        if (invalid)
        {
            GL11.glColor3f(1, 0, 0);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(minLeftX - 5, bottomY - heatLevelScaledMin + 3, zLevel, 84 * texMod, 47 * texMod);
            tessellator.addVertexWithUV(minLeftX - 1, bottomY - heatLevelScaledMin + 3, zLevel, (84 + 5) * texMod, 47 * texMod);
            tessellator.addVertexWithUV(minLeftX - 1, bottomY - heatLeveLScaledMax - 3, zLevel, (84 + 5) * texMod, (47 + 9) * texMod);
            tessellator.addVertexWithUV(minLeftX - 5, bottomY - heatLeveLScaledMax - 3, zLevel, 84 * texMod, (47 + 9) * texMod);
            tessellator.draw();
            GL11.glColor3f(1, 1, 1);
        }

        minLeftX += 10;
        maxLeftX += 10;
        minRightX += 10;
        maxRightX += 10;

        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(minRightX, bottomY, zLevel, 85 * texMod, 47 * texMod);
        tessellator.addVertexWithUV(maxRightX, bottomY, zLevel, (85 + 19) * texMod, 47 * texMod);
        tessellator.addVertexWithUV(maxRightX, topY, zLevel, (85 + 19) * texMod, 0 * texMod);
        tessellator.addVertexWithUV(minRightX, topY, zLevel, 85 * texMod, 0 * texMod);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(minLeftX, bottomY, zLevel, 85 * texMod, 47 * texMod);
        tessellator.addVertexWithUV(maxLeftX, bottomY, zLevel, (85 + 19) * texMod, 47 * texMod);
        tessellator.addVertexWithUV(maxLeftX, topY, zLevel, (85 + 19) * texMod, 0 * texMod);
        tessellator.addVertexWithUV(minLeftX, topY, zLevel, 85 * texMod, 0 * texMod);
        tessellator.draw();
        GL11.glDepthMask(true);

        if (oxygenInTank1 > 0)
        {
            final Tessellator tessellator2 = Tessellator.instance;

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(minLeftX + 1, topY + 1 + oxygenInTank1 / 2, zLevel, 105 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxLeftX - 1, topY + 1 + oxygenInTank1 / 2, zLevel, (105 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxLeftX - 1, topY + 1, zLevel, (105 + 17) * 0.00390625F, 1 * 0.00390625F);
            tessellator.addVertexWithUV(minLeftX + 1, topY + 1, zLevel, 105 * 0.00390625F, 1 * 0.00390625F);
            tessellator2.draw();

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(minLeftX, topY + 1 + oxygenInTank1 / 2, zLevel, 66 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxLeftX - 1, topY + 1 + oxygenInTank1 / 2, zLevel, (66 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxLeftX - 1, topY + 1 + oxygenInTank1 / 2 - 1, zLevel, (66 + 17) * 0.00390625F, (oxygenInTank1 / 2 - 1) * 0.00390625F);
            tessellator.addVertexWithUV(minLeftX, topY + 1 + oxygenInTank1 / 2 - 1, zLevel, 66 * 0.00390625F, (oxygenInTank1 / 2 - 1) * 0.00390625F);
            tessellator2.draw();
        }

        if (oxygenInTank2 > 0)
        {
            final Tessellator tessellator2 = Tessellator.instance;

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(minRightX + 1, topY + 1 + oxygenInTank2 / 2, 0, 105 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxRightX - 1, topY + 1 + oxygenInTank2 / 2, 0, (105 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxRightX - 1, topY + 1, 0, (105 + 17) * 0.00390625F, 1 * 0.00390625F);
            tessellator.addVertexWithUV(minRightX + 1, topY + 1, 0, 105 * 0.00390625F, 1 * 0.00390625F);
            tessellator2.draw();

            tessellator2.startDrawingQuads();
            tessellator.addVertexWithUV(minRightX, topY + 1 + oxygenInTank2 / 2, 0, 66 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxRightX - 1, topY + 1 + oxygenInTank2 / 2, 0, (66 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(maxRightX - 1, topY + 1 + oxygenInTank2 / 2 - 1, 0, (66 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator.addVertexWithUV(minRightX, topY + 1 + oxygenInTank2 / 2 - 1, 0, 66 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F);
            tessellator2.draw();
        }

        if (invalid)
        {
            String value = GCCoreUtil.translate("gui.warning.invalidThermal");
            OverlayOxygenTanks.minecraft.fontRenderer.drawString(value, minLeftX - 18 - OverlayOxygenTanks.minecraft.fontRenderer.getStringWidth(value), (int) bottomY - heatLevelScaled - OverlayOxygenTanks.minecraft.fontRenderer.FONT_HEIGHT / 2 - 1, ColorUtil.to32BitColor(255, 255, 10, 10));
        }
    }
}
