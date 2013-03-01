package micdoodle8.mods.galacticraft.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreGuiInGame 
{
	private static Minecraft minecraft = FMLClientHandler.instance().getClient();
	
	public static void renderSpaceshipGui()
	{
		final ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        final int i = scaledresolution.getScaledWidth();
        final int k = scaledresolution.getScaledHeight();
        minecraft.entityRenderer.setupOverlayRendering();
	}
}
