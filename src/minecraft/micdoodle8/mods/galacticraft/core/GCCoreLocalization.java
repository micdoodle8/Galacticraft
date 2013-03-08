package micdoodle8.mods.galacticraft.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.Properties;

import net.minecraft.util.StringTranslate;
import cpw.mods.fml.common.FMLLog;

/**
* Simple mod localization class.
*
* @author Jimeo Wan
* @license Public domain
*
*/
public class GCCoreLocalization {

    private static final String DEFAULT_LANGUAGE = "en_US";

    private String modName = null;
    private String loadedLanguage = null;
    private final Properties defaultMappings = new Properties();
    private final Properties mappings = new Properties();

    /**
     * Loads the mod's localization files. All language files must be stored in
     * "[modname]/lang/", in .properties files. (ex: for the mod 'invtweaks',
     * the french translation is in: "invtweaks/lang/fr_FR.properties")
     *
     * @param modName The mod name
     */
    public GCCoreLocalization(String modName) {
        if (modName == null) {
            throw new InvalidParameterException("Mod name can't be null");
        }
        this.modName = modName;
        FMLLog.info("" + modName);
        this.load(GCCoreLocalization.getCurrentLanguage(), true);
    }

    /**
     * Get a string for the given key, in the currently active translation.
     *
     * @param key
     * @return
     */
    public synchronized String get(String key) {
        final String currentLanguage = GCCoreLocalization.getCurrentLanguage();
        if (!currentLanguage.equals(this.loadedLanguage)) {
            this.load(currentLanguage, true);
        }
        return this.mappings.getProperty(key, this.defaultMappings.getProperty(key, key));
    }

    private void load(String newLanguage, boolean force) {
        this.defaultMappings.clear();
        this.mappings.clear();
        try {
            final BufferedReader langStream = new BufferedReader(new InputStreamReader(net.minecraft.enchantment.Enchantment.class.getResourceAsStream(
                            this.modName + "lang/" + newLanguage + ".properties"), "UTF-8"));
            final BufferedReader defaultLangStream = new BufferedReader(new InputStreamReader(net.minecraft.enchantment.Enchantment.class.getResourceAsStream(
                            this.modName + "lang/" + GCCoreLocalization.DEFAULT_LANGUAGE + ".properties"), "UTF-8"));
            this.loadMappings(langStream == null ? defaultLangStream : langStream, this.mappings);
            this.loadMappings(defaultLangStream, this.defaultMappings);
            if (langStream != null) {
                langStream.close();
            }
            defaultLangStream.close();
        } catch (final Exception e) {
        	FMLLog.warning("COULD NOT LOAD GALACTICRAFT LANGUAGE FILE(S)");
            if (force){
                this.load(GCCoreLocalization.DEFAULT_LANGUAGE, false);
            }
            return;
        }
        this.loadedLanguage = newLanguage;
    }

    private static String getCurrentLanguage() {
        return StringTranslate.getInstance().getCurrentLanguage();
    }

    private void loadMappings(BufferedReader var3, Properties par1Properties)
    {
    	try
    	{
        	for (String var4 = var3.readLine(); var4 != null; var4 = var3.readLine())
            {
                var4 = var4.trim();

                var4 = var4.replace("?", "");

                if (!var4.startsWith("#"))
                {
                    final String[] var5 = var4.split("=");

                    if (var5 != null && var5.length == 2)
                    {
                        par1Properties.setProperty(var5[0], var5[1]);
                    }
                }
            }
    	}
    	catch (final Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}