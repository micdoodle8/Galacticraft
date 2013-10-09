package gregtechmod.api.gui;

import org.lwjgl.opengl.GL11;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemDye;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * The GUI-Container I use for all my MetaTileEntities
 */
public class GT_GUIContainerMetaTile_Machine extends GT_GUIContainer {
	
	public final GT_ContainerMetaTile_Machine mContainer;
	public final int mID;
	
	public GT_GUIContainerMetaTile_Machine(GT_ContainerMetaTile_Machine aContainer, IGregTechTileEntity aTileEntity, int aID, String aGUIbackground) {
		super(aContainer, aGUIbackground);
        mContainer = aContainer;
		mID = aID;
	}
	
    public GT_GUIContainerMetaTile_Machine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, int aID, String aGUIbackground) {
        this(new GT_ContainerMetaTile_Machine(aInventoryPlayer, aTileEntity, aID), aTileEntity, aID, aGUIbackground);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    	super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    	if (GregTech_API.sColoredGUI && mContainer != null && mContainer.mTileEntity != null) {
    		int tColor = mContainer.mTileEntity.getColorization();
    		if (tColor >= 0 && tColor < ItemDye.dyeColors.length) {
    			tColor = ItemDye.dyeColors[tColor];
    			GL11.glColor4f(((float)((tColor >> 16) & 255)) / 255.0F, ((float)((tColor >> 8) & 255)) / 255.0F, ((float)(tColor & 255)) / 255.0F, 1.0F);
	    	} else GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	} else GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}