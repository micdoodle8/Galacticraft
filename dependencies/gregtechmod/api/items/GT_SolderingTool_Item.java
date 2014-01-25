package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_ToolDictNames;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_OreDictUnificator;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GT_SolderingTool_Item extends GT_Tool_Item {
	public GT_SolderingTool_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDischargedGTID) {
		super(aID, aUnlocalized, aEnglish, "To repair and construct Circuitry", aMaxDamage, aEntityDamage, true, -1, aDischargedGTID);
		GT_OreDictUnificator.registerOre(GT_ToolDictNames.craftingToolSolderingIron, new ItemStack(this, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		GregTech_API.registerSolderingTool(new ItemStack(this, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		setCraftingSound(GregTech_API.sSoundList.get(103));
		setBreakingSound(GregTech_API.sSoundList.get(103));
		setEntityHitSound(GregTech_API.sSoundList.get(103));
	}
	
	@Override
	public void addAdditionalToolTips(List aList, ItemStack aStack) {
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_1", "Sets the Strength of outputted Redstone"));
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_2", "Needs Soldering Metal in Inventory!"));
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		super.onItemUseFirst(aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
		if (aWorld.isRemote) {
    		return false;
    	}
    	return false;
    }
}