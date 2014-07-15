package micdoodle8.mods.galacticraft.core.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiOxygenStorageModule extends GuiContainer
{
	private static final ResourceLocation batteryBoxTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/oxygenStorageModule.png");

	private TileEntityOxygenStorageModule tileEntity;

	public GuiOxygenStorageModule(InventoryPlayer par1InventoryPlayer, TileEntityOxygenStorageModule storageModule)
	{
		super(new ContainerOxygenStorageModule(par1InventoryPlayer, storageModule));
		this.tileEntity = storageModule;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String guiTitle = GCCoreUtil.translate("tile.machine2.6.name");
		this.fontRendererObj.drawString(guiTitle, this.xSize / 2 - this.fontRendererObj.getStringWidth(guiTitle) / 2, 6, 4210752);
		String displayJoules = this.tileEntity.storedOxygen + " of";
		String displayMaxJoules = "" + this.tileEntity.maxOxygen;
		String maxOutputLabel = "Max Output: " + TileEntityOxygenStorageModule.OUTPUT_PER_TICK * 20 + "/s";

		this.fontRendererObj.drawString(displayJoules, 122 - this.fontRendererObj.getStringWidth(displayJoules) / 2 - 35, 30, 4210752);
		this.fontRendererObj.drawString(displayMaxJoules, 122 - this.fontRendererObj.getStringWidth(displayMaxJoules) / 2 - 35, 40, 4210752);
		this.fontRendererObj.drawString(maxOutputLabel, 122 - this.fontRendererObj.getStringWidth(maxOutputLabel) / 2 - 35, 60, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(GuiOxygenStorageModule.batteryBoxTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int containerWidth = (this.width - this.xSize) / 2;
		int containerHeight = (this.height - this.ySize) / 2;
		// Background energy bar
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
		// Foreground energy bar
		int scale = (int) ((double) this.tileEntity.storedOxygen / (double) this.tileEntity.maxOxygen * 72);
		this.drawTexturedModalRect(containerWidth + 52, containerHeight + 52, 176, 0, scale, 3);
	}
}
