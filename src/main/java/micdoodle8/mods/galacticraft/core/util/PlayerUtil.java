package micdoodle8.mods.galacticraft.core.util;

import java.util.Iterator;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLLog;

/**
 * PlayerUtil.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class PlayerUtil
{
	public static GCCorePlayerMP getPlayerBaseServerFromPlayerUsername(String username)
	{
		if (GalacticraftCore.playersServer.isEmpty())
		{
			// new EmptyStackException().printStackTrace();
		}

		final Iterator<Map.Entry<String, GCCorePlayerMP>> it = GalacticraftCore.playersServer.entrySet().iterator();

		while (it.hasNext())
		{
			Map.Entry<String, GCCorePlayerMP> entry = it.next();

			if (entry.getKey().equals(username))
			{
				return entry.getValue();
			}
		}

		GCLog.severe("Warning: Could not find player base server instance for player " + username);

		return null;
	}

	public static GCCorePlayerMP getPlayerBaseServerFromPlayer(EntityPlayer player)
	{
		if (player == null)
		{
			return null;
		}

		if (GalacticraftCore.playersServer.isEmpty())
		{
			// new EmptyStackException().printStackTrace();
		}

		final Iterator<Map.Entry<String, GCCorePlayerMP>> it = GalacticraftCore.playersServer.entrySet().iterator();

		while (it.hasNext())
		{
			final Map.Entry<String, GCCorePlayerMP> entry = it.next();

			if (entry.getKey().equals(player.getGameProfile().getName()))
			{
				return entry.getValue();
			}
		}

		GCLog.severe("Warning: Could not find player base server instance for player " + player.getGameProfile().getName());

		return null;
	}

	public static GCCorePlayerSP getPlayerBaseClientFromPlayer(EntityPlayer player)
	{
		if (player == null)
		{
			return null;
		}

		if (GalacticraftCore.playersClient.isEmpty())
		{
			return null;
		}

		final Iterator<Map.Entry<String, GCCorePlayerSP>> it = GalacticraftCore.playersClient.entrySet().iterator();

		while (it.hasNext())
		{
			final Map.Entry<String, GCCorePlayerSP> entry = it.next();

			if (entry.getKey() != null && entry.getKey().equals(player.getGameProfile().getName()))
			{
				return entry.getValue();
			}
		}

		GCLog.severe("Warning: Could not find player base client instance for player " + player.getGameProfile().getName());

		return null;
	}
}
