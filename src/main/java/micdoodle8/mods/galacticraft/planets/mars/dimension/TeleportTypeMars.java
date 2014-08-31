package micdoodle8.mods.galacticraft.planets.mars.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
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
            GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(player);
			return new Vector3(stats.coordsTeleportedFromX, ConfigManagerCore.disableLander ? 250.0 : 900.0, stats.coordsTeleportedFromZ);
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
		if (!ridingAutoRocket && player != null && GCEntityPlayerMP.getPlayerStats(player).teleportCooldown <= 0)
		{
			final EntityPlayerMP gcPlayer = (EntityPlayerMP) player;

			if (gcPlayer.capabilities.isFlying)
			{
				gcPlayer.capabilities.isFlying = false;
			}

			EntityLandingBalloons lander = new EntityLandingBalloons(gcPlayer);

			if (!newWorld.isRemote)
			{
				newWorld.spawnEntityInWorld(lander);
			}

            GCEntityPlayerMP.getPlayerStats(player).teleportCooldown = 10;
		}
	}
}
