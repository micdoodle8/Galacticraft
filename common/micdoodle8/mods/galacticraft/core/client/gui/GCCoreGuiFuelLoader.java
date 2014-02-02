package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCCoreGuiFuelLoader.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiFuelLoader extends GCCoreGuiContainer
{
	private static final ResourceLocation fuelLoaderTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/fuel_loader.png");

	private final GCCoreTileEntityFuelLoader fuelLoader;

	private GuiButton buttonLoadFuel;
	private GCCoreInfoRegion electricInfoRegion = new GCCoreInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 65, 56, 9, new ArrayList<String>(), this.width, this.height);

	public GCCoreGuiFuelLoader(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityFuelLoader par2TileEntityAirDistributor)
	{
		super(new GCCoreContainerFuelLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.fuelLoader = par2TileEntityAirDistributor;
		this.ySize = 180;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_DISABLEABLE_BUTTON, new Object[] { this.fuelLoader.xCoord, this.fuelLoader.yCoord, this.fuelLoader.zCoord, 0 }));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		List<String> fuelTankDesc = new ArrayList<String>();
		fuelTankDesc.add("The fuel loader fuel tank");
		this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 33, 16, 38, fuelTankDesc, this.width, this.height));
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add("Fuel Loader battery slot, place");
		batterySlotDesc.add("battery here if not using a");
		batterySlotDesc.add("connected power source");
		this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 50, (this.height - this.ySize) / 2 + 54, 18, 18, batterySlotDesc, this.width, this.height));
		List<String> oilSlotDesc = new ArrayList<String>();
		oilSlotDesc.add("Fuel Loader fuel input. Place");
		oilSlotDesc.add("fuel canisters " + (GCCoreCompatibilityManager.isBCraftLoaded() ? "or fuel buckets " : "") + "into this slot");
		oilSlotDesc.add("to load it into the fuel tank");
		this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 6, (this.height - this.ySize) / 2 + 11, 18, 18, oilSlotDesc, this.width, this.height));
		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add("Electrical Storage");
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.fuelLoader.getEnergyStored()) + " / " + (int) Math.floor(this.fuelLoader.getMaxEnergyStored())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 65;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		this.buttonList.add(this.buttonLoadFuel = new GuiButton(0, this.width / 2 + 2, this.height / 2 - 49, 76, 20, StatCollector.translateToLocal("gui.button.loadfuel.name")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.fuelLoader.getInvName(), 60, 10, 4210752);
		this.buttonLoadFuel.enabled = this.fuelLoader.disableCooldown == 0 && !(this.fuelLoader.fuelTank.getFluid() == null || this.fuelLoader.fuelTank.getFluid().amount == 0);
		this.buttonLoadFuel.displayString = !this.fuelLoader.getDisabled(0) ? StatCollector.translateToLocal("gui.button.stoploading.name") : StatCollector.translateToLocal("gui.button.loadfuel.name");
		this.fontRenderer.drawString(StatCollector.translateToLocal("gui.message.status.name") + ": " + this.getStatus(), 28, 45 + 23 - 46, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(GCCoreTileEntityFuelLoader.WATTS_PER_TICK * 20, ElectricUnit.WATT), 28, 56 + 23 - 46, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.fuelLoader.getVoltage(), ElectricUnit.VOLTAGE), 28, 68 + 23 - 46, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 118 + 2 + 11, 4210752);
	}

	private String getStatus()
	{
		if (this.fuelLoader.fuelTank.getFluid() == null || this.fuelLoader.fuelTank.getFluid().amount == 0)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.nofuel.name");
		}

		if (this.fuelLoader.getStackInSlot(0) == null && this.fuelLoader.getEnergyStored() == 0)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.missingpower.name");
		}

		if (this.fuelLoader.getDisabled(0))
		{
			return EnumColor.ORANGE + StatCollector.translateToLocal("gui.status.ready.name");
		}

		if (this.fuelLoader.getEnergyStored() > 0)
		{
			return EnumColor.DARK_GREEN + StatCollector.translateToLocal("gui.status.active.name");
		}

		return EnumColor.ORANGE + StatCollector.translateToLocal("gui.status.ready.name");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GCCoreGuiFuelLoader.fuelLoaderTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);

		final int fuelLevel = this.fuelLoader.getScaledFuelLevel(38);
		this.drawTexturedModalRect((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 17 + 54 - fuelLevel, 176, 38 - fuelLevel, 16, fuelLevel);

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add("Electrical Storage");
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.fuelLoader.getEnergyStored()) + " / " + (int) Math.floor(this.fuelLoader.getMaxEnergyStored())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;

		if (this.fuelLoader.getEnergyStored() > 0)
		{
			this.drawTexturedModalRect(var5 + 99, var6 + 65, 192, 7, 11, 10);
		}

		this.drawTexturedModalRect(var5 + 113, var6 + 66, 192, 0, Math.min(this.fuelLoader.getScaledElecticalLevel(54), 54), 7);
	}
}
