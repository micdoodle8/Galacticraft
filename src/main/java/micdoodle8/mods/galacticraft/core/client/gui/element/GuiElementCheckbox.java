package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;



public class GuiElementCheckbox extends GuiButton
{
	protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");
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
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.isSelected == null)
		{
			this.isSelected = this.parentGui.getInitiallySelected(this);
		}

		if (this.visible)
		{
			par1Minecraft.getTextureManager().bindTexture(GuiElementCheckbox.texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			this.drawTexturedModalRect(this.xPosition, this.yPosition, this.isSelected ? this.texX + this.texWidth : this.texX, this.field_146123_n ? (this.shiftOnHover ? this.texY + this.texHeight : this.texY) : this.texY, this.width, this.height);
			this.mouseDragged(par1Minecraft, par2, par3);
			par1Minecraft.fontRenderer.drawString(this.displayString, this.xPosition + this.width + 3, this.yPosition + (this.height - 6) / 2, this.textColor, false);
		}
	}

	@Override
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + this.texHeight) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + this.texWidth) * f), (double)((float)(par4 + this.texHeight) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + this.texWidth) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
    }

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.enabled && this.visible && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height)
		{
			if (this.parentGui.canPlayerEdit(this, par1Minecraft.thePlayer))
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

	public static interface ICheckBoxCallback
	{
		public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected);

		public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player);

		public boolean getInitiallySelected(GuiElementCheckbox checkbox);

		public void onIntruderInteraction();
	}
}
