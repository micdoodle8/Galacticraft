package micdoodle8.mods.galacticraft.core;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

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
            final String err = "<strong><h1>Galacticraft Requires IndustrialCraft 2, Thermal Expansion or Universal Electricity!</h1></strong><br /><h3>One or more of these mods is REQUIRED for crafting/gameplay. Ignore?</h3>";
            System.out.println(err);
            final JEditorPane ep = new JEditorPane("text/html", "<html>" + err + "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);

            final JOptionPane pane = new JOptionPane(ep, JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, null, new Object[]
            { "Ignore", "Load Basic Components", "Exit", "Do not show again" }, null);
            final JDialog dialog = pane.createDialog(null, "Missing Dependancy");
            dialog.setVisible(true);
            final Object selectedValue = pane.getValue();

            if (selectedValue != null && selectedValue.equals("Exit"))
            {
                System.exit(-1);
            }
            else if (selectedValue != null && selectedValue.equals("Do not show again"))
            {
                GCCoreConfigManager.forceLoadGC.set(true);
                GCCoreConfigManager.configuration.save();
            }
            else if (selectedValue != null && selectedValue.equals("Load Basic Components"))
            {
                GCCoreConfigManager.loadBC.set(true);
                GCCoreConfigManager.configuration.save();
                JOptionPane.showMessageDialog(null, "You must restart Minecraft for this change to take effect!");
            }
        }
    }
}
