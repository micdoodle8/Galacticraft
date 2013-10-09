package gregtechmod.api.util;

import net.minecraft.item.ItemStack;

/**
 * Used in the Universal Macerator
 */
public class GT_PulverizerRecipe {
	private final ItemStack mInput, mOutput1, mOutput2;
	private final int mChance;
	
	public GT_PulverizerRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aChance) {
		mInput = aInput;
		mOutput1 = aOutput1;
		mOutput2 = aOutput2;
		mChance = aChance;
	}
	
	public ItemStack getInput() {
		return mInput;
	}
	
	public ItemStack getPrimaryOutput() {
		return mOutput1;
	}
	
	public ItemStack getSecondaryOutput() {
		return mOutput2;
	}
	
	public int getSecondaryOutputChance() {
		return mChance;
	}
	
	public int getEnergy() {
		return 400;
	}

}
