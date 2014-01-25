package gregtechmod.api.items;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_RadioactiveCell_Item extends GT_Generic_Item {

	protected int maxDelay, pulserate, cellCount;
	protected ItemStack mDepleted;
	
    public GT_RadioactiveCell_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDelay, int aCellcount, int aPulseRate, ItemStack aDepleted) {
        super(aID, aUnlocalized, aEnglish, null);
        setMaxStackSize(1);
        setMaxDamage(10000);
        setNoRepair();
        cellCount = Math.max(1, aCellcount);
        pulserate = aPulseRate;
        maxDelay = aMaxDelay;
        mDepleted = aDepleted;
    }
    
    protected boolean outputPulseForStack(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setInteger("output", tNBT.getInteger("output") + 1);
        return pulserate > 0 || tNBT.getInteger("output") % (-pulserate) == 0;
    }
    
    protected boolean incrementPulseForStack(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setInteger("pulse", tNBT.getInteger("pulse") + 1);
        return pulserate > 0 || tNBT.getInteger("pulse") % (-pulserate) == 0;
    }
    
    protected void setDurabilityForStack(ItemStack aStack, int aDurability) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        
        tNBT.setInteger("durability", aDurability);
        
        if (maxDelay > 0) {
            double var4 = ((double)maxDelay-aDurability) / maxDelay;
            int var6 = (int)(aStack.getMaxDamage() * var4);

            if (var6 >= aStack.getMaxDamage()) {
                var6 = aStack.getMaxDamage() - 1;
            }
            aStack.setItemDamage(aStack.getMaxDamage() - var6);
        }
    }
    
    public static int getDurabilityOfStack(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        return tNBT.getInteger("durability");
    }
    
    public int getMaxNuclearDurability() {
    	return maxDelay;
    }
    
	@Override
    public int getItemEnchantability() {
        return 0;
    }
	
	@Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }
	
    protected static int sumUp(int a) {
        int b = 0;
        for (int c = 1; c <= a; ++c) {b += c;}
        return b;
    }
    
	@Override
	public void addAdditionalToolTips(List aList, ItemStack aStack) {
		super.addAdditionalToolTips(aList, aStack);
		aList.add("Time left: " + (maxDelay - getDurabilityOfStack(aStack)) + " secs");
	}
}
