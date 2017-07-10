package micdoodle8.mods.galacticraft.planets.asteroids.client.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer.AstroMinerRecipeCategory;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer.AstroMinerRecipeWrapper;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeCategory;
import micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket.Tier3RocketRecipeWrapper;
import net.minecraft.item.ItemStack;

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

        registry.handleRecipes(INasaWorkbenchRecipe.class, Tier3RocketRecipeWrapper::new, RecipeCategories.ROCKET_T3_ID);
        registry.handleRecipes(INasaWorkbenchRecipe.class, AstroMinerRecipeWrapper::new, RecipeCategories.ASTRO_MINER_ID);

        registry.addRecipes(GalacticraftRegistry.getRocketT3Recipes(), RecipeCategories.ROCKET_T3_ID);
        registry.addRecipes(GalacticraftRegistry.getAstroMinerRecipes(), RecipeCategories.ASTRO_MINER_ID);
        ItemStack nasaWorkbench = new ItemStack(GCBlocks.nasaWorkbench);
        registry.addRecipeCategoryCraftingItem(nasaWorkbench, RecipeCategories.ROCKET_T3_ID);
        registry.addRecipeCategoryCraftingItem(nasaWorkbench, RecipeCategories.ASTRO_MINER_ID);
    }
}
