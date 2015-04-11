package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayOxygenWarning extends Overlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    private static long screenTicks;

    /**
     * Render the GUI when player is in inventory
     */
    public static void renderOxygenWarningOverlay()
    {
        OverlayOxygenWarning.screenTicks++;
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(OverlayOxygenWarning.minecraft, OverlayOxygenWarning.minecraft.displayWidth, OverlayOxygenWarning.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        OverlayOxygenWarning.minecraft.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        RenderHelper.enableStandardItemLighting();

        GL11.glPushMatrix();

        GL11.glScalef(2.0F, 2.0F, 0.0F);

        OverlayOxygenWarning.minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.warning"), width / 4 - OverlayOxygenWarning.minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.warning")) / 2, height / 8 - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
        final int alpha = (int) (200 * (Math.sin(OverlayOxygenWarning.screenTicks / 20.0F) * 0.5F + 0.5F)) + 5;
        OverlayOxygenWarning.minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.oxygen.warning"), width / 4 - OverlayOxygenWarning.minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.oxygen.warning")) / 2, height / 8, ColorUtil.to32BitColor(alpha, 255, 0, 0));

        GL11.glPopMatrix();
    }
}
