package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.util.Set;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;

public class ConfigGuiFactoryCore implements IModGuiFactory
{
    public static class CoreConfigGUI extends GuiConfig
    {
        public CoreConfigGUI(GuiScreen parent)
        {
            super(parent, ConfigManagerCore.getConfigElements(), Constants.MOD_ID_PLANETS, false, false, GCCoreUtil.translate("gc.configgui.title"));
        }
    }

    @Override
    public void initialize(Minecraft minecraftInstance)
    {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return CoreConfigGUI.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
    {
        return null;
    }
}
