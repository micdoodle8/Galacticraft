package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreOverlayLander.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class OverlayLander extends Overlay
{
	private static Minecraft minecraft = FMLClientHandler.instance().getClient();

	private static long screenTicks;

	/**
	 * Render the GUI when player is in inventory
	 */
	public static void renderLanderOverlay()
	{
		OverlayLander.screenTicks++;
		final ScaledResolution scaledresolution = new ScaledResolution(OverlayLander.minecraft.gameSettings, OverlayLander.minecraft.displayWidth, OverlayLander.minecraft.displayHeight);
		final int width = scaledresolution.getScaledWidth();
		final int height = scaledresolution.getScaledHeight();
		OverlayLander.minecraft.entityRenderer.setupOverlayRendering();

		GL11.glPushMatrix();

		GL11.glScalef(2.0F, 2.0F, 0.0F);

		if (OverlayLander.minecraft.thePlayer.ridingEntity.motionY < -2.0)
		{
			OverlayLander.minecraft.fontRenderer.drawString(StatCollector.translateToLocal("gui.warning"), width / 4 - OverlayLander.minecraft.fontRenderer.getStringWidth(StatCollector.translateToLocal("gui.warning")) / 2, height / 8 - 20, GCCoreUtil.to32BitColor(255, 255, 0, 0));
			final int alpha = (int) (255 * Math.sin(OverlayLander.screenTicks / 20.0F));
			final String press1 = StatCollector.translateToLocal("gui.lander.warning2");
			final String press2 = StatCollector.translateToLocal("gui.lander.warning3");
			OverlayLander.minecraft.fontRenderer.drawString(press1 + Keyboard.getKeyName(KeyHandlerClient.spaceKey.getKeyCode()) + press2, width / 4 - OverlayLander.minecraft.fontRenderer.getStringWidth(press1 + Keyboard.getKeyName(KeyHandlerClient.spaceKey.getKeyCode()) + press2) / 2, height / 8, GCCoreUtil.to32BitColor(alpha, alpha, alpha, alpha));
		}

		GL11.glPopMatrix();

		if (OverlayLander.minecraft.thePlayer.ridingEntity.motionY != 0.0D)
		{
			String string = StatCollector.translateToLocal("gui.lander.velocity") + ": " + Math.round(((EntityLander) OverlayLander.minecraft.thePlayer.ridingEntity).motionY * 1000) / 100.0D + " " + StatCollector.translateToLocal("gui.lander.velocityu");
			int color = GCCoreUtil.to32BitColor(255, (int) Math.floor(Math.abs(OverlayLander.minecraft.thePlayer.ridingEntity.motionY) * 51.0D), 255 - (int) Math.floor(Math.abs(OverlayLander.minecraft.thePlayer.ridingEntity.motionY) * 51.0D), 0);
			OverlayLander.minecraft.fontRenderer.drawString(string, width / 2 - OverlayLander.minecraft.fontRenderer.getStringWidth(string) / 2, height / 3, color);
		}
	}
}
