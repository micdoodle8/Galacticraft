package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.vector.Vector2;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementGradientButton;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GuiJoinSpaceRace extends GuiScreen implements ICheckBoxCallback, ITextBoxCallback
{
	protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");
	
    private int ticksPassed;
    private EntityPlayer thePlayer;
	private boolean initialized;
    
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
    
    private int selectionMinX;
    private int selectionMaxX;
    private int selectionMinY;
    private int selectionMaxY;

	private EntityFlag dummyFlag = new EntityFlag(FMLClientHandler.instance().getClient().theWorld);
	private ModelFlag dummyModel = new ModelFlag();
	
	private FlagData flagData;
	
	private boolean showGrid = false;
	private boolean lastMousePressed = false;
	private boolean hasExitedMain = false;
    
	public GuiJoinSpaceRace(GCEntityClientPlayerMP player)
	{
		this.thePlayer = player;
		
		SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(player.spaceRaceInviteTeamID);
		
		if (race != null)
		{
			this.flagData = race.getFlagData();
		}
		else
		{
			this.flagData = new FlagData(48, 32);
		}
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

			this.buttonFlag_width = 81;
			this.buttonFlag_height = 58;
			this.buttonFlag_xPosition = this.width / 2 - buttonFlag_width / 2;
			this.buttonFlag_yPosition = this.height / 2 - this.height / 3 + 10;
			
			this.buttonList.add(new GuiElementGradientButton(0, this.width / 2 - this.width / 3 + 15, this.height / 2 - this.height / 4 - 15, 50, 15, "Close"));
		}
	}
	
    protected void actionPerformed(GuiButton buttonClicked) 
    {
    	switch (buttonClicked.id)
    	{
    	case 0:
			thePlayer.closeScreen();
    		break;
		default:
			break;
    	}
    }

    protected void mouseClicked(int x, int y, int clickIndex)
    {
    	super.mouseClicked(x, y, clickIndex);
    }

    public void updateScreen()
    {
        super.updateScreen();
        ++this.ticksPassed;
        
        if (!this.initialized)
        {
        	return;
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
			this.drawCenteredString(this.fontRendererObj, "Join Space Race", this.width / 2, this.height / 2 - this.height / 3 - 15, 16777215);
        	this.drawFlagButton(par1, par2);
		}
		
        super.drawScreen(par1, par2, par3);
    }
    
    private void drawFlagButton(int mouseX, int mouseY)
    {
		GL11.glPushMatrix();
        GL11.glTranslatef(this.buttonFlag_xPosition + 2.9F, this.buttonFlag_yPosition + this.buttonFlag_height + 1 - 4, 0);
        GL11.glScalef(49.0F, 47.5F, 1F);
        GL11.glTranslatef(0.0F, 0.0F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, -1F);
		this.dummyFlag.flagData = this.flagData;
		this.dummyModel.renderFlag(this.dummyFlag, 0.0625F, this.ticksPassed);
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
    }

    public void drawWorldBackground(int i)
    {
        if (this.mc.theWorld != null)
        {
        	int scaleX = Math.min(ticksPassed * 14, this.width / 3);
        	int scaleY = Math.min(ticksPassed * 14, this.height / 3);
        	
        	if (scaleX == this.width / 3 && scaleY == this.height / 3 && !this.initialized)
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
		
	}

	@Override
	public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean getInitiallySelected(GuiElementCheckbox checkbox)
	{
		return false;
	}

	@Override
	public void onIntruderInteraction()
	{
		;
	}

	@Override
	public boolean canPlayerEdit(GuiElementTextBox textBox, EntityPlayer player) 
	{
		return true;
	}

	@Override
	public void onTextChanged(GuiElementTextBox textBox, String newText) 
	{
	}

	@Override
	public String getInitialText(GuiElementTextBox textBox) 
	{
		return "";
	}

	@Override
	public int getTextColor(GuiElementTextBox textBox) 
	{
		return GCCoreUtil.to32BitColor(255, 255, 255, 255);
	}
}
