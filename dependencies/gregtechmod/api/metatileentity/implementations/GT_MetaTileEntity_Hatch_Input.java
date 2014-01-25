package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Hatch_Input extends GT_MetaTileEntity_BasicTank {
	public GT_MetaTileEntity_Hatch_Input(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public GT_MetaTileEntity_Hatch_Input() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return true;}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}
	@Override public void onRightclick(EntityPlayer aPlayer)		{getBaseMetaTileEntity().openGUI(aPlayer, 153, GregTech_API.gregtechmod);}
	
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Input();
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
		return "For inputting Materials into Multiblocks";
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
	public int getTankPressure() {
		return -100;
	}
}
