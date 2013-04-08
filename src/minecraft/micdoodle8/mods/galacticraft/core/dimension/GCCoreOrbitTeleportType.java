package micdoodle8.mods.galacticraft.core.dimension;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import universalelectricity.core.vector.Vector3;
import micdoodle8.mods.galacticraft.API.ITeleportType;

public class GCCoreOrbitTeleportType implements ITeleportType
{
	@Override
	public boolean useParachute() 
	{
		return false;
	}

	@Override
	public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player) 
	{
		return new Vector3(0.5, 65.0, 0.5);
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity player)
	{
		return new Vector3(0.5, 65.0, 0.5);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, Entity chest, EntityPlayerMP player, Random rand) 
	{
		return new Vector3(-8.5D, 90.0, -1.5D);
	}
}
