package gregtechmod.api.gui;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * The GUI-Container I use for all my Basic Machines
 * 
 * As the NEI-RecipeTransferRect Handler can't handle one GUI-Class for all GUIs I needed to produce some dummy-classes which extend this class
 */
public class GT_GUIContainer_MultiMachine extends GT_GUIContainerMetaTile_Machine {
	
	String mName = "";
	
    public GT_GUIContainer_MultiMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aID, String aName, String aTextureFile) {
        super(new GT_Container_MultiMachine(aInventoryPlayer, aTileEntity, aID), aTileEntity, aID, GregTech_API.GUI_PATH + aTextureFile);
        mName = aName;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(mName, 10,  8, 16448255);
        
        if (mContainer != null) {
        	if ((((GT_Container_MultiMachine)mContainer).mDisplayErrorCode &  1) != 0) fontRenderer.drawString("Pipe is loose.", 10, 16, 16448255);
        	if ((((GT_Container_MultiMachine)mContainer).mDisplayErrorCode &  2) != 0) fontRenderer.drawString("Screws are missing.", 10, 24, 16448255);
        	if ((((GT_Container_MultiMachine)mContainer).mDisplayErrorCode &  4) != 0) fontRenderer.drawString("Something is stuck.", 10, 32, 16448255);
        	if ((((GT_Container_MultiMachine)mContainer).mDisplayErrorCode &  8) != 0) fontRenderer.drawString("Platings are dented.", 10, 40, 16448255);
        	if ((((GT_Container_MultiMachine)mContainer).mDisplayErrorCode & 16) != 0) fontRenderer.drawString("Circuitry burned out.", 10, 48, 16448255);
        	if ((((GT_Container_MultiMachine)mContainer).mDisplayErrorCode & 32) != 0) fontRenderer.drawString("That doesn't belong there.", 10, 56, 16448255);
        	if ((((GT_Container_MultiMachine)mContainer).mDisplayErrorCode & 64) != 0) fontRenderer.drawString("Incomplete Structure.", 10, 64, 16448255);
        	
        	if (((GT_Container_MultiMachine)mContainer).mDisplayErrorCode == 0) {
        		if (((GT_Container_MultiMachine)mContainer).mActive == 0) {
    				fontRenderer.drawString("Hit with Rubber Hammer", 10, 16, 16448255);
    				fontRenderer.drawString("to (re-)start the Machine", 10, 24, 16448255);
    				fontRenderer.drawString("if it doesn't start.", 10, 32, 16448255);
    			} else {
    				fontRenderer.drawString("Running perfectly.", 10, 16, 16448255);
    			}
        	}
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
