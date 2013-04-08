package micdoodle8.mods.galacticraft.core.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import universalelectricity.core.vector.Vector3;

public class GCCoreOverworldTeleportType implements ITeleportType
{
	@Override
	public boolean useParachute() 
	{
		return true;
	}

	@Override
	public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player) 
	{
		if (player instanceof GCCorePlayerMP)
		{
			return new Vector3(((GCCorePlayerMP) player).coordsTeleportedFromX, 250.0, ((GCCorePlayerMP) player).coordsTeleportedFromZ);
		}
		
		return null;
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity) 
	{
		return new Vector3(entity.posX, 250.0, entity.posZ);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, Entity chest, EntityPlayerMP player, Random rand)
	{
      	double x = ((rand.nextDouble() * 2) - 1.0D) * 5.0D;
      	double z = ((rand.nextDouble() * 2) - 1.0D) * 5.0D;
      	
		return new Vector3(player.posX + x, 230.0D, player.posZ + z);
	}
}
