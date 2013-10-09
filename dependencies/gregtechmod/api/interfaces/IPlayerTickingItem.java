package gregtechmod.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface IPlayerTickingItem {
	/**
	 * @param aPlayer The Player, who is having the Item in Inventory or is wearing it.
	 * @param aStack The Item.
	 * @param aTimer Amount of Ticks since Server start.
	 * @param aIsArmor just a simple boolean indicating, if this is worn as Armor or not, so that you don't have to scan the Armor-Inventory for it.
	 */
	boolean onTick(EntityPlayer aPlayer, ItemStack aStack, int aTimer, boolean aIsArmor);
}