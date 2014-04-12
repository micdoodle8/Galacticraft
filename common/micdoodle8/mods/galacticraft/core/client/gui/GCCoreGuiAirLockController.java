package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiDropdown.IDropboxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCCoreGuiAirLockController.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiAirLockController extends GuiScreen implements ICheckBoxCallback, IDropboxCallback, ITextBoxCallback
{
	private final int xSize;
	private final int ySize;
	private static final ResourceLocation airLockControllerGui = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/airLockController.png");
	private final GCCoreTileEntityAirLockController controller;
	private GCCoreGuiCheckbox checkboxRedstoneSignal;
	private GCCoreGuiCheckbox checkboxPlayerDistance;
	private GCCoreGuiDropdown dropdownPlayerDistance;
	private GCCoreGuiCheckbox checkboxOpenForPlayer;
	private GCCoreGuiTextBox textBoxPlayerToOpenFor;
	private GCCoreGuiCheckbox checkboxInvertSelection;
	private GCCoreGuiCheckbox checkboxHorizontalMode;
	private int cannotEditTimer;

	public GCCoreGuiAirLockController(GCCoreTileEntityAirLockController controller)
	{
		this.controller = controller;
		this.xSize = 176;
		this.ySize = 139;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.checkboxRedstoneSignal = new GCCoreGuiCheckbox(0, this, this.width / 2 - 78, var6 + 18, "Opens on Redstone Signal");
		this.checkboxPlayerDistance = new GCCoreGuiCheckbox(1, this, this.width / 2 - 78, var6 + 33, "Player is within: ");
		this.dropdownPlayerDistance = new GCCoreGuiDropdown(2, this, var5 + 105, var6 + 34, "1 Meter", "2 Meters", "5 Meters", "10 Meters");
		this.checkboxOpenForPlayer = new GCCoreGuiCheckbox(3, this, this.width / 2 - 62, var6 + 49, "Player name is: ");
		this.textBoxPlayerToOpenFor = new GCCoreGuiTextBox(4, this, this.width / 2 - 55, var6 + 64, 110, 15, "", false, 16);
		this.checkboxInvertSelection = new GCCoreGuiCheckbox(5, this, this.width / 2 - 78, var6 + 80, "Invert Selection");
		this.checkboxHorizontalMode = new GCCoreGuiCheckbox(6, this, this.width / 2 - 78, var6 + 96, "Horizontal Mode");
		this.buttonList.add(this.checkboxRedstoneSignal);
		this.buttonList.add(this.checkboxPlayerDistance);
		this.buttonList.add(this.dropdownPlayerDistance);
		this.buttonList.add(this.checkboxOpenForPlayer);
		this.buttonList.add(this.textBoxPlayerToOpenFor);
		this.buttonList.add(this.checkboxInvertSelection);
		this.buttonList.add(this.checkboxHorizontalMode);
	}

	@Override
	protected void keyTyped(char keyChar, int keyID)
	{
		if (this.textBoxPlayerToOpenFor.keyTyped(keyChar, keyID))
		{
			return;
		}

		super.keyTyped(keyChar, keyID);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			switch (par1GuiButton.id)
			{
			case 0:
				break;
			}
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;

		this.mc.renderEngine.bindTexture(GCCoreGuiAirLockController.airLockControllerGui);
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

		this.drawTexturedModalRect(var5 + 15, var6 + 51, 176, 0, 7, 9);

		String displayString = this.controller.ownerName + "\'s " + "Air Lock Controller";
		this.fontRenderer.drawString(displayString, this.width / 2 - this.fontRenderer.getStringWidth(displayString) / 2, this.height / 2 - 65, 4210752);

		if (this.cannotEditTimer > 0)
		{
			this.fontRenderer.drawString(this.controller.ownerName, this.width / 2 - this.fontRenderer.getStringWidth(displayString) / 2, this.height / 2 - 65, this.cannotEditTimer % 30 < 15 ? GCCoreUtil.convertTo32BitColor(255, 255, 100, 100) : 4210752);
			this.cannotEditTimer--;
		}

		displayString = "Status:";
		this.fontRenderer.drawString(displayString, this.width / 2 - this.fontRenderer.getStringWidth(displayString) / 2, this.height / 2 + 45, 4210752);
		displayString = "Air Lock Closed";

		if (this.controller.active)
		{
			displayString = "Air Lock Closed";
		}
		else
		{
			displayString = "Air Lock Open";
		}

		this.fontRenderer.drawString(displayString, this.width / 2 - this.fontRenderer.getStringWidth(displayString) / 2, this.height / 2 + 55, 4210752);

		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void onSelectionChanged(GCCoreGuiCheckbox checkbox, boolean newSelected)
	{
		if (checkbox.equals(this.checkboxRedstoneSignal))
		{
			this.controller.redstoneActivation = newSelected;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.redstoneActivation ? 1 : 0 }));
		}
		else if (checkbox.equals(this.checkboxPlayerDistance))
		{
			this.controller.playerDistanceActivation = newSelected;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 1, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceActivation ? 1 : 0 }));
		}
		else if (checkbox.equals(this.checkboxOpenForPlayer))
		{
			this.controller.playerNameMatches = newSelected;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 3, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerNameMatches ? 1 : 0 }));
		}
		else if (checkbox.equals(this.checkboxInvertSelection))
		{
			this.controller.invertSelection = newSelected;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 4, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.invertSelection ? 1 : 0 }));
		}
		else if (checkbox.equals(this.checkboxHorizontalMode))
		{
			this.controller.lastHorizontalModeEnabled = this.controller.horizontalModeEnabled;
			this.controller.horizontalModeEnabled = newSelected;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 5, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.horizontalModeEnabled ? 1 : 0 }));
		}
	}

	@Override
	public boolean canPlayerEdit(GCCoreGuiCheckbox checkbox, EntityPlayer player)
	{
		return player.username.equals(this.controller.ownerName);
	}

	@Override
	public boolean getInitiallySelected(GCCoreGuiCheckbox checkbox)
	{
		if (checkbox.equals(this.checkboxRedstoneSignal))
		{
			return this.controller.redstoneActivation;
		}
		else if (checkbox.equals(this.checkboxPlayerDistance))
		{
			return this.controller.playerDistanceActivation;
		}
		else if (checkbox.equals(this.checkboxOpenForPlayer))
		{
			return this.controller.playerNameMatches;
		}
		else if (checkbox.equals(this.checkboxInvertSelection))
		{
			return this.controller.invertSelection;
		}
		else if (checkbox.equals(this.checkboxHorizontalMode))
		{
			return this.controller.horizontalModeEnabled;
		}

		return false;
	}

	@Override
	public boolean canBeClickedBy(GCCoreGuiDropdown dropdown, EntityPlayer player)
	{
		return player.username.equals(this.controller.ownerName);
	}

	@Override
	public void onSelectionChanged(GCCoreGuiDropdown dropdown, int selection)
	{
		if (dropdown.equals(this.dropdownPlayerDistance))
		{
			this.controller.playerDistanceSelection = selection;
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 2, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceSelection }));
		}
	}

	@Override
	public int getInitialSelection(GCCoreGuiDropdown dropdown)
	{
		return this.controller.playerDistanceSelection;
	}

	@Override
	public boolean canPlayerEdit(GCCoreGuiTextBox textBox, EntityPlayer player)
	{
		return player.username.equals(this.controller.ownerName);
	}

	@Override
	public void onTextChanged(GCCoreGuiTextBox textBox, String newText)
	{
		if (textBox.equals(this.textBoxPlayerToOpenFor))
		{
			this.controller.playerToOpenFor = newText != null ? newText : "";
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_STRING, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerToOpenFor }));
		}
	}

	@Override
	public String getInitialText(GCCoreGuiTextBox textBox)
	{
		if (textBox.equals(this.textBoxPlayerToOpenFor))
		{
			return this.controller.playerToOpenFor;
		}

		return null;
	}

	@Override
	public int getTextColor(GCCoreGuiTextBox textBox)
	{
		return GCCoreUtil.convertTo32BitColor(255, 200, 200, 200);
	}

	@Override
	public void onIntruderInteraction()
	{
		this.cannotEditTimer = 50;
	}
}
