package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayLander extends Overlay
{
    /**
     * Render the GUI when player is in inventory
     */
    public static void renderLanderOverlay(Minecraft mc, long ticks)
    {
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(mc, mc.displayWidth, mc.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        mc.entityRenderer.setupOverlayRendering();

        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 0.0F);

        if (mc.player.getRidingEntity().motionY < -2.0)
        {
            mc.fontRenderer.drawString(GCCoreUtil.translate("gui.warning"), width / 4 - mc.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.warning")) / 2, height / 8 - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
            final int alpha = (int) (200 * (Math.sin(ticks) * 0.5F + 0.5F)) + 5;
            final String press1 = GCCoreUtil.translate("gui.lander.warning2");
            final String press2 = GCCoreUtil.translate("gui.lander.warning3");
            mc.fontRenderer.drawString(press1 + GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()) + press2, width / 4 - mc.fontRenderer.getStringWidth(press1 + GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()) + press2) / 2, height / 8, ColorUtil.to32BitColor(alpha, alpha, alpha, alpha));
        }

        GlStateManager.popMatrix();

        if (mc.player.getRidingEntity().motionY != 0.0D)
        {
            String string = GCCoreUtil.translate("gui.lander.velocity") + ": " + Math.round(((EntityLander) mc.player.getRidingEntity()).motionY * 1000) / 100.0D + " " + GCCoreUtil.translate("gui.lander.velocityu");
            int color = ColorUtil.to32BitColor(255, (int) Math.floor(Math.abs(mc.player.getRidingEntity().motionY) * 51.0D), 255 - (int) Math.floor(Math.abs(mc.player.getRidingEntity().motionY) * 51.0D), 0);
            mc.fontRenderer.drawString(string, width / 2 - mc.fontRenderer.getStringWidth(string) / 2, height / 3, color);
        }
    }
}
