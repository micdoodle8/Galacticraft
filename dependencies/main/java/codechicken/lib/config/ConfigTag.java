package codechicken.lib.config;

import java.io.PrintWriter;

public class ConfigTag extends ConfigTagParent {

    public ConfigTagParent parent;
    public String name;
    public String qualifiedname;
    public String value;
    public boolean brace;
    public boolean newline;
    public int position = Integer.MAX_VALUE;

    private int IDBase;

    public interface IConfigType<T> {

        String configValue(T entry);

        T valueOf(String text) throws Exception;
    }

    public ConfigTag(ConfigTagParent parent, String name) {
        this.parent = parent;
        this.name = name;
        qualifiedname = parent.getNameQualifier() + name;
        newline = parent.newlinemode == 2;
        parent.addChild(this);
    }

    @Override
    public String getNameQualifier() {
        return qualifiedname + ".";
    }

    @Override
    public void saveConfig() {
        parent.saveConfig();
    }

    /**
     * Called when the tag is loaded from a config file as opposed to constructed by a mod
     *
     * @return this
     */
    public ConfigTag onLoaded() {
        return this;
    }

    public void setValue(String value) {
        this.value = value;
        saveConfig();
    }

    public void setDefaultValue(String defaultValue) {
        if (value == null) {
            value = defaultValue;
            saveConfig();
        }
    }

    public void setIntValue(int i) {
        setValue(Integer.toString(i));
    }

    public void setBooleanValue(boolean b) {
        setValue(Boolean.toString(b));
    }

    public void setHexValue(int i) {
        setValue("0x" + Long.toString(((long) i) << 32 >>> 32, 16));
    }

    public <T> void set(IConfigType<T> type, T entry) {
        setValue(type.configValue(entry));
    }

    public String getValue() {
        return value;
    }

    public String getValue(String defaultValue) {
        setDefaultValue(defaultValue);
        return value;
    }

    public int getIntValue() {
        return Integer.parseInt(getValue());
    }

    public int getIntValue(int defaultValue) {
        try {
            if (value != null) {
                return getIntValue();
            }
        } catch (NumberFormatException ignored) {
        }

        setIntValue(defaultValue);
        return defaultValue;
    }

    public boolean getBooleanValue() {
        String value = getValue();
        if (value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))) {
            return true;
        } else if (value != null && (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no"))) {
            return false;
        }

        throw new NumberFormatException(qualifiedname + ".value=" + value);
    }

    public boolean getBooleanValue(boolean defaultValue) {
        try {
            if (value != null) {
                return getBooleanValue();
            }
        } catch (NumberFormatException ignored) {
        }

        setBooleanValue(defaultValue);
        return defaultValue;
    }

    public int getHexValue() {
        return (int) Long.parseLong(getValue().replace("0x", ""), 16);
    }

    public int getHexValue(int defaultValue) {
        try {
            if (value != null) {
                return getHexValue();
            }
        } catch (NumberFormatException ignored) {
        }

        setHexValue(defaultValue);
        return defaultValue;
    }

    public <T> T get(IConfigType<T> type) {
        try {
            return type.valueOf(getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(IConfigType<T> type, T defaultValue) {
        try {
            if (value != null) {
                return get(type);
            }
        } catch (Exception ignored) {
        }

        set(type, defaultValue);
        return defaultValue;
    }

    public void save(PrintWriter writer, int tabs, String bracequalifier, boolean first) {
        String vname;
        if (qualifiedname.contains(".") && bracequalifier.length() > 0) {
            vname = qualifiedname.substring(bracequalifier.length() + 1);
        } else {
            vname = qualifiedname;
        }

        if (newline && !first) {
            ConfigFile.writeLine(writer, "", tabs);
        }

        writeComment(writer, tabs);
        if (value != null) {
            ConfigFile.writeLine(writer, vname + "=" + value, tabs);
        }

        if (!hasChildTags()) {
            return;
        }

        if (brace) {
            if (value == null) {
                ConfigFile.writeLine(writer, vname, tabs);
            }
            ConfigFile.writeLine(writer, "{", tabs);
            saveTagTree(writer, tabs + 1, qualifiedname);
            ConfigFile.writeLine(writer, "}", tabs);
        } else {
            saveTagTree(writer, tabs, bracequalifier);
        }
    }

    @Override
    public ConfigTag setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    @Override
    public ConfigTag setSortMode(int mode) {
        super.setSortMode(mode);
        return this;
    }

    public ConfigTag setNewLine(boolean b) {
        newline = b;
        saveConfig();
        return this;
    }

    public ConfigTag useBraces() {
        brace = true;
        if (parent.newlinemode == 1) {
            newline = true;
        }

        saveConfig();
        return this;
    }

    public ConfigTag setPosition(int pos) {
        position = pos;
        saveConfig();
        return this;
    }

    public boolean containsTag(String tagname) {
        return getTag(tagname, false) != null;
    }

    public int getId(String name, int defaultValue) {
        return getTag(name).getIntValue(defaultValue);
    }

    public int getId(String name) {
        int ret = getId(name, IDBase);
        IDBase = ret + 1;
        return ret;
    }

    public int getAcheivementId(String name, int defaultValue) {
        return getTag(name).getIntValue(defaultValue);
    }

    public ConfigTag setBaseID(int i) {
        IDBase = i;
        return this;
    }
}
