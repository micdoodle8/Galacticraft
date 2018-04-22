package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiElementCheckboxPreLaunch extends GuiButton
{
    protected static final ResourceLocation texture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/checklist_book.png");
    public Boolean isSelected;
    private ICheckBoxCallback parentGui;
    private int textColor;
    private int texX;
    private int texY;

    public GuiElementCheckboxPreLaunch(int id, ICheckBoxCallback parentGui, int x, int y, String text)
    {
        this(id, parentGui, x, y, text, 4210752);
    }

    public GuiElementCheckboxPreLaunch(int id, ICheckBoxCallback parentGui, int x, int y, String text, int textColor)
    {
        this(id, parentGui, x, y, 9, 9, 194, 0, text, textColor);
    }

    private GuiElementCheckboxPreLaunch(int id, ICheckBoxCallback parentGui, int x, int y, int width, int height, int texX, int texY, String text, int textColor)
    {
        super(id, x, y, width, height, text);
        this.parentGui = parentGui;
        this.textColor = textColor;
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
            par1Minecraft.getTextureManager().bindTexture(GuiElementCheckboxPreLaunch.texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
            int texWidth = this.isSelected ? 12 : 9;
            int texHeight = this.isSelected ? 16 : 9;
            this.drawTexturedModalRect(this.x, this.isSelected ? this.y - 7 : this.y, this.hovered ? (this.texX + 12) : this.texX, this.isSelected ? this.texY + 9 : this.texY, texWidth, texHeight);
            this.mouseDragged(par1Minecraft, par2, par3);
            par1Minecraft.fontRenderer.drawSplitString(EnumColor.BLACK + this.displayString, this.x + this.width + 3, this.y + (this.height - 6) / 2, 100, ColorUtil.to32BitColor(255, 5, 5, 5));
        }
    }

    @Override
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        int texWidth = this.isSelected ? 12 : 9;
        int texHeight = this.isSelected ? 16 : 9;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(par1 + 0, par2 + par6, this.zLevel).tex((par3 + 0) * f, (par4 + texHeight) * f1).endVertex();
        worldRenderer.pos(par1 + par5, par2 + par6, this.zLevel).tex((par3 + texWidth) * f, (par4 + texHeight) * f1).endVertex();
        worldRenderer.pos(par1 + par5, par2 + 0, this.zLevel).tex((par3 + texWidth) * f, (par4 + 0) * f1).endVertex();
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

    public int willFit(int max)
    {
        int size = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(this.displayString, 100).size() * Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        GCLog.debug(displayString + " " + size + " " + max);
        if (size > max)
        {
            return -1;
        }
        return size;
    }

    public interface ICheckBoxCallback
    {
        void onSelectionChanged(GuiElementCheckboxPreLaunch checkbox, boolean newSelected);

        boolean canPlayerEdit(GuiElementCheckboxPreLaunch checkbox, EntityPlayer player);

        boolean getInitiallySelected(GuiElementCheckboxPreLaunch checkbox);

        void onIntruderInteraction();
    }
}
