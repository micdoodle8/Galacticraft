//package micdoodle8.mods.galacticraft.core.client.gui.overlay;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.util.ColorUtil;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class OverlayOxygenTanks extends Overlay
//{
//    private final static ResourceLocation guiTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/gui.png");
//
//    /**
//     * Render the GUI that displays oxygen level in tanks
//     */
//    public static void renderOxygenTankIndicator(Minecraft mc, int heatLevel, int oxygenInTank1, int oxygenInTank2, boolean right, boolean top, boolean invalid)
//    {
//        int width = (int) (mc.mouseHelper.getMouseX() * (double) mc.getMainWindow().getScaledWidth() / (double) mc.getMainWindow().getWidth());
//        int height = (int) (mc.mouseHelper.getMouseY() * (double) mc.getMainWindow().getScaledHeight() / (double) mc.getMainWindow().getHeight());
////        mc.entityRenderer.setupOverlayRendering();
//        GlStateManager.enableBlend();
//        GlStateManager.disableAlphaTest();
//        GlStateManager.depthMask(false);
//        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param);
//        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.disableAlphaTest();
//        mc.textureManager.bindTexture(OverlayOxygenTanks.guiTexture);
//        final Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tessellator.getBuffer();
//        GlStateManager.enableAlphaTest();
//        GlStateManager.disableLighting();
//        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        int minLeftX = 0;
//        int maxLeftX = 0;
//        int minRightX = 0;
//        int maxRightX = 0;
//        double bottomY = 0;
//        double topY = 0;
//        double zLevel = -190.0D;
//
//        if (right)
//        {
//            minLeftX = width - 59;
//            maxLeftX = width - 40;
//            minRightX = width - 39;
//            maxRightX = width - 20;
//        }
//        else
//        {
//            minLeftX = 10;
//            maxLeftX = 29;
//            minRightX = 30;
//            maxRightX = 49;
//        }
//
//        if (top)
//        {
//            topY = 10.5;
//        }
//        else
//        {
//            topY = height - 57;
//        }
//
//        bottomY = topY + 46.5;
//
//        float texMod = 0.00390625F;
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(minLeftX, bottomY, zLevel).tex(66 * texMod, 47 * texMod).endVertex();
//        worldRenderer.pos(minLeftX + 9, bottomY, zLevel).tex((66 + 9) * texMod, 47 * texMod).endVertex();
//        worldRenderer.pos(minLeftX + 9, topY, zLevel).tex((66 + 9) * texMod, 47 * 2 * texMod).endVertex();
//        worldRenderer.pos(minLeftX, topY, zLevel).tex(66 * texMod, 47 * 2 * texMod).endVertex();
//        tessellator.draw();
//
//        int heatLevelScaled = Math.min(Math.max(heatLevel, 1), 45);
//        int heatLeveLScaledMax = Math.min(heatLevelScaled + 2, 45);
//        int heatLevelScaledMin = Math.max(heatLeveLScaledMax - 2, 0);
//
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(minLeftX + 1, bottomY - heatLevelScaledMin, zLevel).tex(76 * texMod, (48 + 45 - heatLevelScaled) * texMod).endVertex();
//        worldRenderer.pos(minLeftX + 8, bottomY - heatLevelScaledMin, zLevel).tex((76 + 7) * texMod, (48 + 45 - heatLevelScaled) * texMod).endVertex();
//        worldRenderer.pos(minLeftX + 8, bottomY - heatLeveLScaledMax, zLevel).tex((76 + 7) * texMod, (48 + 45 - heatLevelScaled) * texMod).endVertex();
//        worldRenderer.pos(minLeftX + 1, bottomY - heatLeveLScaledMax, zLevel).tex(76 * texMod, (48 + 45 - heatLevelScaled) * texMod).endVertex();
//        tessellator.draw();
//
//        if (invalid)
//        {
//            GlStateManager.color3f(1, 0, 0);
//            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(minLeftX - 5, bottomY - heatLevelScaledMin + 3, zLevel).tex(84 * texMod, 47 * texMod).endVertex();
//            worldRenderer.pos(minLeftX - 1, bottomY - heatLevelScaledMin + 3, zLevel).tex((84 + 5) * texMod, 47 * texMod).endVertex();
//            worldRenderer.pos(minLeftX - 1, bottomY - heatLeveLScaledMax - 3, zLevel).tex((84 + 5) * texMod, (47 + 9) * texMod).endVertex();
//            worldRenderer.pos(minLeftX - 5, bottomY - heatLeveLScaledMax - 3, zLevel).tex(84 * texMod, (47 + 9) * texMod).endVertex();
//            tessellator.draw();
//            GlStateManager.color3f(1, 1, 1);
//        }
//
//        minLeftX += 10;
//        maxLeftX += 10;
//        minRightX += 10;
//        maxRightX += 10;
//
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(minRightX, bottomY, zLevel).tex(85 * texMod, 47 * texMod).endVertex();
//        worldRenderer.pos(maxRightX, bottomY, zLevel).tex((85 + 19) * texMod, 47 * texMod).endVertex();
//        worldRenderer.pos(maxRightX, topY, zLevel).tex((85 + 19) * texMod, 0 * texMod).endVertex();
//        worldRenderer.pos(minRightX, topY, zLevel).tex(85 * texMod, 0 * texMod).endVertex();
//        tessellator.draw();
//        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        worldRenderer.pos(minLeftX, bottomY, zLevel).tex(85 * texMod, 47 * texMod).endVertex();
//        worldRenderer.pos(maxLeftX, bottomY, zLevel).tex((85 + 19) * texMod, 47 * texMod).endVertex();
//        worldRenderer.pos(maxLeftX, topY, zLevel).tex((85 + 19) * texMod, 0 * texMod).endVertex();
//        worldRenderer.pos(minLeftX, topY, zLevel).tex(85 * texMod, 0 * texMod).endVertex();
//        tessellator.draw();
//        GlStateManager.depthMask(true);
//
//        if (oxygenInTank1 > 0)
//        {
//            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(minLeftX + 1, topY + 1 + oxygenInTank1 / 2, zLevel).tex(105 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxLeftX - 1, topY + 1 + oxygenInTank1 / 2, zLevel).tex((105 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxLeftX - 1, topY + 1, zLevel).tex((105 + 17) * 0.00390625F, 1 * 0.00390625F).endVertex();
//            worldRenderer.pos(minLeftX + 1, topY + 1, zLevel).tex(105 * 0.00390625F, 1 * 0.00390625F).endVertex();
//            tessellator.draw();
//
//            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(minLeftX, topY + 1 + oxygenInTank1 / 2, zLevel).tex(66 * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxLeftX - 1, topY + 1 + oxygenInTank1 / 2, zLevel).tex((66 + 17) * 0.00390625F, oxygenInTank1 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxLeftX - 1, topY + 1 + oxygenInTank1 / 2 - 1, zLevel).tex((66 + 17) * 0.00390625F, 1 * 0.00390625F).endVertex();
//            worldRenderer.pos(minLeftX, topY + 1 + oxygenInTank1 / 2 - 1, zLevel).tex(66 * 0.00390625F, 1 * 0.00390625F).endVertex();
//            tessellator.draw();
//        }
//
//        if (oxygenInTank2 > 0)
//        {
//            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(minRightX + 1, topY + 1 + oxygenInTank2 / 2, 0).tex(105 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxRightX - 1, topY + 1 + oxygenInTank2 / 2, 0).tex((105 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxRightX - 1, topY + 1, 0).tex((105 + 17) * 0.00390625F, 1 * 0.00390625F).endVertex();
//            worldRenderer.pos(minRightX + 1, topY + 1, 0).tex(105 * 0.00390625F, 1 * 0.00390625F).endVertex();
//            tessellator.draw();
//
//            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//            worldRenderer.pos(minRightX, topY + 1 + oxygenInTank2 / 2, 0).tex(66 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxRightX - 1, topY + 1 + oxygenInTank2 / 2, 0).tex((66 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(maxRightX - 1, topY + 1 + oxygenInTank2 / 2 - 1, 0).tex((66 + 17) * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F).endVertex();
//            worldRenderer.pos(minRightX, topY + 1 + oxygenInTank2 / 2 - 1, 0).tex(66 * 0.00390625F, oxygenInTank2 / 2 * 0.00390625F).endVertex();
//            tessellator.draw();
//        }
//
//        if (invalid)
//        {
//            String value = GCCoreUtil.translate("gui.warning.invalid_thermal");
//            mc.fontRenderer.drawString(value, minLeftX - 18 - mc.fontRenderer.getStringWidth(value), (int) bottomY - heatLevelScaled - mc.fontRenderer.FONT_HEIGHT / 2 - 1, ColorUtil.to32BitColor(255, 255, 10, 10));
//        }
//        GlStateManager.disableBlend();
//    }
//}
