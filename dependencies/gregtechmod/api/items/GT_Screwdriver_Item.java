package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GT_Screwdriver_Item extends GT_Tool_Item {
	public GT_Screwdriver_Item(int aID, String aName, int aMaxDamage, int aEntityDamage, int aDischargedGTID) {
		super(aID, aName, "To screw Covers on Machines", aMaxDamage, aEntityDamage, -1, aDischargedGTID);
		GregTech_API.registerScrewdriver(new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		GT_OreDictUnificator.registerOre("craftingToolScrewdriver", new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		addToEffectiveList(EntityCaveSpider.class.getName());
		addToEffectiveList(EntitySpider.class.getName());
		addToEffectiveList("EntityTFHedgeSpider");
		addToEffectiveList("EntityTFKingSpider");
		addToEffectiveList("EntityTFSwarmSpider");
		addToEffectiveList("EntityTFTowerBroodling");
		setCraftingSound(GregTech_API.sSoundList.get(100));
		setBreakingSound(GregTech_API.sSoundList.get(100));
	}
	
	@Override
	public void addAdditionalToolTips(List aList, ItemStack aStack) {
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_1", "Can switch the Design of certain Blocks"));
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_2", "Can rotate Repeaters and Comparators"));
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		super.onItemUseFirst(aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
    	if (aWorld.isRemote) {
    		return false;
    	}
    	short aBlockID = (short)aWorld.getBlockId(aX, aY, aZ);
    	if (aBlockID < 0 || aBlockID >= Block.blocksList.length) return false;
    	Block aBlock = Block.blocksList[aBlockID];
    	if (aBlock == null) return false;
    	byte aMeta = (byte)aWorld.getBlockMetadata(aX, aY, aZ);
    	TileEntity aTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
    	
    	if (aBlockID == Block.redstoneRepeaterIdle.blockID || aBlockID == Block.redstoneRepeaterActive.blockID) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta / 4) * 4  + (((aMeta%4) + 1) % 4), 3);
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	if (aBlockID == Block.redstoneComparatorIdle.blockID || aBlockID == Block.redstoneComparatorActive.blockID) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta / 4) * 4  + (((aMeta%4) + 1) % 4), 3);
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	return false;
    }
}