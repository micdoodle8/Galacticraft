package micdoodle8.mods.galacticraft.planets.asteroids.client.jei;

import java.lang.reflect.Method;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer.AstroMinerRecipeCategory;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer.AstroMinerRecipeWrapper;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeCategory;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeMaker;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class GalacticraftAsteroidsJEI extends BlankModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        boolean JEIversion450plus = false;
        Method[] methods = registry.getClass().getMethods();
        for (Method m : methods)
        {
            if (m.getName().equals("addRecipeCatalyst"))
            {
                JEIversion450plus = true;
                break;
            }
        }

        if (!JEIversion450plus)
        {
            IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
            registry.addRecipeCategories(new Tier3RocketRecipeCategory(guiHelper),
                    new AstroMinerRecipeCategory(guiHelper));
        }

        registry.handleRecipes(INasaWorkbenchRecipe.class, Tier3RocketRecipeWrapper::new, RecipeCategories.ROCKET_T3_ID);
        registry.handleRecipes(INasaWorkbenchRecipe.class, AstroMinerRecipeWrapper::new, RecipeCategories.ASTRO_MINER_ID);

        registry.addRecipes(Tier3RocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_T3_ID);
        registry.addRecipes(GalacticraftRegistry.getAstroMinerRecipes(), RecipeCategories.ASTRO_MINER_ID);

        if (JEIversion450plus)
        {
            registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T3_ID, RecipeCategories.ASTRO_MINER_ID);
        }
        else
        {
            ItemStack nasaWorkbench = new ItemStack(GCBlocks.nasaWorkbench);
            registry.addRecipeCategoryCraftingItem(nasaWorkbench, RecipeCategories.ROCKET_T3_ID);
            registry.addRecipeCategoryCraftingItem(nasaWorkbench, RecipeCategories.ASTRO_MINER_ID);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new Tier3RocketRecipeCategory(guiHelper),
                new AstroMinerRecipeCategory(guiHelper));
    }
}
