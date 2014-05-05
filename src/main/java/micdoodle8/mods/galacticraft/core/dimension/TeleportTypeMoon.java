package micdoodle8.mods.galacticraft.core.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * GCMoonTeleportType.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
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
		if (player instanceof GCEntityPlayerMP)
		{
			return new Vector3(((GCEntityPlayerMP) player).getCoordsTeleportedFromX(), ConfigManagerCore.disableLander ? 250.0 : 900.0, ((GCEntityPlayerMP) player).getCoordsTeleportedFromZ());
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
			final double x = (rand.nextDouble() * 2 - 1.0D) * 5.0D;
			final double z = (rand.nextDouble() * 2 - 1.0D) * 5.0D;
			return new Vector3(x, 220.0D, z);
		}

		return null;
	}

	@Override
	public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
	{
		if (!ridingAutoRocket && !ConfigManagerCore.disableLander && player instanceof GCEntityPlayerMP && ((GCEntityPlayerMP) player).getTeleportCooldown() <= 0)
		{
			final GCEntityPlayerMP gcPlayer = (GCEntityPlayerMP) player;

			if (gcPlayer.capabilities.isFlying)
			{
				gcPlayer.capabilities.isFlying = false;
			}

			EntityLander lander = new EntityLander(gcPlayer);
			lander.setWaitForPlayer(true);
			lander.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

			lander.riddenByEntity = player;
			player.ridingEntity = lander;

			if (!newWorld.isRemote)
			{
				newWorld.spawnEntityInWorld(lander);
			}

			gcPlayer.setTeleportCooldown(10);
		}
	}
}
