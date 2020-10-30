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
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerSolarArrayController;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySolarArrayController;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiSolarArrayController extends GuiContainerGC<ContainerSolarArrayController>
{
    private static final ResourceLocation solarGuiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/solar_array_controller.png");

    private final TileEntitySolarArrayController solarController;

    private Button buttonEnableSolar;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiSolarArrayController(ContainerSolarArrayController container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.solarController = container.getController();
        this.ySize = 209;
        this.xSize = 176;
    }

    @Override
    public void init()
    {
        super.init();
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.solarController.getEnergyStoredGC()) + " / " + (int) Math.floor(this.solarController.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 96;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 24;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 90, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> sunGenDesc = new ArrayList<String>();
        float sunVisible = Math.round((this.solarController.getActualArraySize() / (float) this.solarController.getPossibleArraySize()) * 1000) / 10.0F;
        sunGenDesc.add(sunVisible > 0 ? GCCoreUtil.translate("gui.status.sun_visible") + ": " + sunVisible + "%" : GCCoreUtil.translate("gui.status.blockedfully"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 47, (this.height - this.ySize) / 2 + 20, 18, 18, sunGenDesc, this.width, this.height, this));
        this.buttons.add(this.buttonEnableSolar = new Button(this.width / 2 - 36, this.height / 2 - 11, 72, 20, GCCoreUtil.translate("gui.button.enable"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.world), new Object[]{this.solarController.getPos(), 0}));
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int offsetY = 35;
        this.buttonEnableSolar.active = this.solarController.disableCooldown == 0;
        this.buttonEnableSolar.setMessage(!this.solarController.getDisabled(0) ? GCCoreUtil.translate("gui.button.disable") : GCCoreUtil.translate("gui.button.enable"));
        String displayString = this.getTitle().getFormattedText();
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 7, 4210752);
        displayString = GCCoreUtil.translate("gui.message.status") + ": " + this.getStatus();
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 45 + 23 - 46 + offsetY, 4210752);
        displayString = GCCoreUtil.translate("gui.message.generating") + ": " + (this.solarController.generateWatts > 0 ? EnergyDisplayHelper.getEnergyDisplayS(this.solarController.generateWatts) + "/t" : GCCoreUtil.translate("gui.status.not_generating"));
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 34 + 23 - 46 + offsetY, 4210752);
        float boost = Math.round((this.solarController.getSolarBoost() - 1) * 1000) / 10.0F;
        displayString = GCCoreUtil.translate("gui.message.environment") + ": " + boost + "%";
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 56 + 23 - 46 + offsetY, 4210752);
        displayString = GCCoreUtil.translateWithFormat("gui.message.connected_solar_controller", this.solarController.getActualArraySize());
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 67 + 23 - 46 + offsetY, 4210752);
        //		displayString = ElectricityDisplay.getDisplay(this.solarController.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 68 + 23 - 46 + offsetY, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 94, 4210752);
    }

    private String getStatus()
    {
        if (this.solarController.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled");
        }

        if (!this.solarController.getWorld().isDaytime())
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.blockedfully");
        }

        if (this.solarController.getWorld().isRaining() || this.solarController.getWorld().isThundering())
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.raining");
        }

        float sunVisible = (float) Math.floor(this.solarController.getActualArraySize() / (float) this.solarController.getPossibleArraySize());

        if (sunVisible == 0)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.blockedfully");
        }

        if (sunVisible < 1.0F)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.blockedpartial");
        }

        if (this.solarController.generateWatts > 0)
        {
            return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.collectingenergy");
        }

        return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.unknown");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiSolarArrayController.solarGuiTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.solarController.getEnergyStoredGC(), this.solarController.getMaxEnergyStoredGC(), electricityDesc);
//		electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.solarController.getEnergyStoredGC()) + " / " + (int) Math.floor(this.solarController.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.solarController.getEnergyStoredGC() > 0)
        {
            this.blit(var5 + 83, var6 + 24, 176, 0, 11, 10);
        }

        float sunVisible = (float) Math.floor(this.solarController.getActualArraySize() / (float) this.solarController.getPossibleArraySize());

        if (sunVisible > 0.0F)
        {
            this.blit(var5 + 48, var6 + 21, 176, 10, 16, 16);
        }

        this.blit(var5 + 97, var6 + 25, 187, 0, Math.min(this.solarController.getScaledElecticalLevel(54), 54), 7);
    }
}
