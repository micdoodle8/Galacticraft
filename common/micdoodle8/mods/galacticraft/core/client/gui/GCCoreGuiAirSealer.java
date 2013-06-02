package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirSealer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreGuiAirSealer extends GuiContainer
{
    private final GCCoreTileEntityOxygenSealer sealer;
    private GuiButton buttonDisable;

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

        this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 38, this.height / 2 - 30 + 21, 76, 20, "Enable Seal"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Oxygen Sealer", 8, 10, 4210752);
        this.fontRenderer.drawString("In:", 90, 31, 4210752);
        String status = "Status: " + this.getStatus();
        this.buttonDisable.enabled = this.sealer.disableCooldown == 0;
        this.buttonDisable.displayString = this.sealer.disabled ? "Enable Seal" : "Disable Seal";
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 50, 4210752);
        status = "Oxygen Input: " + Math.round(this.sealer.getCappedScaledOxygenLevel(1000) * 10.0D) / 100.0D + "%";
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
            return EnumColor.DARK_RED + "Disabled";
        }

        if (this.sealer.storedOxygen < 1)
        {
            return EnumColor.DARK_RED + "Not Enough Oxygen";
        }

        if (this.sealer.wattsReceived == 0 && this.sealer.ic2Energy == 0 && this.sealer.bcEnergy == 0)
        {
            return EnumColor.DARK_RED + "Not Enough Power";
        }

        if (!this.sealer.sealed)
        {
            return EnumColor.DARK_RED + "Unsealed";
        }

        if (this.sealer.sealed)
        {
            return EnumColor.DARK_GREEN + "Sealed";
        }

        return EnumColor.DARK_RED + "Unknown";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/galacticraftcore/textures/gui/oxygen_large.png");
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 200);

        if (this.sealer != null)
        {
            this.drawTexturedModalRect(var5 + 108, var6 + 26, 176, 0, this.sealer.getCappedScaledOxygenLevel(54), 16);
        }
    }
}
