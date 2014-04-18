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

/**
 * GCCoreOverlayOxygenTankIndicator.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreOverlayOxygenTankIndicator extends GCCoreOverlay
{
	private final static ResourceLocation guiTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");

	private static Minecraft minecraft = FMLClientHandler.instance().getClient();

	/**
	 * Render the GUI that displays oxygen level in tanks
	 */
	public static void renderOxygenTankIndicator(int oxygenInTank1, int oxygenInTank2, boolean right, boolean top)
	{
		final ScaledResolution scaledresolution = new ScaledResolution(GCCoreOverlayOxygenTankIndicator.minecraft.gameSettings, GCCoreOverlayOxygenTankIndicator.minecraft.displayWidth, GCCoreOverlayOxygenTankIndicator.minecraft.displayHeight);
		final int i = scaledresolution.getScaledWidth();
		final int j = scaledresolution.getScaledHeight();
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

		int minLeftX = 0;
		int maxLeftX = 0;
		int minRightX = 0;
		int maxRightX = 0;
		double bottomY = 0;
		double topY = 0;
		double zLevel = -190.0D;

		if (right)
		{
			minLeftX = i - 49;
			maxLeftX = i - 30;
			minRightX = i - 29;
			maxRightX = i - 10;
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

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(minRightX, bottomY, zLevel, 85 * 0.00390625F, 47 * 0.00390625F);
		tessellator.addVertexWithUV(maxRightX, bottomY, zLevel, (85 + 19) * 0.00390625F, 47 * 0.00390625F);
		tessellator.addVertexWithUV(maxRightX, topY, zLevel, (85 + 19) * 0.00390625F, 0 * 0.00390625F);
		tessellator.addVertexWithUV(minRightX, topY, zLevel, 85 * 0.00390625F, 0 * 0.00390625F);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(minLeftX, bottomY, zLevel, 85 * 0.00390625F, 47 * 0.00390625F);
		tessellator.addVertexWithUV(maxLeftX, bottomY, zLevel, (85 + 19) * 0.00390625F, 47 * 0.00390625F);
		tessellator.addVertexWithUV(maxLeftX, topY, zLevel, (85 + 19) * 0.00390625F, 0 * 0.00390625F);
		tessellator.addVertexWithUV(minLeftX, topY, zLevel, 85 * 0.00390625F, 0 * 0.00390625F);
		tessellator.draw();
		GL11.glDepthMask(true);

		if (oxygenInTank1 > 0 || oxygenInTank1 <= 0)
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

		if (oxygenInTank2 > 0 || oxygenInTank2 <= 0)
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
			tessellator.addVertexWithUV(maxRightX - 1, topY + 1 + oxygenInTank2 / 2 - 1, 0, (66 + 17) * 0.00390625F, (oxygenInTank2 / 2) * 0.00390625F);
			tessellator.addVertexWithUV(minRightX, topY + 1 + oxygenInTank2 / 2 - 1, 0, 66 * 0.00390625F, (oxygenInTank2 / 2) * 0.00390625F);
			tessellator2.draw();
		}
	}
}
