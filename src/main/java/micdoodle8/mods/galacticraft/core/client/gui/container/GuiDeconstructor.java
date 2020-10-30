package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.inventory.ContainerDeconstructor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDeconstructor;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiDeconstructor extends GuiContainerGC<ContainerDeconstructor>
{
    private static final ResourceLocation guiTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/deconstructor.png");
    private TileEntityDeconstructor deconstructor;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 56, 9, null, 0, 0, this);
    private final GuiElementInfoRegion processInfoRegion = new GuiElementInfoRegion(0, 0, 52, 25, null, 0, 0, this);

    public GuiDeconstructor(ContainerDeconstructor container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerDeconstructor(playerInv, deconstructor), playerInv, new TranslationTextComponent("tile.machine2.10"));
        this.deconstructor = container.getDeconstructor();
        this.ySize = 199;
    }

    @Override
    protected void init()
    {
        super.init();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 17;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 95;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 54, (this.height - this.ySize) / 2 + 74, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.processInfoRegion.tooltipStrings = new ArrayList<String>();
        this.processInfoRegion.xPosition = (this.width - this.xSize) / 2 + 53;//77
        this.processInfoRegion.yPosition = (this.height - this.ySize) / 2 + 28;//30
        this.processInfoRegion.parentWidth = this.width;
        this.processInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.processInfoRegion);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 10, 6, 4210752);
        String displayText;

        if (this.deconstructor.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.running");
        }
        else
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.idle");
        }

        String str = GCCoreUtil.translate("gui.message.status") + ": " + displayText;
        this.font.drawString(str, 120 - this.font.getStringWidth(str) / 2, 75, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 93, 4210752);
//		str = "" + this.tileEntity.storage.getMaxExtract();
//		this.font.drawString(str, 120 - this.font.getStringWidth(str) / 2, 85, 4210752);
//		//		str = ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE);
//		this.font.drawString(str, 120 - this.font.getStringWidth(str) / 2, 95, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bindTexture(GuiDeconstructor.guiTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        int scale;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.deconstructor.getEnergyStoredGC(), this.deconstructor.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.deconstructor.processTicks > 0)
        {
            scale = (int) ((double) this.deconstructor.processTicks / (double) this.deconstructor.processTimeRequired * 100);
        }
        else
        {
            scale = 0;
        }

        List<String> processDesc = new ArrayList<String>();
        processDesc.clear();
        processDesc.add(GCCoreUtil.translate("gui.electric_compressor.desc.0") + ": " + scale + "%");
        this.processInfoRegion.tooltipStrings = processDesc;

        if (this.deconstructor.processTicks > 0)
        {
            scale = (int) ((double) this.deconstructor.processTicks / (double) this.deconstructor.processTimeRequired * 54);
            this.blit(containerWidth + 53, containerHeight + 36, 176, 13, scale, 17);
        }

        if (this.deconstructor.getEnergyStoredGC() > 0)
        {
            scale = this.deconstructor.getScaledElecticalLevel(54);
            this.blit(containerWidth + 116 - 98, containerHeight + 96, 176, 30, scale, 7);
            this.blit(containerWidth + 4, containerHeight + 95, 176, 37, 11, 10);
        }

        if (this.deconstructor.processTicks > this.deconstructor.processTimeRequired / 2)
        {
            this.blit(containerWidth + 77, containerHeight + 28, 176, 0, 15, 13);
        }
    }
}
