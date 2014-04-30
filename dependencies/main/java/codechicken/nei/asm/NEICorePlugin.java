package codechicken.nei.asm;

import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.lib.asm.ASMInit;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import java.io.File;
import java.util.Map;

@TransformerExclusions(value = {"codechicken.nei.asm"})
public class NEICorePlugin implements IFMLLoadingPlugin, IFMLCallHook
{
    public static File location;

    public NEICorePlugin() {
        ASMInit.init();
    }

    @Override
    public String[] getASMTransformerClass() {
        CodeChickenCorePlugin.versionCheck(CodeChickenCorePlugin.mcVersion, "NotEnoughItems");
        return new String[]{"codechicken.nei.asm.NEITransformer"};
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
