package codechicken.core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class GuiWidget extends Gui
{
    protected static final ResourceLocation guiTex = new ResourceLocation("textures/gui/widgets.png");
    
    public GuiScreen parentScreen;
    public TextureManager renderEngine;
    public FontRenderer fontRenderer;
    
    public int x;
    public int y;
    public int width;
    public int height;
    
    public GuiWidget(int x, int y, int width, int height)
    {
        setSize(x, y, width, height);
    }
    
    public void setSize(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean pointInside(int px, int py)
    {
        return px >= x && px < x + width && py >= y && py < y + height;
    }
    
    public void sendAction(String actionCommand, Object... params)
    {
        sendAction(parentScreen, actionCommand, params);
    }

    public static void sendAction(GuiScreen screen, String actionCommand, Object... params)
    {
        if(actionCommand != null && screen instanceof IGuiActionListener)
            ((IGuiActionListener) screen).actionPerformed(actionCommand, params);
    }

    public void mouseClicked(int x, int y, int button)
    {
    }

    public void mouseMovedOrUp(int x, int y, int button)
    {
    }
    
    public void mouseDragged(int x, int y, int button, long time)
    {
    }
    
    public void update()
    {
    }

    public void draw(int mousex, int mousey, float frame)
    {
    }

    public void keyTyped(char c, int keycode)
    {
    }

    public void mouseScrolled(int x, int y, int scroll)
    {
    }
    
    public void onAdded(GuiScreen s)
    {
        Minecraft mc = Minecraft.getMinecraft();
        parentScreen = s;
        renderEngine = mc.renderEngine;
        fontRenderer = mc.fontRenderer;
    }
}
