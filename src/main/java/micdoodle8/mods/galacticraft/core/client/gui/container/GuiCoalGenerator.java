package micdoodle8.mods.galacticraft.core.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiCoalGenerator extends GuiContainer
{
	private static final ResourceLocation coalGeneratorTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/coal_generator.png");

	private TileEntityCoalGenerator tileEntity;

	public GuiCoalGenerator(InventoryPlayer par1InventoryPlayer, TileEntityCoalGenerator tileEntity)
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
		this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 55, 6, 4210752);
		String displayText = GCCoreUtil.translate("gui.status.generating.name");
		this.fontRendererObj.drawString(displayText, 122 - this.fontRendererObj.getStringWidth(displayText) / 2, 33, 4210752);

		if (this.tileEntity.generateWatts <= 0)
		{
			displayText = GCCoreUtil.translate("gui.status.notGenerating.name");
		}
		else if (this.tileEntity.generateWatts < TileEntityCoalGenerator.MIN_GENERATE_WATTS)
		{
			displayText = GCCoreUtil.translate("gui.status.hullHeat.name") + ": " + this.tileEntity.generateWatts / TileEntityCoalGenerator.MIN_GENERATE_WATTS * 100 + "%";
		}
		else
		{
			displayText = ElectricityDisplay.getDisplay(this.tileEntity.generateWatts * 20, ElectricUnit.WATT);
		}

		this.fontRendererObj.drawString(displayText, 122 - this.fontRendererObj.getStringWidth(displayText) / 2, 45, 4210752);
		//		displayText = "Voltage: " + (int) (this.tileEntity.getVoltage() * 1000.0F);
		//		this.fontRendererObj.drawString(displayText, 122 - this.fontRendererObj.getStringWidth(displayText) / 2, 60, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(GuiCoalGenerator.coalGeneratorTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
	}
}
