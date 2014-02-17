package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

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
	private GuiCheckbox checkboxCompete;
	private GuiCheckbox checkboxUploadScore;
	private boolean initialized;
	private EnumSpaceRaceGui currentState = EnumSpaceRaceGui.MAIN;

    private int buttonBack_width;
    private int buttonBack_height;
    private int buttonBack_xPosition;
    private int buttonBack_yPosition;
    private boolean buttonBack_hover;
    
    private int buttonRules_width;
    private int buttonRules_height;
    private int buttonRules_xPosition;
    private int buttonRules_yPosition;
    private boolean buttonRules_hover;
    
    private int buttonDone_width;
    private int buttonDone_height;
    private int buttonDone_xPosition;
    private int buttonDone_yPosition;
    private boolean buttonDone_hover;
    
    private int buttonFlag_width;
    private int buttonFlag_height;
    private int buttonFlag_xPosition;
    private int buttonFlag_yPosition;
    private boolean buttonFlag_hover;
    
    private boolean optionCompete = true;
    private boolean optionUpload = true;

	private EntityFlag dummyFlag = new EntityFlag(FMLClientHandler.instance().getClient().theWorld);
	private ModelFlag dummyModel = new ModelFlag();
    
	public GuiNewSpaceRace(EntityPlayer player)
	{
		this.thePlayer = player;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		
		if (this.initialized)
		{
			final int var5 = (this.width - this.width / 4) / 2;
			final int var6 = (this.height - this.height / 4) / 2;
			
			this.buttonBack_xPosition = this.width / 2 - this.width / 3 + 15;
			this.buttonBack_yPosition = this.height / 2 - this.height / 4 - 15;
			this.buttonBack_width = 50;
			this.buttonBack_height = 15;

			this.buttonRules_width = 50;
			this.buttonRules_xPosition = this.width / 2 + this.width / 3 - 75 - buttonRules_width;
			this.buttonRules_yPosition = this.height / 2 + this.height / 4 - 25;
			this.buttonRules_height = 15;

			this.buttonDone_width = 50;
			this.buttonDone_xPosition = this.buttonRules_xPosition + buttonRules_width + 10;
			this.buttonDone_yPosition = this.height / 2 + this.height / 4 - 25;
			this.buttonDone_height = 15;

			this.buttonFlag_width = 78;
			this.buttonFlag_height = 31;
			this.buttonFlag_xPosition = this.width / 2 - buttonFlag_width / 2 + 50;
			this.buttonFlag_yPosition = this.height / 2 - buttonFlag_height / 2 + 3;
			
			switch (this.currentState)
			{
			case MAIN:
				this.checkboxCompete = new GuiCheckbox(0, this, this.width / 2 - 125, var6 - 10, "Compete against other players on this world?");
				this.checkboxUploadScore = new GuiCheckbox(1, this, this.width / 2 - 125, var6 + 5, "Upload score to Galacticraft website?");
				this.buttonList.add(this.checkboxCompete);
				this.buttonList.add(this.checkboxUploadScore);
				break;
			case RULES:
				break;
			case DESIGN_FLAG:
				break;
			default:
				break;
			}
		}
	}

    protected void mouseClicked(int x, int y, int clickIndex)
    {
    	super.mouseClicked(x, y, clickIndex);
    	
        if (clickIndex == 0)
        {
        	if (this.buttonBack_hover)
        	{
        		if (this.currentState == EnumSpaceRaceGui.MAIN)
        		{
        			this.thePlayer.closeScreen();
        		}
        		else
        		{
            		this.currentState = EnumSpaceRaceGui.MAIN;
            		this.initGui();
        		}
        	}
        	else if (this.buttonRules_hover)
        	{
        		this.currentState = EnumSpaceRaceGui.RULES;
        		this.initGui();
        	}
        	else if (this.buttonDone_hover)
        	{
        		this.thePlayer.closeScreen();
        	}
        	else if (this.buttonFlag_hover)
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
    }
    
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
		final int var5 = (this.width - this.width / 4) / 2;
		final int var6 = (this.height - this.height / 4) / 2;
		
		if (this.initialized)
		{
	        switch (this.currentState)
	        {
	        case MAIN:
				this.drawCenteredString(this.fontRendererObj, "New Space Race", this.width / 2, var6 - 25, 16777215);
				this.drawRulesButton(par1, par2);
	        	this.drawBackButton(par1, par2);
	        	this.drawDoneButton(par1, par2);
	        	this.drawFlagButton(par1, par2);
	        	
	    		String rememberStr = EnumColor.RED + "Remember: You're competing against time spent in-game before reaching space, not who gets to space first!";
	    		int trimWidth = this.width / 4 + 65;
	            List list = this.fontRendererObj.listFormattedStringToWidth(rememberStr, trimWidth);
	    		this.fontRendererObj.drawSplitString(rememberStr, this.width / 2 - this.width / 3 + 7, this.height / 2 + this.height / 4 - list.size() * this.fontRendererObj.FONT_HEIGHT - 5, trimWidth, CoreUtil.to32BitColor(255, 100, 100, 100));
	    		
	        	break;
	        case RULES:
				this.drawCenteredString(this.fontRendererObj, "Rules", this.width / 2, var6 - 25, 16777215);
	        	this.drawBackButton(par1, par2);
	        	break;
	        case DESIGN_FLAG:
				this.drawCenteredString(this.fontRendererObj, "Design New Flag", this.width / 2, var6 - 25, 16777215);
	        	this.drawBackButton(par1, par2);
	        	break;
	        }
		}
		
        super.drawScreen(par1, par2, par3);
    }
    
    private void drawFlagButton(int mouseX, int mouseY)
    {
		GL11.glPushMatrix();
        GL11.glTranslatef(this.buttonFlag_xPosition + this.buttonFlag_width / 2 - 22, this.buttonFlag_yPosition + this.buttonFlag_height / 2 + 22, -2.0F + zLevel);
        GL11.glScalef(35F, 35F, 35F);
        GL11.glTranslatef(0.0F, 0.0F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, -1F);
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(ItemRendererFlag.flagTextures[5]);
		this.dummyModel.renderFlag(this.dummyFlag, 0.0625F);
		this.dummyModel.renderFace(this.dummyFlag, 0.0625F, true);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 500.0F);
        this.drawCenteredString(this.fontRendererObj, EnumColor.ORANGE + "Customize Flag", this.buttonFlag_xPosition + this.buttonFlag_width / 2, this.buttonFlag_yPosition + this.buttonFlag_height / 2 - 5, CoreUtil.to32BitColor(255, 100, 100, 100));
        GL11.glPopMatrix();

		this.buttonFlag_hover = mouseX >= this.buttonFlag_xPosition && mouseY >= this.buttonFlag_yPosition && mouseX < this.buttonFlag_xPosition + this.buttonFlag_width && mouseY < this.buttonFlag_yPosition + this.buttonFlag_height;
		
        if (this.buttonFlag_hover)
        {
            this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 50, 50, 50));
        }
        
        this.drawRect(this.buttonFlag_xPosition + buttonFlag_width - 1, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + 1, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, this.buttonFlag_yPosition, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + 1, CoreUtil.to32BitColor(255, 0, 0, 0));
        this.drawRect(this.buttonFlag_xPosition, buttonFlag_yPosition + buttonFlag_height - 1, buttonFlag_xPosition + buttonFlag_width, buttonFlag_yPosition + buttonFlag_height, CoreUtil.to32BitColor(255, 0, 0, 0));
    }
    
    private void drawBackButton(int mouseX, int mouseY)
    {
		this.buttonBack_hover = mouseX >= this.buttonBack_xPosition && mouseY >= this.buttonBack_yPosition && mouseX < this.buttonBack_xPosition + this.buttonBack_width && mouseY < this.buttonBack_yPosition + this.buttonBack_height;
        this.drawGradientRect(this.buttonBack_xPosition, this.buttonBack_yPosition, this.buttonBack_xPosition + this.buttonBack_width, this.buttonBack_yPosition + this.buttonBack_height, buttonBack_hover ? CoreUtil.to32BitColor(150, 30, 30, 30) : CoreUtil.to32BitColor(150, 10, 10, 10), buttonBack_hover ? CoreUtil.to32BitColor(250, 30, 30, 30) : CoreUtil.to32BitColor(250, 10, 10, 10));
        this.drawCenteredString(this.fontRendererObj, this.currentState == EnumSpaceRaceGui.MAIN ? "Close" : "Back", this.buttonBack_xPosition + this.buttonBack_width / 2, this.buttonBack_yPosition + this.buttonBack_height / 4, CoreUtil.to32BitColor(255, 100, 100, 100));
    }
    
    private void drawRulesButton(int mouseX, int mouseY)
    {
		this.buttonRules_hover = mouseX >= this.buttonRules_xPosition && mouseY >= this.buttonRules_yPosition && mouseX < this.buttonRules_xPosition + this.buttonRules_width && mouseY < this.buttonRules_yPosition + this.buttonRules_height;
        this.drawGradientRect(this.buttonRules_xPosition, this.buttonRules_yPosition, this.buttonRules_xPosition + this.buttonRules_width, this.buttonRules_yPosition + this.buttonRules_height, buttonRules_hover ? CoreUtil.to32BitColor(150, 30, 30, 30) : CoreUtil.to32BitColor(150, 10, 10, 10), buttonRules_hover ? CoreUtil.to32BitColor(250, 30, 30, 30) : CoreUtil.to32BitColor(250, 10, 10, 10));
        this.drawCenteredString(this.fontRendererObj, "Rules", this.buttonRules_xPosition + this.buttonRules_width / 2, this.buttonRules_yPosition + this.buttonRules_height / 4, CoreUtil.to32BitColor(255, 100, 100, 100));
    }
    
    private void drawDoneButton(int mouseX, int mouseY)
    {
		this.buttonDone_hover = mouseX >= this.buttonDone_xPosition && mouseY >= this.buttonDone_yPosition && mouseX < this.buttonDone_xPosition + this.buttonDone_width && mouseY < this.buttonDone_yPosition + this.buttonDone_height;
        this.drawGradientRect(this.buttonDone_xPosition, this.buttonDone_yPosition, this.buttonDone_xPosition + this.buttonDone_width, this.buttonDone_yPosition + this.buttonDone_height, buttonDone_hover ? CoreUtil.to32BitColor(150, 30, 30, 30) : CoreUtil.to32BitColor(150, 10, 10, 10), buttonDone_hover ? CoreUtil.to32BitColor(250, 30, 30, 30) : CoreUtil.to32BitColor(250, 10, 10, 10));
        this.drawCenteredString(this.fontRendererObj, "GO!", this.buttonDone_xPosition + this.buttonDone_width / 2, this.buttonDone_yPosition + this.buttonDone_height / 4, CoreUtil.to32BitColor(255, 100, 100, 100));
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
		
		return false;
	}

	@Override
	public void onIntruderInteraction()
	{
		;
	}
}
