package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiCargoUnloader extends GuiContainerGC
{
	private static final ResourceLocation unloaderTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/cargo_loader.png");

	private final TileEntityCargoUnloader cargoUnloader;

	private GuiButton buttonLoadItems;
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height);

	public GuiCargoUnloader(InventoryPlayer par1InventoryPlayer, TileEntityCargoUnloader par2TileEntityAirDistributor)
	{
		super(new ContainerCargoLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.cargoUnloader = par2TileEntityAirDistributor;
		this.ySize = 201;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.cargoUnloader.xCoord, this.cargoUnloader.yCoord, this.cargoUnloader.zCoord, 0 }));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.cargoUnloader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoUnloader.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 107;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 101;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
		this.buttonList.add(this.buttonLoadItems = new GuiButton(0, this.width / 2 - 1, this.height / 2 - 23, 76, 20, GCCoreUtil.translate("gui.button.unloaditems.name")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int offsetX = -17;
		int offsetY = 45;
		this.fontRendererObj.drawString(this.cargoUnloader.getInventoryName(), 60, 12, 4210752);
		this.buttonLoadItems.enabled = this.cargoUnloader.disableCooldown == 0;
		this.buttonLoadItems.displayString = !this.cargoUnloader.getDisabled(0) ? GCCoreUtil.translate("gui.button.stopunloading.name") : GCCoreUtil.translate("gui.button.unloaditems.name");
		this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus(), 28 + offsetX, 45 + 23 - 46 + offsetY, 4210752);
		this.fontRendererObj.drawString("" + this.cargoUnloader.storage.getMaxExtract(), 28 + offsetX, 56 + 23 - 46 + offsetY, 4210752);
		//		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.cargoUnloader.getVoltage(), ElectricUnit.VOLTAGE), 28 + offsetX, 68 + 23 - 46 + offsetY, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 90, 4210752);
	}

	private String getStatus()
	{
		if (this.cargoUnloader.noTarget)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.notargetunload.name");
		}

		if (this.cargoUnloader.targetEmpty)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.targetempty.name");
		}

		if (this.cargoUnloader.targetNoInventory)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.noinvtarget.name");
		}

		if (this.cargoUnloader.getStackInSlot(0) == null && this.cargoUnloader.getEnergyStoredGC() == 0)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower.name");
		}

		if (this.cargoUnloader.getDisabled(0))
		{
			return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready.name");
		}

		if (this.cargoUnloader.getEnergyStoredGC() > 0)
		{
			return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.active.name");
		}

		return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready.name");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiCargoUnloader.unloaderTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.cargoUnloader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoUnloader.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;

		if (this.cargoUnloader.getEnergyStoredGC() > 0)
		{
			this.drawTexturedModalRect(var5 + 94, var6 + 101, 176, 0, 11, 10);
		}

		this.drawTexturedModalRect(var5 + 108, var6 + 102, 187, 0, Math.min(this.cargoUnloader.getScaledElecticalLevel(54), 54), 7);
	}
}
