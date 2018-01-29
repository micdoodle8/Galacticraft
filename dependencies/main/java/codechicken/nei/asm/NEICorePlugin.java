package codechicken.nei.asm;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.Map;

@TransformerExclusions({ "codechicken.nei.asm" })
public class NEICorePlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static File location;
    public static boolean missingCCL = false;

    public NEICorePlugin() {
        cclCheck();
    }

    @Override
    public String[] getASMTransformerClass() {
        if (missingCCL) {
            FMLLog.log("NotEnoughItems", Level.FATAL, "Missing CCL! not registering class transformer.");
            return new String[0];
        }
        return new String[] { "codechicken.nei.asm.NEITransformer" };
    }

    @Override
    public String getModContainerClass() {
        return "codechicken.nei.NEIModContainer";
    }

    @Override
    public String getSetupClass() {
        return "codechicken.nei.asm.NEICorePlugin";
    }

    @Override
    public void injectData(Map<String, Object> data) {
        location = (File) data.get("coremodLocation");
        if (location == null) {
            location = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() {
        return null;
    }
    private static void cclCheck() {
        try {
            Class.forName("codechicken.lib.asm.ASMHelper", false, NEICorePlugin.class.getClassLoader());
            missingCCL = false;
        } catch (ClassNotFoundException cNFE) {
            cNFE.printStackTrace();
            missingCCL = true;
        }
    }
}
