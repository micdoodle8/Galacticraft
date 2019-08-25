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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiLaserTurret extends GuiContainerGC implements GuiElementCheckbox.ICheckBoxCallback
{
    private static final ResourceLocation backgroundTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/laser_turret.png");

    private final TileEntityLaserTurret laserTurret;
    private GuiElementCheckbox blacklistMode;
    private GuiElementCheckbox targetMeteors;
    private GuiButton buttonEditList;
    private GuiButton buttonEditPriority;

    private int cannotEditTimer;

    private GuiButton buttonEnable;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiLaserTurret(InventoryPlayer par1InventoryPlayer, TileEntityLaserTurret turret)
    {
        super(new ContainerLaserTurret(par1InventoryPlayer, turret));
        this.laserTurret = turret;
        this.ySize = 219;
        this.xSize = 176;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (!this.mc.player.getUniqueID().equals(this.laserTurret.getOwnerUUID()))
        {
            this.cannotEditTimer = 50;
            return;
        }

        switch (button.id)
        {
        case 0:
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(this.mc.world), new Object[] { this.laserTurret.getPos(), 0 }));
            laserTurret.setDisabled(0, !laserTurret.getDisabled(0));
            initGui();
            break;
        case 3:
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiLaserTurretEditList(laserTurret));
            break;
        case 4:
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiLaserTurretEditPriority(laserTurret));
            break;
        }
    }

    @Override
    public void initGui()
    {
        final int yTop = (this.height - this.ySize) / 2;
        this.buttonList.clear();
        super.initGui();
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
        String enableString = !this.laserTurret.getDisabled(0) ? GCCoreUtil.translate("gui.button.disable.name") : GCCoreUtil.translate("gui.button.enable.name");
        this.buttonList.add(this.buttonEnable = new GuiButton(0, (this.width - this.xSize) / 2 + 7, this.height / 2 - 9, 72, 20, enableString));
        this.blacklistMode = new GuiElementCheckbox(1, this, this.width / 2 - 81, yTop + 26, GCCoreUtil.translate("gui.message.blacklist_mode.name"), ColorUtil.to32BitColor(255, 75, 75, 75));
        this.targetMeteors = new GuiElementCheckbox(2, this, this.width / 2 - 81, yTop + 40, GCCoreUtil.translate("gui.message.target_meteors.name"), ColorUtil.to32BitColor(255, 75, 75, 75));
        this.buttonList.add(this.buttonEditList = new GuiButton(3, (this.width - this.xSize) / 2 + this.xSize / 2 - 41, yTop + 55, 82, 20, GCCoreUtil.translate("gui.button.edit_" + (laserTurret.blacklistMode ? "blacklist" : "whitelist") + ".name")));
        this.buttonList.add(this.buttonEditPriority = new GuiButton(4, (this.width - this.xSize) / 2 + this.xSize / 2 - 41, yTop + 78, 82, 20, GCCoreUtil.translate("gui.button.edit_priority.name")));
        this.buttonList.add(this.blacklistMode);
        this.buttonList.add(this.targetMeteors);
        this.buttonList.add(this.buttonEditList);
        this.buttonList.add(this.buttonEditPriority);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.buttonEnable.enabled = this.laserTurret.disableCooldown == 0;
        String displayString = this.laserTurret.getName();
        this.fontRenderer.drawString(displayString, this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 7, ColorUtil.to32BitColor(255, 75, 75, 75));
        displayString = GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus();
        this.fontRenderer.drawString(displayString, this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 18, ColorUtil.to32BitColor(255, 75, 75, 75));
        this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 94, ColorUtil.to32BitColor(255, 75, 75, 75));
    }

    private String getStatus()
    {
        if (this.laserTurret.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
        }

        if (this.laserTurret.getEnergyStoredGC() < 1000)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower.name");
        }

        return this.laserTurret.getGUIstatus();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiLaserTurret.backgroundTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.laserTurret.getEnergyStoredGC(), this.laserTurret.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        this.drawTexturedModalRect(var5 + 101, var6 + 106, 176, 51, 67, 9);
        this.drawTexturedModalRect(var5 + 113, var6 + 107, 176, 0, Math.min(this.laserTurret.getScaledElecticalLevel(54), 54), 7);

        this.drawTexturedModalRect(var5 + 81, var6 + 102, 176, 33, 18, 18);
        if (this.laserTurret.getEnergyStoredGC() > 0)
        {
            this.drawTexturedModalRect(var5 + 100, var6 + 107, 230, 0, 11, 8);
        }
        if (this.cannotEditTimer > 0)
        {
            this.cannotEditTimer--;
            this.blacklistMode.enabled = false;
            this.targetMeteors.enabled = false;
        }
        else
        {
            this.blacklistMode.enabled = true;
            this.targetMeteors.enabled = true;
        }

        this.blacklistMode.isSelected = laserTurret.blacklistMode;
        this.targetMeteors.isSelected = laserTurret.targetMeteors;
        this.buttonEditList.displayString = GCCoreUtil.translate("gui.button.edit_" + (laserTurret.blacklistMode ? "blacklist" : "whitelist") + ".name");

        if (!mc.player.getUniqueID().equals(this.laserTurret.getOwnerUUID()))
        {
            String displayString = this.laserTurret.getName();
            boolean off = false;
            if (this.cannotEditTimer > 0)
            {
                off = this.cannotEditTimer % 30 < 15;
            }

            this.drawTexturedModalRect(var5 + this.xSize / 2 + this.fontRenderer.getStringWidth(displayString) / 2 + (off ? -1 : 0) + 3, var6 + 5 + (off ? -1 : 0), 202 + (off ? 8 : 0), 7, off ? 10 : 8, off ? 12 : 10);
        }
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        this.cannotEditTimer = 40;
        if (checkbox.equals(this.blacklistMode))
        {
            this.laserTurret.blacklistMode = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 0, this.laserTurret.getPos(), this.laserTurret.blacklistMode ? 1 : 0 }));
        }
        else if (checkbox.equals(this.targetMeteors))
        {
            this.laserTurret.targetMeteors = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 1, this.laserTurret.getPos(), this.laserTurret.targetMeteors ? 1 : 0 }));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
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
