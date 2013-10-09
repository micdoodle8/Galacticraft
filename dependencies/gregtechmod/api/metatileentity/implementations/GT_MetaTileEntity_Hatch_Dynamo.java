package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_Dynamo extends MetaTileEntity {
	public GT_MetaTileEntity_Hatch_Dynamo(int aID, String mName, String mNameRegional) {
		super(aID, mName, mNameRegional);
	}
	
	public GT_MetaTileEntity_Hatch_Dynamo() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return true;}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}
	@Override public boolean isEnetOutput() 						{return true;}
	@Override public boolean isOutputFacing(byte aSide)				{return aSide==getBaseMetaTileEntity().getFrontFacing();}
	@Override public int getInvSize()								{return 0;}
	@Override public int getMinimumStoredEU()						{return 512;}
    @Override public int maxEUOutput()								{return Math.max(0, Math.min(getEUVar() - getMinimumStoredEU(), GregTech_API.VOLTAGE_EXTREME));}
    @Override public int maxEUStore()								{return GregTech_API.VOLTAGE_EXTREME * 4 + getMinimumStoredEU();}
    @Override public String getSpecialVoltageToolTip()				{return "Max EU/p OUT: 0 - 2048 (depends on generated Energy)";}
	
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Dynamo();
	}
	
	@Override public void saveNBTData(NBTTagCompound aNBT) {
		
	}
	
	@Override public void loadNBTData(NBTTagCompound aNBT) {
		
	}
	
	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide==aFacing) return 274;
		if (aSide==0) return 32;
		if (aSide==1) return 29;
    	return 40;
	}
	
	@Override
	public String getDescription() {
		return "Generating electric Energy from Multiblocks";
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
