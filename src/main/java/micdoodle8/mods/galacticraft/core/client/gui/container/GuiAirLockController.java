package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiDropdown;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiDropdown.IDropboxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * GCCoreGuiAirLockController.java
 *
 * This file is part of the Galacticraft project
 *
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GuiAirLockController extends GuiScreen implements ICheckBoxCallback, IDropboxCallback, ITextBoxCallback
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation airLockControllerGui = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/airLockController.png");
    private final TileEntityAirLockController controller;
    private GuiCheckbox checkboxRedstoneSignal;
    private GuiCheckbox checkboxPlayerDistance;
    private GuiDropdown dropdownPlayerDistance;
    private GuiCheckbox checkboxOpenForPlayer;
    private GuiTextBox textBoxPlayerToOpenFor;
    private GuiCheckbox checkboxInvertSelection;
    private GuiCheckbox checkboxHorizontalMode;
    private int cannotEditTimer;

    public GuiAirLockController(TileEntityAirLockController controller)
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
        this.checkboxRedstoneSignal = new GuiCheckbox(0, this, this.width / 2 - 78, var6 + 18, "Opens on Redstone Signal");
        this.checkboxPlayerDistance = new GuiCheckbox(1, this, this.width / 2 - 78, var6 + 33, "Player is within: ");
        this.dropdownPlayerDistance = new GuiDropdown(2, this, var5 + 105, var6 + 34, "1 Meter", "2 Meters", "5 Meters", "10 Meters");
        this.checkboxOpenForPlayer = new GuiCheckbox(3, this, this.width / 2 - 62, var6 + 49, "Player name is: ");
        this.textBoxPlayerToOpenFor = new GuiTextBox(4, this, this.width / 2 - 55, var6 + 64, 110, 15, "", false, 16);
        this.checkboxInvertSelection = new GuiCheckbox(5, this, this.width / 2 - 78, var6 + 80, "Invert Selection");
        this.checkboxHorizontalMode = new GuiCheckbox(6, this, this.width / 2 - 78, var6 + 96, "Horizontal Mode");
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

        this.mc.renderEngine.bindTexture(GuiAirLockController.airLockControllerGui);
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        this.drawTexturedModalRect(var5 + 15, var6 + 51, 176, 0, 7, 9);

        String displayString = this.controller.getOwnerName() + "\'s " + "Air Lock Controller";
        this.fontRendererObj.drawString(displayString, this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 - 65, 4210752);

        if (this.cannotEditTimer > 0)
        {
            this.fontRendererObj.drawString(this.controller.getOwnerName(), this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 - 56, this.cannotEditTimer % 30 < 15 ? CoreUtil.to32BitColor(255, 255, 100, 100) : 4210752);
            this.cannotEditTimer--;
        }

        displayString = "Status:";
        this.fontRendererObj.drawString(displayString, this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 + 45, 4210752);
        displayString = "Air Lock Closed";

        if (this.controller.active)
        {
            displayString = "Air Lock Closed";
        }
        else
        {
            displayString = "Air Lock Open";
        }

        this.fontRendererObj.drawString(displayString, this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 + 55, 4210752);

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void onSelectionChanged(GuiCheckbox checkbox, boolean newSelected)
    {
        if (checkbox.equals(this.checkboxRedstoneSignal))
        {
            this.controller.redstoneActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.redstoneActivation ? 1 : 0 }));
//            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.redstoneActivation ? 1 : 0 }));
        }
        else if (checkbox.equals(this.checkboxPlayerDistance))
        {
            this.controller.playerDistanceActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 1, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceActivation ? 1 : 0 }));
//            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 1, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceActivation ? 1 : 0 }));
        }
        else if (checkbox.equals(this.checkboxOpenForPlayer))
        {
            this.controller.playerNameMatches = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 3, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerNameMatches ? 1 : 0 }));
//            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 3, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerNameMatches ? 1 : 0 }));
        }
        else if (checkbox.equals(this.checkboxInvertSelection))
        {
            this.controller.invertSelection = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 4, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.invertSelection ? 1 : 0 }));
//            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 4, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.invertSelection ? 1 : 0 }));
        }
        else if (checkbox.equals(this.checkboxHorizontalMode))
        {
            this.controller.lastHorizontalModeEnabled = this.controller.horizontalModeEnabled;
            this.controller.horizontalModeEnabled = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 5, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.horizontalModeEnabled ? 1 : 0 }));
//            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 5, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.horizontalModeEnabled ? 1 : 0 }));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiCheckbox checkbox, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.controller.getOwnerName());
    }

    @Override
    public boolean getInitiallySelected(GuiCheckbox checkbox)
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
    public boolean canBeClickedBy(GuiDropdown dropdown, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.controller.getOwnerName());
    }

    @Override
    public void onSelectionChanged(GuiDropdown dropdown, int selection)
    {
        if (dropdown.equals(this.dropdownPlayerDistance))
        {
            this.controller.playerDistanceSelection = selection;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 2, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceSelection }));
//            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 2, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceSelection }));
        }
    }

    @Override
    public int getInitialSelection(GuiDropdown dropdown)
    {
        return this.controller.playerDistanceSelection;
    }

    @Override
    public boolean canPlayerEdit(GuiTextBox textBox, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.controller.getOwnerName());
    }

    @Override
    public void onTextChanged(GuiTextBox textBox, String newText)
    {
        if (textBox.equals(this.textBoxPlayerToOpenFor))
        {
            this.controller.playerToOpenFor = newText != null ? newText : "";
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_STRING, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerToOpenFor }));
//            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_ADVANCED_GUI_CLICKED_STRING, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerToOpenFor }));
        }
    }

    @Override
    public String getInitialText(GuiTextBox textBox)
    {
        if (textBox.equals(this.textBoxPlayerToOpenFor))
        {
            return this.controller.playerToOpenFor;
        }

        return null;
    }

    @Override
    public int getTextColor(GuiTextBox textBox)
    {
        return CoreUtil.to32BitColor(255, 200, 200, 200);
    }

    @Override
    public void onIntruderInteraction()
    {
        this.cannotEditTimer = 50;
    }
}
