package codechicken.nei;

import java.util.List;

import static codechicken.core.gui.GuiDraw.*;

public abstract class Button extends Widget
{
    public Button(String s)
    {
        label = s;
    }
    
    public Button()
    {
        label = "";
    }

    public int contentWidth()
    {
        return getRenderIcon() == null ? getStringWidth(label) : getRenderIcon().width;
    }

    @Override
    public void draw(int mousex, int mousey)
    {
        LayoutManager.getLayoutStyle().drawButton(this, mousex, mousey);
    }

    @Override
    public boolean handleClick(int i, int j, int k)
    {
        if(k == 1 || k == 0)
            if(onButtonPress(k == 1))
                NEIClientUtils.mc().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        return true;
    }
    
    public abstract boolean onButtonPress(boolean rightclick);

    public Image getRenderIcon()
    {
        return icon;
    }
    
    @Override
    public List<String> handleTooltip(int mx, int my, List<String> tooltip)
    {
        if(!contains(mx, my))
            return tooltip;
        
        String tip = getButtonTip();
        if(tip != null)
            tooltip.add(tip);
        return tooltip;
    }
    
    public String getButtonTip()
    {
        return null;
    }
    
    public String getRenderLabel()
    {
        return label;
    }

    public String label;
    public Image icon;
    
    /**
     * 0x4 = state flag, as opposed to 1 click
     * 0 = normal
     * 1 = on
     * 2 = disabled
     */
    public int state;
}
