package codechicken.nei.config;

import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.LayoutManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static codechicken.lib.gui.GuiDraw.*;

public abstract class OptionButton extends Option
{
    protected static ResourceLocation guiTex = new ResourceLocation("textures/gui/widgets.png");
    
    public final String prefix;
    public final String text;
    public final String tooltip;
    private boolean isEnabled = true;
    
    public OptionButton(String name, String prefix, String text, String tooltip)
    {
        super(name);
        this.prefix = prefix;
        this.text = text;
        this.tooltip = tooltip;
    }
    
    public OptionButton(String prefix, String text, String tooltip)
    {
        this(text, prefix, text, tooltip);
    }
    
    public OptionButton(String name)
    {
        this(null, name, name+".tip");
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
    public void draw(int mousex, int mousey, float frame)
    {
        changeTexture(guiTex);
        GL11.glColor4f(1, 1, 1, 1);
        drawPrefix();
        drawButton(mousex, mousey);
    }

    public Rectangle4i buttonSize()
    {
        int width;
        int x;
        if(getPrefix() == null)
        {
            width = slot.contentWidth();
            x = (slot.contentWidth()-width)/2;
        }
        else
        {
            width = Math.max(60, getStringWidth(getButtonText()));
            x = slot.contentWidth()-width;
        }
        return new Rectangle4i(x, 2, width, 20);
    }

    public String getPrefix()
    {
        if(prefix == null) return null;
        String s = translateN(prefix);
        if(s.equals(namespaced(prefix))) return null;
        return s;
    }
    
    public String getButtonText()
    {
        return translateN(name);
    }

    public String getTooltip()
    {
        if(tooltip == null) return null;
        String s = translateN(tooltip);
        if(s.equals(namespaced(tooltip))) return null;
        return s;
    }
    
    public void drawPrefix()
    {
        if(getPrefix() != null)
            drawString(getPrefix(), 10, 8, -1);
    }
    
    public void drawButton(int mousex, int mousey)
    {
        Rectangle4i b = buttonSize();
        LayoutManager.drawButtonBackground(b.x, b.y, b.w, b.h, true, getButtonTex(mousex, mousey));
        drawStringC(getButtonText(), b.x, b.y, b.w, b.h, getTextColour(mousex, mousey));
    }
    
    public int getButtonTex(int mousex, int mousey)
    {
        return !isEnabled() ? 0 : pointInside(mousex, mousey) ? 2 : 1;
    }

    public int getTextColour(int mousex, int mousey)
    {
        return !isEnabled() ? 0xFFA0A0A0 : pointInside(mousex, mousey) ? 0xFFFFFFA0 : 0xFFE0E0E0;
    }

    public boolean pointInside(int mousex, int mousey)
    {
        return buttonSize().contains(mousex, mousey);
    }

    @Override
    public void mouseClicked(int x, int y, int button)
    {
        if(pointInside(x, y))
            if(onClick(button))
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }
    
    public boolean onClick(int button)
    {
        return false;
    }
    
    @Override
    public List<String> handleTooltip(int mousex, int mousey, List<String> currenttip)
    {
        if(new Rectangle4i(0, 2, slot.contentWidth(), 20).contains(mousex, mousey) && getTooltip() != null)
            currenttip.add(getTooltip());
        return currenttip;
    }
}
