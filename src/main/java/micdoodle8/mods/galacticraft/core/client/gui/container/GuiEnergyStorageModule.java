package micdoodle8.mods.galacticraft.core.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.transmission.EnergyHelper;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiEnergyStorageModule extends GuiContainer
{
	private static final ResourceLocation batteryBoxTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/energyStorageModule.png");

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
		float energy = this.tileEntity.getEnergyStoredGC();
		if (energy + 49 > this.tileEntity.getMaxEnergyStoredGC()) energy = this.tileEntity.getMaxEnergyStoredGC();
		String displayStr = EnergyHelper.getEnergyDisplayS(energy);
		this.fontRendererObj.drawString(displayStr, 122 - this.fontRendererObj.getStringWidth(displayStr) / 2, 25, 4210752);
        displayStr = "of " + EnergyHelper.getEnergyDisplayS(this.tileEntity.getMaxEnergyStoredGC());
        this.fontRendererObj.drawString(displayStr, 122 - this.fontRendererObj.getStringWidth(displayStr) / 2, 34, 4210752);
        displayStr = "Output: " + EnergyHelper.getEnergyDisplayS(this.tileEntity.storage.getMaxExtract())+"/t";
		this.fontRendererObj.drawString(displayStr, 122 - this.fontRendererObj.getStringWidth(displayStr) / 2, 64, 4210752);
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
		int scale = (int) ((this.tileEntity.getEnergyStoredGC() + 49) / this.tileEntity.getMaxEnergyStoredGC() * 72);
		this.drawTexturedModalRect(containerWidth + 87, containerHeight + 52, 176, 0, scale, 3);
	}
}
