package micdoodle8.mods.galacticraft.core.util;

import java.util.Iterator;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLLog;

public class PlayerUtil
{
	public static GCCorePlayerBase getPlayerBaseServerFromPlayerUsername(String username)
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
	        	return (GCCorePlayerBase) entry.getValue();
	        }
	    }

	    FMLLog.severe("Warning: Could not find player base server instance for player " + username);

        return null;
	}

	public static GCCorePlayerBase getPlayerBaseServerFromPlayer(EntityPlayer player)
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
	        	return (GCCorePlayerBase) entry.getValue();
	        }
	    }

	    FMLLog.severe("Warning: Could not find player base server instance for player " + player.username);

        return null;
	}

	public static GCCorePlayerBaseClient getPlayerBaseClientFromPlayer(EntityPlayer player)
	{
		if (player == null)
		{
			return null;
		}

		if (GalacticraftCore.playersClient.size() == 0)
		{
//			new EmptyStackException().printStackTrace();
		}

	    final Iterator it = GalacticraftCore.playersClient.entrySet().iterator();

	    while (it.hasNext())
	    {
	        final Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey().equals(player.username))
	        {
	        	return (GCCorePlayerBaseClient) entry.getValue();
	        }
	    }

	    FMLLog.severe("Warning: Could not find player base client instance for player " + player.username);

        return null;
	}
}
