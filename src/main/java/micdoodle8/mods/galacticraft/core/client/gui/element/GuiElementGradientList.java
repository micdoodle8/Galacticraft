package micdoodle8.mods.galacticraft.core.client.gui.element;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;

public class GuiElementGradientList extends Gui
{
	public static class ListElement
	{
		public String value;
		public int color;
		
		public ListElement(String value, int color)
		{
			this.value = value;
			this.color = color;
		}
	}
	
    public static final int BUTTON_HEIGHT = 20;
	public int width;
	public int height;
    public int xPosition;
    public int yPosition;
    public List<ListElement> listContents = new ArrayList<ListElement>();
    public int sliderPos = this.yPosition + 1;
    private int lastMousePosY;
    private boolean sliderGrabbed;
    private boolean sliderEnabled;
    private int scrollOffset;
    private int selectedIndex = -1;
    
    public GuiElementGradientList(int xPos, int yPos, int width, int height)
    {
    	this.xPosition = xPos;
    	this.yPosition = yPos;
    	this.width = width;
    	this.height = height;
    }
    
    public void updateListContents(List<ListElement> newContents)
    {
    	this.listContents = newContents;
    	this.sliderEnabled = (this.listContents.size() * BUTTON_HEIGHT) > this.height;
    	if (this.selectedIndex >= this.listContents.size())
    	{
    		this.selectedIndex = -1;
    	}
    }
    
    public void draw(int mousePosX, int mousePosY)
    {
    	if (this.sliderEnabled)
    	{
        	if (sliderGrabbed || (mousePosX >= this.xPosition + this.width - 9 &&
        			mousePosX < this.xPosition + this.width &&
        			mousePosY >= this.yPosition &&
        			mousePosY < this.yPosition + this.height))
        	{
        		if (Mouse.isButtonDown(0))
        		{
        			sliderGrabbed = true;
        			
            		if (lastMousePosY > 0)
            		{
            			if (mousePosY >= this.sliderPos &&
                			mousePosY < this.sliderPos + 15)
            			{
                			int deltaY = this.lastMousePosY - this.sliderPos;
                			this.sliderPos = mousePosY - deltaY;
            			}
            			else
            			{
            				this.sliderPos = mousePosY - 7;
            			}
            		}
            		
            		this.lastMousePosY = mousePosY;	
        		}
        		else
        		{
        			sliderGrabbed = false;
        		}
        	}
        	else
        	{
        		this.lastMousePosY = 0;
        	}
    	}

		if (Mouse.isButtonDown(0))
		{
        	if (mousePosX >= this.xPosition &&
        			mousePosX < this.xPosition + this.width - 10 &&
        			mousePosY >= this.yPosition &&
        			mousePosY < this.yPosition + this.height)
        	{
        		int clickPosY = mousePosY - this.yPosition + (int)Math.floor((this.listContents.size() * BUTTON_HEIGHT - this.height) * this.getSliderPercentage());
        		this.selectedIndex = clickPosY / BUTTON_HEIGHT;
        		
        		if (this.selectedIndex < 0 || this.selectedIndex >= this.listContents.size())
        		{
        			this.selectedIndex = -1;
        		}
        	}
		}
    	
    	this.sliderPos = Math.min(Math.max(this.yPosition, this.sliderPos), this.yPosition + this.height - 15);
    	
        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width - 10, this.yPosition + this.height, GCCoreUtil.to32BitColor(255, 30, 30, 30), GCCoreUtil.to32BitColor(255, 30, 30, 30));
        this.drawGradientRect(this.xPosition + this.width - 9, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, GCCoreUtil.to32BitColor(255, 50, 50, 50), GCCoreUtil.to32BitColor(255, 50, 50, 50));
        int sliderColor = this.sliderEnabled ? GCCoreUtil.to32BitColor(255, 90, 90, 90) : GCCoreUtil.to32BitColor(255, 40, 40, 40);
        this.drawGradientRect(this.xPosition + this.width - 9, this.sliderPos, this.xPosition + this.width, this.sliderPos + 15, sliderColor, sliderColor);
        
        this.drawRect(this.xPosition + width - 1, this.yPosition, xPosition + width, yPosition + height, GCCoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.xPosition + width - 10, this.yPosition, xPosition + width - 9, yPosition + height, GCCoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.xPosition, this.yPosition, xPosition + 1, yPosition + height, GCCoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.xPosition, this.yPosition, xPosition + width, yPosition + 1, GCCoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.xPosition, yPosition + height - 1, xPosition + width, yPosition + height, GCCoreUtil.to32BitColor(255, 0, 0, 0));

        sliderColor = this.sliderEnabled ? GCCoreUtil.to32BitColor(255, 120, 120, 120) : GCCoreUtil.to32BitColor(255, 60, 60, 60);
        this.drawRect(this.xPosition + this.width - 9, this.sliderPos + 1, xPosition + this.width - 8, this.sliderPos + 14, sliderColor);
        this.drawRect(this.xPosition + this.width - 2, this.sliderPos + 1, xPosition + this.width - 1, this.sliderPos + 14, sliderColor);
        this.drawRect(this.xPosition + this.width - 9, this.sliderPos, xPosition + this.width - 1, this.sliderPos + 1, sliderColor);
        this.drawRect(this.xPosition + this.width - 9, this.sliderPos + 15, xPosition + this.width - 1, this.sliderPos + 14, sliderColor);
        
        this.scrollOffset = this.yPosition + 1 - (int)Math.floor((this.listContents.size() * BUTTON_HEIGHT - this.height) * this.getSliderPercentage());
        int currentDrawHeight = this.scrollOffset;
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
        
        for (int i = 0; i < this.listContents.size(); i++)
        {
        	ListElement displayButton = this.listContents.get(i);
        	
        	if (displayButton != null && displayButton.value != null && !displayButton.value.isEmpty())
        	{
        		int yCoord0 = currentDrawHeight;
        		int yCoord1 = currentDrawHeight + BUTTON_HEIGHT - 1;
        		
        		if (yCoord1 > this.yPosition && yCoord0 < this.yPosition + this.height)
        		{
        			yCoord0 = Math.max(this.yPosition + 1, yCoord0);
        			yCoord1 = Math.min(this.yPosition + this.height - 1, yCoord1);
        			
                	int color = i == this.selectedIndex ? GCCoreUtil.to32BitColor(255, 35, 35, 35) : GCCoreUtil.to32BitColor(255, 25, 25, 25);
        			
                    this.drawRect(this.xPosition + 1, yCoord0, this.xPosition + this.width - 10, yCoord1, color);
                    
                    if (currentDrawHeight + BUTTON_HEIGHT / 2 - fontRenderer.FONT_HEIGHT / 2 > this.yPosition &&
                    		currentDrawHeight + BUTTON_HEIGHT / 2 + fontRenderer.FONT_HEIGHT / 2 < this.yPosition + this.height)
                    {                    	
                        fontRenderer.drawString(displayButton.value, this.xPosition + (this.width - 10) / 2 - fontRenderer.getStringWidth(displayButton.value) / 2, currentDrawHeight + BUTTON_HEIGHT / 2 - fontRenderer.FONT_HEIGHT / 2, displayButton.color);
                    }
                    
        		}
        		
                currentDrawHeight += BUTTON_HEIGHT;
        	}
        }
    }
    
    public void update()
    {
    }
    
    private float getSliderPercentage()
    {
    	if (!this.sliderEnabled)
    	{
    		return 0.0F;
    	}
    	
    	return (this.sliderPos - this.yPosition) / (float)(height - 15);
    }
    
    public ListElement getSelectedElement()
    {
    	if (this.selectedIndex == -1)
    	{
    		return null;
    	}
    	
    	return this.listContents.get(this.selectedIndex);
    }
}
