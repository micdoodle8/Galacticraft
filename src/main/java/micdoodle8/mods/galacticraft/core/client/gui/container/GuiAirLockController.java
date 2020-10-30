package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
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
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

public class GuiAirLockController extends Screen implements ICheckBoxCallback, IDropboxCallback, ITextBoxCallback
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation AIR_LOCK_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/air_lock_controller.png");
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
        super(new TranslationTextComponent("gui.title.air_lock"));
        this.controller = controller;
        this.ySize = 139;
        this.xSize = 181;
    }

    @Override
    protected void init()
    {
        super.init();
        this.buttons.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.checkboxRedstoneSignal = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 18, GCCoreUtil.translate("gui.checkbox.redstone_signal"));
        this.checkboxPlayerDistance = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 33, GCCoreUtil.translate("gui.checkbox.player_within") + ": ");
        String[] dropboxStrings = {GCCoreUtil.translate("gui.dropbox.player_distance.name.0"), GCCoreUtil.translate("gui.dropbox.player_distance.name.1"), GCCoreUtil.translate("gui.dropbox.player_distance.name.2"), GCCoreUtil.translate("gui.dropbox.player_distance.name.3")};
        this.dropdownPlayerDistance = new GuiElementDropdown(this, var5 + 99, var6 + 32, dropboxStrings);
        this.checkboxOpenForPlayer = new GuiElementCheckbox(this, this.width / 2 - 68, var6 + 49, GCCoreUtil.translate("gui.checkbox.player") + ": ");
        this.textBoxPlayerToOpenFor = new GuiElementTextBox(this, this.width / 2 - 61, var6 + 64, 110, 15, "", false, 16, false);
        this.checkboxInvertSelection = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 80, GCCoreUtil.translate("gui.checkbox.invert"));
        this.checkboxHorizontalMode = new GuiElementCheckbox(this, this.width / 2 - 84, var6 + 96, GCCoreUtil.translate("gui.checkbox.horizontal"));
        this.buttons.add(this.checkboxRedstoneSignal);
        this.buttons.add(this.checkboxPlayerDistance);
        this.buttons.add(this.dropdownPlayerDistance);
        this.buttons.add(this.checkboxOpenForPlayer);
        this.buttons.add(this.textBoxPlayerToOpenFor);
        this.buttons.add(this.checkboxInvertSelection);
        this.buttons.add(this.checkboxHorizontalMode);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers)
    {
        if (key != GLFW.GLFW_KEY_ESCAPE)
        {
            if (this.textBoxPlayerToOpenFor.keyPressed(key, scanCode, modifiers))
            {
                return true;
            }
        }

        return super.keyPressed(key, scanCode, scanCode);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

//    @Override
//    protected void actionPerformed(Button par1GuiButton)
//    {
//        if (par1GuiButton.enabled)
//        {
//            switch (par1GuiButton.id)
//            {
//            case 0:
//                break;
//            }
//        }
//    }


    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;

        this.minecraft.textureManager.bindTexture(GuiAirLockController.AIR_LOCK_TEXTURE);
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);

        this.blit(var5 + 11, var6 + 51, 181, 0, 7, 9);

        String message = GCCoreUtil.translateWithFormat("gui.title.air_lock", this.controller.ownerName);
        this.font.drawString(message, this.width / 2 - this.font.getStringWidth(message) / 2, this.height / 2 - 65, 4210752);

        if (this.cannotEditTimer > 0)
        {
            this.font.drawString(this.controller.ownerName, this.width / 2 - this.font.getStringWidth(message) / 2, this.height / 2 - 65, this.cannotEditTimer % 30 < 15 ? ColorUtil.to32BitColor(255, 255, 100, 100) : 4210752);
            this.cannotEditTimer--;
        }

        message = GCCoreUtil.translate("gui.message.status") + ":";
        this.font.drawString(message, this.width / 2 - this.font.getStringWidth(message) / 2, this.height / 2 + 45, 4210752);
        message = EnumColor.RED + GCCoreUtil.translate("gui.status.air_lock_closed");

        if (!this.controller.active)
        {
            message = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.air_lock_open");
        }

        this.font.drawString(message, this.width / 2 - this.font.getStringWidth(message) / 2, this.height / 2 + 55, 4210752);

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        if (checkbox.equals(this.checkboxRedstoneSignal))
        {
            this.controller.redstoneActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{0, this.controller.getPos(), this.controller.redstoneActivation ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 0,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.redstoneActivation ? 1 :
            // 0 }));
        }
        else if (checkbox.equals(this.checkboxPlayerDistance))
        {
            this.controller.playerDistanceActivation = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{1, this.controller.getPos(), this.controller.playerDistanceActivation ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 1,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.playerDistanceActivation
            // ? 1 : 0 }));
        }
        else if (checkbox.equals(this.checkboxOpenForPlayer))
        {
            this.controller.playerNameMatches = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{3, this.controller.getPos(), this.controller.playerNameMatches ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 3,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.playerNameMatches ? 1 : 0
            // }));
        }
        else if (checkbox.equals(this.checkboxInvertSelection))
        {
            this.controller.invertSelection = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{4, this.controller.getPos(), this.controller.invertSelection ? 1 : 0}));
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
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{5, this.controller.getPos(), this.controller.horizontalModeEnabled ? 1 : 0}));
            // PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL,
            // EnumPacketServer.ON_ADVANCED_GUI_CLICKED_INT, new Object[] { 5,
            // this.controller.xCoord, this.controller.yCoord,
            // this.controller.zCoord, this.controller.horizontalModeEnabled ? 1
            // : 0 }));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, PlayerEntity player)
    {
        return PlayerUtil.getName(player).equals(this.controller.ownerName);
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
    public boolean canBeClickedBy(GuiElementDropdown dropdown, PlayerEntity player)
    {
        return PlayerUtil.getName(player).equals(this.controller.ownerName);
    }

    @Override
    public void onSelectionChanged(GuiElementDropdown dropdown, int selection)
    {
        if (dropdown.equals(this.dropdownPlayerDistance))
        {
            this.controller.playerDistanceSelection = selection;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{2, this.controller.getPos(), this.controller.playerDistanceSelection}));
        }
    }

    @Override
    public int getInitialSelection(GuiElementDropdown dropdown)
    {
        return this.controller.playerDistanceSelection;
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, PlayerEntity player)
    {
        return PlayerUtil.getName(player).equals(this.controller.ownerName);
    }

    @Override
    public void onTextChanged(GuiElementTextBox textBox, String newText)
    {
        if (textBox.equals(this.textBoxPlayerToOpenFor))
        {
            this.controller.playerToOpenFor = newText != null ? newText : "";
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_STRING, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{0, this.controller.getPos(), this.controller.playerToOpenFor}));
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
