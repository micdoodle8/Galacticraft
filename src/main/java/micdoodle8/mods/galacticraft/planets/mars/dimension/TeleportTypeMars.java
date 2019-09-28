package micdoodle8.mods.galacticraft.planets.mars.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class TeleportTypeMars implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
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
        return null;
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.capabilities.isFlying)
                {
                    player.capabilities.isFlying = false;
                }

                EntityLandingBalloons lander = new EntityLandingBalloons(player);

                if (!newWorld.isRemote)
                {
                    boolean previous = CompatibilityManager.forceLoadChunks((WorldServer) newWorld);
                    lander.forceSpawn = true;
                    newWorld.spawnEntity(lander);
                    CompatibilityManager.forceLoadChunksEnd((WorldServer) newWorld, previous);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub

    }
}
