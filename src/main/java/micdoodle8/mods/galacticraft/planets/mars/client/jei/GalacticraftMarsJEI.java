package micdoodle8.mods.galacticraft.planets.mars.client.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket.CargoRocketRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket.CargoRocketRecipeMaker;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket.CargoRocketRecipeWrapper;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier.GasLiquefierRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier.GasLiquefierRecipeMaker;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier.GasLiquefierRecipeWrapper;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynth.MethaneSynthRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynth.MethaneSynthRecipeMaker;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynth.MethaneSynthRecipeWrapper;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket.Tier2RocketRecipeCategory;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket.Tier2RocketRecipeMaker;
import micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket.Tier2RocketRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class GalacticraftMarsJEI extends BlankModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registry.handleRecipes(INasaWorkbenchRecipe.class, Tier2RocketRecipeWrapper::new, RecipeCategories.ROCKET_T2_ID);
        registry.handleRecipes(GasLiquefierRecipeWrapper.class, recipe -> recipe, RecipeCategories.GAS_LIQUEFIER_ID);
        registry.handleRecipes(INasaWorkbenchRecipe.class, CargoRocketRecipeWrapper::new, RecipeCategories.ROCKET_CARGO_ID);
        registry.handleRecipes(MethaneSynthRecipeWrapper.class, recipe -> recipe, RecipeCategories.METHANE_SYNTHESIZER_ID);

        registry.addRecipes(Tier2RocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_T2_ID);
        registry.addRecipes(GasLiquefierRecipeMaker.getRecipesList(), RecipeCategories.GAS_LIQUEFIER_ID);
        registry.addRecipes(CargoRocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_CARGO_ID);
        registry.addRecipes(MethaneSynthRecipeMaker.getRecipesList(), RecipeCategories.METHANE_SYNTHESIZER_ID);

        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T2_ID, RecipeCategories.ROCKET_CARGO_ID);
        registry.addRecipeCatalyst(new ItemStack(MarsBlocks.machineT2), RecipeCategories.GAS_LIQUEFIER_ID);
        registry.addRecipeCatalyst(new ItemStack(MarsBlocks.machineT2, 1, 4), RecipeCategories.METHANE_SYNTHESIZER_ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new Tier2RocketRecipeCategory(guiHelper),
                new GasLiquefierRecipeCategory(guiHelper),
                new CargoRocketRecipeCategory(guiHelper),
                new MethaneSynthRecipeCategory(guiHelper));
    }
}
