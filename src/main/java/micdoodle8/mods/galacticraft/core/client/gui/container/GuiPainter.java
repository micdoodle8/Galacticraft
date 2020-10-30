package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerPainter;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

public class GuiPainter extends GuiContainerGC<ContainerPainter>
{
    private static final ResourceLocation painterTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/painter.png");

    private final TileEntityPainter painter;

    public GuiPainter(ContainerPainter container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerPainter(playerInv, painter), playerInv, new TranslationTextComponent("tile.machine3.9"));
        this.painter = container.getPainter();
        this.ySize = 186;
    }

    @Override
    protected void init()
    {
        super.init();
        this.buttons.add(new Button(this.width / 2 + 4, this.height / 2 - 48, 76, 20, GCCoreUtil.translate("gui.button.paintapply"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.world), new Object[]{this.painter.getPos(), 0}));
            painter.buttonPressed(0, this.minecraft.player);
        }));
        this.buttons.add(new Button(this.width / 2 - 80, this.height / 2 - 48, 76, 20, GCCoreUtil.translate("gui.button.paintmix"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.world), new Object[]{this.painter.getPos(), 1}));
            painter.buttonPressed(1, this.minecraft.player);
        }));
        this.buttons.add(new Button(this.width / 2 - 80, this.height / 2 - 48 + 22, 76, 20, GCCoreUtil.translate("gui.button.paintreset"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.minecraft.world), new Object[]{this.painter.getPos(), 2}));
            painter.buttonPressed(2, this.minecraft.player);
        }));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 39, 6, 4210752);
        String displayText = "";

        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bindTexture(GuiPainter.painterTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int guiLeft = (this.width - this.xSize) / 2;
        int guiBottom = (this.height - this.ySize) / 2;
        this.blit(guiLeft, guiBottom, 0, 0, this.xSize, this.ySize);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        ColorUtil.setGLColor(painter.guiColor);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        int x = guiLeft + this.xSize / 2 - 9;
        int y = guiBottom + this.ySize / 2 - 69;
        int height = 18;
        int width = 18;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x + 0F, y + height, this.getBlitOffset()).endVertex();
        worldrenderer.pos(x + width, y + height, this.getBlitOffset()).endVertex();
        worldrenderer.pos(x + width, y + 0, this.getBlitOffset()).endVertex();
        worldrenderer.pos(x + 0F, y + 0, this.getBlitOffset()).endVertex();
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
