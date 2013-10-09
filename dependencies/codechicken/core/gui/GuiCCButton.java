package codechicken.core.gui;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GuiCCButton extends GuiWidget
{
    public String text;
    public String actionCommand;
    private boolean isEnabled = true;
    public boolean drawButton = true;
    
    public GuiCCButton(int x, int y, int width, int height, String text)
    {
        super(x, y, width, height);
        this.text = text;
    }
    
    public void setText(String s)
    {
        text = s;
    }
    
    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean b)
    {
        isEnabled = b;
    }
    
    @Override
    public void mouseClicked(int x, int y, int button)
    {
        if(isEnabled && pointInside(x, y) && actionCommand != null)
        {
            sendAction(actionCommand, button);
            Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1, 1);
        }
    }
    
    @Override
    public void draw(int mousex, int mousey, float frame)
    {
        if(!drawButton)
            return;
        
        renderEngine.bindTexture(guiTex);
        GL11.glColor4f(1, 1, 1, 1);
        boolean mouseover = pointInside(mousex, mousey);
        int state = !isEnabled ? 0 : mouseover ? 2 : 1;
        drawTexturedModalRect(x, y, 0, 46 + state * 20, width / 2, height / 2);//top left
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46 + state * 20, width / 2, height / 2);//top right
        drawTexturedModalRect(x, y + height / 2, 0, 46 + state * 20 + 20 - height / 2, width / 2, height / 2);//bottom left
        drawTexturedModalRect(x + width / 2, y + height / 2, 200 - width / 2, 46 + state * 20 + 20 - height / 2, width / 2, height / 2);//bottom right
        
        drawCenteredString(fontRenderer, text, x + width / 2, y + (height - 8) / 2, getTextColour(mousex, mousey));
    }
    
    public int getTextColour(int mousex, int mousey)
    {
        return !isEnabled ? 0xFFA0A0A0 : pointInside(mousex, mousey) ? 0xFFFFFFA0 : 0xFFE0E0E0;
    }

    public GuiCCButton setActionCommand(String string)
    {
        actionCommand = string;
        return this;
    }
}
