package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class OverlayLander extends Overlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    private static long screenTicks;

    /**
     * Render the GUI when player is in inventory
     */
    public static void renderLanderOverlay()
    {
        OverlayLander.screenTicks++;
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(minecraft, OverlayLander.minecraft.displayWidth, OverlayLander.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        OverlayLander.minecraft.entityRenderer.setupOverlayRendering();

        GL11.glPushMatrix();

        GL11.glScalef(2.0F, 2.0F, 0.0F);

        if (OverlayLander.minecraft.thePlayer.ridingEntity.motionY < -2.0)
        {
            OverlayLander.minecraft.fontRendererObj.drawString(GCCoreUtil.translate("gui.warning"), width / 4 - OverlayLander.minecraft.fontRendererObj.getStringWidth(GCCoreUtil.translate("gui.warning")) / 2, height / 8 - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
            final int alpha = (int) (255 * Math.sin(OverlayLander.screenTicks / 20.0F));
            final String press1 = GCCoreUtil.translate("gui.lander.warning2");
            final String press2 = GCCoreUtil.translate("gui.lander.warning3");
            OverlayLander.minecraft.fontRendererObj.drawString(press1 + GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()) + press2, width / 4 - OverlayLander.minecraft.fontRendererObj.getStringWidth(press1 + GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()) + press2) / 2, height / 8, ColorUtil.to32BitColor(alpha, alpha, alpha, alpha));
        }

        GL11.glPopMatrix();

        if (OverlayLander.minecraft.thePlayer.ridingEntity.motionY != 0.0D)
        {
            String string = GCCoreUtil.translate("gui.lander.velocity") + ": " + Math.round(((EntityLander) OverlayLander.minecraft.thePlayer.ridingEntity).motionY * 1000) / 100.0D + " " + GCCoreUtil.translate("gui.lander.velocityu");
            int color = ColorUtil.to32BitColor(255, (int) Math.floor(Math.abs(OverlayLander.minecraft.thePlayer.ridingEntity.motionY) * 51.0D), 255 - (int) Math.floor(Math.abs(OverlayLander.minecraft.thePlayer.ridingEntity.motionY) * 51.0D), 0);
            OverlayLander.minecraft.fontRendererObj.drawString(string, width / 2 - OverlayLander.minecraft.fontRendererObj.getStringWidth(string) / 2, height / 3, color);
        }
    }
}
