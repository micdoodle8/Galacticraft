package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OverlayDockingRocket extends Overlay
{
    /**
     * Render the GUI when player is docking a vehicle
     */
    public static void renderDockingOverlay(long ticks)
    {
        Minecraft mc = Minecraft.getInstance();
//        mc.entityRenderer.setupOverlayRendering(); TODO Needed?
        int width = (int) (mc.mouseHelper.getMouseX() * (double) mc.getMainWindow().getScaledWidth() / (double) mc.getMainWindow().getWidth());
        int height = (int) (mc.mouseHelper.getMouseY() * (double) mc.getMainWindow().getScaledHeight() / (double) mc.getMainWindow().getHeight());

        if (mc.player.getRidingEntity() instanceof EntityAutoRocket)
        {
            EntityAutoRocket rocket = (EntityAutoRocket) mc.player.getRidingEntity();

            if (rocket.launchPhase == EnumLaunchPhase.LANDING.ordinal() && rocket.targetVec != null)
            {
                double dX = Math.round((rocket.getPosX() - rocket.targetVec.getX()) * 100.0D) / 100.0D;
                double dY = Math.round((rocket.getPosY() - rocket.targetVec.getY()) * 100.0D) / 100.0D;
                double dZ = Math.round((rocket.getPosZ() - rocket.targetVec.getZ()) * 100.0D) / 100.0D;
                String dXStr = String.valueOf(dX);
                String dYStr = String.valueOf(dY);
                String dZStr = String.valueOf(dZ);

                double targetMotionY = Math.round(Math.max((rocket.getPosY() - rocket.targetVec.getY()) / -100.0D, -0.9D) * 100.0D) / 100.0D;
                double currentMotionY = Math.round(rocket.getMotion().y * 100.0D) / 100.0D;
                double dMY = Math.floor((targetMotionY - currentMotionY) * 300);
                int dMotionY = (int) Math.max(1, Math.min(255, dMY));
                int dMotionYN = (int) Math.max(1, Math.min(255, -dMY));
                String targetMotionYStr = GCCoreUtil.translate("gui.docking_rocket.target_vel") + ": " + String.format("%.2f", targetMotionY);
                String currentMotionYStr = GCCoreUtil.translate("gui.docking_rocket.current_vel") + ": " + String.format("%.2f", currentMotionY);

                int red = ColorUtil.to32BitColor(dMY > 0 ? 0 : dMotionYN, 255, 255, 255);
                int green = ColorUtil.to32BitColor(dMY < 0 ? 0 : dMotionY, 255, 255, 255);
                int grey = ColorUtil.to32BitColor(255, 220, 220, 220);

//                if (dMY > 25)
//                {
//                    String warning = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.0", GameSettings.getKeymessage(KeyHandlerClient.spaceKey.getKeyCode()));
//                    mc.fontRenderer.drawString(warning, width / 2 - mc.fontRenderer.getStringWidth(warning) / 2, height / 3 - 50, green);
//                }
//                else if (dMY < -25)
//                {
//                    String warning2 = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.1", GameSettings.getKeymessage(KeyHandlerClient.leftShiftKey.getKeyCode()));
//                    mc.fontRenderer.drawString(warning2, width / 2 - mc.fontRenderer.getStringWidth(warning2) / 2, height / 3 - 35, red);
//                }

                mc.fontRenderer.drawString(targetMotionYStr, width - mc.fontRenderer.getStringWidth(targetMotionYStr) - 50, height / 3.0F + 50, grey);
                mc.fontRenderer.drawString(currentMotionYStr, width - mc.fontRenderer.getStringWidth(currentMotionYStr) - 50, height / 3.0F + 35, grey);

                mc.fontRenderer.drawString(GCCoreUtil.translate("gui.docking_rocket.distance_from"), 50, height / 3.0F + 15, grey);
                mc.fontRenderer.drawString("X: " + dXStr, 50, height / 3.0F + 35, Math.abs(dX) > 15 ? red : grey);
                mc.fontRenderer.drawString("Y: " + dYStr, 50, height / 3.0F + 45, Math.abs(dY) > 50 || Math.abs(dY) < 1.9 ? grey : ticks / 10 % 2 == 0 ? red : grey);
                mc.fontRenderer.drawString("Z: " + dZStr, 50, height / 3.0F + 55, Math.abs(dZ) > 15 ? red : grey);
            }
        }
    }
}
