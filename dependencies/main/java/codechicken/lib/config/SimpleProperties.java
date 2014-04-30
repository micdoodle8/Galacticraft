package codechicken.lib.config;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;

public class SimpleProperties
{
    public HashMap<String, String> propertyMap = new HashMap<String, String>();
    public File propertyFile;
    public boolean saveOnChange = false;
    public String encoding;
    
    private boolean loading = false;
    
    public SimpleProperties(File file, boolean saveOnChange, String encoding)
    {
        propertyFile = file;
        this.saveOnChange = saveOnChange;
        this.encoding = encoding;
    }
    
    public SimpleProperties(File file, boolean saveOnChange)
    {
        this(file, saveOnChange, Charset.defaultCharset().name());
    }
    
    public SimpleProperties(File file)
    {
        this(file, true);
    }
    
    public void load()
    {
        clear();
        loading = true;
        
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propertyFile), encoding));
            while(true)
            {
                String read = reader.readLine();
                if(read == null)
                    break;

                int equalIndex = read.indexOf('=');
                if(equalIndex == -1)
                    continue;
                
                setProperty(read.substring(0, equalIndex), read.substring(equalIndex+1));
            }
            reader.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        loading = false;
    }
    
    public void save()
    {
        try
        {
            PrintStream writer = new PrintStream(propertyFile);
            
            for(Entry<String, String> entry : propertyMap.entrySet())
            {
                writer.println(entry.getKey()+"="+entry.getValue());
            }
            
            writer.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void clear()
    {
        propertyMap.clear();
    }
    
    public boolean hasProperty(String key)
    {
        return propertyMap.containsKey(key);
    }
    
    public void removeProperty(String key)
    {
        if(propertyMap.remove(key) != null && saveOnChange && !loading)
            save();
    }
    
    public void setProperty(String key, int value)
    {
        setProperty(key, Integer.toString(value));
    }
    
    public void setProperty(String key, boolean value)
    {
        setProperty(key, Boolean.toString(value));
    }
    
    public void setProperty(String key, String value)
    {
        propertyMap.put(key, value);
        if(saveOnChange && !loading)
            save();
    }
    
    public int getProperty(String property, int defaultvalue)
    {
        try {
            return Integer.parseInt(getProperty(property, Integer.toString(defaultvalue)));
        } catch(NumberFormatException nfe)
        {return defaultvalue;}
    }
    
    public boolean getProperty(String property, boolean defaultvalue)
    {
        try {
            return Boolean.parseBoolean(getProperty(property, Boolean.toString(defaultvalue)));
        } catch(NumberFormatException nfe)
        {return defaultvalue;}
    }
    
    public String getProperty(String property, String defaultvalue)
    {
        String value = propertyMap.get(property);
        if(value == null)
        {
            setProperty(property, defaultvalue);
            return defaultvalue;
        }
        return value;
    }
    
    public String getProperty(String property)
    {
        return propertyMap.get(property);
    }
}
