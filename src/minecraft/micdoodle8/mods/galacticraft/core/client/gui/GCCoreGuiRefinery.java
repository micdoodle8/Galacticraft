package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiRefinery extends GuiContainer
{
    private GCCoreTileEntityRefinery refineryInventory;

    public GCCoreGuiRefinery(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityRefinery par2TileEntityRefinery)
    {
        super(new GCCoreContainerRefinery(par1InventoryPlayer, par2TileEntityRefinery));
        this.refineryInventory = par2TileEntityRefinery;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Fuel Refinery", 6, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/refinery.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7;

        if (!this.refineryInventory.isCookin())
        {
        	return;
        }
        
        var7 = this.refineryInventory.getBurnTimeRemainingScaled(12);
        this.drawTexturedModalRect(var5 + 74, var6 + 45 + 12 - var7, 176, 69 + 12 - var7, 14, var7 + 2);
        var7 = this.refineryInventory.getBurnTimeRemainingScaled(12);
        this.drawTexturedModalRect(var5 + 90, var6 + 45 + 12 - var7, 176, 69 + 12 - var7, 14, var7 + 2);
        
        if (this.refineryInventory.getStackInSlot(0) == null)
        {
        	return;
        }
        
        var7 = this.refineryInventory.getCookProgressScaled(80, 5);
        this.drawTexturedModalRect(var5 + 57, var6 + 38, 176, 0, Math.min(var7 / 4, 30) + 1, 5);
        
        var7 = this.refineryInventory.getCookProgressScaled(34 * 8, 5);
        
        if (var7 > 34 && var7 < 515)
        {
            this.drawTexturedModalRect(var5 + 77, var6 + 44 - (var7 / 8) + 34, 176, 38 - (var7 / 8) + 34, 24, (var7 / 8) + 1 - 34);
        }
        else if (var7 >= 515)
        {
            this.drawTexturedModalRect(var5 + 77, var6 + 44 - (515 / 8) + 34, 176, 38 - (515 / 8) + 34, 24, (515 / 8) + 1 - 34);
        }

        var7 = this.refineryInventory.getCookProgressScaled(36, 5);

        if (var7 > 79)
        {
            this.drawTexturedModalRect(var5 + 101, var6 + 6, 176, 39, ((var7 - 79) / 4) + 1, 4);
        }

        var7 = this.refineryInventory.getCookProgressScaled(36, 5);

        if (var7 > 105)
        {
            this.drawTexturedModalRect(var5 + 107, var6 + 7, 176, 43, 3, Math.min(((var7 - 100) / 4) + 1, 10));
        }

        var7 = this.refineryInventory.getCookProgressScaled(57, 5);

        if (var7 > 215)
        {
            this.drawTexturedModalRect(var5 + 107, var6 + 9, 176, 53, ((var7 - 215) / 4) + 2, 16);
        }
    }
}
