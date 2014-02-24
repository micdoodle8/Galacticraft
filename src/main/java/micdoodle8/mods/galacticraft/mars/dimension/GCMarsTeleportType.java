package micdoodle8.mods.galacticraft.mars.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCConfigManager;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityLandingBalloons;
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
public class GCMarsTeleportType implements ITeleportType
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
			return new Vector3(((GCEntityPlayerMP) player).getCoordsTeleportedFromX(), GCConfigManager.disableLander ? 250.0 : 900.0, ((GCEntityPlayerMP) player).getCoordsTeleportedFromZ());
		}

		return null;
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
	{
		return new Vector3(entity.posX, GCConfigManager.disableLander ? 250.0 : 900.0, entity.posZ);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand)
	{
		return null;
	}

	@Override
	public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
	{
		if (!ridingAutoRocket && player instanceof GCEntityPlayerMP && ((GCEntityPlayerMP) player).getTeleportCooldown() <= 0)
		{
			final GCEntityPlayerMP gcPlayer = (GCEntityPlayerMP) player;

			if (gcPlayer.capabilities.isFlying)
			{
				gcPlayer.capabilities.isFlying = false;
			}

			GCMarsEntityLandingBalloons lander = new GCMarsEntityLandingBalloons(gcPlayer);
			lander.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

			if (!newWorld.isRemote)
			{
				newWorld.spawnEntityInWorld(lander);
			}

			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.C_ZOOM_CAMERA, new Object[] { 1 }));

			gcPlayer.setTeleportCooldown(10);
		}
	}
}
