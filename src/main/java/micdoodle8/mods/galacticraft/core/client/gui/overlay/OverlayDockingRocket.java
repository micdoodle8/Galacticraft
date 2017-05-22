package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayDockingRocket extends Overlay
{
    private static Minecraft minecraft = FMLClientHandler.instance().getClient();

    private static long screenTicks;

    /**
     * Render the GUI when player is docking a vehicle
     */
    public static void renderDockingOverlay()
    {
        OverlayDockingRocket.screenTicks++;
        final ScaledResolution scaledresolution = ClientUtil.getScaledRes(OverlayDockingRocket.minecraft, OverlayDockingRocket.minecraft.displayWidth, OverlayDockingRocket.minecraft.displayHeight);
        final int width = scaledresolution.getScaledWidth();
        final int height = scaledresolution.getScaledHeight();
        OverlayDockingRocket.minecraft.entityRenderer.setupOverlayRendering();

        if (OverlayDockingRocket.minecraft.thePlayer.ridingEntity instanceof EntityAutoRocket)
        {
            EntityAutoRocket rocket = (EntityAutoRocket) OverlayDockingRocket.minecraft.thePlayer.ridingEntity;

            if (rocket.launchPhase == EnumLaunchPhase.LANDING.ordinal() && rocket.targetVec != null)
            {
                double dX = Math.round((rocket.posX - rocket.targetVec.getX()) * 100.0D) / 100.0D;
                double dY = Math.round((rocket.posY - rocket.targetVec.getY()) * 100.0D) / 100.0D;
                double dZ = Math.round((rocket.posZ - rocket.targetVec.getZ()) * 100.0D) / 100.0D;
                String dXStr = String.valueOf(dX);
                String dYStr = String.valueOf(dY);
                String dZStr = String.valueOf(dZ);

                double targetMotionY = Math.round(Math.max((rocket.posY - rocket.targetVec.getY()) / -100.0D, -0.9D) * 100.0D) / 100.0D;
                double currentMotionY = Math.round(rocket.motionY * 100.0D) / 100.0D;
                double dMY = Math.floor((targetMotionY - currentMotionY) * 300);
                int dMotionY = (int) Math.max(1, Math.min(255, dMY));
                int dMotionYN = (int) Math.max(1, Math.min(255, -dMY));
                String targetMotionYStr = GCCoreUtil.translate("gui.docking_rocket.target_vel.name") + ": " + String.format("%.2f", targetMotionY);
                String currentMotionYStr = GCCoreUtil.translate("gui.docking_rocket.current_vel.name") + ": " + String.format("%.2f", currentMotionY);

                int red = ColorUtil.to32BitColor(dMY > 0 ? 0 : dMotionYN, 255, 255, 255);
                int green = ColorUtil.to32BitColor(dMY < 0 ? 0 : dMotionY, 255, 255, 255);
                int grey = ColorUtil.to32BitColor(255, 220, 220, 220);

//                if (dMY > 25)
//                {
//                    String warning = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.0", GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()));
//                    OverlayDockingRocket.minecraft.fontRendererObj.drawString(warning, width / 2 - OverlayDockingRocket.minecraft.fontRendererObj.getStringWidth(warning) / 2, height / 3 - 50, green);
//                }
//                else if (dMY < -25)
//                {
//                    String warning2 = GCCoreUtil.translateWithFormat("gui.docking_rocket.warning.name.1", GameSettings.getKeyDisplayString(KeyHandlerClient.leftShiftKey.getKeyCode()));
//                    OverlayDockingRocket.minecraft.fontRendererObj.drawString(warning2, width / 2 - OverlayDockingRocket.minecraft.fontRendererObj.getStringWidth(warning2) / 2, height / 3 - 35, red);
//                }

                OverlayDockingRocket.minecraft.fontRendererObj.drawString(targetMotionYStr, width - OverlayDockingRocket.minecraft.fontRendererObj.getStringWidth(targetMotionYStr) - 50, height / 3 + 50, grey);
                OverlayDockingRocket.minecraft.fontRendererObj.drawString(currentMotionYStr, width - OverlayDockingRocket.minecraft.fontRendererObj.getStringWidth(currentMotionYStr) - 50, height / 3 + 35, grey);

                OverlayDockingRocket.minecraft.fontRendererObj.drawString(GCCoreUtil.translate("gui.docking_rocket.distance_from.name"), 50, height / 3 + 15, grey);
                OverlayDockingRocket.minecraft.fontRendererObj.drawString("X: " + dXStr, 50, height / 3 + 35, Math.abs(dX) > 15 ? red : grey);
                OverlayDockingRocket.minecraft.fontRendererObj.drawString("Y: " + dYStr, 50, height / 3 + 45, Math.abs(dY) > 50 || Math.abs(dY) < 1.9 ? grey : OverlayDockingRocket.screenTicks / 10 % 2 == 0 ? red : grey);
                OverlayDockingRocket.minecraft.fontRendererObj.drawString("Z: " + dZStr, 50, height / 3 + 55, Math.abs(dZ) > 15 ? red : grey);
            }
        }
    }
}
