package codechicken.nei.config;

import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.TextField;
import codechicken.nei.config.GuiOptionList.OptionScrollSlot;

import java.util.List;

import static codechicken.lib.gui.GuiDraw.drawString;
import static codechicken.lib.gui.GuiDraw.getStringWidth;

public class OptionTextField extends Option
{
    private boolean focused = false;
    private TextField textField = new TextField("")
    {
        @Override
        public void onTextChange(String oldText)
        {
            if(!text().equals(renderTag().getValue()))
                getTag().setValue(text());
        }

        @Override
        public boolean focused()
        {
            return focused;
        }

        @Override
        public void setFocus(boolean focus)
        {
            if(focus == true && renderDefault())
                return;
            
            focused = focus;
        }
    };
    
    public OptionTextField(String name)
    {
        super(name);
        textField.height = 20;
        textField.y = 2;
    }
    
    @Override
    public void onAdded(OptionScrollSlot slot)
    {
        super.onAdded(slot);
    }
    
    @Override
    public void update()
    {
        textField.setText(renderText());
    }
    
    public String getPrefix()
    {
        return translateN(name);
    }
    
    @Override
    public void draw(int mousex, int mousey, float frame)
    {
        drawString(getPrefix(), 10, 8, -1);
        
        textField.setText(renderText());
        textField.width = slot.contentWidth()-getStringWidth(getPrefix())-16;
        textField.x = slot.contentWidth()-textField.width;
        textField.draw(mousex, mousey);
    }
    
    public String renderText()
    {
        return renderTag().getValue();
    }
    
    @Override
    public void keyTyped(char c, int keycode)
    {
        textField.handleKeyPress(keycode, c);
    }
    
    @Override
    public void mouseClicked(int mousex, int mousey, int button)
    {
        if(textField.contains(mousex, mousey))
            textField.handleClick(mousex, mousey, button);
    }
    
    @Override
    public void onMouseClicked(int mousex, int mousey, int button)
    {
        textField.onGuiClick(mousex, mousey);
    }
    
    @Override
    public List<String> handleTooltip(int mousex, int mousey, List<String> currenttip)
    {
        if(new Rectangle4i(10, 2, textField.x-10, 20).contains(mousex, mousey))
        {
            String tip = translateN(name+".tip");
            if(!tip.equals(name+".tip"))
                currenttip.add(tip);
        }
        return currenttip;
    }
}
