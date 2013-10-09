package gregtechmod.api.gui;

import gregtechmod.api.interfaces.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * The Container I use for all my Basic Machines
 */
public class GT_Container_MultiMachine extends GT_ContainerMetaTile_Machine {
	public GT_Container_MultiMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aID) {
		super(aInventoryPlayer, aTileEntity, aID);
	}
	
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity,  1, 152,   5));
    }
    
    public int getSlotCount() {
    	return 1;
    }
    
    public int getShiftClickSlotCount() {
    	return 1;
    }
}
