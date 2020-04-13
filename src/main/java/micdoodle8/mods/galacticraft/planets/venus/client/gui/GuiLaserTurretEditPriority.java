package micdoodle8.mods.galacticraft.planets.venus.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementSpinner;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.network.PacketSimpleVenus;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiLaserTurretEditPriority extends GuiScreen implements GuiElementSpinner.ISpinnerCallback
{
    private static final ResourceLocation backgroundTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/laser_turret_edit.png");

    private final TileEntityLaserTurret laserTurret;

    private int ySize;
    private int xSize;
    private GuiElementSpinner priorityClosest;
    private GuiElementSpinner priorityLowestHealth;
    private GuiElementSpinner priorityHighestHealth;

    public GuiLaserTurretEditPriority(TileEntityLaserTurret turret)
    {
        this.laserTurret = turret;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
        case 0:
            break;
        }
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        super.initGui();
        this.ySize = 95;
        this.xSize = 222;
        int yTop = (this.height - this.ySize) / 2;
        this.priorityClosest = new GuiElementSpinner(0, this, this.width / 2 - 95, yTop + 21, ColorUtil.to32BitColor(255, 220, 220, 220));
        this.buttonList.add(this.priorityClosest);
        this.priorityLowestHealth = new GuiElementSpinner(1, this, this.width / 2 - 95, yTop + 43, ColorUtil.to32BitColor(255, 220, 220, 220));
        this.buttonList.add(this.priorityLowestHealth);
        this.priorityHighestHealth = new GuiElementSpinner(2, this, this.width / 2 - 95, yTop + 65, ColorUtil.to32BitColor(255, 220, 220, 220));
        this.buttonList.add(this.priorityHighestHealth);
    }

    @Override
    protected void keyTyped(char keyChar, int keyID) throws IOException
    {
        if (keyID == 1)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_OPEN_LASER_TURRET_GUI, GCCoreUtil.getDimensionID(laserTurret.getWorld()), new Object[] { laserTurret.getPos() }));
        }
        else
        {
            super.keyTyped(keyChar, keyID);
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiLaserTurretEditPriority.backgroundTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize - 10);
        this.drawTexturedModalRect(var5, var6 + this.ySize - 10, 0, 144 - 10, this.xSize, 10);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.priority_low.name"), var5 + 6, var6 + 8, ColorUtil.to32BitColor(255, 75, 75, 75));
        this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.priority_closest.name"), this.priorityClosest.x + 35, this.priorityClosest.y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, ColorUtil.to32BitColor(255, 75, 75, 75));
        this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.priority_health_low.name"), this.priorityLowestHealth.x + 35, this.priorityLowestHealth.y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, ColorUtil.to32BitColor(255, 75, 75, 75));
        this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.priority_health_high.name"), this.priorityHighestHealth.x + 35, this.priorityHighestHealth.y + 10 - mc.fontRenderer.FONT_HEIGHT / 2, ColorUtil.to32BitColor(255, 75, 75, 75));
    }

    @Override
    public void onIntruderInteraction()
    {

    }

    @Override
    public void onSelectionChanged(GuiElementSpinner spinner, int newVal)
    {
        for (GuiButton button : this.buttonList)
        {
            if (button instanceof GuiElementSpinner)
            {
                GuiElementSpinner spinner1 = (GuiElementSpinner) button;
                if (spinner1.value == newVal)
                {
                    spinner1.value = spinner.value;
                }
            }
        }

        if (newVal < 0)
        {
            newVal++;
            spinner.value++;
        }

        this.laserTurret.priorityClosest = spinner == priorityClosest ? newVal : priorityClosest.value;
        this.laserTurret.priorityLowestHealth = spinner == priorityLowestHealth ? newVal : priorityLowestHealth.value;
        this.laserTurret.priorityHighestHealth = spinner == priorityHighestHealth ? newVal : priorityHighestHealth.value;
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 3, this.laserTurret.getPos(), this.laserTurret.priorityClosest }));
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 4, this.laserTurret.getPos(), this.laserTurret.priorityLowestHealth }));
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleVenus(PacketSimpleVenus.EnumSimplePacketVenus.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 5, this.laserTurret.getPos(), this.laserTurret.priorityHighestHealth }));
    }

    @Override
    public boolean canPlayerEdit(GuiElementSpinner spinner, EntityPlayer player)
    {
        return player.getUniqueID().equals(this.laserTurret.getOwnerUUID());
    }

    @Override
    public int getInitialValue(GuiElementSpinner spinner)
    {
        if (spinner == this.priorityClosest)
        {
            return laserTurret.priorityClosest;
        }
        else if (spinner == this.priorityLowestHealth)
        {
            return laserTurret.priorityLowestHealth;
        }
        else if (spinner == this.priorityHighestHealth)
        {
            return laserTurret.priorityHighestHealth;
        }
        return 0;
    }
}
