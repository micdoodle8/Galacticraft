package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

/**
 * GCMarsGuiTerraformer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GuiTerraformer extends GuiContainer implements ICheckBoxCallback
{
	private static final ResourceLocation terraformerGui = new ResourceLocation(MarsModule.TEXTURE_DOMAIN, "textures/gui/terraformer.png");

	private TileEntityTerraformer terraformer;

	private GuiButton enableTreesButton;
	private GuiButton enableGrassButton;
	private GuiElementCheckbox checkboxRenderBubble;

	public GuiTerraformer(InventoryPlayer par1InventoryPlayer, TileEntityTerraformer terraformer)
	{
		super(new ContainerTerraformer(par1InventoryPlayer, terraformer));
		this.ySize = 237;
		this.terraformer = terraformer;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		if (this.terraformer.disableCooldown > 0)
		{
			this.enableTreesButton.enabled = false;
			this.enableGrassButton.enabled = false;
		}
		else
		{
			this.enableTreesButton.enabled = true;
			this.enableGrassButton.enabled = true;
		}

		this.enableTreesButton.displayString = (this.terraformer.treesDisabled ? "Enable" : "Disable") + " Trees";
		this.enableGrassButton.displayString = (this.terraformer.grassDisabled ? "Enable" : "Disable") + " Grass";

		super.drawScreen(par1, par2, par3);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.enableTreesButton = new GuiButton(0, var5 + 98, var6 + 85, 72, 20, "Enable Trees");
		this.enableGrassButton = new GuiButton(1, var5 + 98, var6 + 109, 72, 20, "Enable Grass");
		this.buttonList.add(this.enableTreesButton);
		this.buttonList.add(this.enableGrassButton);
		this.checkboxRenderBubble = new GuiElementCheckbox(0, this, var5 + 85, var6 + 132, "Bubble Visible");
		this.buttonList.add(this.checkboxRenderBubble);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			switch (par1GuiButton.id)
			{
			case 0:
				GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.terraformer.xCoord, this.terraformer.yCoord, this.terraformer.zCoord, 0 }));
				break;
			case 1:
				GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.terraformer.xCoord, this.terraformer.yCoord, this.terraformer.zCoord, 1 }));
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String displayString = "Terraformer";
		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 144, 4210752);
		this.fontRendererObj.drawSplitString(this.getStatus(), 105, 24, this.xSize - 105, 4210752);
//		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.terraformer.ueWattsPerTick * 20, ElectricUnit.WATT), 105, 56, 4210752);
//		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.terraformer.getVoltage(), ElectricUnit.VOLTAGE), 105, 68, 4210752);
	}

	private String getStatus()
	{
		if (this.terraformer.getEnergyStoredGC() <= 0.0F)
		{
			return EnumColor.RED + "Not Enough Energy";
		}

		if (this.terraformer.grassDisabled && this.terraformer.treesDisabled)
		{
			return EnumColor.ORANGE + "Disabled";
		}

		if (this.terraformer.waterTank.getFluid() == null || this.terraformer.waterTank.getFluid().amount <= 0)
		{
			return EnumColor.RED + "Not Enough Water";
		}

		if (this.terraformer.getFirstBonemealStack() == null)
		{
			return EnumColor.RED + "Not Enough Bonemeal";
		}

		if (!this.terraformer.grassDisabled && this.terraformer.getFirstSeedStack() == null)
		{
			return EnumColor.RED + "Not Enough Seeds";
		}

		if (!this.terraformer.treesDisabled && this.terraformer.getFirstSaplingStack() == null)
		{
			return EnumColor.RED + "Not Enough Saplings";
		}

		if (this.terraformer.terraformBubble.getSize() < this.terraformer.MAX_SIZE)
		{
			return EnumColor.YELLOW + "Bubble Expanding";
		}

		if (!this.terraformer.treesDisabled && this.terraformer.grassBlocksListSize <= 0)
		{
			return EnumColor.RED + "No Valid Blocks in Bubble";
		}
		else if (!this.terraformer.grassDisabled && this.terraformer.terraformableBlocksListSize <= 0)
		{
			return EnumColor.RED + "No Valid Blocks in Bubble";
		}

		return EnumColor.BRIGHT_GREEN + "Terraforming";
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GuiTerraformer.terraformerGui);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int scale = this.terraformer.getScaledElecticalLevel(54);
		this.drawTexturedModalRect(var5 + 45, var6 + 48, 176, 26, Math.min(scale, 54), 7);

		int fuelLevel = this.terraformer.getScaledWaterLevel(26);
		this.drawTexturedModalRect((this.width - this.xSize) / 2 + 56, (this.height - this.ySize) / 2 + 17 + 27 - fuelLevel, 176, 26 - fuelLevel, 39, fuelLevel);
		this.checkboxRenderBubble.isSelected = this.terraformer.getBubble().shouldRender();
	}

	@Override
	public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
	{
		this.terraformer.terraformBubble.setShouldRender(newSelected);
		GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 6, this.terraformer.xCoord, this.terraformer.yCoord, this.terraformer.zCoord, newSelected ? 1 : 0 }));
	}

	@Override
	public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean getInitiallySelected(GuiElementCheckbox checkbox)
	{
		return this.terraformer.terraformBubble.shouldRender();
	}

	@Override
	public void onIntruderInteraction()
	{
		;
	}
}
