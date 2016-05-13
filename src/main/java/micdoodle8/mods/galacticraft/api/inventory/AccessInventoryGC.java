package micdoodle8.mods.galacticraft.api.inventory;

import net.minecraft.entity.player.EntityPlayerMP;

import java.lang.reflect.Field;
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
	private static Method getMethod;
	private static Field extendedInventoryField;

	public static IInventoryGC getGCInventoryForPlayer(EntityPlayerMP player)
	{
		try
		{
			if (playerStatsClass == null || getMethod == null || extendedInventoryField == null)
			{
				playerStatsClass = Class.forName("micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats");
				getMethod = playerStatsClass.getMethod("get", EntityPlayerMP.class);
				extendedInventoryField = playerStatsClass.getField("extendedInventory");
			}

			Object stats = getMethod.invoke(null, player);
			if (stats == null)
			{
				return null;
			}
			return (IInventoryGC)extendedInventoryField.get(stats);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
