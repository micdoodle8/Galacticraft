package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThreadVersionCheck extends Thread
{
    public static ThreadVersionCheck INSTANCE = new ThreadVersionCheck();
    private int count = 0;

    public static int remoteMajVer;
    public static int remoteMinVer;
    public static int remotePatchVer = -1;

    public ThreadVersionCheck()
    {
        super("Galacticraft Version Check Thread");
    }

    public static void startCheck()
    {
        final Thread thread = new Thread(ThreadVersionCheck.INSTANCE);
        thread.start();
    }

    @Override
    public void run()
    {
        final LogicalSide sideToCheck = EffectiveSide.get();

        if (sideToCheck == null || ConfigManagerCore.disableUpdateCheck.get())
        {
            return;
        }

        while (this.count < 3 && remotePatchVer == -1)
        {
            BufferedReader in = null;
            try
            {
                final URL url = new URL("https://micdoodle8.com/galacticraft/version.html");
                final HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.addRequestProperty("User-Agent", "Mozilla/4.76");
                InputStreamReader streamReader = new InputStreamReader(http.getInputStream());
                in = new BufferedReader(streamReader);
                String str;
                String[] str2 = null;

                while ((str = in.readLine()) != null)
                {

                    if (str.contains("Version"))
                    {
                        str = str.replace("Version=", "");

                        str2 = str.split("#");

                        if (str2.length >= 3)
                        {
                            remoteMajVer = Integer.parseInt(str2[0]);
                            remoteMinVer = Integer.parseInt(str2[1]);
                            remotePatchVer = Integer.parseInt(str2[2]);
                        }

                        if (remoteMajVer == Constants.LOCALMAJVERSION && (remoteMinVer > Constants.LOCALMINVERSION || (remoteMinVer == Constants.LOCALMINVERSION && remotePatchVer > Constants.LOCALPATCHVERSION)))
                        {
                            Thread.sleep(5000);

                            if (sideToCheck.equals(LogicalSide.CLIENT))
                            {
                                Minecraft.getInstance().player.sendMessage(new StringTextComponent(EnumColor.GREY + "New " + EnumColor.DARK_AQUA + Constants.MOD_NAME_SIMPLE + EnumColor.GREY + " version available! v" + remoteMajVer + "." + remoteMinVer + "." + remotePatchVer + ".xxx" + EnumColor.DARK_BLUE + " http://micdoodle8.com/"));
                            }
                            else if (sideToCheck.equals(LogicalSide.SERVER))
                            {
                                GCLog.severe("New Galacticraft version available! v" + remoteMajVer + "." + remoteMinVer + "." + remotePatchVer + ".xxx" + " http://micdoodle8.com/");
                            }
                        }
                        break;
                    }
                }

                in.close();
                streamReader.close();
            }
            catch (final Exception e)
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }

            if (remotePatchVer == -1)
            {
                try
                {
                    GCLog.severe(GCCoreUtil.translate("newversion.failed"));
                    Thread.sleep(15000);
                }
                catch (final InterruptedException e)
                {
                }
            }
            else
            {
                GCLog.info(GCCoreUtil.translate("newversion.success") + " " + remoteMajVer + "." + remoteMinVer + "." + remotePatchVer);
            }

            this.count++;
        }
    }
}
