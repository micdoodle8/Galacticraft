package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirCompressor;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreGuiAirCompressor extends GCCoreGuiContainer
{
    private static final ResourceLocation compressorTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/compressor.png");

    private final GCCoreTileEntityOxygenCompressor compressor;

    public GCCoreGuiAirCompressor(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenCompressor par2TileEntityAirDistributor)
    {
        super(new GCCoreContainerAirCompressor(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.compressor = par2TileEntityAirDistributor;
        this.ySize = 180;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add("Compressor battery slot, place battery here");
        batterySlotDesc.add("if not using a connected power source");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 31, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add("Oxygen input into the compressor");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 25, 56, 18, oxygenDesc, this.width, this.height));
        List<String> compressorSlotDesc = new ArrayList<String>();
        compressorSlotDesc.add("Compressor tank slot, place oxygen tank");
        compressorSlotDesc.add("here to fill it with breathable oxygen.");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 132, (this.height - this.ySize) / 2 + 70, 18, 18, compressorSlotDesc, this.width, this.height));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.compressor.getInvName(), 8, 10, 4210752);
        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.in.name") + ":", 90, 31, 4210752);
        String status = LanguageRegistry.instance().getStringLocalization("gui.message.status.name") + ": " + this.getStatus();
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 50, 4210752);
        status = LanguageRegistry.instance().getStringLocalization("gui.message.oxinput.name") + ": " + Math.round(this.compressor.getCappedScaledOxygenLevel(1000) * 10.0D) / 100.0D + "%";
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 60, 4210752);
        status = ElectricityDisplay.getDisplay(this.compressor.ueWattsPerTick * 20, ElectricUnit.WATT);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 70, 4210752);
        status = ElectricityDisplay.getDisplay(this.compressor.getVoltage(), ElectricUnit.VOLTAGE);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 80, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 104 + 17, 4210752);
    }

    private String getStatus()
    {
        if (this.compressor.getStackInSlot(0) == null || !(this.compressor.getStackInSlot(0).getItem() instanceof GCCoreItemOxygenTank))
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.missingtank.name");
        }

        if (this.compressor.ueWattsReceived == 0 && this.compressor.ic2Energy == 0 && this.compressor.bcEnergy == 0)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.missingpower.name");
        }

        if (this.compressor.getStackInSlot(0) != null && this.compressor.getStackInSlot(0).getItemDamage() == 0)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.fulltank.name");
        }

        if (this.compressor.storedOxygen < 1.0D)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.missingoxygen.name");
        }

        return EnumColor.DARK_GREEN + LanguageRegistry.instance().getStringLocalization("gui.status.active.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(GCCoreGuiAirCompressor.compressorTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);

        if (this.compressor != null)
        {
            this.drawTexturedModalRect(var5 + 108, var6 + 26, 176, 0, this.compressor.getCappedScaledOxygenLevel(54), 16);
        }
    }
}
