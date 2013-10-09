package gregtechmod.api.gui;

import gregtechmod.api.interfaces.IGregTechTileEntity;
import gregtechmod.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * The Container I use for all my Basic Machines
 */
public class GT_Container_BasicMachine extends GT_ContainerMetaTile_Machine {
	
	public GT_Container_BasicMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aID) {
		super(aInventoryPlayer, aTileEntity, aID);
	}
	
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity,  1,  35,  25));
        addSlotToContainer(new Slot(mTileEntity,  2,  53,  25));
        addSlotToContainer(new Slot(mTileEntity,  5,  80,  63));
        addSlotToContainer(new GT_Slot_Output(mTileEntity,  3, 107,  25));
        addSlotToContainer(new GT_Slot_Output(mTileEntity,  4, 125,  25));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0,  8, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 26, 63, false, true, 1));
        addSlotToContainer(new GT_Slot_Holo(mTileEntity, 0, 44, 63, false, true, 1));
    }
    
    public boolean mOutput = false, mItemTransfer = false, mSeperatedInputs = false;
    
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
    	if (aSlotIndex < 5) return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	    
    	Slot tSlot = (Slot)inventorySlots.get(aSlotIndex);
	    if (tSlot != null) {
	    	if (mTileEntity.getMetaTileEntity() == null) return null;
		    if (aSlotIndex == 5) {
		    	((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bOutput = !((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bOutput;
			    return null;
		    }
		    if (aSlotIndex == 6) {
		    	((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bItemTransfer = !((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bItemTransfer;
			    return null;
		    }
		    if (aSlotIndex == 7) {
		    	((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bSeperatedInputs = !((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bSeperatedInputs;
			    return null;
		    }
    	}
	    
    	return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }
    
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    	if (mTileEntity.isClientSide() || mTileEntity.getMetaTileEntity() == null) return;
    	
    	mOutput = ((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bOutput;
    	mItemTransfer = ((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bItemTransfer;
    	mSeperatedInputs = ((GT_MetaTileEntity_BasicMachine)mTileEntity.getMetaTileEntity()).bSeperatedInputs;
    	
        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting)var2.next();
            var1.sendProgressBarUpdate(this, 101, mOutput?1:0);
            var1.sendProgressBarUpdate(this, 102, mItemTransfer?1:0);
            var1.sendProgressBarUpdate(this, 103, mSeperatedInputs?1:0);
        }
    }
    
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    	super.updateProgressBar(par1, par2);
    	switch (par1) {
    	case 101: mOutput = (par2 != 0); break;
    	case 102: mItemTransfer = (par2 != 0); break;
    	case 103: mSeperatedInputs = (par2 != 0); break;
    	}
    }
    
    public int getSlotCount() {
    	return 5;
    }
    
    public int getShiftClickSlotCount() {
    	return 2;
    }
}
