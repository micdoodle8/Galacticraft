package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector2;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiGradientButton;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiSlider;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererFlag;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
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
import cpw.mods.fml.common.FMLLog;

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
	private GuiCheckbox checkboxCompete;
	private GuiCheckbox checkboxUploadScore;
	private GuiCheckbox checkboxShowFace;
	private boolean initialized;
	private GuiSlider sliderColorR;
	private GuiSlider sliderColorG;
	private GuiSlider sliderColorB;
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

	private EntityFlag dummyFlag = new EntityFlag(FMLClientHandler.instance().getClient().theWorld);
	private ModelFlag dummyModel = new ModelFlag();
	
	private FlagData flagData = new FlagData(48, 32);
    
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
			FMLLog.info("" + sliderR + " " + sliderG + " " + sliderB);
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
			
			this.buttonList.add(new GuiGradientButton(0, this.width / 2 - this.width / 3 + 15, this.height / 2 - this.height / 4 - 15, 50, 15, this.currentState == EnumSpaceRaceGui.MAIN ? "Close" : "Back"));
			
			switch (this.currentState)
			{
			case MAIN:
				this.checkboxCompete = new GuiCheckbox(1, this, this.width / 2 - 125, var6 - 10, "Compete against other players on this world?", CoreUtil.to32BitColor(255, 240, 240, 240));
				this.checkboxUploadScore = new GuiCheckbox(2, this, this.width / 2 - 125, var6 + 5, "Upload score to Galacticraft website?", CoreUtil.to32BitColor(255, 240, 240, 240));
				this.buttonList.add(new GuiGradientButton(3, this.width / 2 + this.width / 3 - 125, this.height / 2 + this.height / 4 - 25, 50, 15, "Rules"));
				this.buttonList.add(new GuiGradientButton(4, this.width / 2 + this.width / 3 - 65, this.height / 2 + this.height / 4 - 25, 50, 15, "Done"));
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
				this.sliderColorR = new GuiSlider(1, flagDesignerRight + 10, guiTop + 10, availWidth, height, true, new Vector3(0, 0, 0), new Vector3(1, 0, 0));
				this.sliderColorG = new GuiSlider(2, flagDesignerRight + 11 + availWidth, guiTop + 10, availWidth, height, true, new Vector3(0, 0, 0), new Vector3(0, 1, 0));
				this.sliderColorB = new GuiSlider(3, flagDesignerRight + 12 + availWidth * 2, guiTop + 10, availWidth, height, true, new Vector3(0, 0, 0), new Vector3(0, 0, 1));
				this.checkboxShowFace = new GuiCheckbox(4, this, this.width / 2 - this.width / 3 + 2, this.height / 2 + this.height / 4 - 16, "Show Face");
				this.sliderColorR.setSliderPos(sliderR);
				this.sliderColorG.setSliderPos(sliderG);
				this.sliderColorB.setSliderPos(sliderB);
				this.buttonList.add(this.sliderColorR);
				this.buttonList.add(this.sliderColorG);
				this.buttonList.add(this.sliderColorB);
				this.buttonList.add(this.checkboxShowFace);
				
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

        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        
        if (Mouse.isButtonDown(0) && x >= this.flagDesignerMinX && y >= this.flagDesignerMinY && x <= this.flagDesignerMinX + this.flagDesignerWidth && y <= this.flagDesignerMinY + this.flagDesignerHeight)
    	{
    		int unScaledX = (int)Math.floor((x - this.flagDesignerMinX) / this.flagDesignerScale.x);
    		int unScaledY = (int)Math.floor((y - this.flagDesignerMinY) / this.flagDesignerScale.y);
    		this.flagData.setColorAt(unScaledX, unScaledY, new Vector3(this.sliderColorR.getNormalizedValue() * 256, this.sliderColorG.getNormalizedValue() * 256, this.sliderColorB.getNormalizedValue() * 256));
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
	    		this.fontRendererObj.drawSplitString(rememberStr, this.width / 2 - this.width / 3 + 7, this.height / 2 + this.height / 4 - list2.size() * this.fontRendererObj.FONT_HEIGHT - 5, trimWidth, CoreUtil.to32BitColor(255, 100, 100, 100));
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
	            
		        tessellator.startDrawingQuads();
	        	int guiRight = this.width / 2 + this.width / 3;
	        	int guiBottom = this.height / 2 + this.height / 4;
		        float x1 = this.sliderColorR.xPosition;
		        float x2 = guiRight - 10;
		        float y1 = guiBottom - 10 - (x2 - x1);
		        float y2 = guiBottom - 10;
		        tessellator.setColorRGBA_F(this.sliderColorR.getNormalizedValue(), this.sliderColorG.getNormalizedValue(), this.sliderColorB.getNormalizedValue(), 1.0F);
		        tessellator.addVertex((double)x2, (double)y1, (double)this.zLevel);
		        tessellator.addVertex((double)x1, (double)y1, (double)this.zLevel);
		        tessellator.addVertex((double)x1, (double)y2, (double)this.zLevel);
		        tessellator.addVertex((double)x2, (double)y2, (double)this.zLevel);
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
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(ItemRendererFlag.flagTextures[5]);
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
        this.drawCenteredString(this.fontRendererObj, "Customize Flag", this.buttonFlag_xPosition + this.buttonFlag_width / 2, this.buttonFlag_yPosition + this.buttonFlag_height / 2 - 5, CoreUtil.to32BitColor(255, 250, 250, 250));
        GL11.glPopMatrix();

        if (this.buttonFlag_hover)
        {
            this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 50, 50, 50));
        }
        
        this.drawRect(this.buttonFlag_xPosition + buttonFlag_width - 1, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + 1, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + 1, CoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, buttonFlag_yPosition + buttonFlag_height - 1, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 0, 0, 0));
    }

    public void drawWorldBackground(int i)
    {
        if (this.mc.theWorld != null)
        {
        	int scaleX = Math.min(ticksPassed * 4, this.width / 3);
        	int scaleY = Math.min(ticksPassed * 4, this.height / 4);
        	
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
	public void onSelectionChanged(GuiCheckbox checkbox, boolean newSelected)
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
	}

	@Override
	public boolean canPlayerEdit(GuiCheckbox checkbox, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean getInitiallySelected(GuiCheckbox checkbox)
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
		
		return false;
	}

	@Override
	public void onIntruderInteraction()
	{
		;
	}
}
