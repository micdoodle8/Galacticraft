package codechicken.nei.config;

import codechicken.nei.api.GuiInfo;

public class OptionHighlightTips extends OptionButton
{
    public OptionHighlightTips(String name)
    {
        super(name, null, name, null);
    }
    
    @Override
    public void copyGlobals()
    {
        copyGlobal(name);
        copyGlobal(name+".x");
        copyGlobal(name+".y");
    }
    
    @Override
    public boolean onClick(int button)
    {
        if(renderDefault())
            return false;
        
        GuiInfo.switchGui(new GuiHighlightTips(this));
        return true;
    }
}
