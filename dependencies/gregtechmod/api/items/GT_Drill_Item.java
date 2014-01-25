package gregtechmod.api.items;

import net.minecraft.block.Block;


public class GT_Drill_Item extends GT_Tool_Item {
	public GT_Drill_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak, int aDisChargedGTID) {
		super(aID, aUnlocalized, aEnglish, "For quickly making Holes", aMaxDamage, aEntityDamage, true, -1, aDisChargedGTID, aToolQuality, aToolStrength);
		setElectricConsumptionPerBrokenBlock(aEnergyConsumptionPerBlockBreak);
		addToBlockList(Block.silverfish);
		addToBlockList(Block.tnt);
	}
	
	@Override
    public boolean hasContainerItem() {
        return false;
    }
}