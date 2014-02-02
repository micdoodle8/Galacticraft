package micdoodle8.mods.galacticraft.moon.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
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
public class GCMoonTeleportType implements ITeleportType
{
	@Override
	public boolean useParachute()
	{
		return GCCoreConfigManager.disableLander;
	}

	@Override
	public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
	{
		if (player instanceof GCCorePlayerMP)
		{
			return new Vector3(((GCCorePlayerMP) player).getCoordsTeleportedFromX(), GCCoreConfigManager.disableLander ? 250.0 : 900.0, ((GCCorePlayerMP) player).getCoordsTeleportedFromZ());
		}

		return null;
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
	{
		return new Vector3(entity.posX, GCCoreConfigManager.disableLander ? 250.0 : 900.0, entity.posZ);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand)
	{
		if (GCCoreConfigManager.disableLander)
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
		if (!ridingAutoRocket && !GCCoreConfigManager.disableLander && player instanceof GCCorePlayerMP && ((GCCorePlayerMP) player).getTeleportCooldown() <= 0)
		{
			final GCCorePlayerMP gcPlayer = (GCCorePlayerMP) player;

			if (gcPlayer.capabilities.isFlying)
			{
				gcPlayer.capabilities.isFlying = false;
			}

			GCCoreEntityLander lander = new GCCoreEntityLander(gcPlayer);
			lander.setWaitForPlayer(true);
			lander.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

			lander.riddenByEntity = player;
			player.ridingEntity = lander;

			if (!newWorld.isRemote)
			{
				newWorld.spawnEntityInWorld(lander);
			}

			final Object[] toSend2 = { 1 };
			gcPlayer.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.ZOOM_CAMERA, toSend2));

			gcPlayer.setTeleportCooldown(10);
		}
	}
}
