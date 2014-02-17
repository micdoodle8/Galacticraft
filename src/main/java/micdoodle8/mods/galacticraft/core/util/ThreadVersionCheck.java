package micdoodle8.mods.galacticraft.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreThreadVersionCheck.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ThreadVersionCheck extends Thread
{
	public static ThreadVersionCheck instance = new ThreadVersionCheck();
	private int count = 0;
	public static final int LOCALMAJVERSION = 3;
	public static final int LOCALMINVERSION = 0;
	public static final int LOCALBUILDVERSION = 0;
	public static int remoteMajVer;
	public static int remoteMinVer;
	public static int remoteBuildVer;

	private ThreadVersionCheck()
	{
		super("Galacticraft Version Check Thread");
	}

	public static void startCheck(Side sideToCheck)
	{
		final Thread thread = new Thread(ThreadVersionCheck.instance);
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

		while (this.count < 3 && ThreadVersionCheck.remoteBuildVer == 0)
		{
			try
			{
				final URL url = new URL("http://micdoodle8.com/galacticraft/version.html");
				final HttpURLConnection http = (HttpURLConnection) url.openConnection();
				http.addRequestProperty("User-Agent", "Mozilla/4.76");
				final BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
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
							ThreadVersionCheck.remoteMajVer = Integer.parseInt(str2[0]);
							ThreadVersionCheck.remoteMinVer = Integer.parseInt(str2[1]);
							ThreadVersionCheck.remoteBuildVer = Integer.parseInt(str2[2]);
						}

						if (ThreadVersionCheck.remoteMajVer > ThreadVersionCheck.LOCALMAJVERSION || ThreadVersionCheck.remoteMajVer == ThreadVersionCheck.LOCALMAJVERSION && ThreadVersionCheck.remoteMinVer > ThreadVersionCheck.LOCALMINVERSION || ThreadVersionCheck.remoteMajVer == ThreadVersionCheck.LOCALMAJVERSION && ThreadVersionCheck.remoteMinVer == ThreadVersionCheck.LOCALMINVERSION && ThreadVersionCheck.remoteBuildVer > ThreadVersionCheck.LOCALBUILDVERSION)
						{
							Thread.sleep(5000);

							if (sideToCheck.equals(Side.CLIENT))
							{
								FMLClientHandler.instance().getClient().thePlayer.addChatMessage(new ChatComponentTranslation(EnumColor.GREY + "New " + EnumColor.DARK_AQUA + "Galacticraft" + EnumColor.GREY + " version available! v" + String.valueOf(ThreadVersionCheck.remoteMajVer) + "." + String.valueOf(ThreadVersionCheck.remoteMinVer) + "." + String.valueOf(ThreadVersionCheck.remoteBuildVer) + EnumColor.DARK_BLUE + " http://micdoodle8.com/"));
							}
							else if (sideToCheck.equals(Side.SERVER))
							{
								GCLog.severe("New Galacticraft version available! v" + String.valueOf(ThreadVersionCheck.remoteMajVer) + "." + String.valueOf(ThreadVersionCheck.remoteMinVer) + "." + String.valueOf(ThreadVersionCheck.remoteBuildVer) + " http://micdoodle8.com/");
							}
						}
					}
				}
			}
			catch (final Exception e)
			{
			}

			if (ThreadVersionCheck.remoteBuildVer == 0)
			{
				try
				{
					GCLog.severe(StatCollector.translateToLocal("newversion.failed.name"));
					Thread.sleep(15000);
				}
				catch (final InterruptedException e)
				{
				}
			}
			else
			{
				GCLog.info(StatCollector.translateToLocal("newversion.success.name") + " " + ThreadVersionCheck.remoteMajVer + "." + ThreadVersionCheck.remoteMinVer + "." + ThreadVersionCheck.remoteBuildVer);
			}

			this.count++;
		}
	}
}
