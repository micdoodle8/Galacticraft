package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreOverlayCountdown.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class OverlayCountdown extends Overlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    public static void renderCountdownOverlay()
    {
        int count = ((EntitySpaceshipBase) OverlayCountdown.minecraft.thePlayer.ridingEntity).timeUntilLaunch / 2;

        count = Math.round(count / 10);

        final ScaledResolution scaledresolution = new ScaledResolution(OverlayCountdown.minecraft.gameSettings, OverlayCountdown.minecraft.displayWidth, OverlayCountdown.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        OverlayCountdown.minecraft.entityRenderer.setupOverlayRendering();

        GL11.glPushMatrix();

        if (count <= 10)
        {
            GL11.glScalef(4.0F, 4.0F, 0.0F);

            OverlayCountdown.minecraft.fontRenderer.drawString(String.valueOf(count), width / 8 - OverlayCountdown.minecraft.fontRenderer.getStringWidth(String.valueOf(count)) / 2, height / 20, GCCoreUtil.to32BitColor(255, 255, 0, 0));
        }
        else
        {
            GL11.glScalef(2.0F, 2.0F, 0.0F);

            OverlayCountdown.minecraft.fontRenderer.drawString(String.valueOf(count), width / 4 - OverlayCountdown.minecraft.fontRenderer.getStringWidth(String.valueOf(count)) / 2, height / 8, GCCoreUtil.to32BitColor(255, 255, 0, 0));
        }

        GL11.glPopMatrix();
    }
}
