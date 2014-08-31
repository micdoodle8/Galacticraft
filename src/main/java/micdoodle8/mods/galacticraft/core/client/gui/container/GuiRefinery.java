package micdoodle8.mods.galacticraft.core.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRefinery;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiRefinery extends GuiContainerGC
{
	private static final ResourceLocation refineryTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/refinery.png");

	private final TileEntityRefinery tileEntity;

	private GuiButton buttonDisable;

	private GuiElementInfoRegion fuelTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
	private GuiElementInfoRegion oilTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 16, 56, 9, new ArrayList<String>(), this.width, this.height, this);

	public GuiRefinery(InventoryPlayer par1InventoryPlayer, TileEntityRefinery tileEntity)
	{
		super(new ContainerRefinery(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		this.ySize = 168;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		List<String> oilTankDesc = new ArrayList<String>();
		oilTankDesc.add(GCCoreUtil.translate("gui.oilTank.desc.0"));
		oilTankDesc.add(GCCoreUtil.translate("gui.oilTank.desc.1"));
		int oilLevel = this.tileEntity.oilTank != null && this.tileEntity.oilTank.getFluid() != null ? this.tileEntity.oilTank.getFluid().amount : 0;
		int oilCapacity = this.tileEntity.oilTank != null ? this.tileEntity.oilTank.getCapacity() : 0;
		oilTankDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.message.oil.name") + ": " + oilLevel + " / " + oilCapacity);
		this.oilTankRegion.tooltipStrings = oilTankDesc;
		this.oilTankRegion.xPosition = (this.width - this.xSize) / 2 + 7;
		this.oilTankRegion.yPosition = (this.height - this.ySize) / 2 + 28;
		this.oilTankRegion.parentWidth = this.width;
		this.oilTankRegion.parentHeight = this.height;
		this.infoRegions.add(this.oilTankRegion);
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
		batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 49, (this.height - this.ySize) / 2 + 50, 18, 18, batterySlotDesc, this.width, this.height, this));
		List<String> fuelTankDesc = new ArrayList<String>();
		fuelTankDesc.add(GCCoreUtil.translate("gui.fuelTank.desc.4"));
		int fuelLevel = this.tileEntity.fuelTank != null && this.tileEntity.fuelTank.getFluid() != null ? this.tileEntity.fuelTank.getFluid().amount : 0;
		int fuelCapacity = this.tileEntity.fuelTank != null ? this.tileEntity.fuelTank.getCapacity() : 0;
		fuelTankDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.message.fuel.name") + ": " + fuelLevel + " / " + fuelCapacity);
		this.fuelTankRegion.tooltipStrings = fuelTankDesc;
		this.fuelTankRegion.xPosition = (this.width - this.xSize) / 2 + 153;
		this.fuelTankRegion.yPosition = (this.height - this.ySize) / 2 + 28;
		this.fuelTankRegion.parentWidth = this.width;
		this.fuelTankRegion.parentHeight = this.height;
		this.infoRegions.add(this.fuelTankRegion);
		List<String> fuelSlotDesc = new ArrayList<String>();
		fuelSlotDesc.add(GCCoreUtil.translate("gui.fuelOutput.desc.0"));
		fuelSlotDesc.add(GCCoreUtil.translate("gui.fuelOutput.desc.1"));
		fuelSlotDesc.add(GCCoreUtil.translate("gui.fuelOutput.desc.2"));
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 152, (this.height - this.ySize) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));
		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.tileEntity.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tileEntity.getMaxEnergyStoredGC())));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 62;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 16;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 39, this.height / 2 - 56, 76, 20, GCCoreUtil.translate("gui.button.refine.name")));
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord, 0 }));
			break;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 68, 5, 4210752);
		String displayText = "";
		int yOffset = -18;

		if (this.tileEntity.oilTank.getFluid() == null || this.tileEntity.oilTank.getFluidAmount() == 0)
		{
			displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.nooil.name");
		}
		else if (this.tileEntity.oilTank.getFluidAmount() > 0 && this.tileEntity.disabled)
		{
			displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready.name");
		}
		else if (this.tileEntity.canProcess())
		{
			displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.refining.name");
		}
		else
		{
			displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.unknown.name");
		}

		this.buttonDisable.enabled = this.tileEntity.disableCooldown == 0;
		this.buttonDisable.displayString = this.tileEntity.processTicks == 0 ? GCCoreUtil.translate("gui.button.refine.name") : GCCoreUtil.translate("gui.button.stoprefine.name");
		this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.status.name") + ": " + displayText, 72, 45 + 23 + yOffset, 4210752);
		//		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23 + yOffset, 4210752);
		//		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23 + yOffset, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 118 + 2 + 23, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(GuiRefinery.refineryTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int containerWidth = (this.width - this.xSize) / 2;
		int containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		int displayInt = this.tileEntity.getScaledOilLevel(38);
		this.drawTexturedModalRect((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 17 + 49 - displayInt, 176, 38 - displayInt, 16, displayInt);

		displayInt = this.tileEntity.getScaledFuelLevel(38);
		this.drawTexturedModalRect((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 17 + 49 - displayInt, 176 + 16, 38 - displayInt, 16, displayInt);

		List<String> oilTankDesc = new ArrayList<String>();
		oilTankDesc.add(GCCoreUtil.translate("gui.oilTank.desc.0"));
		oilTankDesc.add(GCCoreUtil.translate("gui.oilTank.desc.1"));
		int oilLevel = this.tileEntity.oilTank != null && this.tileEntity.oilTank.getFluid() != null ? this.tileEntity.oilTank.getFluid().amount : 0;
		int oilCapacity = this.tileEntity.oilTank != null ? this.tileEntity.oilTank.getCapacity() : 0;
		oilTankDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.message.oil.name") + ": " + oilLevel + " / " + oilCapacity);
		this.oilTankRegion.tooltipStrings = oilTankDesc;

		List<String> fuelTankDesc = new ArrayList<String>();
		fuelTankDesc.add(GCCoreUtil.translate("gui.fuelTank.desc.4"));
		int fuelLevel = this.tileEntity.fuelTank != null && this.tileEntity.fuelTank.getFluid() != null ? this.tileEntity.fuelTank.getFluid().amount : 0;
		int fuelCapacity = this.tileEntity.fuelTank != null ? this.tileEntity.fuelTank.getCapacity() : 0;
		fuelTankDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.message.fuel.name") + ": " + fuelLevel + " / " + fuelCapacity);
		this.fuelTankRegion.tooltipStrings = fuelTankDesc;

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.tileEntity.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tileEntity.getMaxEnergyStoredGC())));
	    EnergyDisplayHelper.getEnergyDisplayTooltip(this.tileEntity.getEnergyStoredGC(), this.tileEntity.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

		if (this.tileEntity.getEnergyStoredGC() > 0)
		{
			this.drawTexturedModalRect(containerWidth + 49, containerHeight + 16, 208, 0, 11, 10);
		}

		this.drawTexturedModalRect(containerWidth + 63, containerHeight + 17, 176, 38, Math.min(this.tileEntity.getScaledElecticalLevel(54), 54), 7);
	}
}
