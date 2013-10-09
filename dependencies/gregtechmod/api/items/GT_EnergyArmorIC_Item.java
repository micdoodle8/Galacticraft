package gregtechmod.api.items;

import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;

public class GT_EnergyArmorIC_Item extends GT_EnergyArmor_Item implements IMetalArmor, IElectricItem {
	public GT_EnergyArmorIC_Item(int aID, String aName, int aCharge, int aTransfer, int aTier, int aDamageEnergyCost, int aSpecials, double aArmorAbsorbtionPercentage, boolean aChargeProvider, int aType, int aArmorIndex) {
		super(aID, aName, aCharge, aTransfer, aTier, aDamageEnergyCost, aSpecials, aArmorAbsorbtionPercentage, aChargeProvider, aType, aArmorIndex);
	}
}