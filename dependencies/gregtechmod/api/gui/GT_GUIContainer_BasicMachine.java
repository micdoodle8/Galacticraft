package gregtechmod.api.gui;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * The GUI-Container I use for all my Basic Machines
 * 
 * As the NEI-RecipeTransferRect Handler can't handle one GUI-Class for all GUIs I needed to produce some dummy-classes which extend this class
 */
public class GT_GUIContainer_BasicMachine extends GT_GUIContainerMetaTile_Machine {
	
	String mName = "";
	
	public final byte mProgressBarDirection, mProgressBarAmount;

    public GT_GUIContainer_BasicMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
        this(aInventoryPlayer, aTileEntity, aName, aTextureFile, (byte)0, (byte)1);
    }
    
    public GT_GUIContainer_BasicMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile, byte aProgressBarDirection, byte aProgressBarAmount) {
        super(new GT_Container_BasicMachine(aInventoryPlayer, aTileEntity), GregTech_API.GUI_PATH + aTextureFile);
        mProgressBarDirection = aProgressBarDirection;
        mProgressBarAmount = (byte)Math.max(1, aProgressBarAmount);
        mName = aName;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(mName, 8,  4, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (mContainer != null) {
        	if (((GT_Container_BasicMachine)mContainer).mOutputting)			drawTexturedModalRect(x +  7, y + 62, 176, 18, 18, 18);
        	if (((GT_Container_BasicMachine)mContainer).mItemTransfer)		drawTexturedModalRect(x + 25, y + 62, 176, 36, 18, 18);
        	if (((GT_Container_BasicMachine)mContainer).mSeperatedInputs)	drawTexturedModalRect(x + 43, y + 62, 176, 54, 18, 18);
        	
        	if (mContainer.mMaxProgressTime > 0) {
	        	int tSize = (mProgressBarDirection < 2 ? 20 : 18), tProgress = Math.max(1, Math.min(tSize * mProgressBarAmount, (mContainer.mProgressTime>0?1:0) + (mContainer.mProgressTime * tSize * mProgressBarAmount) / mContainer.mMaxProgressTime)) % (tSize+1);
	        	
	        	switch (mProgressBarDirection) { // yes, my OCD was mad at me before I did the Tabs.
	        	case 0:								drawTexturedModalRect(x + 78					, y + 24					, 176					, 0					, tProgress	, 18		); break;
	        	case 1:								drawTexturedModalRect(x + 78 + 20 - tProgress	, y + 24					, 176 + 20 - tProgress	, 0					, tProgress	, 18		); break;
	        	case 2:								drawTexturedModalRect(x + 78					, y + 24					, 176					, 0					, 20		, tProgress	); break;
	        	case 3:								drawTexturedModalRect(x + 78					, y + 24 + 18 - tProgress	, 176					, 18 - tProgress	, 20		, tProgress	); break;
	        	case 4: tProgress = 20 - tProgress;	drawTexturedModalRect(x + 78					, y + 24					, 176					, 0					, tProgress	, 18		); break;
	        	case 5: tProgress = 20 - tProgress;	drawTexturedModalRect(x + 78 + 20 - tProgress	, y + 24					, 176 + 20 - tProgress	, 0					, tProgress	, 18		); break;
	        	case 6: tProgress = 18 - tProgress;	drawTexturedModalRect(x + 78					, y + 24					, 176					, 0					, 20		, tProgress	); break;
	        	case 7: tProgress = 18 - tProgress;	drawTexturedModalRect(x + 78					, y + 24 + 18 - tProgress	, 176					, 18 - tProgress	, 20		, tProgress	); break;
	        	}
        	}
        }
    }
}