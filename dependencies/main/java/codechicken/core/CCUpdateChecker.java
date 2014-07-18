package codechicken.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.versioning.ComparableVersion;
import cpw.mods.fml.relauncher.FMLInjectionData;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class CCUpdateChecker
{
    private static final ArrayList<String> updates = new ArrayList<String>();
    
    private static class ThreadUpdateCheck extends Thread
    {
        private final URL url;
        private final String mod;
        private final ComparableVersion version;
        
        public ThreadUpdateCheck(URL url, String mod, ComparableVersion version)
        {
            this.url = url;
            this.mod = mod;
            this.version = version;
            
            setName("CodeChicken Update Checker");
        }
        
        @Override
        public void run()
        {
            try
            {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String ret = read.readLine();
                read.close();
                if(!ret.startsWith("Ret: "))
                {
                    System.err.println("Failed to check update for "+mod+" returned: "+ret);
                    return;
                }
                ComparableVersion newversion = new ComparableVersion(ret.substring(5));
                if(newversion.compareTo(version) > 0)
                {
                    addUpdateMessage("Version "+newversion+" of "+mod+" is available");
                }
            }
            catch(SocketTimeoutException ste)
            {}
            catch(UnknownHostException uhe)
            {}
            catch(IOException iox)
            {
                iox.printStackTrace();
            }
        }
    }
    
    public static void tick()
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(!mc.inGameHasFocus)
            return;
        
        synchronized(updates)
        {
            for(String s : updates)
                mc.thePlayer.addChatMessage(new ChatComponentText(s));
            updates.clear();
        }
    }
    
    public static void addUpdateMessage(String s)
    {
        synchronized(updates)
        {
            updates.add(s);
        }
    }
    
    public static String mcVersion()
    {
        return (String) FMLInjectionData.data()[4];
    }
    
    public static void updateCheck(String mod, String version)
    {
        try
        {
            new ThreadUpdateCheck(
                    new URL("http://www.chickenbones.craftsaddle.org/Files/New_Versions/version.php?"+
                        "version="+mcVersion()+"&"+
                        "file="+mod), 
                    mod, new ComparableVersion(version)).start();
        }
        catch(MalformedURLException e)
        {}
    }

    public static void updateCheck(String mod)
    {
        updateCheck(mod, Loader.instance().getIndexedModList().get(mod).getVersion());
    }
}
