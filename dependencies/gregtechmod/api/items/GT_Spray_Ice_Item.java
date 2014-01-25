package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_OreDictNames;
import gregtechmod.api.enums.Materials;
import gregtechmod.api.enums.OrePrefixes;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class GT_Spray_Ice_Item extends GT_Tool_Item {
	public GT_Spray_Ice_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
		super(aID, aUnlocalized, aEnglish, "Very effective against Slimes", aMaxDamage, aEntityDamage, true);
		addToEffectiveList(EntitySlime.class.getName());
		addToEffectiveList("BlueSlime");
		addToEffectiveList("SlimeClone");
		addToEffectiveList("MetalSlime");
		addToEffectiveList("EntityTFFireBeetle");
		addToEffectiveList("EntityTFMazeSlime");
		addToEffectiveList("EntityTFSlimeBeetle");
		setCraftingSound(GregTech_API.sSoundList.get(102));
		setBreakingSound(GregTech_API.sSoundList.get(102));
		setEntityHitSound(GregTech_API.sSoundList.get(102));
		setUsageAmounts(4, 16, 1);
		
		for (String tName : Arrays.asList(OrePrefixes.bucket.get(Materials.Water), OrePrefixes.cell.get(Materials.Water), OrePrefixes.capsule.get(Materials.Water))) {
			GT_ModHandler.addShapelessCraftingRecipe(new ItemStack(Block.ice, 1, 0), false, new Object[] {new ItemStack(this, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), tName});
		}
	}
	
	@Override
	public void onHitEntity(Entity aEntity) {
		if (aEntity instanceof EntityLiving) {
			((EntityLiving)aEntity).addPotionEffect(new PotionEffect(Potion.weakness.getId(), 400, 2, false));
			((EntityLiving)aEntity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 400, 2, false));
		}
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
    	aX += ForgeDirection.getOrientation(aSide).offsetX;
    	aY += ForgeDirection.getOrientation(aSide).offsetY;
    	aZ += ForgeDirection.getOrientation(aSide).offsetZ;
    	Block aBlock = Block.blocksList[aWorld.getBlockId(aX, aY, aZ)];
    	if (aBlock == null) return false;
    	byte aMeta = (byte)aWorld.getBlockMetadata(aX, aY, aZ);
//    	TileEntity aTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
    	
    	if (aBlock == Block.waterStill || aBlock == Block.waterMoving) {
    		if (aMeta == 0 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
    			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(102), 1.0F, -1, aX, aY, aZ);
        		aWorld.setBlock(aX, aY, aZ, Block.ice.blockID, 0, 3);
    			return true;
    		}
    		return false;
    	}
    	
    	if (aBlock == Block.lavaStill || aBlock == Block.lavaMoving) {
    		if (aMeta == 0 && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
    			GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(102), 1.0F, -1, aX, aY, aZ);
        		aWorld.setBlock(aX, aY, aZ, Block.obsidian.blockID, 0, 3);
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
}