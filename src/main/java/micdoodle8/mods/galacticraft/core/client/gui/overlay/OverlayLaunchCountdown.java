package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayLaunchCountdown extends Overlay
{
    public static void renderCountdownOverlay(Minecraft mc)
    {
        GlStateManager.disableLighting();
        int count = ((EntitySpaceshipBase) mc.player.getRidingEntity()).timeUntilLaunch / 2;

        count = (int) Math.floor(count / 10.0F);

        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(mc, mc.displayWidth, mc.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        mc.entityRenderer.setupOverlayRendering();

        GlStateManager.pushMatrix();

        if (count <= 10)
        {
            GlStateManager.scale(4.0F, 4.0F, 0.0F);
            mc.fontRenderer.drawString(String.valueOf(count), width / 8 - mc.fontRenderer.getStringWidth(String.valueOf(count)) / 2, height / 20, ColorUtil.to32BitColor(255, 255, 0, 0));
        }
        else
        {
            GlStateManager.scale(2.0F, 2.0F, 0.0F);
            mc.fontRenderer.drawString(String.valueOf(count), width / 4 - mc.fontRenderer.getStringWidth(String.valueOf(count)) / 2, height / 8, ColorUtil.to32BitColor(255, 255, 0, 0));
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
}
