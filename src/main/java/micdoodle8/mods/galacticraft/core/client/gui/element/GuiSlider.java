package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiSlider extends GuiButton
{
	private Vector3 firstColor;
	private Vector3 lastColor;
	private final boolean isVertical;
	private int sliderPos;
	
	public GuiSlider(int id, int x, int y, int width, int height, boolean vertical, Vector3 firstColor, Vector3 lastColor)
	{
		super(id, x, y, width, height, "");
		this.isVertical = vertical;
		this.firstColor = firstColor;
		this.lastColor = lastColor;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.visible)
		{
            this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            
			if (Mouse.isButtonDown(0) && this.field_146123_n)
			{
				this.sliderPos = par3 - this.yPosition;
			}
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	        GL11.glShadeModel(GL11.GL_SMOOTH);
	        Tessellator tessellator = Tessellator.instance;
	        
	        if (this.isVertical)
	        {
		        tessellator.startDrawingQuads();
		        tessellator.setColorRGBA_F(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F);
		        tessellator.addVertex((double)this.xPosition + this.width, (double)this.yPosition, (double)this.zLevel);
		        tessellator.addVertex((double)this.xPosition, (double)this.yPosition, (double)this.zLevel);
		        tessellator.setColorRGBA_F(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F);
		        tessellator.addVertex((double)this.xPosition, (double)this.yPosition + this.height, (double)this.zLevel);
		        tessellator.addVertex((double)this.xPosition + this.width, (double)this.yPosition + this.height, (double)this.zLevel);
		        tessellator.draw();

		        tessellator.startDrawingQuads();
		        tessellator.setColorRGBA_F(1, 1, 1, 1.0F);
		        tessellator.addVertex((double)this.xPosition + this.width, (double)this.yPosition + this.sliderPos - 1, (double)this.zLevel);
		        tessellator.addVertex((double)this.xPosition, (double)this.yPosition + this.sliderPos - 1, (double)this.zLevel);
		        tessellator.addVertex((double)this.xPosition, (double)this.yPosition + this.sliderPos + 1, (double)this.zLevel);
		        tessellator.addVertex((double)this.xPosition + this.width, (double)this.yPosition + this.sliderPos + 1, (double)this.zLevel);
		        tessellator.draw();
	        }
	        else
	        {
		        tessellator.startDrawingQuads();
		        tessellator.setColorRGBA_F(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F);
		        tessellator.addVertex((double)this.xPosition + this.width, (double)this.yPosition, (double)this.zLevel);
		        tessellator.setColorRGBA_F(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F);
		        tessellator.addVertex((double)this.xPosition, (double)this.yPosition, (double)this.zLevel);
		        tessellator.addVertex((double)this.xPosition, (double)this.yPosition + this.height, (double)this.zLevel);
		        tessellator.setColorRGBA_F(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F);
		        tessellator.addVertex((double)this.xPosition + this.width, (double)this.yPosition + this.height, (double)this.zLevel);
		        tessellator.draw();
	        }
	        
	        GL11.glShadeModel(GL11.GL_FLAT);
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public void setSliderPos(float pos)
	{
		this.sliderPos = (int) Math.floor(this.height * pos);
	}
	
	public int getSliderPos()
	{
		return this.sliderPos;
	}
	
	public float getNormalizedValue()
	{
		return this.sliderPos / (float)this.height;
	}
	
	public int getButtonHeight()
	{
		return this.height;
	}
}
