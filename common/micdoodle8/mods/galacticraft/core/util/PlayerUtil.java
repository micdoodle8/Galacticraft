package micdoodle8.mods.galacticraft.core.util;

import java.util.Iterator;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import net.minecraft.entity.player.EntityPlayer;

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
	public static GCCorePlayerMP getPlayerBaseServerFromPlayerUsername(String username, boolean ignoreCase)
	{
		GCCorePlayerMP thePlayer = GalacticraftCore.playersServer.get(username);
		if (thePlayer != null)
		{
			return thePlayer;
		}

		if (ignoreCase)
		{
			final Iterator<Map.Entry<String, GCCorePlayerMP>> it = GalacticraftCore.playersServer.entrySet().iterator();

			while (it.hasNext())
			{
				Map.Entry<String, GCCorePlayerMP> entry = it.next();

				if (entry.getKey().equalsIgnoreCase(username))
				{
					return entry.getValue();
				}
			}
		}

		GCLog.severe("Warning: Could not find player base server instance for player " + username);

		return null;
	}

	public static GCCorePlayerMP getPlayerBaseServerFromPlayer(EntityPlayer player, boolean ignoreCase)
	{
		if (player == null)
		{
			return null;
		}

		GCCorePlayerMP thePlayer = GalacticraftCore.playersServer.get(player.username);
		if (thePlayer != null)
		{
			return thePlayer;
		}

		if (ignoreCase)
		{
			final Iterator<Map.Entry<String, GCCorePlayerMP>> it = GalacticraftCore.playersServer.entrySet().iterator();

			while (it.hasNext())
			{
				Map.Entry<String, GCCorePlayerMP> entry = it.next();

				if (entry.getKey().equalsIgnoreCase(player.username))
				{
					return entry.getValue();
				}
			}
		}

		GCLog.severe("Warning: Could not find player base server instance for player " + player.username);

		return null;
	}

	public static GCCorePlayerSP getPlayerBaseClientFromPlayer(EntityPlayer player, boolean ignoreCase)
	{
		if (player == null)
		{
			return null;
		}

		if (GalacticraftCore.playersClient.isEmpty())
		{
			return null;
		}

		GCCorePlayerSP thePlayer = GalacticraftCore.playersClient.get(player.username);
		if (thePlayer != null)
		{
			return thePlayer;
		}

		if (ignoreCase)
		{
			final Iterator<Map.Entry<String, GCCorePlayerSP>> it = GalacticraftCore.playersClient.entrySet().iterator();

			while (it.hasNext())
			{
				Map.Entry<String, GCCorePlayerSP> entry = it.next();

				if (entry.getKey().equalsIgnoreCase(player.username))
				{
					return entry.getValue();
				}
			}
		}

		GCLog.severe("Warning: Could not find player base client instance for player " + player.username);

		return null;
	}
}
