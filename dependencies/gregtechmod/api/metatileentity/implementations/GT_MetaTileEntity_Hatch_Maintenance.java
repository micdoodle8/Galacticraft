package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_Maintenance extends MetaTileEntity {
	
	public boolean mDuctTape = false, mWrench = false, mScrewdriver = false, mSoftHammer = false, mHardHammer = false, mSolderingTool = false, mCrowbar = false;
	
	public GT_MetaTileEntity_Hatch_Maintenance(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public GT_MetaTileEntity_Hatch_Maintenance() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return true;}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}
	@Override public boolean isValidSlot(int aIndex)				{return aIndex > 0;}
	@Override public int getInvSize()								{return 4;}
	
	@Override public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
		if (aSide == getBaseMetaTileEntity().getFrontFacing()) getBaseMetaTileEntity().openGUI(aPlayer, 155, GregTech_API.gregtechmod);
		return true;
	}
	
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Maintenance();
	}
	
	@Override public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setBoolean("mDuctTape", mDuctTape);
	}
	
	@Override public void loadNBTData(NBTTagCompound aNBT) {
		mDuctTape = aNBT.getBoolean("mDuctTape");
	}
	
	@Override
	public void onValueUpdate(byte aValue) {
		mDuctTape = ((aValue & 1) != 0);
	}
	
	@Override
	public byte getUpdateData() {
		return mDuctTape?(byte)1:0;
	}
	
	public void onToolClick(ItemStack aStack, EntityLivingBase aPlayer) {
		if (aStack == null || aPlayer == null) return;
		if (GT_Utility.isItemStackInList(aStack, GregTech_API.sWrenchList)			&& GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mWrench = true;
		if (GT_Utility.isItemStackInList(aStack, GregTech_API.sScrewdriverList)		&& GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mScrewdriver = true;
		if (GT_Utility.isItemStackInList(aStack, GregTech_API.sSoftHammerList)		&& GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mSoftHammer = true;
		if (GT_Utility.isItemStackInList(aStack, GregTech_API.sHardHammerList)		&& GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mHardHammer = true;
		if (GT_Utility.isItemStackInList(aStack, GregTech_API.sCrowbarList)			&& GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) mCrowbar = true;
		if (GT_ModHandler.useSolderingIron(aStack, aPlayer)) mSolderingTool = true;
		if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, "craftingDuctTape")) {
			mDuctTape = mWrench = mScrewdriver = mSoftHammer = mHardHammer = mCrowbar = mSolderingTool = true;
			aStack.stackSize--;
		}
	}
	
	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide==aFacing) return mDuctTape?295:294;
		if (aSide==0) return 32;
		if (aSide==1) return 29;
    	return 40;
	}
	
	@Override
	public String getDescription() {
		return "For maintaining Multiblocks";
	}
	
	@Override
	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}
	
	@Override
	public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}
}
