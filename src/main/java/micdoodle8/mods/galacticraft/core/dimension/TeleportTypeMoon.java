package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TeleportTypeMoon implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return ConfigManagerCore.disableLander;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerWorld world, ServerPlayerEntity player)
    {
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            double x = stats.getCoordsTeleportedFromX();
            double z = stats.getCoordsTeleportedFromZ();
            int limit = ConfigManagerCore.otherPlanetWorldBorders - 2;
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
            return new Vector3D(x, ConfigManagerCore.disableLander ? 250.0 : 900.0, z);
        }

        return null;
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerWorld world, Entity entity)
    {
        return new Vector3D(entity.getPosX(), ConfigManagerCore.disableLander ? 250.0 : 900.0, entity.getPosZ());
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerWorld world, ServerPlayerEntity player, Random rand)
    {
        if (ConfigManagerCore.disableLander)
        {
            final float x = (rand.nextFloat() * 2 - 1.0F) * 4.0F;
            final float z = (rand.nextFloat() * 2 - 1.0F) * 4.0F;
            return new Vector3D(player.getPosX() + x, 220.0, player.getPosZ() + z);
        }

        return null;
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, ServerPlayerEntity player, boolean ridingAutoRocket)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        if (!ridingAutoRocket && !ConfigManagerCore.disableLander && stats.getTeleportCooldown() <= 0)
        {
            if (player.abilities.isFlying)
            {
                player.abilities.isFlying = false;
            }

            EntityLander lander = new EntityLander(player);
            lander.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());

            if (!newWorld.isRemote)
            {
                boolean previous = CompatibilityManager.forceLoadChunks((ServerWorld) newWorld);
                lander.forceSpawn = true;
                newWorld.addEntity(lander);
                lander.setWorld(newWorld);
//                newWorld.updateEntityWithOptionalForce(lander, true);
                ((ServerWorld) newWorld).chunkCheck(lander);
                player.startRiding(lander);
                CompatibilityManager.forceLoadChunksEnd((ServerWorld) newWorld, previous);
                GCLog.debug("Entering lander at : " + player.getPosX() + "," + player.getPosZ() + " lander spawn at: " + lander.getPosX() + "," + lander.getPosZ());
            }

            stats.setTeleportCooldown(10);
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayerEntity player)
    {
        // TODO Auto-generated method stub

    }
}
