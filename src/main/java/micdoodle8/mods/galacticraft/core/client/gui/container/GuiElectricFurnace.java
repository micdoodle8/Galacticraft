package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerDeconstructor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
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

public class GuiElectricFurnace extends GuiContainerGC<ContainerElectricFurnace>
{
    private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/electric_furnace.png");
    private static final ResourceLocation arcFurnaceTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/electric_arc_furnace.png");
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 56, 9, null, 0, 0, this);

    private TileEntityElectricFurnace furnace;
    private final ResourceLocation texture;

    public GuiElectricFurnace(ContainerElectricFurnace container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerElectricFurnace(playerInv, furnace), playerInv, new TranslationTextComponent(furnace.getTierGC() == 1 ? "tile.machine.2" : "tile.machine.7"));
        this.furnace = container.getFurnace();
        texture = furnace.tierGC == 2 ? arcFurnaceTexture : electricFurnaceTexture;
    }

    @Override
    protected void init()
    {
        super.init();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 39;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 52;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 48, 18, 18, batterySlotDesc, this.width, this.height, this));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 45, 6, 4210752);
        String displayText = "";

        if (this.furnace.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.running");
        }
        else
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.idle");
        }

        this.font.drawString(GCCoreUtil.translate("gui.message.status") + ": ", 97, 52, 4210752);
        this.font.drawString(this.furnace.getGUIstatus(null, displayText, true), 107, 62, 4210752);
//		this.font.drawString("" + this.tileEntity.storage.getMaxExtract(), 97, 56, 4210752);
//		this.font.drawString("Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F), 97, 68, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bindTexture(this.texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        int scale;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.furnace.getEnergyStoredGC(), this.furnace.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.furnace.processTicks > 0)
        {
            scale = (int) ((double) this.furnace.processTicks / (double) this.furnace.processTimeRequired * 23);
            this.blit(containerWidth + 78, containerHeight + 24, 176, 0, 23 - scale, 15);
        }

        if (this.furnace.getEnergyStoredGC() > 0)
        {
            scale = this.furnace.getScaledElecticalLevel(54);
            this.blit(containerWidth + 40, containerHeight + 53, 176, 15, scale, 7);
            this.blit(containerWidth + 26, containerHeight + 52, 176, 22, 11, 10);
        }
    }
}
