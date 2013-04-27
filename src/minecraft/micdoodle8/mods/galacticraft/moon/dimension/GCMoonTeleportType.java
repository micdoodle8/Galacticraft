package micdoodle8.mods.galacticraft.moon.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import universalelectricity.core.vector.Vector3;

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
			return new Vector3(((GCCorePlayerMP) player).coordsTeleportedFromX, GCCoreConfigManager.disableLander ? 250.0 : 900.0, ((GCCorePlayerMP) player).coordsTeleportedFromZ);
		}

		return null;
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
	{
		return new Vector3(entity.posX, GCCoreConfigManager.disableLander ? 250.0 : 900.0, entity.posZ);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, Entity chest, EntityPlayerMP player, Random rand)
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
	public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player) 
	{
		if (!GCCoreConfigManager.disableLander && player instanceof GCCorePlayerMP && ((GCCorePlayerMP) player).teleportCooldown <= 0)
		{
			GCCorePlayerMP gcPlayer = (GCCorePlayerMP) player;
			
			if (gcPlayer.capabilities.isFlying)
			{
				gcPlayer.capabilities.isFlying = false;
			}
			
			GCCoreEntityLander lander = new GCCoreEntityLander(gcPlayer);
			lander.setPositionAndRotation(player.posX, player.posY + 3, player.posZ, 0, 0);
			
			if (!newWorld.isRemote)
			{
				newWorld.spawnEntityInWorld(lander);
			}
			
    	  	final Object[] toSend2 = {1};
    	  	gcPlayer.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));
			
			gcPlayer.teleportCooldown = 10;
		}
	}
}
