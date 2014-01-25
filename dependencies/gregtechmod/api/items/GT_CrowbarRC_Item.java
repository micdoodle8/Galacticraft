//package gregtechmod.api.items;
//
//import gregtechmod.api.GregTech_API;
//import gregtechmod.api.util.GT_LanguageManager;
//import gregtechmod.api.util.GT_ModHandler;
//import gregtechmod.api.util.GT_Utility;
//
//import java.util.List;
//
//import net.minecraft.entity.item.EntityMinecart;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//
///**
// * Railcraft compatibility Patch for GT-Crowbar
// * 
// * According to the Comment Field of the IToolCrowbar Interface, I need to look up for permission either at the Railcraft Wiki or from CovertJaguar himself to use it.
// * It states at http://railcraft.wikispaces.com/License+%28Info%29 that: "The Railcraft API is released open source with no restrictions on how you use or distribute it."
// * No restrictions means, that the Comment above the IToolCrowbar Interface is basically futile and just ensures that one reads the Licence, even though I almost overlooked that Comment.
// * Also it's definitely part of the api due to being in the subpackage called api.
// * 
// * Achievement get: Defused the legal Stuff of an Interface instead of asking CJ :P
// */
//public class GT_CrowbarRC_Item extends GT_Crowbar_Item implements mods.railcraft.api.core.items.IToolCrowbar {
//	public GT_CrowbarRC_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
//		super(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
//	}
//	
//	@Override
//	protected boolean isRCCrowbar() {
//		return true;
//	}
//	
//	@Override
//	public boolean canWhack(EntityPlayer aPlayer, ItemStack aCrowbar, int aX, int aY, int aZ) {
//		return true;
//	}
//	
//	@Override
//	public void onWhack(EntityPlayer aPlayer, ItemStack aCrowbar, int aX, int aY, int aZ) {
//		GT_Utility.sendSoundToPlayers(aPlayer.worldObj, GregTech_API.sSoundList.get(0), 1.0F, -1, aX, aY, aZ);
//		GT_ModHandler.damageOrDechargeItem(aCrowbar, 1, 1000, aPlayer);
//	}
//	
//	@Override
//	public boolean canLink(EntityPlayer aPlayer, ItemStack aCrowbar, EntityMinecart aCart) {
//		return true;
//	}
//	
//	@Override
//	public void onLink(EntityPlayer aPlayer, ItemStack aCrowbar, EntityMinecart aCart) {
//		GT_Utility.sendSoundToPlayers(aCart.worldObj, GregTech_API.sSoundList.get(0), 1.0F, -1, (int)aCart.posX, (int)aCart.posY, (int)aCart.posZ);
//		GT_ModHandler.damageOrDechargeItem(aCrowbar, 1, 1000, aPlayer);
//	}
//	
//	@Override
//	public boolean canBoost(EntityPlayer aPlayer, ItemStack aCrowbar, EntityMinecart aCart) {
//		return true;
//	}
//	
//	@Override
//	public void onBoost(EntityPlayer aPlayer, ItemStack aCrowbar, EntityMinecart aCart) {
//		GT_Utility.sendSoundToPlayers(aCart.worldObj, GregTech_API.sSoundList.get(0), 1.0F, -1, (int)aCart.posX, (int)aCart.posY, (int)aCart.posZ);
//		GT_ModHandler.damageOrDechargeItem(aCrowbar, 5, 5000, aPlayer);
//	}
//	
//	@Override
//	public void addAdditionalToolTips(List aList, ItemStack aStack) {
//		super.addAdditionalToolTips(aList, aStack);
//		aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".tooltip_rc", "Works as Railcraft Crowbar too"));
//	}
//}