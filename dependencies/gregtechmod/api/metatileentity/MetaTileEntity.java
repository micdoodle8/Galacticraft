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
public abstract class MetaTileEntity implements IMetaTileEntity {
	public static volatile int VERSION = 407;
	
	@Override
	public byte getTileEntityBaseType() {
		return 0;
	}
	
	/**
	 * Only assigned for the MetaTileEntity in the List! Also only used to get the localized Name for the ItemStack and for getInvName.
	 */
	public String mName;
	
	public boolean doTickProfilingInThisTick = true;
	
	/**
	 * accessibility to this Field is no longer given, see below
	 */
	private IGregTechTileEntity mBaseMetaTileEntity;
	
	@Override
	public IGregTechTileEntity getBaseMetaTileEntity() {
		return mBaseMetaTileEntity;
	}
	
	@Override
	public ItemStack getStackForm(long aAmount) {
		return new ItemStack(GregTech_API.sBlockList[1], (int)aAmount, getBaseMetaTileEntity().getMetaTileID());
	}
	
	/**
	 * The Inventory of the MetaTileEntity. Amount of Slots can be larger than 256. HAYO!
	 */
	public ItemStack[] mInventory = new ItemStack[getInvSize()];
	
	/**
	 * This registers your Machine at the List.
	 * Use only ID's larger than 2048, because i reserved these ones.
	 * See also the List in the API, as it has a Description containing all the reservations.
	 * @param aID the ID
	 * @example for Constructor overload.
	 * 
	 * 	public GT_MetaTileEntity_EBench(int aID, String mName, String mNameRegional) {
	 * 		super(aID, mName, mNameRegional);
	 * 	}
	 */
	public MetaTileEntity(int aID, String aBasicName, String aRegionalName) {
		if (GregTech_API.sPostloadStarted || !GregTech_API.sPreloadStarted) throw new IllegalAccessError("This Constructor has to be called in the load Phase");
		if (GregTech_API.mMetaTileList[aID] == null) {
			GregTech_API.mMetaTileList[aID] = this;
		} else {
			throw new IllegalArgumentException("MetaMachine-Slot Nr. " + aID + " is already occupied!");
		}
		mName = aBasicName.replaceAll(" ", "_");
		setBaseMetaTileEntity(GregTech_API.constructBaseMetaTileEntity());
		getBaseMetaTileEntity().setMetaTileID((short)aID);
		GT_LanguageManager.addStringLocalization("tile.BlockMetaID_Machine." + mName + ".name", aRegionalName);
	}
	
	@Override
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
	public MetaTileEntity() {/*Do nothing*/}
	
	@Override
	public void onServerStart() {/*Do nothing*/}
	@Override
	public void onServerStop() {/*Do nothing*/}
	@Override
	public void onConfigLoad(GT_Config aConfig) {/*Do nothing*/}
	@Override
	public void setItemNBT(NBTTagCompound aNBT) {/*Do nothing*/}
	@Override
	public Icon getTextureIcon(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {return null;}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister aBlockIconRegister) {/*Do nothing*/}
	
	@Override
	public boolean allowCoverOnSide(byte aSide, int aCoverID) {return true;}
	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {/*Do nothing*/}
	@Override
	public void onExplosion() {/*Do nothing*/}
	@Override
	public void onFirstTick() {/*Do nothing*/}
	@Override
	public void onPreTick() {/*Do nothing*/}
	@Override
	public void onPostTick() {/*Do nothing*/}
	@Override
	public void inValidate() {/*Do nothing*/}
	@Override
	public void onRemoval() {/*Do nothing*/}
	@Override
	public void onFirstServerTick() {/*Do nothing*/}
	@Override
	public void initDefaultModes(NBTTagCompound aNBT) {/*Do nothing*/}
	
	/**
	 * When a GUI is opened
	 */
	public void onOpenGUI() {/*Do nothing*/}
	
	/**
	 * When a GUI is closed
	 */
	public void onCloseGUI() {/*Do nothing*/}
	
	/**
	 * a Player rightclicks the Machine
	 * Sneaky rightclicks are not getting passed to this!
	 */
	public void onRightclick(EntityPlayer aPlayer) {/*Do nothing*/}
	
	@Override
	public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {onRightclick(aPlayer); return true;}
	@Override
	public void onLeftclick(EntityPlayer aPlayer) {/*Do nothing*/}
	@Override
	public void onValueUpdate(byte aValue) {/*Do nothing*/}
	@Override
	public byte getUpdateData() {return 0;}
	
    @Override
	public void doSound(byte aIndex, double aX, double aY, double aZ) {/*Do nothing*/}
    @Override
	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {/*Do nothing*/}
    @Override
	public void stopSoundLoop(byte aValue, double aX, double aY, double aZ) {/*Do nothing*/}
	
    @Override
	public final void sendSound(byte aIndex) {if (!getBaseMetaTileEntity().hasMufflerUpgrade()) getBaseMetaTileEntity().sendBlockEvent((byte)4, aIndex);}
    @Override
	public final void sendLoopStart(byte aIndex) {if (!getBaseMetaTileEntity().hasMufflerUpgrade()) getBaseMetaTileEntity().sendBlockEvent((byte)5, aIndex);}
    @Override
	public final void sendLoopEnd(byte aIndex) {if (!getBaseMetaTileEntity().hasMufflerUpgrade()) getBaseMetaTileEntity().sendBlockEvent((byte)6, aIndex);}
    
    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isElectric() {return true;}
    
    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isPneumatic() {return false;}
    
    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isSteampowered() {return false;}
    
    /**
     * @return true if this Device emits Energy at all
     */
    public boolean isEnetOutput() {return false;}
    
    /**
     * @return true if this Device consumes Energy at all
     */
    public boolean isEnetInput()  {return false;}
    
    /**
     * @return the amount of EU, which can be stored in this Device. Default is 0 EU.
     */
    public int maxEUStore()  {return 0;}
    
    /**
     * @return the amount of EU/t, which can be accepted by this Device before it explodes.
     */
    public int maxEUInput()  {return 0;}
    
    /**
     * @return the amount of EU/t, which can be outputted by this Device.
     */
    public int maxEUOutput() {return 0;}
    
    /**
     * @return the amount of E-net Impulses of the maxEUOutput size, which can be outputted by this Device.
     * Default is 1 Pulse, this shouldn't be set to smaller Values than 1, as it won't output anything in that Case!
     */
    public int maxEUPulses()  {return 1;}
    
    /**
     * @return true if that Side is an Output.
     */
    public boolean isOutputFacing(byte aSide) {return false;}
    
    /**
     * @return true if that Side is an Input.
     */
    public boolean isInputFacing(byte aSide) {return false;}
    
    /**
     * @return true if Transformer Upgrades increase Packet Amount.
     */
    public boolean isTransformingLowEnergy() {return true;}
    
    @Override
	public boolean isFacingValid(byte aFacing) {return false;}
	@Override
	public boolean isWrenchable() {return true;}
    @Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {return false;}
    @Override
	public boolean isValidSlot(int aIndex) {return true;}
    @Override
	public boolean setStackToZeroInsteadOfNull(int aIndex) {return false;}
    
    /**
     * This is used to set the internal Energy to the given Parameter. I use this for the IDSU.
     */
	public void setEUVar(int aEU) {
		((BaseMetaTileEntity)mBaseMetaTileEntity).mStoredEnergy = aEU;
	}
	
    /**
     * This is used to get the internal Energy. I use this for the IDSU.
     */
	public int getEUVar() {
		return ((BaseMetaTileEntity)mBaseMetaTileEntity).mStoredEnergy;
	}
	
    /**
     * This is used to set the internal MJ Energy to the given Parameter.
     */
	public void setMJVar(int aMJ) {
		((BaseMetaTileEntity)mBaseMetaTileEntity).mStoredMJ = aMJ;
	}
	
    /**
     * This is used to get the internal MJ Energy.
     */
	public int getMJVar() {
		return ((BaseMetaTileEntity)mBaseMetaTileEntity).mStoredMJ;
	}

    /**
     * This is used to set the internal Steam Energy to the given Parameter.
     */
	public void setSteamVar(int aSteam) {
		((BaseMetaTileEntity)mBaseMetaTileEntity).mStoredSteam = aSteam;
	}
	
    /**
     * This is used to get the internal Steam Energy.
     */
	public int getSteamVar() {
		return ((BaseMetaTileEntity)mBaseMetaTileEntity).mStoredSteam;
	}
	
    /**
     * @return the amount of Steam, which can be stored in this Device. Default is 0 EU.
     */
    public int maxSteamStore()  {return 0;}
    
    /**
     * @return the amount of MJ, which can be stored in this Device. Default is 0 EU.
     */
    public int maxMJStore()  {return 0;}
    
	/**
	 * @return the amount of EU, which this Device stores before starting to emit Energy.
	 * useful if you don't want to emit stored Energy until a certain Level is reached.
	 */
	public int getMinimumStoredEU() {
		return 512;
	}
	
    /**
     * Determines the Tier of the Machine, used for de-charging Tools.
     */
    public int getInputTier() {
    	return GT_Utility.getTier(getBaseMetaTileEntity().getInputVoltage());
    }
    
    /**
     * Determines the Tier of the Machine, used for charging Tools.
     */
    public int getOutputTier() {
    	return GT_Utility.getTier(getBaseMetaTileEntity().getOutputVoltage());
    }
    
    /**
     * Amount of decharge cycles per Slot
     */
    public int getDechargeCyles() {
    	return getInputTier();
    }
    
    /**
     * Amount of charge cycles per Slot
     */
    public int getChargeCyles() {
    	return getOutputTier();
    }
    
    /**
     * gets the first RechargerSlot
     */
    public int rechargerSlotStartIndex() {
    	return 0;
    }
    
    /**
     * gets the amount of RechargerSlots
     */
    public int rechargerSlotCount() {
    	return 0;
    }
    
    /**
     * gets the first DechargerSlot
     */
    public int dechargerSlotStartIndex() {
    	return 0;
    }

    /**
     * gets the amount of DechargerSlots
     */
    public int dechargerSlotCount() {
    	return 0;
    }
    
    /**
     * gets if this is protected from other Players per default or not
     */
    public boolean ownerControl() {
    	return false;
    }
    
    /**
     * gets if it has hardness -1 and so is only breakable via Wrench (just to prevent accidently breaking the Blocks)
     */
    public boolean unbreakable() {
    	return false;
    }
    
	@Override
	public ArrayList<String> getSpecialDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> aList) {
		return aList;
	}
	
    @Override
	public boolean isLiquidInput(byte aSide) {
    	return true;
    }
    
    @Override
	public boolean isLiquidOutput(byte aSide) {
    	return true;
    }
    
	/**
	 * gets the contained Liquid
	 */
	@Override
	public FluidStack getFluid() {return null;}
	
	/**
	 * tries to fill this Tank
	 */
	@Override
	public int fill(FluidStack resource, boolean doFill) {return 0;}
	
	/**
	 * tries to empty this Tank
	 */
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {return null;}
	
	/**
	 * Tank pressure
	 */
	public int getTankPressure() {return 0;}
	
	/**
	 * Liquid Capacity
	 */
	@Override
	public int getCapacity() {return 0;}
	
	@Override
	public void onMachineBlockUpdate() {/*Do nothing*/}
	@Override
	public void receiveClientEvent(byte aEventID, byte aValue) {/*Do nothing*/}
	@Override
	public boolean isSimpleMachine() {return false;}
	
	/**
	 * If this accepts up to 4 Overclockers
	 */
	public boolean isOverclockerUpgradable() {return false;}
	
	/**
	 * If this accepts Transformer Upgrades
	 */
	public boolean isTransformerUpgradable() {return false;}
	
	/**
	 * If this accepts Battery Upgrades
	 */
	public boolean isBatteryUpgradable() {return false;}
	
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
	
	/**
	 * If this TileEntity makes use of Sided Redstone behaviors.
	 * Determines only, if the Output Redstone Array is getting filled with 0 for true, or 15 for false.
	 */
	public boolean hasSidedRedstoneOutputBehavior() {
		return false;
	}
	
	/**
	 * When the Facing gets changed.
	 */
	public void onFacingChange() {/*Do nothing*/}
	
	/**
	 * if the IC2 Teleporter can drain from this.
	 */
	public boolean isTeleporterCompatible() {
		return isEnetOutput() && getBaseMetaTileEntity().getOutputVoltage() >= 128 && getBaseMetaTileEntity().getUniversalEnergyCapacity() >= 500000;
	}
	
	/**
	 * Gets the Output for the comparator on the given Side
	 */
	@Override
	public byte getComparatorValue(byte aSide) {
		return 0;
	}
	
	public boolean acceptsRotationalEnergy(byte aSide) {
		return false;
	}
	
	public boolean injectRotationalEnergy(byte aSide, int aSpeed, int aEnergy) {
		return false;
	}
	
	@Override
	public String getSpecialVoltageToolTip() {return null;}
	
	@Override
	public boolean isGivingInformation() {return false;}
	@Override
	public String[] getInfoData() {return new String[]{};}
	
	public boolean isDigitalChest() {return false;}
	public ItemStack[] getStoredItemData() {return null;}
	public void setItemCount(int aCount) {/*Do nothing*/}
	public int getMaxItemCount() {return 0;}
	
	public abstract int getInvSize();
	@Override
	public int getSizeInventory() {return getInvSize();}
	@Override
	public ItemStack getStackInSlot(int aIndex) {if (aIndex >= 0 && aIndex < mInventory.length) return mInventory[aIndex]; return null;}
	@Override
	public void setInventorySlotContents(int aIndex, ItemStack aStack) {if (aIndex >= 0 && aIndex < mInventory.length) mInventory[aIndex] = aStack;}
	@Override
	public String getInvName() {if (GregTech_API.mMetaTileList[getBaseMetaTileEntity().getMetaTileID()] != null) return GregTech_API.mMetaTileList[getBaseMetaTileEntity().getMetaTileID()].getMetaName(); return "";}
	@Override
	public int getInventoryStackLimit() {return 64;}
	@Override
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {return getBaseMetaTileEntity().isValidSlot(aIndex);}
	
	@Override
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
	
	@Override
	public int[] getAccessibleSlotsFromSide(int aSide) {
		ArrayList<Integer> tList = new ArrayList<Integer>();
		for (int i = 0; i < getSizeInventory(); i++) if (isValidSlot(i)) tList.add(i);
		int[] rArray = new int[tList.size()];
		for (int i = 0; i < rArray.length; i++) rArray[i] = tList.get(i);
		return rArray;
	}
	
	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		return isValidSlot(aIndex) && aStack != null && (mInventory[aIndex] == null || GT_Utility.areStacksEqual(aStack, mInventory[aIndex])) && allowPutStack(aIndex, (byte)aSide, aStack);
	}
	
	@Override
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
    	if (getBaseMetaTileEntity().hasSteamEngineUpgrade() && GT_ModHandler.isSteam(aFluid) && aFluid.amount > 1) {
        	int tSteam = Math.min(aFluid.amount/2, getBaseMetaTileEntity().getSteamCapacity() - getBaseMetaTileEntity().getStoredSteam());
	    	if (tSteam > 0) {
	        	if (doFill) getBaseMetaTileEntity().increaseStoredSteam(tSteam, true);
	    		return tSteam*2;
	    	}
    	} else {
        	return fill_default(aSide, aFluid, doFill);
    	}
		return 0;
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
	public boolean doTickProfilingMessageDuringThisTick() {
		return doTickProfilingInThisTick;
	}
	
	@Override
	public void onInventoryChanged() {
		//
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}
	
	@Override
	public void openChest() {
		//
	}
	
	@Override
	public void closeChest() {
		//
	}
	
	@Override
	public float getExplosionResistance(byte aSide) {
		return 10.0F;
	}
	
	@Override
	public ItemStack[] getRealInventory() {
		return mInventory;
	}
}