package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class GuiElectricFurnace extends GuiContainer
{
	private static final ResourceLocation electricFurnaceTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/electric_furnace.png");

	private TileEntityElectricFurnace tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GuiElectricFurnace(InventoryPlayer par1InventoryPlayer, TileEntityElectricFurnace tileEntity)
	{
		super(new ContainerElectricFurnace(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 45, 6, 4210752);
		String displayText = "";

		if (this.tileEntity.processTicks > 0)
		{
			displayText = GCCoreUtil.translate("gui.status.running.name");
		}
		else
		{
			displayText = GCCoreUtil.translate("gui.status.idle.name");
		}

		this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.status.name") + ": " + displayText, 97, 45, 4210752);
		this.fontRendererObj.drawString("" + tileEntity.storage.getMaxExtract(), 97, 56, 4210752);
//		this.fontRendererObj.drawString("Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F), 97, 68, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(GuiElectricFurnace.electricFurnaceTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
		int scale;

		if (this.tileEntity.processTicks > 0)
		{
			scale = (int) ((double) this.tileEntity.processTicks / (double) TileEntityElectricFurnace.PROCESS_TIME_REQUIRED * 23);
			this.drawTexturedModalRect(this.containerWidth + 78, this.containerHeight + 24, 176, 0, 23 - scale, 15);
		}

		if (this.tileEntity.getEnergyStoredGC() > 0)
		{
			scale = this.tileEntity.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(this.containerWidth + 40, this.containerHeight + 53, 176, 15, scale, 7);
			this.drawTexturedModalRect(this.containerWidth + 26, this.containerHeight + 52, 176, 22, 11, 10);
		}
	}
}
