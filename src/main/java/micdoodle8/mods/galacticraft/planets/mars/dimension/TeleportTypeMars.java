package micdoodle8.mods.galacticraft.planets.mars.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * GCMarsTeleportType.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
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
		if (player instanceof GCEntityPlayerMP)
		{
			return new Vector3(((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromX, ConfigManagerCore.disableLander ? 250.0 : 900.0, ((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromZ);
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
		if (!ridingAutoRocket && player instanceof GCEntityPlayerMP && ((GCEntityPlayerMP) player).getPlayerStats().teleportCooldown <= 0)
		{
			final GCEntityPlayerMP gcPlayer = (GCEntityPlayerMP) player;

			if (gcPlayer.capabilities.isFlying)
			{
				gcPlayer.capabilities.isFlying = false;
			}

			EntityLandingBalloons lander = new EntityLandingBalloons(gcPlayer);
			lander.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

			if (!newWorld.isRemote)
			{
				newWorld.spawnEntityInWorld(lander);
			}

			gcPlayer.getPlayerStats().teleportCooldown = 10;
		}
	}
}
