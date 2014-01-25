package gregtechmod.api.items;

import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_ModHandler;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import buildcraft.api.tools.IToolWrench;

public class GT_WrenchICBC_Item extends GT_WrenchIC_Item implements IToolWrench {
	public GT_WrenchICBC_Item(int aID, String aName, int aMaxDamage, int aEntityDamage, int aDischargedGTID) {
		super(aID, aName, aName, aMaxDamage, aEntityDamage, aDischargedGTID);
	}
	
	@Override
	public boolean canWrench(EntityPlayer aPlayer, int aX, int aY, int aZ) {
		return GT_ModHandler.canUseElectricItem(aPlayer.inventory.getCurrentItem(), 1000);
	}
	
	@Override
	public void wrenchUsed(EntityPlayer aPlayer, int aX, int aY, int aZ) {
		GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer);
	}
	
	@Override
	public void addAdditionalToolTips(List aList, ItemStack aStack) {
		super.addAdditionalToolTips(aList, aStack);
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_bc", "Works as Buildcraft Wrench too"));
	}
}