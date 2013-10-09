package codechicken.nei.config;

import static codechicken.core.gui.GuiDraw.drawString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.LayoutManager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public abstract class OptionStringSet extends Option
{
    public LinkedList<String> options = new LinkedList<String>();
    public Multimap<String, String> dependants = ArrayListMultimap.create();
    public Map<String, String> dependancies = new HashMap<String, String>();
    public Multimap<String, String> groups = ArrayListMultimap.create();

    public OptionStringSet(String name)
    {
        super(name);
    }

    public void addDep(String base, String dep)
    {
        dependants.put(dep, base);
        dependancies.put(base, dep);
    }
    
    @Override
    public void draw(int mousex, int mousey, float frame)
    {
        drawPrefix();
        drawButtons();
        drawIcons();
    }
    
    public void drawPrefix()
    {
        drawString(translateN(name), 10, 8, -1);
    }

    public void drawButtons()
    {
        int x = buttonX();
        List<String> values = values();
        for(int i = 0; i < options.size(); i++)
        {
            LayoutManager.drawButtonBackground(x, 2, 20, 20, true, values.contains(options.get(i)) ? 1 : 0);
            x+=24;
        }
    }

    public abstract void drawIcons();
    
    @Override
    public List<String> handleTooltip(int mousex, int mousey, List<String> currenttip)
    {
        if(new Rectangle4i(4, 4, 50, 20).contains(mousex, mousey))
            currenttip.add(translateN(name+".tip"));
        int x = buttonX();
        for(int i = 0; i < options.size(); i++)
        {
            if(new Rectangle4i(x, 2, 20, 20).contains(mousex, mousey))
                currenttip.add(translateN(name+"."+options.get(i)));
            
            x+=24;
        }
        return currenttip;
    }
    
    @Override
    public void mouseClicked(int x, int y, int button)
    {
        if(renderDefault())
            return;
        
        if(clickButton(x, y, button))
            Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1, 1);
    }
    
    public boolean clickButton(int mousex, int mousey, int button)
    {
        int x = buttonX();
        List<String> values = values();
        for(int i = 0; i < options.size(); i++)
        {
            if(new Rectangle4i(x, 2, 20, 20).contains(mousex, mousey))
            {
                String s = options.get(i);
                boolean set = values.contains(s);
                if(button == 0 && !set)
                {
                    setValue(s);
                    return true;
                }
                if(button == 1 && set)
                {
                    remValue(s);
                    return true;
                }
                return false;
            }
            x+=24;
        }
        return false;
    }

    public void setValue(String s)
    {
        if(values().contains(s))
            return;
        
        String dep = dependancies.get(s);
        if(dep != null)
            setValue(dep);

        if(groups.containsKey(s))
        {
            for(String grp : groups.get(s))
                setValue(grp);
        }
        else
        {
            List<String> setUtils = new LinkedList<String>(values());
            setUtils.add(s);
            setValues(setUtils);
        }
    }

    public void remValue(String s)
    {
        for(String dep : dependants.get(s))
            remValue(dep);
        
        if(groups.containsKey(s))
        {
            for(String grp : groups.get(s))
                remValue(grp);
        }
        else
        {
            List<String> values = new LinkedList<String>(values());
            values.remove(s);
            setValues(values);
        }
    }

    public void setValues(List<String> values)
    {
        StringBuilder sb = new StringBuilder();
        for(String s : values)
        {
            if(sb.length() > 0)
                sb.append(", ");
            sb.append(s);
        }
        getTag().setValue(sb.toString());
    }

    public List<String> values()
    {
        return Arrays.asList(renderTag().getValue().replace(" ", "").split(","));
    }
    
    public int buttonX()
    {
        return slot.contentWidth()-(24*options.size()-4);
    }
}
