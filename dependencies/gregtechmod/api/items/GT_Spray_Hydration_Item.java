package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_OreDictNames;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GT_Spray_Hydration_Item extends GT_Tool_Item {
	public GT_Spray_Hydration_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
		super(aID, aUnlocalized, aEnglish, "To hydrate Crops and similar", aMaxDamage, aEntityDamage, true);
		setCraftingSound(GregTech_API.sSoundList.get(102));
		setBreakingSound(GregTech_API.sSoundList.get(102));
		setEntityHitSound(GregTech_API.sSoundList.get(102));
		setUsageAmounts(20, 3, 1);
	}
	
	@Override
	public ItemStack getEmptyItem(ItemStack aStack) {
		return GT_OreDictUnificator.getFirstOre(GT_OreDictNames.craftingSprayCan, 1);
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		super.onItemUseFirst(aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
		if (aWorld.isRemote) {
    		return false;
    	}
    	Block aBlock = Block.blocksList[aWorld.getBlockId(aX, aY, aZ)];
    	if (aBlock == null) return false;
//    	byte aMeta = (byte)aWorld.getBlockMetadata(aX, aY, aZ);
    	TileEntity aTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
    	
    	try {
    		if (aTileEntity instanceof ic2.api.crops.ICropTile) {
    			int tCropBefore = ((ic2.api.crops.ICropTile)aTileEntity).getHydrationStorage();
	    		if (tCropBefore <= 100 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
	    			((ic2.api.crops.ICropTile)aTileEntity).setHydrationStorage(tCropBefore+100);
	    			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(102), 1.0F, -1, aX, aY, aZ);
	    		}
        		return true;
    		}
    	} catch (Throwable e) {/*Do nothing*/}
    	
    	return false;
    }
}