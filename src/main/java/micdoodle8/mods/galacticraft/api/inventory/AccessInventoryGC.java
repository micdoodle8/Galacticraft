package micdoodle8.mods.galacticraft.api.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

import java.lang.reflect.Method;

/**
 * A static method for other mods to access the Galacticraft
 * extended inventory.
 * 
 * Call: AccessInventoryGC.getGCInventoryForPlayer(player)
 */
public class AccessInventoryGC
{
	private static Class<?> playerStatsClass;
	private static Method getStats;
	private static Method getExtendedInventory;

	public static IInventoryGC getGCInventoryForPlayer(EntityPlayerMP player)
	{
		try
		{
			if (playerStatsClass == null || getStats == null || getExtendedInventory == null)
			{
				playerStatsClass = Class.forName("micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats");
				getStats = playerStatsClass.getMethod("get", Entity.class);
				getExtendedInventory = playerStatsClass.getMethod("getExtendedInventory");
			}

			Object stats = getStats.invoke(null, player);
			if (stats == null)
			{
				return null;
			}
			return (IInventoryGC)getExtendedInventory.invoke(stats);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
