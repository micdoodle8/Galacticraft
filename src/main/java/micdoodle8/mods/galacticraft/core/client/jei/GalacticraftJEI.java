package micdoodle8.mods.galacticraft.core.client.jei;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeMaker;
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
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;

@JEIPlugin
public class GalacticraftJEI extends BlankModPlugin
{
    private static IModRegistry registryCached = null;
    private static IRecipeRegistry recipesCached = null;
    
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registryCached = registry;
        IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();

        registry.handleRecipes(INasaWorkbenchRecipe.class, Tier1RocketRecipeWrapper::new, RecipeCategories.ROCKET_T1_ID);
        registry.handleRecipes(INasaWorkbenchRecipe.class, BuggyRecipeWrapper::new, RecipeCategories.BUGGY_ID);
        registry.handleRecipes(CircuitFabricatorRecipeWrapper.class, recipe -> recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.handleRecipes(ShapedRecipesGC.class, IngotCompressorShapedRecipeWrapper::new, RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.handleRecipes(ShapelessOreRecipeGC.class, new IRecipeWrapperFactory<ShapelessOreRecipeGC>() {
        	@Override public IRecipeWrapper getRecipeWrapper(ShapelessOreRecipeGC recipe) { return new IngotCompressorShapelessRecipeWrapper(stackHelper, recipe); }
        		}, RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.handleRecipes(RefineryRecipeWrapper.class, recipe -> recipe, RecipeCategories.REFINERY_ID);

        registry.addRecipes(Tier1RocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_T1_ID);
        registry.addRecipes(BuggyRecipeMaker.getRecipesList(), RecipeCategories.BUGGY_ID);
        registry.addRecipes(CircuitFabricatorRecipeMaker.getRecipesList(), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipes(CompressorRecipes.getRecipeList(), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipes(RefineryRecipeMaker.getRecipesList(), RecipeCategories.REFINERY_ID);

        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T1_ID, RecipeCategories.BUGGY_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase2, 1, 4), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase, 1, 12), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.refinery), RecipeCategories.REFINERY_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.crafting), VanillaRecipeCategoryUid.CRAFTING);

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
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime rt)
    {
        recipesCached = rt.getRecipeRegistry();
    }

    public static boolean refreshJEIpre()
    {
        if (recipesCached != null)
        {
            try {
                IStackHelper stackHelper = registryCached.getJeiHelpers().getStackHelper();
                for (CircuitFabricatorRecipeWrapper recipe : CircuitFabricatorRecipeMaker.getRecipesList())
                {
                    removeRecipe(recipesCached, recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
                }
                for (IRecipe recipe : CompressorRecipes.getRecipeList())
                {
                    if (recipe instanceof ShapelessOreRecipeGC)
                    {
                        removeRecipe(recipesCached, new IngotCompressorShapelessRecipeWrapper(stackHelper, (ShapelessOreRecipeGC) recipe), RecipeCategories.INGOT_COMPRESSOR_ID);
                    }
                    else if (recipe instanceof ShapedRecipesGC)
                    {
                        removeRecipe(recipesCached, new IngotCompressorShapedRecipeWrapper((ShapedRecipesGC) recipe), RecipeCategories.INGOT_COMPRESSOR_ID);
                    }
                }
                return true;
            } catch (Exception ignore) {}
        }
        return false;
    }

    public static void refreshJEIpost()
    {
        if (recipesCached != null)
        {
            try {
                IStackHelper stackHelper = registryCached.getJeiHelpers().getStackHelper();
                for (CircuitFabricatorRecipeWrapper recipe : CircuitFabricatorRecipeMaker.getRecipesList())
                {
                    recipesCached.addRecipe(recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
                }
                for (IRecipe recipe : CompressorRecipes.getRecipeList())
                {
                    if (recipe instanceof ShapelessOreRecipeGC)
                    {
                        recipesCached.addRecipe(new IngotCompressorShapelessRecipeWrapper(stackHelper, (ShapelessOreRecipeGC) recipe), RecipeCategories.INGOT_COMPRESSOR_ID);
                    }
                    else if (recipe instanceof ShapedRecipesGC)
                    {
                        recipesCached.addRecipe(new IngotCompressorShapedRecipeWrapper((ShapedRecipesGC) recipe), RecipeCategories.INGOT_COMPRESSOR_ID);
                    }
                }
            } catch (Exception ignore) {}
        }
    }
    
    // This is a hacky solution because JEI API enforces a thread check - this is threadsafe on client in Galacticraft because only called during connection
    private static <T> void removeRecipe(IRecipeRegistry registry, T recipe, String recipeCategoryUid)
    {
        try {
            Field recipeMap = registry.getClass().getDeclaredField("recipeCategoriesMap");
            recipeMap.setAccessible(true);
            ImmutableMap<String, IRecipeCategory> map = (ImmutableMap<String, IRecipeCategory>) recipeMap.get(registry);
            IRecipeCategory recipeCategory = map.get(recipeCategoryUid);
            if (recipeCategory == null)
            {
                System.out.println("No recipe category registered for recipeCategoryUid: " + recipeCategoryUid);
                return;
            }
            Method removeIt = null;
            for (Method m : registry.getClass().getDeclaredMethods())
            {
                if (m.getName().equals("removeRecipeUnchecked"))
                {
                    removeIt = m;
                    removeIt.setAccessible(true);
                    break;
                }
            }
            removeIt.invoke(registry, recipe, recipeCategory);
        } catch (Exception ignore) {}
    }
}
