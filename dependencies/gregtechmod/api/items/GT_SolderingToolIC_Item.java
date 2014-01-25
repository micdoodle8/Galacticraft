package gregtechmod.api.items;

import ic2.api.item.IElectricItem;

public class GT_SolderingToolIC_Item extends GT_SolderingTool_Item implements IElectricItem {
	public GT_SolderingToolIC_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDischargedGTID) {
		super(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDischargedGTID);
	}
}