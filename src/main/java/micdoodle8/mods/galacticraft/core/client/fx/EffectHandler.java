//package micdoodle8.mods.galacticraft.core.client.fx;
//
//import micdoodle8.mods.galacticraft.api.vector.Vector3;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.particle.Particle;
//import net.minecraft.entity.LivingEntity;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//@OnlyIn(Dist.CLIENT)
//public class EffectHandler
//{
//    public static void addParticle(String particleID, Vector3 position, Vector3 motion, Object... otherInfo)
//    {
//        Minecraft mc = Minecraft.getInstance();
//
//        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
//        {
//            double dX = mc.getRenderViewEntity().posX - position.x;
//            double dY = mc.getRenderViewEntity().posY - position.y;
//            double dZ = mc.getRenderViewEntity().posZ - position.z;
//            Particle particle = null;
//            double viewDistance = 64.0D;
//
//            if (particleID.equals("whiteSmokeIdle"))
//            {
//                particle = new ParticleLaunchSmoke(mc.world, position, motion, 1.0F, false);
//            }
//            else if (particleID.equals("whiteSmokeLaunched"))
//            {
//                particle = new ParticleLaunchSmoke(mc.world, position, motion, 1.0F, true);
//            }
//            else if (particleID.equals("whiteSmokeLargeIdle"))
//            {
//                particle = new ParticleLaunchSmoke(mc.world, position, motion, 2.5F, false);
//            }
//            else if (particleID.equals("whiteSmokeLargeLaunched"))
//            {
//                particle = new ParticleLaunchSmoke(mc.world, position, motion, 2.5F, true);
//            }
//            else if (particleID.equals(GCParticles.LAUNCH_FLAME_IDLE))
//            {
//                particle = new ParticleLaunchFlame(mc.world, position, motion, false, (LivingEntity) otherInfo[0]);
//            }
//            else if (particleID.equals(GCParticles.LAUNCH_FLAME_LAUNCHED))
//            {
//                particle = new ParticleLaunchFlame(mc.world, position, motion, true, (LivingEntity) otherInfo[0]);
//            }
//            else if (particleID.equals("whiteSmokeTiny"))
//            {
//                particle = new ParticleSmokeSmall(mc.world, position, motion);
//            }
//            else if (particleID.equals("oilDrip"))
//            {
//                particle = new ParticleOilDrip(mc.world, position.x, position.y, position.z);
//            }
//
//            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
//            {
//                if (particleID.equals("oxygen"))
//                {
//                    particle = new ParticleOxygen(mc.world, position, motion, (Vector3) otherInfo[0]);
//                }
//            }
//
//            if (particle != null)
//            {
//                mc.effectRenderer.addEffect(particle);
//            }
//        }
//    }
//}
