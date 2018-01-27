package micdoodle8.mods.galacticraft.planets.asteroids.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiCargoLoader;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiAstroMinerDock extends GuiContainerGC
{
    private static final ResourceLocation dockGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gui_astro_miner_dock.png");

    private TileEntityMinerBase tile;

    private GuiButton recallButton;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 233, (this.height - this.ySize) / 2 + 31, 10, 68, new ArrayList<String>(), this.width, this.height, this);

    private boolean extraLines;

    public GuiAstroMinerDock(InventoryPlayer playerInventory, TileEntityMinerBase dock)
    {
        super(new ContainerAstroMinerDock(playerInventory, dock));
        this.xSize = 256;
        this.ySize = 221;
        this.tile = dock;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.recallButton.enabled = true;
        if (this.tile.linkedMinerID == null)
        {
            this.recallButton.enabled = false;
        }
        else
        {
            if (this.tile.linkedMinerDataAIState < EntityAstroMiner.AISTATE_TRAVELLING || this.tile.linkedMinerDataAIState == EntityAstroMiner.AISTATE_DOCKING)
            {
                this.recallButton.enabled = false;
            }
        }
        this.recallButton.displayString = GCCoreUtil.translate("gui.button.recall.name");

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.tile.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tile.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = xPos + 233;
        this.electricInfoRegion.yPosition = yPos + 29;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion(xPos + 230, yPos + 108, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.buttonList.add(this.recallButton = new GuiButton(0, xPos + 173, yPos + 195, 76, 20, GCCoreUtil.translate("gui.button.recall.name")));
    }

    @Override
    protected void mouseClicked(int px, int py, int par3) throws IOException
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
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.tile.getPos(), 0 }));
                break;
            default:
                break;
            }
        }
    }

    private String getDeltaString(int num)
    {
        return (num > 0) ? "+" + num : "" + num;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(this.tile.getName(), 7, 6, 4210752);
        this.fontRendererObj.drawString(this.getStatus(), 177, 141, 4210752);
        if (this.extraLines)
        {
            this.fontRendererObj.drawString("\u0394x: " + getDeltaString(this.tile.linkedMinerDataDX), 186, 152, 2536735);
        }
        if (this.extraLines)
        {
            this.fontRendererObj.drawString("\u0394y: " + getDeltaString(this.tile.linkedMinerDataDY), 186, 162, 2536735);
        }
        if (this.extraLines)
        {
            this.fontRendererObj.drawString("\u0394z: " + getDeltaString(this.tile.linkedMinerDataDZ), 186, 172, 2536735);
        }
        if (this.extraLines)
        {
            this.fontRendererObj.drawString(GCCoreUtil.translate("gui.miner.mined") + ": " + this.tile.linkedMinerDataCount, 177, 183, 2536735);
        }
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 7, this.ySize - 92, 4210752);
    }

    private String getStatus()
    {
        this.extraLines = false;
        switch (this.tile.linkedMinerDataAIState)
        {
        case -3:  //no linked miner
            return "";
        case -2:
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.miner.out_of_range");
        case EntityAstroMiner.AISTATE_OFFLINE:
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.miner.offline");
        case EntityAstroMiner.AISTATE_STUCK:
            this.extraLines = true;
            return EnumColor.RED + GCCoreUtil.translate("gui.miner.stuck");
        case EntityAstroMiner.AISTATE_ATBASE:
            return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.miner.docked");
        case EntityAstroMiner.AISTATE_TRAVELLING:
            this.extraLines = true;
            return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.miner.travelling");
        case EntityAstroMiner.AISTATE_MINING:
            this.extraLines = true;
            return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.miner.mining");
        case EntityAstroMiner.AISTATE_RETURNING:
            this.extraLines = true;
            return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.miner.returning");
        case EntityAstroMiner.AISTATE_DOCKING:
            return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.miner.docking");
        }
        return "";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(GuiAstroMinerDock.dockGui);
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.tile.getEnergyStoredGC(), this.tile.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        this.mc.getTextureManager().bindTexture(GuiCargoLoader.loaderTexture);
        if (this.tile.getEnergyStoredGC() > 0)
        {
            this.drawTexturedModalRect(xPos + 233, yPos + 17, 176, 0, 11, 10);
        }

        int level = Math.min(this.tile.getScaledElecticalLevel(66), 66);
        this.drawColorModalRect(xPos + 234, yPos + 29 + 66 - level, 8, level, 0xc1aa24);
    }

    public void drawColorModalRect(int x, int y, int width, int height, int color)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        worldRenderer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        worldRenderer.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        worldRenderer.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
