package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

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

        if (this.refineryInventory.isCookin())
        {
            var7 = this.refineryInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(var5 + 56, var6 + 36 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
        }

        var7 = this.refineryInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(var5 + 79, var6 + 34, 176, 14, var7 + 1, 16);
    }
}
