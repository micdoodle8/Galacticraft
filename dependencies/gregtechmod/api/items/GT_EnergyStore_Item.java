package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_ModHandler;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GT_EnergyStore_Item extends GT_Generic_Item {
	public int mCharge, mTransfer, mTier, mEmptyID, mFullID;
	
	public GT_EnergyStore_Item(int aID, String aName, int aCharge, int aTransfer, int aTier, int aEmptyID, int aFullID) {
		super(aID, aName, null);
		setMaxStackSize(1);
		setMaxDamage(100);
		setNoRepair();
		mCharge = aCharge;
		mTransfer = aTransfer;
		mTier = aTier;
		mEmptyID = aEmptyID;
		mFullID = aFullID;
	}
    
    public boolean getShareTag() {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(int var1, CreativeTabs var2, List var3) {
        ItemStack tCharged = GregTech_API.getGregTechItem(mFullID, 1, 0), tUncharged = GregTech_API.getGregTechItem(mEmptyID, 1, getMaxDamage());
        GT_ModHandler.chargeElectricItem(tCharged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        if (itemID == GregTech_API.getGregTechItem(mFullID, 1, 0).itemID) var3.add(tCharged);
        if (itemID == GregTech_API.getGregTechItem(mEmptyID, 1, 0).itemID) var3.add(tUncharged);
    }
    
	@Override
    public int getItemEnchantability() {
        return 0;
    }
	
	@Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }
	
	public boolean canProvideEnergy(ItemStack aStack) {
		return true;
	}
	
	public int getChargedItemId(ItemStack aStack) {
		return GregTech_API.getGregTechItem(mFullID, 1, 0).itemID;
	}
	
	public int getEmptyItemId(ItemStack aStack) {
		return GregTech_API.getGregTechItem(mEmptyID, 1, 0).itemID;
	}
	
	public int getMaxCharge(ItemStack aStack) {
		return mCharge;
	}
	
	public int getTier(ItemStack aStack) {
		return mTier;
	}
	
	public int getTransferLimit(ItemStack aStack) {
		return mTransfer;
	}
}
