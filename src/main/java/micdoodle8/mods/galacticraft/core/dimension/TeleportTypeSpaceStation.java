package micdoodle8.mods.galacticraft.core.dimension;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * GCCoreOrbitTeleportType.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TeleportTypeSpaceStation implements ITeleportType
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
	public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand)
	{
		return new Vector3(-8.5D, 90.0, -1.5D);
	}

	@Override
	public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
	{
		if (ConfigManagerCore.spaceStationsRequirePermission && !newWorld.isRemote)
		{
			player.addChatMessage(new ChatComponentText(EnumColor.YELLOW + "Type " + EnumColor.AQUA + "/ssinvite <playername> " + EnumColor.YELLOW + "to allow another player to enter this space station!"));
		}
	}
}
