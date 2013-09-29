package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricIngotCompressor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiElectricIngotCompressor extends GuiContainer
{
    private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/electric_IngotCompressor.png");

    private GCCoreTileEntityElectricIngotCompressor tileEntity;

    private int containerWidth;
    private int containerHeight;

    public GCCoreGuiElectricIngotCompressor(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityElectricIngotCompressor tileEntity)
    {
        super(new GCCoreContainerElectricIngotCompressor(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        this.ySize = 197;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 10, 6, 4210752);
        String displayText = "Ingots:";
        this.fontRenderer.drawString(displayText, 50 - this.fontRenderer.getStringWidth(displayText), 32, 4210752);
        this.fontRenderer.drawString(displayText, 50 - this.fontRenderer.getStringWidth(displayText), 53, 4210752);
        displayText = "Battery:";
        this.fontRenderer.drawString(displayText, 50 - this.fontRenderer.getStringWidth(displayText), 74, 4210752);

        if (this.tileEntity.processTicks1 > 0 || this.tileEntity.processTicks2 > 0)
        {
            displayText = mekanism.api.EnumColor.BRIGHT_GREEN + "Running";
        }
        else
        {
            displayText = mekanism.api.EnumColor.ORANGE + "Idle";
        }

        String str = "Status: " + displayText;
        this.fontRenderer.drawString(str, 120 - this.fontRenderer.getStringWidth(str) / 2, 75, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96, 4210752);
        str = ElectricityDisplay.getDisplay(GCCoreTileEntityElectricIngotCompressor.WATTS_PER_TICK_PER_STACK * 20, ElectricUnit.WATT) + " Per";
        this.fontRenderer.drawString(str, 120 - this.fontRenderer.getStringWidth(str) / 2, 85, 4210752);
        str = ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE);
        this.fontRenderer.drawString(str, 120 - this.fontRenderer.getStringWidth(str) / 2, 95, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.bindTexture(GCCoreGuiElectricIngotCompressor.electricFurnaceTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);

        int scale;
        
        if (this.tileEntity.processTicks1 > 0)
        {
            scale = (int) ((double) this.tileEntity.processTicks1 / (double) GCCoreTileEntityElectricFurnace.PROCESS_TIME_REQUIRED * 34);
            this.drawTexturedModalRect(this.containerWidth + 77, this.containerHeight + 26, 176, 13, scale, 17);
        }

        if (this.tileEntity.processTicks2 > 0)
        {
            scale = (int) ((double) this.tileEntity.processTicks2 / (double) GCCoreTileEntityElectricFurnace.PROCESS_TIME_REQUIRED * 34);
            this.drawTexturedModalRect(this.containerWidth + 77, this.containerHeight + 47, 176, 13, scale, 17);
        }

        if (this.tileEntity.getEnergyStored() > 0)
        {
            scale = this.tileEntity.getScaledElecticalLevel(54);
            this.drawTexturedModalRect(this.containerWidth + 116 - 98, this.containerHeight + 6 + 85, 176, 30, scale, 7);
            this.drawTexturedModalRect(this.containerWidth + 4, this.containerHeight + 6 + 84, 176, 37, 11, 10);
        }
        
        if (this.tileEntity.processTicks1 > GCCoreTileEntityElectricIngotCompressor.PROCESS_TIME_REQUIRED / 2 || this.tileEntity.processTicks2 > GCCoreTileEntityElectricIngotCompressor.PROCESS_TIME_REQUIRED / 2)
        {
            this.drawTexturedModalRect(this.containerWidth + 101, this.containerHeight + 18, 176, 0, 15, 13);
        }
    }
}
