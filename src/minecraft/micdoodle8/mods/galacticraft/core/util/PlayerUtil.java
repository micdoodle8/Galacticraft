package micdoodle8.mods.galacticraft.core.util;

import java.util.Iterator;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtil
{
	public static GCCorePlayerMP getPlayerBaseServerFromPlayerUsername(String username)
	{
		if (GalacticraftCore.playersServer.size() == 0)
		{
//			new EmptyStackException().printStackTrace();
		}

	    final Iterator it = GalacticraftCore.playersServer.entrySet().iterator();

	    while (it.hasNext())
	    {
	        final Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey().equals(username))
	        {
	        	return (GCCorePlayerMP) entry.getValue();
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

		if (GalacticraftCore.playersServer.size() == 0)
		{
//			new EmptyStackException().printStackTrace();
		}

	    final Iterator it = GalacticraftCore.playersServer.entrySet().iterator();

	    while (it.hasNext())
	    {
	        final Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey().equals(player.username))
	        {
	        	return (GCCorePlayerMP) entry.getValue();
	        }
	    }

	    GCLog.severe("Warning: Could not find player base server instance for player " + player.username);

        return null;
	}

	public static GCCorePlayerSP getPlayerBaseClientFromPlayer(EntityPlayer player)
	{
		if (player == null)
		{
			return null;
		}

		if (GalacticraftCore.playersClient.size() == 0)
		{
			return null;
		}

	    final Iterator it = GalacticraftCore.playersClient.entrySet().iterator();

	    while (it.hasNext())
	    {
	        final Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey() != null && entry.getKey().equals(player.username))
	        {
	        	return (GCCorePlayerSP) entry.getValue();
	        }
	    }

	    GCLog.severe("Warning: Could not find player base client instance for player " + player.username);

        return null;
	}
}
