package codechicken.nei.config;

import codechicken.nei.Image;
import codechicken.nei.LayoutManager;

public class OptionGamemodes extends OptionStringSet
{
    public OptionGamemodes(String name)
    {
        super(name);

        options.add("creative");
        options.add("creative+");
        options.add("adventure");
    }
    
    @Override
    public void drawIcons()
    {
        int x = buttonX();
        LayoutManager.drawIcon(x+4, 6, new Image(132, 12, 12, 12)); x+=24;
        LayoutManager.drawIcon(x+4, 6, new Image(156, 12, 12, 12)); x+=24;
        LayoutManager.drawIcon(x+4, 6, new Image(168, 12, 12, 12)); x+=24;
    }
}
