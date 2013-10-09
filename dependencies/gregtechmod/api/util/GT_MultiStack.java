package gregtechmod.api.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

/**
 * This Class is meant to be used for Recipes in the future.
 * Not suitable to store real ingame ItemStacks.
 * toItemStack is just for things like the NEI-Plugins
 */
public class GT_MultiStack {
	public final ItemStack mStack;
	public final String mOreName;
	public final ArrayList<Integer> mStackHashList;
	public final ArrayList<ItemStack> mStackList;
	public final ArrayList<String> mOreList;
	
	public GT_MultiStack(ItemStack aStack) {
		this(aStack, "");
	}
	
	public GT_MultiStack(String aOreName) {
		this(GT_OreDictUnificator.getFirstOre(aOreName, 1), aOreName);
	}
	
	public GT_MultiStack(ArrayList<String> aOreList) {
		this(GT_OreDictUnificator.getFirstOre(aOreList.get(0), 1), aOreList.get(0), aOreList, null);
	}
	
	public GT_MultiStack(ItemStack aStack, ArrayList<ItemStack> aStackList) {
		this(aStack==null?aStackList.get(0):aStack, "", null, aStackList);
	}
	
	public GT_MultiStack(ItemStack aStack, String aOreName) {
		this(aStack==null?GT_OreDictUnificator.getFirstOre(aOreName, 1):aStack, aOreName, null, null);
	}
	
	public GT_MultiStack(ItemStack aStack, String aOreName, ArrayList<String> aOreList, ArrayList<ItemStack> aStackList) {
		mStack = aStack;
		mOreName = aOreName;
		mOreList = aOreList;
		mStackList = aStackList;
		
		if (mStackList == null) {
			mStackHashList = null;
		} else {
			mStackHashList = new ArrayList<Integer>();
			for (ItemStack tStack : mStackList) {
				mStackHashList.add(GT_Utility.stackToInt(tStack));
			}
		}
	}
	
	public ItemStack toItemStack() {
		return mStack.copy();
	}
	
	public boolean isItemEqual(ItemStack aStack) {
		if (GT_Utility.areStacksEqual(mStack, aStack)) return true;
		if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, mOreName, false)) return true;
		if (mStackHashList != null && GT_Utility.isItemStackInList(aStack, mStackHashList)) return true;
		if (mOreList != null) for (String tOreName : mOreList) {
			if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, tOreName, false)) return true;
		}
		return false;
	}
}