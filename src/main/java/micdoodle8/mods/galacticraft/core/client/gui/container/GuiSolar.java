package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSolar;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;



public class GuiSolar extends GuiContainerGC
{
	private static final ResourceLocation solarGuiTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/solar.png");

	private final TileEntitySolar solarPanel;

	private GuiButton buttonEnableSolar;
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height);

	public GuiSolar(InventoryPlayer par1InventoryPlayer, TileEntitySolar solarPanel)
	{
		super(new ContainerSolar(par1InventoryPlayer, solarPanel));
		this.solarPanel = solarPanel;
		this.ySize = 201;
		this.xSize = 176;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.solarPanel.xCoord, this.solarPanel.yCoord, this.solarPanel.zCoord, 0 }));
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
		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.solarPanel.getEnergyStoredGC()) + " / " + (int) Math.floor(this.solarPanel.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 96;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 24;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 82, 18, 18, batterySlotDesc, this.width, this.height));
		List<String> sunGenDesc = new ArrayList<String>();
		float sunVisible = Math.round(this.solarPanel.solarStrength / 9.0F * 1000) / 10.0F;
		sunGenDesc.add(this.solarPanel.solarStrength > 0 ? GCCoreUtil.translate("gui.status.sunVisible.name") + ": " + sunVisible + "%" : GCCoreUtil.translate("gui.status.blockedfully.name"));
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 47, (this.height - this.ySize) / 2 + 20, 18, 18, sunGenDesc, this.width, this.height));
		this.buttonList.add(this.buttonEnableSolar = new GuiButton(0, this.width / 2 - 36, this.height / 2 - 10, 72, 20, GCCoreUtil.translate("gui.button.enable.name")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		int offsetY = 35;
		this.buttonEnableSolar.enabled = this.solarPanel.disableCooldown == 0;
		this.buttonEnableSolar.displayString = !this.solarPanel.getDisabled(0) ? GCCoreUtil.translate("gui.button.disable.name") : GCCoreUtil.translate("gui.button.enable.name");
		String displayString = this.solarPanel.getInventoryName();
		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 7, 4210752);
		displayString = GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus();
		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 45 + 23 - 46 + offsetY, 4210752);
		displayString = GCCoreUtil.translate("gui.message.generating.name") + ": " + (this.solarPanel.generateWatts > 0 ? ElectricityDisplay.getDisplay(this.solarPanel.generateWatts * 20.0F, ElectricUnit.WATT) : "Not Generating");
		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 34 + 23 - 46 + offsetY, 4210752);
		float boost = Math.round((this.solarPanel.getSolarBoost() - 1) * 1000) / 10.0F;
		displayString = GCCoreUtil.translate("gui.message.environment.name") + ": " + boost + "%";
		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 56 + 23 - 46 + offsetY, 4210752);
//		displayString = ElectricityDisplay.getDisplay(this.solarPanel.getVoltage(), ElectricUnit.VOLTAGE);
//		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 68 + 23 - 46 + offsetY, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 90, 4210752);
	}

	private String getStatus()
	{
		if (this.solarPanel.getDisabled(0))
		{
			return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
		}

		if (!this.solarPanel.getWorldObj().isDaytime())
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.blockedfully.name");
		}

		if (this.solarPanel.getWorldObj().isRaining() || this.solarPanel.getWorldObj().isThundering())
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.raining.name");
		}

		if (this.solarPanel.solarStrength == 0)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.blockedfully.name");
		}

		if (this.solarPanel.solarStrength < 9)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.blockedpartial.name");
		}

		if (this.solarPanel.generateWatts > 0)
		{
			return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.collectingenergy.name");
		}

		return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.unknown.name");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiSolar.solarGuiTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.solarPanel.getEnergyStoredGC()) + " / " + (int) Math.floor(this.solarPanel.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;

		if (this.solarPanel.getEnergyStoredGC() > 0)
		{
			this.drawTexturedModalRect(var5 + 83, var6 + 24, 176, 0, 11, 10);
		}

		if (this.solarPanel.solarStrength > 0)
		{
			this.drawTexturedModalRect(var5 + 48, var6 + 21, 176, 10, 16, 16);
		}

		this.drawTexturedModalRect(var5 + 97, var6 + 25, 187, 0, Math.min(this.solarPanel.getScaledElecticalLevel(54), 54), 7);
	}
}
