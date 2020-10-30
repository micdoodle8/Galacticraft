package micdoodle8.mods.galacticraft.planets.venus.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.network.PacketSimpleVenus;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiLaserTurret extends GuiContainerGC<ContainerLaserTurret> implements GuiElementCheckbox.ICheckBoxCallback
{
    private static final ResourceLocation backgroundTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/laser_turret.png");

    private final TileEntityLaserTurret laserTurret;
    private GuiElementCheckbox blacklistMode;
    private GuiElementCheckbox targetMeteors;
    private Button buttonEditList;
    private Button buttonEditPriority;

    private int cannotEditTimer;

    private Button buttonEnable;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiLaserTurret(ContainerLaserTurret container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.laserTurret = container.getTurret();
        this.ySize = 219;
        this.xSize = 176;
    }

    @Override
    public void init()
    {
        final int yTop = (this.height - this.ySize) / 2;
        this.buttons.clear();
        super.init();
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.laserTurret.getEnergyStoredGC()) + " / " + (int) Math.floor(this.laserTurret.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 106;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 81, (this.height - this.ySize) / 2 + 92, 18, 18, batterySlotDesc, this.width, this.height, this));
        String enableString = !this.laserTurret.getDisabled(0) ? GCCoreUtil.translate("gui.button.disable") : GCCoreUtil.translate("gui.button.enable");
        this.buttons.add(this.buttonEnable = new Button((this.width - this.xSize) / 2 + 7, this.height / 2 - 9, 72, 20, enableString, (button) ->
        {
            if (!this.minecraft.player.getUniqueID().equals(this.laserTurret.getOwnerUUID()))
            {
                this.cannotEditTimer = 50;
                return;
            }
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.world), new Object[]{this.laserTurret.getPos(), 0}));
            laserTurret.setDisabled(0, !laserTurret.getDisabled(0));
            init();
        }));
        this.blacklistMode = new GuiElementCheckbox(this, this.width / 2 - 81, yTop + 26, GCCoreUtil.translate("gui.message.blacklist_mode"), ColorUtil.to32BitColor(255, 75, 75, 75));
        this.targetMeteors = new GuiElementCheckbox(this, this.width / 2 - 81, yTop + 40, GCCoreUtil.translate("gui.message.target_meteors"), ColorUtil.to32BitColor(255, 75, 75, 75));
        this.buttons.add(this.buttonEditList = new Button((this.width - this.xSize) / 2 + this.xSize / 2 - 41, yTop + 55, 82, 20, GCCoreUtil.translate("gui.button.edit_" + (laserTurret.blacklistMode ? "blacklist" : "whitelist") + ""), (button) ->
        {
            if (!this.minecraft.player.getUniqueID().equals(this.laserTurret.getOwnerUUID()))
            {
                this.cannotEditTimer = 50;
                return;
            }
            Minecraft.getInstance().displayGuiScreen(new GuiLaserTurretEditList(laserTurret));
        }));
        this.buttons.add(this.buttonEditPriority = new Button((this.width - this.xSize) / 2 + this.xSize / 2 - 41, yTop + 78, 82, 20, GCCoreUtil.translate("gui.button.edit_priority"), (button) ->
        {
            if (!this.minecraft.player.getUniqueID().equals(this.laserTurret.getOwnerUUID()))
            {
                this.cannotEditTimer = 50;
                return;
            }
            Minecraft.getInstance().displayGuiScreen(new GuiLaserTurretEditPriority(laserTurret));
        }));
        this.buttons.add(this.blacklistMode);
        this.buttons.add(this.targetMeteors);
        this.buttons.add(this.buttonEditList);
        this.buttons.add(this.buttonEditPriority);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.buttonEnable.active = this.laserTurret.disableCooldown == 0;
        String displayString = this.getTitle().getFormattedText();
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 7, ColorUtil.to32BitColor(255, 75, 75, 75));
        displayString = GCCoreUtil.translate("gui.message.status") + ": " + this.getStatus();
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 18, ColorUtil.to32BitColor(255, 75, 75, 75));
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 94, ColorUtil.to32BitColor(255, 75, 75, 75));
    }

    private String getStatus()
    {
        if (this.laserTurret.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled");
        }

        if (this.laserTurret.getEnergyStoredGC() < 1000)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower");
        }

        return this.laserTurret.getGUIstatus();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiLaserTurret.backgroundTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.laserTurret.getEnergyStoredGC(), this.laserTurret.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        this.blit(var5 + 101, var6 + 106, 176, 51, 67, 9);
        this.blit(var5 + 113, var6 + 107, 176, 0, Math.min(this.laserTurret.getScaledElecticalLevel(54), 54), 7);

        this.blit(var5 + 81, var6 + 102, 176, 33, 18, 18);
        if (this.laserTurret.getEnergyStoredGC() > 0)
        {
            this.blit(var5 + 100, var6 + 107, 230, 0, 11, 8);
        }
        if (this.cannotEditTimer > 0)
        {
            this.cannotEditTimer--;
            this.blacklistMode.active = false;
            this.targetMeteors.active = false;
        }
        else
        {
            this.blacklistMode.active = true;
            this.targetMeteors.active = true;
        }

        this.blacklistMode.isSelected = laserTurret.blacklistMode;
        this.targetMeteors.isSelected = laserTurret.targetMeteors;
        this.buttonEditList.setMessage(GCCoreUtil.translate("gui.button.edit_" + (laserTurret.blacklistMode ? "blacklist" : "whitelist") + ""));

        if (!minecraft.player.getUniqueID().equals(this.laserTurret.getOwnerUUID()))
        {
            String displayString = this.getTitle().getFormattedText();
            boolean off = false;
            if (this.cannotEditTimer > 0)
            {
                off = this.cannotEditTimer % 30 < 15;
            }

            this.blit(var5 + this.xSize / 2 + this.font.getStringWidth(displayString) / 2 + (off ? -1 : 0) + 3, var6 + 5 + (off ? -1 : 0), 202 + (off ? 8 : 0), 7, off ? 10 : 8, off ? 12 : 10);
        }
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        this.cannotEditTimer = 40;
        if (checkbox.equals(this.blacklistMode))
        {
            this.laserTurret.blacklistMode = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{0, this.laserTurret.getPos(), this.laserTurret.blacklistMode ? 1 : 0}));
        }
        else if (checkbox.equals(this.targetMeteors))
        {
            this.laserTurret.targetMeteors = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{1, this.laserTurret.getPos(), this.laserTurret.targetMeteors ? 1 : 0}));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, PlayerEntity player)
    {
        return player.getUniqueID().equals(this.laserTurret.getOwnerUUID()) && this.cannotEditTimer <= 0;
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
    {
        if (checkbox.equals(this.blacklistMode))
        {
            return this.laserTurret.blacklistMode;
        }
        else if (checkbox.equals(this.targetMeteors))
        {
            return this.laserTurret.targetMeteors;
        }

        return false;
    }

    @Override
    public void onIntruderInteraction()
    {
        this.cannotEditTimer = 50;
    }
}
