package codechicken.lib.config;

import java.io.File;

public class DefaultingConfigFile extends ConfigFile
{
    public DefaultingConfigFile(File file) {
        super();
        if(file.exists())
            load(file);
    }

    @Override
    public void saveConfig() {
        if(file != null)
            super.saveConfig();
    }
}
