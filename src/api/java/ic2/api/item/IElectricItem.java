package ic2.api.item;

import net.minecraft.item.ItemStack;

/**
 * Provides the ability to store energy on the implementing item.
 *
 * The item should have a maximum damage of 13.
 */
public interface IElectricItem {
	/**
	 * Determine if the item can be used in a machine or as an armor part to supply energy.
	 *
	 * @return Whether the item can supply energy
	 */
	boolean canProvideEnergy(ItemStack stack);

	/**
	 * Get the item's maximum charge energy in EU.
	 *
	 * @return Maximum charge energy
	 */
	double getMaxCharge(ItemStack stack);

	/**
	 * Get the item's tier, lower tiers can't send energy to higher ones.
	 *
	 * Batteries are Tier 1, Advanced Batteries are Tier 2, Energy Crystals are Tier 3, Lapotron
	 * Crystals are Tier 4.
	 *
	 * @return Item's tier
	 */
	int getTier(ItemStack stack);

	/**
	 * Get the item's transfer limit in EU per transfer operation.
	 *
	 * @return Transfer limit
	 */
	double getTransferLimit(ItemStack stack);
}

