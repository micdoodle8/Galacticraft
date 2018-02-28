package micdoodle8.mods.galacticraft.planets.venus.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerGeothermal;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityGeothermalGenerator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiGeothermal extends GuiContainerGC
{
    private static final ResourceLocation backgroundTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/geothermal.png");

    private final TileEntityGeothermalGenerator geothermalGenerator;

    private GuiButton buttonEnableSolar;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiGeothermal(InventoryPlayer par1InventoryPlayer, TileEntityGeothermalGenerator geothermalGenerator)
    {
        super(new ContainerGeothermal(par1InventoryPlayer, geothermalGenerator));
        this.geothermalGenerator = geothermalGenerator;
        this.ySize = 201;
        this.xSize = 176;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(this.mc.theWorld), new Object[] { this.geothermalGenerator.getPos(), 0 }));
            break;
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.geothermalGenerator.getEnergyStoredGC()) + " / " + (int) Math.floor(this.geothermalGenerator.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 96;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 24;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 82, 18, 18, batterySlotDesc, this.width, this.height, this));
//        List<String> sunGenDesc = new ArrayList<String>();
//        float sunVisible = Math.round(this.geothermalGenerator.solarStrength / 9.0F * 1000) / 10.0F;
//        sunGenDesc.add(this.geothermalGenerator.solarStrength > 0 ? GCCoreUtil.translate("gui.status.sun_visible.name") + ": " + sunVisible + "%" : GCCoreUtil.translate("gui.status.blockedfully.name"));
//        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 47, (this.height - this.ySize) / 2 + 20, 18, 18, sunGenDesc, this.width, this.height, this));
        this.buttonList.add(this.buttonEnableSolar = new GuiButton(0, this.width / 2 - 36, this.height / 2 - 19, 72, 20, GCCoreUtil.translate("gui.button.enable.name")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int offsetY = 35;
        this.buttonEnableSolar.enabled = this.geothermalGenerator.disableCooldown == 0;
        this.buttonEnableSolar.displayString = !this.geothermalGenerator.getDisabled(0) ? GCCoreUtil.translate("gui.button.disable.name") : GCCoreUtil.translate("gui.button.enable.name");
        String displayString = this.geothermalGenerator.getName();
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 7, 4210752);
        displayString = GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus();
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 45 + 23 - 46 + offsetY, 4210752);
        displayString = this.getStatus2();
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 56 + 23 - 46 + offsetY, 4210752);
        displayString = GCCoreUtil.translate("gui.message.generating.name") + ": " + (this.geothermalGenerator.generateWatts > 0 ? EnergyDisplayHelper.getEnergyDisplayS(this.geothermalGenerator.generateWatts) + "/t" : GCCoreUtil.translate("gui.status.not_generating.name"));
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 34 + 23 - 46 + offsetY, 4210752);
//        float boost = Math.round((this.geothermalGenerator.getSolarBoost() - 1) * 1000) / 10.0F;
//        displayString = GCCoreUtil.translate("gui.message.environment.name") + ": " + boost + "%";
//        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 56 + 23 - 46 + offsetY, 4210752);
        //		displayString = ElectricityDisplay.getDisplay(this.geothermalGenerator.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 68 + 23 - 46 + offsetY, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 94, 4210752);
    }

    private String getStatus()
    {
        if (this.geothermalGenerator.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
        }

        if (this.geothermalGenerator.generateWatts > 0)
        {
            return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.collectingenergy.name");
        }

        if (!this.geothermalGenerator.hasValidSpout())
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.status.invalid_spout_1.name");
        }

        return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.unknown.name");
    }

    private String getStatus2()
    {
        if (this.geothermalGenerator.getDisabled(0) || this.geothermalGenerator.generateWatts > 0)
        {
            return "";
        }

        if (!this.geothermalGenerator.hasValidSpout())
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.status.invalid_spout_2.name");
        }

        return "";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiGeothermal.backgroundTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.geothermalGenerator.getEnergyStoredGC(), this.geothermalGenerator.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.geothermalGenerator.getEnergyStoredGC() > 0)
        {
            this.drawTexturedModalRect(var5 + 83, var6 + 24, 176, 0, 11, 10);
        }

        if (this.geothermalGenerator.hasValidSpout())
        {
            int ySize = 16 * this.geothermalGenerator.generateWatts / (TileEntityGeothermalGenerator.MAX_GENERATE_GJ_PER_TICK - 1);
            this.drawTexturedModalRect(var5 + 33, var6 + 21 + (16 - ySize), 176, (int) (10 + ((this.geothermalGenerator.ticks / 3) % 7) * 16) + (16 - ySize), 46, ySize);
        }

        this.drawTexturedModalRect(var5 + 97, var6 + 25, 187, 0, Math.min(this.geothermalGenerator.getScaledElecticalLevel(54), 54), 7);
    }
}
