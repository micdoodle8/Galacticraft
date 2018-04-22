package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerPainter;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiPainter extends GuiContainerGC
{
    private static final ResourceLocation painterTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/painter.png");

    private TileEntityPainter tileEntity;
    private GuiButton buttonApplyPaint;

    public GuiPainter(InventoryPlayer par1InventoryPlayer, TileEntityPainter tileEntity)
    {
        super(new ContainerPainter(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        this.ySize = 186;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(this.buttonApplyPaint = new GuiButton(0, this.width / 2 + 4, this.height / 2 - 48, 76, 20, GCCoreUtil.translate("gui.button.paintapply.name")));
        this.buttonList.add(this.buttonApplyPaint = new GuiButton(1, this.width / 2 - 80, this.height / 2 - 48, 76, 20, GCCoreUtil.translate("gui.button.paintmix.name")));
        this.buttonList.add(this.buttonApplyPaint = new GuiButton(2, this.width / 2 - 80, this.height / 2 - 48 + 22, 76, 20, GCCoreUtil.translate("gui.button.paintreset.name")));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(this.mc.world), new Object[] { this.tileEntity.getPos(), par1GuiButton.id }));
        tileEntity.buttonPressed(par1GuiButton.id, this.mc.player, Side.CLIENT);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.tileEntity.getName(), 39, 6, 4210752);
        String displayText = "";

        this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.bindTexture(GuiPainter.painterTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int guiLeft = (this.width - this.xSize) / 2;
        int guiBottom = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(guiLeft, guiBottom, 0, 0, this.xSize, this.ySize);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        ColorUtil.setGLColor(tileEntity.guiColor);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        int x = guiLeft + this.xSize / 2 - 9;
        int y = guiBottom + this.ySize / 2 - 69;
        int height = 18;
        int width = 18;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)(x + 0F), (double)(y + height), (double)this.zLevel).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), (double)this.zLevel).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).endVertex();
        worldrenderer.pos((double)(x + 0F), (double)(y + 0), (double)this.zLevel).endVertex();
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
