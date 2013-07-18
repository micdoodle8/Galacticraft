package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
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
public class GCCoreGuiCargoLoader extends GCCoreGuiContainer
{
    private static final ResourceLocation loaderTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/cargo_loader.png");

    private final GCCoreTileEntityCargoLoader fuelLoader;

    private GuiButton buttonLoadItems;
    private GCCoreInfoRegion electricInfoRegion = new GCCoreInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height);
    
    public GCCoreGuiCargoLoader(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityCargoLoader par2TileEntityAirDistributor)
    {
        super(new GCCoreContainerCargoLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.fuelLoader = par2TileEntityAirDistributor;
        this.ySize = 201;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] { this.fuelLoader.xCoord, this.fuelLoader.yCoord, this.fuelLoader.zCoord }));
            break;
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add("Electrical Storage");
        electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.fuelLoader.getEnergyStored()) + " / " + ((int) Math.floor(this.fuelLoader.getMaxEnergyStored()))));
        electricInfoRegion.tooltipStrings = electricityDesc;
        electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 107;
        electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 101;
        electricInfoRegion.parentWidth = this.width;
        electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add("Cargo Loader battery slot, place battery here");
        batterySlotDesc.add("if not using a connected power source");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
        this.buttonList.add(this.buttonLoadItems = new GuiButton(0, this.width / 2 - 1, this.height / 2 - 23, 76, 20, LanguageRegistry.instance().getStringLocalization("gui.button.loaditems.name")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int offsetX = -17;
        int offsetY = 45;
        this.fontRenderer.drawString(this.fuelLoader.getInvName(), 60, 12, 4210752);
        this.buttonLoadItems.enabled = this.fuelLoader.disableCooldown == 0;
        this.buttonLoadItems.displayString = !this.fuelLoader.getDisabled() ? LanguageRegistry.instance().getStringLocalization("gui.button.stoploading.name") : LanguageRegistry.instance().getStringLocalization("gui.button.loaditems.name");
        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.status.name") + ": " + this.getStatus(), 28 + offsetX, 45 + 23 - 46 + offsetY, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(GCCoreTileEntityFuelLoader.WATTS_PER_TICK * 20, ElectricUnit.WATT), 28 + offsetX, 56 + 23 - 46 + offsetY, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.fuelLoader.getVoltage(), ElectricUnit.VOLTAGE), 28 + offsetX, 68 + 23 - 46 + offsetY, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90, 4210752);
    }

    private String getStatus()
    {
        if (this.fuelLoader.outOfItems)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.noitems.name");
        }

        if (this.fuelLoader.noTarget)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.notargetload.name");
        }

        if (this.fuelLoader.targetNoInventory)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.noinvtarget.name");
        }

        if (this.fuelLoader.targetFull)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.targetfull.name");
        }

        if (this.fuelLoader.getStackInSlot(0) == null && this.fuelLoader.getEnergyStored() == 0)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.missingpower.name");
        }

        if (this.fuelLoader.getDisabled())
        {
            return EnumColor.ORANGE + LanguageRegistry.instance().getStringLocalization("gui.status.ready.name");
        }

        if (this.fuelLoader.getEnergyStored() > 0)
        {
            return EnumColor.DARK_GREEN + LanguageRegistry.instance().getStringLocalization("gui.status.active.name");
        }

        return EnumColor.ORANGE + LanguageRegistry.instance().getStringLocalization("gui.status.ready.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(GCCoreGuiCargoLoader.loaderTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add("Electrical Storage");
        electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.fuelLoader.getEnergyStored()) + " / " + ((int) Math.floor(this.fuelLoader.getMaxEnergyStored()))));
        electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.fuelLoader.getEnergyStored() > 0)
        {
            this.drawTexturedModalRect(var5 + 94, var6 + 101, 176, 0, 11, 10);
        }
        
        this.drawTexturedModalRect(var5 + 108, var6 + 102, 187, 0, Math.min(this.fuelLoader.getScaledElecticalLevel(54), 54), 7);
    }
}
