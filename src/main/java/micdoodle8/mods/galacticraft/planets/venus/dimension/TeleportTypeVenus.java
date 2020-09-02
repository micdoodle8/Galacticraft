package micdoodle8.mods.galacticraft.planets.venus.dimension;

import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TeleportTypeVenus implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vec3d getPlayerSpawnLocation(ServerWorld world, ServerPlayerEntity player)
    {
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            double x = stats.getCoordsTeleportedFromX();
            double z = stats.getCoordsTeleportedFromZ();
            int limit = ConfigManagerCore.INSTANCE.otherPlanetWorldBorders.get() - 2;
            if (limit > 20)
            {
                if (x > limit)
                {
                    z *= limit / x;
                    x = limit;
                }
                else if (x < -limit)
                {
                    z *= -limit / x;
                    x = -limit;
                }
                if (z > limit)
                {
                    x *= limit / z;
                    z = limit;
                }
                else if (z < -limit)
                {
                    x *= -limit / z;
                    z = -limit;
                }
            }
            return new Vec3d(x, 900.0, z);
        }

        return null;
    }

    @Override
    public Vec3d getEntitySpawnLocation(ServerWorld world, Entity entity)
    {
        return new Vec3d(entity.getPosX(), 900.0, entity.getPosZ());
    }

    @Override
    public Vec3d getParaChestSpawnLocation(ServerWorld world, ServerPlayerEntity player, Random rand)
    {
        return null;
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, ServerPlayerEntity player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.abilities.isFlying)
                {
                    player.abilities.isFlying = false;
                }

                if (!newWorld.isRemote)
                {
                    EntityEntryPodVenus entryPod = EntityEntryPodVenus.createEntityEntryPodVenus(player);

                    boolean previous = CompatibilityManager.forceLoadChunks((ServerWorld) newWorld);
                    entryPod.forceSpawn = true;
                    newWorld.addEntity(entryPod);
                    CompatibilityManager.forceLoadChunksEnd((ServerWorld) newWorld, previous);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayerEntity player)
    {
        // TODO Auto-generated method stub

    }
}
