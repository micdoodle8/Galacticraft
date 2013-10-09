package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_EnergyInput extends MetaTileEntity {
	public GT_MetaTileEntity_Hatch_EnergyInput(int aID, String mName, String mNameRegional) {
		super(aID, mName, mNameRegional);
	}
	
	public GT_MetaTileEntity_Hatch_EnergyInput() {
		
	}
	
	@Override public boolean isBatteryUpgradable()					{return true;}
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return true;}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}
	@Override public boolean isEnetInput() 							{return true;}
	@Override public boolean isInputFacing(byte aSide)				{return aSide==getBaseMetaTileEntity().getFrontFacing();}
	@Override public int getInvSize()								{return 0;}
    @Override public int maxEUInput()								{return GregTech_API.VOLTAGE_EXTREME;}
    @Override public int maxEUStore()								{return 100000;}
    @Override public int maxMJStore()								{return maxEUStore();}
    @Override public int maxSteamStore()							{return maxEUStore();}
	
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_EnergyInput();
	}
	
	@Override public void saveNBTData(NBTTagCompound aNBT) {
		
	}
	
	@Override public void loadNBTData(NBTTagCompound aNBT) {
		
	}
	
	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide==aFacing) return 278;
		if (aSide==0) return 32;
		if (aSide==1) return 29;
    	return 40;
	}
	
	@Override
	public String getDescription() {
		return "Energy Injector for Multiblocks";
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
