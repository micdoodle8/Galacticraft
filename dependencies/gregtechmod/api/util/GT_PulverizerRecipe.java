package gregtechmod.api.util;

import net.minecraft.item.ItemStack;

/**
 * Used in the Universal Macerator
 */
public class GT_PulverizerRecipe {
	private final ItemStack mInput, mOutput1, mOutput2;
	private final int mChance;
	
	public GT_PulverizerRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aChance) {
		mInput = GT_Utility.copy(aInput);
		mOutput1 = GT_Utility.copy(aOutput1);
		mOutput2 = GT_Utility.copy(aOutput2);
		mChance = aChance;
	}
	
	public ItemStack getInput() {
		return GT_Utility.copy(mInput);
	}
	
	public ItemStack getPrimaryOutput() {
		return GT_Utility.copy(mOutput1);
	}
	
	public ItemStack getSecondaryOutput() {
		return GT_Utility.copy(mOutput2);
	}
	
	public int getSecondaryOutputChance() {
		return mChance;
	}
	
	public int getEnergy() {
		return 400;
	}
}
