package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenSealer;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiOxygenSealer extends GuiContainerGC
{
	private static final ResourceLocation sealerTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/oxygen_large.png");

	private final TileEntityOxygenSealer sealer;
	private GuiButton buttonDisable;

	private GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height);
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height);

	public GuiOxygenSealer(InventoryPlayer par1InventoryPlayer, TileEntityOxygenSealer par2TileEntityAirDistributor)
	{
		super(new ContainerOxygenSealer(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.sealer = par2TileEntityAirDistributor;
		this.ySize = 200;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.sealer.xCoord, this.sealer.yCoord, this.sealer.zCoord, 0 }));
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 31, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height));
		List<String> oxygenDesc = new ArrayList<String>();
		oxygenDesc.add(GCCoreUtil.translate("gui.oxygenStorage.desc.0"));
		oxygenDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.oxygenStorage.desc.1") + ": " + ((int) Math.floor(this.sealer.storedOxygen) + " / " + (int) Math.floor(this.sealer.maxOxygen)));
		this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
		this.oxygenInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
		this.oxygenInfoRegion.yPosition = (this.height - this.ySize) / 2 + 23;
		this.oxygenInfoRegion.parentWidth = this.width;
		this.oxygenInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.oxygenInfoRegion);
		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ": " + ((int) Math.floor(this.sealer.getEnergyStoredGC()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 36;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 38, this.height / 2 - 30 + 21, 76, 20, GCCoreUtil.translate("gui.button.enableseal.name")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(this.sealer.getInventoryName(), 8, 10, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.in.name") + ":", 87, 25, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.in.name") + ":", 87, 37, 4210752);
		String status = GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus();
		this.buttonDisable.enabled = this.sealer.disableCooldown == 0;
		this.buttonDisable.displayString = this.sealer.disabled ? GCCoreUtil.translate("gui.button.enableseal.name") : GCCoreUtil.translate("gui.button.disableseal.name");
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 50, 4210752);
		status = GCCoreUtil.translate("gui.oxygenUse.desc") + ": " + this.sealer.oxygenPerTick * 20 + "/s";
		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 60, 4210752);
		//		status = ElectricityDisplay.getDisplay(this.sealer.ueWattsPerTick * 20, ElectricUnit.WATT);
		//		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 70, 4210752);
		//		status = ElectricityDisplay.getDisplay(this.sealer.getVoltage(), ElectricUnit.VOLTAGE);
		//		this.fontRendererObj.drawString(status, this.xSize / 2 - this.fontRendererObj.getStringWidth(status) / 2, 80, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 90 + 3, 4210752);
	}

	private String getStatus()
	{
		Block blockAbove = this.sealer.getWorldObj().getBlock(this.sealer.xCoord, this.sealer.yCoord + 1, this.sealer.zCoord);

		if (!(blockAbove instanceof BlockAir) && !OxygenPressureProtocol.canBlockPassAir(this.sealer.getWorldObj(), blockAbove, new BlockVec3(this.sealer.xCoord, this.sealer.yCoord + 1, this.sealer.zCoord), 1))
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.sealerblocked.name");
		}

		if (this.sealer.disabled)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.disabled.name");
		}

		if (this.sealer.getEnergyStoredGC() == 0)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower.name");
		}

		if (this.sealer.storedOxygen < 1)
		{
			return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingoxygen.name");
		}

		if (this.sealer.calculatingSealed)
		{
			return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.checkingSeal.name") + "...";
		}

		int threadCooldown = this.sealer.getScaledThreadCooldown(25);

		if (threadCooldown < 15)
		{
			if (threadCooldown < 4)
			{
				String elipsis = "";
				for (int i = 0; i < (23 - threadCooldown) % 4; i++)
				{
					elipsis += ".";
				}

				return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.checkStarting.name") + elipsis;
			}
			else
			{
				return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.checkPending.name");
			}
		}
		else
		{
			if (!this.sealer.sealed)
			{
				return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.unsealed.name");
			}
			else
			{
				return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.sealed.name");
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiOxygenSealer.sealerTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

		if (this.sealer != null)
		{
			List<String> oxygenDesc = new ArrayList<String>();
			oxygenDesc.add(GCCoreUtil.translate("gui.oxygenStorage.desc.0"));
			oxygenDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.oxygenStorage.desc.1") + ": " + ((int) Math.floor(this.sealer.storedOxygen) + " / " + (int) Math.floor(this.sealer.maxOxygen)));
			this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

			List<String> electricityDesc = new ArrayList<String>();
			electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
			electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ": " + ((int) Math.floor(this.sealer.getEnergyStoredGC()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStoredGC())));
			this.electricInfoRegion.tooltipStrings = electricityDesc;

			int scale = this.sealer.getCappedScaledOxygenLevel(54);
			this.drawTexturedModalRect(var5 + 113, var6 + 24, 197, 7, Math.min(scale, 54), 7);
			scale = this.sealer.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(var5 + 113, var6 + 37, 197, 0, Math.min(scale, 54), 7);
			scale = 25 - this.sealer.getScaledThreadCooldown(25);
			this.drawTexturedModalRect(var5 + 148, var6 + 60, 176, 14, 10, 27);
			if (scale != 0)
			{
				this.drawTexturedModalRect(var5 + 149, var6 + 61 + scale, 186, 14, 8, 25 - scale);
			}

			if (this.sealer.getEnergyStoredGC() > 0)
			{
				this.drawTexturedModalRect(var5 + 99, var6 + 36, 176, 0, 11, 10);
			}

			if (this.sealer.storedOxygen > 0)
			{
				this.drawTexturedModalRect(var5 + 100, var6 + 23, 187, 0, 10, 10);
			}
		}
	}
}
