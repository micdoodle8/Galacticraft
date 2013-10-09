package gregtechmod.api.metatileentity.implementations;

import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.interfaces.IMetaTileEntity;
import gregtechmod.api.metatileentity.BaseMetaPipeEntity;
import gregtechmod.api.metatileentity.MetaPipeEntity;
import gregtechmod.api.util.GT_Utility;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;

public abstract class GT_MetaPipeEntity_Item extends MetaPipeEntity {
	
	public int mTransferredItems = 0;
	public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;
	
	public GT_MetaPipeEntity_Item(int aID, String mName, String mNameRegional) {
		super(aID, mName, mNameRegional);
	}
	
	public GT_MetaPipeEntity_Item() {
		
	}
	
	@Override public boolean isSimpleMachine()						{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return false;}
	@Override public boolean isValidSlot(int aIndex)				{return true;}
    @Override public final int getInvSize()							{return 1;}
    @Override public int getProgresstime()							{return getPipeContent()*64;}
    @Override public int maxProgresstime()							{return getMaxPipeCapacity()*64;}
	
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setByte("mLastReceivedFrom", mLastReceivedFrom);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
    	mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
	}
	
    @Override
    public void onPostTick() {
	    if (getBaseMetaTileEntity().isServerSide() && getBaseMetaTileEntity().getTimer() % 10 == 0) {
			mConnections = 0;
			if (getBaseMetaTileEntity().getTimer() % 20 == 0) mTransferredItems = 0;
			
			for (byte i = 0; i < 6; i++) {
			   	IInventory tTileEntity = getBaseMetaTileEntity().getIInventoryAtSide(i);
			    if (tTileEntity != null) {
		    		if (tTileEntity.getSizeInventory() <= 0) {
		    			continue;
		    		}
		    		if (tTileEntity instanceof ISidedInventory) {
			    		int[] tSlots = ((ISidedInventory)tTileEntity).getAccessibleSlotsFromSide(GT_Utility.getOppositeSide(i));
			    		if (tSlots == null || tSlots.length <= 0) {
			    			continue;
			    		}
	    			}
		    		if (tTileEntity instanceof IGregTechTileEntity) {
		    			if (getBaseMetaTileEntity().getColorization() >= 0) {
			    			byte tColor = ((IGregTechTileEntity)tTileEntity).getColorization();
			    			if (tColor >= 0 && tColor != getBaseMetaTileEntity().getColorization()) {
			    				continue;
			    			}
			    		}
	    			}
		    		if (getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsItemsIn(i, getBaseMetaTileEntity().getCoverIDAtSide(i), getBaseMetaTileEntity().getCoverDataAtSide(i), getBaseMetaTileEntity())) {
				    	mConnections |= (1<<i);
				    }
				    if (getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsItemsOut(i, getBaseMetaTileEntity().getCoverIDAtSide(i), getBaseMetaTileEntity().getCoverDataAtSide(i), getBaseMetaTileEntity())) {
				    	mConnections |= (1<<i);
				    }
			    }
			}
			
	    	if (oLastReceivedFrom == mLastReceivedFrom && mInventory[0] != null && pipeCapacityCheck()) {
	    		Map<GT_MetaPipeEntity_Item, Integer> tMap = new HashMap<GT_MetaPipeEntity_Item, Integer>();
	    		stepForward(tMap, 0);
	    		tMap = GT_Utility.sortMapByValuesAcending(tMap);
	    		for (GT_MetaPipeEntity_Item tTileEntity : tMap.keySet()) {
	    			tTileEntity.sendItemStack(getBaseMetaTileEntity());
	    			if (mInventory[0] == null) break;
	    		}
	    	}
	    	
			if (mInventory[0] == null) mLastReceivedFrom = 6;
	    	oLastReceivedFrom = mLastReceivedFrom;
		}
    }
    
    private void sendItemStack(IInventory aSender) {
    	if (pipeCapacityCheck()) {
	    	byte tOffset = (byte)getBaseMetaTileEntity().getRandomNumber(6), tSide = 0;
	    	for (byte i = 0; i < 6; i++) {
	    		tSide = (byte)((i+tOffset)%6);
	    		if ((tSide != mLastReceivedFrom || mInventory[0] == null) && (mConnections & (1<<tSide)) != 0) {
	    			insertItemStackIntoTileEntity(aSender, tSide);
		    	}
	    	}
	    	mTransferredItems++;
    	}
    }
    
    public void insertItemStackIntoTileEntity(IInventory aSender, byte aSide) {
    	if (getBaseMetaTileEntity().canExtractItem(0, null, aSide)) {
	    	IInventory tInventory = getBaseMetaTileEntity().getIInventoryAtSide(aSide);
			if (tInventory != null && !(tInventory instanceof BaseMetaPipeEntity)) {
				if (!(tInventory instanceof TileEntityHopper) || !(tInventory instanceof TileEntityDispenser) || getBaseMetaTileEntity().getMetaIDAtSide(aSide) != GT_Utility.getOppositeSide(aSide)) {
					GT_Utility.moveOneItemStack(aSender, tInventory, (byte)6, GT_Utility.getOppositeSide(aSide), null, false, (byte)64, (byte)1, (byte)64, (byte)1);
				}
			}
    	}
    }
    
    public void stepForward(Map<GT_MetaPipeEntity_Item, Integer> aMap, int aStep) {
    	aStep+=getStepSize();
    	if (pipeCapacityCheck()) if (aMap.get(this) == null || aMap.get(this) > aStep) {
    		aMap.put(this, aStep);
	    	for (byte i = 0; i < 6; i++) if ((mConnections & (1<<i)) != 0) {
	    		IInventory tInventory = getBaseMetaTileEntity().getIInventoryAtSide(i);
	    		IMetaTileEntity tMetaTileEntity;
	    		if (tInventory != null && tInventory instanceof BaseMetaPipeEntity && (tMetaTileEntity = ((BaseMetaPipeEntity)tInventory).getMetaTileEntity()) != null && tMetaTileEntity instanceof GT_MetaPipeEntity_Item) {
	    			((GT_MetaPipeEntity_Item)tMetaTileEntity).stepForward(aMap, aStep);
	    		}
	    	}
    	}
    }
    
    private boolean pipeCapacityCheck() {
    	return mTransferredItems == 0 || getPipeContent() < getMaxPipeCapacity();
    }
    
	private int getPipeContent() {
		return mInventory[0] == null ? mTransferredItems : mTransferredItems + 1;
	}
	
	private int getMaxPipeCapacity() {
		return Math.max(1, getPipeCapacity());
	}
	
	/**
	 * Amount of ItemStacks this Pipe can conduct per Second.
	 */
	public abstract int getPipeCapacity();
	
	/**
	 * Can be used to make flow control Pipes, like Redpowers Restriction Tubes.
	 * Every normal Pipe returns a Value of 32768, so you can easily insert lower Numbers to set Routing priorities
	 * Negative Numbers to "suck" Items into a certain direction are also possible.
	 */
	public abstract int getStepSize();
	
	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		return true;
	}
	
	@Override
	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack) {
		return true;
	}
	
	@Override
	public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack) {
		if (mInventory[0] == null) mLastReceivedFrom = aSide;
		return mLastReceivedFrom == aSide;
	}
	
	@Override
	public String getDescription() {
		return "Item Capactiy: "+getMaxPipeCapacity()+" Stacks/sec";
	}
}