package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiElementCheckbox extends GuiButton
{
    protected static final ResourceLocation texture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/gui.png");
    public Boolean isSelected;
    private ICheckBoxCallback parentGui;
    private int textColor;
    private int texWidth;
    private int texHeight;
    private int texX;
    private int texY;
    private boolean shiftOnHover;

    public GuiElementCheckbox(int id, ICheckBoxCallback parentGui, int x, int y, String text)
    {
        this(id, parentGui, x, y, text, 4210752);
    }

    public GuiElementCheckbox(int id, ICheckBoxCallback parentGui, int x, int y, String text, int textColor)
    {
        this(id, parentGui, x, y, 13, 13, 20, 24, text, textColor);
    }

    private GuiElementCheckbox(int id, ICheckBoxCallback parentGui, int x, int y, int width, int height, int texX, int texY, String text, int textColor)
    {
        this(id, parentGui, x, y, width, height, width, height, texX, texY, text, textColor, true);
    }

    public GuiElementCheckbox(int id, ICheckBoxCallback parentGui, int x, int y, int width, int height, int texWidth, int texHeight, int texX, int texY, String text, int textColor, boolean shiftOnHover)
    {
        super(id, x, y, width, height, text);
        this.parentGui = parentGui;
        this.textColor = textColor;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.shiftOnHover = shiftOnHover;
        this.texX = texX;
        this.texY = texY;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3, float partial)
    {
        if (this.isSelected == null)
        {
            this.isSelected = this.parentGui.getInitiallySelected(this);
        }

        if (this.visible)
        {
            par1Minecraft.getTextureManager().bindTexture(GuiElementCheckbox.texture);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
            this.drawTexturedModalRect(this.x, this.y, this.isSelected ? this.texX + this.texWidth : this.texX, this.hovered ? this.shiftOnHover ? this.texY + this.texHeight : this.texY : this.texY, this.width, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
            par1Minecraft.fontRenderer.drawString(this.displayString, this.x + this.width + 3, this.y + (this.height - 6) / 2, this.textColor, false);
        }
    }

    @Override
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(par1 + 0, par2 + par6, this.zLevel).tex((par3 + 0) * f, (par4 + this.texHeight) * f1).endVertex();
        worldRenderer.pos(par1 + par5, par2 + par6, this.zLevel).tex((par3 + this.texWidth) * f, (par4 + this.texHeight) * f1).endVertex();
        worldRenderer.pos(par1 + par5, par2 + 0, this.zLevel).tex((par3 + this.texWidth) * f, (par4 + 0) * f1).endVertex();
        worldRenderer.pos(par1 + 0, par2 + 0, this.zLevel).tex((par3 + 0) * f, (par4 + 0) * f1).endVertex();
        tessellator.draw();
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.enabled && this.visible && par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height)
        {
            if (this.parentGui.canPlayerEdit(this, par1Minecraft.player))
            {
                this.isSelected = !this.isSelected;
                this.parentGui.onSelectionChanged(this, this.isSelected);
                return true;
            }
            else
            {
                this.parentGui.onIntruderInteraction();
            }
        }

        return false;
    }

    public interface ICheckBoxCallback
    {
        void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected);

        boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player);

        boolean getInitiallySelected(GuiElementCheckbox checkbox);

        void onIntruderInteraction();
    }
}
