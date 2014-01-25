package gregtechmod.api.items;

import ic2.api.item.IElectricItem;

public class GT_SawIC_Item extends GT_Saw_Item implements IElectricItem {
	public GT_SawIC_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak, int aDisChargedGTID) {
		super(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aToolQuality, aToolStrength, aEnergyConsumptionPerBlockBreak, aDisChargedGTID);
	}
}