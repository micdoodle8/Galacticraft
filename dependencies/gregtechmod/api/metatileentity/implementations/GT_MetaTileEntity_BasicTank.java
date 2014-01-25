package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.enums.GT_Items;
import gregtechmod.api.metatileentity.MetaTileEntity;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This is the main construct for my generic Tanks. Filling and emptying behavior have to be implemented manually
 */
public abstract class GT_MetaTileEntity_BasicTank extends MetaTileEntity {
	
	public FluidStack mFluid;
	
	public GT_MetaTileEntity_BasicTank(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public GT_MetaTileEntity_BasicTank() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isValidSlot(int aIndex)				{return aIndex < 2;}
	@Override public int getInvSize()								{return 3;}
	
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		if (mFluid != null) {
			try {
				aNBT.setCompoundTag("mLiquid", mFluid.writeToNBT(new NBTTagCompound("mLiquid")));
			} catch(Throwable e) {/*Do nothing*/}
		}
	}
	
	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
    	mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mLiquid"));
	}
	
	@Override
	public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
    	if (aSide == 0) return 38;
    	if (aSide == 1) return 79;
    	return 36;
	}
	
	public abstract boolean doesFillContainers();
	public abstract boolean doesEmptyContainers();
	public abstract boolean canTankBeFilled();
	public abstract boolean canTankBeEmptied();
	public abstract boolean displaysItemStack();
	public abstract boolean displaysStackSize();
	public int getInputSlot() {return 0;}
	public int getOutputSlot() {return 1;}
	public int getStackDisplaySlot() {return 2;}
	
	public boolean isFluidInputAllowed(FluidStack aFluid) {return true;}
	public boolean isFluidChangingAllowed() {return true;}
	
	public FluidStack getFillableStack() {return mFluid;}
	public FluidStack setFillableStack(FluidStack aFluid) {mFluid = aFluid; return mFluid;}
	public FluidStack getDrainableStack() {return mFluid;}
	public FluidStack setDrainableStack(FluidStack aFluid) {mFluid = aFluid; return mFluid;}
	
	@Override
	public void onPreTick() {
		if (getBaseMetaTileEntity().isServerSide()) {
			if (isFluidChangingAllowed() && mFluid != null && mFluid.amount <= 0) mFluid = null;
			
			if (displaysItemStack()) {
				if (getDrainableStack() != null) {
					mInventory[getStackDisplaySlot()] = GT_Items.Display_Fluid.getWithDamage(displaysStackSize()?Math.max(1, Math.min(getDrainableStack().amount/1000, 64)):1, getDrainableStack().fluidID);
				} else {
					mInventory[getStackDisplaySlot()] = null;
				}
			}
			
			if (doesEmptyContainers()) {
				FluidStack tFluid = GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()]);
				if (tFluid != null && isFluidInputAllowed(tFluid)) {
					if (getFillableStack() == null) {
						if (isFluidInputAllowed(tFluid) && tFluid.amount <= getCapacity()) {
							if (getBaseMetaTileEntity().addStackToSlot(getOutputSlot(), GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()]), 1)) {
			    				setFillableStack(tFluid.copy());
			    				getBaseMetaTileEntity().decrStackSize(getInputSlot(), 1);
							}
						}
					} else {
						if (tFluid.isFluidEqual(getFillableStack()) && tFluid.amount + getFillableStack().amount <= getCapacity()) {
							if (getBaseMetaTileEntity().addStackToSlot(getOutputSlot(), GT_Utility.getContainerForFilledItem(mInventory[getInputSlot()]), 1)) {
			    				getFillableStack().amount+=tFluid.amount;
			    				getBaseMetaTileEntity().decrStackSize(getInputSlot(), 1);
							}
						}
					}
				}
			}
			
			if (doesFillContainers()) {
				ItemStack tOutput = GT_Utility.fillFluidContainer(getDrainableStack(), mInventory[getInputSlot()]);
				if (tOutput != null && getBaseMetaTileEntity().addStackToSlot(getOutputSlot(), tOutput, 1)) {
					FluidStack tFluid = GT_Utility.getFluidForFilledItem(tOutput);
					getBaseMetaTileEntity().decrStackSize(getInputSlot(), 1);
					if (tFluid != null) getDrainableStack().amount -= tFluid.amount;
					if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) setDrainableStack(null);
				}
			}
		}
	}
	
	@Override
	public final FluidStack getFluid() {
		return getDrainableStack();
	}
	
	@Override
	public final int getFluidAmount() {
		return getDrainableStack() != null ? getDrainableStack().amount : 0;
	}
	
	@Override
	public final int fill(FluidStack aFluid, boolean doFill) {
		if (aFluid == null || aFluid.fluidID <= 0 || !canTankBeFilled() || !isFluidInputAllowed(aFluid)) return 0;
		
		if (getFillableStack() == null || getFillableStack().fluidID <= 0) {
			if(aFluid.amount <= getCapacity()) {
				if (doFill)
					setFillableStack(aFluid.copy());
				return aFluid.amount;
			}
			if (doFill) {
				setFillableStack(aFluid.copy());
				getFillableStack().amount = getCapacity();
				if (getBaseMetaTileEntity()!=null)
					FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(getFillableStack(), getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(), getBaseMetaTileEntity().getZCoord(), this));
			}
			return getCapacity();
		}
		
		if (!getFillableStack().isFluidEqual(aFluid))
			return 0;

		int space = getCapacity() - getFillableStack().amount;
		if (aFluid.amount <= space) {
			if (doFill)
				getFillableStack().amount += aFluid.amount;
			return aFluid.amount;
		}
		if (doFill)
			getFillableStack().amount = getCapacity();
		return space;
	}
	
	@Override
	public final FluidStack drain(int maxDrain, boolean doDrain) {
		if (getDrainableStack() == null || !canTankBeEmptied()) return null;
		if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) {
			setDrainableStack(null);
			return null;
		}
		
		int used = maxDrain;
		if (getDrainableStack().amount < used)
			used = getDrainableStack().amount;
		
		if (doDrain) {
			getDrainableStack().amount -= used;
		}
		
		FluidStack drained = getDrainableStack().copy();
		drained.amount = used;
		
		if (getDrainableStack().amount <= 0 && isFluidChangingAllowed()) {
			setDrainableStack(null);
		}
		
		if (doDrain && getBaseMetaTileEntity()!=null)
			FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(drained, getBaseMetaTileEntity().getWorld(), getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(), getBaseMetaTileEntity().getZCoord(), this));
		
		return drained;
	}
	
	@Override
	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack) {
		return aIndex==1;
	}
	
	@Override
	public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack) {
		return aIndex==0;
	}
}
