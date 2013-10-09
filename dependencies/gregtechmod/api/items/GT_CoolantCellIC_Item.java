package gregtechmod.api.items;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.item.ItemStack;

public class GT_CoolantCellIC_Item extends GT_CoolantCell_Item implements IReactorComponent {
    public GT_CoolantCellIC_Item(int aID, String aName, int aMaxStore, int aCellCount) {
        super(aID, aName, aMaxStore, aCellCount);
    }
    
	@Override
	public void processChamber(IReactor aReactor, ItemStack aStack, int x, int y) {
		return;
	}
	
	@Override
	public boolean acceptUraniumPulse(IReactor aReactor, ItemStack aStack, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY) {
		return false;
	}
	
	@Override
	public boolean canStoreHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
		return true;
	}
	
	@Override
	public int getMaxHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
		return heatStorage;
	}
	
	@Override
	public int getCurrentHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
		return getHeatOfStack(aStack);
	}
	
	@Override
	public float influenceExplosion(IReactor aReactor, ItemStack aStack) {
		return 1.0F+heatStorage/30000.0F;
	}
	
	@Override
    public int alterHeat(IReactor aReactor, ItemStack aStack, int x, int y, int aHeat) {
        int tHeat = getHeatOfStack(aStack);
        tHeat += aHeat;

        if (tHeat > heatStorage) {
            aReactor.setItemAt(x, y, (ItemStack)null);
            aHeat = heatStorage - tHeat + 1;
        } else {
            if (tHeat < 0) {
                aHeat = tHeat;
                tHeat = 0;
            } else {
                aHeat = 0;
            }
            setHeatForStack(aStack, tHeat);
        }
        return aHeat;
    }
}
