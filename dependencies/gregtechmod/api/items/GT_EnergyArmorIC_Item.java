package gregtechmod.api.items;

import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT_EnergyArmorIC_Item extends GT_EnergyArmor_Item implements IMetalArmor, IElectricItem {
	public GT_EnergyArmorIC_Item(int aID, String aUnlocalized, String aEnglish, int aCharge, int aTransfer, int aTier, int aDamageEnergyCost, int aSpecials, double aArmorAbsorbtionPercentage, boolean aChargeProvider, int aType, int aArmorIndex) {
		super(aID, aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aDamageEnergyCost, aSpecials, aArmorAbsorbtionPercentage, aChargeProvider, aType, aArmorIndex);
	}
	
	@Override
	public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player) {
		return true;
	}
}