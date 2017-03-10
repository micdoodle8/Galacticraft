package codechicken.lib.util;

import net.minecraft.util.text.translation.I18n;

public class LangProxy {

    public final String namespace;

    public LangProxy(String namespace) {
        this.namespace = namespace + ".";
    }

    public String translate(String key) {
        return I18n.translateToLocal(namespace + key);
    }

    public String format(String key, Object... params) {
        return I18n.translateToLocalFormatted(namespace + key, params);
    }
}
