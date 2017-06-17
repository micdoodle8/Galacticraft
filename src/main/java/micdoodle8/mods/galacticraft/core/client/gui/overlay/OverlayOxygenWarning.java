package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayOxygenWarning extends Overlay
{
    private static long screenTicks;

    /**
     * Render the GUI when player is in inventory
     */
    public static void renderOxygenWarningOverlay()
    {
        OverlayOxygenWarning.screenTicks++;
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(mc, mc.displayWidth, mc.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 0.0F);
        GlStateManager.enableBlend();
        mc.fontRendererObj.drawString(GCCoreUtil.translate("gui.warning"), width / 4 - mc.fontRendererObj.getStringWidth(GCCoreUtil.translate("gui.warning")) / 2, height / 8 - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
        final int alpha = (int) (200 * (Math.sin(OverlayOxygenWarning.screenTicks / 20.0F) * 0.5F + 0.5F)) + 5;
        mc.fontRendererObj.drawString(GCCoreUtil.translate("gui.oxygen.warning"), width / 4 - mc.fontRendererObj.getStringWidth(GCCoreUtil.translate("gui.oxygen.warning")) / 2, height / 8, ColorUtil.to32BitColor(alpha, 255, 0, 0));
        GlStateManager.popMatrix();
    }
}
