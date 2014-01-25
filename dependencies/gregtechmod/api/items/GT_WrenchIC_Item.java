package gregtechmod.api.items;

import ic2.api.item.IElectricItem;

public class GT_WrenchIC_Item extends GT_Wrench_Item implements IElectricItem {
	public GT_WrenchIC_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDischargedGTID) {
		super(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDischargedGTID);
	}
}