package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCircuitFabricator extends GuiContainerGC<ContainerCircuitFabricator>
{
    private static final ResourceLocation CIRCUIT_FABRICATOR_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/circuit_fabricator.png");
    private final TileEntityCircuitFabricator fabricator;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 56, 9, null, 0, 0, this);
    private final GuiElementInfoRegion processInfoRegion = new GuiElementInfoRegion(0, 0, 53, 12, null, 0, 0, this);

    public GuiCircuitFabricator(ContainerCircuitFabricator container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.fabricator = container.getFabricator();
        this.ySize = 192;
    }

    @Override
    protected void init()
    {
        super.init();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 17;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 88;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 68, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.processInfoRegion.tooltipStrings = new ArrayList<String>();
        this.processInfoRegion.xPosition = (this.width - this.xSize) / 2 + 87;
        this.processInfoRegion.yPosition = (this.height - this.ySize) / 2 + 19;
        this.processInfoRegion.parentWidth = this.width;
        this.processInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.processInfoRegion);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 10, 6, 4210752);
        String displayText;

        if (this.fabricator.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.running");
        }
        else
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.idle");
        }

        String str = GCCoreUtil.translate("gui.message.status") + ":";
        this.font.drawString(str, 115 - this.font.getStringWidth(str) / 2, 80, 4210752);
        displayText = this.fabricator.getGUIstatus(displayText, null, true);
        this.font.drawString(displayText, 115 - this.font.getStringWidth(displayText) / 2, 90, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 93, 4210752);
//		str = "" + this.tileEntity.storage.getMaxExtract();
//		this.font.drawString(str, 5, 42, 4210752);
//		//		str = ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE);
//		this.font.drawString(str, 5, 52, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bindTexture(GuiCircuitFabricator.CIRCUIT_FABRICATOR_TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        this.blit(containerWidth + 5, containerHeight + 68, 176, 47, 18, 18);
        this.blit(containerWidth + 5, containerHeight + 89, 194, 47, 9, 8);
        this.blit(containerWidth + 17, containerHeight + 88, 176, 65, 56, 9);
        int scale;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.fabricator.getEnergyStoredGC(), this.fabricator.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.fabricator.processTicks > 0)
        {
            scale = (this.fabricator.processTicks * 100) / this.fabricator.getProcessTimeRequired();
        }
        else
        {
            scale = 0;
        }

        List<String> processDesc = new ArrayList<String>();
        processDesc.clear();
        processDesc.add(GCCoreUtil.translate("gui.electric_compressor.desc.0") + ": " + scale + "%");
        this.processInfoRegion.tooltipStrings = processDesc;

        if (this.fabricator.processTicks > 0)
        {
            scale = (this.fabricator.processTicks * 51) / this.fabricator.getProcessTimeRequired();
            this.blit(containerWidth + 88, containerHeight + 20, 176, 17 + this.fabricator.processTicks % 9 / 3 * 10, scale, 10);
        }

        if (this.fabricator.getEnergyStoredGC() > 0)
        {
            scale = this.fabricator.getScaledElecticalLevel(54);
            this.blit(containerWidth + 116 - 98, containerHeight + 89, 176, 0, scale, 7);
            this.blit(containerWidth + 4, containerHeight + 88, 176, 7, 11, 10);
        }
    }
}
