package gregtechmod.api.items;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.item.ItemStack;

public class GT_RadioactiveCellIC_Item extends GT_RadioactiveCell_Item implements IReactorComponent {
    public GT_RadioactiveCellIC_Item(int aID, String aUnlocalized, String aEnglish, int aMaxDelay, int aCellcount, int aPulseRate, ItemStack aDepleted) {
        super(aID, aUnlocalized, aEnglish, aMaxDelay, aCellcount, aPulseRate, aDepleted);
    }
    
	@Override
	public boolean acceptUraniumPulse(IReactor aReactor, ItemStack aStack, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean aHeatRun) {
		if (aStack.stackSize != 1) return false;
		return aReactor.addOutput(1) > 0;
	}
	
	@Override
	public boolean canStoreHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
		return false;
	}
	
	@Override
	public int getMaxHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
		return 0;
	}
	
	@Override
	public int getCurrentHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
		return 0;
	}
	
	@Override
	public float influenceExplosion(IReactor aReactor, ItemStack aStack) {
		return aStack.stackSize * cellCount * (pulserate>0?pulserate:1);
	}
	
	@Override
    public int alterHeat(IReactor aReactor, ItemStack aStack, int x, int y, int aHeat) {
        return aHeat;
    }
	
	@Override
	public void processChamber(IReactor aReactor, ItemStack aStack, int x, int y, boolean aHeatRun) {
		//
	}
	
    protected class ItemStackCoord {
        public ItemStack stack;
        public int x;
        public int y;

        final GT_RadioactiveCell_Item mThis;

        public ItemStackCoord(GT_RadioactiveCell_Item var1, ItemStack var2, int var3, int var4) {
        	mThis = var1;
            stack = var2;
            x = var3;
            y = var4;
        }
    }
}