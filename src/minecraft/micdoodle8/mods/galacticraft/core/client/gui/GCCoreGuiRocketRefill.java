package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerRocketRefill;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiRocketRefill extends GuiContainer
{
    private final IInventory upperChestInventory;
    private final IInventory lowerChestInventory;

    /**
     * window height is calculated with this values, the more rows, the heigher
     */
    private int inventoryRows = 0;
    
    private final int type;

    public GCCoreGuiRocketRefill(IInventory par1IInventory, IInventory par2IInventory, int type)
    {
        super(new GCCoreContainerRocketRefill(par1IInventory, par2IInventory, type));
        this.upperChestInventory = par1IInventory;
        this.lowerChestInventory = par2IInventory;
        this.allowUserInput = false;
        final short var3 = 222;
        final int var4 = var3 - 108;
        this.inventoryRows = par2IInventory.getSizeInventory() / 9;
        this.ySize = type == 0 ? 132 : type == 1 ? 199 : 0;
        this.type = type;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(StatCollector.translateToLocal("Fuel"), (this.width - this.xSize) / 4 + 2, 6, 4210752);
        
    	if (this.type == 1)
    	{
            this.fontRenderer.drawString(StatCollector.translateToLocal("Spaceship"), 8, 38, 4210752);
    	}
    	
        this.fontRenderer.drawString(StatCollector.translateToLocal(this.upperChestInventory.getInvName()), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
    	int var4 = 0;
    	
    	switch (this.type)
    	{
    	case 0:
            var4 = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/rocketrefill-nochest.png");
            break;
    	case 1:
            var4 = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/rocketrefill-chest.png");
            break;
    	}
    	
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, 176, 199);
    }
}
