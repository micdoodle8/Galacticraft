package codechicken.nei.api;

/**
 * An nei configuration entry point should implement this class and have name "NEI<someting>Config"
 * loadConfig will only be called when NEI is installed.
 */
public interface IConfigureNEI
{
    public void loadConfig();
    
    public String getName();
    public String getVersion();
}
