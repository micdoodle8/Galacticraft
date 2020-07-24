package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OverlayLander extends Overlay
{
    /**
     * Render the GUI when player is in inventory
     */
    public static void renderLanderOverlay(long ticks)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getRidingEntity() instanceof EntityLander)
        {
            int width = (int) (mc.mouseHelper.getMouseX() * (double) mc.getMainWindow().getScaledWidth() / (double) mc.getMainWindow().getWidth());
            int height = (int) (mc.mouseHelper.getMouseY() * (double) mc.getMainWindow().getScaledHeight() / (double) mc.getMainWindow().getHeight());
//        mc.entityRenderer.setupOverlayRendering();

            RenderSystem.pushMatrix();
            RenderSystem.scalef(2.0F, 2.0F, 0.0F);

            if (mc.player.getRidingEntity().getMotion().y < -2.0)
            {
                mc.fontRenderer.drawString(GCCoreUtil.translate("gui.warning"), width / 4.0F - mc.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.warning")) / 2.0F, height / 8.0F - 20, ColorUtil.to32BitColor(255, 255, 0, 0));
                final int alpha = (int) (200 * (Math.sin(ticks) * 0.5F + 0.5F)) + 5;
                final String press1 = GCCoreUtil.translate("gui.lander.warning2");
                final String press2 = GCCoreUtil.translate("gui.lander.warning3");
                mc.fontRenderer.drawString(press1 + KeyHandlerClient.spaceKey.getLocalizedName() + press2, width / 4.0F - mc.fontRenderer.getStringWidth(press1 + KeyHandlerClient.spaceKey.getLocalizedName() + press2) / 2.0F, height / 8.0F, ColorUtil.to32BitColor(alpha, alpha, alpha, alpha));
            }

            RenderSystem.popMatrix();

            if (mc.player.getRidingEntity().getMotion().y != 0.0D)
            {
                String string = GCCoreUtil.translate("gui.lander.velocity") + ": " + Math.round(mc.player.getRidingEntity().getMotion().y * 1000) / 100.0D + " " + GCCoreUtil.translate("gui.lander.velocityu");
                int color = ColorUtil.to32BitColor(255, (int) Math.floor(Math.abs(mc.player.getRidingEntity().getMotion().y) * 51.0D), 255 - (int) Math.floor(Math.abs(mc.player.getRidingEntity().getMotion().y) * 51.0D), 0);
                mc.fontRenderer.drawString(string, width / 2.0F - mc.fontRenderer.getStringWidth(string) / 2.0F, height / 3.0F, color);
            }
        }
    }
}
