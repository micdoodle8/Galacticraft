package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityIngotCompressor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiIngotCompressor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreGuiIngotCompressor extends GuiContainer
{
	private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/ingotCompressor.png");

	private GCCoreTileEntityIngotCompressor tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GCCoreGuiIngotCompressor(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityIngotCompressor tileEntity)
	{
		super(new GCCoreContainerIngotCompressor(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		this.ySize = 192;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 10, 6, 4210752);
		String displayText = "Fuel:";
		this.fontRenderer.drawString(displayText, 50 - this.fontRenderer.getStringWidth(displayText), 79, 4210752);

		if (this.tileEntity.processTicks > 0)
		{
			displayText = "Compressing";
		}
		else
		{
			displayText = "Idle";
		}

		String str = "Status:";
		this.fontRenderer.drawString("Status:", 120 - this.fontRenderer.getStringWidth(str) / 2, 70, 4210752);
		str = displayText;
		this.fontRenderer.drawString(displayText, 120 - this.fontRenderer.getStringWidth(str) / 2, 80, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(GCCoreGuiIngotCompressor.electricFurnaceTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.processTicks > 0)
		{
			int scale = (int) ((double) this.tileEntity.processTicks / (double) GCCoreTileEntityIngotCompressor.PROCESS_TIME_REQUIRED * 54);
			this.drawTexturedModalRect(this.containerWidth + 77, this.containerHeight + 36, 176, 13, scale, 17);
		}

		if (this.tileEntity.furnaceBurnTime > 0)
		{
			int scale = (int) ((double) this.tileEntity.furnaceBurnTime / (double) this.tileEntity.currentItemBurnTime * 14);
			this.drawTexturedModalRect(this.containerWidth + 81, this.containerHeight + 27 + 14 - scale, 176, 30 + 14 - scale, 14, scale);
		}

		if (this.tileEntity.processTicks > GCCoreTileEntityIngotCompressor.PROCESS_TIME_REQUIRED / 2)
		{
			this.drawTexturedModalRect(this.containerWidth + 101, this.containerHeight + 28, 176, 0, 15, 13);
		}
	}
}
