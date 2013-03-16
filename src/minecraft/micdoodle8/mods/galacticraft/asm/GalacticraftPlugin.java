package micdoodle8.mods.galacticraft.asm;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value={"micdoodle8.mods.galacticraft.asm"})
public class GalacticraftPlugin implements IFMLLoadingPlugin, IFMLCallHook
{
	private static String transformerDir = "micdoodle8.mods.galacticraft.asm.";
	private static String serverConfigManager = transformerDir + "GCCoreServerConfigurationManagerTransformer";
	public static boolean hasRegistered = false;
    public static File fileLocation;
	
	@Override
	public String[] getLibraryRequestClass() 
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass() 
	{
		String[] asmStrings = new String[] {serverConfigManager};
		
        if (!hasRegistered) 
        {
            List<String> asm = Arrays.asList(asmStrings);
            
            for (String s : asm) 
            {
                try 
                {
                    Class c = Class.forName(s);
                    
                    if (c != null) 
                    {
                        String a = transformerDir + "Transformer";
                        
                        int l = a.length() + 1;
                        
                        FMLLog.info("[GCCoreTransformer]: " + "Registered Transformer " + s.substring(l));
                    }
                } 
                catch (Exception ex)
                {
                    FMLLog.info("[GCCoreTransformer]: " + "Error while running transformer " + s);
                    return null;
                }
            }
            
            hasRegistered = true;
        }
        
        return asmStrings;
	}

	@Override
	public String getModContainerClass() 
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) 
	{
		fileLocation = (File) data.get("coremodLocation");
		FMLLog.info("[GCCoreTransformer]: " + "Patching game...");
	}

	@Override
	public Void call() throws Exception 
	{
		return null;
	}
}
