package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiLaunchController extends GuiContainerGC implements ITextBoxCallback
{
    private static final ResourceLocation launchControllerGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/launch_controller.png");

    private TileEntityLaunchController launchController;

    private GuiButton enableControllerButton;
    private GuiButton hideDestinationFrequency;
    private GuiButton openAdvancedConfig;
    private GuiElementTextBox frequency;
    private GuiElementTextBox destinationFrequency;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 52, 9, null, 0, 0, this);
    private GuiElementInfoRegion waterTankInfoRegion = new GuiElementInfoRegion(0, 0, 41, 28, null, 0, 0, this);

    private int cannotEditTimer;

    public GuiLaunchController(InventoryPlayer playerInventory, TileEntityLaunchController launchController)
    {
        super(new ContainerLaunchController(playerInventory, launchController, FMLClientHandler.instance().getClient().thePlayer));
        this.ySize = 209;
        this.launchController = launchController;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        if (this.launchController.disableCooldown > 0)
        {
            this.enableControllerButton.enabled = false;
            this.hideDestinationFrequency.enabled = false;
        }
        else
        {
            boolean isOwner = FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName().equals(this.launchController.getOwnerName());
            this.enableControllerButton.enabled = isOwner;
            this.hideDestinationFrequency.enabled = isOwner;
        }

        this.enableControllerButton.displayString = this.launchController.getDisabled(0) ? GCCoreUtil.translate("gui.button.enable.name") : GCCoreUtil.translate("gui.button.disable.name");
        this.hideDestinationFrequency.displayString = !this.launchController.getDisabled(2) ? GCCoreUtil.translate("gui.button.unhide_dest.name") : GCCoreUtil.translate("gui.button.hide_dest.name");
        // Hacky way of rendering buttons properly, possibly bugs here:
        List<GuiButton> buttonList = new ArrayList<>(this.buttonList);
        List<GuiLabel> labelList = new ArrayList<>(this.labelList);
        List<GuiElementInfoRegion> infoRegions = new ArrayList<>(this.infoRegions);
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
            ((GuiLabel) labelList.get(k)).drawLabel(this.mc, par1, par2);
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

        if (Math.random() < 0.025 && !destinationFrequency.isTextFocused)
        {
            if (!Minecraft.getMinecraft().thePlayer.getGameProfile().getName().equals(this.launchController.getOwnerName()) &&
                    !this.launchController.getDisabled(2))
            {
                // in case the player is not equal to the owner of the controller,
                // scramble the destination number such that other players can't
                // fly to it directly
                Random r = new Random();
                String fakefrequency = "";
                for (int i = 0; i < this.destinationFrequency.getMaxLength(); i++)
                {
                    fakefrequency += (char) (r.nextInt(126 - 33) + 33);
                }
                destinationFrequency.text = fakefrequency;
            }
            else
            {
                destinationFrequency.text = String.valueOf(this.launchController.destFrequency);
            }
        }
    }

    @Override
    protected void keyTyped(char keyChar, int keyID) throws IOException
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

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.enableControllerButton = new GuiButton(0, var5 + 70 + 124 - 72, var6 + 16, 48, 20, GCCoreUtil.translate("gui.button.enable.name"));
        this.frequency = new GuiElementTextBox(4, this, var5 + 66, var6 + 16, 48, 20, "", true, 6, false);
        this.destinationFrequency = new GuiElementTextBox(5, this, var5 + 45, var6 + 16 + 22, 48, 20, "", true, 6, false);
        this.hideDestinationFrequency = new GuiButton(6, var5 + 95, var6 + 16 + 22, 39, 20, GCCoreUtil.translate("gui.button.hide_dest.name"));
        this.openAdvancedConfig = new GuiButton(7, var5 + 48, var6 + 62, 80, 20, GCCoreUtil.translate("gui.launch_controller.advanced") + "...");
        this.buttonList.add(this.enableControllerButton);
        this.buttonList.add(this.frequency);
        this.buttonList.add(this.destinationFrequency);
        this.buttonList.add(this.hideDestinationFrequency);
        this.buttonList.add(this.openAdvancedConfig);
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 98;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 113;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 104, 18, 18, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launch_controller.desc.0"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 20, 109, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launch_controller.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 42, 88, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launch_controller.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 59, 78, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launch_controller.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 77, 82, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launch_controller.desc.4"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 95, (this.height - this.ySize) / 2 + 38, 38, 20, batterySlotDesc, this.width, this.height, this));
    }

    @Override
    protected void mouseClicked(int px, int py, int par3) throws IOException
    {
        super.mouseClicked(px, py, par3);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName().equals(this.launchController.getOwnerName()))
        {
            this.cannotEditTimer = 50;
            return;
        }

        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.launchController.getPos(), 0 }));
                break;
            case 6:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.launchController.getPos(), 2 }));
                break;
            case 7:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_SWITCH_LAUNCH_CONTROLLER_GUI, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.launchController.getPos(), 0 }));
                break;
            default:
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = this.launchController.getOwnerName() + "\'s " + this.launchController.getName();
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, 4210752);

        if (this.cannotEditTimer > 0)
        {
            this.fontRendererObj.drawString(this.launchController.getOwnerName(), this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, this.cannotEditTimer % 30 < 15 ? ColorUtil.to32BitColor(255, 255, 100, 100) : 4210752);
            this.cannotEditTimer--;
        }

        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 115, 4210752);
        displayString = this.getStatus();
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 86, 4210752);
        //		displayString = ElectricityDisplay.getDisplay(this.launchController.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.fontRendererObj.drawString(displayString, this.xSize - 26 - this.fontRendererObj.getStringWidth(displayString), 94, 4210752);
        //		displayString = ElectricityDisplay.getDisplay(this.launchController.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.fontRendererObj.drawString(displayString, this.xSize - 26 - this.fontRendererObj.getStringWidth(displayString), 104, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.frequency.name") + ":", 7, 22, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.dest_frequency.name") + ":", 7, 44, 4210752);

    }

    private String getStatus()
    {
        if (!this.launchController.frequencyValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalid_freq.name");
        }

        if (this.launchController.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_energy.name");
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
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
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
    public boolean canPlayerEdit(GuiElementTextBox textBox, EntityPlayer player)
    {
        return player.getGameProfile().getName().equals(this.launchController.getOwnerName());
    }

    @Override
    public void onTextChanged(GuiElementTextBox textBox, String newText)
    {
        if (FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName().equals(this.launchController.getOwnerName()))
        {
            if (textBox.equals(this.frequency))
            {
                this.launchController.frequency = textBox.getIntegerValue();
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { 0, this.launchController.getPos(), this.launchController.frequency }));
            }
            else if (textBox.equals(this.destinationFrequency))
            {
                this.launchController.destFrequency = textBox.getIntegerValue();
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { 2, this.launchController.getPos(), this.launchController.destFrequency }));
            }
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
            if (Minecraft.getMinecraft().thePlayer.getGameProfile().getName().equals(this.launchController.getOwnerName()) || this.launchController.getDisabled(2))
            {
                return String.valueOf(this.launchController.destFrequency);
            }
            else
            {
                // in case the player is not equal to the owner of the controller,
                // scramble the destination number such that other players can't
                // fly to it directly
                Random r = new Random();
                String fakefrequency = "";
                for (int i = 0; i < this.destinationFrequency.getMaxLength(); i++)
                {
                    fakefrequency += (char) (r.nextInt(126 - 33) + 33);
                }
                return fakefrequency;
            }
        }

        return "";
    }

    @Override
    public int getTextColor(GuiElementTextBox textBox)
    {
        if (textBox.equals(this.frequency))
        {
            return this.launchController.frequencyValid ? ColorUtil.to32BitColor(255, 20, 255, 20) : ColorUtil.to32BitColor(255, 255, 25, 25);
        }
        else if (textBox.equals(this.destinationFrequency))
        {
            return this.launchController.destFrequencyValid ? ColorUtil.to32BitColor(255, 20, 255, 20) : ColorUtil.to32BitColor(255, 255, 25, 25);
        }

        return 0;
    }

    @Override
    public void onIntruderInteraction(GuiElementTextBox textBox)
    {
        this.cannotEditTimer = 50;
    }
}
