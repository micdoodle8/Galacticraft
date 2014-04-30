package codechicken.nei;

import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class TMIUninstaller
{
    public static class InstallerGui extends JFrame
    {
        private static final long serialVersionUID = 1L;
        private JTextField labelInfo;
        private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

        public InstallerGui() {
            setTitle("TMI Uninstaller");
            initComponents();
        }

        private class ResizeListener implements LayoutManager
        {
            public void addLayoutComponent(String name, Component comp) {
            }

            public void layoutContainer(Container parent) {
                int w = getContentPane().getWidth();
                labelInfo.setBounds(10, 45, w - 20, 20);
                labelInfo.setScrollOffset(1000);
                labelInfo.update(getGraphics());
            }

            public Dimension minimumLayoutSize(Container parent) {
                return new Dimension(150, 100);
            }

            public Dimension preferredLayoutSize(Container parent) {
                return new Dimension(250, 120);
            }


            public void removeLayoutComponent(Component comp) {
            }
        }

        private void initComponents() {
            setLayout(new ResizeListener());
            add(getLabelInfo());
            setSize(358, 270);
        }

        public class InstallerListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e) {
            }
        }

        public JTextField getLabelInfo() {
            if (labelInfo == null) {
                labelInfo = new JTextField();
                labelInfo.setFont(new Font("Tahoma", Font.PLAIN, 13));
                labelInfo.setHorizontalAlignment(SwingConstants.CENTER);
                labelInfo.setEditable(false);
                labelInfo.setText("Uninstalling TMI");
            }
            return labelInfo;
        }

        public static void installLnF() {
            try {
                String lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
                UIManager.setLookAndFeel(lnfClassname);
            } catch (Exception e) {
                System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL + " on this platform:" + e.getMessage());
            }
        }
    }

    private static File jarFile;
    public static InstallerGui mainframe;

    public static void main(String[] args) {
        jarFile = new File(args[0]);

        InstallerGui.installLnF();
        mainframe = new InstallerGui();
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainframe.setLocationRelativeTo(null);
        mainframe.pack();
        mainframe.setVisible(true);

        if (!jarFile.exists())
            mainframe.getLabelInfo().setText("Invalid Minecraft.jar");
        else
            uninstall();
    }

    private static File getJarFile() {
        URL url = Minecraft.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            if (url.getProtocol().equals("jar"))
                url = new URL(url.getPath().substring(0, url.getPath().indexOf('!')));
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            return new File(url.getPath());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTMIUninstaller() throws IOException {
        File file = new File(getJarFile().getParentFile(), "TMIUninstaller");
        if (file.exists()) {
            NEIClientConfig.logger.info("Removing TMI Uninstaller");
            deleteDir(file, true);
        }
    }

    public static boolean TMIInstalled() {
        File jarFile = getJarFile();
        if (!jarFile.getName().endsWith(".jar"))
            return false;

        try {
            ZipFile zip = new ZipFile(jarFile);
            ZipEntry tmi = zip.getEntry("mod_TooManyItems.class");
            zip.close();
            return tmi != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static void runTMIUninstaller() throws IOException {
        File jarFile = getJarFile();
        File uninstDir = new File(jarFile.getParentFile(), "TMIUninstaller");
        if (!uninstDir.exists())
            uninstDir.mkdirs();

        NEIClientConfig.logger.info("Installing Uninstaller: " + uninstDir.getPath());

        FileInputStream fileinputstream = new FileInputStream(jarFile);
        ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
        do {
            ZipEntry zipentry = zipinputstream.getNextEntry();
            if (zipentry == null) {
                break;
            }
            String fullname = zipentry.getName().replace('\\', '/');
            if (!zipentry.isDirectory() && fullname.replace('/', '.').startsWith(TMIUninstaller.class.getCanonicalName())) {
                File outFile = new File(uninstDir, fullname);
                NEIClientConfig.logger.info("Extracting File: " + outFile.getPath());

                if (!outFile.getParentFile().exists())
                    outFile.getParentFile().mkdirs();

                outFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(outFile);

                byte[] buffer = new byte[65535];
                int read = 0;
                while ((read = zipinputstream.read(buffer)) != -1)//write loop
                {
                    fout.write(buffer, 0, read);
                }
                fout.close();
            }
        }
        while (true);
        fileinputstream.close();

        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

        String classpath = uninstDir.getAbsolutePath();
        String className = TMIUninstaller.class.getCanonicalName();

        String jarPath = jarFile.getPath();

        NEIClientConfig.logger.info("Running Process: " + javaBin + " -cp \"" + classpath + "\" \"" + className + "\" \"" + jarPath + "\"");

        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className, jarPath);
        builder.start();
    }

    public static void deleteDir(File directory, boolean remove) throws IOException {
        NEIClientConfig.logger.debug("Deleting Dir: " + directory.getPath());
        if (!directory.exists()) {
            if (!remove)
                directory.mkdirs();

            return;
        }
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file, true);
            } else {
                if (!file.delete()) {
                    throw new IOException("Delete Failed: " + file);
                }
            }
        }
        if (remove) {
            try {
                if (!directory.delete()) {
                    throw new IOException("Delete Failed: " + directory);
                }
            } catch (SecurityException e) {
                throw e;
            }
        }
    }

    public static void uninstall() {
        File backupfile = new File(jarFile.getParentFile(), jarFile.getName() + ".bak");
        try {
            if (!backupfile.exists())
                backupfile.createNewFile();

            FileOutputStream backout = new FileOutputStream(backupfile);
            FileInputStream jarin = new FileInputStream(jarFile);
            backout.getChannel().transferFrom(jarin.getChannel(), 0, jarFile.length());

            backout.close();
            jarin.close();

            ZipInputStream zipin = new ZipInputStream(new FileInputStream(backupfile));
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(jarFile));
            byte[] buffer = new byte[20000];
            int read = 0;
            while (true) {
                ZipEntry entry = zipin.getNextEntry();
                if (entry == null) {
                    break;
                }
                if (entry.isDirectory()) continue;
                String name = entry.getName();
                if (name.startsWith("TMI") || name.startsWith("_tmi") || name.startsWith("tmi.png") || name.startsWith("mod_TooManyItems")) {
                    continue;
                }
                zipout.putNextEntry(new ZipEntry(name));
                InputStream in = zipin;
                while ((read = in.read(buffer)) != -1) {
                    zipout.write(buffer, 0, read);
                }
                zipout.closeEntry();
                zipin.closeEntry();
            }
            zipin.close();
            zipout.close();

            mainframe.getLabelInfo().setText("Uninstall Completed. Close this and restart minecraft.");
        } catch (Exception e) {
            e.printStackTrace();
            mainframe.getLabelInfo().setText("Invalid Minecraft.jar");
            try {
                FileInputStream backin = new FileInputStream(backupfile);
                FileOutputStream jarout = new FileOutputStream(jarFile);
                jarout.getChannel().transferFrom(backin.getChannel(), 0, backupfile.length());

                backin.close();
                jarout.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
