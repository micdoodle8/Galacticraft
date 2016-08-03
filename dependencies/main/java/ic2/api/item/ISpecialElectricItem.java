package ic2.api.item;

import net.minecraft.item.ItemStack;

public interface ISpecialElectricItem extends IElectricItem {
	/**
	 * Supply a custom IElectricItemManager.
	 * 
	 * @param stack ItemStack to get the manager for
	 * @return IElectricItemManager instance
	 */
	IElectricItemManager getManager(ItemStack stack);
}
