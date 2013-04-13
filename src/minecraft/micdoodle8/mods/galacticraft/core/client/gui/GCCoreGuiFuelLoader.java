package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.API.IRefinableItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerFuelLoader;
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
 *  All rights reserved.
 *
 */
public class GCCoreGuiFuelLoader extends GuiContainer
{
    private final GCCoreTileEntityFuelLoader fuelLoaderInv;

    private GuiButton buttonLoadFuel;

	public GCCoreGuiFuelLoader(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityFuelLoader par2TileEntityAirDistributor)
	{
        super(new GCCoreContainerFuelLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.fuelLoaderInv = par2TileEntityAirDistributor;
        this.ySize = 180;
	}

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	switch (par1GuiButton.id)
    	{
    	case 0:
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] {this.fuelLoaderInv.xCoord, this.fuelLoaderInv.yCoord, this.fuelLoaderInv.zCoord}));
			break;
    	}
    }

	@Override
    public void initGui()
    {
    	super.initGui();

        this.buttonList.add(this.buttonLoadFuel = new GuiButton(0, this.width / 2 + 2, this.height / 2 - 49, 76, 20, "Load Fuel"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	this.fontRenderer.drawString("Fuel Loader", 60, 10, 4210752);
        this.buttonLoadFuel.enabled = this.fuelLoaderInv.disableCooldown == 0 && !(this.fuelLoaderInv.fuelTank.getLiquid() == null || this.fuelLoaderInv.fuelTank.getLiquid().amount == 0);
    	this.buttonLoadFuel.displayString = !this.fuelLoaderInv.getDisabled() ? "Stop" : "Load Fuel";
    	this.fontRenderer.drawString("Status: " + this.getStatus(), 28, 45 + 23 - 46, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(GCCoreTileEntityFuelLoader.WATTS_PER_TICK * 20, ElectricUnit.WATT), 28, 56 + 23 - 46, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.fuelLoaderInv.getVoltage(), ElectricUnit.VOLTAGE), 28, 68 + 23 - 46, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 118 + 2 + 11, 4210752);
//        String status = "Status: ";
//        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 30, 4210752);
//        status = this.getStatus();
//        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 40, 4210752);
//        status = ElectricityDisplay.getDisplay(this.fuelLoaderInv.WATTS_PER_TICK * 20, ElectricUnit.WATT);
//        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 50, 4210752);
//        status = ElectricityDisplay.getDisplay(this.fuelLoaderInv.getVoltage(), ElectricUnit.VOLTAGE);
//        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 60, 4210752);
//        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 102, 4210752);
    }

    private String getStatus()
    {
    	if (this.fuelLoaderInv.fuelTank.getLiquid() == null || this.fuelLoaderInv.fuelTank.getLiquid().amount == 0)
    	{
    		return EnumColor.DARK_RED + "No Fuel to Load!";
    	}
    	
    	if (this.fuelLoaderInv.getStackInSlot(0) == null && (this.fuelLoaderInv.wattsReceived == 0 || this.fuelLoaderInv.ic2WattsReceived == 0))
    	{
    		return EnumColor.DARK_RED + "Not Enough Power";
    	}

    	if (this.fuelLoaderInv.getDisabled())
    	{
    		return EnumColor.ORANGE + "Ready";
    	}

    	if (this.fuelLoaderInv.wattsReceived > 0 || this.fuelLoaderInv.ic2WattsReceived > 0)
    	{
    		return EnumColor.DARK_GREEN + "Active";
    	}

//    	if (this.fuelLoaderInv.wattsReceived == 0 && this.fuelLoaderInv.ic2WattsReceived == 0)
//    	{
//    		return EnumColor.DARK_RED + "Not Enough Power";
//    	}

    	return EnumColor.ORANGE + "Ready";
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/gui/loader.png");
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);

//		final int fuelLevel = this.fuelLoaderInv.fuelTank.getLiquid() == null ? 0 : (this.fuelLoaderInv.fuelTank.getLiquid().amount / MathHelper.floor_double((2000.0D / GCCoreItems.fuelCanister.getMaxDamage() + 1)));

		final int fuelLevel = this.fuelLoaderInv.getScaledFuelLevel(38);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 17 + 54 - fuelLevel, 176, 38 - fuelLevel, 16, fuelLevel);
	}
}