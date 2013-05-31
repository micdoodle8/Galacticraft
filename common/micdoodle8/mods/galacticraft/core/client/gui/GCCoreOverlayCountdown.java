package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreOverlayCountdown extends GCCoreOverlay
{
	private static Minecraft minecraft = FMLClientHandler.instance().getClient();

	/**
	 * Render the GUI when player is in inventory
	 */
	public static void renderCountdownOverlay()
	{
		int count = ((EntitySpaceshipBase)GCCoreOverlayCountdown.minecraft.thePlayer.ridingEntity).timeUntilLaunch / 2;

		count = Math.round(count / 10);

		final ScaledResolution scaledresolution = new ScaledResolution(GCCoreOverlayCountdown.minecraft.gameSettings, GCCoreOverlayCountdown.minecraft.displayWidth, GCCoreOverlayCountdown.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        GCCoreOverlayCountdown.minecraft.entityRenderer.setupOverlayRendering();
        final GCCoreFontRendererBig fr = new GCCoreFontRendererBig(GCCoreOverlayCountdown.minecraft.gameSettings, "/font/default.png", GCCoreOverlayCountdown.minecraft.renderEngine, false);

        if (count <= 10)
        {
            fr.drawString(String.valueOf(count), width / 4 - fr.getStringWidth(String.valueOf(count)) / 2, height / 20, GCCoreUtil.convertTo32BitColor(255, 255, 0, 0));
        }
        else
        {
        	GCCoreOverlayCountdown.minecraft.fontRenderer.drawString(String.valueOf(count), width / 2 - fr.getStringWidth(String.valueOf(count)) / 2, height / 8, GCCoreUtil.convertTo32BitColor(255, 255, 0, 0));
        }
	}
}
