package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

public class GT_File_Item extends GT_Tool_Item {
	public GT_File_Item(int aID, String aName, int aMaxDamage, int aEntityDamage) {
		super(aID, aName, "For sharpening or rounding Edges", aMaxDamage, aEntityDamage);
		//GregTech_API.registerFile(new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		GT_OreDictUnificator.registerOre("craftingToolFile", new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		setUsageAmounts(1, 3, 2);
	}
}