package gregtechmod.api.items;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class GT_RadioactiveCellIC_Item extends GT_RadioactiveCell_Item implements IReactorComponent {
    public GT_RadioactiveCellIC_Item(int aID, String aName, int aMaxDelay, int aCellcount, int aPulseRate, ItemStack aDepleted) {
        super(aID, aName, aMaxDelay, aCellcount, aPulseRate, aDepleted);
    }
    
	@Override
	public boolean acceptUraniumPulse(IReactor aReactor, ItemStack aStack, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY) {
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
		return (float)(aStack.stackSize * cellCount * (pulserate>0?pulserate:1));
	}
	
	@Override
    public int alterHeat(IReactor aReactor, ItemStack aStack, int x, int y, int aHeat) {
        return aHeat;
    }
    
    public void processChamber(IReactor aReactor, ItemStack aStack, int x, int y) {
		if (aStack.stackSize > 1) return;
        if (aReactor.produceEnergy()) {
	        for (byte j = 0; j < cellCount; ++j) {
	            
                if (pulserate != 0 && outputPulseForStack(aStack)) for (byte i = 0; i < pulserate || i == 0; i++) {
    	            int tPulsables = 1 + cellCount / 2;
    	            
    	            for (byte k = 0; k < tPulsables; ++k) {
    	            	acceptUraniumPulse(aReactor, aStack, aStack, x, y, x, y);
    	            }
    	            
            		tPulsables += checkPulseable(aReactor, x - 1, y, aStack, x, y) + this.checkPulseable(aReactor, x + 1, y, aStack, x, y) + this.checkPulseable(aReactor, x, y - 1, aStack, x, y) + this.checkPulseable(aReactor, x, y + 1, aStack, x, y);
	                int tAddedHeat = sumUp(tPulsables) * 4;
	                ArrayList tList = new ArrayList<ItemStackCoord>();
	                this.checkHeatAcceptor(aReactor, x - 1, y, tList);
	                this.checkHeatAcceptor(aReactor, x + 1, y, tList);
	                this.checkHeatAcceptor(aReactor, x, y - 1, tList);
	                this.checkHeatAcceptor(aReactor, x, y + 1, tList);
	                
	                while (tList.size() > 0 && tAddedHeat > 0) {
	                    int tDistributedHeatPerStack = tAddedHeat / tList.size();
	                    tAddedHeat -= tDistributedHeatPerStack;
	                    tDistributedHeatPerStack = ((IReactorComponent)((ItemStackCoord)tList.get(0)).stack.getItem()).alterHeat(aReactor, ((ItemStackCoord)tList.get(0)).stack, ((ItemStackCoord)tList.get(0)).x, ((ItemStackCoord)tList.get(0)).y, tDistributedHeatPerStack);
	                    tAddedHeat += tDistributedHeatPerStack;
	                    tList.remove(0);
	                }
	                
	                if (tAddedHeat > 0) {
	                    aReactor.addHeat(tAddedHeat);
	                }
                }
	        }
	        
            if (getDurabilityOfStack(aStack) >= maxDelay - 1) {
                aReactor.setItemAt(x, y, null);
                if (mDepleted != null) {
	                for (int i = 0; i < cellCount; i++) {
	                	if (aReactor.getWorld().rand.nextInt(4) == 0) {
	                		if (aReactor.getItemAt(x, y) == null) {
	                            aReactor.setItemAt(x, y, mDepleted.copy());
	                		} else {
	                			aReactor.getItemAt(x, y).stackSize += mDepleted.stackSize;
	                		}
	                	}
                	}
                }
            } else {
                setDurabilityForStack(aStack, getDurabilityOfStack(aStack) + 1);
            }
	    }
    }

    protected int checkPulseable(IReactor var1, int var2, int var3, ItemStack var4, int var5, int var6) {
        ItemStack var7 = var1.getItemAt(var2, var3);
        return var7 != null && var7.getItem() instanceof IReactorComponent && ((IReactorComponent)var7.getItem()).acceptUraniumPulse(var1, var7, var4, var2, var3, var5, var6) ? 1 : 0;
    }
    
    protected void checkHeatAcceptor(IReactor var1, int var2, int var3, ArrayList var4) {
        ItemStack var5 = var1.getItemAt(var2, var3);

        if (var5 != null && var5.getItem() instanceof IReactorComponent && ((IReactorComponent)var5.getItem()).canStoreHeat(var1, var5, var2, var3)) {
            var4.add(new ItemStackCoord(this, var5, var2, var3));
        }
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