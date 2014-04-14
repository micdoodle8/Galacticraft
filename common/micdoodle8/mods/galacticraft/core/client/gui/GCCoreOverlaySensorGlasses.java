package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.Iterator;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreOverlaySensorGlasses.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreOverlaySensorGlasses extends GCCoreOverlay
{
	private static final ResourceLocation hudTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/hud.png");
	private static final ResourceLocation indicatorTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/indicator.png");

	private static Minecraft minecraft = FMLClientHandler.instance().getClient();

	private static int zoom = 0;

	/**
	 * Render the GUI that displays sensor glasses
	 */
	public static void renderSensorGlassesMain(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY)
	{
		GCCoreOverlaySensorGlasses.zoom++;

		final float f = MathHelper.sin(GCCoreOverlaySensorGlasses.zoom / 80.0F) * 0.1F + 0.1F;

		final ScaledResolution scaledresolution = new ScaledResolution(GCCoreOverlaySensorGlasses.minecraft.gameSettings, GCCoreOverlaySensorGlasses.minecraft.displayWidth, GCCoreOverlaySensorGlasses.minecraft.displayHeight);
		final int i = scaledresolution.getScaledWidth();
		final int k = scaledresolution.getScaledHeight();
		GCCoreOverlaySensorGlasses.minecraft.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCCoreOverlaySensorGlasses.hudTexture);
		final Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(i / 2 - 2 * k - f * 80, k + f * 40, -90D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(i / 2 + 2 * k + f * 80, k + f * 40, -90D, 1.0D, 1.0D);
		tessellator.addVertexWithUV(i / 2 + 2 * k + f * 80, 0.0D - f * 40, -90D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(i / 2 - 2 * k - f * 80, 0.0D - f * 40, -90D, 0.0D, 0.0D);
		tessellator.draw();

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void renderSensorGlassesValueableBlocks(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY)
	{
		final Iterator<Vector3> var51 = ClientProxyCore.valueableBlocks.iterator();
		double var52;
		double var58;
		double var59;
		double var20;
		double var21;
		float var60;

		while (var51.hasNext())
		{
			Vector3 coords = var51.next();

			var52 = ClientProxyCore.playerPosX - coords.x - 0.5D;
			var58 = ClientProxyCore.playerPosY - coords.y - 0.5D;
			var59 = ClientProxyCore.playerPosZ - coords.z - 0.5D;
			var60 = (float) Math.toDegrees(Math.atan2(var52, var59));
			var20 = Math.sqrt(var52 * var52 + var58 * var58 + var59 * var59) * 0.5D;
			var21 = Math.sqrt(var52 * var52 + var59 * var59) * 0.5D;

			final ScaledResolution var5 = new ScaledResolution(GCCoreOverlaySensorGlasses.minecraft.gameSettings, GCCoreOverlaySensorGlasses.minecraft.displayWidth, GCCoreOverlaySensorGlasses.minecraft.displayHeight);
			final int var6 = var5.getScaledWidth();
			final int var7 = var5.getScaledHeight();

			boolean var2 = false;

			final GCCorePlayerSP client = PlayerUtil.getPlayerBaseClientFromPlayer(GCCoreOverlaySensorGlasses.minecraft.thePlayer, false);

			if (client != null)
			{
				var2 = client.getUsingGoggles();
			}

			GCCoreOverlaySensorGlasses.minecraft.fontRenderer.drawString(StatCollector.translateToLocal("gui.sensor.advanced") + ": " + (var2 ? StatCollector.translateToLocal("gui.sensor.advancedon") : StatCollector.translateToLocal("gui.sensor.advancedoff")), var6 / 2 - 50, 4, 0x03b88f);

			try
			{
				GL11.glPushMatrix();

				if (var20 < 4.0D)
				{
					GL11.glColor4f(0.0F, 255F / 255F, 198F / 255F, (float) Math.min(1.0D, Math.max(0.2D, (var20 - 1.0D) * 0.1D)));
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCCoreOverlaySensorGlasses.indicatorTexture);
					GL11.glRotatef(-var60 - ClientProxyCore.playerRotationYaw + 180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslated(0.0D, var2 ? -var20 * 16 : -var21 * 16, 0.0D);
					GL11.glRotatef(-(-var60 - ClientProxyCore.playerRotationYaw + 180.0F), 0.0F, 0.0F, 1.0F);
					GCCoreOverlay.drawCenteringRectangle(var6 / 2, var7 / 2, 1.0D, 8.0D, 8.0D);
				}
			}
			finally
			{
				GL11.glPopMatrix();
			}
		}
	}
}
