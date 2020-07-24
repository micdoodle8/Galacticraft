package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OverlayLaunchCountdown extends Overlay
{
    public static void renderCountdownOverlay()
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getRidingEntity() instanceof EntitySpaceshipBase && !((EntitySpaceshipBase) mc.player.getRidingEntity()).getLaunched())
        {
            RenderSystem.disableLighting();
            int count = ((EntitySpaceshipBase) mc.player.getRidingEntity()).timeUntilLaunch / 2;

            count = (int) Math.floor(count / 10.0F);

            int width = (int) (mc.mouseHelper.getMouseX() * (double) mc.getMainWindow().getScaledWidth() / (double) mc.getMainWindow().getWidth());
            int height = (int) (mc.mouseHelper.getMouseY() * (double) mc.getMainWindow().getScaledHeight() / (double) mc.getMainWindow().getHeight());
//        mc.entityRenderer.setupOverlayRendering();

            RenderSystem.pushMatrix();

            if (count <= 10)
            {
                RenderSystem.scalef(4.0F, 4.0F, 0.0F);
                mc.fontRenderer.drawString(String.valueOf(count), width / 8.0F - mc.fontRenderer.getStringWidth(String.valueOf(count)) / 2.0F, height / 20.0F, ColorUtil.to32BitColor(255, 255, 0, 0));
            }
            else
            {
                RenderSystem.scalef(2.0F, 2.0F, 0.0F);
                mc.fontRenderer.drawString(String.valueOf(count), width / 4.0F - mc.fontRenderer.getStringWidth(String.valueOf(count)) / 2.0F, height / 8.0F, ColorUtil.to32BitColor(255, 255, 0, 0));
            }

            RenderSystem.popMatrix();
            RenderSystem.enableLighting();
        }
    }
}
