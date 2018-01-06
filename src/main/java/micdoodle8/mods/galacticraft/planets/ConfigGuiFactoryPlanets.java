package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Set;

public class ConfigGuiFactoryPlanets implements IModGuiFactory
{
    public static class PlanetsConfigGUI extends GuiConfig
    {
        public PlanetsConfigGUI(GuiScreen parent)
        {
            super(parent, GalacticraftPlanets.getConfigElements(), Constants.MOD_ID_PLANETS, false, false, GCCoreUtil.translate("gc.configgui.planets.title"));
        }
    }

    @Override
    public void initialize(Minecraft minecraftInstance)
    {

    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

	public GuiScreen createConfigGui(GuiScreen arg0)
	{
		// TODO  Forge 2282 addition!
		return new PlanetsConfigGUI(arg0);
	}

	public boolean hasConfigGui()
	{
		return true;
	}
}
