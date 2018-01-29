package codechicken.core.launch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.asm.transformers.ModAccessTransformer;
import net.minecraftforge.fml.common.versioning.ComparableVersion;
import net.minecraftforge.fml.relauncher.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.URLClassPath;
import sun.net.util.URLUtil;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * For autodownloading stuff.
 * This is really unoriginal, mostly ripped off FML, credits to cpw.
 */
@Deprecated
public class DepLoader implements IFMLLoadingPlugin, IFMLCallHook {
    private static ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(1 << 23);
    private static final String owner = "DepLoader";
    private static DepLoadInst inst;
    private static final Logger logger = LogManager.getLogger(owner);

    public interface IYellAtPeople {
        void addYellingData(String data);

        void displayYellingData();
    }

    public static class YellGui extends JOptionPane implements IYellAtPeople {
        private ArrayList<String> yellingData = new ArrayList<String>();

        @Override
        public void addYellingData(String data) {
            yellingData.add(data);
        }

        @Override
        public void displayYellingData() {
            StringBuilder builder = new StringBuilder();
            builder.append("<html>");
            for (String data : yellingData) {
                builder.append("<br>");
                builder.append(data);
            }
            builder.append("</html>");
            JEditorPane ep = new JEditorPane("text/html", builder.toString());
            ep.setAutoscrolls(true);
            ep.setEditable(false);
            ep.setOpaque(false);
            ep.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent event) {
                    try {
                        if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                            Desktop.getDesktop().browse(event.getURL().toURI());
                        }
                    } catch (Exception ignored) {
                    }
                }
            });
            JOptionPane.showMessageDialog(null, ep, "DepLoader is Deprecated!", -1);
        }
    }

    public static class YellServer implements IYellAtPeople {
        private ArrayList<String> yellingData = new ArrayList<String>();

        @Override
        public void addYellingData(String data) {
            yellingData.add(data);
        }

        @Override
        public void displayYellingData() {
            for (String string : yellingData) {
                FMLLog.severe(string);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public interface IDownloadDisplay {
        void resetProgress(int sizeGuess);

        void setPokeThread(Thread currentThread);

        void updateProgress(int fullLength);

        boolean shouldStopIt();

        void updateProgressString(String string, Object... data);

        Object makeDialog();

        void showErrorDialog(String name, String url);
    }

    @SuppressWarnings("serial")
    public static class Downloader extends JOptionPane implements IDownloadDisplay {
        private JDialog container;
        private JLabel currentActivity;
        private JProgressBar progress;
        boolean stopIt;
        Thread pokeThread;

        private Box makeProgressPanel() {
            Box box = Box.createVerticalBox();
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            JLabel welcomeLabel = new JLabel("<html><b><font size='+1'>" + owner + " is setting up your minecraft environment</font></b></html>");
            box.add(welcomeLabel);
            welcomeLabel.setAlignmentY(LEFT_ALIGNMENT);
            welcomeLabel = new JLabel("<html>Please wait, " + owner + " has some tasks to do before you can play</html>");
            welcomeLabel.setAlignmentY(LEFT_ALIGNMENT);
            box.add(welcomeLabel);
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            currentActivity = new JLabel("Currently doing ...");
            box.add(currentActivity);
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            progress = new JProgressBar(0, 100);
            progress.setStringPainted(true);
            box.add(progress);
            box.add(Box.createRigidArea(new Dimension(0, 30)));
            return box;
        }

        @Override
        public JDialog makeDialog() {
            if (container != null) {
                return container;
            }

            setMessageType(JOptionPane.INFORMATION_MESSAGE);
            setMessage(makeProgressPanel());
            setOptions(new Object[] { "Stop" });
            addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getSource() == Downloader.this && evt.getPropertyName() == VALUE_PROPERTY) {
                        requestClose("This will stop minecraft from launching\nAre you sure you want to do this?");
                    }
                }
            });
            container = new JDialog(null, "Hello", ModalityType.MODELESS);
            container.setResizable(false);
            container.setLocationRelativeTo(null);
            container.add(this);
            this.updateUI();
            container.pack();
            container.setMinimumSize(container.getPreferredSize());
            container.setVisible(true);
            container.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            container.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    requestClose("Closing this window will stop minecraft from launching\nAre you sure you wish to do this?");
                }
            });
            return container;
        }

        protected void requestClose(String message) {
            int shouldClose = JOptionPane.showConfirmDialog(container, message, "Are you sure you want to stop?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (shouldClose == JOptionPane.YES_OPTION) {
                container.dispose();
            }

            stopIt = true;
            if (pokeThread != null) {
                pokeThread.interrupt();
            }
        }

        @Override
        public void updateProgressString(String progressUpdate, Object... data) {
            //FMLLog.finest(progressUpdate, data);
            if (currentActivity != null) {
                currentActivity.setText(String.format(progressUpdate, data));
            }
        }

        @Override
        public void resetProgress(int sizeGuess) {
            if (progress != null) {
                progress.getModel().setRangeProperties(0, 0, 0, sizeGuess, false);
            }
        }

        @Override
        public void updateProgress(int fullLength) {
            if (progress != null) {
                progress.getModel().setValue(fullLength);
            }
        }

        @Override
        public void setPokeThread(Thread currentThread) {
            this.pokeThread = currentThread;
        }

        @Override
        public boolean shouldStopIt() {
            return stopIt;
        }

        @Override
        public void showErrorDialog(String name, String url) {
            JEditorPane ep = new JEditorPane("text/html", "<html>" + owner + " was unable to download required library " + name + "<br>Check your internet connection and try restarting or download it manually from" + "<br><a href=\"" + url + "\">" + url + "</a> and put it in your mods folder" + "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);
            ep.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent event) {
                    try {
                        if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                            Desktop.getDesktop().browse(event.getURL().toURI());
                        }
                    } catch (Exception e) {
                    }
                }
            });

            JOptionPane.showMessageDialog(null, ep, "A download error has occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static class DummyDownloader implements IDownloadDisplay {
        @Override
        public void resetProgress(int sizeGuess) {
        }

        @Override
        public void setPokeThread(Thread currentThread) {
        }

        @Override
        public void updateProgress(int fullLength) {
        }

        @Override
        public boolean shouldStopIt() {
            return false;
        }

        @Override
        public void updateProgressString(String string, Object... data) {
        }

        @Override
        public Object makeDialog() {
            return null;
        }

        @Override
        public void showErrorDialog(String name, String url) {
        }
    }

    public static class VersionedFile {
        public final Pattern pattern;
        public final String filename;
        public final ComparableVersion version;
        public final String name;

        public VersionedFile(String filename, Pattern pattern) {
            this.pattern = pattern;
            this.filename = filename;
            Matcher m = pattern.matcher(filename);
            if (m.matches()) {
                name = m.group(1);
                version = new ComparableVersion(m.group(2));
            } else {
                name = null;
                version = null;
            }
        }

        public boolean matches() {
            return name != null;
        }
    }

    public static class Dependency {
        /**
         * Zip file to extract packed dependencies from
         */
        public File source;
        public String repo;
        public String packed;
        public VersionedFile file;
        public String testClass;
        public boolean coreLib;

        public String existing;

        /**
         * Flag set to add this dep to the classpath immediately because it is required for a coremod.
         */

        public Dependency(File source, String repo, String packed, VersionedFile file, String testClass, boolean coreLib) {
            this.source = source;
            this.repo = repo;
            this.packed = packed;
            this.file = file;
            this.coreLib = coreLib;
            this.testClass = testClass;
        }

        public void set(Dependency dep) {
            this.source = dep.source;
            this.repo = dep.repo;
            this.packed = dep.packed;
            this.file = dep.file;
        }
    }

    public static class DepLoadInst {
        private File modsDir;
        private File v_modsDir;
        private IDownloadDisplay downloadMonitor;
        private JDialog popupWindow;

        private Map<String, Dependency> depMap = new HashMap<String, Dependency>();
        private HashSet<String> depSet = new HashSet<String>();

        private File scanning;
        private LaunchClassLoader loader = (LaunchClassLoader) DepLoader.class.getClassLoader();

        public DepLoadInst() {
            String mcVer = (String) FMLInjectionData.data()[4];
            File mcDir = (File) FMLInjectionData.data()[6];

            modsDir = new File(mcDir, "mods");
            v_modsDir = new File(mcDir, "mods/" + mcVer);
            if (!v_modsDir.exists()) {
                v_modsDir.mkdirs();
            }
        }

        private void addClasspath(File file) {
            try {
                loader.addURL(file.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        private void deleteMod(File mod) {
            if (mod.delete()) {
                return;
            }

            try {
                URL url = mod.toURI().toURL();
                Field f_ucp = URLClassLoader.class.getDeclaredField("ucp");
                Field f_loaders = URLClassPath.class.getDeclaredField("loaders");
                Field f_lmap = URLClassPath.class.getDeclaredField("lmap");
                f_ucp.setAccessible(true);
                f_loaders.setAccessible(true);
                f_lmap.setAccessible(true);

                URLClassPath ucp = (URLClassPath) f_ucp.get(loader);
                Closeable loader = ((Map<String, Closeable>) f_lmap.get(ucp)).remove(URLUtil.urlNoFragString(url));
                if (loader != null) {
                    loader.close();
                    ((List<?>) f_loaders.get(ucp)).remove(loader);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!mod.delete()) {
                mod.deleteOnExit();
                String msg = owner + " was unable to delete file " + mod.getPath() + " the game will now try to delete it on exit. If this dialog appears again, delete it manually.";
                logger.error(msg);
                if (!GraphicsEnvironment.isHeadless()) {
                    JOptionPane.showMessageDialog(null, msg, "An update error has occured", JOptionPane.ERROR_MESSAGE);
                }

                System.exit(1);
            }
        }

        private void install(Dependency dep) {
            popupWindow = (JDialog) downloadMonitor.makeDialog();

            if (!extract(dep)) {
                download(dep);
            }

            dep.existing = dep.file.filename;
            scanDepInfo(new File(v_modsDir, dep.existing));
        }

        private boolean extract(Dependency dep) {
            if (dep.packed == null) {
                return false;
            }

            ZipFile zip = null;
            try {
                zip = new ZipFile(dep.source);
                ZipEntry libEntry = zip.getEntry(dep.packed + dep.file.filename);
                if (libEntry == null) {
                    return false;
                }

                downloadMonitor.updateProgressString("Extracting file %s\n", dep.source.getPath() + '!' + libEntry.toString());
                logger.info("Extracting file " + dep.source.getPath() + '!' + libEntry.toString());

                download(zip.getInputStream(libEntry), (int) libEntry.getSize(), dep);

                downloadMonitor.updateProgressString("Extraction complete");
                logger.info("Extraction complete");
            } catch (Exception e) {
                installError(e, dep, "extraction");
            } finally {
                try {
                    if (zip != null) {
                        zip.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        private void download(Dependency dep) {
            try {
                URL libDownload = new URL(dep.repo + dep.file.filename);
                downloadMonitor.updateProgressString("Downloading file %s", libDownload.toString());
                logger.info("Downloading file " + libDownload.toString());
                URLConnection connection = libDownload.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("User-Agent", "" + owner + " Downloader");
                download(connection.getInputStream(), connection.getContentLength(), dep);
                downloadMonitor.updateProgressString("Download complete");
                logger.info("Download complete");
            } catch (Exception e) {
                installError(e, dep, "download");
            }
        }

        private void installError(Exception e, Dependency dep, String s) {
            if (downloadMonitor.shouldStopIt()) {
                logger.error("You have stopped the " + s + " before it could complete");
                System.exit(1);
            }
            downloadMonitor.showErrorDialog(dep.file.filename, dep.repo + '/' + dep.file.filename);
            throw new RuntimeException(s + " error", e);
        }

        private void download(InputStream is, int sizeGuess, Dependency dep) throws Exception {
            File target = new File(v_modsDir, dep.file.filename);
            if (sizeGuess > downloadBuffer.capacity()) {
                throw new Exception(String.format("The file %s is too large to be downloaded by " + owner + " - the download is invalid", target.getName()));
            }

            downloadBuffer.clear();

            int read, fullLength = 0;

            downloadMonitor.resetProgress(sizeGuess);
            try {
                downloadMonitor.setPokeThread(Thread.currentThread());
                byte[] buffer = new byte[1024];
                while ((read = is.read(buffer)) >= 0) {
                    downloadBuffer.put(buffer, 0, read);
                    fullLength += read;
                    if (downloadMonitor.shouldStopIt()) {
                        break;
                    }

                    downloadMonitor.updateProgress(fullLength);
                }
                is.close();
                downloadMonitor.setPokeThread(null);
                downloadBuffer.limit(fullLength);
                downloadBuffer.position(0);
            } catch (InterruptedIOException e) {
                // We were interrupted by the stop button. We're stopping now.. clear interruption flag.
                Thread.interrupted();
                throw new Exception("Stop");
            }

            if (!target.exists()) {
                target.createNewFile();
            }

            downloadBuffer.position(0);
            FileOutputStream fos = new FileOutputStream(target);
            fos.getChannel().write(downloadBuffer);
            fos.close();
        }

        private String checkExisting(Dependency dep) {
            for (File f : modsDir.listFiles()) {
                VersionedFile vfile = new VersionedFile(f.getName(), dep.file.pattern);
                if (!vfile.matches() || !vfile.name.equals(dep.file.name)) {
                    continue;
                }

                if (f.renameTo(new File(v_modsDir, f.getName()))) {
                    continue;
                }

                deleteMod(f);
            }

            for (File f : v_modsDir.listFiles()) {
                VersionedFile vfile = new VersionedFile(f.getName(), dep.file.pattern);
                if (!vfile.matches() || !vfile.name.equals(dep.file.name)) {
                    continue;
                }

                int cmp = vfile.version.compareTo(dep.file.version);
                if (cmp < 0) {
                    logger.info("Deleted old version " + f.getName());
                    deleteMod(f);
                    return null;
                }
                if (cmp > 0) {
                    logger.info("Warning: version of " + dep.file.name + ", " + vfile.version + " is newer than request " + dep.file.version);
                    return f.getName();
                }
                return f.getName();//found dependency
            }
            return null;
        }

        public void load() {
            scanDepInfos();
            if (depMap.isEmpty()) {
                return;
            }
            boolean dontYell = Boolean.parseBoolean(System.getProperty("deploader.dontYellAtPeople", "false"));
            if (!dontYell) {
                yellAtPeople();
            }
            forceRemoveCCL();

            loadDeps();
            activateDeps();
        }

        private void activateDeps() {
            for (Dependency dep : depMap.values()) {
                File file = new File(v_modsDir, dep.existing);
                if (!searchCoreMod(file) && dep.coreLib) {
                    addClasspath(file);
                }
            }
        }

        /**
         * Looks for FMLCorePlugin attributes and adds to CoreModManager
         */
        private boolean searchCoreMod(File coreMod) {
            JarFile jar = null;
            Attributes mfAttributes;
            try {
                jar = new JarFile(coreMod);
                if (jar.getManifest() == null) {
                    return false;
                }

                ModAccessTransformer.addJar(jar);
                mfAttributes = jar.getManifest().getMainAttributes();
            } catch (IOException ioe) {
                FMLRelaunchLog.log(Level.ERROR, ioe, "Unable to read the jar file %s - ignoring", coreMod.getName());
                return false;
            } finally {
                try {
                    if (jar != null) {
                        jar.close();
                    }
                } catch (IOException ignored) {
                }
            }

            String fmlCorePlugin = mfAttributes.getValue("FMLCorePlugin");
            if (fmlCorePlugin == null) {
                return false;
            }

            addClasspath(coreMod);

            try {
                if (!mfAttributes.containsKey(new Attributes.Name("FMLCorePluginContainsFMLMod"))) {
                    FMLRelaunchLog.finer("Adding %s to the list of known coremods, it will not be examined again", coreMod.getName());
                    CoreModManager.getIgnoredMods().add(coreMod.getName());
                } else {
                    FMLRelaunchLog.finer("Found FMLCorePluginContainsFMLMod marker in %s, it will be examined later for regular @Mod instances", coreMod.getName());
                    CoreModManager.getReparseableCoremods().add(coreMod.getName());
                }
                Method m_loadCoreMod = CoreModManager.class.getDeclaredMethod("loadCoreMod", LaunchClassLoader.class, String.class, File.class);
                m_loadCoreMod.setAccessible(true);
                m_loadCoreMod.invoke(null, loader, fmlCorePlugin, coreMod);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return true;
        }

        private void loadDeps() {
            downloadMonitor = FMLLaunchHandler.side().isClient() ? new Downloader() : new DummyDownloader();
            try {
                while (!depSet.isEmpty()) {
                    Iterator<String> it = depSet.iterator();
                    Dependency dep = depMap.get(it.next());
                    it.remove();
                    load(dep);
                }
            } finally {
                if (popupWindow != null) {
                    popupWindow.setVisible(false);
                    popupWindow.dispose();
                }
            }
        }

        private void load(Dependency dep) {
            dep.existing = checkExisting(dep);
            if (dep.existing == null && DepLoader.class.getResource("/" + dep.testClass.replace('.', '/') + ".class") == null) {
                install(dep);
            }
        }

        private List<File> modFiles() {
            List<File> list = new LinkedList<File>();
            list.addAll(Arrays.asList(modsDir.listFiles()));
            list.addAll(Arrays.asList(v_modsDir.listFiles()));
            return list;
        }

        private void scanDepInfos() {
            for (File file : modFiles()) {
                if (!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip")) {
                    continue;
                }

                scanDepInfo(file);
            }
        }

        private void scanDepInfo(File file) {
            try {
                scanning = file;
                ZipFile zip = new ZipFile(file);
                ZipEntry e = zip.getEntry("dependencies.info");
                if (e != null) {
                    loadJSon(zip.getInputStream(e));
                }

                zip.close();
            } catch (Exception e) {
                logger.error("Failed to load dependencies.info from " + file.getName() + " as JSON", e);
            }
        }

        private void loadJSon(InputStream input) throws IOException {
            InputStreamReader reader = new InputStreamReader(input);
            JsonElement root = new JsonParser().parse(reader);
            if (root.isJsonArray()) {
                loadJSonArr(root);
            } else {
                loadJson(root.getAsJsonObject());
            }
            reader.close();
        }

        private void loadJSonArr(JsonElement root) throws IOException {
            for (JsonElement node : root.getAsJsonArray()) {
                loadJson(node.getAsJsonObject());
            }
        }

        private void loadJson(JsonObject node) throws IOException {
            boolean obfuscated = ((LaunchClassLoader) DepLoader.class.getClassLoader()).getClassBytes("net.minecraft.world.World") == null;

            String repo = node.has("repo") ? node.get("repo").getAsString() : null;
            String packed = node.has("packed") ? node.get("packed").getAsString() : null;
            String testClass = node.get("class").getAsString();

            String filename = node.get("file").getAsString();
            if (!obfuscated && node.has("dev")) {
                filename = node.get("dev").getAsString();
            }

            boolean coreLib = node.has("coreLib") && node.get("coreLib").getAsBoolean();

            Pattern pattern = null;
            try {
                if (node.has("pattern")) {
                    pattern = Pattern.compile(node.get("pattern").getAsString());
                }
            } catch (PatternSyntaxException e) {
                logger.error("Invalid filename pattern: " + node.get("pattern"), e);
            }
            if (pattern == null) {
                pattern = Pattern.compile("(\\w+).*?([\\d\\.]+)[-\\w]*\\.[^\\d]+");
            }

            VersionedFile file = new VersionedFile(filename, pattern);
            if (!file.matches()) {
                throw new RuntimeException("Invalid filename format for dependency: " + filename);
            }

            addDep(new Dependency(scanning, repo, packed, file, testClass, coreLib));
        }

        private void addDep(Dependency newDep) {
            Dependency oldDep = depMap.get(newDep.file.name);
            if (oldDep == null) {
                depMap.put(newDep.file.name, newDep);
                depSet.add(newDep.file.name);
                return;
            }

            //combine newer info from newDep into oldDep
            oldDep.coreLib |= newDep.coreLib;
            int cmp = newDep.file.version.compareTo(oldDep.file.version);
            if (cmp == 1) {
                oldDep.set(newDep);
            } else if (cmp == 0) {
                if (oldDep.repo == null) {
                    oldDep.repo = newDep.repo;
                }
                if (oldDep.packed == null) {
                    oldDep.source = newDep.source;
                    oldDep.packed = newDep.packed;
                }
            }
        }

        public void yellAtPeople() {
            IYellAtPeople yellAtPeople = FMLLaunchHandler.side().isClient() ? new YellGui() : new YellServer();
            yellAtPeople.addYellingData("The CodeChicken Dependency Downloader is being phased out and has detected mods using it.");
            yellAtPeople.addYellingData("The downloader will function as normal but this message serves as a log of what mods are using it.");
            yellAtPeople.addYellingData("If you are a mod dev, Please phase out all use of the dependency downloader.");
            yellAtPeople.addYellingData("If you are a normal user, you can follow the instructions at the bottom of the window if you would like.");
            yellAtPeople.addYellingData("The following mods are being requested to download...");
            yellAtPeople.addYellingData("");
            for (Entry<String, Dependency> entry : depMap.entrySet()) {
                yellAtPeople.addYellingData("\"" + entry.getValue().source.getName() + "\"" + " Wants: " + entry.getKey() + "-" + entry.getValue().file.version);
            }
            yellAtPeople.addYellingData("");
            yellAtPeople.addYellingData("");
            yellAtPeople.addYellingData("Don't be alarmed by this window, it is simply so i can log what mods are using the Dependency Downloader.");
            yellAtPeople.addYellingData("If you would like to report this please follow instructions listed <a href=\"https://github.com/TheCBProject/CodeChickenLib/issues/12\">here</a>.");
            yellAtPeople.addYellingData("Click the OK button to continue.");

            yellAtPeople.displayYellingData();
        }

        private void forceRemoveCCL() {
            if (depSet.contains("CodeChickenLib")) {
                depSet.remove("CodeChickenLib");
                depMap.remove("CodeChickenLib");
            }
            try {
                File[] files = v_modsDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().contains("CodeChickenLib") && file.getName().endsWith(".jar")) {
                            FMLLog.info("Found old CCL [%s] attempting delete..", file.getName());
                            deleteMod(file);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void load() {
        if (inst == null) {
            inst = new DepLoadInst();
            inst.load();
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public Void call() {
        load();

        return null;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
