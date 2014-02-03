package micdoodle8.mods.galacticraft.mars.client.gui;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket.EnumAutoLaunch;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityDisplay.ElectricUnit;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiDropdown;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiDropdown.IDropboxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCMarsGuiLaunchController.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsGuiLaunchController extends GuiContainer implements IDropboxCallback, ITextBoxCallback, ICheckBoxCallback
{
	private static final ResourceLocation launchControllerGui = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/gui/launchController.png");

	private GCMarsTileEntityLaunchController launchController;

	private GuiButton enableControllerButton;
	private GCCoreGuiCheckbox enablePadRemovalButton;
	private GCCoreGuiCheckbox launchWhenCheckbox;
	private GCCoreGuiDropdown dropdownTest;
	private GCCoreGuiTextBox frequency;
	private GCCoreGuiTextBox destinationFrequency;

	private int cannotEditTimer;

	public GCMarsGuiLaunchController(InventoryPlayer playerInventory, GCMarsTileEntityLaunchController launchController)
	{
		super(new GCMarsContainerLaunchController(playerInventory, launchController));
		this.ySize = 209;
		this.launchController = launchController;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		if (this.launchController.disableCooldown > 0)
		{
			this.enableControllerButton.enabled = false;
			this.enablePadRemovalButton.enabled = false;
		}
		else
		{
			this.enableControllerButton.enabled = true;
			this.enablePadRemovalButton.enabled = true;
		}

		this.enableControllerButton.displayString = this.launchController.getDisabled(0) ? "Enable" : "Disable";
		super.drawScreen(par1, par2, par3);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);
		this.dropdownTest.drawButton(this.mc, par1, par2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	protected void keyTyped(char keyChar, int keyID)
	{
		if (this.frequency.keyTyped(keyChar, keyID))
		{
			return;
		}

		if (this.destinationFrequency.keyTyped(keyChar, keyID))
		{
			return;
		}

		super.keyTyped(keyChar, keyID);
	}

	public boolean isValid(String string)
	{
		if (string.length() > 0 && ChatAllowedCharacters.allowedCharacters.indexOf(string.charAt(string.length() - 1)) >= 0)
		{
			try
			{
				Integer.parseInt(string);
				return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.enableControllerButton = new GuiButton(0, var5 + 70 + 124 - 72, var6 + 16, 48, 20, "Enable");
		this.enablePadRemovalButton = new GCCoreGuiCheckbox(1, this, this.width / 2 - 78, var6 + 59, "Remove Pad");
		this.launchWhenCheckbox = new GCCoreGuiCheckbox(2, this, this.width / 2 - 78, var6 + 77, "Launch when: ");
		this.dropdownTest = new GCCoreGuiDropdown(3, this, var5 + 95, var6 + 77, EnumAutoLaunch.CARGO_IS_UNLOADED.getTitle(), EnumAutoLaunch.CARGO_IS_FULL.getTitle(), EnumAutoLaunch.ROCKET_IS_FUELED.getTitle(), EnumAutoLaunch.INSTANT.getTitle(), EnumAutoLaunch.TIME_10_SECONDS.getTitle(), EnumAutoLaunch.TIME_30_SECONDS.getTitle(), EnumAutoLaunch.TIME_1_MINUTE.getTitle(), EnumAutoLaunch.REDSTONE_SIGNAL.getTitle());
		this.frequency = new GCCoreGuiTextBox(4, this, var5 + 66, var6 + 16, 48, 20, "", true, 6);
		this.destinationFrequency = new GCCoreGuiTextBox(5, this, var5 + 122, var6 + 16 + 22, 48, 20, "", true, 6);
		this.buttonList.add(this.enableControllerButton);
		this.buttonList.add(this.enablePadRemovalButton);
		this.buttonList.add(this.launchWhenCheckbox);
		this.buttonList.add(this.dropdownTest);
		this.buttonList.add(this.frequency);
		this.buttonList.add(this.destinationFrequency);
	}

	@Override
	protected void mouseClicked(int px, int py, int par3)
	{
		super.mouseClicked(px, py, par3);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			switch (par1GuiButton.id)
			{
			case 0:
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_DISABLEABLE_BUTTON, new Object[] { this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, 0 }));
				break;
			case 1:
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_DISABLEABLE_BUTTON, new Object[] { this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, 1 }));
				break;
			default:
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_DISABLEABLE_BUTTON, new Object[] { this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, 1 }));
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String displayString = this.launchController.getOwnerName() + "\'s " + this.launchController.getInvName();
		this.fontRenderer.drawString(displayString, this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 5, 4210752);

		if (this.cannotEditTimer > 0)
		{
			this.fontRenderer.drawString(this.launchController.getOwnerName(), this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 5, this.cannotEditTimer % 30 < 15 ? GCCoreUtil.convertTo32BitColor(255, 255, 100, 100) : 4210752);
			this.cannotEditTimer--;
		}

		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 115, 4210752);
		displayString = this.getStatus();
		this.fontRenderer.drawSplitString(displayString, 60 - this.fontRenderer.getStringWidth(displayString) / 2, 94, 60, 4210752);
		displayString = ElectricityDisplay.getDisplay(this.launchController.ueWattsPerTick * 20, ElectricUnit.WATT);
		this.fontRenderer.drawString(displayString, this.xSize - 26 - this.fontRenderer.getStringWidth(displayString), 94, 4210752);
		displayString = ElectricityDisplay.getDisplay(this.launchController.getVoltage(), ElectricUnit.VOLTAGE);
		this.fontRenderer.drawString(displayString, this.xSize - 26 - this.fontRenderer.getStringWidth(displayString), 104, 4210752);
		this.fontRenderer.drawString("Frequency:", 7, 22, 4210752);
		this.fontRenderer.drawString("Destination Frequency:", 7, 44, 4210752);

	}

	private String getStatus()
	{
		if (!this.launchController.frequencyValid)
		{
			return EnumColor.RED + "Invalid Frequency";
		}

		if (this.launchController.getEnergyStored() <= 0.0F)
		{
			return EnumColor.RED + "Not Enough Energy";
		}

		if (this.launchController.getDisabled(0))
		{
			return EnumColor.ORANGE + "Disabled";
		}

		return EnumColor.BRIGHT_GREEN + "Active";
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GCMarsGuiLaunchController.launchControllerGui);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.launchController.getEnergyStored() > 0)
		{
			int scale = this.launchController.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(var5 + 99, var6 + 114, 176, 0, Math.min(scale, 54), 7);
		}

		GL11.glPopMatrix();
	}

	@Override
	public boolean canBeClickedBy(GCCoreGuiDropdown dropdown, EntityPlayer player)
	{
		if (dropdown.equals(this.dropdownTest))
		{
			return player.username.equals(this.launchController.getOwnerName());
		}

		return false;
	}

	@Override
	public void onSelectionChanged(GCCoreGuiDropdown dropdown, int selection)
	{
		if (dropdown.equals(this.dropdownTest))
		{
			this.launchController.launchDropdownSelection = selection;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 5, new Object[] { 1, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.launchDropdownSelection }));
		}
	}

	@Override
	public int getInitialSelection(GCCoreGuiDropdown dropdown)
	{
		if (dropdown.equals(this.dropdownTest))
		{
			return this.launchController.launchDropdownSelection;
		}

		return 0;
	}

	@Override
	public boolean canPlayerEdit(GCCoreGuiTextBox textBox, EntityPlayer player)
	{
		return player.username.equals(this.launchController.getOwnerName());
	}

	@Override
	public void onTextChanged(GCCoreGuiTextBox textBox, String newText)
	{
		if (textBox.equals(this.frequency))
		{
			this.launchController.frequency = textBox.getIntegerValue();
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 5, new Object[] { 0, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.frequency }));
		}
		else if (textBox.equals(this.destinationFrequency))
		{
			this.launchController.destFrequency = textBox.getIntegerValue();
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 5, new Object[] { 2, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.destFrequency }));
		}
	}

	@Override
	public String getInitialText(GCCoreGuiTextBox textBox)
	{
		if (textBox.equals(this.frequency))
		{
			return String.valueOf(this.launchController.frequency);
		}
		else if (textBox.equals(this.destinationFrequency))
		{
			return String.valueOf(this.launchController.destFrequency);
		}

		return "";
	}

	@Override
	public int getTextColor(GCCoreGuiTextBox textBox)
	{
		if (textBox.equals(this.frequency))
		{
			return this.launchController.frequencyValid ? GCCoreUtil.convertTo32BitColor(255, 20, 20, 255) : GCCoreUtil.convertTo32BitColor(255, 255, 25, 25);
		}
		else if (textBox.equals(this.destinationFrequency))
		{
			return this.launchController.destFrequencyValid ? GCCoreUtil.convertTo32BitColor(255, 20, 20, 255) : GCCoreUtil.convertTo32BitColor(255, 255, 25, 25);
		}

		return 0;
	}

	@Override
	public void onSelectionChanged(GCCoreGuiCheckbox checkbox, boolean newSelected)
	{
		if (checkbox.equals(this.enablePadRemovalButton))
		{
			this.launchController.launchPadRemovalDisabled = !newSelected;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 5, new Object[] { 3, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.launchPadRemovalDisabled ? 1 : 0 }));
		}
		else if (checkbox.equals(this.launchWhenCheckbox))
		{
			this.launchController.launchSchedulingEnabled = newSelected;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 5, new Object[] { 4, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.launchSchedulingEnabled ? 1 : 0 }));
		}
	}

	@Override
	public boolean canPlayerEdit(GCCoreGuiCheckbox checkbox, EntityPlayer player)
	{
		return player.username.equals(this.launchController.getOwnerName());
	}

	@Override
	public boolean getInitiallySelected(GCCoreGuiCheckbox checkbox)
	{
		if (checkbox.equals(this.enablePadRemovalButton))
		{
			return !this.launchController.launchPadRemovalDisabled;
		}
		else if (checkbox.equals(this.launchWhenCheckbox))
		{
			return this.launchController.launchSchedulingEnabled;
		}

		return false;
	}

	@Override
	public void onIntruderInteraction()
	{
		this.cannotEditTimer = 50;
	}
}
