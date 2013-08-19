package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElectricFurnace extends GuiContainer
{
	private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/electric_furnace.png");

	private GCCoreTileEntityElectricFurnace tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GuiElectricFurnace(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityElectricFurnace tileEntity)
	{
		super(new ContainerElectricFurnace(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 45, 6, 4210752);
		this.fontRenderer.drawString("Smelting:", 10, 28, 4210752);
		this.fontRenderer.drawString("Battery:", 10, 53, 4210752);
		String displayText = "";

		if (this.tileEntity.processTicks > 0)
		{
			displayText = "Smelting";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status: " + displayText, 82, 45, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(GCCoreTileEntityElectricFurnace.WATTS_PER_TICK * 20, ElectricUnit.WATT), 82, 56, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 82, 68, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.func_110577_a(electricFurnaceTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.processTicks > 0)
		{
			int scale = (int) (((double) this.tileEntity.processTicks / (double) GCCoreTileEntityElectricFurnace.PROCESS_TIME_REQUIRED) * 23);
			this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
		}
	}
}