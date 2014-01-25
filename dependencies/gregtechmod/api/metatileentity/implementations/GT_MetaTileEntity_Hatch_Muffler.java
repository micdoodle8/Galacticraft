package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_Muffler extends MetaTileEntity {
	public GT_MetaTileEntity_Hatch_Muffler(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public GT_MetaTileEntity_Hatch_Muffler() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return true;}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}
	@Override public int getInvSize()								{return 0;}
	
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Muffler();
	}
	
	@Override public void saveNBTData(NBTTagCompound aNBT) {
		//
	}
	
	@Override public void loadNBTData(NBTTagCompound aNBT) {
		//
	}
	
	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
		if (aSide==aFacing) return 279;
		if (aSide==0) return 32;
		if (aSide==1) return 29;
    	return 40;
	}
	
	@Override
	public String getDescription() {
		return "Outputs the Pollution (Pollution might come later)";
	}
	
	public boolean polluteEnvironment() {
		return getBaseMetaTileEntity().getAirAtSide(getBaseMetaTileEntity().getFrontFacing());
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
