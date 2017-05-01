package micdoodle8.mods.galacticraft.planets.asteroids.client.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer.AstroMinerRecipeCategory;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer.AstroMinerRecipeHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer.AstroMinerRecipeMaker;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeCategory;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeMaker;

import javax.annotation.Nonnull;

@JEIPlugin
public class GalacticraftAsteroidsJEI extends BlankModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new Tier3RocketRecipeCategory(guiHelper),
                new AstroMinerRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new Tier3RocketRecipeHandler(),
                new AstroMinerRecipeHandler());
        registry.addRecipes(Tier3RocketRecipeMaker.getRecipesList());
        registry.addRecipes(AstroMinerRecipeMaker.getRecipesList());
    }
}
