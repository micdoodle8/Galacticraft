package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown.IDropboxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class GuiAirLockController extends GuiScreen implements ICheckBoxCallback, IDropboxCallback, ITextBoxCallback
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation airLockControllerGui = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/airLockController.png");
    private final TileEntityAirLockController controller;
    private GuiElementCheckbox checkboxRedstoneSignal;
    private GuiElementCheckbox checkboxPlayerDistance;
    private GuiElementDropdown dropdownPlayerDistance;
    private GuiElementCheckbox checkboxOpenForPlayer;
    private GuiElementTextBox textBoxPlayerToOpenFor;
    private GuiElementCheckbox checkboxInvertSelection;
    private GuiElementCheckbox checkboxHorizontalMode;
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
        this.checkboxRedstoneSignal = new GuiElementCheckbox(0, this, this.width / 2 - 78, var6 + 18, GCCoreUtil.translate("gui.checkbox.redstoneSignal.name"));
        this.checkboxPlayerDistance = new GuiElementCheckbox(1, this, this.width / 2 - 78, var6 + 33, GCCoreUtil.translate("gui.checkbox.playerWithin.name") + ": ");
        String[] dropboxStrings = { GCCoreUtil.translate("gui.dropbox.playerDistance.name.0"), GCCoreUtil.translate("gui.dropbox.playerDistance.name.1"), GCCoreUtil.translate("gui.dropbox.playerDistance.name.2"), GCCoreUtil.translate("gui.dropbox.playerDistance.name.3") };
        this.dropdownPlayerDistance = new GuiElementDropdown(2, this, var5 + 105, var6 + 34, dropboxStrings);
        this.checkboxOpenForPlayer = new GuiElementCheckbox(3, this, this.width / 2 - 62, var6 + 49, GCCoreUtil.translate("gui.checkbox.playerName.name") + ": ");
        this.textBoxPlayerToOpenFor = new GuiElementTextBox(4, this, this.width / 2 - 55, var6 + 64, 110, 15, "", false, 16, false);
        this.checkboxInvertSelection = new GuiElementCheckbox(5, this, this.width / 2 - 78, var6 + 80, GCCoreUtil.translate("gui.checkbox.invert.name"));
        this.checkboxHorizontalMode = new GuiElementCheckbox(6, this, this.width / 2 - 78, var6 + 96, GCCoreUtil.translate("gui.checkbox.horizontal.name"));
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
        if (keyID != Keyboard.KEY_ESCAPE)
        {
            if (this.textBoxPlayerToOpenFor.keyTyped(keyChar, keyID))
            {
                return;
            }
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

        String displayString = GCCoreUtil.translateWithFormat("gui.title.airLock.name", this.controller.ownerName);
        this.fontRendererObj.drawString(displayString, this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 - 65, 4210752);

        if (this.cannotEditTimer > 0)
        {
            this.fontRendererObj.drawString(this.controller.ownerName, this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 - 65, this.cannotEditTimer % 30 < 15 ? ColorUtil.to32BitColor(255, 255, 100, 100) : 4210752);
            this.cannotEditTimer--;
        }

        displayString = GCCoreUtil.translate("gui.message.status.name") + ":";
        this.fontRendererObj.drawString(displayString, this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 + 45, 4210752);
        displayString = EnumColor.RED + GCCoreUtil.translate("gui.status.airLockClosed.name");

        if (!this.controller.active)
        {
            displayString = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.airLockOpen.name");
        }

        this.fontRendererObj.drawString(displayString, this.width / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, this.height / 2 + 55, 4210752);

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        if (checkbox.equals(this.checkboxRedstoneSignal))
        {
            this.controller.redstoneActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.redstoneActivation ? 1 : 0 }));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 0,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.redstoneActivation ? 1 :
            // 0 }));
        }
        else if (checkbox.equals(this.checkboxPlayerDistance))
        {
            this.controller.playerDistanceActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 1, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceActivation ? 1 : 0 }));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 1,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.playerDistanceActivation
            // ? 1 : 0 }));
        }
        else if (checkbox.equals(this.checkboxOpenForPlayer))
        {
            this.controller.playerNameMatches = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 3, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerNameMatches ? 1 : 0 }));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 3,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.playerNameMatches ? 1 : 0
            // }));
        }
        else if (checkbox.equals(this.checkboxInvertSelection))
        {
            this.controller.invertSelection = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 4, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.invertSelection ? 1 : 0 }));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 4,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.invertSelection ? 1 : 0
            // }));
        }
        else if (checkbox.equals(this.checkboxHorizontalMode))
        {
            this.controller.lastHorizontalModeEnabled = this.controller.horizontalModeEnabled;
            this.controller.horizontalModeEnabled = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 5, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.horizontalModeEnabled ? 1 : 0 }));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 5,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.horizontalModeEnabled ? 1
            // : 0 }));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.controller.ownerName);
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
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
    public boolean canBeClickedBy(GuiElementDropdown dropdown, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.controller.ownerName);
    }

    @Override
    public void onSelectionChanged(GuiElementDropdown dropdown, int selection)
    {
        if (dropdown.equals(this.dropdownPlayerDistance))
        {
            this.controller.playerDistanceSelection = selection;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 2, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerDistanceSelection }));
        }
    }

    @Override
    public int getInitialSelection(GuiElementDropdown dropdown)
    {
        return this.controller.playerDistanceSelection;
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.controller.ownerName);
    }

    @Override
    public void onTextChanged(GuiElementTextBox textBox, String newText)
    {
        if (textBox.equals(this.textBoxPlayerToOpenFor))
        {
            this.controller.playerToOpenFor = newText != null ? newText : "";
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_STRING, new Object[] { 0, this.controller.xCoord, this.controller.yCoord, this.controller.zCoord, this.controller.playerToOpenFor }));
        }
    }

    @Override
    public String getInitialText(GuiElementTextBox textBox)
    {
        if (textBox.equals(this.textBoxPlayerToOpenFor))
        {
            return this.controller.playerToOpenFor;
        }

        return null;
    }

    @Override
    public int getTextColor(GuiElementTextBox textBox)
    {
        return ColorUtil.to32BitColor(255, 200, 200, 200);
    }

    @Override
    public void onIntruderInteraction()
    {
        this.cannotEditTimer = 50;
    }

    @Override
    public void onIntruderInteraction(GuiElementTextBox textBox)
    {
        this.cannotEditTimer = 50;
    }
}
