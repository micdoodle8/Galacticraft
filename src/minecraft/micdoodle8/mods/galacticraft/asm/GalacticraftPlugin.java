package micdoodle8.mods.galacticraft.asm;

import java.awt.Desktop;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value={"micdoodle8.mods.galacticraft.asm"})
public class GalacticraftPlugin implements IFMLLoadingPlugin, IFMLCallHook
{
	private static String transformerDir = "micdoodle8.mods.galacticraft.asm.";
	private static String transformerMain = GalacticraftPlugin.transformerDir + "GCCoreTransformer";
	public static boolean hasRegistered = false;
    public static File fileLocation;
	public static final String mcVersion = "[1.5.1]";
	
	@Override
	public String[] getLibraryRequestClass() 
	{
		return null;
	}

	public static void versionCheck(String reqVersion, String mod)
	{
		String mcVersion = (String) FMLInjectionData.data()[4];
		
		FMLLog.info(mcVersion);
		
		if(!VersionParser.parseRange(reqVersion).containsVersion(new DefaultArtifactVersion(mcVersion)))
		{
			String err = "This version of " + mod + " does not support minecraft version " + mcVersion;
			System.err.println(err);
			
            JEditorPane ep = new JEditorPane("text/html", 
					"<html>" +
					err + 
					"<br>Remove it from your coremods folder and check <a href=\"http://micdoodle8.com\">here</a> for updates" +
					"</html>");

            ep.setEditable(false);
            ep.setOpaque(false);
            ep.addHyperlinkListener(new HyperlinkListener()
			{
				@Override
				public void hyperlinkUpdate(HyperlinkEvent event)
				{
					try
					{
						if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
							Desktop.getDesktop().browse(event.getURL().toURI());
					}
					catch(Exception e)
					{}
				}
			});
            
            JOptionPane.showMessageDialog(null, ep, "Fatal error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	@Override
	public String[] getASMTransformerClass() 
	{
		GalacticraftPlugin.versionCheck(GalacticraftPlugin.mcVersion, "GalacticraftCore");
		String[] asmStrings = new String[] {GalacticraftPlugin.transformerMain};
		
        if (!GalacticraftPlugin.hasRegistered) 
        {
            List<String> asm = Arrays.asList(asmStrings);
            
            for (String s : asm) 
            {
                try 
                {
                    Class c = Class.forName(s);
                    
                    if (c != null) 
                    {
                        String a = GalacticraftPlugin.transformerDir + "Transformer";
                        
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
            
            GalacticraftPlugin.hasRegistered = true;
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
        return "micdoodle8.mods.galacticraft.asm.GalacticraftPlugin";
	}

	@Override
	public void injectData(Map<String, Object> data) 
	{
		GalacticraftPlugin.fileLocation = (File) data.get("coremodLocation");
		FMLLog.info("[GCCoreTransformer]: " + "Patching game...");
        if(data.containsKey("mcLocation"))
        	GalacticraftCore.minecraftDir = (File) data.get("mcLocation");
	}

	@Override
	public Void call() throws Exception 
	{
        GalacticraftAccessTransformer.addTransformerMap("galacticraft_at.cfg");
		return null;
	}
}
