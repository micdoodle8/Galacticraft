package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

/**
 * GCCoreGuiCargoLoader.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GuiCargoLoader extends GuiContainerGC
{
	private static final ResourceLocation loaderTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/cargo_loader.png");

	private final TileEntityCargoLoader cargoLoader;

	private GuiButton buttonLoadItems;
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height);

	public GuiCargoLoader(InventoryPlayer par1InventoryPlayer, TileEntityCargoLoader par2TileEntityAirDistributor)
	{
		super(new ContainerCargoLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.cargoLoader = par2TileEntityAirDistributor;
		this.ySize = 201;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.cargoLoader.xCoord, this.cargoLoader.yCoord, this.cargoLoader.zCoord, 0 }));
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
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.cargoLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoLoader.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 107;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 101;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add("Cargo Loader battery slot, place battery here");
		batterySlotDesc.add("if not using a connected power source");
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
		this.buttonList.add(this.buttonLoadItems = new GuiButton(0, this.width / 2 - 1, this.height / 2 - 23, 76, 20, StatCollector.translateToLocal("gui.button.loaditems.name")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int offsetX = -17;
		int offsetY = 45;
		this.fontRendererObj.drawString(this.cargoLoader.getInventoryName(), 60, 12, 4210752);
		this.buttonLoadItems.enabled = this.cargoLoader.disableCooldown == 0;
		this.buttonLoadItems.displayString = !this.cargoLoader.getDisabled(0) ? StatCollector.translateToLocal("gui.button.stoploading.name") : StatCollector.translateToLocal("gui.button.loaditems.name");
		this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.message.status.name") + ": " + this.getStatus(), 28 + offsetX, 45 + 23 - 46 + offsetY, 4210752);
		this.fontRendererObj.drawString("" + this.cargoLoader.storage.getMaxExtract(), 28 + offsetX, 56 + 23 - 46 + offsetY, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90, 4210752);
	}

	private String getStatus()
	{
		if (this.cargoLoader.outOfItems)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.noitems.name");
		}

		if (this.cargoLoader.noTarget)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.notargetload.name");
		}

		if (this.cargoLoader.targetNoInventory)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.noinvtarget.name");
		}

		if (this.cargoLoader.targetFull)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.targetfull.name");
		}

		if (this.cargoLoader.getStackInSlot(0) == null && this.cargoLoader.getEnergyStoredGC() == 0)
		{
			return EnumColor.DARK_RED + StatCollector.translateToLocal("gui.status.missingpower.name");
		}

		if (this.cargoLoader.getDisabled(0))
		{
			return EnumColor.ORANGE + StatCollector.translateToLocal("gui.status.ready.name");
		}

		if (this.cargoLoader.getEnergyStoredGC() > 0)
		{
			return EnumColor.DARK_GREEN + StatCollector.translateToLocal("gui.status.active.name");
		}

		return EnumColor.ORANGE + StatCollector.translateToLocal("gui.status.ready.name");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiCargoLoader.loaderTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add("Electrical Storage");
		electricityDesc.add(EnumColor.YELLOW + "Energy: " + ((int) Math.floor(this.cargoLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoLoader.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;

		if (this.cargoLoader.getEnergyStoredGC() > 0)
		{
			this.drawTexturedModalRect(var5 + 94, var6 + 101, 176, 0, 11, 10);
		}

		this.drawTexturedModalRect(var5 + 108, var6 + 102, 187, 0, Math.min(this.cargoLoader.getScaledElecticalLevel(54), 54), 7);
	}
}
