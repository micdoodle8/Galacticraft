package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiOxygenDecompressor extends GuiContainerGC
{
    private static final ResourceLocation compressorTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/decompressor.png");

    private final TileEntityOxygenDecompressor decompressor;

    private GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height, this);
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiOxygenDecompressor(InventoryPlayer par1InventoryPlayer, TileEntityOxygenDecompressor par2TileEntityAirDistributor)
    {
        super(new ContainerOxygenDecompressor(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.decompressor = par2TileEntityAirDistributor;
        this.ySize = 180;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 31, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> compressorSlotDesc = new ArrayList<String>();
        compressorSlotDesc.add(GCCoreUtil.translate("gui.oxygenDecompressor.slot.desc.0"));
        compressorSlotDesc.add(GCCoreUtil.translate("gui.oxygenDecompressor.slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 132, (this.height - this.ySize) / 2 + 70, 18, 18, compressorSlotDesc, this.width, this.height, this));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(GCCoreUtil.translate("gui.oxygenStorage.desc.0"));
        oxygenDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.oxygenStorage.desc.1") + ": " + ((int) Math.floor(this.decompressor.storedOxygen) + " / " + (int) Math.floor(this.decompressor.maxOxygen)));
        this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
        this.oxygenInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        this.oxygenInfoRegion.yPosition = (this.height - this.ySize) / 2 + 24;
        this.oxygenInfoRegion.parentWidth = this.width;
        this.oxygenInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.oxygenInfoRegion);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.decompressor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.decompressor.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 37;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(this.decompressor.getInventoryName(), 8, 10, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.in.name") + ":", 87, 26, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.in.name") + ":", 87, 38, 4210752);
        String status = GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus();
        this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 50, 4210752);
        status = GCCoreUtil.translate("gui.maxOutput.desc") + ": " + TileEntityOxygenDecompressor.OUTPUT_PER_TICK * 20 + GCCoreUtil.translate("gui.perSecond");
        this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 60, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.decompressor.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.decompressor.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 80, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 104 + 17, 4210752);
    }

    private String getStatus()
    {
        if (this.decompressor.getStackInSlot(0) == null || !(this.decompressor.getStackInSlot(0).getItem() instanceof ItemOxygenTank))
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingtank.name");
        }

        if (this.decompressor.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower.name");
        }

        if (this.decompressor.getStackInSlot(0) != null && this.decompressor.getStackInSlot(0).getItemDamage() == this.decompressor.getStackInSlot(0).getMaxDamage())
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.tankEmpty.name");
        }

        return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.active.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiOxygenDecompressor.compressorTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);

        if (this.decompressor != null)
        {
            int scale = this.decompressor.getCappedScaledOxygenLevel(54);
            this.drawTexturedModalRect(var5 + 113, var6 + 25, 197, 7, Math.min(scale, 54), 7);
            scale = this.decompressor.getScaledElecticalLevel(54);
            this.drawTexturedModalRect(var5 + 113, var6 + 38, 197, 0, Math.min(scale, 54), 7);

            if (this.decompressor.getEnergyStoredGC() > 0)
            {
                this.drawTexturedModalRect(var5 + 99, var6 + 37, 176, 0, 11, 10);
            }

            if (this.decompressor.storedOxygen > 0)
            {
                this.drawTexturedModalRect(var5 + 100, var6 + 24, 187, 0, 10, 10);
            }

            List<String> oxygenDesc = new ArrayList<String>();
            oxygenDesc.add(GCCoreUtil.translate("gui.oxygenStorage.desc.0"));
            oxygenDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.oxygenStorage.desc.1") + ": " + ((int) Math.floor(this.decompressor.storedOxygen) + " / " + (int) Math.floor(this.decompressor.maxOxygen)));
            this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

            List<String> electricityDesc = new ArrayList<String>();
            electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
            EnergyDisplayHelper.getEnergyDisplayTooltip(this.decompressor.getEnergyStoredGC(), this.decompressor.getMaxEnergyStoredGC(), electricityDesc);
//			electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.decompressor.getEnergyStoredGC()) + " / " + (int) Math.floor(this.decompressor.getMaxEnergyStoredGC())));
            this.electricInfoRegion.tooltipStrings = electricityDesc;
        }
    }
}
