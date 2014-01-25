package gregtechmod.api.metatileentity;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.interfaces.IMetaTileEntity;
import gregtechmod.api.util.*;

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
import net.minecraft.util.Icon;
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
public class BaseMetaPipeEntity extends BaseTileEntity implements IGregTechTileEntity {
	public static volatile int VERSION = 407;
	
	public byte mConnections = 0;
	
	protected MetaPipeEntity mMetaTileEntity;
	private byte[] mSidedRedstone = new byte[] {0,0,0,0,0,0};
	private int[] mCoverSides = new int[] {0,0,0,0,0,0}, mCoverData = new int[] {0,0,0,0,0,0}, mTimeStatistics = new int[GregTech_API.TICKS_FOR_LAG_AVERAGING];
	private boolean mInventoryChanged = false, mWorkUpdate = false, mWorks = true, mNeedsUpdate = true, mNeedsBlockUpdate = true, mSendClientData = false;
	private byte mColor = 0, oColor = 0, mStrongRedstone = 0, oRedstoneData = 63, oTextureData = 0, oUpdateData = 0, mLagWarningCount = 0;
	private int oX = 0, oY = 0, oZ = 0, mTimeStatisticsIndex = 0;
	private short mID = 0;
	private long mTickTimer = 0;
	
	public BaseMetaPipeEntity() {}
	
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
	        aNBT.setIntArray	("mCoverData"		, mCoverData);
	        aNBT.setIntArray	("mCoverSides"		, mCoverSides);
	    	aNBT.setByteArray	("mRedstoneSided"	, mSidedRedstone);
	        aNBT.setByte		("mConnections"		, mConnections);
	        aNBT.setByte		("mColor"			, mColor);
	        aNBT.setByte		("mStrongRedstone"	, mStrongRedstone);
	    	aNBT.setBoolean		("mWorks"			, !mWorks);
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
		} else {
	        if (aID<=0) 	mID	= (short)aNBT.getInteger	("mID"); else mID = aID;
	        mCoverSides 		= aNBT.getIntArray	("mCoverSides");
	        mCoverData 			= aNBT.getIntArray	("mCoverData");
	        mSidedRedstone		= aNBT.getByteArray ("mRedstoneSided");
	        mConnections		= aNBT.getByte		("mConnections");
	        mColor				= aNBT.getByte		("mColor");
	        mStrongRedstone		= aNBT.getByte		("mStrongRedstone");
	    	mWorks				=!aNBT.getBoolean	("mWorks");
			
	    	if (mCoverData.length != 6)    	mCoverData	   = new int[]  { 0, 0, 0, 0, 0, 0};
	    	if (mCoverSides.length != 6)    mCoverSides    = new int[]  { 0, 0, 0, 0, 0, 0};
	        if (mSidedRedstone.length != 6) mSidedRedstone = new byte[] { 0, 0, 0, 0, 0, 0};
	        
	    	if (mID!=0 && createNewMetatileEntity(mID)) {
	            NBTTagList tItemList = aNBT.getTagList("Inventory");
	            for (int i = 0; i < tItemList.tagCount(); i++) {
	                NBTTagCompound tTag = (NBTTagCompound)tItemList.tagAt(i);
	                int tSlot = tTag.getInteger("IntSlot");
	                if (tSlot >= 0 && tSlot < mMetaTileEntity.getRealInventory().length) {
	                	mMetaTileEntity.getRealInventory()[tSlot] = GT_Utility.loadItem(tTag);
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
		
    	if (mCoverData.length != 6)    	mCoverData	   = new int[]  { 0, 0, 0, 0, 0, 0};
    	if (mCoverSides.length != 6)    mCoverSides    = new int[]  { 0, 0, 0, 0, 0, 0};
        if (mSidedRedstone.length != 6) mSidedRedstone = new byte[] { 0, 0, 0, 0, 0, 0};
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
	
	@Override
    public void updateEntity() {
		
    	if (!hasValidMetaTileEntity()) {
    		if (mMetaTileEntity == null) return;
	    	mMetaTileEntity.setBaseMetaTileEntity(this);
    	}
    	
    	long tTime = System.currentTimeMillis();
    	
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
        			return;
        		}
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
    	    	
    	    	mConnections = (byte)(mMetaTileEntity.mConnections | (mConnections & ~63));
    	    	
    	    	if ((mConnections & -64) == 64 && getRandomNumber(1000) == 0) {
    	    		mConnections = (byte)((mConnections & ~64) | -128);
    	    	}
    	    }
    	    
    	    try {
    	    	mMetaTileEntity.onPreTick();
    	    } catch(Throwable e) {
    	    	GT_Log.err.println("Encountered Exception while ticking MetaTileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
    	    	e.printStackTrace(GT_Log.err);
    	    }
    	    
    		if (!hasValidMetaTileEntity()) {
    			return;
    		}
    		
    	    if (isServerSide()) {
    	    	if (mTickTimer == 10) {
    	    		issueBlockUpdate();
    	    	}
    	    	
    	    	if (getXCoord() != oX || getYCoord() != oY || getZCoord() != oZ) {
    	    		oX = getXCoord();
    	    		oY = getYCoord();
    	    		oZ = getZCoord();
    		    	issueClientUpdate();
    	    	}
   	        }
    	    
    	    try {
    	    	mMetaTileEntity.onPostTick();
    	    } catch(Throwable e) {
    	    	GT_Log.err.println("Encountered Exception while ticking MetaTileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
    	    	e.printStackTrace(GT_Log.err);
    	    }
    	    
    		if (!hasValidMetaTileEntity()) {
    			return;
    		}
    		
        	if (isServerSide()) {
        		if (mTickTimer % 10 == 0) {
        		    if (mSendClientData) {
        		    	GT_Utility.sendPacketToAllPlayersInRange(getWorld(), getDescriptionPacket(), getXCoord(), getZCoord());
	    		    	mSendClientData = false;
        	    	}
        		}
        		
        		if (mTickTimer > 10) {
	    		    byte tData = mConnections;
	    		    if (tData != oTextureData) sendBlockEvent((byte)0, oTextureData = tData);
	    		    tData = mMetaTileEntity.getUpdateData();
	    		    if (tData != oUpdateData) sendBlockEvent((byte)1, oUpdateData = tData);
	    		    tData = mColor;
	    		    if (tData != oColor) sendBlockEvent((byte)2, oColor = tData);
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
    	
    	if (isServerSide() && hasValidMetaTileEntity()) {
        	tTime = System.currentTimeMillis() - tTime;
	    	if (mTimeStatistics.length > 0) mTimeStatistics[mTimeStatisticsIndex = (mTimeStatisticsIndex + 1) % mTimeStatistics.length] = (int)tTime;
	    	if (tTime > 0 && tTime > GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING && mTickTimer > 1000 && getMetaTileEntity().doTickProfilingMessageDuringThisTick() && mLagWarningCount++<10) System.out.println("WARNING: Possible Lag Source at [" + getXCoord() + ", " + getYCoord() + ", " + getZCoord() + "] in Dimension " + getWorld().provider.dimensionId + " with " + tTime + "ms caused by an instance of " + getMetaTileEntity().getClass());
    	}
    	
    	mWorkUpdate = mInventoryChanged = false;
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
        
        tOut.writeByte(oTextureData = mConnections);
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
				mConnections = (byte)aValue;
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
			if (mTimeStatistics.length > 0) {
				double tAverageTime = 0; for (int tTime : mTimeStatistics) tAverageTime += tTime;
				tList.add("This particular TileEntity has caused an average CPU-load of ~" + (tAverageTime/mTimeStatistics.length) + "ms over the last " + mTimeStatistics.length + " ticks.");
			}
			if (mLagWarningCount > 0) {
				tList.add("This TileEntity has also caused " + (mLagWarningCount>=10?"more than 10":mLagWarningCount) + " Lag Spike Warnings (anything taking longer than " + GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING + "ms) on the Server.");
			}
			tList.add("Is" + (mMetaTileEntity.isAccessAllowed(aPlayer)?" ":" not ") + "accessible for you");
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
	
	@Override public boolean isGivingInformation() {if (hasValidMetaTileEntity()) return mMetaTileEntity.isGivingInformation(); return false;}
	@Override public boolean isValidFacing(byte aSide) {if (hasValidMetaTileEntity()) return mMetaTileEntity.isFacingValid(aSide); return false;}
	@Override public byte getBackFacing() {return GT_Utility.getOppositeSide(getFrontFacing());}
	@Override public byte getFrontFacing() {return 6;}
	@Override public void setFrontFacing(byte aFacing) {/*Do nothing*/}
	@Override public int getSizeInventory() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getSizeInventory(); return 0;}
	@Override public ItemStack getStackInSlot(int aIndex) {if (hasValidMetaTileEntity()) return mMetaTileEntity.getStackInSlot(aIndex); return null;}
	@Override public void setInventorySlotContents(int aIndex, ItemStack aStack) {mInventoryChanged = true; if (hasValidMetaTileEntity()) mMetaTileEntity.setInventorySlotContents(aIndex, GT_OreDictUnificator.setStack(true, aStack));}
	@Override public String getInvName() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getInvName(); if (GregTech_API.mMetaTileList[mID] != null) return GregTech_API.mMetaTileList[mID].getInvName(); return "";}
	@Override public int getInventoryStackLimit() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getInventoryStackLimit(); return 64;}
	@Override public void openChest()  {/*Do nothing*/}
	@Override public void closeChest() {/*Do nothing*/}
	@Override public boolean isUseableByPlayer(EntityPlayer aPlayer) {return hasValidMetaTileEntity() && mTickTimer>40 && getTileEntityOffset(0, 0, 0) == this && aPlayer.getDistanceSq(getXCoord() + 0.5, getYCoord() + 0.5, getZCoord() + 0.5) < 64 && mMetaTileEntity.isAccessAllowed(aPlayer);}
	@Override public void validate() {super.validate(); mTickTimer = 0;}
    @Override public void invalidate() {tileEntityInvalid = false; if (hasValidMetaTileEntity()) {mMetaTileEntity.onRemoval(); mMetaTileEntity.setBaseMetaTileEntity(null);} super.invalidate();}
    @Override public void onChunkUnload() {super.onChunkUnload();}
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
	@Override public void setWorkDataValue(byte aValue) {/*Do nothing*/}
	@Override public byte getWorkDataValue() {return 0;}
    @Override public int getMetaTileID() {return mID;}
    @Override public int setMetaTileID(short aID) {return mID = aID;}
	@Override public boolean isActive() {return false;}
    @Override public void setActive(boolean aActive) {/*Do nothing*/}
	@Override public long getTimer() {return mTickTimer;}
	@Override public boolean decreaseStoredEnergyUnits(int aEnergy, boolean aIgnoreTooLessEnergy) {return false;}
	@Override public boolean increaseStoredEnergyUnits(int aEnergy, boolean aIgnoreTooMuchEnergy) {return false;}
	@Override public boolean inputEnergyFrom(byte aSide) {return false;}
	@Override public boolean outputsEnergyTo(byte aSide) {return false;}
	@Override public int getOutputAmperage() {return 0;}
	@Override public int getOutputVoltage() {return 0;}
	@Override public int getInputVoltage() {return 0;}
	@Override public boolean increaseStoredMJ(int aEnergy, boolean aIgnoreTooMuchEnergy) {return false;}
	@Override public boolean increaseStoredSteam(int aEnergy, boolean aIgnoreTooMuchEnergy) {return false;}
	@Override public String getDescription() {if (hasValidMetaTileEntity()) return mMetaTileEntity.getDescription(); return "";}
    @Override public boolean isValidSlot(int aIndex) {if (hasValidMetaTileEntity()) return mMetaTileEntity.isValidSlot(aIndex); return false;}
    @Override public int getUniversalEnergyStored() {return Math.max(Math.max(getStoredEU(), getStoredMJ()), getStoredSteam());}
	@Override public int getUniversalEnergyCapacity() {return Math.max(Math.max(getEUCapacity(), getMJCapacity()), getSteamCapacity());}
	@Override public int getStoredMJ() {return 0;}
    @Override public int getMJCapacity() {return 0;}
    @Override public int getStoredEU() {return 0;}
    @Override public int getEUCapacity() {return 0;}
    @Override public int getStoredSteam() {return 0;}
    @Override public int getSteamCapacity() {return 0;}
    @Override public int getTextureIndex(byte aSide, byte aMeta) {return getUncoveredIndex(aSide, aMeta);}
    @Override public Icon getTextureIcon(byte aSide, byte aMeta) {Icon rIcon = getCoverTexture(aSide); if (rIcon != null) return rIcon; return getUncoveredIcon(aSide, aMeta);}
    public Icon getUncoveredIcon(byte aSide, byte aMeta) {if ((mConnections & -64) != 0) return null; byte tConnections = mConnections; if (tConnections ==  1 || tConnections ==  2) tConnections = 3; else if (tConnections ==  4 || tConnections ==  8) tConnections = 12; else if (tConnections == 16 || tConnections == 32) tConnections = 48; if (hasValidMetaTileEntity()) return mMetaTileEntity.getTextureIcon(aSide, tConnections, tConnections == 0 || (tConnections & (1<<aSide)) != 0, getOutputRedstoneSignal(aSide)>0); return null;}
    public int getUncoveredIndex(byte aSide, byte aMeta) {if ((mConnections & 64) != 0) return 368; if ((mConnections & -128) != 0) return 369; byte tConnections = mConnections; if (tConnections ==  1 || tConnections ==  2) tConnections = 3; else if (tConnections ==  4 || tConnections ==  8) tConnections = 12; else if (tConnections == 16 || tConnections == 32) tConnections = 48; if (hasValidMetaTileEntity()) return mMetaTileEntity.getTextureIndex(aSide, tConnections, tConnections == 0 || (tConnections & (1<<aSide)) != 0, getOutputRedstoneSignal(aSide)>0); return -2;}
    
    protected boolean hasValidMetaTileEntity() {return mMetaTileEntity != null && mMetaTileEntity.getBaseMetaTileEntity() == this;}
    
    @Override
    public void doExplosion(int aAmount) {
    	if (hasValidMetaTileEntity()) mMetaTileEntity.onExplosion();
    	float tStrength = aAmount<GregTech_API.VOLTAGE_ULTRALOW?1.0F:aAmount<GregTech_API.VOLTAGE_LOW?2.0F:aAmount<GregTech_API.VOLTAGE_MEDIUM?3.0F:aAmount<GregTech_API.VOLTAGE_HIGH?4.0F:aAmount<GregTech_API.VOLTAGE_EXTREME?5.0F:aAmount<GregTech_API.VOLTAGE_EXTREME*2?6.0F:aAmount<GregTech_API.VOLTAGE_INSANE?7.0F:aAmount<GregTech_API.VOLTAGE_MEGA?8.0F:aAmount<GregTech_API.VOLTAGE_ULTIMATE?9.0F:10.0F;
    	int tX=getXCoord(), tY=getYCoord(), tZ=getZCoord();
    	getWorld().setBlock(tX, tY, tZ, 0);
    	if (GregTech_API.sMachineExplosions) getWorld().createExplosion(null, tX+0.5, tY+0.5, tZ+0.5, tStrength, true);
    }
	
	private ItemStack getDrop() {
		ItemStack rStack = new ItemStack(GregTech_API.sBlockList[1], 1, mID);
		NBTTagCompound tNBT = new NBTTagCompound();
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
	
	@Override
	public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
		if (isClientSide()) {
			if (getCoverBehaviorAtSide(aSide).onCoverRightclickClient(aSide, this, aPlayer, aX, aY, aZ)) return true;
		}
		if (isServerSide()) {
			byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
			if (getColorization() >= 0 && GT_Utility.areStacksEqual(new ItemStack(Item.bucketWater, 1), aPlayer.inventory.getCurrentItem())) {
				aPlayer.inventory.getCurrentItem().itemID = Item.bucketEmpty.itemID;
				setColorization((byte)(getColorization() >= 16 ? -2 : -1));
				return true;
			}
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sWrenchList)) {
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
				if (getCoverIDAtSide(aSide) == 0 && getCoverIDAtSide(tSide) != 0) {
					if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 200, aPlayer)) {
						setCoverDataAtSide(tSide, getCoverBehaviorAtSide(tSide).onCoverScrewdriverclick(tSide, getCoverIDAtSide(tSide), getCoverDataAtSide(tSide), this, aPlayer, 0.5F, 0.5F, 0.5F));
						mMetaTileEntity.onScrewdriverRightClick(tSide, aPlayer, aX, aY, aZ);
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(100), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
					}
				} else {
					if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
						setCoverDataAtSide(aSide, getCoverBehaviorAtSide(aSide).onCoverScrewdriverclick(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ));
						mMetaTileEntity.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(100), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
					}
				}
				return true;
			}
			
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sHardHammerList)) {
				//if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
				//	GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(1), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
				//}
				return true;
			}
			
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sSoftHammerList)) {
				//if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
				//	GT_Utility.sendChatToPlayer(aPlayer, "Machine Processing: " + (isAllowedToWork()?"Enabled":"Disabled"));
				//	GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(101), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
				//}
				return true;
			}
			
			if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sSolderingToolList)) {
				if (GT_ModHandler.useSolderingIron(aPlayer.inventory.getCurrentItem(), aPlayer)) {
					mStrongRedstone ^= (1 << tSide);
					GT_Utility.sendChatToPlayer(aPlayer, "Redstone Output at Side " + tSide + " set to: " + ((mStrongRedstone & (1 << tSide))!=0?"Strong":"Weak"));
					GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(103), 3.0F, -1, getXCoord(), getYCoord(), getZCoord());
				}
				return true;
			}
			
			byte cSide = tSide;
			if (getCoverIDAtSide(aSide) != 0) cSide = aSide;
			
			if (getCoverIDAtSide(cSide) == 0) {
				if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sCovers.keySet())) {
					if (GregTech_API.getCoverBehavior(aPlayer.inventory.getCurrentItem()).isCoverPlaceable(cSide, GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem()), this) && mMetaTileEntity.allowCoverOnSide(cSide, GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem()))) {
						setCoverItemAtSide(cSide, aPlayer.inventory.getCurrentItem());
						if (!aPlayer.capabilities.isCreativeMode) aPlayer.inventory.getCurrentItem().stackSize--;
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(100), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
					}
					return true;
				}
			} else {
				if (GT_Utility.isItemStackInList(aPlayer.inventory.getCurrentItem(), GregTech_API.sCrowbarList)) {
					if (GT_ModHandler.damageOrDechargeItem(aPlayer.inventory.getCurrentItem(), 1, 1000, aPlayer)) {
						GT_Utility.sendSoundToPlayers(getWorld(), GregTech_API.sSoundList.get(0), 1.0F, -1, getXCoord(), getYCoord(), getZCoord());
						dropCover(cSide, aSide, false);
					}
					return true;
				}
			}
			
			if (getCoverBehaviorAtSide(aSide).onCoverRightclick(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this, aPlayer, aX, aY, aZ)) return true;
			
			if (!getCoverBehaviorAtSide(aSide).isGUIClickable(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this)) return false;
			
			try {
				if (hasValidMetaTileEntity() && mID > 15) return mMetaTileEntity.onRightclick(aPlayer, aSide, aX, aY, aZ);
	    	} catch(Throwable e) {
	    		GT_Log.err.println("Encountered Exception while rightclicking TileEntity, the Game should've crashed now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
	    		e.printStackTrace(GT_Log.err);
	    	}
		}
		
		return false;
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
		return false;
	}
	
	@Override
	public ItemStack[] getStoredItemData() {
		return null;
	}
	
	@Override
	public void setItemCount(int aCount) {
		//
	}
	
	@Override
	public int getMaxItemCount() {
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
		return hasValidMetaTileEntity() && getCoverBehaviorAtSide((byte)aSide).letsItemsIn ((byte)aSide, getCoverIDAtSide((byte)aSide), getCoverDataAtSide((byte)aSide), this) && mMetaTileEntity.canInsertItem(aIndex, aStack, aSide);
	}
	
	/**
	 * Can pull aStack out of Slot from Side
	 */
	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		return hasValidMetaTileEntity() && getCoverBehaviorAtSide((byte)aSide).letsItemsOut((byte)aSide, getCoverIDAtSide((byte)aSide), getCoverDataAtSide((byte)aSide), this) && mMetaTileEntity.canExtractItem(aIndex, aStack, aSide);
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isMJConverterUpgradable() {
		return false;
	}
	
	@Override
	public boolean isOverclockerUpgradable() {
		return false;
	}
	
	@Override
	public boolean isTransformerUpgradable() {
		return false;
	}
	
	@Override
	public boolean isBatteryUpgradable(int aStorage, byte aTier) {
		return false;
	}
	
	@Override
	public boolean hasMJConverterUpgrade() {
		return false;
	}
	
	@Override
	public byte getOverclockerUpgradeCount() {
		return 0;
	}
	
	@Override
	public byte getTransformerUpgradeCount() {
		return 0;
	}
	
	@Override
	public int getUpgradeStorageVolume() {
		return 0;
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
		return (byte)(getCoverBehaviorAtSide(aSide).manipulatesSidedRedstoneOutput(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this) || (getCoverBehaviorAtSide(aSide).letsRedstoneGoOut(aSide, getCoverIDAtSide(aSide), getCoverDataAtSide(aSide), this))?mSidedRedstone[aSide]&15:0);
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
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasSteamEngineUpgrade() {
		return false;
	}
	
	@Override
	public boolean addMJConverterUpgrade() {
		return false;
	}
	
	@Override
	public boolean addOverclockerUpgrade() {
		return false;
	}
	
	@Override
	public boolean addTransformerUpgrade() {
		return false;
	}
	
	@Override
	public boolean addBatteryUpgrade(int aStorage, byte aTier) {
		return false;
	}
	
	@Override
	public boolean hasInventoryBeenModified() {
		return mInventoryChanged;
	}
	
	@Override
	public void setGenericRedstoneOutput(boolean aOnOff) {
		//
	}
	
	@Override
	public int getErrorDisplayID() {
		return 0;
	}
	
	@Override
	public void setErrorDisplayID(int aErrorID) {
		//
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
		//
	}
	
	@Override
	public void setOnFire() {
		for (byte i = 0; i < 6; i++) {
			Block tBlock = getBlockAtSide(i);
			if (tBlock == null || null == tBlock.getCollisionBoundingBoxFromPool(getWorld(), getXCoord() + getOffsetX(i, 1), getOffsetY(i, 1), getOffsetZ(i, 1))) {
    			getWorld().setBlock(getOffsetX(i, 1), getOffsetY(i, 1), getOffsetZ(i, 1), Block.fire.blockID);
    		}
    	}
	}
	
	@Override
	public int getAverageElectricInput() {
		return 0;
	}
	
	@Override
	public int getAverageElectricOutput() {
		return 0;
	}

	@Override
	public boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced) {
		if (getCoverBehaviorAtSide(aSide).onCoverRemoval(aSide, getCoverIDAtSide(aSide), mCoverData[aSide], this, aForced) || aForced) {
			ItemStack tStack = GT_OreDictUnificator.get(true, getCoverItemAtSide(aSide));
			if (tStack != null) {
				EntityItem tEntity = new EntityItem(getWorld(), getOffsetX(aDroppedSide, 1) + 0.5, getOffsetY(aDroppedSide, 1) + 0.5, getOffsetZ(aDroppedSide, 1) + 0.5, tStack);
				tEntity.motionX = 0;
				tEntity.motionY = 0;
				tEntity.motionZ = 0;
				getWorld().spawnEntityInWorld(tEntity);
			}
			
			setCoverIDAtSide(aSide, 0);
			setOutputRedstoneSignal(aSide, (byte) 0);
			return true;
		}
		return false;
	}
	
	@Override
	public String getOwnerName() {
		return "Player";
	}
	
	@Override
	public String setOwnerName(String aName) {
		return "Player";
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
		return false;
	}
	
	@Override
	public boolean drainEnergyUnits(byte aSide, int aVoltage, int aAmperage) {
		return false;
	}
	
	@Override
	public boolean acceptsRotationalEnergy(byte aSide) {
		return false;
	}
	
	@Override
	public boolean injectRotationalEnergy(byte aSide, int aSpeed, int aEnergy) {
		return false;
	}
    
    @Override
    public int fill(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
    	if (mTickTimer > 5 && hasValidMetaTileEntity() && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput ((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidIn ((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.fill(aSide, aFluid, doFill);
		return 0;
    }
    
	@Override
	public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
		if (mTickTimer > 5 && hasValidMetaTileEntity() && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.drain(aSide, maxDrain, doDrain);
		return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
		if (mTickTimer > 5 && hasValidMetaTileEntity() && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.drain(aSide, aFluid, doDrain);
		return null;
	}
	
	@Override
	public boolean canFill(ForgeDirection aSide, Fluid aFluid) {
		if (mTickTimer > 5 && hasValidMetaTileEntity() && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput ((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidIn ((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.canFill(aSide, aFluid);
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection aSide, Fluid aFluid) {
		if (mTickTimer > 5 && hasValidMetaTileEntity() && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.canDrain(aSide, aFluid);
		return false;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
		if (hasValidMetaTileEntity() && (aSide == ForgeDirection.UNKNOWN || (mMetaTileEntity.isLiquidInput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidIn ((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)) || (mMetaTileEntity.isLiquidOutput((byte)aSide.ordinal()) && getCoverBehaviorAtSide((byte)aSide.ordinal()).letsLiquidOut((byte)aSide.ordinal(), getCoverIDAtSide((byte)aSide.ordinal()), getCoverDataAtSide((byte)aSide.ordinal()), this)))) return mMetaTileEntity.getTankInfo(aSide);
		return new FluidTankInfo[] {};
	}
	
	@Override
	public boolean isInvalidTileEntity() {
		return isInvalid();
	}
	
	@Override
	public boolean addStackToSlot(int aIndex, ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) return true;
		if (aIndex < 0 || aIndex >= getSizeInventory()) return false;
		if (GT_Utility.isStackInvalid(getStackInSlot(aIndex))) {
			setInventorySlotContents(aIndex, aStack);
			return true;
		}
		if (GT_Utility.areStacksEqual(getStackInSlot(aIndex), aStack) && getStackInSlot(aIndex).stackSize + aStack.stackSize <= Math.min(aStack.getMaxStackSize(), getInventoryStackLimit())) {
			getStackInSlot(aIndex).stackSize+=aStack.stackSize;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
		return addStackToSlot(aIndex, GT_Utility.copyAmount(aAmount, aStack));
	}
	
	@Override
	public void setMetaTileEntity(IMetaTileEntity aMetaTileEntity) {
		mMetaTileEntity = (MetaPipeEntity)aMetaTileEntity;
	}
	
	@Override
	public byte getColorization() {
		return (byte)(mColor-1);
	}
	
	@Override
	public byte setColorization(byte aColor) {
		return mColor = (byte)(aColor+1);
	}
	
	public float getThickNess() {
		if (!hasValidMetaTileEntity()) return 0;
		return mMetaTileEntity.getThickNess();
	}
	
	public boolean renderInside() {
		if (!hasValidMetaTileEntity()) return false;
		return mMetaTileEntity.renderInside();
	}
	
	@Override
	public float getBlastResistance(byte aSide) {
		return (mConnections & 192) != 0 ? 50.0F : 5.0F;
	}

	@Override
	public boolean isMufflerUpgradable() {
		return false;
	}

	@Override
	public boolean addMufflerUpgrade() {
		return false;
	}

	@Override
	public boolean hasMufflerUpgrade() {
		return false;
	}
	
	@Override
	public boolean isUniversalEnergyStored(int aEnergyAmount) {
		return getUniversalEnergyStored() >= aEnergyAmount;
	}
	
	@Override
	public String[] getInfoData() {
		{if (hasValidMetaTileEntity()) return getMetaTileEntity().getInfoData(); return new String[]{};}
	}
}