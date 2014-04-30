package codechicken.nei.config;

import codechicken.nei.config.GuiOptionList.OptionScrollSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class OptionList extends OptionButton
{
    public static final OptionList top = new OptionList(null);
    
    public static OptionList getOptionList(String fullName)
    {
        Option o = top.getOption(fullName);
        if(o == null)
            top.addOption(o = new OptionList(fullName));
        return (OptionList) o;
    }

    public static String parent(String fullName)
    {
        int i = fullName.indexOf('.');
        if(i < 0)
            return fullName;
        return fullName.substring(0, i);
    }
    
    public static String child(String fullName)
    {
        int i = fullName.indexOf('.');
        return fullName.substring(i+1);
    }
    
    public ArrayList<Option> optionList = new ArrayList<Option>();
    public HashMap<String, Option> options = new HashMap<String, Option>();
    public IConfigSetHolder config;
    
    public OptionList(String name)
    {
        super(name);
    }
    
    public OptionList bindConfig(IConfigSetHolder config)
    {
        this.config = config;
        return this;
    }
    
    private OptionList subList(String fullName)
    {
        OptionList o = (OptionList) getOption(fullName);
        if(o == null)
            addOption(o = new OptionList(fullName));
        
        return o;
    }

    public Option getOption(String fullName)
    {
        if(fullName.contains("."))
            return subList(parent(fullName)).getOption(child(fullName));
        
        return options.get(fullName);
    }
    
    public void addOption(Option o)
    {
        o.namespace = fullName();
        addOption(o, o.fullName(), o.name);
    }

    private void addOption(Option o, String fullName, String subName)
    {
        if(subName.contains("."))
        {
            subList(parent(subName)).addOption(o, fullName, child(subName));
            return;
        }
        
        if(options.containsKey(subName))
            System.err.println("Warning, replacing option: "+fullName);
        
        options.put(subName, o);
        addSorted(o);
        o.onAdded(this);
    }

    public void addSorted(Option o)
    {
        optionList.add(o);
    }
    
    public void expandOptionList(OptionList replacement)
    {
        addOption(replacement);
        
        OptionList base = (OptionList) getOption(replacement.name);
        replacement.options = base.options;
        replacement.optionList = base.optionList;
        options.remove(base.name);
        optionList.remove(base);
    }

    public void showGui(GuiScreen parent)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiOptionList(parent, this, false));
    }
    
    public void synthesizeEnvironment()
    {
        new GuiOptionList(null, this, false).addWidgets();
    }
    
    @Override
    public boolean onClick(int button)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiOptionList(slot.getGui(), OptionList.this, slot.getGui().worldConfig()));
        return true;
    }
    
    @Override
    public boolean showWorldSelector()
    {
        return false;
    }
    
    @Override
    public void onAdded(OptionScrollSlot slot)
    {
        super.onAdded(slot);
        
        globalConfigSet().config.getTag(configName()).useBraces();
        worldConfigSet().config.getTag(configName()).useBraces();
    }
    
    @Override
    public ConfigSet worldConfigSet()
    {
        return config != null ? config.worldConfigSet() : super.worldConfigSet();
    }
    
    @Override
    public ConfigSet globalConfigSet()
    {
        return config != null ? config.globalConfigSet() : super.globalConfigSet();
    }
    
    @Override
    public OptionList configBase()
    {
        return config != null ? this : super.configBase();
    }
}
