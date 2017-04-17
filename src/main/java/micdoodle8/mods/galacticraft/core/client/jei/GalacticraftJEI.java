package micdoodle8.mods.galacticraft.core.client.jei;

import mezz.jei.api.*;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;

import javax.annotation.Nonnull;

@JEIPlugin
public class GalacticraftJEI extends BlankModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new Tier1RocketRecipeCategory(guiHelper),
                                    new BuggyRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new Tier1RocketRecipeHandler(),
                                    new BuggyRecipeHandler());
        registry.addRecipes(Tier1RocketRecipeMaker.getRecipesList());
        registry.addRecipes(BuggyRecipeMaker.getRecipesList());
    }
}
