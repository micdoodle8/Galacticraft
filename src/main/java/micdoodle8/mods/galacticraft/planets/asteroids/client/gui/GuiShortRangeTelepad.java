package micdoodle8.mods.galacticraft.planets.asteroids.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiShortRangeTelepad extends GuiContainerGC<ContainerShortRangeTelepad> implements ITextBoxCallback
{
    private static final ResourceLocation launchControllerGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/short_range_telepad.png");

    private final TileEntityShortRangeTelepad telepad;

    private Button enableControllerButton;
    private GuiElementTextBox address;
    private GuiElementTextBox targetAddress;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 52, 9, null, 0, 0, this);

    public GuiShortRangeTelepad(ContainerShortRangeTelepad container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.ySize = 209;
        this.telepad = container.getTelepad();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.enableControllerButton.active = this.telepad.disableCooldown <= 0;

        this.enableControllerButton.setMessage(this.telepad.getDisabled(0) ? GCCoreUtil.translate("gui.button.enable") : GCCoreUtil.translate("gui.button.disable"));

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers)
    {
        if (key != GLFW.GLFW_KEY_ESCAPE)
        {
            if (this.address.keyPressed(key, scanCode, modifiers))
            {
                return true;
            }

            if (this.targetAddress.keyPressed(key, scanCode, modifiers))
            {
                return true;
            }
        }

        return super.keyPressed(key, scanCode, scanCode);
    }

    @Override
    public void init()
    {
        super.init();
        this.buttons.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.enableControllerButton = new Button(var5 + 70 + 124 - 72, var6 + 16, 48, 20, GCCoreUtil.translate("gui.button.enable"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{this.telepad.getPos(), 0}));
        });
        this.address = new GuiElementTextBox(this, var5 + 66, var6 + 16, 48, 20, "", true, 6, false);
        this.targetAddress = new GuiElementTextBox(this, var5 + 122, var6 + 16 + 22, 48, 20, "", true, 6, false);
        this.buttons.add(this.enableControllerButton);
        this.buttons.add(this.address);
        this.buttons.add(this.targetAddress);
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
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.telepad.desc.0"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 20, 59, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.telepad.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 42, 117, 13, batterySlotDesc, this.width, this.height, this));
    }

//    @Override
//    protected void mouseClicked(int px, int py, int par3)
//    {
//        super.mouseClicked(px, py, par3);
//    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = this.getTitle().getFormattedText();
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 5, 4210752);

        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, 115, 4210752);
        this.font.drawString(GCCoreUtil.translate("gui.message.address") + ":", 7, 22, 4210752);
        this.font.drawString(GCCoreUtil.translate("gui.message.dest_address") + ":", 7, 44, 4210752);
        this.font.drawString(this.telepad.getReceivingStatus(), 7, 66, 4210752);
        if (!this.telepad.getReceivingStatus().equals(this.telepad.getSendingStatus()))
        {
            this.font.drawString(this.telepad.getSendingStatus(), 7, 88, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(GuiShortRangeTelepad.launchControllerGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.telepad.getEnergyStoredGC(), this.telepad.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.telepad.getEnergyStoredGC() > 0)
        {
            int scale = this.telepad.getScaledElecticalLevel(54);
            this.blit(var5 + 99, var6 + 114, 176, 0, Math.min(scale, 54), 7);
        }

        GL11.glPopMatrix();
    }

    @Override
    public boolean canPlayerEdit(GuiElementTextBox textBox, PlayerEntity player)
    {
        return PlayerUtil.getName(player).equals(this.telepad.getOwner());
    }

    @Override
    public void onTextChanged(GuiElementTextBox textBox, String newText)
    {
        if (textBox.equals(this.address))
        {
            this.telepad.address = textBox.getIntegerValue();
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionType(this.minecraft.world), new Object[]{0, this.telepad.getPos(), this.telepad.address}));
        }
        else if (textBox.equals(this.targetAddress))
        {
            this.telepad.targetAddress = textBox.getIntegerValue();
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionType(this.minecraft.world), new Object[]{1, this.telepad.getPos(), this.telepad.targetAddress}));
        }
    }

    @Override
    public String getInitialText(GuiElementTextBox textBox)
    {
        if (textBox.equals(this.address))
        {
            return String.valueOf(this.telepad.address);
        }
        else if (textBox.equals(this.targetAddress))
        {
            return String.valueOf(this.telepad.targetAddress);
        }

        return "";
    }

    @Override
    public int getTextColor(GuiElementTextBox textBox)
    {
        if (textBox.equals(this.address))
        {
            return this.telepad.addressValid ? ColorUtil.to32BitColor(255, 20, 255, 20) : ColorUtil.to32BitColor(255, 255, 25, 25);
        }
        else if (textBox.equals(this.targetAddress))
        {
            if (this.telepad.targetAddressResult == TileEntityShortRangeTelepad.EnumTelepadSearchResult.VALID)
            {
                return ColorUtil.to32BitColor(255, 20, 255, 20);
            }
            else if (this.telepad.targetAddressResult == TileEntityShortRangeTelepad.EnumTelepadSearchResult.TARGET_DISABLED)
            {
                return ColorUtil.to32BitColor(255, 255, 170, 25);
            }
            else
            {
                return ColorUtil.to32BitColor(255, 255, 25, 25);
            }
        }

        return 0;
    }

    @Override
    public void onIntruderInteraction(GuiElementTextBox textBox)
    {
    }
}
