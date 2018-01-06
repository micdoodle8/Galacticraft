package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayLaunchCountdown extends Overlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    public static void renderCountdownOverlay()
    {
        GlStateManager.disableLighting();
        int count = ((EntitySpaceshipBase) OverlayLaunchCountdown.minecraft.player.getRidingEntity()).timeUntilLaunch / 2;

        count = (int) Math.floor(count / 10.0F);

        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(OverlayLaunchCountdown.minecraft, OverlayLaunchCountdown.minecraft.displayWidth, OverlayLaunchCountdown.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        OverlayLaunchCountdown.minecraft.entityRenderer.setupOverlayRendering();

        GL11.glPushMatrix();

        if (count <= 10)
        {
            GL11.glScalef(4.0F, 4.0F, 0.0F);

            OverlayLaunchCountdown.minecraft.fontRenderer.drawString(String.valueOf(count), width / 8 - OverlayLaunchCountdown.minecraft.fontRenderer.getStringWidth(String.valueOf(count)) / 2, height / 20, ColorUtil.to32BitColor(255, 255, 0, 0));
        }
        else
        {
            GL11.glScalef(2.0F, 2.0F, 0.0F);

            OverlayLaunchCountdown.minecraft.fontRenderer.drawString(String.valueOf(count), width / 4 - OverlayLaunchCountdown.minecraft.fontRenderer.getStringWidth(String.valueOf(count)) / 2, height / 8, ColorUtil.to32BitColor(255, 255, 0, 0));
        }

        GL11.glPopMatrix();
        GlStateManager.enableLighting();
    }
}
