package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket.EnumAutoLaunch;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown.IDropboxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiLaunchController extends GuiContainerGC implements IDropboxCallback, ITextBoxCallback, ICheckBoxCallback
{
    private static final ResourceLocation launchControllerGui = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/gui/launchController.png");

    private TileEntityLaunchController launchController;

    private GuiButton enableControllerButton;
    private GuiElementCheckbox enablePadRemovalButton;
    private GuiElementCheckbox launchWhenCheckbox;
    private GuiElementDropdown dropdownTest;
    private GuiElementTextBox frequency;
    private GuiElementTextBox destinationFrequency;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 52, 9, null, 0, 0, this);
    private GuiElementInfoRegion waterTankInfoRegion = new GuiElementInfoRegion(0, 0, 41, 28, null, 0, 0, this);

    private int cannotEditTimer;

    public GuiLaunchController(InventoryPlayer playerInventory, TileEntityLaunchController launchController)
    {
        super(new ContainerLaunchController(playerInventory, launchController));
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

        this.enableControllerButton.displayString = this.launchController.getDisabled(0) ? GCCoreUtil.translate("gui.button.enable.name") : GCCoreUtil.translate("gui.button.disable.name");
        // Hacky way of rendering buttons properly, possibly bugs here:
        List buttonList = new ArrayList(this.buttonList);
        List labelList = new ArrayList(this.labelList);
        List<GuiElementInfoRegion> infoRegions = new ArrayList(this.infoRegions);
        this.buttonList.clear();
        this.labelList.clear();
        this.infoRegions.clear();
        super.drawScreen(par1, par2, par3);

        GL11.glColor3f(1, 1, 1);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int k;
        for (k = 0; k < buttonList.size(); ++k)
        {
            ((GuiButton) buttonList.get(k)).drawButton(this.mc, par1, par2);
        }

        for (k = 0; k < labelList.size(); ++k)
        {
            ((GuiLabel) labelList.get(k)).func_146159_a(this.mc, par1, par2);
        }

        for (k = 0; k < infoRegions.size(); ++k)
        {
            infoRegions.get(k).drawRegion(par1, par2);
        }

        this.buttonList = buttonList;
        this.labelList = labelList;
        this.infoRegions = infoRegions;

//		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    protected void keyTyped(char keyChar, int keyID)
    {
        if (keyID != Keyboard.KEY_ESCAPE && keyID != this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            if (this.frequency.keyTyped(keyChar, keyID))
            {
                return;
            }

            if (this.destinationFrequency.keyTyped(keyChar, keyID))
            {
                return;
            }
        }

        super.keyTyped(keyChar, keyID);
    }

    public boolean isValid(String string)
    {
        if (string.length() > 0 && ChatAllowedCharacters.isAllowedCharacter(string.charAt(string.length() - 1)))
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
        this.enableControllerButton = new GuiButton(0, var5 + 70 + 124 - 72, var6 + 16, 48, 20, GCCoreUtil.translate("gui.button.enable.name"));
        this.enablePadRemovalButton = new GuiElementCheckbox(1, this, this.width / 2 - 78, var6 + 59, GCCoreUtil.translate("gui.message.removePad.name"));
        this.launchWhenCheckbox = new GuiElementCheckbox(2, this, this.width / 2 - 78, var6 + 77, GCCoreUtil.translate("gui.message.launchWhen.name") + ": ");
        this.dropdownTest = new GuiElementDropdown(3, this, var5 + 95, var6 + 77, EnumAutoLaunch.CARGO_IS_UNLOADED.getTitle(), EnumAutoLaunch.CARGO_IS_FULL.getTitle(), EnumAutoLaunch.ROCKET_IS_FUELED.getTitle(), EnumAutoLaunch.INSTANT.getTitle(), EnumAutoLaunch.TIME_10_SECONDS.getTitle(), EnumAutoLaunch.TIME_30_SECONDS.getTitle(), EnumAutoLaunch.TIME_1_MINUTE.getTitle(), EnumAutoLaunch.REDSTONE_SIGNAL.getTitle());
        this.frequency = new GuiElementTextBox(4, this, var5 + 66, var6 + 16, 48, 20, "", true, 6, false);
        this.destinationFrequency = new GuiElementTextBox(5, this, var5 + 122, var6 + 16 + 22, 48, 20, "", true, 6, false);
        this.buttonList.add(this.enableControllerButton);
        this.buttonList.add(this.enablePadRemovalButton);
        this.buttonList.add(this.launchWhenCheckbox);
        this.buttonList.add(this.dropdownTest);
        this.buttonList.add(this.frequency);
        this.buttonList.add(this.destinationFrequency);
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 98;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 113;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 104, 18, 18, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launchController.desc.0"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 20, 59, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launchController.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 42, 117, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launchController.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 59, 78, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launchController.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 77, 82, 13, batterySlotDesc, this.width, this.height, this));
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
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, 0 }));
                break;
            default:
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = this.launchController.getOwnerName() + "\'s " + this.launchController.getInventoryName();
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, 4210752);

        if (this.cannotEditTimer > 0)
        {
            this.fontRendererObj.drawString(this.launchController.getOwnerName(), this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, this.cannotEditTimer % 30 < 15 ? GCCoreUtil.to32BitColor(255, 255, 100, 100) : 4210752);
            this.cannotEditTimer--;
        }

        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 115, 4210752);
        displayString = this.getStatus();
        this.fontRendererObj.drawSplitString(displayString, 60 - this.fontRendererObj.getStringWidth(displayString) / 2, 94, 60, 4210752);
        //		displayString = ElectricityDisplay.getDisplay(this.launchController.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.fontRendererObj.drawString(displayString, this.xSize - 26 - this.fontRendererObj.getStringWidth(displayString), 94, 4210752);
        //		displayString = ElectricityDisplay.getDisplay(this.launchController.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.fontRendererObj.drawString(displayString, this.xSize - 26 - this.fontRendererObj.getStringWidth(displayString), 104, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.frequency.name") + ":", 7, 22, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.destFrequency.name") + ":", 7, 44, 4210752);

    }

    private String getStatus()
    {
        if (!this.launchController.frequencyValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalidFreq.name");
        }

        if (this.launchController.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.noEnergy.name");
        }

        if (this.launchController.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.active.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiLaunchController.launchControllerGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.launchController.getEnergyStoredGC(), this.launchController.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.launchController.getEnergyStoredGC() > 0)
        {
            int scale = this.launchController.getScaledElecticalLevel(54);
            this.drawTexturedModalRect(var5 + 99, var6 + 114, 176, 0, Math.min(scale, 54), 7);
        }

        GL11.glPopMatrix();
    }

    @Override
    public boolean canBeClickedBy(GuiElementDropdown dropdown, EntityPlayer player)
    {
        if (dropdown.equals(this.dropdownTest))
        {
            return player.getGameProfile().getName().equals(this.launchController.getOwnerName());
        }

        return false;
    }

    @Override
    public void onSelectionChanged(GuiElementDropdown dropdown, int selection)
    {
        if (dropdown.equals(this.dropdownTest))
        {
            this.launchController.launchDropdownSelection = selection;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, new Object[] { 1, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.launchDropdownSelection }));
        }
    }

    @Override
    public int getInitialSelection(GuiElementDropdown dropdown)
    {
        if (dropdown.equals(this.dropdownTest))
        {
            return this.launchController.launchDropdownSelection;
        }

        return 0;
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.launchController.getOwnerName());
    }

    @Override
    public void onTextChanged(GuiElementTextBox textBox, String newText)
    {
        if (textBox.equals(this.frequency))
        {
            this.launchController.frequency = textBox.getIntegerValue();
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, new Object[] { 0, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.frequency }));
        }
        else if (textBox.equals(this.destinationFrequency))
        {
            this.launchController.destFrequency = textBox.getIntegerValue();
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, new Object[] { 2, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.destFrequency }));
        }
    }

    @Override
    public String getInitialText(GuiElementTextBox textBox)
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
    public int getTextColor(GuiElementTextBox textBox)
    {
        if (textBox.equals(this.frequency))
        {
            return this.launchController.frequencyValid ? GCCoreUtil.to32BitColor(255, 20, 255, 20) : GCCoreUtil.to32BitColor(255, 255, 25, 25);
        }
        else if (textBox.equals(this.destinationFrequency))
        {
            return this.launchController.destFrequencyValid ? GCCoreUtil.to32BitColor(255, 20, 255, 20) : GCCoreUtil.to32BitColor(255, 255, 25, 25);
        }

        return 0;
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        if (checkbox.equals(this.enablePadRemovalButton))
        {
            this.launchController.launchPadRemovalDisabled = !newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, new Object[] { 3, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.launchPadRemovalDisabled ? 1 : 0 }));
        }
        else if (checkbox.equals(this.launchWhenCheckbox))
        {
            this.launchController.launchSchedulingEnabled = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, new Object[] { 4, this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, this.launchController.launchSchedulingEnabled ? 1 : 0 }));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.launchController.getOwnerName());
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
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

    @Override
    public void onIntruderInteraction(GuiElementTextBox textBox)
    {
        this.cannotEditTimer = 50;
    }
}
