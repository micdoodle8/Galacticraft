package gregtechmod.api.metatileentity;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.interfaces.IMetaTileEntity;
import gregtechmod.api.util.GT_Config;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_Utility;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * Extend this Class to add a new MetaMachine
 * Call the Constructor with the desired ID at the load-phase (not preload and also not postload!)
 * Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * 
 * Call the Constructor like the following example inside the Load Phase, to register it. 
 * "new GT_MetaTileEntity_E_Furnace(54, "GT_E_Furnace", "Automatic E-Furnace");"
 */
public abstract class MetaPipeEntity implements IMetaTileEntity {
	public static volatile int VERSION = 402;
	
	/**
	 * This variable tells, which directions the Block is connected to. It is a Bitmask.
	 */
	public byte mConnections = 0;
	
	/**
	 * For Pipe Rendering
	 */
	public abstract float getThickNess();
	
	public byte getTileEntityBaseType() {
		return 1;
	}
	
	/**
	 * Only assigned for the MetaTileEntity in the List! Also only used to get the localized Name for the ItemStack and for getInvName.
	 */
	public String mName;
	
	/**
	 * accessibility to this Field is no longer given, see below
	 */
	private IGregTechTileEntity mBaseMetaTileEntity;
	
	/**
	 * new getter for the BaseMetaTileEntity, which restricts usage to certain Functions.
	 */
	public IGregTechTileEntity getBaseMetaTileEntity() {
		return mBaseMetaTileEntity;
	}
	
	/**
	 * The Inventory of the MetaTileEntity. Amount of Slots can be larger than 256. HAYO!
	 */
	public ItemStack[] mInventory = new ItemStack[getInvSize()];
	
	/**
	 * This registers your Machine at the List.
	 * Use only ID's larger than 512, because i reserved these ones.
	 * See also the List in the API, as it has a Description containing all the reservations.
	 * @param aID the ID
	 * @example for Constructor overload.
	 * 
	 * 	public GT_MetaTileEntity_EBench(int aID, String mName, String mNameRegional) {
	 * 		super(aID, mName, mNameRegional);
	 * 	}
	 */
	public MetaPipeEntity(int aID, String aBasicName, String aRegionalName) {
		if (GregTech_API.mMetaTileList[aID] == null) {
			GregTech_API.mMetaTileList[aID] = this;
		} else {
			throw new IllegalArgumentException("MetaMachine-Slot Nr. " + aID + " is already occupied!");
		}
		mName = aBasicName.replaceAll(" ", "_");
		setBaseMetaTileEntity(new BaseMetaPipeEntity());
		GT_LanguageManager.addStringLocalization("tile.BlockMetaID_Machine." + mName + ".name", aRegionalName);
	}
	
	public void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity) {
		if (mBaseMetaTileEntity != null && aBaseMetaTileEntity == null) {
			mBaseMetaTileEntity.getMetaTileEntity().inValidate();
			mBaseMetaTileEntity.setMetaTileEntity(null);
		}
		mBaseMetaTileEntity = aBaseMetaTileEntity;
		if (mBaseMetaTileEntity != null) {
			mBaseMetaTileEntity.setMetaTileEntity(this);
		}
	}
	
	/**
	 * This is the normal Constructor.
	 */
	public MetaPipeEntity() {}
	
	public void onServerStart() {}
	public void onServerStop() {}
	public void onConfigLoad(GT_Config aConfig) {}
	public void setItemNBT(NBTTagCompound aNBT) {}
	public Icon getTextureIcon(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {return null;}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister aBlockIconRegister) {}
	
	public boolean allowCoverOnSide(byte aSide, int aCoverID) {return true;}
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {}
	public void onExplosion() {}
	public void onFirstTick() {}
	public void onPreTick() {}
	public void onPostTick() {}
	public void inValidate() {}
	public void onRemoval() {}
	
	/**
	 * When a GUI is opened
	 */
	public void onOpenGUI() {}
	
	/**
	 * When a GUI is closed
	 */
	public void onCloseGUI() {}
	
	/**
	 * a Player rightclicks the Machine
	 * Sneaky rightclicks are not getting passed to this!
	 */
	public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {return false;}
	public void onLeftclick(EntityPlayer aPlayer) {}
	public void onValueUpdate(byte aValue) {}
	public byte getUpdateData() {return 0;}
	
    public void doSound(byte aIndex, double aX, double aY, double aZ) {}
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {}
    public void stopSoundLoop(byte aValue, double aX, double aY, double aZ) {}
	
    public final void sendSound(byte aIndex) {getBaseMetaTileEntity().sendBlockEvent((byte)4, aIndex);}
    public final void sendLoopStart(byte aIndex) {getBaseMetaTileEntity().sendBlockEvent((byte)5, aIndex);}
    public final void sendLoopEnd(byte aIndex) {getBaseMetaTileEntity().sendBlockEvent((byte)6, aIndex);}
    
    public boolean isFacingValid(byte aFacing) {return false;}
	public boolean isWrenchable() {return true;}
    public boolean isAccessAllowed(EntityPlayer aPlayer) {return true;}
    public boolean isValidSlot(int aIndex) {return true;}
    public boolean setStackToZeroInsteadOfNull(int aIndex) {return false;}
    
	public ArrayList<String> getSpecialDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> aList) {
		return aList;
	}
	
    public boolean isLiquidInput(byte aSide) {
    	return true;
    }
    
    public boolean isLiquidOutput(byte aSide) {
    	return true;
    }
    
	/**
	 * gets the contained Liquid
	 */
	public FluidStack getFluid() {return null;}
	
	/**
	 * tries to fill this Tank
	 */
	public int fill(FluidStack resource, boolean doFill) {return 0;}
	
	/**
	 * tries to empty this Tank
	 */
	public FluidStack drain(int maxDrain, boolean doDrain) {return null;}
	
	/**
	 * Tank pressure
	 */
	public int getTankPressure() {return 0;}
	
	/**
	 * Liquid Capacity
	 */
	public int getCapacity() {return 0;}
	
	/**
	 * Progress this machine has already made
	 */
	public int getProgresstime() {return 0;}
	
	/**
	 * Progress this Machine has to do to produce something
	 */
	public int maxProgresstime() {return 0;}
	
	/**
	 * Increases the Progress, returns the overflown Progress.
	 */
	public int increaseProgress(int aProgress) {return 0;}
	
	public void onMachineBlockUpdate() {}
	public void receiveClientEvent(byte aEventID, byte aValue) {}
	public boolean isSimpleMachine() {return false;}
	
	public byte getComparatorValue(byte aSide) {
		return 0;
	}
	
	public boolean acceptsRotationalEnergy(byte aSide) {
		return false;
	}
	
	public boolean injectRotationalEnergy(byte aSide, int aSpeed, int aEnergy) {
		return false;
	}
	
	public String getSpecialVoltageToolTip() {return null;}
	
	public String getMainInfo() {return "";}
	public String getSecondaryInfo() {return "";}
	public String getTertiaryInfo() {return "";}
	public boolean isGivingInformation() {return false;}
	
	public boolean isDigitalChest() {return false;}
	public ItemStack[] getStoredItemData() {return null;}
	public void setItemCount(int aCount) {}
	public int getMaxItemCount() {return 0;}
	
	public abstract int getInvSize();
	public int getSizeInventory() {return getInvSize();}
	public ItemStack getStackInSlot(int aIndex) {if (aIndex >= 0 && aIndex < mInventory.length) return mInventory[aIndex]; return null;}
	public void setInventorySlotContents(int aIndex, ItemStack aStack) {if (aIndex >= 0 && aIndex < mInventory.length) mInventory[aIndex] = aStack;}
	public String getInvName() {if (GregTech_API.mMetaTileList[getBaseMetaTileEntity().getMetaTileID()] != null) return GregTech_API.mMetaTileList[getBaseMetaTileEntity().getMetaTileID()].getMetaName(); return "";}
	public int getInventoryStackLimit() {return 64;}
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {return getBaseMetaTileEntity().isValidSlot(aIndex);}
	
	public ItemStack decrStackSize(int aIndex, int aAmount) {
		ItemStack rStack = getStackInSlot(aIndex);
		if (rStack != null) {
			if (rStack.stackSize <= aAmount) {
				if (setStackToZeroInsteadOfNull(aIndex))
					rStack.stackSize = 0;
				else
					setInventorySlotContents(aIndex, null);
			} else {
				rStack = rStack.splitStack(aAmount);
				if (rStack.stackSize == 0) {
					if (!setStackToZeroInsteadOfNull(aIndex))
						setInventorySlotContents(aIndex, null);
				}
			}
		}
		return rStack;
	}
	
	public int[] getAccessibleSlotsFromSide(int aSide) {
		ArrayList<Integer> tList = new ArrayList<Integer>();
		for (int i = 0; i < getSizeInventory(); i++) if (isValidSlot(i)) tList.add(i);
		int[] rArray = new int[tList.size()];
		for (int i = 0; i < rArray.length; i++) rArray[i] = (int)tList.get(i);
		return rArray;
	}
	
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		return isValidSlot(aIndex) && aStack != null && (mInventory[aIndex] == null || aStack.isItemEqual(mInventory[aIndex])) && allowPutStack(aIndex, (byte)aSide, aStack);
	}
	
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		return isValidSlot(aIndex) && aStack != null && allowPullStack(aIndex, (byte)aSide, aStack);
	}
	
	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
		return fill(aSide, new FluidStack(aFluid, 1), false) == 1;
	}
	
	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
		return drain(aSide, new FluidStack(aFluid, 1), false) != null;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
		if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[] {};
		return new FluidTankInfo[] {getInfo()};
	}
	
    public int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        return fill(aFluid, doFill);
    }
    
	@Override
    public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        return fill_default(aSide, aFluid, doFill);
    }
    
	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
		if (getFluid() != null && aFluid != null && getFluid().isFluidEqual(aFluid)) return drain(aFluid.amount, doDrain);
		return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain);
	}
	
	@Override
	public int getFluidAmount() {
		return 0;
	}
	
	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public String getMetaName() {
		return mName;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}
	
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}
	
	@Override
	public void onInventoryChanged() {
		
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}
	
	@Override
	public void openChest() {
		
	}
	
	@Override
	public void closeChest() {
		
	}
	
	@Override
	public ItemStack[] getRealInventory() {
		return mInventory;
	}
}