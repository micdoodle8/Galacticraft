package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCircuitFabricator;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

public class GuiCoalGenerator extends GuiContainerGC<ContainerCoalGenerator>
{
    private static final ResourceLocation COAL_GENERATOR_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/coal_generator.png");

    private TileEntityCoalGenerator coalGenerator;

    public GuiCoalGenerator(ContainerCoalGenerator container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerCoalGenerator(playerInv, coalGenerator), playerInv, new TranslationTextComponent("tile.machine.0"));
        this.coalGenerator = container.getGenerator();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(this.title.getFormattedText(), 55, 6, 4210752);
        String displayText = GCCoreUtil.translate("gui.status.generating");

        if (this.coalGenerator.heatGJperTick <= 0 || this.coalGenerator.heatGJperTick < TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK)
        {
            displayText = GCCoreUtil.translate("gui.status.not_generating");
        }

        this.font.drawString(displayText, 122 - this.font.getStringWidth(displayText) / 2, 33, 4210752);

        if (this.coalGenerator.heatGJperTick < TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK)
        {
            displayText = GCCoreUtil.translate("gui.status.hull_heat") + ": " + (int) (this.coalGenerator.heatGJperTick / TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK * 100) + "%";
        }
        else
        {
            displayText = EnergyDisplayHelper.getEnergyDisplayS(this.coalGenerator.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK) + "/t";
        }

        this.font.drawString(displayText, 122 - this.font.getStringWidth(displayText) / 2, 45, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        this.minecraft.textureManager.bindTexture(GuiCoalGenerator.COAL_GENERATOR_TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
        this.blit(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
    }
}
