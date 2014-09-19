package micdoodle8.mods.galacticraft.core.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiElectricFurnace extends GuiContainerGC
{
    private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/electric_furnace.png");
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 56, 9, null, 0, 0, this);

    private TileEntityElectricFurnace tileEntity;

    public GuiElectricFurnace(InventoryPlayer par1InventoryPlayer, TileEntityElectricFurnace tileEntity)
    {
        super(new ContainerElectricFurnace(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 39;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 52;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 48, 18, 18, batterySlotDesc, this.width, this.height, this));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 45, 6, 4210752);
        String displayText = "";

        if (this.tileEntity.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.running.name");
        }
        else
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.idle.name");
        }

        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.status.name") + ": " + displayText, 97, 52, 4210752);
//		this.fontRendererObj.drawString("" + this.tileEntity.storage.getMaxExtract(), 97, 56, 4210752);
//		this.fontRendererObj.drawString("Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F), 97, 68, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
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

        int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        int scale;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.tileEntity.getEnergyStoredGC(), this.tileEntity.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.tileEntity.processTicks > 0)
        {
            scale = (int) ((double) this.tileEntity.processTicks / (double) this.tileEntity.processTimeRequired * 23);
            this.drawTexturedModalRect(containerWidth + 78, containerHeight + 24, 176, 0, 23 - scale, 15);
        }

        if (this.tileEntity.getEnergyStoredGC() > 0)
        {
            scale = this.tileEntity.getScaledElecticalLevel(54);
            this.drawTexturedModalRect(containerWidth + 40, containerHeight + 53, 176, 15, scale, 7);
            this.drawTexturedModalRect(containerWidth + 26, containerHeight + 52, 176, 22, 11, 10);
        }
    }
}
