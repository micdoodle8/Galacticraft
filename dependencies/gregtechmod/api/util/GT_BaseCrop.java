package gregtechmod.api.util;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class GT_BaseCrop extends CropCard {
	private String mName = "", mDiscoveredBy = "Gregorius Techneticies", mAttributes[];
	private int mTier = 0, mMaxSize = 0, mAfterHarvestSize = 0, mHarvestSize = 0, mStats[] = new int[5];
	private ItemStack mDrop = null, mSpecialDrops[] = null;
	
	public static ArrayList<GT_BaseCrop> sCropList = new ArrayList<GT_BaseCrop>();
	
	/**
	 * To create new Crops
	 * @param aID Default ID
	 * @param aCropName Name of the Crop
	 * @param aDiscoveredBy The one who discovered the Crop
	 * @param aDrop The Item which is dropped by the Crop. must be != null
	 * @param aBaseSeed Baseseed to plant this Crop. null == crossbreed only
	 * @param aTier tier of the Crop. forced to be >= 1
	 * @param aMaxSize maximum Size of the Crop. forced to be >= 3
	 * @param aGrowthSpeed how fast the Crop grows. if < 0 then its set to Tier*300
	 * @param aHarvestSize the size the Crop needs to be harvested. forced to be between 2 and max size
	 */
	public GT_BaseCrop(int aID, String aCropName, String aDiscoveredBy, ItemStack aDrop, ItemStack[] aSpecialDrops, ItemStack aBaseSeed, int aTier, int aMaxSize, int aGrowthSpeed, int aAfterHarvestSize, int aHarvestSize, int aStatChemical, int aStatFood, int aStatDefensive, int aStatColor, int aStatWeed, String[] aAttributes) {
		mName = aCropName;
		aID = GT_Config.addIDConfig("crops", mName.replaceAll(" ", "_"), aID);
		if (aDiscoveredBy != null && !aDiscoveredBy.equals("")) mDiscoveredBy = aDiscoveredBy;
		if (aDrop != null && aID > 0 && aID < 256) {
			mDrop = GT_Utility.copy(aDrop);
			mSpecialDrops = aSpecialDrops;
			mTier = Math.max(1, aTier);
			mMaxSize = Math.max(3, aMaxSize);
//			mGrowthSpeed = aGrowthSpeed>0?aGrowthSpeed:mTier*300;
			mHarvestSize = Math.min(Math.max(aHarvestSize, 2), mMaxSize);
			mAfterHarvestSize = Math.min(Math.max(aAfterHarvestSize, 1), mMaxSize-1);
			mStats[0] = aStatChemical;
			mStats[1] = aStatFood;
			mStats[2] = aStatDefensive;
			mStats[3] = aStatColor;
			mStats[4] = aStatWeed;
			mAttributes = aAttributes;
			if (!Crops.instance.registerCrop(this, aID)) throw new GT_ItsNotMyFaultException("Make sure the Crop ID is valid!");
			if (aBaseSeed != null) Crops.instance.registerBaseSeed(aBaseSeed, aID, 1, 1, 1, 1);
			sCropList.add(this);
		}
	}
	
	@Override
	public byte getSizeAfterHarvest(ICropTile crop) {
		return (byte)mAfterHarvestSize;
	}
	
	@Override
    public String[] attributes() {
		return mAttributes;
	}
	
	@Override
	public String discoveredBy() {
		return mDiscoveredBy;
	}
	
	@Override
    public final boolean canGrow(ICropTile aCrop) {
        return aCrop.getSize()  < maxSize();
    }
	
	@Override
    public final boolean canBeHarvested(ICropTile aCrop) {
        return aCrop.getSize() >= mHarvestSize;
    }
	
	@Override
	public boolean canCross(ICropTile aCrop) {
		return aCrop.getSize() + 2 > maxSize();
	}
	
	@Override
    public int stat(int n) {
		if (n < 0 || n >= mStats.length) return 0;
		return mStats[n];
    }
	
	@Override
	public String name() {
		return mName;
	}
	
	@Override
	public int tier() {
		return mTier;
	}
	
	@Override
	public int maxSize() {
		return mMaxSize;
	}
	
	@Override
	public ItemStack getGain(ICropTile aCrop) {
		int tDrop = 0;
		if (mSpecialDrops != null && (tDrop = new Random().nextInt(mSpecialDrops.length+4)) < mSpecialDrops.length && mSpecialDrops[tDrop] != null) {
			return GT_Utility.copy(mSpecialDrops[tDrop]);
		}
		if (mDrop.getItem().getContainerItemStack(mDrop)==null) return GT_Utility.copy(mDrop);
		return null;
	}
	
    @Override
    public boolean rightclick(ICropTile aCrop, EntityPlayer aPlayer) {
    	if (!canBeHarvested(aCrop)) return false;
    	ItemStack tContainerItem = mDrop.getItem().getContainerItemStack(mDrop);
    	if (tContainerItem == null) {
    		return aCrop.harvest(aPlayer==null?false:aPlayer instanceof EntityPlayerMP);
    	}
		if (aPlayer!=null&&GT_Utility.areStacksEqual(tContainerItem, aPlayer.getCurrentEquippedItem())&&aPlayer.getCurrentEquippedItem().stackSize>=mDrop.stackSize&&aPlayer.inventory.addItemStackToInventory(GT_Utility.copy(mDrop))) {
			aPlayer.getCurrentEquippedItem().stackSize-=mDrop.stackSize;
			return aCrop.harvest(aPlayer instanceof EntityPlayerMP);
		}
    	return false;
    }
}
