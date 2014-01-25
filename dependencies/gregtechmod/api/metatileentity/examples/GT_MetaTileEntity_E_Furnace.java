package gregtechmod.api.metatileentity.examples;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This is a copy of my Electric Furnace, with a few Comments from me.
 * 
 * I chose this one as it contains also the Heating Coils as a Machine-specific Upgrade
 * 
 * It uses the Basic Machine GUIs and Containers
 */
public class GT_MetaTileEntity_E_Furnace extends GT_MetaTileEntity_BasicMachine {
	
	public int mHeatingCoilTier = 0;
	
	// see @MetaTileEntity to register MetaTileEntities
	public GT_MetaTileEntity_E_Furnace(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	// An empty constructor, which is needed for several Java reasons
	public GT_MetaTileEntity_E_Furnace() {}
	
	// Apply your empty constructor here
	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_E_Furnace();
	}
	
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		// saving the Heating Coil Upgrades
    	aNBT.setByte("mHeatingCoilTier", (byte)mHeatingCoilTier);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		// loading the Heating Coil Upgrades
    	mHeatingCoilTier = aNBT.getByte("mHeatingCoilTier");
	}
	
	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
		// putting the Heating Coil Upgrades on the dropped ItemStack after wrenching the Machine away.
		if (mHeatingCoilTier > 0) aNBT.setByte("mHeatingCoilTier", (byte)mHeatingCoilTier);
	}
	
	@Override
	public void onRightclick(EntityPlayer aPlayer) {
	    ItemStack tPlayerItem = aPlayer.inventory.getCurrentItem();
	    // Adds the Heating Coil Upgrades when rightclicking the Machine with them
	    if (mHeatingCoilTier <= 0 && GT_OreDictUnificator.isItemStackInstanceOf(tPlayerItem, "craftingHeatingCoilTier01")) {
	    	// Creative Players don't have to pay
	    	if (!aPlayer.capabilities.isCreativeMode) tPlayerItem.stackSize--;
		    mHeatingCoilTier = 1;
	    	return;
	    }
	    if (mHeatingCoilTier == 1 && GT_OreDictUnificator.isItemStackInstanceOf(tPlayerItem, "craftingHeatingCoilTier02")) {
	    	if (!aPlayer.capabilities.isCreativeMode) tPlayerItem.stackSize--;
		    mHeatingCoilTier = 2;
	    	return;
	    }
	    if (mHeatingCoilTier == 2 && GT_OreDictUnificator.isItemStackInstanceOf(tPlayerItem, "craftingHeatingCoilTier03")) {
	    	if (!aPlayer.capabilities.isCreativeMode) tPlayerItem.stackSize--;
		    mHeatingCoilTier = 3;
	    	return;
	    }
	    // Opens the GUI of your Machine. Replace GregTech_API.gregtechmod with your Mod to call your GUI.
		getBaseMetaTileEntity().openGUI(aPlayer, 135, GregTech_API.gregtechmod);
	}
	
	@Override
    public void checkRecipe() {
		// Slot 0 = HoloSlot
		// Slot 1 = Left Input
		// Slot 2 = right Input
		// Slot 3 = left Output
		// Slot 4 = right Output
		// Slot 5 = battery Slot in most cases
		// Moves a Stack from the first Input Slot to the Second one if possible
		GT_Utility.moveStackFromSlotAToSlotB(getBaseMetaTileEntity(), getBaseMetaTileEntity(), 1, 2, (byte)64, (byte)1, (byte)64, (byte)1);
		// Moves a Stack from the first Output Slot to the Second one if possible
		GT_Utility.moveStackFromSlotAToSlotB(getBaseMetaTileEntity(), getBaseMetaTileEntity(), 3, 4, (byte)64, (byte)1, (byte)64, (byte)1);
		// Actually checks the Recipe, mOutputItem1 is the ItemStack the Machine will output at the end of the Process
    	if (mInventory[2] != null && null != (mOutputItem1 = GT_ModHandler.getSmeltingOutput(mInventory[2], true, mInventory[3]))) {
    		// It shall cook at 3 EU/t, if this Machine is overclocked then it will consume more
    		mEUt = 3;
    		// The time it usually needs, the Heating Coils re decreasing this Time, and if the Machine is overclocked, then it gets processed faster
    		mMaxProgresstime = 130 / (1+mHeatingCoilTier);
    		// consume the Stack before beginning to produce. The Stack will get set to null, if its StackSize is <= 0
    		//mInventory[2].stackSize--;
    		// now the getSmeltingOutput Function does decrease the stacksize
    	}
    }
	
	@Override
	public int getFrontFacingInactive() {
		// The Furnace Front Texture when it does nothing
		// Since this relies on my Texture Indices I would recommend the use of @getTextureIcon in @MetaTileEntity
		return 250;
	}
	
	@Override
	public int getFrontFacingActive() {
		// The Furnace Front Texture when it is Active
		// Since this relies on my Texture Indices I would recommend the use of @getTextureIcon in @MetaTileEntity
		return 251;
	}
	
	@Override
	public String getDescription() {
		// The Description of the Machine, as seen in the Tooltip
		return "Test Description";
	}
}
