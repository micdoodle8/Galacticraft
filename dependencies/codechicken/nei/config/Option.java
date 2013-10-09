package codechicken.nei.config;

import static codechicken.lib.lang.LangUtil.translateG;

import java.util.List;

import codechicken.lib.config.ConfigTag;
import codechicken.nei.config.GuiOptionList.OptionScrollSlot;

public abstract class Option
{
    public OptionScrollSlot slot;
    
    public String namespace = null;
    public final String name;
    public OptionList parent;
    
    public Option(String name)
    {
        this.name = name;
    }
    
    public String fullName()
    {
        return namespaced(name);
    }
    
    public String configName()
    {
        return fullName().substring(configBase().fullName().length()+1);
    }
    
    public String namespaced(String name)
    {
        return namespace == null ? name : (namespace+"."+name);
    }
    
    public String translateN(String s, Object... args)
    {
        return translateG(namespaced(s), args);
    }
    
    public ConfigSet globalConfigSet()
    {
        return parent.globalConfigSet();
    }
    
    public ConfigSet worldConfigSet()
    {
        return parent.worldConfigSet();
    }
    
    public OptionList configBase()
    {
        return parent.configBase();
    }
    
    public boolean worldSpecific()
    {
        return worldSpecific(configName());
    }
    
    public boolean worldSpecific(String s)
    {
        return worldConfigSet().config.containsTag(s);
    }
    
    public ConfigSet configSet()
    {
        return worldConfig() ? worldConfigSet() : globalConfigSet();
    }
    
    public ConfigTag renderTag()
    {
        return renderTag(configName());
    }
    
    public ConfigTag renderTag(String s)
    {
        return (worldConfig() && worldSpecific(s) ? worldConfigSet() : globalConfigSet()).config.getTag(s);
    }
    
    public ConfigTag getTag()
    {
        return getTag(configName());
    }
    
    public ConfigTag getTag(String s)
    {
        return configSet().config.getTag(s);
    }

    public boolean worldConfig()
    {
        return slot.getGui().worldConfig();
    }
    
    public boolean renderDefault()
    {
        return renderDefault(configName());
    }
    
    public boolean renderDefault(String s)
    {
        return worldConfig() && !worldSpecific(s);
    }
    
    public void useGlobal()
    {
        useGlobal(configName());
    }
    
    public void useGlobal(String s)
    {
        if(worldConfig())
            worldConfigSet().config.removeTag(s);
    }
    
    public void copyGlobal()
    {
        copyGlobal(configName());
    }
    
    public void copyGlobal(String s)
    {
        if(worldConfig())
            worldConfigSet().config.getTag(s).setValue(globalConfigSet().config.getTag(s).getValue());
    }
    
    public void onAdded(OptionScrollSlot slot)
    {
        this.slot = slot;
    }
    
    public void onAdded(OptionList list)
    {
        parent = list;
    }
    
    public void onMouseClicked(int mousex, int mousey, int button)
    {
    }
    
    public void mouseClicked(int mousex, int mousey, int button)
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

    public List<String> handleTooltip(int mousex, int mousey, List<String> currenttip)
    {
        return currenttip;
    }
    
    public boolean showWorldSelector()
    {
        return true;
    }
    
    public void copyGlobals()
    {
        copyGlobal();
    }
    
    public void useGlobals()
    {
        useGlobal();
    }
    
    public boolean hasWorldOverride()
    {
        return worldSpecific();
    }
}
