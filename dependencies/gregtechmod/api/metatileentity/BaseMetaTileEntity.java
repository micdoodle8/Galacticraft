package gregtechmod.api.metatileentity;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.interfaces.IMetaTileEntity;
import gregtechmod.api.util.GT_CoverBehavior;
import gregtechmod.api.util.GT_Log;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This is the main TileEntity for EVERYTHING.
 */
public class BaseMetaTileEntity extends BaseTileEntity implements IGregTechTileEntity {
	public static volatile int VERSION = 402;
	
	protected MetaTileEntity mMetaTileEntity;
	protected int mStoredMJ = 0, mStoredEnergy = 0, mStoredSteam = 0, mAverageEUInputIndex = 0, mAverageEUOutputIndex = 0;
	protected boolean mIsAddedToEnet = false, mReleaseEnergy = false;
	protected double mRestJoules = 0.0;
	protected int[] mAverageEUInput = new int[] {0,0,0,0,0}, mAverageEUOutput = new int[] {0,0,0,0,0};
	
	private boolean[] mActiveEUInputs = new boolean[] {false, false, false, false, false, false}, mActiveEUOutputs = new boolean[] {false, false, false, false, false, false};
	private byte[] mSidedRedstone = new byte[] {15,15,15,15,15,15};
	private int[] mCoverSides = new int[] {0,0,0,0,0,0}, mCoverData = new int[] {0,0,0,0,0,0};
	private boolean mRunningThroughTick = false, mInputDisabled = false, mOutputDisabled = false, mLockUpgrade = false, mActive = false, mRedstone = false, mWorkUpdate = false, mSteamConverter = false, mMJConverter = false, mInventoryChanged = false, mWorks = true, mNeedsUpdate = true, mNeedsBlockUpdate = true, mSendClientData = false, oRedstone = false;
	private byte mColor = 0, oColor = 0, mStrongRedstone = 0, oRedstoneData = 63, oTextureData = 0, oUpdateData = 0, oLightValue = 0, mLightValue = 0, mRSEnergyCells = 0, mSteamTanks = 0, mOverclockers = 0, mTransformers = 0, mOtherUpgrades = 0, mFacing = 0, oFacing = 0, mWorkData = 0;
	private int mDisplayErrorCode = 0, oOutput = 0, oX = 0, oY = 0, oZ = 0, mUpgradedStorage = 0;
	private short mID = 0;
	private long mTickTimer = 0;
	private String mOwnerName = "";
	
	public BaseMetaTileEntity() {}
	
	@Override
    public void writeToNBT(NBTTagCompound aNBT) {
		try {
			super.writeToNBT(aNBT);
    	} catch(Throwable e) {
			GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}
		try {
	        aNBT.setInteger		("mID"				, mID);
	        aNBT.setInteger		("mStoredMJ"		, mStoredMJ);
	        aNBT.setInteger		("mStoredSteam"		, mStoredSteam);
	        aNBT.setInteger		("mStoredEnergy"	, mStoredEnergy);
	        aNBT.setInteger		("mUpgradedStorage"	, mUpgradedStorage);
	        aNBT.setIntArray	("mCoverData"		, mCoverData);
	        aNBT.setIntArray	("mCoverSides"		, mCoverSides);
	    	aNBT.setByteArray	("mRedstoneSided"	, mSidedRedstone);
	        aNBT.setByte		("mColor"			, mColor);
	        aNBT.setByte		("mLightValue"		, mLightValue);
	        aNBT.setByte		("mOverclockers"	, mOverclockers);
	        aNBT.setByte		("mTransformers"	, mTransformers);
	        aNBT.setByte		("mRSEnergyCells"	, mRSEnergyCells);
	        aNBT.setByte		("mSteamTanks"		, mSteamTanks);
	        aNBT.setByte		("mOtherUpgrades"	, mOtherUpgrades);
	        aNBT.setByte		("mWorkData"		, mWorkData);
	        aNBT.setByte		("mStrongRedstone"	, mStrongRedstone);
	        aNBT.setShort		("mFacing"			, getFrontFacing());
	        aNBT.setString		("mOwnerName"		, mOwnerName);
	    	aNBT.setBoolean		("mLockUpgrade"		, mLockUpgrade);
	    	aNBT.setBoolean		("mMJConverter"		, mMJConverter);
	    	aNBT.setBoolean		("mSteamConverter"	, mSteamConverter);
	    	aNBT.setBoolean		("mActive"			, mActive);
	    	aNBT.setBoolean		("mRedstone"		, mRedstone);
	    	aNBT.setBoolean		("mWorks"			, !mWorks);
	    	aNBT.setBoolean		("mInputDisabled"	, mInputDisabled);
	    	aNBT.setBoolean		("mOutputDisabled"	, mOutputDisabled);
	    	aNBT.setDouble		("mRestJoules"		, mRestJoules);
		} catch(Throwable e) {
			GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}
		try {
	    	if (hasValidMetaTileEntity()) {
	            NBTTagList tItemList = new NBTTagList();
	            for (int i = 0; i < mMetaTileEntity.getRealInventory().length; i++) {
	                ItemStack tStack = mMetaTileEntity.getRealInventory()[i];
	                if (tStack != null) {
	                    NBTTagCompound tTag = new NBTTagCompound();
	                    tTag.setInteger("IntSlot", i);
	                    tStack.writeToNBT(tTag);
	                    tItemList.appendTag(tTag);
	                }
	            }
	            aNBT.setTag("Inventory", tItemList);
	            
	    		try {
	    			mMetaTileEntity.saveNBTData(aNBT);
	    		} catch(Throwable e) {
	    			GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
	    			e.printStackTrace(GT_Log.err);
	    		}
	    	}
    	} catch(Throwable e) {
			GT_Log.err.println("Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould've been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			e.printStackTrace(GT_Log.err);
		}
    }
	
	@Override
	public void readFromNBT(NBTTagCompound aNBT) {
		super.readFromNBT(aNBT);
		setInitialValuesAsNBT(aNBT, (short)0);
    }
	
	@Override
	public void setInitialValuesAsNBT(NBTTagCompound aNBT, short aID) {
		if (aNBT == null) {
			if (aID>0) mID=aID; else mID=mID>0?mID:0;
			if (mID!=0) createNewMetatileEntity(mID);
			mSidedRedstone = new byte[0];
		} else {
	        if (aID<=0) 	mID	= (short)aNBT.getInteger	("mID"); else mID = aID;
	        mStoredMJ			= aNBT.getInteger	("mStoredMJ");
	        mStoredSteam		= aNBT.getInteger	("mStoredSteam");
	        mStoredEnergy		= aNBT.getInteger	("mStoredEnergy");
	        mUpgradedStorage	= aNBT.getInteger	("mUpgradedStorage")+aNBT.getByte("mBatteries")*10000 + aNBT.getByte("mLiBatteries")*100000;
	    	mCoverSides 		= aNBT.getIntArray	("mCoverSides");
	        mCoverData 			= aNBT.getIntArray	("mCoverData");
	        mSidedRedstone		= aNBT.getByteArray ("mRedstoneSided");
	        mColor				= aNBT.getByte		("mColor");
	        mLightValue			= aNBT.getByte		("mLightValue");
	        mSteamTanks			= aNBT.getByte		("mSteamTanks");
	        mRSEnergyCells		= aNBT.getByte		("mRSEnergyCells");
	        mOverclockers		= aNBT.getByte		("mOverclockers");
	        mTransformers		= aNBT.getByte		("mTransformers");
	        mWorkData			= aNBT.getByte		("mWorkData");
	        mStrongRedstone		= aNBT.getByte		("mStrongRedstone");
	        mFacing = oFacing	= (byte)aNBT.getShort("mFacing");
	        mOwnerName			= aNBT.getString	("mOwnerName");
	        mLockUpgrade		= aNBT.getBoolean	("mLockUpgrade");
	        mMJConverter		= aNBT.getBoolean	("mMJConverter");
	        mSteamConverter		= aNBT.getBoolean	("mSteamConverter");
	    	mActive				= aNBT.getBoolean	("mActive");
	    	mRedstone			= aNBT.getBoolean	("mRedstone");
	    	mWorks				=!aNBT.getBoolean	("mWorks");
	    	mInputDisabled		= aNBT.getBoolean	("mInputDisabled");
	    	mOutputDisabled		= aNBT.getBoolean	("mOutputDisabled");
	    	mOtherUpgrades		= (byte)(aNBT.getByte("mOtherUpgrades")+aNBT.getByte("mBatteries")+aNBT.getByte("mLiBatteries"));
	    	mRestJoules			= aNBT.getDouble	("mRestJoules");
	    	
	    	if (mID!=0 && createNewMetatileEntity(mID)) {
	            NBTTagList tItemList = aNBT.getTagList("Inventory");
	            for (int i = 0; i < tItemList.tagCount(); i++) {
	                NBTTagCompound tTag = (NBTTagCompound)tItemList.tagAt(i);
	                int tSlot = tTag.getInteger("IntSlot");
	                if (tSlot >= 0 && tSlot < mMetaTileEntity.getRealInventory().length) {
	                	mMetaTileEntity.getRealInventory()[tSlot] = ItemStack.loadItemStackFromNBT(tTag);
	                }
	            }
	            
	    		try {
	    			mMetaTileEntity.loadNBTData(aNBT);
	        	} catch(Throwable e) {
	        		GT_Log.err.println("Encountered Exception while loading MetaTileEntity, the Server should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
	        		e.printStackTrace(GT_Log.err);
	        	}
			}
		}
		
    	if (mCoverData.length != 6)    	mCoverData	   = new int[] { 0, 0, 0, 0, 0, 0};
    	if (mCoverSides.length != 6)    mCoverSides    = new int[] { 0, 0, 0, 0, 0, 0};
        if (mSidedRedstone.length != 6) if (hasValidMetaTileEntity() && mMetaTileEntity.hasSidedRedstoneOutputBehavior()) mSidedRedstone = new byte[] {0,0,0,0,0,0}; else mSidedRedstone = new byte[] {15,15,15,15,15,15};
	}
	
	private boolean createNewMetatileEntity(short aID) {
		if (aID < 16 || aID >= GregTech_API.MAXIMUM_METATILE_IDS || GregTech_API.mMetaTileList[aID] == null) {
			GT_Log.err.println("MetaID " + aID + " not loadable => locking TileEntity!");
		} else {
			if (aID != 0) {
				if (hasValidMetaTileEntity()) mMetaTileEntity.setBaseMetaTileEntity(null);
				GregTech_API.mMetaTileList[aID].newMetaEntity(this).setBaseMetaTileEntity(this);
	    		mTickTimer = 0;
				mID = aID;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Used for ticking special BaseMetaTileEntities, which need that for Energy Conversion
	 * It's called right before onPostTick()
	 */
	public void updateStatus() {
		
	}
	
	/**
	 * Called when trying to charge Items
	 */
	public void chargeItem(ItemStack aStack) {
		decreaseStoredEU(GT_ModHandler.chargeElectricItem(aStack, getStoredEU(), mMetaTileEntity.getOutputTier(), false, false), true);
	}
	
	/**
	 * Called when trying to discharge Items
	 */
	public void dischargeItem(ItemStack aStack) {
		increaseStoredEnergyUnits(GT_ModHandler.dischargeElectricItem(aStack, getEUCapacity() - getStoredEU(), mMetaTileEntity.getInputTier(), false, false, false), true);
	}
	
	@Override
    public void updateEntity() {
		
    	if (!hasValidMetaTileEntity()) {
    		if (mMetaTileEntity == null) {
	    		return;
	    	} else {
	    		mMetaTileEntity.setBaseMetaTileEntity(this);
	    	}
    	}
    	
    	mRunningThroughTick = true;
    	
    	try {
    	
    	if (hasValidMetaTileEntity()) {
    	    if (mTickTimer++==0) {
	    		oX = getXCoord();
	    		oY = getYCoord();
	    		oZ = getZCoord();
	    		if (isServerSide()) for (byte i = 0; i < 6; i++) if (getCoverIDAtSide(i)!=0) if (!mMetaTileEntity.allowCoverOnSide(i, getCoverIDAtSide(i))) dropCover(i, i, true);
	    		
	    	    try {
	    	    	mMetaTileEntity.onFirstTick();
	    	    } catch(Throwable e) {
	    	    	GT_Log.err.println("Encountered Exception while ticking MetaTileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
	    	    	e.printStackTrace(GT_Log.err);
	    	    }
	    		
        		if (!hasValidMetaTileEntity()) {
        	    	mRunningThroughTick = false;
        			return;
        		}
    	    }
    	    
	    	if (mLightValue != oLightValue) {
	    		//getWorld().setLightValue(EnumSkyBlock.Block, getXCoord(), getYCoord(), getZCoord(), mLightValue);
	    		getWorld().updateLightByType(EnumSkyBlock.Block, getXCoord()  , getYCoord()  , getZCoord()  );
	    		getWorld().updateLightByType(EnumSkyBlock.Block, getXCoord()+1, getYCoord()  , getZCoord()  );
	    		getWorld().updateLightByType(EnumSkyBlock.Block, getXCoord()-1, getYCoord()  , getZCoord()  );
	    		getWorld().updateLightByType(EnumSkyBlock.Block, getXCoord()  , getYCoord()+1, getZCoord()  );
	    		getWorld().updateLightByType(EnumSkyBlock.Block, getXCoord()  , getYCoord()-1, getZCoord()  );
	    		getWorld().updateLightByType(EnumSkyBlock.Block, getXCoord()  , getYCoord()  , getZCoord()+1);
	    		getWorld().updateLightByType(EnumSkyBlock.Block, getXCoord()  , getYCoord()  , getZCoord()-1);
	    		oLightValue = mLightValue;
		    	issueTextureUpdate();
	    	}
	    	
    	    if (isClientSide()) {
    	    	if (mColor != oColor) {
    	    		oColor = mColor;
    		    	issueTextureUpdate();
    	    	}
    	    	
    	    	if (mNeedsUpdate) {
    			    getWorld().markBlockForRenderUpdate(getXCoord(), getYCoord(), getZCoord());
    			    mNeedsUpdate = false;
    	    	}
    	    }
    	    
    	    if (isServerSide()) {
    	    	for (byte i = 0; i < 6; i++) {
    	    		short tCoverTickRate = getCoverBehaviorAtSide(i).getTickRate(i, getCoverIDAtSide(i), mCoverData[i], this);
    	    		if (tCoverTickRate > 0 && mTickTimer % tCoverTickRate == 0) {
    	    			try {
    	    				mCoverData[i] = getCoverBehaviorAtSide(i).doCoverThings(i, getInputRedstoneSignal(i), getCoverIDAtSide(i), mCoverData[i], this);
	    	    	    } catch(Throwable e) {
	    	    	    	GT_Log.err.println("Encountered Exception while ticking Cover, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
	    	    	    	e.printStackTrace(GT_Log.err);
	    	    	    }
    	    		}
    	    	}
    	    	
    		    if (++mAverageEUInputIndex  >= mAverageEUInput.length ) mAverageEUInputIndex  = 0;
    		    if (++mAverageEUOutputIndex >= mAverageEUOutput.length) mAverageEUOutputIndex = 0;
    		    
    		    mAverageEUInput [mAverageEUInputIndex ] = 0;
    		    mAverageEUOutput[mAverageEUOutputIndex] = 0;
    	    }
    	    
    	    try {
    	    	mMetaTileEntity.onPreTick();
    	    } catch(Throwable e) {
    	    	GT_Log.err.println("Encountered Exception while ticking MetaTileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
    	    	e.printStackTrace(GT_Log.err);
    	    }
    	    
    		if (!hasValidMetaTileEntity()) {
    	    	mRunningThroughTick = false;
    			return;
    		}
    		
    	    if (isServerSide()) {
    	    	if (mRedstone != oRedstone || mTickTimer == 10) {
    	    		oRedstone = mRedstone;
    	    		issueBlockUpdate();
    	    	}
    	    	
    	    	if (getXCoord() != oX || getYCoord() != oY || getZCoord() != oZ) {
    	    		oX = getXCoord();
    	    		oY = getYCoord();
    	    		oZ = getZCoord();
    		    	issueClientUpdate();
    	    	}
    	    	
    		    if (mFacing != oFacing) {
    		    	oFacing = mFacing;
    		    	for (byte i = 0; i < 6; i++) if (getCoverIDAtSide(i)!=0) if (!mMetaTileEntity.allowCoverOnSide(i, getCoverIDAtSide(i))) dropCover(i, i, true);
    	    		issueBlockUpdate();
    		    }
    		    
    		    if (getOutputVoltage() != oOutput) {
    		    	oOutput = getOutputVoltage();
    		    }
    		    
	    	    if (mMetaTileEntity.isElectric() && (mMetaTileEntity.isEnetOutput()||mMetaTileEntity.isEnetInput())) {
	    	    	for (byte i = 0; i < 6; i++) {
	    	    		boolean
	    	    		temp = isEnergyInputSide(i);
	    	    		if (temp != mActiveEUInputs[i]) {
	    	    			mActiveEUInputs[i] = temp;
	    	    			if (mIsAddedToEnet) {
	    	    				mIsAddedToEnet = !GT_ModHandler.removeTileFromEnet(getWorld(), this);
	    	    			}
	    	    		}
	    	    		temp = isEnergyOutputSide(i);
	    	    		if (temp != mActiveEUOutputs[i]) {
	    	    			mActiveEUOutputs[i] = temp;
	    	    			if (mIsAddedToEnet) {
	    	    				mIsAddedToEnet = !GT_ModHandler.removeTileFromEnet(getWorld(), this);
	    	    			}
	    	    		}
	    	    	}
	    	    	if (!mIsAddedToEnet) mIsAddedToEnet = GT_ModHandler.addTileToEnet(getWorld(), this);
	    	    }
    		    
	    	    if (mIsAddedToEnet && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput() && getOutputVoltage() > 0) {
	    	    	for (int i = 0; i < getOutputAmperage() && getStoredEU() >= getOutputVoltage() + mMetaTileEntity.getMinimumStoredEU(); i++) {
	    	    		int tEU = GT_ModHandler.emitEnergyToEnet(getOutputVoltage(), getWorld(), this) - getOutputVoltage();
	    	    		mAverageEUOutput[mAverageEUOutputIndex] -= tEU;
	    	    		decreaseStoredEU(-tEU, true);
	    	    	}
	    	    }
    	    	
    	        try {
	    	        if (getEUCapacity() > 0) {
		    	        if (GregTech_API.sMachineFireExplosions && getRandomNumber(1000) == 0 && getBlockIDAtSide((byte)getRandomNumber(6)) == Block.fire.blockID) {
		        		    doEnergyExplosion();
		    	        }
		    	        
		        		if (!hasValidMetaTileEntity()) {
		        	    	mRunningThroughTick = false;
		        			return;
		        		}
		        		
		    	        if (getCoverIDAtSide((byte)1) == 0) {
			    	       	if (getWorld().getPrecipitationHeight(getXCoord(), getZCoord()) - 2 < getYCoord()) {
			        	       	if (GregTech_API.sMachineRainExplosions && getWorld().isRaining() && getRandomNumber(1000) == 0 && getBiome(getXCoord(), getZCoord()).rainfall > 0) {
			            	    	if (getRandomNumber(10)==0) {
			            	    		doEnergyExplosion();
			            	    	} else {
			            	    		setOnFire();
			            	    	}
			        	       	}
				        		if (!hasValidMetaTileEntity()) {
				        	    	mRunningThroughTick = false;
				        			return;
				        		}
			        	       	if (GregTech_API.sMachineThunderExplosions && getWorld().isThundering() && getRandomNumber(2500) == 0) {
			            	    	doEnergyExplosion();
			        	       	}
			    	       	}
		    	        }
	    	        }
    	        } catch(Throwable e) {
    	        	GT_Log.err.println("Encountered Exception while checking for Explosion conditions");
    	        	e.printStackTrace(GT_Log.err);
    	        }
    	        
        		if (!hasValidMetaTileEntity()) {
        	    	mRunningThroughTick = false;
        			return;
        		}
        		
        	    try {
		    	    if (mMetaTileEntity.dechargerSlotCount() > 0 && getStoredEU() < getEUCapacity()) {
			    	    for (int j = 0; j < mMetaTileEntity.getDechargeCyles(); j++) {
			    	        for (int i = mMetaTileEntity.dechargerSlotStartIndex(); i < mMetaTileEntity.dechargerSlotCount()+mMetaTileEntity.dechargerSlotStartIndex(); i++) {
			    		        if (mMetaTileEntity.mInventory[i] != null && getStoredEU() < getEUCapacity()) {
			    		        	dischargeItem(mMetaTileEntity.mInventory[i]);
			    		        	mInventoryChanged = true;
			    		        }
			    	        }
			    	    }
		    	    }
		    	    
		    	    if (mMetaTileEntity.rechargerSlotCount() > 0 && getStoredEU() > 0) {
			        	for (int j = 0; j < mMetaTileEntity.getChargeCyles(); j++) {
		    		        for (int i = mMetaTileEntity.rechargerSlotStartIndex(); i < mMetaTileEntity.rechargerSlotCount()+mMetaTileEntity.rechargerSlotStartIndex(); i++) {
		    			        if (getStoredEU() > 0 && mMetaTileEntity.mInventory[i] != null) {
		    			        	chargeItem(mMetaTileEntity.mInventory[i]);
		    			        	mInventoryChanged = true;
		    			        }
		    		        }
		    	        }
	        	    }
        	    } catch(Throwable e) {
        	    	GT_Log.err.println("Encountered Exception while charging/decharging Items");
        	    	e.printStackTrace(GT_Log.err);
        	    }
   	        }
    	    
    	    try {
    	    	updateStatus();
    	    } catch(Throwable e) {
    	    	GT_Log.err.println("Encountered Exception in Cross Mod Energy Systems");
    	    	e.printStackTrace(GT_Log.err);
    	    }
    	    
    		if (!hasValidMetaTileEntity()) {
    	    	mRunningThroughTick = false;
    			return;
    		}
    		
    	    try {
    	    	mMetaTileEntity.onPostTick();
    	    } catch(Throwable e) {
    	    	GT_Log.err.println("Encountered Exception while ticking MetaTileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
    	    	e.printStackTrace(GT_Log.err);
    	    }
    	    
    		if (!hasValidMetaTileEntity()) {
    	    	mRunningThroughTick = false;
    			return;
    		}
    		
        	if (isServerSide()) {
        		if (getTimer() % 10 == 0) {
        		    if (mSendClientData) {
        		    	GT_Utility.sendPacketToAllPlayersInRange(getWorld(), getDescriptionPacket(), getXCoord(), getZCoord());
	    		    	mSendClientData = false;
        	    	}
        		}
        		
        		if (getTimer() > 10) {
	    		    byte tData;
	    		    
	    		    tData = (byte)((getFrontFacing()&7)|(mActive?8:0)|(mRedstone?16:0)|(mLockUpgrade?32:0));
	    		    if (tData != oTextureData) sendBlockEvent((byte)0, oTextureData = tData);
	    		    tData = mMetaTileEntity.getUpdateData();
	    		    if (tData != oUpdateData) sendBlockEvent((byte)1, oUpdateData = tData);
	    		    tData = mColor;
	    		    if (tData != oColor) sendBlockEvent((byte)2, oColor = tData);
	    		    tData = mLightValue;
	    		    if (tData != oLightValue) sendBlockEvent((byte)7, oLightValue = tData);
	    		    tData = (byte)(((mSidedRedstone[0]>0)?1:0)|((mSidedRedstone[1]>0)?2:0)|((mSidedRedstone[2]>0)?4:0)|((mSidedRedstone[3]>0)?8:0)|((mSidedRedstone[4]>0)?16:0)|((mSidedRedstone[5]>0)?32:0));
	    		    if (tData != oRedstoneData) sendBlockEvent((byte)3, oRedstoneData = tData);
        		}
        		
    	    	if (mNeedsBlockUpdate) {
    		    	getWorld().notifyBlocksOfNeighborChange(getXCoord(), getYCoord(), getZCoord(), getBlockIDOffset(0, 0, 0));
    	    		mNeedsBlockUpdate = false;
    	    	}
    	    }
    	}
    	
    	} catch(Throwable e) {
    		GT_Log.err.println("Encountered Exception while ticking TileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
    		e.printStackTrace(GT_Log.err);
    	}
    	
    	mWorkUpdate = mInventoryChanged = mRunningThroughTick = false;
    }
	
	@Override
    public Packet getDescriptionPacket() {
		Packet250CustomPayload rPacket = new Packet250CustomPayload();
		rPacket.channel = GregTech_API.TILEENTITY_PACKET_CHANNEL;
        rPacket.isChunkDataPacket = true;
        
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput();
        
        tOut.writeInt(getXCoord());
        tOut.writeShort(getYCoord());
        tOut.writeInt(getZCoord());
        
        tOut.writeShort(mID);
        tOut.writeInt(mCoverSides[0]);
        tOut.writeInt(mCoverSides[1]);
        tOut.writeInt(mCoverSides[2]);
        tOut.writeInt(mCoverSides[3]);
        tOut.writeInt(mCoverSides[4]);
        tOut.writeInt(mCoverSides[5]);
        
        tOut.writeByte(oTextureData = (byte)((getFrontFacing()&7) | (mActive?8:0) | (mRedstone?16:0) | (mLockUpgrade?32:0)));
        tOut.writeByte(oUpdateData = hasValidMetaTileEntity()?mMetaTileEntity.getUpdateData():0);
        tOut.writeByte(oRedstoneData = (byte)(((mSidedRedstone[0]>0)?1:0)|((mSidedRedstone[1]>0)?2:0)|((mSidedRedstone[2]>0)?4:0)|((mSidedRedstone[3]>0)?8:0)|((mSidedRedstone[4]>0)?16:0)|((mSidedRedstone[5]>0)?32:0)));
        tOut.writeByte(oColor = mColor);
        
        rPacket.data = tOut.toByteArray();
        rPacket.length = rPacket.data.length;
        
        return rPacket;
    }
	
	public final void receiveMetaTileEntityData(short aID, int aCover0, int aCover1, int aCover2, int aCover3, int aCover4, int aCover5, byte aTextureData, byte aUpdateData, byte aRedstoneData, byte aColorData) {
    	issueTextureUpdate();
		if (mID != aID && aID > 0) {
	    	mID = aID;
	    	createNewMetatileEntity(mID);
		}
		
		mCoverSides[0] = aCover0;
		mCoverSides[1] = aCover1;
		mCoverSides[2] = aCover2;
		mCoverSides[3] = aCover3;
		mCoverSides[4] = aCover4;
		mCoverSides[5] = aCover5;
		
		receiveClientEvent(0, aTextureData);
		receiveClientEvent(1, aUpdateData);
		receiveClientEvent(3, aRedstoneData);
		receiveClientEvent(2, aColorData);
	}
	
    @Override
    public boolean receiveClientEvent(int aEventID, int aValue) {
		super.receiveClientEvent(aEventID, aValue);
		
		if (hasValidMetaTileEntity()) {
			try {
				mMetaTileEntity.receiveClientEvent((byte)aEventID, (byte)aValue);
			} catch(Throwable e) {
				GT_Log.err.println("Encountered Exception while receiving Data from the Server, the Client should've been crashed by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
				e.printStackTrace(GT_Log.err);
			}
		}
		
		if (isClientSide()) {
	    	issueTextureUpdate();
			switch(aEventID) {
			case  0:
				mFacing			= (byte)(aValue& 7);
				mActive			= ((aValue& 8) != 0);
				mRedstone		= ((aValue&16) != 0);
				mLockUpgrade	= ((aValue&32) != 0);
				break;
			case  1:
				if (hasValidMetaTileEntity()) mMetaTileEntity.onValueUpdate((byte)aValue);
				break;
			case  2:
				mColor = (byte)aValue;
				break;
			case  3:
				mSidedRedstone[0] = (byte)((aValue& 1)>0?15:0);
				mSidedRedstone[1] = (byte)((aValue& 2)>0?15:0);
				mSidedRedstone[2] = (byte)((aValue& 4)>0?15:0);
				mSidedRedstone[3] = (byte)((aValue& 8)>0?15:0);
				mSidedRedstone[4] = (byte)((aValue&16)>0?15:0);
				mSidedRedstone[5] = (byte)((aValue&32)>0?15:0);
				break;
			case  4:
		    	if (hasValidMetaTileEntity() && mTickTimer > 20) mMetaTileEntity.doSound((byte)aValue, getXCoord()+0.5, getYCoord()+0.5, getZCoord()+0.5);
		    	break;
			case  5:
				if (hasValidMetaTileEntity() && mTickTimer > 20) mMetaTileEntity.startSoundLoop((byte)aValue, getXCoord()+0.5, getYCoord()+0.5, getZCoord()+0.5);
		    	break;
			case  6:
				if (hasValidMetaTileEntity() && mTickTimer > 20) mMetaTileEntity.stopSoundLoop((byte)aValue, getXCoord()+0.5, getYCoord()+0.5, getZCoord()+0.5);
	    		break;
			case  7:
				mLightValue = (byte)aValue;
				break;
			}
		}
		return true;
	}
	
	public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aLogLevel) {
		ArrayList<String> tList = new ArrayList<String>();
		if (aLogLevel > 2) {
			tList.add("Meta-ID: " + mID + (hasValidMetaTileEntity()?" valid":" invalid") + (mMetaTileEntity==null?" MetaTileEntity == null!":" "));
		}
		if (aLogLevel > 1) {
			tList.add("Is" + (mMetaTileEntity.isAccessAllowed(aPlayer)?" ":" not ") + "accessible for you");
		}
		if (aLogLevel > 0) {
			if (getMJCapacity() > 0 && hasMJConverterUpgrade()) tList.add(getStoredMJ() + " of " + getMJCapacity() + " MJ");
			if (getSteamCapacity() > 0 && hasSteamEngineUpgrade()) tList.add(getStoredSteam() + " of " + getSteamCapacity() + " Steam");
			tList.add("Machine is " + (mActive?"active":"inactive"));
		}
		return mMetaTileEntity.getSpecialDebugInfo(aPlayer, aLogLevel, tList);
	}
	
	@Override public void issueTextureUpdate() {mNeedsUpdate = true;}
	@Override public void issueBlockUpdate() {mNeedsBlockUpdate = true;}
	@Override public void issueClientUpdate() {mSendClientData = true;}
	@Override public void issueCoverUpdate(byte aSide) {issueClientUpdate();}
	
	@Override public byte getStrongestRedstone() {return (byte)Math.max(getInternalInputRedstoneSignal((byte)0), Math.max(getInternalInputRedstoneSignal((byte)1), Math.max(getInternalInputRedstoneSignal((byte)2), Math.max(getInternalInputRedstoneSignal((byte)3), Math.max(getInternalInputRedstoneSignal((byte)4), getInternalInputRedstoneSignal((byte)5))))));}
	
	@Override public boolean getRedstone() {return getRedstone((byte)0)||getRedstone((byte)1)||getRedstone((byte)2)||getRedstone((byte)3)||getRedstone((byte)4)||getRedstone((byte)5);}
	@Override public boolean getRedstone(byte aSide) {return getInternalInputRedstoneSignal(aSide) > 0;}
    
    public Icon getCoverTexture(byte aSide) {return GregTech_API.sCovers.get(getCoverIDAtSide(aSide));}
	
	@Override public String getMainInfo() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getMainInfo(); return "";}
	@Override public String getSecondaryInfo() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getSecondaryInfo(); return "";}
	@Override public String getTertiaryInfo() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getTertiaryInfo(); return "";}
	@Override public boolean isGivingInformation() {if (hasValidMetaTileEntity()) return mMetaTileEntity.isGivingInformation(); return false;}
	@Override public boolean isValidFacing(byte aSide) {if (hasValidMetaTileEntity()) return mMetaTileEntity.isFacingValid(aSide); return false;}
	@Override public byte getBackFacing() {return GT_Utility.getOppositeSide(getFrontFacing());}
	@Override public byte getFrontFacing() {return mFacing;}
	@Override public void setFrontFacing(byte aFacing) {if (isValidFacing(aFacing)) {mFacing = aFacing; mMetaTileEntity.onFacingChange(); onMachineBlockUpdate();}}
	@Override public int getSizeInventory() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getSizeInventory(); else return 0;}
	@Override public ItemStack getStackInSlot(int aIndex) {if (hasValidMetaTileEntity()) return mMetaTileEntity.getStackInSlot(aIndex); return null;}
	@Override public void setInventorySlotContents(int aIndex, ItemStack aStack) {mInventoryChanged = true; if (hasValidMetaTileEntity()) mMetaTileEntity.setInventorySlotContents(aIndex, aStack);}
	@Override public String getInvName() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getInvName(); if (GregTech_API.mMetaTileList[mID] != null) return GregTech_API.mMetaTileList[mID].getInvName(); return "";}
	@Override public int getInventoryStackLimit() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getInventoryStackLimit(); return 64;}
	@Override public void openChest()  {if (hasValidMetaTileEntity()) mMetaTileEntity.onOpenGUI();}
	@Override public void closeChest() {if (hasValidMetaTileEntity()) mMetaTileEntity.onCloseGUI();}
	@Override public boolean isUseableByPlayer(EntityPlayer aPlayer) {return hasValidMetaTileEntity() && playerOwnsThis(aPlayer, false) && mTickTimer>40 && getTileEntityOffset(0, 0, 0) == this && aPlayer.getDistanceSq(getXCoord() + 0.5, getYCoord() + 0.5, getZCoord() + 0.5) < 64 && mMetaTileEntity.isAccessAllowed(aPlayer);}
	@Override public void validate() {super.validate(); mTickTimer = 0;}
    @Override public void invalidate() {tileEntityInvalid = false; if (hasValidMetaTileEntity()) {mMetaTileEntity.onRemoval(); mMetaTileEntity.setBaseMetaTileEntity(null);} GT_ModHandler.removeTileFromEnet(getWorld(), this); mIsAddedToEnet = false; super.invalidate();}
    @Override public void onChunkUnload() {GT_ModHandler.removeTileFromEnet(getWorld(), this); mIsAddedToEnet = false; super.onChunkUnload();}
    @Override public boolean isInvNameLocalized() {return false;}
    @Override public ItemStack getStackInSlotOnClosing(int slot) {ItemStack stack = getStackInSlot(slot); if (stack != null) setInventorySlotContents(slot, null); return stack;}
    @Override public void onMachineBlockUpdate() {if (hasValidMetaTileEntity()) mMetaTileEntity.onMachineBlockUpdate();}
	@Override public int getProgress() {return hasValidMetaTileEntity()?mMetaTileEntity.getProgresstime():0;}
	@Override public int getMaxProgress() {return hasValidMetaTileEntity()?mMetaTileEntity.maxProgresstime():0;}
	@Override public boolean increaseProgress(int aProgressAmountInTicks) {return hasValidMetaTileEntity()?mMetaTileEntity.increaseProgress(aProgressAmountInTicks)!=aProgressAmountInTicks:false;}
	@Override public boolean hasThingsToDo() {return getMaxProgress()>0;}
	@Override public void enableWorking() {if (!mWorks) mWorkUpdate = true; mWorks = true;}
	@Override public void disableWorking() {mWorks = false;}
	@Override public boolean isAllowedToWork() {return mWorks;}
	@Override public boolean hasWorkJustBeenEnabled() {return mWorkUpdate;}
	@Override public void setWorkDataValue(byte aValue) {mWorkData = aValue;}
	@Override public byte getWorkDataValue() {return mWorkData;}
    @Override public int getMetaTileID() {return mID;}
	@Override public boolean isActive() {return mActive;}
    @Override public void setActive(boolean aActive) {mActive = aActive;}
	@Override public long getTimer() {return mTickTimer;}
	@Override public boolean decreaseStoredEnergyUnits(int aEnergy, boolean aIgnoreTooLessEnergy) {if (!hasValidMetaTileEntity()) return false; return decreaseStoredMJ(aEnergy, false) || decreaseStoredSteam(aEnergy, false) || decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy) || (aIgnoreTooLessEnergy && (decreaseStoredMJ(aEnergy, true) || decreaseStoredSteam(aEnergy, true)));}
	@Override public boolean increaseStoredEnergyUnits(int aEnergy, boolean aIgnoreTooMuchEnergy) {if (!hasValidMetaTileEntity()) return false; if (getStoredEU() < getEUCapacity() || aIgnoreTooMuchEnergy) {setStoredEU(mMetaTileEntity.getEUVar() + aEnergy); return true;} return false;}
	@Override public boolean inputEnergyFrom(byte aSide) {if (aSide == 6) return true; if (isServerSide()) return (aSide>=0&&aSide<6?mActiveEUInputs [aSide]:false)&&!mReleaseEnergy; return isEnergyInputSide (aSide);}
	@Override public boolean outputsEnergyTo(byte aSide) {if (aSide == 6) return true; if (isServerSide()) return (aSide>=0&&aSide<6?mActiveEUOutputs[aSide]:false)|| mReleaseEnergy; return isEnergyOutputSide(aSide);}
	@Override public int getOutputAmperage() {if (hasValidMetaTileEntity() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxEUPulses()==1&&mMetaTileEntity.isMultiplePacketsForTransformer()&&mTransformers>0?4:mMetaTileEntity.maxEUPulses(); return 0;}
	@Override public int getOutputVoltage() {if (hasValidMetaTileEntity() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput()) return mMetaTileEntity.maxEUOutput() * (mTransformers>0?(int)Math.pow(4, mTransformers-(mMetaTileEntity.isMultiplePacketsForTransformer()?1:0)):1); return 0;}
	@Override public int getInputVoltage() {if (hasValidMetaTileEntity() && mMetaTileEntity.isElectric()) return mMetaTileEntity.maxEUInput()*(int)Math.pow(4, mTransformers); return mMetaTileEntity.isElectric()?Integer.MAX_VALUE:0;}
	@Override public boolean increaseStoredMJ(int aEnergy, boolean aIgnoreTooMuchEnergy) {if (!hasValidMetaTileEntity()) return false; if (mMetaTileEntity.getMJVar() < getMJCapacity()	|| aIgnoreTooMuchEnergy) {setStoredMJ(mMetaTileEntity.getMJVar() + aEnergy); return true;} return false;}
	@Override public boolean increaseStoredSteam(int aEnergy, boolean aIgnoreTooMuchEnergy) {if (!hasValidMetaTileEntity()) return false; if (mMetaTileEntity.getSteamVar() < getSteamCapacity() || aIgnoreTooMuchEnergy) {setStoredSteam(mMetaTileEntity.getSteamVar() + aEnergy); return true;} return false;}
	@Override public String getDescription() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getDescription(); return "";}
    @Override public boolean isValidSlot(int aIndex) {if (hasValidMetaTileEntity()) return mMetaTileEntity.isValidSlot(aIndex); return false;}
    @Override public int getUniversalEnergyStored() {return Math.max(Math.max(getStoredEU(), getStoredMJ()), getStoredSteam());}
	@Override public int getUniversalEnergyCapacity() {return Math.max(Math.max(getEUCapacity(), getMJCapacity()), getSteamCapacity());}
	@Override public int getStoredMJ() {if (hasValidMetaTileEntity()) return Math.min(mMetaTileEntity.getMJVar(), getMJCapacity()); return 0;}
    @Override public int getMJCapacity() {if (hasValidMetaTileEntity()) return mMetaTileEntity.maxMJStore() + mRSEnergyCells * 100000; return 0;}
    @Override public int getStoredEU() {if (hasValidMetaTileEntity()) return Math.min(mMetaTileEntity.getEUVar(), getEUCapacity()); return 0;}
    @Override public int getEUCapacity() {if (hasValidMetaTileEntity()) return mMetaTileEntity.maxEUStore() + getUpgradeStorageVolume(); return 0;}
    @Override public int getStoredSteam() {if (hasValidMetaTileEntity()) return Math.min(mMetaTileEntity.getSteamVar(), getSteamCapacity()); return 0;}
    @Override public int getSteamCapacity() {if (hasValidMetaTileEntity()) return mMetaTileEntity.maxSteamStore() + mSteamTanks * 32000; return 0;}
    @Override public int getTextureIndex(byte aSide, byte aMeta) {if (hasValidMetaTileEntity()) return mMetaTileEntity.getTextureIndex(aSide, getFrontFacing(), mActive, getOutputRedstoneSignal(aSide)>0); return -2;}
    @Override public Icon getTextureIcon(byte aSide, byte aMeta) {Icon rIcon = getCoverTexture(aSide); if (rIcon != null) return rIcon; if (hasValidMetaTileEntity()) return mMetaTileEntity.getTextureIcon(aSide, getFrontFacing(), mActive, getOutputRedstoneSignal(aSide)>0); return null;}
    
    private boolean isEnergyInputSide(byte aSide)  {if (aSide >= 0 && aSide < 6) {if (!getCoverBehaviorAtSide(aSide).letsEnergyIn (aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this)) return false; if (isInvalid()||mReleaseEnergy) return false;			if (hasValidMetaTileEntity() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetInput ()) return mMetaTileEntity.isInputFacing (aSide);} return false;}
    private boolean isEnergyOutputSide(byte aSide) {if (aSide >= 0 && aSide < 6) {if (!getCoverBehaviorAtSide(aSide).letsEnergyOut(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this)) return false; if (isInvalid()||mReleaseEnergy) return mReleaseEnergy;	if (hasValidMetaTileEntity() && mMetaTileEntity.isElectric() && mMetaTileEntity.isEnetOutput()) return mMetaTileEntity.isOutputFacing(aSide);} return false;}
    
    protected boolean hasValidMetaTileEntity() {return mMetaTileEntity != null && mMetaTileEntity.getBaseMetaTileEntity() == this;}
    
    public boolean setStoredEU			(int aEnergy) {if (!hasValidMetaTileEntity()) return false; if (aEnergy < 0) aEnergy = 0; mMetaTileEntity.setEUVar		(aEnergy); return true;}
    public boolean setStoredMJ			(int aEnergy) {if (!hasValidMetaTileEntity()) return false; if (aEnergy < 0) aEnergy = 0; mMetaTileEntity.setMJVar		(aEnergy); return true;}
    public boolean setStoredSteam		(int aEnergy) {if (!hasValidMetaTileEntity()) return false; if (aEnergy < 0) aEnergy = 0; mMetaTileEntity.setSteamVar	(aEnergy); return true;}
    public boolean decreaseStoredEU		(int aEnergy, boolean aIgnoreTooLessEnergy)	{if (!hasValidMetaTileEntity()) return false; if (mMetaTileEntity.getEUVar()	- aEnergy >= 0	|| aIgnoreTooLessEnergy) {setStoredEU	(mMetaTileEntity.getEUVar()		- aEnergy); if (mMetaTileEntity.getEUVar()		< 0) {setStoredEU	(0); return false;} return true;} return false;}
    public boolean decreaseStoredMJ		(int aEnergy, boolean aIgnoreTooLessEnergy)	{if (!hasValidMetaTileEntity()) return false; if (mMetaTileEntity.getMJVar()	- aEnergy >= 0	|| aIgnoreTooLessEnergy) {setStoredMJ	(mMetaTileEntity.getMJVar()		- aEnergy); if (mMetaTileEntity.getMJVar() 		< 0) {setStoredMJ	(0); return false;} return true;} return false;}
    public boolean decreaseStoredSteam	(int aEnergy, boolean aIgnoreTooLessEnergy)	{if (!hasValidMetaTileEntity()) return false; if (mMetaTileEntity.getSteamVar()	- aEnergy >= 0	|| aIgnoreTooLessEnergy) {setStoredSteam(mMetaTileEntity.getSteamVar()	- aEnergy); if (mMetaTileEntity.getSteamVar()	< 0) {setStoredSteam(0); return false;} return true;} return false;}
	
    public boolean playerOwnsThis(EntityPlayer aPlayer, boolean aCheckPrecicely) {if (!hasValidMetaTileEntity()) return false; if (aCheckPrecicely || privateAccess() || mOwnerName.equals("")) if (mOwnerName.equals("")&&isServerSide()) setOwnerName(aPlayer.username); else if (!aPlayer.username.equals("Player") && !mOwnerName.equals("Player") && !mOwnerName.equals(aPlayer.username)) return false; return true;}
    public boolean privateAccess() {if (!hasValidMetaTileEntity()) return mLockUpgrade; return mLockUpgrade || mMetaTileEntity.ownerControl();}
    public boolean unbreakable()   {if (!hasValidMetaTileEntity()) return mLockUpgrade; return mLockUpgrade || mMetaTileEntity.unbreakable();}
    public void doEnergyExplosion() {if (getUniversalEnergyCapacity() > 0 && getUniversalEnergyStored() >= getUniversalEnergyCapacity()/5) doExplosion(getOutputVoltage()*(getUniversalEnergyStored() >= getUniversalEnergyCapacity()?4:getUniversalEnergyStored() >= getUniversalEnergyCapacity()/2?2:1));}
    
    @Override
    public void doExplosion(int aAmount) {
    	if (mIsAddedToEnet && GregTech_API.sMachineWireFire && hasValidMetaTileEntity() && mMetaTileEntity.isElectric()) {
	        try {
	        	mReleaseEnergy = true;
	        	
	        	GT_ModHandler.removeTileFromEnet(getWorld(), this);
	        	GT_ModHandler.addTileToEnet(getWorld(), this);
		        GT_ModHandler.emitEnergyToEnet(GregTech_API.VOLTAGE_LOW			, getWorld(), this);
		        GT_ModHandler.emitEnergyToEnet(GregTech_API.VOLTAGE_MEDIUM		, getWorld(), this);
		        GT_ModHandler.emitEnergyToEnet(GregTech_API.VOLTAGE_HIGH		, getWorld(), this);
		        GT_ModHandler.emitEnergyToEnet(GregTech_API.VOLTAGE_EXTREME		, getWorld(), this);
		        GT_ModHandler.emitEnergyToEnet(GregTech_API.VOLTAGE_INSANE		, getWorld(), this);
	        } catch(Exception e) {}
    	}
    	mReleaseEnergy = false;
    	if (hasValidMetaTileEntity()) mMetaTileEntity.onExplosion();
    	float tStrength = aAmount<GregTech_API.VOLTAGE_ULTRALOW?1.0F:aAmount<GregTech_API.VOLTAGE_LOW?2.0F:aAmount<GregTech_API.VOLTAGE_MEDIUM?3.0F:aAmount<GregTech_API.VOLTAGE_HIGH?4.0F:aAmount<GregTech_API.VOLTAGE_EXTREME?5.0F:aAmount<GregTech_API.VOLTAGE_EXTREME*2?6.0F:aAmount<GregTech_API.VOLTAGE_INSANE?7.0F:aAmount<GregTech_API.VOLTAGE_MEGA?8.0F:aAmount<GregTech_API.VOLTAGE_ULTIMATE?9.0F:10.0F;
    	int tX=getXCoord(), tY=getYCoord(), tZ=getZCoord();
    	getWorld().setBlock(tX, tY, tZ, 0);
    	getWorld().createExplosion(null, tX+0.5, tY+0.5, tZ+0.5, tStrength, true);
    }
	
	private ItemStack getDrop() {
		ItemStack rStack = new ItemStack(GregTech_API.sBlockList[1], 1, mID);
		NBTTagCompound tNBT = new NBTTagCompound();
    	if (mLockUpgrade		) tNBT.setBoolean	("mLockUpgrade"		, mLockUpgrade);
    	if (mMJConverter		) tNBT.setBoolean	("mMJConverter"		, mMJConverter);
    	if (mSteamConverter		) tNBT.setBoolean	("mSteamConverter"	, mSteamConverter);
		if (mColor				> 0) tNBT.setByte	("mColor"			, mColor);
		if (mTransformers		> 0) tNBT.setByte	("mTransformers"	, mTransformers);
		if (mOverclockers		> 0) tNBT.setByte	("mOverclockers"	, mOverclockers);
		if (mRSEnergyCells		> 0) tNBT.setByte	("mRSEnergyCells"	, mRSEnergyCells);
		if (mSteamTanks			> 0) tNBT.setByte	("mSteamTanks"		, mSteamTanks);
		if (mOtherUpgrades		> 0) tNBT.setByte	("mOtherUpgrades"	, mOtherUpgrades);
		if (mUpgradedStorage	> 0) tNBT.setInteger("mUpgradedStorage"	, mUpgradedStorage);
		if (mStrongRedstone		> 0) tNBT.setByte	("mStrongRedstone"	, mStrongRedstone);
        for (byte i = 0; i < mCoverSides.length; i++) {
        	if (mCoverSides[i] != 0) {
        		tNBT.setIntArray("mCoverData"	, mCoverData);
        		tNBT.setIntArray("mCoverSides"	, mCoverSides);
        		break;
        	}
        }
		if (hasValidMetaTileEntity()) mMetaTileEntity.setItemNBT(tNBT);
		if (!tNBT.hasNoTags()) rStack.setTagCompound(tNBT);
		return rStack;
	}
	
	public int getUpgradeCount() {
		return (mLockUpgrade?1:0)+(mMJConverter?1:0)+(mSteamConverter?1:0)+mSteamTanks+mTransformers+mOverclockers+mOtherUpgrades+mRSEnergyCells;
	}
	
	@Override
	public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
		if (isClientSide()) {
			if (getCoverBehaviorAtSide(aSide).onCoverRightclickClient(aSide, this, aPlayer, aX, aY, aZ)) return true;
		}
		if (isServerSide()) {
			if (mColor != 0 && GT_Utility.areStacksEqual(new ItemStack(Item.bucketWater, 1), aPlayer.inventory.getCurrentItem())) {
				aPlayer.inventory.getCurrentItem().itemID = Item.bucketEmpty.itemID;
				mColor = 0;
				return true;
			}
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sWrenchList)) {
				byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
				if (!isValidFacing(tSide) || tSide == getFrontFacing()) {
					if (mMetaTileEntity.isWrenchable()) {
						if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), mMetaTileEntity.isSimpleMachine()?3:10, mMetaTileEntity.isSimpleMachine()?3000:10000, aPlayer)) {
							getWorld().spawnEntityInWorld(new EntityItem(getWorld(), getXCoord()+0.5, getYCoord()+0.5, getZCoord()+0.5, getDrop()));
							getWorld().setBlockToAir(getXCoord(), getYCoord(), getZCoord());
							GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(100), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						}
					}
				} else {
					if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
						setFrontFacing(tSide);
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(100), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
					}
				}
				return true;
			}
			
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sScrewdriverList)) {
				if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
					if (getCoverIDAtSide(aSide) == -2 && mMetaTileEntity.allowCoverOnSide(aSide, -1)) setCoverIDAtSide(aSide, -1); else
					if (getCoverIDAtSide(aSide) == -1 && mMetaTileEntity.allowCoverOnSide(aSide, -2)) setCoverIDAtSide(aSide, -2); else
					if (getCoverIDAtSide(aSide) ==  0 && mMetaTileEntity.allowCoverOnSide(aSide, -1)) setCoverIDAtSide(aSide, -1); else
					setCoverDataAtSide(aSide, getCoverBehaviorAtSide(aSide).onCoverScrewdriverclick(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ));
					mMetaTileEntity.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
					GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(100), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
				}
				return true;
			}
			
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sHardHammerList)) {
				if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
					if (mInputDisabled = !mInputDisabled) mOutputDisabled = !mOutputDisabled;
					GT_Utility.sendChatToPlayer(aPlayer, "Auto-Input: " + (mInputDisabled?"Disabled":"Enabled") + "  Auto-Output: " + (mOutputDisabled?"Disabled":"Enabled"));
					GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(1), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
				}
				return true;
			}
			
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sSoftHammerList)) {
				if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
					mWorks = !mWorks;
					GT_Utility.sendChatToPlayer(aPlayer, "Machine Processing: " + (isAllowedToWork()?"Enabled":"Disabled"));
					GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(101), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
				}
				return true;
			}
			
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sSolderingToolList)) {
				byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
				if (GT_ModHandler.useSolderingIron(aPlayer.inventory.getCurrentItem(), aPlayer)) {
					mStrongRedstone ^= (1 << tSide);
					GT_Utility.sendChatToPlayer(aPlayer, "Redstone Output at Side " + tSide + " set to: " + ((mStrongRedstone & (1 << tSide))!=0?"Strong":"Weak"));
					GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(103), 3.0F, -1, getXCoord(), getYCoord(), getZCoord());
				}
				return true;
			}
			
			if (getCoverIDAtSide(aSide) == 0) {
				if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sCovers.keySet())) {
					if (GregTech_API.getCoverBehavior(aPlayer.inventory.getCurrentItem()).isCoverPlaceable(aSide, GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem()), this) && mMetaTileEntity.allowCoverOnSide(aSide, GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem()))) {
						setCoverItemAtSide(aSide, aPlayer.inventory.getCurrentItem());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(100), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
					}
					return true;
				}
			} else {
				if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sCrowbarList)) {
					if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(0), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						dropCover(aSide, aSide, false);
					}
					return true;
				}
			}
			
			if (getCoverBehaviorAtSide(aSide).onCoverRightclick(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ)) return true;
			
			if (!getCoverBehaviorAtSide(aSide).isGUIClickable(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this)) return false;
			
			if (isUpgradable() && aPlayer.inventory.getCurrentItem() != null) {
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 25))) {
					if (addMJConverterUpgrade()) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 80))) {
					if (addSteamEngineUpgrade()) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 88))) {
					if (isUpgradable() && !mLockUpgrade) {
						mLockUpgrade = true;
						setOwnerName(aPlayer.username);
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GT_ModHandler.getIC2Item("overclockerUpgrade", 1))) {
					if (addOverclockerUpgrade()) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (getInputVoltage() < GregTech_API.VOLTAGE_HIGH && getOutputVoltage()*getOutputAmperage() < GregTech_API.VOLTAGE_HIGH){
					if (aPlayer.inventory.getCurrentItem().isItemEqual(GT_ModHandler.getIC2Item("transformerUpgrade", 1))) {
						if (addTransformerUpgrade()) {
							GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
							if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
						}
						return true;
					}
				} else {
					if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 27))) {
						if (addTransformerUpgrade()) {
							GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
							if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
						}
						return true;
					}
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GT_ModHandler.getIC2Item("energyStorageUpgrade", 1))) {
					if (addBatteryUpgrade(10000, (byte)1)) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 26))) {
					if (addBatteryUpgrade(100000, (byte)1)) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 12))) {
					if (addBatteryUpgrade(100000, (byte)2)) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 13))) {
					if (addBatteryUpgrade(1000000, (byte)3)) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 14))) {
					if (addBatteryUpgrade(10000000, (byte)4)) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					}
					return true;
				}
				if (hasMJConverterUpgrade() && aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 28))) {
					mRSEnergyCells++;
					GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
					if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					return true;
				}
				if (hasSteamEngineUpgrade() && aPlayer.inventory.getCurrentItem().isItemEqual(GregTech_API.getGregTechItem(3, 1, 81))) {
					mSteamTanks++;
					GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(3), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
					if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
					return true;
				}
			}
			
			try {
				if (aPlayer != null && hasValidMetaTileEntity() && mID > 15) return mMetaTileEntity.onRightclick(aPlayer, aSide, aX, aY, aZ);
	    	} catch(Throwable e) {
	    		GT_Log.err.println("Encountered Exception while rightclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
	    		e.printStackTrace(GT_Log.err);
	    	}
		}
		return true;
	}
	
	@Override
	public void onLeftclick(EntityPlayer aPlayer) {
		try {
			if (aPlayer != null && hasValidMetaTileEntity() && mID > 15) mMetaTileEntity.onLeftclick(aPlayer);
    	} catch(Throwable e) {
    		GT_Log.err.println("Encountered Exception while leftclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
    		e.printStackTrace(GT_Log.err);
    	}
	}
	
	@Override
	public boolean isDigitalChest() {
		if (hasValidMetaTileEntity()) return mMetaTileEntity.isDigitalChest();
		return false;
	}
	
	@Override
	public ItemStack[] getStoredItemData() {
		if (hasValidMetaTileEntity()) return mMetaTileEntity.getStoredItemData();
		return null;
	}
	
	@Override
	public void setItemCount(int aCount) {
		if (hasValidMetaTileEntity()) mMetaTileEntity.setItemCount(aCount);
	}
	
	@Override
	public int getMaxItemCount() {
		if (hasValidMetaTileEntity()) return mMetaTileEntity.getMaxItemCount();
		return 0;
	}
	
	/**
	 * Can put aStack into Slot
	 */
	@Override
	public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
		return hasValidMetaTileEntity() && mMetaTileEntity.isItemValidForSlot(aIndex, aStack);
	}
	
	/**
	 * returns all valid Inventory Slots, no matter which Side (Unless it's covered).
	 * The Side Stuff is done in the following two Functions.
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int aSide) {
		if (hasValidMetaTileEntity() && (getCoverBehaviorAtSide((byte)aSide).letsItemsOut((byte)aSide, getCoverIDAtSide((byte)aSide), getCoverDataAtSide((byte)aSide), this) || getCoverBehaviorAtSide((byte)aSide).letsItemsIn((byte)aSide, getCoverIDAtSide((byte)aSide), getCoverDataAtSide((byte)aSide), this))) return mMetaTileEntity.getAccessibleSlotsFromSide(aSide);
		return new int[0];
	}
	
	/**
	 * Can put aStack into Slot at Side
	 */
	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		return hasValidMetaTileEntity() && (mRunningThroughTick || !mInputDisabled ) && getCoverBehaviorAtSide((byte)aSide).letsItemsIn ((byte)aSide, getCoverIDAtSide((byte)aSide), getCoverDataAtSide((byte)aSide), this) && mMetaTileEntity.canInsertItem(aIndex, aStack, aSide);
	}
	
	/**
	 * Can pull aStack out of Slot from Side
	 */
	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		return hasValidMetaTileEntity() && (mRunningThroughTick || !mOutputDisabled) && getCoverBehaviorAtSide((byte)aSide).letsItemsOut((byte)aSide, getCoverIDAtSide((byte)aSide), getCoverDataAtSide((byte)aSide), this) && mMetaTileEntity.canExtractItem(aIndex, aStack, aSide);
	}
	
	@Override
	public boolean isUpgradable() {
		return hasValidMetaTileEntity() && getUpgradeCount() < 16;
	}
	
	@Override
	public boolean isMJConverterUpgradable() {
		return isUpgradable()&&mMetaTileEntity.isElectric()&&!hasMJConverterUpgrade()&&getMJCapacity()>0;
	}
	
	@Override
	public boolean isOverclockerUpgradable() {
		return isUpgradable()&&mMetaTileEntity.isOverclockerUpgradable()&&mOverclockers<4;
	}
	
	@Override
	public boolean isTransformerUpgradable() {
		return isUpgradable()&&mMetaTileEntity.isElectric()&&mMetaTileEntity.isTransformerUpgradable()&&getInputVoltage()<GregTech_API.VOLTAGE_INSANE&&getOutputVoltage()*getOutputAmperage()<GregTech_API.VOLTAGE_INSANE;
	}
	
	@Override
	public boolean isBatteryUpgradable(int aStorage, byte aTier) {
		return isUpgradable()&&mMetaTileEntity.isElectric()&&mMetaTileEntity.isBatteryUpgradable()&&GT_Utility.getTier(getInputVoltage())>=aTier&&aStorage+getEUCapacity()>0;
	}
	
	@Override
	public boolean hasMJConverterUpgrade() {
		if (hasValidMetaTileEntity() && mMetaTileEntity.isPneumatic()) return true;
		return mMJConverter;
	}
	
	@Override
	public byte getOverclockerUpgradeCount() {
		return mOverclockers;
	}
	
	@Override
	public byte getTransformerUpgradeCount() {
		return mTransformers;
	}
	
	@Override
	public int getUpgradeStorageVolume() {
		return mUpgradedStorage;
	}
	
	@Override
	public byte getInternalInputRedstoneSignal(byte aSide) {
		return (byte)(getCoverBehaviorAtSide(aSide).getRedstoneInput(aSide, getInputRedstoneSignal(aSide), getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this)&15);
	}
	
	@Override
	public byte getInputRedstoneSignal(byte aSide) {
		return (byte)(getWorld().getIndirectPowerLevelTo(getOffsetX(aSide, 1), getOffsetY(aSide, 1), getOffsetZ(aSide, 1), aSide)&15);
	}
	
	@Override
	public byte getOutputRedstoneSignal(byte aSide) {
		return (byte)(getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this) || (mRedstone && getCoverBehaviorAtSide(aSide).letsRedstoneGoOut(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this))?mSidedRedstone[aSide]&15:0);
	}
	
	@Override
	public void setInternalOutputRedstoneSignal(byte aSide, byte aStrength) {
		if (!getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this)) setOutputRedstoneSignal(aSide, aStrength);
	}
	
	@Override
	public void setOutputRedstoneSignal(byte aSide, byte aStrength) {
		aStrength = (byte)Math.min(Math.max(0, aStrength), 15);
		if (aSide >= 0 && aSide < 6 && mSidedRedstone[aSide] != aStrength) {
			mSidedRedstone[aSide] = aStrength;
    		issueBlockUpdate();
		}
	}
	
	@Override
	public boolean isSteamEngineUpgradable() {
		return isUpgradable()&&!hasSteamEngineUpgrade()&&getSteamCapacity()>0;
	}
	
	@Override
	public boolean addSteamEngineUpgrade() {
		if (isSteamEngineUpgradable()) {
			issueBlockUpdate();
			mSteamConverter = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasSteamEngineUpgrade() {
		if (hasValidMetaTileEntity() && mMetaTileEntity.isSteampowered()) return true;
		return mSteamConverter;
	}
	
	@Override
	public boolean addMJConverterUpgrade() {
		if (isMJConverterUpgradable()) {
			mMJConverter = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addOverclockerUpgrade() {
		if (isOverclockerUpgradable()) {
			mOverclockers++;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addTransformerUpgrade() {
		if (isTransformerUpgradable()) {
			mTransformers++;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addBatteryUpgrade(int aStorage, byte aTier) {
		if (isBatteryUpgradable(aStorage, aTier)) {
			mUpgradedStorage+=aStorage;
			mOtherUpgrades++;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasInventoryBeenModified() {
		return mInventoryChanged;
	}
	
	@Override
	public void setGenericRedstoneOutput(boolean aOnOff) {
		mRedstone = aOnOff;
	}
	
	@Override
	public int getErrorDisplayID() {
		return mDisplayErrorCode;
	}
	
	@Override
	public void setErrorDisplayID(int aErrorID) {
		mDisplayErrorCode = aErrorID;
	}
	
	@Override
	public IMetaTileEntity getMetaTileEntity() {
		return hasValidMetaTileEntity()?mMetaTileEntity:null;
	}
	
	@Override
	public void setCoverIDAtSide(byte aSide, int aID) {
		if (aSide >= 0 && aSide < 6) {
			mCoverSides[aSide] = aID;
			mCoverData[aSide] = 0;
			issueCoverUpdate(aSide);
			issueBlockUpdate();
		}
	}
	
	@Override
	public void setCoverItemAtSide(byte aSide, ItemStack aCover) {
		setCoverIDAtSide(aSide, GT_Utility.stackToInt(aCover));
	}
	
	@Override
	public int getCoverIDAtSide(byte aSide) {
		if (aSide >= 0 && aSide < 6) return mCoverSides[aSide]; return 0;
	}
	
	@Override
	public ItemStack getCoverItemAtSide(byte aSide) {
		return GT_Utility.intToStack(getCoverIDAtSide(aSide));
	}
	
	@Override
	public GT_CoverBehavior getCoverBehaviorAtSide(byte aSide) {
		return GregTech_API.getCoverBehavior(getCoverIDAtSide(aSide));
	}
	
	@Override
	public boolean canPlaceCoverIDAtSide(byte aSide, int aID) {
		return getCoverIDAtSide(aSide) == 0;
	}
	
	@Override
	public boolean canPlaceCoverItemAtSide(byte aSide, ItemStack aCover) {
		return getCoverIDAtSide(aSide) == 0;
	}
	
	@Override
	public void setCoverDataAtSide(byte aSide, int aData) {
		if (aSide >= 0 && aSide < 6) mCoverData[aSide] = aData;
	}
	
	@Override
	public int getCoverDataAtSide(byte aSide) {
		if (aSide >= 0 && aSide < 6) return mCoverData[aSide];
		return 0;
	}

	@Override
	public void setLightValue(byte aLightValue) {
		mLightValue = (byte)(aLightValue & 15);
	}
	
	public byte getLightValue() {
		return mLightValue;
	}
	
	@Override
	public void setOnFire() {
		for (byte i = 0; i < 6; i++) {
			short tID = getBlockIDAtSide(i);
			Block tBlock = Block.blocksList[tID];
			if (tID == 0 || tBlock == null || null == tBlock.getCollisionBoundingBoxFromPool(getWorld(), getXCoord() + getOffsetX(i, 1), getOffsetY(i, 1), getOffsetZ(i, 1))) {
    			getWorld().setBlock(getOffsetX(i, 1), getOffsetY(i, 1), getOffsetZ(i, 1), Block.fire.blockID);
    		}
    	}
	}
	
	@Override
	public int getAverageElectricInput() {
		int rEU = 0;
		for (int tEU : mAverageEUInput) rEU += tEU;
		return rEU / mAverageEUInput.length;
	}
	
	@Override
	public int getAverageElectricOutput() {
		int rEU = 0;
		for (int tEU : mAverageEUOutput) rEU += tEU;
		return rEU / mAverageEUOutput.length;
	}
	
	@Override
	public boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced) {
		if (getCoverBehaviorAtSide(aSide).onCoverRemoval(aSide, getCoverIDAtSide(aSide), mCoverData[aSide], this, aForced) || aForced) {
			ItemStack tStack = GT_OreDictUnificator.get(getCoverItemAtSide(aSide));
			if (tStack != null) {
				EntityItem tEntity = new EntityItem(getWorld(), getOffsetX(aDroppedSide, 1) + 0.5, getOffsetY(aDroppedSide, 1) + 0.5, getOffsetZ(aDroppedSide, 1) + 0.5, tStack);
				tEntity.motionX = 0;
				tEntity.motionY = 0;
				tEntity.motionZ = 0;
				getWorld().spawnEntityInWorld(tEntity);
			}
			setCoverIDAtSide(aSide, 0);
			if (mMetaTileEntity.hasSidedRedstoneOutputBehavior()) {
				setOutputRedstoneSignal(aSide, (byte) 0);
			} else {
				setOutputRedstoneSignal(aSide, (byte)15);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String getOwnerName() {
		if (mOwnerName == null || mOwnerName.equals("")) return "Player";
		return mOwnerName;
	}
	
	@Override
	public String setOwnerName(String aName) {
		if (aName == null) aName = "Player";
		return mOwnerName = aName;
	}
	
	@Override
	public byte getComparatorValue(byte aSide) {
		return hasValidMetaTileEntity()?mMetaTileEntity.getComparatorValue(aSide):0;
	}
	
	@Override
	public byte getStrongOutputRedstoneSignal(byte aSide) {
		return aSide>=0&&aSide<6&&(mStrongRedstone & (1 << aSide))!=0?(byte)(mSidedRedstone[aSide]&15):0;
	}
	
	@Override
	public void setStrongOutputRedstoneSignal(byte aSide, byte aStrength) {
		mStrongRedstone |= (1 << aSide);
		setOutputRedstoneSignal(aSide, aStrength);
	}
	
	@Override
	public ItemStack decrStackSize(int aIndex, int aAmount) {
		if (hasValidMetaTileEntity()) {
			mInventoryChanged = true;
			return mMetaTileEntity.decrStackSize(aIndex, aAmount);
		}
		return null;
	}
	
	@Override
	public boolean injectEnergyUnits(byte aSide, int aVoltage, int aAmperage) {
		if (!hasValidMetaTileEntity() || !mMetaTileEntity.isElectric() || !inputEnergyFrom(aSide)) return false;
		if (aVoltage > getInputVoltage()) {
			doExplosion(aVoltage);
			return true;
		}
		if (increaseStoredEnergyUnits(aVoltage*aAmperage, true)) {
			mAverageEUInput[mAverageEUInputIndex] += aVoltage*aAmperage;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean drainEnergyUnits(byte aSide, int aVoltage, int aAmperage) {
		if (!hasValidMetaTileEntity() || !mMetaTileEntity.isElectric() || !outputsEnergyTo(aSide) || getStoredEU() - (aVoltage*aAmperage) < mMetaTileEntity.getMinimumStoredEU()) return false;
		if (decreaseStoredEU(aVoltage*aAmperage, false)) {
			mAverageEUOutput[mAverageEUOutputIndex] += aVoltage*aAmperage;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean acceptsRotationalEnergy(byte aSide) {
		if (!hasValidMetaTileEntity() || getCoverIDAtSide(aSide) != 0) return false;
		return mMetaTileEntity.acceptsRotationalEnergy(aSide);
	}
	
	@Override
	public boolean injectRotationalEnergy(byte aSide, int aSpeed, int aEnergy) {
		if (!hasValidMetaTileEntity() || getCoverIDAtSide(aSide) != 0) return false;
		return mMetaTileEntity.injectRotationalEnergy(aSide, aSpeed, aEnergy);
	}
    
    @Override
    public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
    	if (hasValidMetaTileEntity() && (mRunningThroughTick || !mInputDisabled ) && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput ((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidIn ((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.fill(aSide, aFluid, doFill);
		return 0;
    }
    
	@Override
	public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
		if (hasValidMetaTileEntity() && (mRunningThroughTick || !mOutputDisabled) && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.drain(aSide, maxDrain, doDrain);
		return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
		if (hasValidMetaTileEntity() && (mRunningThroughTick || !mOutputDisabled) && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.drain(aSide, aFluid, doDrain);
		return null;
	}
	
	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
		if (hasValidMetaTileEntity() && (mRunningThroughTick || !mInputDisabled ) && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput ((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidIn ((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.canFill(aSide, aFluid);
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
		if (hasValidMetaTileEntity() && (mRunningThroughTick || !mOutputDisabled) && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.canDrain(aSide, aFluid);
		return false;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
		if (hasValidMetaTileEntity() && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidIn ((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)) || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.getTankInfo(aSide);
		return new FluidTankInfo[] {};
	}
	
	public double getOutputEnergyUnitsPerTick() { return getOutputVoltage();}
	public boolean isTeleporterCompatible(ForgeDirection aSide) {return hasValidMetaTileEntity() && mMetaTileEntity.isTeleporterCompatible();}
	public double demandedEnergyUnits() {if (mReleaseEnergy || !hasValidMetaTileEntity() || !mMetaTileEntity.isEnetInput()) return 0; return getEUCapacity() - getStoredEU();}
	public double injectEnergyUnits(ForgeDirection aDirection, double aAmount) {return injectEnergyUnits((byte)aDirection.ordinal(), (int)aAmount, 1)?0:aAmount;}
	public boolean acceptsEnergyFrom(TileEntity aEmitter, ForgeDirection aDirection) {return inputEnergyFrom((byte)aDirection.ordinal());}
	public boolean emitsEnergyTo(TileEntity aReceiver, ForgeDirection aDirection) {return outputsEnergyTo((byte)aDirection.ordinal());}
	public double getOfferedEnergy() {return (hasValidMetaTileEntity() && getStoredEU() - mMetaTileEntity.getMinimumStoredEU() >= getOutputVoltage())?Math.max(0, getOutputVoltage()):0;}
	public void drawEnergy(double amount) {mAverageEUOutput[mAverageEUOutputIndex] += amount; decreaseStoredEU((int)amount, true);}
	
	public int addEnergy(int aEnergy) {
		if (!hasValidMetaTileEntity()) return 0;
		if (aEnergy > 0)
			increaseStoredEnergyUnits( aEnergy, true);
		else
			decreaseStoredEU(-aEnergy, true);
		return mMetaTileEntity.getEUVar();
	}
	
    public boolean isAddedToEnergyNet() {return mIsAddedToEnet;}
	public void setStored(int aEU) {if (hasValidMetaTileEntity()) setStoredEU(aEU);}
	public int demandsEnergy() {if (mReleaseEnergy || !hasValidMetaTileEntity() || !mMetaTileEntity.isEnetInput()) return 0; return getCapacity() - getStored();}
	public int getCapacity() {return getEUCapacity();}
    public int getStored() {return Math.min(getStoredEU(), getCapacity());}
	public int getMaxSafeInput() {return getInputVoltage();}
	public int getMaxEnergyOutput() {if (mReleaseEnergy) return Integer.MAX_VALUE; return getOutput();}
	public int getOutput() {return getOutputVoltage();}
	
	@Override
	public boolean isInvalidTileEntity() {
		return isInvalid();
	}
	
	@Override
	public boolean addStackToSlot(int aIndex, ItemStack aStack) {
		if (aStack == null) return true;
		if (aIndex < 0 || aIndex >= getSizeInventory()) return false;
		if (getStackInSlot(aIndex) == null) {
			setInventorySlotContents(aIndex, aStack);
			return true;
		}
		if (getStackInSlot(aIndex).isItemEqual(aStack) && getStackInSlot(aIndex).stackSize + aStack.stackSize <= Math.min(aStack.getMaxStackSize(), getInventoryStackLimit())) {
			getStackInSlot(aIndex).stackSize+=aStack.stackSize;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
		aStack = aStack.copy();
		aStack.stackSize = aAmount;
		return addStackToSlot(aIndex, aStack);
	}
	
	@Override
	public void setMetaTileEntity(IMetaTileEntity aMetaTileEntity) {
		mMetaTileEntity = (MetaTileEntity)aMetaTileEntity;
	}
	
	@Override
	public byte getColorization() {
		return (byte)(mColor-1);
	}
	
	@Override
	public byte setColorization(byte aColor) {
		return mColor = (byte)(aColor+1);
	}
}