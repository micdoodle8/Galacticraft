package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.util.GT_Recipe;
import gregtechmod.api.util.GT_Utility;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class GT_MetaTileEntity_BasicGenerator extends GT_MetaTileEntity_BasicTank {
	
	public GT_MetaTileEntity_BasicGenerator(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public GT_MetaTileEntity_BasicGenerator() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isValidSlot(int aIndex)				{return aIndex < 2;}
	@Override public boolean isEnetOutput() 						{return true;}
	@Override public boolean isOutputFacing(byte aSide)				{return true;}
	@Override public int maxEUOutput()								{return getBaseMetaTileEntity().isAllowedToWork()?12:0;}
	@Override public int maxEUStore()								{return 1000000;}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}
    
	@Override public boolean doesFillContainers()	{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean doesEmptyContainers()	{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean canTankBeFilled()		{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean canTankBeEmptied()		{return getBaseMetaTileEntity().isAllowedToWork();}
	@Override public boolean displaysItemStack()	{return true;}
	@Override public boolean displaysStackSize()	{return false;}
	@Override public boolean isFluidInputAllowed(FluidStack aFluid) {return getFuelValue(aFluid) > 0;}
	
    @Override
    public void onPostTick() {
    	if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().isAllowedToWork() && getBaseMetaTileEntity().getTimer()%10==0) {
    		if (mFluid == null) {
    			if (getBaseMetaTileEntity().getUniversalEnergyStored() < (getBaseMetaTileEntity().getOutputVoltage() * 10 + getMinimumStoredEU()))
    				mInventory[2] = null;
    			else
    				if (mInventory[2] == null)
    					mInventory[2] = new ItemStack(Block.fire, 1);
    		} else {
    			int tFuelValue = getFuelValue(mFluid);
    			if (tFuelValue > 0) while (getBaseMetaTileEntity().getUniversalEnergyStored() < (getBaseMetaTileEntity().getOutputVoltage() * 10 + getMinimumStoredEU()) && mFluid.amount > 0) {
    				if (getBaseMetaTileEntity().increaseStoredEnergyUnits(tFuelValue, true)) mFluid.amount--;
    			}
    		}
    		if (mInventory[getInputSlot()] != null && getBaseMetaTileEntity().getUniversalEnergyStored() < (getBaseMetaTileEntity().getOutputVoltage() * 10 + getMinimumStoredEU()) && GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()]) == null) {
    			int tFuelValue = getFuelValue(mInventory[getInputSlot()]);
    			if (tFuelValue > 0) {
        			ItemStack tEmptyContainer = getEmptyContainer(mInventory[getInputSlot()]);
					if (getBaseMetaTileEntity().addStackToSlot(getOutputSlot(), tEmptyContainer)) {
						getBaseMetaTileEntity().increaseStoredEnergyUnits(tFuelValue, true);
						getBaseMetaTileEntity().decrStackSize(getInputSlot(), 1);
					}
    			}
    		}
		}
    	
    	if (getBaseMetaTileEntity().isServerSide()) getBaseMetaTileEntity().setActive(getBaseMetaTileEntity().isAllowedToWork() && getBaseMetaTileEntity().getUniversalEnergyStored() >= getBaseMetaTileEntity().getOutputVoltage() + getMinimumStoredEU());
    }
    
    public abstract Map<Long, GT_Recipe> getRecipeMap();
    
    public abstract int getEfficiency();
    
    public int getFuelValue(FluidStack aLiquid) {
    	if (aLiquid == null) return 0;
    	FluidStack tLiquid;
    	for (GT_Recipe tFuel : getRecipeMap().values()) if ((tLiquid = GT_Utility.getFluidForFilledItem(tFuel.mInput1)) != null) if (aLiquid.isFluidEqual(tLiquid)) return (int)(((long)tFuel.mStartEU * getEfficiency()) / 100);
    	return 0;
    }
    
    public int getFuelValue(ItemStack aStack) {
    	if (GT_Utility.isStackInvalid(aStack)) return 0;
    	GT_Recipe tFuel = GT_Recipe.findEqualRecipe(aStack, null, true, getRecipeMap());
    	if (tFuel != null) return (int)((tFuel.mStartEU * 1000L * getEfficiency()) / 100);
    	return 0;
    }
    
    public ItemStack getEmptyContainer(ItemStack aStack) {
    	if (GT_Utility.isStackInvalid(aStack)) return null;
    	GT_Recipe tFuel = GT_Recipe.findEqualRecipe(aStack, null, true, getRecipeMap());
    	if (tFuel != null) return tFuel.mOutput1;
    	return GT_Utility.getContainerItem(aStack);
    }
    
	@Override
	public int getCapacity() {
		return 10000;
	}
	
	@Override
	public int getTankPressure() {
		return -100;
	}
}
