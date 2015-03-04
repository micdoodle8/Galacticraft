package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EffectHandler
{
    public static void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... otherInfo)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            double dX = mc.getRenderViewEntity().posX - position.x;
            double dY = mc.getRenderViewEntity().posY - position.y;
            double dZ = mc.getRenderViewEntity().posZ - position.z;
            EntityFX particle = null;
            double viewDistance = 64.0D;

            if (particleID.equals("whiteSmokeIdle"))
            {
                particle = new EntityFXLaunchSmoke(mc.theWorld, position, motion, 1.0F, false);
            }
            else if (particleID.equals("whiteSmokeLaunched"))
            {
                particle = new EntityFXLaunchSmoke(mc.theWorld, position, motion, 1.0F, true);
            }
            else if (particleID.equals("whiteSmokeLargeIdle"))
            {
                particle = new EntityFXLaunchSmoke(mc.theWorld, position, motion, 2.5F, false);
            }
            else if (particleID.equals("whiteSmokeLargeLaunched"))
            {
                particle = new EntityFXLaunchSmoke(mc.theWorld, position, motion, 2.5F, true);
            }
            else if (particleID.equals("launchFlameIdle"))
            {
                particle = new EntityFXLaunchFlame(mc.theWorld, position, motion, false, (EntityLivingBase)otherInfo[0]);
            }
            else if (particleID.equals("launchFlameLaunched"))
            {
                particle = new EntityFXLaunchFlame(mc.theWorld, position, motion, true, (EntityLivingBase)otherInfo[0]);
            }
            else if (particleID.equals("whiteSmokeTiny"))
            {
                particle = new EntityFXSmokeSmall(mc.theWorld, position, motion);
            }

            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
            {
                if (particleID.equals("oxygen"))
                {
                    particle = new EntityFXEntityOxygen(mc.theWorld, position, motion, (Vector3) otherInfo[0]);
                }
            }

            if (particle != null)
            {
                particle.prevPosX = particle.posX;
                particle.prevPosY = particle.posY;
                particle.prevPosZ = particle.posZ;
                mc.effectRenderer.addEffect(particle);
            }
        }
    }
}
