package codechicken.lib.asm;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@TransformerExclusions ({ "codechicken.lib.asm", "codechicken.lib.config", "codechicken.lib.fingerprint" })
public class CCLCorePlugin implements IFMLLoadingPlugin, IFMLCallHook {

    public static Logger logger = LogManager.getLogger("CodeChicken Lib");

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "codechicken.lib.asm.ClassHeirachyManager", "codechicken.lib.asm.CCL_ASMTransformer" };
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
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() throws Exception {
        scanMods();
        new ASMHelper();
        return null;
    }

    private void scanMods() {
        File modsDir = new File((File) FMLInjectionData.data()[6], "mods");
        for (File file : modsDir.listFiles()) {
            scanMod(file);
        }
        File versionModsDir = new File(modsDir, (String) FMLInjectionData.data()[4]);
        if (versionModsDir.exists()) {
            File[] files = versionModsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    scanMod(file);
                }
            }
        }
    }

    private void scanMod(File file) {
        if (!file.getName().endsWith(".jar") && !file.getName().endsWith(".zip")) {
            return;
        }

        try {
            JarFile jar = new JarFile(file);
            try {
                Manifest manifest = jar.getManifest();
                if (manifest == null) {
                    return;
                }
                Attributes attr = manifest.getMainAttributes();
                if (attr == null) {
                    return;
                }

                String transformer = attr.getValue("CCTransformerExclusions");
                if (transformer != null) {
                    List<String> exclusions = Arrays.asList(transformer.split(","));
                    for (String ex : exclusions) {
                        Launch.classLoader.addTransformerExclusion(ex);
                    }
                }
            } finally {
                jar.close();
            }
        } catch (Exception e) {
            logger.error("CodeChickenLib: Failed to read jar file: " + file.getName(), e);
        }
    }
}
