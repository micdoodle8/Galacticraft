package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

public class GT_SolderingMetal_Item extends GT_Tool_Item {
	public GT_SolderingMetal_Item(int aID, String aName, int aMaxDamage, int aEntityDamage) {
		super(aID, aName, "Used in conjunction with Soldering Tools", aMaxDamage, aEntityDamage);
		GregTech_API.registerSolderingMetal(new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		GT_OreDictUnificator.registerOre("craftingToolSolderingMetal", new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		setUsageAmounts(1, 3, 1);
	}
}