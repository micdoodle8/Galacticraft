package micdoodle8.mods.galacticraft.planets.asteroids.entities.player;

import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.AsteroidEntities;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class AsteroidsPlayerHandler
{
//    @SubscribeEvent
//    public void onPlayerLogin(PlayerLoggedInEvent event)
//    {
//        if (event.player instanceof ServerPlayerEntity)
//        {
//            this.onPlayerLogin((ServerPlayerEntity) event.player);
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerLogout(PlayerLoggedOutEvent event)
//    {
//        if (event.player instanceof ServerPlayerEntity)
//        {
//            this.onPlayerLogout((ServerPlayerEntity) event.player);
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerRespawn(PlayerRespawnEvent event)
//    {
//        if (event.player instanceof ServerPlayerEntity)
//        {
//            this.onPlayerRespawn((ServerPlayerEntity) event.player);
//        }
//    }

//    @SubscribeEvent
//    public void onEntityConstructing(EntityEvent.EntityConstructing event)
//    {
//        if (event.getEntity() instanceof EntityPlayerMP && GCPlayerStats.get((EntityPlayerMP) event.entity) == null)
//        {
//            GCPlayerStats.register((EntityPlayerMP) event.entity);
//        }
//    }

    public void onPlayerUpdate(ServerPlayerEntity player)
    {
        if (!ConfigManagerPlanets.disableSmallAsteroids)
        {
            if (!player.world.isRemote && player.world.getDimension() instanceof DimensionAsteroids)
            {
                final int f = 50;

                if (player.world.rand.nextInt(f) == 0 && player.posY < 260D)
                {
                    final PlayerEntity closestPlayer = player.world.getClosestPlayer(player, 100);

                    if (closestPlayer == null || closestPlayer.getEntityId() <= player.getEntityId())
                    {
                        double x, y, z;
                        double motX, motY, motZ;
                        double r = player.world.rand.nextInt(60) + 30D;
                        double theta = Math.PI * 2.0 * player.world.rand.nextDouble();
                        x = player.posX + Math.cos(theta) * r;
                        y = player.posY + player.world.rand.nextInt(5);
                        z = player.posZ + Math.sin(theta) * r;
                        motX = (player.posX - x + (player.world.rand.nextDouble() - 0.5) * 40) / 400.0F;
                        motY = (player.world.rand.nextDouble() - 0.5) * 0.4;
                        motZ = (player.posZ - z + (player.world.rand.nextDouble() - 0.5) * 40) / 400.0F;

                        final EntitySmallAsteroid smallAsteroid = new EntitySmallAsteroid(AsteroidEntities.SMALL_ASTEROID.get(), player.world);
                        smallAsteroid.setPosition(x, y, z);
//                        smallAsteroid.motionX = motX;
//                        smallAsteroid.motionY = motY;
//                        smallAsteroid.motionZ = motZ;
                        smallAsteroid.setMotion(motX, motY, motZ);
                        smallAsteroid.spinYaw = player.world.rand.nextFloat() * 4;
                        smallAsteroid.spinPitch = player.world.rand.nextFloat() * 2;

                        player.world.addEntity(smallAsteroid);
                    }
                }
            }
        }
    }
}
