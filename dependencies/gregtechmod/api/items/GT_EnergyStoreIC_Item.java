package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;

public class GT_EnergyStoreIC_Item extends GT_EnergyStore_Item implements IElectricItem {
	public GT_EnergyStoreIC_Item(int aID, String aUnlocalized, String aEnglish, int aCharge, int aTransfer, int aTier, int aEmptyID, int aFullID) {
		super(aID, aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aEmptyID, aFullID);
	}
	
	@Override
	public boolean canProvideEnergy(ItemStack aStack) {
		return true;
	}
	
	@Override
	public int getChargedItemId(ItemStack aStack) {
		return GregTech_API.sItemList[mFullID].itemID;
	}
	
	@Override
	public int getEmptyItemId(ItemStack aStack) {
		return GregTech_API.sItemList[mEmptyID].itemID;
	}
	
	@Override
	public int getMaxCharge(ItemStack aStack) {
		return mCharge;
	}
	
	@Override
	public int getTransferLimit(ItemStack aStack) {
		return mTransfer;
	}
}