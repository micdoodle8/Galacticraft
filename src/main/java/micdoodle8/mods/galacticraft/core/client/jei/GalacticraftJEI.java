package micdoodle8.mods.galacticraft.core.client.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.IStackHelper;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor.IngotCompressorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor.IngotCompressorShapedRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor.IngotCompressorShapelessRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class GalacticraftJEI implements IModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();

        registry.handleRecipes(INasaWorkbenchRecipe.class, Tier1RocketRecipeWrapper::new, RecipeCategories.ROCKET_T1_ID);
        registry.handleRecipes(INasaWorkbenchRecipe.class, BuggyRecipeWrapper::new, RecipeCategories.BUGGY_ID);
        registry.handleRecipes(CircuitFabricatorRecipeWrapper.class, recipe -> recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.handleRecipes(ShapedRecipesGC.class, IngotCompressorShapedRecipeWrapper::new, RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.handleRecipes(ShapelessOreRecipeGC.class, recipe -> new IngotCompressorShapelessRecipeWrapper(stackHelper, recipe), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.handleRecipes(RefineryRecipeWrapper.class, recipe -> recipe, RecipeCategories.REFINERY_ID);

        registry.addRecipes(GalacticraftRegistry.getRocketT1Recipes(), RecipeCategories.ROCKET_T1_ID);
        registry.addRecipes(GalacticraftRegistry.getBuggyBenchRecipes(), RecipeCategories.BUGGY_ID);
        registry.addRecipes(CircuitFabricatorRecipeMaker.getRecipesList(), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipes(CompressorRecipes.getRecipeList(), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipes(RefineryRecipeMaker.getRecipesList(), RecipeCategories.REFINERY_ID);

        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T1_ID, RecipeCategories.BUGGY_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase2, 1, 4), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase, 1, 12), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.refinery), RecipeCategories.REFINERY_ID);

        GCItems.hideItemsJEI(registry.getJeiHelpers().getIngredientBlacklist());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new Tier1RocketRecipeCategory(guiHelper),
                new BuggyRecipeCategory(guiHelper),
                new CircuitFabricatorRecipeCategory(guiHelper),
                new IngotCompressorRecipeCategory(guiHelper),
                new RefineryRecipeCategory(guiHelper));
    }
}
