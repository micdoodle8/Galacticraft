package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class TeleportTypeMoon implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return ConfigManagerCore.disableLander;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
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
                    x *= - limit / z;
                    z = -limit;
                }
            }
            return new Vector3(x, ConfigManagerCore.disableLander ? 250.0 : 900.0, z);
        }

        return null;
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
    {
        return new Vector3(entity.posX, ConfigManagerCore.disableLander ? 250.0 : 900.0, entity.posZ);
    }

    @Override
    public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand)
    {
        if (ConfigManagerCore.disableLander)
        {
            final double x = (rand.nextDouble() * 2 - 1.0D) * 4.0D;
            final double z = (rand.nextDouble() * 2 - 1.0D) * 4.0D;
            return new Vector3(player.posX + x, 220.0D, player.posZ + z);
        }

        return null;
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        if (!ridingAutoRocket && !ConfigManagerCore.disableLander && stats.getTeleportCooldown() <= 0)
        {
            if (player.capabilities.isFlying)
            {
                player.capabilities.isFlying = false;
            }

            EntityLander lander = new EntityLander(player);
            lander.setPosition(player.posX, player.posY, player.posZ);

            if (!newWorld.isRemote)
            {
                lander.forceSpawn = true;
                newWorld.spawnEntityInWorld(lander);
            }

            stats.setTeleportCooldown(10);
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub

    }
}
