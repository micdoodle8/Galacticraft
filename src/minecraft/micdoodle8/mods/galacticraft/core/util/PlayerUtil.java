package micdoodle8.mods.galacticraft.core.util;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtil 
{
	
	public static GCCorePlayerBase getPlayerBaseServerFromPlayer(EntityPlayer player)
	{
		if (player == null)
		{
			return null;
		}
		
		if (GalacticraftCore.playersServer.size() == 0)
		{
			new EmptyStackException().printStackTrace();
		}
		
	    Iterator it = GalacticraftCore.playersServer.entrySet().iterator();
	    
	    while (it.hasNext()) 
	    {
	        Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey().equals(player.username))
	        {
	        	return (GCCorePlayerBase) entry.getValue();
	        }
	    }
        
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
			new EmptyStackException().printStackTrace();
		}
		
	    Iterator it = GalacticraftCore.playersClient.entrySet().iterator();
	    
	    while (it.hasNext()) 
	    {
	        Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey().equals(player.username))
	        {
	        	return (GCCorePlayerBaseClient) entry.getValue();
	        }
	    }
        
        return null;
	}
}
