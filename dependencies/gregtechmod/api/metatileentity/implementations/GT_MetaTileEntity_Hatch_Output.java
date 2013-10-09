package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_Output extends GT_MetaTileEntity_BasicTank {
	
	public byte mMode = 0;
	
	public GT_MetaTileEntity_Hatch_Output(int aID, String mName, String mNameRegional) {
		super(aID, mName, mNameRegional);
	}
	
	public GT_MetaTileEntity_Hatch_Output() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return true;}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}
	@Override public void onRightclick(EntityPlayer aPlayer)		{getBaseMetaTileEntity().openGUI(aPlayer, 154, GregTech_API.gregtechmod);}
	
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Output();
	}
	
	@Override public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setByte("mMode", mMode);
	}
	
	@Override public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mMode = aNBT.getByte("mMode");
	}
	
	@Override public boolean doesFillContainers()	{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean doesEmptyContainers()	{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean canTankBeFilled()		{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean canTankBeEmptied()		{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean displaysItemStack()	{return true;}
	@Override public boolean displaysStackSize()	{return false;}
	
	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide==aFacing) {
	    	if (aSide == 0) return 38;
	    	if (aSide == 1) return 79;
	    	return 36;
		}
		if (aSide==0) return 32;
		if (aSide==1) return 29;
    	return 40;
	}
	
	@Override
	public String getDescription() {
		return "Use Screwdriver to specify Output Type";
	}
	
	@Override
	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack) {
		return aSide == getBaseMetaTileEntity().getFrontFacing() && aIndex == 1;
	}
	
	@Override
	public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack) {
		return aSide == getBaseMetaTileEntity().getFrontFacing() && aIndex == 0;
	}
	
	@Override
	public int getCapacity() {
		return 16000;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, int aCoverID) {
		return aCoverID != -1 && aCoverID != -2;
	}
	
	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (!getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide).isGUIClickable(aSide, getBaseMetaTileEntity().getCoverIDAtSide(aSide), getBaseMetaTileEntity().getCoverDataAtSide(aSide), getBaseMetaTileEntity())) return;
		mMode = (byte)((mMode + 1) % 8);
		switch (mMode) {
		case 0: GT_Utility.sendChatToPlayer(aPlayer, "Outputs Liquids, Steam and Items"); break;
		case 1: GT_Utility.sendChatToPlayer(aPlayer, "Outputs Steam and Items"); break;
		case 2: GT_Utility.sendChatToPlayer(aPlayer, "Outputs Steam and Liquids"); break;
		case 3: GT_Utility.sendChatToPlayer(aPlayer, "Outputs Steam"); break;
		case 4: GT_Utility.sendChatToPlayer(aPlayer, "Outputs Liquids and Items"); break;
		case 5: GT_Utility.sendChatToPlayer(aPlayer, "Outputs only Items"); break;
		case 6: GT_Utility.sendChatToPlayer(aPlayer, "Outputs only Liquids"); break;
		case 7: GT_Utility.sendChatToPlayer(aPlayer, "Outputs nothing"); break;
		}
	}
	
	public boolean outputsSteam() {
		return mMode < 4;
	}
	
	public boolean outputsLiquids() {
		return mMode % 2 == 0;
	}
	
	public boolean outputsItems() {
		return mMode % 4 < 2;
	}
	
	@Override
	public int getTankPressure() {
		return +100;
	}
}
