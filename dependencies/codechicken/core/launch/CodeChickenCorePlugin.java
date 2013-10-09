package codechicken.core.launch;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.minecraft.launchwrapper.LaunchClassLoader;

import codechicken.core.asm.CodeChickenAccessTransformer;
import codechicken.core.asm.CodeChickenCoreModContainer;
import codechicken.core.asm.DelegatedTransformer;
import codechicken.core.asm.MCPDeobfuscationTransformer;
import codechicken.core.asm.TweakTransformer;

import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value={"codechicken.core.asm"})
public class CodeChickenCorePlugin implements IFMLLoadingPlugin, IFMLCallHook
{    
    public static final String mcVersion = "[1.6.4]";
    
    public static LaunchClassLoader cl = (LaunchClassLoader) CodeChickenCorePlugin.class.getClassLoader();
    public static File minecraftDir;
    public static String currentMcVersion;
    
    public CodeChickenCorePlugin()
    {
        if(minecraftDir != null)
            return;//get called twice, once for IFMLCallHook
        
        minecraftDir = (File) FMLInjectionData.data()[6];
        currentMcVersion = (String) FMLInjectionData.data()[4];
        
        DepLoader.load();
        CodeChickenCoreModContainer.loadConfig();
        MCPDeobfuscationTransformer.load();
    }
    
    @Override
    public String[] getLibraryRequestClass()
    {
        return null;
    }

    public static void versionCheck(String reqVersion, String mod)
    {
        String mcVersion = (String) FMLInjectionData.data()[4];
        if(!VersionParser.parseRange(reqVersion).containsVersion(new DefaultArtifactVersion(mcVersion)))
        {
            String err = "This version of "+mod+" does not support minecraft version "+mcVersion;
            System.err.println(err);
            
            JEditorPane ep = new JEditorPane("text/html", 
                    "<html>" +
                    err + 
                    "<br>Remove it from your coremods folder and check <a href=\"http://www.minecraftforum.net/topic/909223-\">here</a> for updates" +
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
        versionCheck(mcVersion, "CodeChickenCore");
        return new String[]{
                "codechicken.lib.asm.ClassHeirachyManager",
                "codechicken.core.asm.CodeChickenAccessTransformer",
                "codechicken.core.asm.InterfaceDependancyTransformer",
                "codechicken.core.asm.TweakTransformer",
                "codechicken.core.asm.FeatureHackTransformer", 
                "codechicken.core.asm.DelegatedTransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return "codechicken.core.asm.CodeChickenCoreModContainer";
    }

    @Override
    public String getSetupClass()
    {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        //sorry, had to get these earlier
    }

    @Override
    public Void call()
    {
        TweakTransformer.load();
        scanCodeChickenMods();
        
        return null;
    }

    private void scanCodeChickenMods()
    {
        File modsDir = new File(minecraftDir, "mods");
        for(File file : modsDir.listFiles())
            scanMod(file);
        File versionModsDir = new File(minecraftDir, "mods/"+currentMcVersion);
        if(versionModsDir.exists())
            for(File file : versionModsDir.listFiles())
                scanMod(file);
    }

    private void scanMod(File file) {
        if(!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip"))
            return;
        
        try
        {
            JarFile jar = new JarFile(file);
            try
            {
                Manifest manifest = jar.getManifest();
                if(manifest == null)
                    return;
                Attributes attr = manifest.getMainAttributes();
                if(attr == null)
                    return;
                
                String mapFile = attr.getValue("AccessTransformer");
                if(mapFile != null)
                {
                    File temp = extractTemp(jar, mapFile);
                    System.out.println("Adding AccessTransformer: "+mapFile);
                    CodeChickenAccessTransformer.addTransformerMap(temp.getPath());
                    temp.delete();
                }
                String transformer = attr.getValue("CCTransformer");
                if(transformer != null)
                {
                    System.out.println("Adding CCTransformer: "+transformer);
                    DelegatedTransformer.addTransformer(transformer, jar, file);
                }
            }
            finally
            {
                jar.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("CodeChickenCore: Failed to read jar file: "+file.getName());
        }
    }

    private File extractTemp(JarFile jar, String mapFile) throws IOException
    {
        File temp = new File("temp.dat");
        if(!temp.exists())
            temp.createNewFile();
        FileOutputStream fout = new FileOutputStream(temp);
        byte[] data = new byte[4096];
        int read = 0;
        InputStream fin = jar.getInputStream(jar.getEntry(mapFile));
        while((read = fin.read(data)) > 0)
        {
            fout.write(data, 0, read);
        }
        fin.close();
        fout.close();
        return temp;
    }
}
