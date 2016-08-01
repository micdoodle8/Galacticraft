package codechicken.nei.asm;

import codechicken.core.launch.CodeChickenCorePlugin;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import java.io.File;
import java.util.Map;

@TransformerExclusions({ "codechicken.nei.asm" })
public class NEICorePlugin implements IFMLLoadingPlugin, IFMLCallHook {
    public static File location;

    @Override
    public String[] getASMTransformerClass() {
        CodeChickenCorePlugin.versionCheck(CodeChickenCorePlugin.mcVersion, "NotEnoughItems");
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
}
