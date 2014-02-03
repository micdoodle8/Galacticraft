package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCoalGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiCoalGenerator.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreGuiCoalGenerator extends GuiContainer
{
	private static final ResourceLocation coalGeneratorTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/coal_generator.png");

	private GCCoreTileEntityCoalGenerator tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GCCoreGuiCoalGenerator(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityCoalGenerator tileEntity)
	{
		super(new ContainerCoalGenerator(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 55, 6, 4210752);
		String displayText = "Generating";
		this.fontRenderer.drawString(displayText, 122 - this.fontRenderer.getStringWidth(displayText) / 2, 33, 4210752);

		if (this.tileEntity.generateWatts <= 0)
		{
			displayText = "Not Generating";
		}
		else if (this.tileEntity.generateWatts < GCCoreTileEntityCoalGenerator.MIN_GENERATE_WATTS)
		{
			displayText = "Hull Heat: " + (int) (this.tileEntity.generateWatts / GCCoreTileEntityCoalGenerator.MIN_GENERATE_WATTS * 100) + "%";
		}
		else
		{
			displayText = ElectricityDisplay.getDisplay(this.tileEntity.generateWatts * 20, ElectricUnit.WATT);
		}

		this.fontRenderer.drawString(displayText, 122 - this.fontRenderer.getStringWidth(displayText) / 2, 45, 4210752);
		displayText = "Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F);
		this.fontRenderer.drawString(displayText, 122 - this.fontRenderer.getStringWidth(displayText) / 2, 60, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(GCCoreGuiCoalGenerator.coalGeneratorTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
	}
}
