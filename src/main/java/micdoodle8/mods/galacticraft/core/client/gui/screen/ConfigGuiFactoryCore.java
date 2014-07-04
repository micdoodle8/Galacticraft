package micdoodle8.mods.galacticraft.core.client.gui.screen;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

public class ConfigGuiFactoryCore implements IModGuiFactory
{
    public static class CoreConfigGUI extends GuiConfig
    {
        public CoreConfigGUI(GuiScreen parent)
        {
            super(parent, ConfigManagerCore.getConfigElements(), GalacticraftCore.MODID, false, false, "Galacticraft Config");
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
