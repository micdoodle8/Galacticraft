package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayLaunchCountdown extends Overlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    public static void renderCountdownOverlay()
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        int count = ((EntitySpaceshipBase) OverlayLaunchCountdown.minecraft.thePlayer.ridingEntity).timeUntilLaunch / 2;

        count = Math.round(count / 10);

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
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
