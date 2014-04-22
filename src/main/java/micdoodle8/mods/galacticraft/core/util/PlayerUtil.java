package micdoodle8.mods.galacticraft.core.util;

import java.util.Iterator;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
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
	public static GCEntityPlayerMP getPlayerBaseServerFromPlayerUsername(String username, boolean ignoreCase)
	{
		GCEntityPlayerMP thePlayer = GalacticraftCore.playersServer.get(username);
		if (thePlayer != null)
		{
			return thePlayer;
		}

		if (ignoreCase)
		{
			final Iterator<Map.Entry<String, GCEntityPlayerMP>> it = GalacticraftCore.playersServer.entrySet().iterator();

			while (it.hasNext())
			{
				Map.Entry<String, GCEntityPlayerMP> entry = it.next();

				if (entry.getKey().equalsIgnoreCase(username))
				{
					return entry.getValue();
				}
			}
		}

		GCLog.severe("Warning: Could not find player base server instance for player " + username);

		return null;
	}

	public static GCEntityPlayerMP getPlayerBaseServerFromPlayer(EntityPlayer player, boolean ignoreCase)
	{
		if (player == null)
		{
			return null;
		}

		GCEntityPlayerMP thePlayer = GalacticraftCore.playersServer.get(player.getGameProfile().getName());
		if (thePlayer != null)
		{
			return thePlayer;
		}

		if (ignoreCase)
		{
			final Iterator<Map.Entry<String, GCEntityPlayerMP>> it = GalacticraftCore.playersServer.entrySet().iterator();

			while (it.hasNext())
			{
				Map.Entry<String, GCEntityPlayerMP> entry = it.next();

				if (entry.getKey().equalsIgnoreCase(player.getGameProfile().getName()))
				{
					return entry.getValue();
				}
			}
		}

		GCLog.severe("Warning: Could not find player base server instance for player " + player.getGameProfile().getName());

		return null;
	}

	public static GCEntityClientPlayerMP getPlayerBaseClientFromPlayer(EntityPlayer player, boolean ignoreCase)
	{
		if (player == null)
		{
			return null;
		}

		if (GalacticraftCore.playersClient.isEmpty())
		{
			return null;
		}

		GCEntityClientPlayerMP thePlayer = GalacticraftCore.playersClient.get(player.getGameProfile().getName());
		if (thePlayer != null)
		{
			return thePlayer;
		}

		if (ignoreCase)
		{
			final Iterator<Map.Entry<String, GCEntityClientPlayerMP>> it = GalacticraftCore.playersClient.entrySet().iterator();

			while (it.hasNext())
			{
				Map.Entry<String, GCEntityClientPlayerMP> entry = it.next();

				if (entry.getKey().equalsIgnoreCase(player.getGameProfile().getName()))
				{
					return entry.getValue();
				}
			}
		}

		GCLog.severe("Warning: Could not find player base client instance for player " + player.getGameProfile().getName());

		return null;
	}
}
