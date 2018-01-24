package micdoodle8.mods.galacticraft.planets.venus.client.gui;

import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCrashedProbe extends GuiContainerGC
{
    private static final ResourceLocation guiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/crashed_probe.png");

    private final TileEntityCrashedProbe geothermalGenerator;

    public GuiCrashedProbe(InventoryPlayer par1InventoryPlayer, TileEntityCrashedProbe geothermalGenerator)
    {
        super(new ContainerCrashedProbe(par1InventoryPlayer, geothermalGenerator));
        this.geothermalGenerator = geothermalGenerator;
        this.ySize = 133;
        this.xSize = 176;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiCrashedProbe.guiTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = this.geothermalGenerator.getName();
        this.fontRenderer.drawString(displayString, this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 7, 4210752);
        this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 94, 4210752);
    }
}
