package codechicken.lib.config;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigTag extends ConfigTagParent
{    
    public ConfigTag(ConfigTagParent parent, String name)
    {
        this.parent = parent;
        this.name = name;
        qualifiedname = parent.getNameQualifier()+name;
        newline = parent.newlinemode == 2;
        parent.addChild(this);
    }
    
    @Override
    public String getNameQualifier()
    {
        return qualifiedname+".";
    }
    
    @Override
    public void saveConfig()
    {
        parent.saveConfig();
    }
    
    /**
     * Called when the tag is loaded from a config file as opposed to constructed by a mod
     * @return this
     */
    public ConfigTag onLoaded()
    {
        return this;
    }
    
    public void setValue(String value)
    {
        this.value = value;
        saveConfig();
    }
    
    public void setDefaultValue(String defaultvalue)
    {
        if(value == null)
        {
            value = defaultvalue;
            saveConfig();
        }
    }
    
    public void setIntValue(int i)
    {
        setValue(Integer.toString(i));
    }
    
    public void setBooleanValue(boolean b)
    {
        setValue(Boolean.toString(b));
    }
    
    public void setHexValue(int i)
    {
        setValue("0x"+Long.toString(((long)i) << 32 >>> 32, 16));
    }
    
    public void setColourRGB(Colour c)
    {
        String s = Long.toString(((long)c.rgb()) << 32 >>> 32, 16);
        while(s.length() < 6)
            s = "0"+s;
        setValue("0x"+s.toUpperCase());
    }
    
    public String getValue()
    {
        return value;
    }
    
    public String getValue(String defaultvalue)
    {
        setDefaultValue(defaultvalue);
        return value;
    }
    
    public int getIntValue()
    {
        return Integer.parseInt(getValue());
    }
    
    public int getIntValue(int defaultvalue)
    {
        if(value == null)
        {
            setIntValue(defaultvalue);
        }
        try
        {
            return getIntValue();
        }
        catch(NumberFormatException nfe)
        {
            setIntValue(defaultvalue);
            return getIntValue();
        }
    }
    
    public boolean getBooleanValue()
    {
        String value = getValue();
        if(value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")))
        {
            return true;
        }
        else if(value != null && (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")))
        {
            return false;
        }
        throw new NumberFormatException(qualifiedname+".value="+value);
    }
    
    public boolean getBooleanValue(boolean defaultvalue)
    {
        if(value == null)
        {
            setBooleanValue(defaultvalue);
        }
        try
        {
            return getBooleanValue();
        }
        catch(NumberFormatException nfe)
        {
            setBooleanValue(defaultvalue);
            return getBooleanValue();
        }
    }

    public int getHexValue()
    {        
        return (int) Long.parseLong(getValue().replace("0x", ""), 16);
    }
    
    public int getHexValue(int defaultvalue)
    {
        if(value == null)
        {
            setHexValue(defaultvalue);
        }
        try
        {
            return getHexValue();
        }
        catch(NumberFormatException nfe)
        {
            setHexValue(defaultvalue);
            return getHexValue();
        }
    }
    
    private static final Pattern patternRGB = Pattern.compile("(\\d+),(\\d+),(\\d+)");
    public Colour getColourRGB()
    {
        Matcher matcherRGB = patternRGB.matcher(getValue().replaceAll("\\s", ""));
        if(matcherRGB.matches())
        {
            return new ColourRGBA(
                    Integer.parseInt(matcherRGB.group(1)), 
                    Integer.parseInt(matcherRGB.group(2)), 
                    Integer.parseInt(matcherRGB.group(3)), 
                    0xFF);
        }
        
        return new ColourRGBA(getHexValue()<<8|0xFF);
    }
    
    public Colour getColourRGB(Colour defaultvalue)
    {
        if(value == null)
        {
            setColourRGB(defaultvalue);
        }
        try
        {
            return getColourRGB();
        }
        catch(NumberFormatException nfe)
        {
            setColourRGB(defaultvalue);
            return getColourRGB();
        }
    }

    public void save(PrintWriter writer, int tabs, String bracequalifier, boolean first)
    {
        String vname;
        if(qualifiedname.contains(".") && bracequalifier.length() > 0)
            vname = qualifiedname.substring(bracequalifier.length() + 1);
        else
            vname = qualifiedname;
        
        if(newline && !first)
            ConfigFile.writeLine(writer, "", tabs);
        
        writeComment(writer, tabs);
        if(value != null)
            ConfigFile.writeLine(writer, vname+"="+value, tabs);

        if(!hasChildTags())
            return;
        
        if(brace)
        {
            if(value == null)
                ConfigFile.writeLine(writer, vname, tabs);
            ConfigFile.writeLine(writer, "{", tabs);
            saveTagTree(writer, tabs+1, qualifiedname);
            ConfigFile.writeLine(writer, "}", tabs);
        }
        else
        {
            saveTagTree(writer, tabs, bracequalifier);
        }
    }    

    @Override
    public ConfigTag setComment(String comment)
    {
        super.setComment(comment);
        return this;
    }
    
    @Override
    public ConfigTag setSortMode(int mode)
    {
        super.setSortMode(mode);
        return this;
    }
    
    public ConfigTag setNewLine(boolean b)
    {
        newline = b;
        saveConfig();
        return this;
    }
    
    public ConfigTag useBraces()
    {
        brace = true;
        if(parent.newlinemode == 1)
            newline = true;
        
        saveConfig();
        return this;
    }
    
    public ConfigTag setPosition(int pos)
    {
        position = pos;
        saveConfig();
        return this;
    }
    
    public boolean containsTag(String tagname)
    {
        return getTag(tagname, false) != null;
    }
    
    public int getId(String name, int defaultValue)
    {
        return getTag(name).getIntValue(defaultValue);
    }
    
    public int getId(String name)
    {
        int ret = getId(name, IDBase);
        IDBase = ret+1;
        return ret;
    }
    
    public int getAcheivementId(String name, int defaultValue)
    {
        return getTag(name).getIntValue(defaultValue);
    }
    
    public ConfigTag setBaseID(int i)
    {
        IDBase = i;
        return this;
    }
    
    public ConfigTagParent parent;
    public String name;
    public String qualifiedname;
    public String value;
    public boolean brace;
    public boolean newline;
    public int position = Integer.MAX_VALUE;
    
    private int IDBase;
}
