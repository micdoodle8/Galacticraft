package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GT_Crowbar_Item extends GT_Tool_Item {
	public GT_Crowbar_Item(int aID, String aName, int aMaxDamage, int aEntityDamage) {
		super(aID, aName, "To remove Covers from Machines", aMaxDamage, aEntityDamage, -1, -1, 5, 20.0F);
		GregTech_API.registerCrowbar(new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		GT_OreDictUnificator.registerOre("craftingToolCrowbar", new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		addToBlockList(Block.rail);
		addToBlockList(Block.railPowered);
		addToBlockList(Block.railDetector);
		addToBlockList(Block.railActivator);
		addToBlockList(GT_ModHandler.getRCItem("track.boarding", 1));
		addToBlockList(GT_ModHandler.getRCItem("track.elevator", 1));
		setUsageAmounts(1, 2, 1);
	}
	
	protected boolean isRCCrowbar() {
		return false;
	}
	
	@Override
	public void addAdditionalToolTips(List aList, ItemStack aStack) {
		super.addAdditionalToolTips(aList, aStack);
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_1", "Can turn Rails"));
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		super.onItemUseFirst(aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
		if (aWorld.isRemote) {
    		return false;
    	}
		if (isRCCrowbar()) return false;
    	short aBlockID = (short)aWorld.getBlockId(aX, aY, aZ);
    	if (aBlockID < 0 || aBlockID >= Block.blocksList.length) return false;
    	Block aBlock = Block.blocksList[aBlockID];
    	if (aBlock == null) return false;
    	byte aMeta = (byte)aWorld.getBlockMetadata(aX, aY, aZ);
    	TileEntity aTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
    	
    	if (aBlock == Block.rail) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.isRemote = true;
				aWorld.setBlock(aX, aY, aZ, aBlockID, (aMeta + 1) % 10, 0);
				aWorld.isRemote = false;
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(0), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	if (aBlock == Block.railPowered || aBlock == Block.railActivator || aBlock == Block.railDetector) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.isRemote = true;
				aWorld.setBlock(aX, aY, aZ, aBlockID, ((aMeta / 8) * 8) + (((aMeta%8)+1) % 6), 0);
				aWorld.isRemote = false;
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(0), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	return false;
    }
}