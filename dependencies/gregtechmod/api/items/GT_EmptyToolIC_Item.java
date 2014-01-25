package gregtechmod.api.items;

import ic2.api.item.IElectricItem;

public class GT_EmptyToolIC_Item extends GT_EmptyTool_Item implements IElectricItem {
	public GT_EmptyToolIC_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aChargedGTID) {
		super(aID, aUnlocalized, aEnglish, aMaxDamage, aChargedGTID);
	}
}