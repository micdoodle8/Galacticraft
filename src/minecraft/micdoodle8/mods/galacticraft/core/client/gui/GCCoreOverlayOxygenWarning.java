package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreOverlayOxygenWarning extends GCCoreOverlay
{
	private static Minecraft minecraft = FMLClientHandler.instance().getClient();
	
	private static long screenTicks;

	/**
	 * Render the GUI when player is in inventory
	 */
	public static void renderOxygenWarningOverlay()
	{
		screenTicks++;
		final ScaledResolution scaledresolution = new ScaledResolution(GCCoreOverlayOxygenWarning.minecraft.gameSettings, GCCoreOverlayOxygenWarning.minecraft.displayWidth, GCCoreOverlayOxygenWarning.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        GCCoreOverlayOxygenWarning.minecraft.entityRenderer.setupOverlayRendering();
        GCCoreFontRendererBig fr = new GCCoreFontRendererBig(minecraft.gameSettings, "/font/default.png", minecraft.renderEngine, false);
        fr.drawString("WARNING!", width / 4 - (fr.getStringWidth(LanguageRegistry.instance().getStringLocalization("gui.warning")) / 2), height / 8 - 20, GCCoreUtil.convertTo32BitColor(255, 255, 0, 0));
        int alpha = (int) (255 * (Math.sin(screenTicks / 20.0F)));
        fr.drawString(LanguageRegistry.instance().getStringLocalization("gui.oxygen.warning"), width / 4 - (fr.getStringWidth(LanguageRegistry.instance().getStringLocalization("gui.oxygen.warning")) / 2), height / 8, GCCoreUtil.convertTo32BitColor(alpha, alpha, alpha, alpha));
	}
}
