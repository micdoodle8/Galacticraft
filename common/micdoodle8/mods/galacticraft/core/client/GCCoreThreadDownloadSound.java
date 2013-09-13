package micdoodle8.mods.galacticraft.core.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiDownloadingSounds;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.gui.GuiScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreThreadDownloadSound extends Thread
{
    public File resourcesFolder;
    private final Minecraft mc;
    private boolean closing = false;
    private GCCoreGuiDownloadingSounds gui;
    private GuiScreen previousGui;
    private int downloadCount = 0;
    private int byteCount = 0;
    private final List<String> downloadUrls;

    public GCCoreThreadDownloadSound(File par1File, Minecraft par2Minecraft, List<String> downloadUrls)
    {
        this.mc = par2Minecraft;
        this.setName("GC Resource download thread");
        this.setDaemon(true);
        this.resourcesFolder = new File(par1File, "assets/sound/");
        this.downloadUrls = downloadUrls;

        if (!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs())
        {
            throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
        }
    }

    @Override
    public void run()
    {
        this.previousGui = FMLClientHandler.instance().getClient().currentScreen;
        this.gui = new GCCoreGuiDownloadingSounds();
        
        for (String urlString : this.downloadUrls)
        {
            try
            {
                final URL url = new URL(urlString);
                final DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
                final DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
                // Add a timeout of 60 seconds to getting the list, MC stalls
                // without sound for some users.
                final URLConnection con = url.openConnection();
                con.addRequestProperty("User-Agent", "Mozilla/4.76");
                con.setConnectTimeout(60000);
                con.setReadTimeout(60000);
                final Document document = documentbuilder.parse(con.getInputStream());
                final NodeList nodelist = document.getElementsByTagName("Contents");

                for (int i = 0; i < 2; ++i)
                {
                    for (int j = 0; j < nodelist.getLength(); ++j)
                    {
                        final Node node = nodelist.item(j);

                        if (node.getNodeType() == 1)
                        {
                            final Element element = (Element) node;
                            final String s = element.getElementsByTagName("Key").item(0).getChildNodes().item(0).getNodeValue();
                            final long k = Long.parseLong(element.getElementsByTagName("Size").item(0).getChildNodes().item(0).getNodeValue());

                            if (k > 0L)
                            {
                                this.downloadAndInstallResource(url, s, k, i);

                                if (this.closing)
                                {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            catch (final Exception exception)
            {
                this.loadResource(this.resourcesFolder, "");
                exception.printStackTrace();
            }
        }

        GCCoreTickHandlerClient.lastOpenGui = this.previousGui;
    }

    public void reloadResources()
    {
        this.loadResource(this.resourcesFolder, "");
    }

    private void loadResource(File par1File, String par2Str)
    {
        final File[] afile = par1File.listFiles();

        for (File element : afile)
        {
            if (element.isDirectory())
            {
                this.loadResource(element, par2Str + element.getName() + "/");
            }
            else
            {
                try
                {
                    this.mc.sndManager.addSound(par2Str + element.getName());
                }
                catch (final Exception exception)
                {
                    this.mc.getLogAgent().logWarning("Failed to add " + par2Str + element.getName() + " in resources");
                }
            }
        }
    }

    private void downloadAndInstallResource(URL par1URL, String par2Str, long par3, int par5)
    {
        try
        {
            final int k = par2Str.indexOf("/");
            final String s1 = par2Str.substring(0, k);

            if (s1.equalsIgnoreCase("sound3"))
            {
                if (par5 != 0)
                {
                    return;
                }
            }
            else if (par5 != 1)
            {
                return;
            }

            final File file1 = new File(this.resourcesFolder, par2Str);

            if (!file1.exists() || file1.length() != par3)
            {
                file1.getParentFile().mkdirs();
                final String s2 = par2Str.replaceAll(" ", "%20");
                FMLClientHandler.instance().getClient().displayGuiScreen(this.gui);
                this.downloadResource(new URL(par1URL, s2), file1, par3);
                this.downloadCount++;
                int roundedDataSize = (int) Math.floor(10 * this.byteCount / (1024.0F * 1024.0F));
                this.gui.displayStatus = "Downloaded " + this.downloadCount + " sounds#" + roundedDataSize / 10.0F + " MB";
                this.gui.displayStatusColor = 0x11FF11;

                if (this.closing)
                {
                    return;
                }
            }

            if (!par2Str.contains("music"))
            {
                this.mc.sndManager.addSound(par2Str);
            }
            else
            {
                ClientProxyCore.newMusic.add(new SoundPoolEntry(par2Str, file1.toURI().toURL()));
            }
        }
        catch (MalformedURLException exception)
        {
            this.gui.displayStatus = "Download Failed!#MalformedURLException 1";
            this.gui.displayStatusColor = 0xFF1111;
            this.loadResource(this.resourcesFolder, "");
            exception.printStackTrace();
        }
        catch (IOException exception)
        {
            this.gui.displayStatus = "Download Failed!#IOException, unable to open connection to URL.";
            this.gui.displayStatusColor = 0xFF1111;
            this.loadResource(this.resourcesFolder, "");
            exception.printStackTrace();
        }
        catch (final Exception exception)
        {
            this.gui.displayStatus = "Download Failed!#General Exception";
            this.gui.displayStatusColor = 0xFF1111;
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    private void downloadResource(URL par1URL, File par2File, long par3) throws IOException
    {
        final byte[] abyte = new byte[4096];
        // Add a timeout of 60 seconds to getting the list, MC stalls without
        // sound for some users.
        final URLConnection con = par1URL.openConnection();
        con.addRequestProperty("User-Agent", "Mozilla/4.76");
        con.setConnectTimeout(60000);
        con.setReadTimeout(60000);
        final DataInputStream datainputstream = new DataInputStream(con.getInputStream());
        final DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(par2File));
        do
        {
            int j;

            if ((j = datainputstream.read(abyte)) < 0)
            {
                datainputstream.close();
                dataoutputstream.close();
                return;
            }

            dataoutputstream.write(abyte, 0, j);
            this.byteCount += j;
        }
        while (!this.closing);
    }

    public void closeMinecraft()
    {
        this.closing = true;
    }
}
