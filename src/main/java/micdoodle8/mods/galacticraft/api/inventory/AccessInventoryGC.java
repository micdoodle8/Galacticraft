package micdoodle8.mods.galacticraft.api.inventory;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * A static method for other mods to access the Galacticraft
 * extended inventory.
 * 
 * Call: AccessInventoryGC.getGCInventoryForPlayer(player)
 */
public class AccessInventoryGC
{
	public static IInventoryGC getGCInventoryForPlayer(EntityPlayerMP player)
	{
		GCPlayerStats stats = GCPlayerStats.get(player);
		if (stats == null) return null;
		return stats.extendedInventory;
	}
}
