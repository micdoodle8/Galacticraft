package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.ICoverable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * For Covers with a special behavior.
 */
public abstract class GT_CoverBehavior {
	public static volatile int VERSION = 402;
	
	/**
	 * Gives all Covers which are out of these Stacks a special behavior.
	 */
	public GT_CoverBehavior(ItemStack[] aCovers) {
		for (ItemStack tCover : aCovers) GregTech_API.sCoverBehaviors.put(GT_Utility.stackToInt(tCover), this);
	}
	
	/**
	 * Gives all Covers which are this ID a special behavior.
	 */
	public GT_CoverBehavior(int[] aCovers) {
		for (int tCover : aCovers) GregTech_API.sCoverBehaviors.put(tCover, this);
	}
	
	/**
	 * Gives Cover which is out of this Stack a special behavior.
	 */
	public GT_CoverBehavior(ItemStack aCover) {
		GregTech_API.sCoverBehaviors.put(GT_Utility.stackToInt(aCover), this);
	}
	
	/**
	 * Gives Cover which has this ID a special behavior.
	 */
	public GT_CoverBehavior(int aCover) {
		GregTech_API.sCoverBehaviors.put(aCover, this);
	}
	
	/**
	 * No specials, you shouldn't use this Constructor, this is only to register the generic Cover
	 */
	public GT_CoverBehavior() {
		
	}
	
	/**
	 * Called by updateEntity inside the covered TileEntity. aCoverVariable is the Value you returned last time.
	 */
	public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return aCoverVariable;
	}
	
	/**
	 * Called when someone rightclicks this Cover. 
	 * 
	 * return true, if something actually happens.
	 */
	public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		return false;
	}
	
	/**
	 * Called when someone rightclicks this Cover Client Side
	 * 
	 * return true, if something actually happens.
	 */
	public boolean onCoverRightclickClient(byte aSide, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		return false;
	}
	
	/**
	 * Called when someone rightclicks this Cover with a Screwdriver. Doesn't call @onCoverRightclick in this Case.
	 * 
	 * return the new Value of the Cover Variable
	 */
	public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		return aCoverVariable;
	}
	
	/**
	 * Checks if the Cover can be placed on this.
	 */
	public boolean isCoverPlaceable(byte aSide, int aCoverID, ICoverable aTileEntity) {
		return true;
	}
	
	/**
	 * Removes the Cover if this returns true, or if aForced is true.
	 * Doesn't get called when the Machine Block is getting broken, only if you break the Cover away from the Machine.
	 */
	public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, boolean aForced) {
		return true;
	}
	
	/**
	 * Gives a small Text for the status of the Cover.
	 */
	public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return "";
	}
	
	/**
	 * How Blast Proof the Cover is. 30 is normal.
	 */
	public float getBlastProofLevel(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return 30.0F;
	}
	
	/**
	 * If it lets RS-Signals into the Block
	 * 
	 * This is just Informative so that Machines know if their Redstone Input is blocked or not
	 */
	public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets RS-Signals out of the Block
	 */
	public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets Energy into the Block
	 */
	public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets Energy out of the Block
	 */
	public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets Liquids into the Block
	 */
	public boolean letsLiquidIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets Liquids out of the Block
	 */
	public boolean letsLiquidOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets Items into the Block
	 */
	public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets Items out of the Block
	 */
	public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * If it lets you rightclick the Machine normally
	 */
	public boolean isGUIClickable(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * Needs to return true for Covers, which have a Redstone Output on their Facing.
	 * 
	 */
	public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return false;
	}
	
	/**
	 * Called to determine the incoming Redstone Signal of a Machine.
	 * Returns the original Redstone per default.
	 * The Cover should @letsRedstoneGoIn or the aInputRedstone Parameter is always 0.
	 */
	public byte getRedstoneInput(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return letsRedstoneGoIn(aSide, aCoverID, aCoverVariable, aTileEntity)?aInputRedstone:0;
	}
	
	/**
	 * Gets the Tick Rate for doCoverThings of the Cover
	 * 
	 * 0 = No Ticks! Yes, 0 is Default, you have to override this
	 */
	public short getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return 0;
	}
	
	/**
	 * if this is a simple Cover, which can also be used on Bronze Machines and similar.
	 */
	public boolean isSimpleCover() {
		return false;
	}
}