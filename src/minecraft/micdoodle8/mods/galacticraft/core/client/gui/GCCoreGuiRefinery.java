package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiRefinery extends GuiContainer
{
	private final GCCoreTileEntityRefinery tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GCCoreGuiRefinery(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityRefinery tileEntity)
	{
		super(new GCCoreContainerRefinery(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Refinery", 68, 5, 4210752);
		this.fontRenderer.drawString("Battery:", 10, 53, 4210752);
		String displayText = "";

		if (this.tileEntity.wattsReceived == 0)
		{
			displayText = "Idle";
		}
		else if (this.tileEntity.wattsReceived < this.tileEntity.WATTS_PER_TICK)
		{
			displayText = "Heating";
		}
		else if (this.tileEntity.processTicks > 0)
		{
			displayText = "Refining";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status: " + displayText, 82, 45, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileEntity.WATTS_PER_TICK * 20, ElectricUnit.WATT), 82, 56, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 82, 68, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/gui/refinery.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.processTicks > 0)
		{
			int scale = (int) ((double) this.tileEntity.processTicks / (double) this.tileEntity.PROCESS_TIME_REQUIRED * 124);
			this.drawTexturedModalRect(this.containerWidth + 26, this.containerHeight + 15, 0, 166, 124 - scale, 20);
		}
	}
}