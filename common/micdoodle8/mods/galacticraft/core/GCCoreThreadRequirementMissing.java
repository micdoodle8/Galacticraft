package micdoodle8.mods.galacticraft.core;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiMissingCore;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreThreadRequirementMissing extends Thread
{
    private static Side threadSide;

    public GCCoreThreadRequirementMissing(Side threadSide)
    {
        super("Galacticraft Requirement Check Thread");
        this.setDaemon(true);
        GCCoreThreadRequirementMissing.threadSide = threadSide;
    }

    @Override
    public void run()
    {
        JEditorPane ep = null;
        JOptionPane pane = null;

        if (!Loader.isModLoaded("Micdoodlecore"))
        {
            if (GCCoreThreadRequirementMissing.threadSide.isServer())
            {
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("===================================================================");
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("MicdoodleCore not found in mods folder. Galacticraft will not load.");
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("===================================================================");
            }
            else
            {
                GCCoreThreadRequirementMissing.openGuiClient();
            }
        }
        else if (!GCCoreConfigManager.forceLoadGC.getBoolean(false) && !GCCoreCompatibilityManager.isBCLoaded() && !GCCoreCompatibilityManager.isIc2Loaded() && !GCCoreCompatibilityManager.isTELoaded())
        {
            if (GCCoreThreadRequirementMissing.threadSide.isServer())
            {
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("===============================================================================");
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere(LanguageRegistry.instance().getStringLocalization("message.warning1.name", "en_US"));
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("        " + LanguageRegistry.instance().getStringLocalization("message.warning2.name", "en_US").replace(" Ignore?", ""));
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("    Set \"Load Basic Componets\" to \"true\" in the Galacticraft Core config.");
                FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("===============================================================================");
            }
            else
            {
                final String err = "<strong><h1>" + LanguageRegistry.instance().getStringLocalization("message.warning1.name") + "</h1></strong><br /><h3>" + LanguageRegistry.instance().getStringLocalization("message.warning2.name") + "</h3>";
                System.out.println(err);
                ep = new JEditorPane("text/html", "<html>" + err + "</html>");

                ep.setEditable(false);
                ep.setOpaque(false);

                pane = new JOptionPane(ep, JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, null, new Object[] { LanguageRegistry.instance().getStringLocalization("message.button1.name"), LanguageRegistry.instance().getStringLocalization("message.button2.name"), LanguageRegistry.instance().getStringLocalization("message.button3.name"), LanguageRegistry.instance().getStringLocalization("message.button4.name") }, null);
                final JDialog dialog = pane.createDialog(null, "Missing Dependancy");
                dialog.setVisible(true);
                dialog.requestFocusInWindow();
                final Object selectedValue = pane.getValue();

                if (selectedValue != null && selectedValue.equals(LanguageRegistry.instance().getStringLocalization("message.button3.name")))
                {
                    System.exit(-1);
                }
                else if (selectedValue != null && selectedValue.equals(LanguageRegistry.instance().getStringLocalization("message.button4.name")))
                {
                    GCCoreConfigManager.forceLoadGC.set(true);
                    GCCoreConfigManager.configuration.save();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static void openGuiClient()
    {
        FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiMissingCore());
    }
}
