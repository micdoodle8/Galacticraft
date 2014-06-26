package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnergyStorageModule extends GuiContainer
{
	private static final ResourceLocation batteryBoxTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/energyStorageModule.png");

	private TileEntityEnergyStorageModule tileEntity;

	public GuiEnergyStorageModule(InventoryPlayer par1InventoryPlayer, TileEntityEnergyStorageModule batteryBox)
	{
		super(new ContainerEnergyStorageModule(par1InventoryPlayer, batteryBox));
		this.tileEntity = batteryBox;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), this.xSize / 2 - this.fontRendererObj.getStringWidth(this.tileEntity.getInventoryName()) / 2, 6, 4210752);
		String displayMJ = ElectricityDisplay.roundDecimals(this.tileEntity.getEnergyStoredGC() / 1000F, 2) + " of " + ElectricityDisplay.getDisplayShort(this.tileEntity.getMaxEnergyStoredGC(), ElectricUnit.JOULES);
		//String displayVoltage = "Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F)+"V";
		String displayOutput = "Max output: 75kW";

		this.fontRendererObj.drawString(displayMJ, 122 - this.fontRendererObj.getStringWidth(displayMJ) / 2, 30, 4210752);
		//this.fontRendererObj.drawString(displayVoltage, 122 - this.fontRendererObj.getStringWidth(displayVoltage) / 2, 40, 4210752);
		this.fontRendererObj.drawString(displayOutput, 122 - this.fontRendererObj.getStringWidth(displayOutput) / 2, 60, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(GuiEnergyStorageModule.batteryBoxTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int containerWidth = (this.width - this.xSize) / 2;
		int containerHeight = (this.height - this.ySize) / 2;
		// Background energy bar
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
		// Foreground energy bar
		int scale = (int) (this.tileEntity.getEnergyStoredGC() / this.tileEntity.getMaxEnergyStoredGC() * 72);
		this.drawTexturedModalRect(containerWidth + 87, containerHeight + 52, 176, 0, scale, 3);
	}
}
