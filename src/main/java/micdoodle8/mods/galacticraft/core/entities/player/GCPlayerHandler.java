package micdoodle8.mods.galacticraft.core.entities.player;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraftforge.event.entity.EntityEvent;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class GCPlayerHandler
{
	private ConcurrentHashMap<UUID, GCPlayerStats> playerStatsMap = new ConcurrentHashMap<UUID, GCPlayerStats>();
	
	public ConcurrentHashMap<UUID, GCPlayerStats> getServerStatList()
	{
		return playerStatsMap;
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
    	if (event.player instanceof GCEntityPlayerMP)
    	{
    		onPlayerLogin((GCEntityPlayerMP) event.player);
    	}
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
    	if (event.player instanceof GCEntityPlayerMP)
    	{
    		onPlayerLogout((GCEntityPlayerMP) event.player);
    	}
	}

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
    	if (event.player instanceof GCEntityPlayerMP)
    	{
            onPlayerRespawn((GCEntityPlayerMP) event.player);
    	}
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
    	FMLLog.info("CONSTRUCTION OF " + event.entity.getClass());
        if (event.entity instanceof GCEntityPlayerMP && GCPlayerStats.get((GCEntityPlayerMP) event.entity) == null)
        {
            GCPlayerStats.register((GCEntityPlayerMP) event.entity);
        }
    }
	
	private void onPlayerLogin(GCEntityPlayerMP player)
	{
		GCPlayerStats oldData = playerStatsMap.remove(player.getPersistentID());
		if (oldData != null)
		{
			oldData.saveNBTData(player.getEntityData());
		}
		
		GCPlayerStats stats = GCPlayerStats.get(player);
	}
	
	private void onPlayerLogout(GCEntityPlayerMP player)
	{
		
	}
	
	private void onPlayerRespawn(GCEntityPlayerMP player)
	{
		GCPlayerStats oldData = playerStatsMap.remove(player.getPersistentID());
		GCPlayerStats stats = GCPlayerStats.get(player);
		
		if (oldData != null)
		{
			stats.copyFrom(oldData, false);
		}
		
		stats.player = new WeakReference<GCEntityPlayerMP>(player);
	}
}
