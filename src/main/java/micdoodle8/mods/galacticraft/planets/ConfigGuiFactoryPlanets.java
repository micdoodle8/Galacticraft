package micdoodle8.mods.galacticraft.planets;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

public class ConfigGuiFactoryPlanets implements IModGuiFactory
{
    public static class PlanetsConfigGUI extends GuiConfig
    {
        public PlanetsConfigGUI(GuiScreen parent)
        {
            super(parent, GalacticraftPlanets.getConfigElements(), GalacticraftPlanets.MODID, false, false, "Galacticraft Planets Config");
        }
    }

    @Override
    public void initialize(Minecraft minecraftInstance)
    {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return PlanetsConfigGUI.class;
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
