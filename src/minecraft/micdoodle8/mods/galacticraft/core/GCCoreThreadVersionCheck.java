package micdoodle8.mods.galacticraft.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

public class GCCoreThreadVersionCheck extends Thread
{
	public static GCCoreThreadVersionCheck instance = new GCCoreThreadVersionCheck();
	private int count = 0;

	public GCCoreThreadVersionCheck()
	{
		super("Galacticraft Version Check Thread");
	}

	public static void startCheck(Side sideToCheck)
	{
		final Thread thread = new Thread(GCCoreThreadVersionCheck.instance);
		thread.start();
	}

	@Override
	public void run()
	{
		final Side sideToCheck = FMLCommonHandler.instance().getSide();

		if (sideToCheck == null)
		{
			return;
		}

		while (count < 3 && GalacticraftCore.remoteBuildVer == 0)
		{
			try
			{
				URL url = new URL("http://micdoodle8.com/galacticraft/version.html");
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				http.addRequestProperty("User-Agent", "Mozilla/4.76");
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
				String str;
				String str2[] = null;

				while ((str = in.readLine()) != null)
				{
		    		
		    		if (str.contains("Version"))
		    		{
		        		str = str.replace("Version=", "");

			    		str2 = str.split("#");

			    		if (str2 != null && str2.length == 3)
			    		{
			    			GalacticraftCore.remoteMajVer = Integer.parseInt(str2[0]);
			    			GalacticraftCore.remoteMinVer = Integer.parseInt(str2[1]);
			    			GalacticraftCore.remoteBuildVer = Integer.parseInt(str2[2]);
			    		}

			    		if (GalacticraftCore.remoteMajVer > GalacticraftCore.LOCALMAJVERSION || GalacticraftCore.remoteMajVer == GalacticraftCore.LOCALMAJVERSION && GalacticraftCore.remoteMinVer > GalacticraftCore.LOCALMINVERSION || GalacticraftCore.remoteMajVer == GalacticraftCore.LOCALMAJVERSION && GalacticraftCore.remoteMinVer == GalacticraftCore.LOCALMINVERSION && GalacticraftCore.remoteBuildVer > GalacticraftCore.LOCALBUILDVERSION)
			    		{
		    				Thread.sleep(5000);

			    			if (sideToCheck.equals(Side.CLIENT))
			    			{
			    				FMLClientHandler.instance().getClient().thePlayer.addChatMessage("\u00a77New \u00a73Galacticraft \u00a77version available! v" + String.valueOf(GalacticraftCore.remoteMajVer) + "." + String.valueOf(GalacticraftCore.remoteMinVer) + "." + String.valueOf(GalacticraftCore.remoteBuildVer) + " \u00a71http://micdoodle8.com/");
			    			}
			    			else if (sideToCheck.equals(Side.SERVER))
			    			{
			    				GCLog.severe("New Galacticraft version available! v" + String.valueOf(GalacticraftCore.remoteMajVer) + "." + String.valueOf(GalacticraftCore.remoteMinVer) + "." + String.valueOf(GalacticraftCore.remoteBuildVer) + " http://micdoodle8.com/");
			    			}
			    		}
		    		}
				}
			}
			catch (final Exception e)
			{
			}

			if (GalacticraftCore.remoteBuildVer == 0)
			{
				try
				{
					GCLog.severe("Galacticraft update check failed! Trying again in 15 seconds");
					Thread.sleep(15000);
				}
				catch (final InterruptedException e)
				{
				}
			}
			else
			{
				GCLog.info("Galacticraft remote version found: " + GalacticraftCore.remoteMajVer + "." + GalacticraftCore.remoteMinVer + "." + GalacticraftCore.remoteBuildVer);
			}
			
			count++;
		}
	}
}
