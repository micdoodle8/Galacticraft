package codechicken.lib.util;

import net.minecraft.util.StatCollector;

public class LangProxy
{
    public final String namespace;

    public LangProxy(String namespace) {
        this.namespace = namespace+".";
    }

    public String translate(String key) {
        return StatCollector.translateToLocal(namespace+key);
    }

    public String format(String key, Object... params) {
        return StatCollector.translateToLocalFormatted(namespace+key, params);
    }
}
