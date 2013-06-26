package micdoodle8.mods.galacticraft.core;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreThreadRequirementMissing extends Thread
{
    public static GCCoreThreadRequirementMissing instance = new GCCoreThreadRequirementMissing();

    public GCCoreThreadRequirementMissing()
    {
        super("Galacticraft Requirement Check Thread");
    }

    public static void startCheck()
    {
        final Thread thread = new Thread(GCCoreThreadRequirementMissing.instance);
        thread.start();
    }

    @Override
    public void run()
    {
        if (!GCCoreConfigManager.forceLoadGC.getBoolean(false) && !GCCoreConfigManager.loadBC.getBoolean(false) && !GCCoreCompatibilityManager.isIc2Loaded() && !GCCoreCompatibilityManager.isTELoaded())
        {
            final String err = "<strong><h1>" + LanguageRegistry.instance().getStringLocalization("message.warning1.name") + "</h1></strong><br /><h3>" + LanguageRegistry.instance().getStringLocalization("message.warning2.name") + "</h3>";
            System.out.println(err);
            final JEditorPane ep = new JEditorPane("text/html", "<html>" + err + "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);

            final JOptionPane pane = new JOptionPane(ep, JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, null, new Object[] { LanguageRegistry.instance().getStringLocalization("message.button1.name"), LanguageRegistry.instance().getStringLocalization("message.button2.name"), LanguageRegistry.instance().getStringLocalization("message.button3.name"), LanguageRegistry.instance().getStringLocalization("message.button4.name") }, null);
            final JDialog dialog = pane.createDialog(null, "Missing Dependancy");
            dialog.setVisible(true);
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
            else if (selectedValue != null && selectedValue.equals(LanguageRegistry.instance().getStringLocalization("message.button2.name")))
            {
                GCCoreConfigManager.loadBC.set(true);
                GCCoreConfigManager.configuration.save();
                JOptionPane.showMessageDialog(null, "You must restart Minecraft for this change to take effect!");
            }
        }
    }
}
