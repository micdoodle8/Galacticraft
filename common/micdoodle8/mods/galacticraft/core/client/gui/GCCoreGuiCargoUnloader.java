package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
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
public class GCCoreGuiCargoUnloader extends GuiContainer
{
    private final GCCoreTileEntityCargoUnloader fuelLoader;

    private GuiButton buttonLoadItems;

    public GCCoreGuiCargoUnloader(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityCargoUnloader par2TileEntityAirDistributor)
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

        this.buttonList.add(this.buttonLoadItems = new GuiButton(0, this.width / 2 - 1, this.height / 2 - 23, 76, 20, "Unload Items"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int offsetX = -17;
        int offsetY = 45;
        this.fontRenderer.drawString("Cargo Unloader", 60, 12, 4210752);
        this.buttonLoadItems.enabled = this.fuelLoader.disableCooldown == 0;
        this.buttonLoadItems.displayString = !this.fuelLoader.getDisabled() ? "Stop Unloading" : "Unload Items";
        this.fontRenderer.drawString("Status: " + this.getStatus(), 28 + offsetX, 45 + 23 - 46 + offsetY, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(GCCoreTileEntityFuelLoader.WATTS_PER_TICK * 20, ElectricUnit.WATT), 28 + offsetX, 56 + 23 - 46 + offsetY, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.fuelLoader.getVoltage(), ElectricUnit.VOLTAGE), 28 + offsetX, 68 + 23 - 46 + offsetY, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90, 4210752);
    }

    private String getStatus()
    {
        if (this.fuelLoader.noTarget)
        {
            return EnumColor.DARK_RED + "No Target to Unload!";
        }

        if (this.fuelLoader.targetEmpty)
        {
            return EnumColor.DARK_RED + "Target is Empty!";
        }

        if (this.fuelLoader.targetNoInventory)
        {
            return EnumColor.DARK_RED + "No Inventory on Target!";
        }

        if (this.fuelLoader.getStackInSlot(0) == null && this.fuelLoader.wattsReceived == 0 && this.fuelLoader.ic2Energy == 0 && this.fuelLoader.bcEnergy == 0)
        {
            return EnumColor.DARK_RED + "Not Enough Power";
        }

        if (this.fuelLoader.getDisabled())
        {
            return EnumColor.ORANGE + "Ready";
        }

        if (this.fuelLoader.wattsReceived > 0 || this.fuelLoader.ic2Energy > 0 || this.fuelLoader.bcEnergy > 0)
        {
            return EnumColor.DARK_GREEN + "Active";
        }

        return EnumColor.ORANGE + "Ready";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/galacticraftcore/textures/gui/cargo_loader.png");
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);
    }
}
