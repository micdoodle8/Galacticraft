package micdoodle8.mods.galacticraft.planets.venus.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiCrashedProbe extends GuiContainerGC<ContainerCrashedProbe>
{
    private static final ResourceLocation guiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/crashed_probe.png");

    private final TileEntityCrashedProbe probe;

    public GuiCrashedProbe(ContainerCrashedProbe container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.probe = container.getProbe();
        this.ySize = 133;
        this.xSize = 176;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiCrashedProbe.guiTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = this.getTitle().getFormattedText();
        this.font.drawString(displayString, this.xSize / 2 - this.font.getStringWidth(displayString) / 2, 7, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 94, 4210752);
    }
}
