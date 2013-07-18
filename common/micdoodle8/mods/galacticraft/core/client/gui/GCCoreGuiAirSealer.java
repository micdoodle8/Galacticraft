package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirSealer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreGuiAirSealer extends GCCoreGuiContainer
{
    private static final ResourceLocation sealerTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/oxygen_large.png");

    private final GCCoreTileEntityOxygenSealer sealer;
    private GuiButton buttonDisable;

    private GCCoreInfoRegion oxygenInfoRegion = new GCCoreInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height);
    private GCCoreInfoRegion electricInfoRegion = new GCCoreInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height);

    public GCCoreGuiAirSealer(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenSealer par2TileEntityAirDistributor)
    {
        super(new GCCoreContainerAirSealer(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.sealer = par2TileEntityAirDistributor;
        this.ySize = 200;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] { this.sealer.xCoord, this.sealer.yCoord, this.sealer.zCoord }));
            break;
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add("Sealer battery slot, place battery here");
        batterySlotDesc.add("if not using a connected power source");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 31, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add("Oxygen Storage");
        oxygenDesc.add(EnumColor.YELLOW + "Oxygen: " + ((int) Math.floor(this.sealer.storedOxygen) + " / " + ((int) Math.floor(this.sealer.maxOxygen))));
        oxygenInfoRegion.tooltipStrings = oxygenDesc;
        oxygenInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        oxygenInfoRegion.yPosition = (this.height - this.ySize) / 2 + 23;
        oxygenInfoRegion.parentWidth = this.width;
        oxygenInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.oxygenInfoRegion);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add("Electrical Storage");
        electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.sealer.getEnergyStored()) + " / " + ((int) Math.floor(this.sealer.getMaxEnergyStored()))));
        electricInfoRegion.tooltipStrings = electricityDesc;
        electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 36;
        electricInfoRegion.parentWidth = this.width;
        electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 38, this.height / 2 - 30 + 21, 76, 20, LanguageRegistry.instance().getStringLocalization("gui.button.enableseal.name")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.sealer.getInvName(), 8, 10, 4210752);
        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.in.name") + ":", 87, 25, 4210752);
        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.in.name") + ":", 87, 37, 4210752);
        String status = LanguageRegistry.instance().getStringLocalization("gui.message.status.name") + ": " + this.getStatus();
        this.buttonDisable.enabled = this.sealer.disableCooldown == 0;
        this.buttonDisable.displayString = this.sealer.disabled ? LanguageRegistry.instance().getStringLocalization("gui.button.enableseal.name") : LanguageRegistry.instance().getStringLocalization("gui.button.disableseal.name");
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 50, 4210752);
        status = LanguageRegistry.instance().getStringLocalization("gui.message.oxinput.name") + ": " + Math.round(this.sealer.getCappedScaledOxygenLevel(1000) * 10.0D) / 100.0D + "%";
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 60, 4210752);
        status = ElectricityDisplay.getDisplay(this.sealer.ueWattsPerTick * 20, ElectricUnit.WATT);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 70, 4210752);
        status = ElectricityDisplay.getDisplay(this.sealer.getVoltage(), ElectricUnit.VOLTAGE);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 80, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90 + 3, 4210752);
    }

    private String getStatus()
    {
        if (this.sealer.disabled)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.disabled.name");
        }

        if (this.sealer.getEnergyStored() == 0)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.missingpower.name");
        }

        if (this.sealer.storedOxygen < 1)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.missingoxygen.name");
        }

        if (!this.sealer.sealed)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.unsealed.name");
        }

        if (this.sealer.sealed)
        {
            return EnumColor.DARK_GREEN + LanguageRegistry.instance().getStringLocalization("gui.status.sealed.name");
        }

        return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.unknown.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(GCCoreGuiAirSealer.sealerTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 200);

        if (this.sealer != null)
        {
            List<String> oxygenDesc = new ArrayList<String>();
            oxygenDesc.add("Oxygen Storage");
            oxygenDesc.add(EnumColor.YELLOW + "Oxygen: " + ((int) Math.floor(this.sealer.storedOxygen) + " / " + ((int) Math.floor(this.sealer.maxOxygen))));
            oxygenInfoRegion.tooltipStrings = oxygenDesc;

            List<String> electricityDesc = new ArrayList<String>();
            electricityDesc.add("Electrical Storage");
            electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.sealer.getEnergyStored()) + " / " + ((int) Math.floor(this.sealer.getMaxEnergyStored()))));
            electricInfoRegion.tooltipStrings = electricityDesc;

            int scale = this.sealer.getCappedScaledOxygenLevel(54);
            this.drawTexturedModalRect(var5 + 113, var6 + 24, 197, 7, Math.min(scale, 54), 7);
            scale = this.sealer.getScaledElecticalLevel(54);
            this.drawTexturedModalRect(var5 + 113, var6 + 37, 197, 0, Math.min(scale, 54), 7);
            
            if (this.sealer.getEnergyStored() > 0)
            {
                this.drawTexturedModalRect(var5 + 99, var6 + 36, 176, 0, 11, 10);
            }
            
            if (this.sealer.storedOxygen > 0)
            {
                this.drawTexturedModalRect(var5 + 100, var6 + 23, 187, 0, 10, 10);
            }
        }
    }
}
