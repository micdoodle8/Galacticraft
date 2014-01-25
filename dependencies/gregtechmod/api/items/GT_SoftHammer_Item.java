package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_ToolDictNames;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GT_SoftHammer_Item extends GT_Tool_Item {
	public GT_SoftHammer_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
		super(aID, aUnlocalized, aEnglish, "To give a Machine a soft whack", aMaxDamage, aEntityDamage, true);
		GregTech_API.registerSoftHammer(new ItemStack(this, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		GT_OreDictUnificator.registerOre(GT_ToolDictNames.craftingToolSoftHammer, new ItemStack(this, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		setCraftingSound(GregTech_API.sSoundList.get(101));
		setBreakingSound(GregTech_API.sSoundList.get(101));
		setEntityHitSound(GregTech_API.sSoundList.get(101));
	}
	
	@Override
	public void checkEnchantmentEffects(ItemStack aStack) {
		super.checkEnchantmentEffects(aStack);
		if (aStack != null) aStack.addEnchantment(Enchantment.knockback, 2);
	}
	
	@Override
	public void addAdditionalToolTips(List aList, ItemStack aStack) {
		super.addAdditionalToolTips(aList, aStack);
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_1", "Can enable/disable Machines"));
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_2", "Can rotate some Blocks as well"));
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_4", "Can switch Redstone Lamps and Booster Tracks"));
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		super.onItemUseFirst(aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
		if (aWorld.isRemote) {
    		return false;
    	}
    	Block aBlock = Block.blocksList[aWorld.getBlockId(aX, aY, aZ)];
    	if (aBlock == null) return false;
    	byte aMeta = (byte)aWorld.getBlockMetadata(aX, aY, aZ);
//    	TileEntity aTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
    	
    	if (aBlock == Block.redstoneLampActive) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.isRemote = true;
				aWorld.setBlock(aX, aY, aZ, Block.redstoneLampIdle.blockID, 0, 0);
				aWorld.isRemote = false;
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(101), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	if (aBlock == Block.redstoneLampIdle) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.isRemote = true;
				aWorld.setBlock(aX, aY, aZ, Block.redstoneLampActive.blockID, 0, 0);
				aWorld.isRemote = false;
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(101), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	if (aBlock == Block.railPowered) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.isRemote = true;
				aWorld.setBlock(aX, aY, aZ, aBlock.blockID, (aMeta + 8) % 16, 0);
				aWorld.isRemote = false;
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(101), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	if (aBlock == Block.railActivator) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.isRemote = true;
				aWorld.setBlock(aX, aY, aZ, aBlock.blockID, (aMeta + 8) % 16, 0);
				aWorld.isRemote = false;
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(101), 1.0F, -1, aX, aY, aZ);
			}
    		return true;
    	}
    	if (aBlock == Block.wood || aBlock == Block.hay) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta + 4) % 12, 3);
			}
    		return true;
    	}
	    if (aBlock == Block.pistonBase || aBlock == Block.pistonStickyBase || aBlock == Block.dispenser || aBlock == Block.dropper) {
			if (aMeta < 6 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta+1) % 6, 3);
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(101), 1.0F, -1, aX, aY, aZ);
			}
	    	return true;
	    }
	    if (aBlock == Block.pumpkin || aBlock == Block.pumpkinLantern || aBlock == Block.furnaceIdle || aBlock == Block.furnaceBurning || aBlock == Block.chest || aBlock == Block.chestTrapped) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, ((aMeta-1)%4)+2, 3);
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(101), 1.0F, -1, aX, aY, aZ);
			}
	    	return true;
	    }
	    if (aBlock == Block.hopperBlock) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta+1)%6==1?(aMeta+1)%6:2, 3);
				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(101), 1.0F, -1, aX, aY, aZ);
			}
	    	return true;
	    }
    	return false;
    }
}