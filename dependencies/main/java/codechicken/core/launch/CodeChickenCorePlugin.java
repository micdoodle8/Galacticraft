package codechicken.core.launch;

import codechicken.core.CCUpdateChecker;
import codechicken.core.internal.CCCEventHandler;
import codechicken.lib.CodeChickenLib;
import codechicken.lib.config.ConfigFile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "CodeChickenCore", name = "CodeChicken Core", dependencies = "required-after:CodeChickenLib@[" + CodeChickenLib.version + ",)", acceptedMinecraftVersions = CodeChickenLib.mcVersion, certificateFingerprint = "f1850c39b2516232a2108a7bd84d1cb5df93b261")
public class CodeChickenCorePlugin {
    public static final String version = "${mod_version}";

    public static File minecraftDir;
    public static String currentMcVersion;
    public static Logger logger = LogManager.getLogger("CodeChickenCore");

    public static ConfigFile config;

    public static void loadConfig() {
        if (config == null) {
            config = new ConfigFile(new File(minecraftDir, "config/CodeChickenCore.cfg")).setComment("CodeChickenCore configuration file.");
        }
    }

    public CodeChickenCorePlugin() {
        if (minecraftDir != null) {
            return;//get called twice, once for IFMLCallHook
        }

        minecraftDir = (File) FMLInjectionData.data()[6];
        currentMcVersion = (String) FMLInjectionData.data()[4];

        loadConfig();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FingerprintChecker.runFingerprintChecks();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide().isClient()) {
            if (config.getTag("checkUpdates").getBooleanValue(true)) {
                CCUpdateChecker.updateCheck("CodeChickenCore");
            }

            MinecraftForge.EVENT_BUS.register(new CCCEventHandler());
        }
    }
}
