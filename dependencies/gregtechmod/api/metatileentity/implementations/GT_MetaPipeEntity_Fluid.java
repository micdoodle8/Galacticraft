package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.interfaces.IMetaTileEntity;
import gregtechmod.api.metatileentity.BaseMetaPipeEntity;
import gregtechmod.api.metatileentity.MetaPipeEntity;
import gregtechmod.api.util.GT_Utility;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class GT_MetaPipeEntity_Fluid extends MetaPipeEntity {
	
	public FluidStack mFluid;
	public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;

	public GT_MetaPipeEntity_Fluid(int aID, String mName, String mNameRegional) {
		super(aID, mName, mNameRegional);
	}
	
	public GT_MetaPipeEntity_Fluid() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return false;}
	@Override public boolean isValidSlot(int aIndex)				{return false;}
    @Override public final int getInvSize()							{return 0;}
    @Override public int getProgresstime()							{return getFluidAmount();}
    @Override public int maxProgresstime()							{return getCapacity();}
	
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		if (mFluid != null) {
			try {
				aNBT.setCompoundTag("mLiquid", mFluid.writeToNBT(new NBTTagCompound("mLiquid")));
			} catch(Throwable e) {}
		}
		aNBT.setByte("mLastReceivedFrom", mLastReceivedFrom);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
    	mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mLiquid"));
    	mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
	}
	
    @Override
    public void onPostTick() {
	    if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().getTimer() % 5 == 0) {
	    	mLastReceivedFrom &= 63;
	    	if (mLastReceivedFrom == 63) {
	    		mLastReceivedFrom = 0;
	    	}
	    	
	    	if (mLastReceivedFrom == oLastReceivedFrom) {
			    HashMap<IFluidHandler, ForgeDirection> tTanks = new HashMap<IFluidHandler, ForgeDirection>();
			    
			    mConnections = 0;
			    
			    for (byte i = 0; i < 6; i++) {
			    	IFluidHandler tTileEntity = getBaseMetaTileEntity().getITankContainerAtSide(i);
			    	if (tTileEntity != null) {
		    			if (tTileEntity instanceof IGregTechTileEntity) {
		    				if (getBaseMetaTileEntity().getColorization() >= 0) {
			    				byte tColor = ((IGregTechTileEntity)tTileEntity).getColorization();
			    				if (tColor >= 0 && tColor != getBaseMetaTileEntity().getColorization()) {
			    					continue;
			    				}
			    			}
	    				}
			    		FluidTankInfo[] tInfo = tTileEntity.getTankInfo(ForgeDirection.getOrientation(i).getOpposite());
			    		if (tInfo != null && tInfo.length > 0) {
					    	if (getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsLiquidIn(i, getBaseMetaTileEntity().getCoverIDAtSide(i), getBaseMetaTileEntity().getCoverDataAtSide(i), getBaseMetaTileEntity())) {
					    		mConnections |= (1<<i);
					    	}
					    	if (getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsLiquidOut(i, getBaseMetaTileEntity().getCoverIDAtSide(i), getBaseMetaTileEntity().getCoverDataAtSide(i), getBaseMetaTileEntity())) {
					    		mConnections |= (1<<i);
					    		if (((1<<i) & mLastReceivedFrom) == 0) tTanks.put(tTileEntity, ForgeDirection.getOrientation(i).getOpposite());
					    	}
			    		}
			    	}
			    }
			    
			    int tAmount = Math.min(getFluidCapacityPerTick()*20, mFluid==null?0:mFluid.amount) / 2;
			    
			    if (tTanks.size() > 0) {
			    	if (tAmount >= tTanks.size()) {
			    		for (IFluidHandler tTileEntity : tTanks.keySet()) {
					    	int tFilledAmount = tTileEntity.fill(tTanks.get(tTileEntity), drain(tAmount / tTanks.size(), false), false);
					    	if (tFilledAmount > 0) {
					    		tTileEntity.fill(tTanks.get(tTileEntity), drain(tFilledAmount, true), true);
					    	}
				    	}
				    } else {
				    	if (mFluid != null && mFluid.amount > 0) {
				    		for (IFluidHandler tTileEntity : tTanks.keySet()) {
						    	int tFilledAmount = tTileEntity.fill(tTanks.get(tTileEntity), drain(mFluid.amount, false), false);
						    	if (tFilledAmount > 0) {
						    		tTileEntity.fill(tTanks.get(tTileEntity), drain(tFilledAmount, true), true);
						    	}
						    	if (mFluid == null || mFluid.amount <= 0) break;
					    	}
				    	}
				    }
			    }
		    	
		    	mLastReceivedFrom = 0;
	    	}
	    	
	    	oLastReceivedFrom = mLastReceivedFrom;
		}
    }
    
    public abstract int getFluidCapacityPerTick();
    
	@Override
	public final int getCapacity() {
		return getFluidCapacityPerTick() * 20;
	}
    
	@Override
	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}
	
	@Override
	public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}
	
	@Override
	public final FluidStack getFluid() {
		return mFluid;
	}
	
	@Override
	public final int getFluidAmount() {
		return mFluid != null ? mFluid.amount : 0;
	}
	
	@Override
	public final int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
		if (aFluid == null || aFluid.fluidID <= 0) return 0;
		
		if (mFluid == null || mFluid.fluidID <= 0) {
			if(aFluid.amount <= getCapacity()) {
				if (doFill) {
					mFluid = aFluid.copy();
					mLastReceivedFrom |= (1<<aSide.ordinal());
				}
				return aFluid.amount;
			} else {
				if (doFill) {
					mFluid = aFluid.copy();
					mLastReceivedFrom |= (1<<aSide.ordinal());
					mFluid.amount = getCapacity();
					if (getBaseMetaTileEntity()!=null)
						FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(mFluid, getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(), getBaseMetaTileEntity().getZCoord(), this));
				}
				return getCapacity();
			}
		}
		
		if (!mFluid.isFluidEqual(aFluid)) return 0;
		
		int space = getCapacity() - mFluid.amount;
		if (aFluid.amount <= space) {
			if (doFill) {
				mFluid.amount += aFluid.amount;
				mLastReceivedFrom |= (1<<aSide.ordinal());
			}
			return aFluid.amount;
		} else {
			if (doFill) {
				mFluid.amount = getCapacity();
				mLastReceivedFrom |= (1<<aSide.ordinal());
			}
			return space;
		}
	}
	
	@Override
	public final FluidStack drain(int maxDrain, boolean doDrain) {
		if (mFluid == null) return null;
		if (mFluid.amount <= 0) {
			mFluid = null;
			return null;
		}
		
		int used = maxDrain;
		if (mFluid.amount < used)
			used = mFluid.amount;
		
		if (doDrain) {
			mFluid.amount -= used;
		}
		
		FluidStack drained = mFluid.copy();
		drained.amount = used;
		
		if (mFluid.amount <= 0) {
			mFluid = null;
		}
		
		if (doDrain && getBaseMetaTileEntity()!=null) FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drained, getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(), getBaseMetaTileEntity().getZCoord(), this));
		
		return drained;
	}
	
	@Override
	public int getTankPressure() {
		return (mFluid==null?0:mFluid.amount) - (getCapacity()/2);
	}
	
	@Override
	public String getDescription() {
		return "Fluid Capacity: " + (getFluidCapacityPerTick()*20) + "L/sec";
	}
}