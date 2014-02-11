package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEffectHandler.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreEffectHandler
{
	public static void spawnParticle(String particleID, Vector3 position, Vector3 motion, Vector3 color)
	{
		Minecraft mc = FMLClientHandler.instance().getClient();

		if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
		{
			double dX = mc.renderViewEntity.posX - position.x;
			double dY = mc.renderViewEntity.posY - position.y;
			double dZ = mc.renderViewEntity.posZ - position.z;
			EntityFX particle = null;
			double viewDistance = 64.0D;

			if (particleID.equals("whiteSmokeIdle"))
			{
				particle = new GCCoreEntityLaunchSmokeFX(mc.theWorld, position, motion, 1.0F, false);
			}
			else if (particleID.equals("whiteSmokeLaunched"))
			{
				particle = new GCCoreEntityLaunchSmokeFX(mc.theWorld, position, motion, 1.0F, true);
			}
			else if (particleID.equals("whiteSmokeLargeIdle"))
			{
				particle = new GCCoreEntityLaunchSmokeFX(mc.theWorld, position, motion, 2.5F, false);
			}
			else if (particleID.equals("whiteSmokeLargeLaunched"))
			{
				particle = new GCCoreEntityLaunchSmokeFX(mc.theWorld, position, motion, 2.5F, true);
			}
			else if (particleID.equals("launchFlameIdle"))
			{
				particle = new GCCoreEntityLaunchFlameFX(mc.theWorld, position, motion, 1F, false);
			}
			else if (particleID.equals("launchFlameLaunched"))
			{
				particle = new GCCoreEntityLaunchFlameFX(mc.theWorld, position, motion, 1F, true);
			}
			else if (particleID.equals("distanceSmoke") && dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance * 1.7)
			{
				particle = new EntitySmokeFX(mc.theWorld, position.x, position.y, position.z, motion.x, motion.y, motion.z, 2.5F);
			}

			if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
			{
				if (particleID.equals("oxygen"))
				{
					particle = new GCCoreEntityOxygenFX(mc.theWorld, position, motion, color);
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
