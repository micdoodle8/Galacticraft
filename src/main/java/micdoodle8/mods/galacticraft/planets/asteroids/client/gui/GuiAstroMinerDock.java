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
import net.minecraft.client.gui.widget.button.Button;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiAstroMinerDock extends GuiContainerGC<ContainerAstroMinerDock>
{
    private static final ResourceLocation dockGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gui_astro_miner_dock.png");
    private final TileEntityMinerBase minerBase;
    private Button recallButton;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 233, (this.height - this.ySize) / 2 + 31, 10, 68, new ArrayList<String>(), this.width, this.height, this);
    private boolean extraLines;

    public GuiAstroMinerDock(ContainerAstroMinerDock container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.xSize = 256;
        this.ySize = 221;
        this.minerBase = container.getMinerBase();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.recallButton.active = true;

        if (this.minerBase.linkedMinerID == null)
        {
            this.recallButton.active = false;
        }
        else
        {
            if (this.minerBase.linkedMinerDataAIState < EntityAstroMiner.AISTATE_TRAVELLING || this.minerBase.linkedMinerDataAIState == EntityAstroMiner.AISTATE_DOCKING)
            {
                this.recallButton.active = false;
            }
        }
        this.recallButton.setMessage(GCCoreUtil.translate("gui.button.recall.name"));
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void init()
    {
        super.init();
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        List<String> electricityDesc = new ArrayList<>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.minerBase.getEnergyStoredGC()) + " / " + (int) Math.floor(this.minerBase.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = xPos + 233;
        this.electricInfoRegion.yPosition = yPos + 29;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion(xPos + 230, yPos + 108, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.buttons.add(this.recallButton = new Button(xPos + 173, yPos + 195, 76, 20, GCCoreUtil.translate("gui.button.recall.name"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(this.minecraft.world), new Object[]{this.minerBase.getPos(), 0}));
        }));
    }

    private String getDeltaString(int num)
    {
        return num > 0 ? "+" + num : "" + num;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(this.title.getFormattedText(), 7, 6, 4210752);
        this.font.drawString(this.getStatus(), 177, 141, 4210752);

        if (this.extraLines)
        {
            this.font.drawString("\u0394x: " + this.getDeltaString(this.minerBase.linkedMinerDataDX), 186, 152, 2536735);
        }
        if (this.extraLines)
        {
            this.font.drawString("\u0394y: " + this.getDeltaString(this.minerBase.linkedMinerDataDY), 186, 162, 2536735);
        }
        if (this.extraLines)
        {
            this.font.drawString("\u0394z: " + this.getDeltaString(this.minerBase.linkedMinerDataDZ), 186, 172, 2536735);
        }
        if (this.extraLines)
        {
            this.font.drawString(GCCoreUtil.translate("gui.miner.mined") + ": " + this.minerBase.linkedMinerDataCount, 177, 183, 2536735);
        }
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 7, this.ySize - 92, 4210752);
    }

    private String getStatus()
    {
        this.extraLines = false;

        switch (this.minerBase.linkedMinerDataAIState)
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.minecraft.getTextureManager().bindTexture(GuiAstroMinerDock.dockGui);
        this.blit(xPos, yPos, 0, 0, this.xSize, this.ySize);
        List<String> electricityDesc = new ArrayList<>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.minerBase.getEnergyStoredGC(), this.minerBase.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        this.minecraft.getTextureManager().bindTexture(GuiCargoLoader.loaderTexture);

        if (this.minerBase.getEnergyStoredGC() > 0)
        {
            this.blit(xPos + 233, yPos + 17, 176, 0, 11, 10);
        }

        int level = Math.min(this.minerBase.getScaledElecticalLevel(66), 66);
        this.drawColorModalRect(xPos + 234, yPos + 29 + 66 - level, 8, level, 0xc1aa24);
    }

    private void drawColorModalRect(int x, int y, int width, int height, int color)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x + 0, y + height, this.blitOffset).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        worldRenderer.pos(x + width, y + height, this.blitOffset).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        worldRenderer.pos(x + width, y + 0, this.blitOffset).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        worldRenderer.pos(x + 0, y + 0, this.blitOffset).color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.blendFunc(770, 771);
    }
}
