package codechicken.lib;

import codechicken.lib.fingerprint.FingerprintChecker;
import codechicken.lib.internal.proxy.CommonProxy;
import codechicken.lib.util.FuelUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.io.File;

/**
 * Created by covers1624 on 12/10/2016.
 */
@Mod (modid = CodeChickenLib.MOD_ID, name = CodeChickenLib.MOD_NAME, acceptedMinecraftVersions = CodeChickenLib.mcVersion, certificateFingerprint = "f1850c39b2516232a2108a7bd84d1cb5df93b261")
public class CodeChickenLib {

    public static final String MOD_ID = "CodeChickenLib";
    public static final String MOD_NAME = "CodeChicken Lib";
    public static final String version = "${mod_version}";
    public static final String mcVersion = "[1.10.2]";

    public static final File minecraftDir = (File) FMLInjectionData.data()[6];

    @SidedProxy (clientSide = "codechicken.lib.internal.proxy.ClientProxy", serverSide = "codechicken.lib.internal.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FingerprintChecker.runFingerprintChecks();
        GameRegistry.registerFuelHandler(new FuelUtils());
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @EventHandler
    public void onServerStartingEvent(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

}
