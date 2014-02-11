package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiElectricFurnace.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiElectricFurnace extends GuiContainer
{
    private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/electric_furnace.png");

    private TileEntityElectricFurnace tileEntity;

    private int containerWidth;
    private int containerHeight;

    public GuiElectricFurnace(InventoryPlayer par1InventoryPlayer, TileEntityElectricFurnace tileEntity)
    {
        super(new ContainerElectricFurnace(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 45, 6, 4210752);
        this.fontRendererObj.drawString("Smelting:", 10, 28, 4210752);
        this.fontRendererObj.drawString("Battery:", 10, 53, 4210752);
        String displayText = "";

        if (this.tileEntity.processTicks > 0)
        {
            displayText = "Smelting";
        }
        else
        {
            displayText = "Idle";
        }

        this.fontRendererObj.drawString("Status: " + displayText, 82, 45, 4210752);
        this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(TileEntityElectricFurnace.WATTS_PER_TICK * 20, ElectricUnit.WATT), 82, 56, 4210752);
        this.fontRendererObj.drawString("Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F), 82, 68, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.bindTexture(GuiElectricFurnace.electricFurnaceTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);

        if (this.tileEntity.processTicks > 0)
        {
            int scale = (int) ((double) this.tileEntity.processTicks / (double) TileEntityElectricFurnace.PROCESS_TIME_REQUIRED * 23);
            this.drawTexturedModalRect(this.containerWidth + 77, this.containerHeight + 24, 176, 0, 23 - scale, 20);
        }
    }
}
