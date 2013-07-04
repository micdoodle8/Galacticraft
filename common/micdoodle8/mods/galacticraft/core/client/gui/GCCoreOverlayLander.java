package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.ResourceLocation;
import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreOverlayLander extends GCCoreOverlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    private static long screenTicks;

    /**
     * Render the GUI when player is in inventory
     */
    public static void renderLanderOverlay()
    {
        GCCoreOverlayLander.screenTicks++;
        final ScaledResolution scaledresolution = new ScaledResolution(GCCoreOverlayLander.minecraft.gameSettings, GCCoreOverlayLander.minecraft.displayWidth, GCCoreOverlayLander.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        GCCoreOverlayLander.minecraft.entityRenderer.setupOverlayRendering();

        if (GCCoreOverlayLander.minecraft.thePlayer.ridingEntity.motionY < -2.0)
        {
            final GCCoreFontRendererLarge fr = new GCCoreFontRendererLarge(GCCoreOverlayLander.minecraft.gameSettings, new ResourceLocation("textures/font/ascii.png"), GCCoreOverlayLander.minecraft.renderEngine, false);
            fr.drawString(LanguageRegistry.instance().getStringLocalization("gui.warning"), width / 4 - fr.getStringWidth(LanguageRegistry.instance().getStringLocalization("gui.warning")) / 2, height / 8 - 20, GCCoreUtil.convertTo32BitColor(255, 255, 0, 0));
            final int alpha = (int) (255 * Math.sin(GCCoreOverlayLander.screenTicks / 20.0F));
            final String press1 = LanguageRegistry.instance().getStringLocalization("gui.lander.warning2");
            final String press2 = LanguageRegistry.instance().getStringLocalization("gui.lander.warning3");
            fr.drawString(press1 + Keyboard.getKeyName(ClientProxyCore.GCKeyHandler.spaceKey.keyCode) + press2, width / 4 - fr.getStringWidth(press1 + Keyboard.getKeyName(ClientProxyCore.GCKeyHandler.spaceKey.keyCode) + press2) / 2, height / 8, GCCoreUtil.convertTo32BitColor(alpha, alpha, alpha, alpha));
        }

        if (GCCoreOverlayLander.minecraft.thePlayer.ridingEntity.motionY != 0.0D)
        {
            String string = LanguageRegistry.instance().getStringLocalization("gui.lander.velocity") + ": " + Math.round(((GCCoreEntityLander) GCCoreOverlayLander.minecraft.thePlayer.ridingEntity).motionY * 1000) / 100.0D + " " + LanguageRegistry.instance().getStringLocalization("gui.lander.velocityu");
            int color = GCCoreUtil.convertTo32BitColor(255, (int) Math.floor(Math.abs(GCCoreOverlayLander.minecraft.thePlayer.ridingEntity.motionY) * 51.0D), 0, 255 - (int) Math.floor(Math.abs(GCCoreOverlayLander.minecraft.thePlayer.ridingEntity.motionY) * 51.0D));
            GCCoreOverlayLander.minecraft.fontRenderer.drawString(string, width / 2 - GCCoreOverlayLander.minecraft.fontRenderer.getStringWidth(string) / 2, height / 3, color);
        }
    }
}
