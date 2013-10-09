package gregtechmod.api.items;

import ic2.api.item.IElectricItem;

public class GT_DrillIC_Item extends GT_Drill_Item implements IElectricItem {
	public GT_DrillIC_Item(int aID, String aName, int aMaxDamage, int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak, int aDisChargedGTID) {
		super(aID, aName, aMaxDamage, aEntityDamage, aToolQuality, aToolStrength, aEnergyConsumptionPerBlockBreak, aDisChargedGTID);
	}
}