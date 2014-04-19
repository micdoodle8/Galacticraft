package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector2;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementGradientButton;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementSlider;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GuiNewSpaceRace extends GuiScreen implements ICheckBoxCallback
{
	protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");
	
	public static enum EnumSpaceRaceGui
	{
		MAIN,
		RULES,
		DESIGN_FLAG
	}
	
    private int ticksPassed;
    private EntityPlayer thePlayer;
	private GuiElementCheckbox checkboxCompete;
	private GuiElementCheckbox checkboxUploadScore;
	private GuiElementCheckbox checkboxShowFace;
	private GuiElementCheckbox checkboxPaintbrush;
	private GuiElementCheckbox checkboxShowGrid;
	private GuiElementCheckbox checkboxEraser;
	private GuiElementCheckbox checkboxSelector;
	private GuiElementCheckbox checkboxColorSelector;
	private boolean initialized;
	private GuiElementSlider sliderColorR;
	private GuiElementSlider sliderColorG;
	private GuiElementSlider sliderColorB;
	private GuiElementSlider sliderBrushSize;
	private GuiElementSlider sliderEraserSize;
	private EnumSpaceRaceGui currentState = EnumSpaceRaceGui.MAIN;
    
    private int buttonFlag_width;
    private int buttonFlag_height;
    private int buttonFlag_xPosition;
    private int buttonFlag_yPosition;
    private boolean buttonFlag_hover;

    private Vector2 flagDesignerScale = new Vector2();
    private float flagDesignerMinX;
    private float flagDesignerMinY;
    private float flagDesignerWidth;
    private float flagDesignerHeight;
	
    private boolean optionCompete = true;
    private boolean optionUpload = true;
    
    private int selectionMinX;
    private int selectionMaxX;
    private int selectionMinY;
    private int selectionMaxY;

	private EntityFlag dummyFlag = new EntityFlag(FMLClientHandler.instance().getClient().theWorld);
	private ModelFlag dummyModel = new ModelFlag();
	
	private FlagData flagData = new FlagData(48, 32);
	
	private boolean showGrid = false;
	private boolean lastMousePressed = false;
    
	public GuiNewSpaceRace(EntityPlayer player)
	{
		this.thePlayer = player;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		float sliderR = 0;
		float sliderG = 0;
		float sliderB = 0;
		
		if (this.sliderColorR != null && this.sliderColorG != null && this.sliderColorR != null)
		{
			sliderR = this.sliderColorR.getSliderPos() / (float)this.sliderColorR.getButtonHeight();
			sliderG = this.sliderColorG.getSliderPos() / (float)this.sliderColorG.getButtonHeight();
			sliderB = this.sliderColorB.getSliderPos() / (float)this.sliderColorB.getButtonHeight();
		}
		
		super.initGui();
		this.buttonList.clear();
		
		if (this.initialized)
		{
			final int var5 = (this.width - this.width / 4) / 2;
			final int var6 = (this.height - this.height / 4) / 2;

			this.buttonFlag_width = 78;
			this.buttonFlag_height = 31;
			this.buttonFlag_xPosition = this.width / 2 - buttonFlag_width / 2 + 50;
			this.buttonFlag_yPosition = this.height / 2 - buttonFlag_height / 2 + 3;
			
			this.buttonList.add(new GuiElementGradientButton(0, this.width / 2 - this.width / 3 + 15, this.height / 2 - this.height / 4 - 15, 50, 15, this.currentState == EnumSpaceRaceGui.MAIN ? "Close" : "Back"));
			
			switch (this.currentState)
			{
			case MAIN:
				this.checkboxCompete = new GuiElementCheckbox(1, this, this.width / 2 - 125, var6 - 10, "Compete against other players on this world?", GCCoreUtil.to32BitColor(255, 240, 240, 240));
				this.checkboxUploadScore = new GuiElementCheckbox(2, this, this.width / 2 - 125, var6 + 5, "Upload score to Galacticraft website?", GCCoreUtil.to32BitColor(255, 240, 240, 240));
				this.buttonList.add(new GuiElementGradientButton(3, this.width / 2 + this.width / 3 - 125, this.height / 2 + this.height / 4 - 25, 50, 15, "Rules"));
				this.buttonList.add(new GuiElementGradientButton(4, this.width / 2 + this.width / 3 - 65, this.height / 2 + this.height / 4 - 25, 50, 15, "Done"));
				this.buttonList.add(this.checkboxCompete);
				this.buttonList.add(this.checkboxUploadScore);
				break;
			case RULES:
				break;
			case DESIGN_FLAG:
				int guiBottom = this.height / 2 + this.height / 4;
				int guiTop = this.height / 2 - this.height / 4;
				int guiLeft = this.width / 2 - this.width / 3;
	        	int guiRight = this.width / 2 + this.width / 3;
				this.flagDesignerScale = new Vector2(this.width / 130.0F, this.height / 70.0F);
	        	this.flagDesignerMinX = this.width / 2 - (this.flagData.getWidth() * (float)this.flagDesignerScale.x) / 2;
	        	this.flagDesignerMinY = this.height / 2 - (this.flagData.getHeight() * (float)this.flagDesignerScale.y) / 2;
	        	this.flagDesignerWidth = this.flagData.getWidth() * (float)this.flagDesignerScale.x;
	        	this.flagDesignerHeight = this.flagData.getHeight() * (float)this.flagDesignerScale.y;
	        	int flagDesignerRight = (int) (this.flagDesignerMinX + this.flagDesignerWidth);
	        	int availWidth = (int) (((guiRight - 10) - (this.flagDesignerMinX + this.flagDesignerWidth + 10)) / 3);
		        float x1 = flagDesignerRight + 10;
		        float x2 = guiRight - 10;
		        float y1 = guiBottom - 10 - (x2 - x1);
	        	int height = (int) ((y1 - 10) - (guiTop + 10));
				this.sliderColorR = new GuiElementSlider(1, flagDesignerRight + 10, guiTop + 10, availWidth, height, true, new Vector3(0, 0, 0), new Vector3(1, 0, 0));
				this.sliderColorG = new GuiElementSlider(2, flagDesignerRight + 11 + availWidth, guiTop + 10, availWidth, height, true, new Vector3(0, 0, 0), new Vector3(0, 1, 0));
				this.sliderColorB = new GuiElementSlider(3, flagDesignerRight + 12 + availWidth * 2, guiTop + 10, availWidth, height, true, new Vector3(0, 0, 0), new Vector3(0, 0, 1));
				this.checkboxShowFace = new GuiElementCheckbox(4, this, this.width / 2 - this.width / 3 + 2, this.height / 2 + this.height / 4 - 16, "Show Face");
				this.sliderColorR.setSliderPos(sliderR);
				this.sliderColorG.setSliderPos(sliderG);
				this.sliderColorB.setSliderPos(sliderB);
				this.buttonList.add(this.sliderColorR);
				this.buttonList.add(this.sliderColorG);
				this.buttonList.add(this.sliderColorB);
				this.buttonList.add(this.checkboxShowFace);
				this.checkboxPaintbrush = new GuiElementCheckbox(5, this, (int) (this.flagDesignerMinX - 15), this.height / 2 - this.height / 4 + 10, 13, 13, 26, 26, 133, 0, "", 4210752, false);
				this.checkboxEraser = new GuiElementCheckbox(6, this, (int) (this.flagDesignerMinX - 15), this.height / 2 - this.height / 4 + 25, 13, 13, 26, 26, 133, 52, "", 4210752, false);
				this.checkboxSelector = new GuiElementCheckbox(7, this, (int) (this.flagDesignerMinX - 15), this.height / 2 - this.height / 4 + 40, 13, 13, 26, 26, 133, 78, "", 4210752, false);
				this.checkboxColorSelector = new GuiElementCheckbox(8, this, (int) (this.flagDesignerMinX - 15), this.height / 2 - this.height / 4 + 55, 13, 13, 26, 26, 133, 104, "", 4210752, false);
				this.checkboxShowGrid = new GuiElementCheckbox(9, this, (int) (this.flagDesignerMinX - 15), this.height / 2 - this.height / 4 + 90, 13, 13, 26, 26, 133, 26, "", 4210752, false);
				this.sliderBrushSize = new GuiElementSlider(10, checkboxPaintbrush.xPosition - 40, checkboxPaintbrush.yPosition, 35, 13, false, new Vector3(0.34, 0.34, 0.34), new Vector3(0.34, 0.34, 0.34), "Brush Size");
				this.sliderEraserSize = new GuiElementSlider(11, checkboxEraser.xPosition - 40, checkboxEraser.yPosition, 35, 13, false, new Vector3(0.34, 0.34, 0.34), new Vector3(0.34, 0.34, 0.34), "Eraser Size");
				this.sliderEraserSize.visible = false;
				this.buttonList.add(this.checkboxPaintbrush);
				this.buttonList.add(this.checkboxShowGrid);
				this.buttonList.add(this.checkboxEraser);
				this.buttonList.add(this.checkboxSelector);
				this.buttonList.add(this.checkboxColorSelector);
				this.buttonList.add(this.sliderBrushSize);
				this.buttonList.add(this.sliderEraserSize);
				
				break;
			default:
				break;
			}
		}
		else
		{
			for (int i = 0; i < this.flagData.getWidth(); i++)
			{
				for (int j = 0; j < this.flagData.getHeight(); j++)
				{
					this.flagData.setColorAt(i, j, new Vector3(255, 255, 255));
				}
			}
		}
	}
	
    protected void actionPerformed(GuiButton buttonClicked) 
    {
    	switch (buttonClicked.id)
    	{
    	case 0:
    		if (this.currentState == EnumSpaceRaceGui.MAIN)
    		{
    			this.thePlayer.closeScreen();
    		}
    		else
    		{
        		this.currentState = EnumSpaceRaceGui.MAIN;
        		this.initGui();
    		}
    		break;
    	case 3:
    		if (this.currentState == EnumSpaceRaceGui.MAIN)
    		{
        		this.currentState = EnumSpaceRaceGui.RULES;
        		this.initGui();
    		}
    		break;
    	case 4:
    		if (this.currentState == EnumSpaceRaceGui.MAIN)
    		{
        		List<Object> objList = new ArrayList<Object>();
        		objList.add("NewTeam");
        		objList.add(this.flagData);
        		objList.add(new String[] { this.thePlayer.getGameProfile().getName() });
        		GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_START_NEW_SPACE_RACE, objList));
        		List<String> players = new ArrayList<String>();
        		players.add(this.thePlayer.getGameProfile().getName());
        		SpaceRaceManager.addSpaceRace(players, "NewTeam", flagData);
        		this.thePlayer.closeScreen();
    		}
    		break;
		default:
			break;
    	}
    }

    protected void mouseClicked(int x, int y, int clickIndex)
    {
    	super.mouseClicked(x, y, clickIndex);
    	
        if (clickIndex == 0)
        {
        	if (this.buttonFlag_hover)
        	{
        		this.currentState = EnumSpaceRaceGui.DESIGN_FLAG;
        		this.initGui();
        	}
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
        ++this.ticksPassed;
        
        if (!this.initialized)
        {
        	return;
        }

        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        
        if (x >= this.flagDesignerMinX && y >= this.flagDesignerMinY && x <= this.flagDesignerMinX + this.flagDesignerWidth && y <= this.flagDesignerMinY + this.flagDesignerHeight)
    	{
    		int unScaledX = (int)Math.floor((x - this.flagDesignerMinX) / this.flagDesignerScale.x);
    		int unScaledY = (int)Math.floor((y - this.flagDesignerMinY) / this.flagDesignerScale.y);
    		
    		if (Mouse.isButtonDown(0))
    		{
        		if (this.checkboxEraser.isSelected != null && this.checkboxEraser.isSelected)
        		{
            		this.setColorWithBrushSize(unScaledX, unScaledY, new Vector3(255, 255, 255), (int)Math.floor(this.sliderEraserSize.getNormalizedValue() * 10) + 1);
        		}
        		else if (this.checkboxColorSelector.isSelected != null && this.checkboxColorSelector.isSelected)
        		{
        			Vector3 colorAt = this.flagData.getColorAt(unScaledX, unScaledY);
        			this.sliderColorR.setSliderPos(colorAt.floatX());
        			this.sliderColorG.setSliderPos(colorAt.floatY());
        			this.sliderColorB.setSliderPos(colorAt.floatZ());
        		}
        		else if (this.checkboxPaintbrush.isSelected != null && this.checkboxPaintbrush.isSelected)
        		{
            		this.setColorWithBrushSize(unScaledX, unScaledY, new Vector3(this.sliderColorR.getNormalizedValue() * 256, this.sliderColorG.getNormalizedValue() * 256, this.sliderColorB.getNormalizedValue() * 256), (int)Math.floor(this.sliderBrushSize.getNormalizedValue() * 10) + 1);
        		}
    		}
    	}
		
        if (this.checkboxSelector != null)
        {
    		if (!this.lastMousePressed && Mouse.isButtonDown(0) && this.checkboxSelector.isSelected != null && this.checkboxSelector.isSelected)
    		{
    			if (x >= this.flagDesignerMinX && y >= this.flagDesignerMinY && x <= this.flagDesignerMinX + this.flagDesignerWidth && y <= this.flagDesignerMinY + this.flagDesignerHeight)
    	    	{
    	    		int unScaledX = (int)Math.floor((x - this.flagDesignerMinX) / this.flagDesignerScale.x);
    	    		int unScaledY = (int)Math.floor((y - this.flagDesignerMinY) / this.flagDesignerScale.y);
    				this.selectionMinX = unScaledX;
    				this.selectionMinY = unScaledY;
    	    	}
    			else
    			{
    				this.selectionMinX = this.selectionMinY = -1;
    			}
    		}
    		else if (this.lastMousePressed && !Mouse.isButtonDown(0) && this.checkboxSelector.isSelected != null && this.checkboxSelector.isSelected)
    		{
    			if (selectionMinX != -1 && selectionMinY != -1 && x >= this.flagDesignerMinX && y >= this.flagDesignerMinY && x <= this.flagDesignerMinX + this.flagDesignerWidth && y <= this.flagDesignerMinY + this.flagDesignerHeight)
    	    	{
    	    		int unScaledX = (int)Math.floor((x - this.flagDesignerMinX) / this.flagDesignerScale.x);
    	    		int unScaledY = (int)Math.floor((y - this.flagDesignerMinY) / this.flagDesignerScale.y);
    				this.selectionMaxX = Math.min(unScaledX + 1, this.flagData.getWidth());
    				this.selectionMaxY = Math.min(unScaledY + 1, this.flagData.getHeight());
    				
    				if (this.selectionMinX > this.selectionMaxX)
    				{
    					int temp = this.selectionMaxX - 1;
    					this.selectionMaxX = this.selectionMinX + 1;
    					this.selectionMinX = temp;
    				}
    				
    				if (this.selectionMinY > this.selectionMaxY)
    				{
    					int temp = this.selectionMaxY - 1;
    					this.selectionMaxY = this.selectionMinY + 1;
    					this.selectionMinY = temp;
    				}
    	    	}
    			else
    			{
    				this.selectionMaxX = this.selectionMaxY = -1;
    			}
    		}
        }
        
        if (this.sliderBrushSize != null && this.sliderBrushSize.visible)
        {
        	this.sliderBrushSize.displayString = "Brush Rad: " + ((int)Math.floor(this.sliderBrushSize.getNormalizedValue() * 10) + 1);
        }
        
        if (this.sliderEraserSize != null && this.sliderEraserSize.visible)
        {
        	this.sliderEraserSize.displayString = "Eraser Rad: " + ((int)Math.floor(this.sliderEraserSize.getNormalizedValue() * 10) + 1);
        }
        
        this.lastMousePressed = Mouse.isButtonDown(0);
    }
    
    private void setColor(int unScaledX, int unScaledY, Vector3 color)
    {
		if (this.selectionMaxX - selectionMinX > 0 && this.selectionMaxY - selectionMinY > 0)
		{
			if (unScaledX >= this.selectionMinX && unScaledX <= this.selectionMaxX - 1 && unScaledY >= this.selectionMinY && unScaledY <= this.selectionMaxY - 1)
			{
	    		this.flagData.setColorAt(unScaledX, unScaledY, color);
			}
		}
		else
		{
    		this.flagData.setColorAt(unScaledX, unScaledY, color);
		}
    }
    
    private void setColorWithBrushSize(int unScaledX, int unScaledY, Vector3 color, int brushSize)
    {
    	for (int x = unScaledX - brushSize + 1; x < unScaledX + brushSize; x++)
    	{
        	for (int y = unScaledY - brushSize + 1; y < unScaledY + brushSize; y++)
        	{
        		if (x >= 0 && x < this.flagData.getWidth() && y >= 0 && y < this.flagData.getHeight())
        		{
        			float relativeX = (x + 0.5F) - (unScaledX + 0.5F);
        			float relativeY = (y + 0.5F) - (unScaledY + 0.5F);
        			
            		if (Math.sqrt(relativeX * relativeX + relativeY * relativeY) <= brushSize)
            		{
                		this.setColor(x, y, color);
            		}
        		}
        	}
    	}
    }
    
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
		final int var5 = (this.width - this.width / 4) / 2;
		final int var6 = (this.height - this.height / 4) / 2;
		
		if (this.initialized)
		{
			this.buttonFlag_hover = this.currentState == EnumSpaceRaceGui.MAIN && par1 >= this.buttonFlag_xPosition && par2 >= this.buttonFlag_yPosition && par1 < this.buttonFlag_xPosition + this.buttonFlag_width && par2 < this.buttonFlag_yPosition + this.buttonFlag_height;
			
	        switch (this.currentState)
	        {
	        case MAIN:
				this.drawCenteredString(this.fontRendererObj, "New Space Race", this.width / 2, var6 - 25, 16777215);
	        	this.drawFlagButton(par1, par2);
	    		String rememberStr = EnumColor.RED + "Remember: You're competing against time spent in-game before reaching space, not who gets to space first!";
	    		int trimWidth = this.width / 4 + 65;
	            List list2 = this.fontRendererObj.listFormattedStringToWidth(rememberStr, trimWidth);
	    		this.fontRendererObj.drawSplitString(rememberStr, this.width / 2 - this.width / 3 + 7, this.height / 2 + this.height / 4 - list2.size() * this.fontRendererObj.FONT_HEIGHT - 5, trimWidth, GCCoreUtil.to32BitColor(255, 100, 100, 100));
	        	break;
	        case RULES:
				this.drawCenteredString(this.fontRendererObj, "Rules", this.width / 2, var6 - 25, 16777215);
	        	break;
	        case DESIGN_FLAG:
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		        GL11.glDisable(GL11.GL_TEXTURE_2D);
		        GL11.glEnable(GL11.GL_BLEND);
		        GL11.glDisable(GL11.GL_ALPHA_TEST);
		        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		        GL11.glShadeModel(GL11.GL_SMOOTH);
		        Tessellator tessellator = Tessellator.instance;
    	        
	        	for (int x = 0; x < this.flagData.getWidth(); x++)
	        	{
	        		for (int y = 0; y < this.flagData.getHeight(); y++)
	        		{
	        			Vector3 color = this.flagData.getColorAt(x, y);
	        	        GL11.glColor4f(color.floatX(), color.floatY(), color.floatZ(), 1.0F);
	        	        tessellator.startDrawingQuads();
	        	        tessellator.addVertex((double)this.flagDesignerMinX + x * this.flagDesignerScale.x, (double)this.flagDesignerMinY + y * this.flagDesignerScale.y + 1 * this.flagDesignerScale.y, 0.0D);
	        	        tessellator.addVertex((double)this.flagDesignerMinX + x * this.flagDesignerScale.x + 1 * this.flagDesignerScale.x, (double)this.flagDesignerMinY + y * this.flagDesignerScale.y + 1 * this.flagDesignerScale.y, 0.0D);
	        	        tessellator.addVertex((double)this.flagDesignerMinX + x * this.flagDesignerScale.x + 1 * this.flagDesignerScale.x, (double)this.flagDesignerMinY + y * this.flagDesignerScale.y, 0.0D);
	        	        tessellator.addVertex((double)this.flagDesignerMinX + x * this.flagDesignerScale.x, (double)this.flagDesignerMinY + y * this.flagDesignerScale.y, 0.0D);
	        	        tessellator.draw();
	        		}
	        	}
	        	
	        	
	        	if (this.checkboxShowGrid != null && this.checkboxShowGrid.isSelected != null && this.checkboxShowGrid.isSelected)
	        	{
		        	for (int x = 0; x <= this.flagData.getWidth(); x++)
		        	{
			        	tessellator.startDrawing(GL11.GL_LINES);
			        	tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
			        	tessellator.addVertex(this.flagDesignerMinX + x * this.flagDesignerScale.x, this.flagDesignerMinY, this.zLevel);
			        	tessellator.addVertex(this.flagDesignerMinX + x * this.flagDesignerScale.x, this.flagDesignerMinY + this.flagDesignerHeight, this.zLevel);
		        		tessellator.draw();
		        	}
		        	
		        	for (int y = 0; y <= this.flagData.getHeight(); y++)
	        		{
			        	tessellator.startDrawing(GL11.GL_LINES);
			        	tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
			        	tessellator.addVertex(this.flagDesignerMinX, this.flagDesignerMinY + y * this.flagDesignerScale.y, this.zLevel);
			        	tessellator.addVertex(this.flagDesignerMinX + this.flagDesignerWidth, this.flagDesignerMinY + y * this.flagDesignerScale.y, this.zLevel);
		        		tessellator.draw();	
	        		}
	        	}
	        	
	        	if (!(this.lastMousePressed && this.checkboxSelector.isSelected != null && this.checkboxSelector.isSelected) && this.selectionMaxX - selectionMinX > 0 && this.selectionMaxY - selectionMinY > 0)
	        	{
		        	tessellator.startDrawing(GL11.GL_LINE_STRIP);
		        	float col = (float) (Math.sin(this.ticksPassed * 0.3) * 0.4 + 0.1);
		        	tessellator.setColorRGBA_F(col, col, col, 1.0F);
		        	tessellator.addVertex(this.flagDesignerMinX + this.selectionMinX * this.flagDesignerScale.x, this.flagDesignerMinY + this.selectionMinY * this.flagDesignerScale.y, this.zLevel);
		        	tessellator.addVertex(this.flagDesignerMinX + this.selectionMaxX * this.flagDesignerScale.x, this.flagDesignerMinY + this.selectionMinY * this.flagDesignerScale.y, this.zLevel);
		        	tessellator.addVertex(this.flagDesignerMinX + this.selectionMaxX * this.flagDesignerScale.x, this.flagDesignerMinY + this.selectionMaxY * this.flagDesignerScale.y, this.zLevel);
		        	tessellator.addVertex(this.flagDesignerMinX + this.selectionMinX * this.flagDesignerScale.x, this.flagDesignerMinY + this.selectionMaxY * this.flagDesignerScale.y, this.zLevel);
		        	tessellator.addVertex(this.flagDesignerMinX + this.selectionMinX * this.flagDesignerScale.x, this.flagDesignerMinY + this.selectionMinY * this.flagDesignerScale.y, this.zLevel);
	        		tessellator.draw();	
	        	}
	            
	        	int guiRight = this.width / 2 + this.width / 3;
	        	int guiBottom = this.height / 2 + this.height / 4;
		        float x1 = this.sliderColorR.xPosition;
		        float x2 = guiRight - 10;
		        float y1 = guiBottom - 10 - (x2 - x1);
		        float y2 = guiBottom - 10;
		        
		        tessellator.startDrawingQuads();
		        tessellator.setColorRGBA_F(0, 0, 0, 1.0F);
		        tessellator.addVertex((double)x2, (double)y1, (double)this.zLevel);
		        tessellator.addVertex((double)x1, (double)y1, (double)this.zLevel);
		        tessellator.addVertex((double)x1, (double)y2, (double)this.zLevel);
		        tessellator.addVertex((double)x2, (double)y2, (double)this.zLevel);
		        tessellator.draw();
		        
		        tessellator.startDrawingQuads();
		        tessellator.setColorRGBA_F(this.sliderColorR.getNormalizedValue(), this.sliderColorG.getNormalizedValue(), this.sliderColorB.getNormalizedValue(), 1.0F);
		        tessellator.addVertex((double)x2 - 1, (double)y1 + 1, (double)this.zLevel);
		        tessellator.addVertex((double)x1 + 1, (double)y1 + 1, (double)this.zLevel);
		        tessellator.addVertex((double)x1 + 1, (double)y2 - 1, (double)this.zLevel);
		        tessellator.addVertex((double)x2 - 1, (double)y2 - 1, (double)this.zLevel);
		        tessellator.draw();
		        
		        GL11.glShadeModel(GL11.GL_FLAT);
		        GL11.glDisable(GL11.GL_BLEND);
		        GL11.glEnable(GL11.GL_ALPHA_TEST);
		        GL11.glEnable(GL11.GL_TEXTURE_2D);

		        if (this.flagData.getHasFace())
		        {
		        	float oldZLevel = this.zLevel;
		        	this.zLevel += 1;
					ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;

					resourcelocation = AbstractClientPlayer.getLocationSkin(this.thePlayer.getGameProfile().getName());
					AbstractClientPlayer.getDownloadImageSkin(resourcelocation, this.thePlayer.getGameProfile().getName());

					FMLClientHandler.instance().getClient().renderEngine.bindTexture(resourcelocation);
					GL11.glColor3f(1.0F, 1.0F, 1.0F);
					float width = (float) (this.flagDesignerScale.x * 16);
					float height = (float) (this.flagDesignerScale.y * 16);
					float minX = this.flagDesignerMinX + this.flagDesignerWidth / 2.0F - width / 2;
					float minY = this.flagDesignerMinY + this.flagDesignerHeight / 2.0F - height / 2;
					float u = 32;
					float v = 64;
					float uW = 32F;
					float vH = 64F;
			        float f = 0.00390625F;
			        float f1 = 0.00390625F;
			        tessellator.startDrawingQuads();
			        tessellator.addVertexWithUV((double)(minX + 0), (double)(minY + height), (double)this.zLevel, (double)((float)(u + 0) * f), (double)((float)(v + vH) * f1));
			        tessellator.addVertexWithUV((double)(minX + width), (double)(minY + height), (double)this.zLevel, (double)((float)(u + uW) * f), (double)((float)(v + vH) * f1));
			        tessellator.addVertexWithUV((double)(minX + width), (double)(minY + 0), (double)this.zLevel, (double)((float)(u + uW) * f), (double)((float)(v + 0) * f1));
			        tessellator.addVertexWithUV((double)(minX + 0), (double)(minY + 0), (double)this.zLevel, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f1));
			        tessellator.draw();
					this.zLevel = oldZLevel;
		        }
	        	
	        	break;
	        }
		}
		
        super.drawScreen(par1, par2, par3);
    }
    
    private void drawFlagButton(int mouseX, int mouseY)
    {
		GL11.glPushMatrix();
        GL11.glTranslatef(this.buttonFlag_xPosition + this.buttonFlag_width / 2 - 22, this.buttonFlag_yPosition + this.buttonFlag_height / 2 + 22, -2.0F + zLevel);
        GL11.glScalef(28F, 28F, 28F);
        GL11.glTranslatef(0.0F, -0.215F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, -1F);
		this.dummyFlag.flagData = this.flagData;
		this.dummyModel.renderFlag(this.dummyFlag, 0.0625F);
		GL11.glColor3f(1, 1, 1);
		if (this.flagData.getHasFace())
		{
			this.dummyModel.renderFace(this.dummyFlag, 0.0625F, true);
		}
		GL11.glPopMatrix();

		GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 500.0F);
        this.drawCenteredString(this.fontRendererObj, "Customize Flag", this.buttonFlag_xPosition + this.buttonFlag_width / 2, this.buttonFlag_yPosition + this.buttonFlag_height / 2 - 5, GCCoreUtil.to32BitColor(255, 250, 250, 250));
        GL11.glPopMatrix();

        if (this.buttonFlag_hover)
        {
            this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, GCCoreUtil.to32BitColor(255, 50, 50, 50));
        }
        
        this.drawRect(this.buttonFlag_xPosition + buttonFlag_width - 1, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, GCCoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + 1, buttonFlag_yPosition + buttonFlag_height, GCCoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + 1, GCCoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, buttonFlag_yPosition + buttonFlag_height - 1, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, GCCoreUtil.to32BitColor(255, 0, 0, 0));
    }

    public void drawWorldBackground(int i)
    {
        if (this.mc.theWorld != null)
        {
        	int scaleX = Math.min(ticksPassed * 8, this.width / 3);
        	int scaleY = Math.min(ticksPassed * 8, this.height / 4);
        	
        	if (scaleX == this.width / 3 && scaleY == this.height / 4 && !this.initialized)
        	{
        		this.initialized = true;
        		this.initGui();
        	}
        	
            this.drawGradientRect(this.width / 2 - scaleX, this.height / 2 - scaleY, this.width / 2 + scaleX, this.height / 2 + scaleY, -1072689136, -804253680);
        }
        else
        {
            this.drawBackground(i);
        }
    }

	@Override
	public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
	{
		if (checkbox.equals(this.checkboxCompete))
		{
			this.optionCompete = newSelected;
		}
		else if (checkbox.equals(this.checkboxUploadScore))
		{
			this.optionUpload = newSelected;
		}
		else if (checkbox.equals(this.checkboxShowFace))
		{
			this.flagData.setHasFace(newSelected);
		}
		else if (checkbox.equals(this.checkboxEraser))
		{
			if (newSelected)
			{
				this.sliderEraserSize.visible = true;
				
				if (this.checkboxPaintbrush.isSelected)
				{
					this.sliderBrushSize.visible = false;
					this.checkboxPaintbrush.isSelected = false;
				}
				else if (this.checkboxSelector.isSelected)
				{
					this.checkboxSelector.isSelected = false;
				}
				else if (this.checkboxColorSelector.isSelected)
				{
					this.checkboxColorSelector.isSelected = false;
				}
			}
			else
			{
				this.sliderEraserSize.visible = false;
			}
		}
		else if (checkbox.equals(this.checkboxPaintbrush))
		{
			if (newSelected)
			{
				this.sliderBrushSize.visible = true;
				
				if (this.checkboxEraser.isSelected)
				{
					this.sliderEraserSize.visible = false;
					this.checkboxEraser.isSelected = false;
				}
				else if (this.checkboxSelector.isSelected)
				{
					this.checkboxSelector.isSelected = false;
				}
				else if (this.checkboxColorSelector.isSelected)
				{
					this.checkboxColorSelector.isSelected = false;
				}
			}
			else
			{
				this.sliderBrushSize.visible = false;
			}
		}
		else if (checkbox.equals(this.checkboxSelector))
		{
			if (newSelected)
			{
				if (this.checkboxEraser.isSelected)
				{
					this.sliderEraserSize.visible = false;
					this.checkboxEraser.isSelected = false;
				}
				else if (this.checkboxPaintbrush.isSelected)
				{
					this.sliderBrushSize.visible = false;
					this.checkboxPaintbrush.isSelected = false;
				}
				else if (this.checkboxColorSelector.isSelected)
				{
					this.checkboxColorSelector.isSelected = false;
				}
			}
		}
		else if (checkbox.equals(this.checkboxColorSelector))
		{
			if (newSelected)
			{
				if (this.checkboxEraser.isSelected)
				{
					this.sliderEraserSize.visible = false;
					this.checkboxEraser.isSelected = false;
				}
				else if (this.checkboxPaintbrush.isSelected)
				{
					this.sliderBrushSize.visible = false;
					this.checkboxPaintbrush.isSelected = false;
				}
				else if (this.checkboxSelector.isSelected)
				{
					this.checkboxSelector.isSelected = false;
				}
			}
		}
	}

	@Override
	public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean getInitiallySelected(GuiElementCheckbox checkbox)
	{
		if (checkbox.equals(this.checkboxCompete))
		{
			return this.optionCompete;
		}
		else if (checkbox.equals(this.checkboxUploadScore))
		{
			return this.optionUpload;
		}
		else if (checkbox.equals(this.checkboxShowFace))
		{
			return this.flagData.getHasFace();
		}
		else if (checkbox.equals(this.checkboxPaintbrush))
		{
			return true;
		}
		
		return false;
	}

	@Override
	public void onIntruderInteraction()
	{
		;
	}
}
