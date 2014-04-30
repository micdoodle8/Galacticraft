package codechicken.nei.config;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class OptionKeyBind extends OptionButton
{
    private boolean hasFocus = false;
    
    public OptionKeyBind(String name)
    {
        super("keys."+name);
    }
    
    @Override
    public void onMouseClicked(int mousex, int mousey, int button)
    {
        hasFocus = false;
    }
    
    @Override
    public void keyTyped(char c, int keycode)
    {
        if(hasFocus)
        {
            setValue(keycode);
            hasFocus = false;
        }
    }

    @Override
    public boolean onClick(int button)
    {
        if(renderDefault())
            return false;
        
        if(button == 0)
        {
            hasFocus = true;
            return true;
        }
        if(button == 1 && getValue() != 0)
        {
            setValue(0);
            return true;
        }
        return false;
    }
    
    public boolean conflicted()
    {
        if(getValue() == 0)
            return false;
        
        for(Option o : slot.options)
            if(o instanceof OptionKeyBind && o != this && ((OptionKeyBind)o).getValue() == getValue())
                return true;
        
        return false;
    }
    
    public void setValue(int keycode)
    {
        getTag().setIntValue(keycode);
    }
    
    public int getValue()
    {
        return renderTag().getIntValue();
    }
    
    @Override
    public String getPrefix()
    {
        return translateN(name);
    }
    
    @Override
    public String getButtonText()
    {
        if(hasFocus)
            return EnumChatFormatting.WHITE+"> "+EnumChatFormatting.YELLOW+"??? "+EnumChatFormatting.WHITE+"<";
        
        if(conflicted())
            return EnumChatFormatting.RED + Keyboard.getKeyName(getValue());
        
        return Keyboard.getKeyName(getValue());
    }
    
    @Override
    public int getTextColour(int mousex, int mousey)
    {
        return -1;
    }
}
