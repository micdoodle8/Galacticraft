package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GT_Spray_Bug_Item extends GT_Tool_Item {
	public GT_Spray_Bug_Item(int aID, String aName, int aMaxDamage, int aEntityDamage) {
		super(aID, aName, "A very 'buggy' Spray", aMaxDamage, aEntityDamage);
		addToEffectiveList(EntityCaveSpider.class.getName());
		addToEffectiveList(EntitySpider.class.getName());
		addToEffectiveList("EntityTFHedgeSpider");
		addToEffectiveList("EntityTFKingSpider");
		addToEffectiveList("EntityTFSwarmSpider");
		addToEffectiveList("EntityTFTowerBroodling");
		addToEffectiveList("EntityTFFireBeetle");
		addToEffectiveList("EntityTFSlimeBeetle");
		setCraftingSound(GregTech_API.sSoundList.get(102));
		setBreakingSound(GregTech_API.sSoundList.get(102));
		setEntityHitSound(GregTech_API.sSoundList.get(102));
		setUsageAmounts(8, 4, 1);
	}
	
	@Override
	public void onHitEntity(Entity aEntity) {
		if (aEntity instanceof EntityLiving) {
			((EntityLiving)aEntity).addPotionEffect(new PotionEffect(Potion.poison.getId(), 60, 1, false));
			((EntityLiving)aEntity).addPotionEffect(new PotionEffect(Potion.confusion.getId(), 600, 1, false));
		}
	}
	
	@Override
	public ItemStack getEmptyItem(ItemStack aStack) {
		return GT_OreDictUnificator.getFirstOre("craftingSprayCan", 1);
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
    	
    	try {
    		if (aTileEntity instanceof ic2.api.crops.ICropTile) {
    			int tCropBefore = ((ic2.api.crops.ICropTile)aTileEntity).getWeedExStorage();
	    		if (tCropBefore <= 100 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
	    			((ic2.api.crops.ICropTile)aTileEntity).setWeedExStorage(tCropBefore+100);
	    			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(102), 1.0F, -1, aX, aY, aZ);
	        		return true;
	    		}
    		}
    	} catch (Throwable e) {}
    	
    	return false;
    }
}