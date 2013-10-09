package gregtechmod.api.interfaces;

import net.minecraft.item.ItemStack;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface ICapsuleCellContainer {
	/**
	 * Get the amount of Tin Cells
	 * @param aStack the ItemStack
	 * @return the amount of Tin Cells per SINGLE Item in the Stack.
	 * A return value for a simple Cell, like IC2s Water Cell would be 1
	 */
	public int CapsuleCellContainerCount(ItemStack aStack);
}
