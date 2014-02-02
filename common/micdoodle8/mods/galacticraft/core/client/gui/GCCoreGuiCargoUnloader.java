package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCCoreGuiCargoUnloader.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiCargoUnloader extends GCCoreGuiContainer
{
	private static final ResourceLocation unloaderTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/cargo_loader.png");

	private final GCCoreTileEntityCargoUnloader cargoUnloader;

	private GuiButton buttonLoadItems;
	private GCCoreInfoRegion electricInfoRegion = new GCCoreInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height);

	public GCCoreGuiCargoUnloader(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityCargoUnloader par2TileEntityAirDistributor)
	{
		super(new GCCoreContainerCargoLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.cargoUnloader = par2TileEntityAirDistributor;
		this.ySize = 201;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_DISABLEABLE_BUTTON, new Object[] { this.cargoUnloader.xCoord, this.cargoUnloader.yCoord, this.cargoUnloader.zCoord, 0 }));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add("Electrical Storage");
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.cargoUnloader.getEnergyStored()) + " / " + (int) Math.floor(this.cargoUnloader.getMaxEnergyStored())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 107;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 101;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add("Cargo Unloader battery slot, place battery");
		batterySlotDesc.add("here if not using a connected power source");
		this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
		this.buttonList.add(this.buttonLoadItems = new GuiButton(0, this.width / 2 - 1, this.height / 2 - 23, 76, 20, StatCollector.translateToLocal("gui.button.unloaditems.name")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int offsetX = -17;
		int offsetY = 45;
		this.fontRenderer.drawString(this.cargoUnloader.getInvName(), 60, 12, 4210752);
		this.buttonLoadItems.enabled = this.cargoUnloader.disableCooldown == 0;
		this.buttonLoadItems.displayString = !this.cargoUnloader.getDisabled(0) ? StatCollector.translateToLocal("gui.button.stopunloading.name") : StatCollector.translateToLocal("gui.button.unloaditems.name");
		this.fontRenderer.drawString(StatCollector.translateToLocal("gui.message.status.name") + ": " + this.getStatus(), 28 + offsetX, 45 + 23 - 46 + offsetY, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(GCCoreTileEntityCargoUnloader.WATTS_PER_TICK * 20, ElectricUnit.WATT), 28 + offsetX, 56 + 23 - 46 + offsetY, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.cargoUnloader.getVoltage(), ElectricUnit.VOLTAGE), 28 + offsetX, 68 + 23 - 46 + offsetY, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90, 4210752);
	}

	private String getStatus()
	{
		if (this.cargoUnloader.noTarget)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.notargetunload.name");
		}

		if (this.cargoUnloader.targetEmpty)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.targetempty.name");
		}

		if (this.cargoUnloader.targetNoInventory)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.noinvtarget.name");
		}

		if (this.cargoUnloader.getStackInSlot(0) == null && this.cargoUnloader.getEnergyStored() == 0)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.missingpower.name");
		}

		if (this.cargoUnloader.getDisabled(0))
		{
			return EnumColor.ORANGE + StatCollector.translateToLocal("gui.status.ready.name");
		}

		if (this.cargoUnloader.getEnergyStored() > 0)
		{
			return EnumColor.DARK_GREEN + StatCollector.translateToLocal("gui.status.active.name");
		}

		return EnumColor.ORANGE + StatCollector.translateToLocal("gui.status.ready.name");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GCCoreGuiCargoUnloader.unloaderTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add("Electrical Storage");
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.cargoUnloader.getEnergyStored()) + " / " + (int) Math.floor(this.cargoUnloader.getMaxEnergyStored())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;

		if (this.cargoUnloader.getEnergyStored() > 0)
		{
			this.drawTexturedModalRect(var5 + 94, var6 + 101, 176, 0, 11, 10);
		}

		this.drawTexturedModalRect(var5 + 108, var6 + 102, 187, 0, Math.min(this.cargoUnloader.getScaledElecticalLevel(54), 54), 7);
	}
}
