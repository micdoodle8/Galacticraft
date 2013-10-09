package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class GT_Wrench_Item extends GT_Tool_Item {
	
	public GT_Wrench_Item(int aID, String aName, int aMaxDamage, int aEntityDamage, int aDischargedGTID) {
		super(aID, aName, "To dismantle and rotate Blocks of most Mods", aMaxDamage, aEntityDamage, -1, aDischargedGTID);
		GregTech_API.registerWrench(new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		GT_OreDictUnificator.registerOre("craftingToolWrench", new ItemStack(itemID, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		addToEffectiveList(EntityIronGolem.class.getName());
		addToEffectiveList("EntityTFTowerGolem");
		addToEffectiveList("EntityGolemBase");
		addToEffectiveList("EntityGolemClay");
		addToEffectiveList("EntityGolemClayAdvanced");
		addToEffectiveList("EntityGolemIronGuardian");
		addToEffectiveList("EntityGolemStone");
		addToEffectiveList("EntityGolemStoneAdvanced");
		addToEffectiveList("EntityGolemStraw");
		addToEffectiveList("EntityGolemTallow");
		addToEffectiveList("EntityGolemTallowAdvanced");
		addToEffectiveList("EntityGolemWarrior");
		addToEffectiveList("EntityGolemWood");
		addToEffectiveList("EntityGolemWorker");
		addToEffectiveList("EntitySummonedEarthGolem");
		addToEffectiveList("EntityTowerGuardian");
		setCraftingSound(GregTech_API.sSoundList.get(100));
		setBreakingSound(GregTech_API.sSoundList.get(100));
		setUsageAmounts(8, 3, 1);
	}
	
	@Override
	public void addAdditionalToolTips(List aList, ItemStack aStack) {
		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_1", "Rotation of target depends on where exactly you click"));
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
    	byte aMeta = (byte)aWorld.getBlockMetadata(aX, aY, aZ), aTargetSide = GT_Utility.determineWrenchingSide((byte)aSide, hitX, hitY, hitZ);
    	TileEntity aTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
    	
    	try {
        	if (aTileEntity != null && aTileEntity instanceof ic2.api.tile.IWrenchable) {
        		if (((ic2.api.tile.IWrenchable)aTileEntity).wrenchCanSetFacing(aPlayer, aTargetSide)) {
        			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
            			((ic2.api.tile.IWrenchable)aTileEntity).setFacing(aTargetSide);
            			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
            		}
        			return true;
        		}
        		if (((ic2.api.tile.IWrenchable)aTileEntity).wrenchCanRemove(aPlayer)) {
        			int tDamage = (((ic2.api.tile.IWrenchable)aTileEntity).getWrenchDropRate() < 1.0F ? 10 : 3);
        			if (GT_ModHandler.damageOrDechargeItem(aStack, tDamage, tDamage*1000, aPlayer)) {
            			ItemStack tOutput = ((ic2.api.tile.IWrenchable)aTileEntity).getWrenchDrop(aPlayer);
            			if (tOutput != null) aWorld.spawnEntityInWorld(new EntityItem(aWorld, aX+0.5, aY+0.5, aZ+0.5, tOutput));
            			aWorld.setBlockToAir(aX, aY, aZ);
            			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
            		}
        			return true;
        		}
        		return true;
        	}
        } catch(Throwable e) {}
    	
    	try {
        	if (aTileEntity instanceof universalelectricity.prefab.tile.IRotatable) {
        		if (((universalelectricity.prefab.tile.IRotatable)aTileEntity).getDirection().ordinal() != aTargetSide) {
        			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
            			((universalelectricity.prefab.tile.IRotatable)aTileEntity).setDirection(ForgeDirection.getOrientation(aTargetSide));
            			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
            		}
        			return true;
        		}
        		return true;
        	}
        } catch(Throwable e) {}
    	
//    	if (aBlock == Block.wood || aBlock == Block.field_111038_cB) {
//			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
//				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, (aMeta + 4) % 12, 3);
//				GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
//			}
//    		return true;
//    	}
    	
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
    	
    	if (aBlockID == Block.workbench.blockID || aBlockID == Block.bookShelf.blockID) {
			if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
    			aWorld.spawnEntityInWorld(new EntityItem(aWorld, aX+0.5, aY+0.5, aZ+0.5, new ItemStack(aBlockID, 1, aMeta)));
    			aWorld.setBlockToAir(aX, aY, aZ);
    			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
    		}
    		return true;
    	}
    	
    	if (aMeta == aTargetSide) {
	    	if (aBlockID == Block.pistonBase.blockID || aBlockID == Block.pistonStickyBase.blockID || aBlockID == Block.dispenser.blockID || aBlockID == Block.dropper.blockID || aBlockID == Block.furnaceIdle.blockID || aBlockID == Block.furnaceBurning.blockID || aBlockID == Block.chest.blockID || aBlockID == Block.chestTrapped.blockID || aBlockID == Block.hopperBlock.blockID) {
	    		if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
        			aWorld.spawnEntityInWorld(new EntityItem(aWorld, aX+0.5, aY+0.5, aZ+0.5, new ItemStack(aBlockID, 1, 0)));
        			aWorld.setBlockToAir(aX, aY, aZ);
        			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
	    		}
	    		return true;
	    	}
    	} else {
	    	if (aBlockID == Block.pistonBase.blockID || aBlockID == Block.pistonStickyBase.blockID || aBlockID == Block.dispenser.blockID || aBlockID == Block.dropper.blockID) {
				if (aMeta < 6 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
					aWorld.setBlockMetadataWithNotify(aX, aY, aZ, aTargetSide, 3);
					GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
		    	}
	    		return true;
	    	}
	    	if (aBlockID == Block.pumpkin.blockID || aBlockID == Block.pumpkinLantern.blockID || aBlockID == Block.furnaceIdle.blockID || aBlockID == Block.furnaceBurning.blockID || aBlockID == Block.chest.blockID || aBlockID == Block.chestTrapped.blockID) {
				if (aTargetSide > 1 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
					aWorld.setBlockMetadataWithNotify(aX, aY, aZ, aTargetSide, 3);
					GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
		    	}
	    		return true;
	    	}
	    	if (aBlockID == Block.hopperBlock.blockID) {
				if (aTargetSide != 1 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
					aWorld.setBlockMetadataWithNotify(aX, aY, aZ, aTargetSide, 3);
					GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(100), 1.0F, -1, aX, aY, aZ);
		    	}
	    		return true;
	    	}
    	}
    	
    	return false;
    }
}