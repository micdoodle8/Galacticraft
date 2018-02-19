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
    private static boolean JEIversion450plus = false;
    
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registryCached = registry;
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
            registry.addRecipeCategories(new Tier1RocketRecipeCategory(guiHelper),
                    new BuggyRecipeCategory(guiHelper),
                    new CircuitFabricatorRecipeCategory(guiHelper),
                    new IngotCompressorRecipeCategory(guiHelper),
                    new RefineryRecipeCategory(guiHelper));
        }

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

        if (JEIversion450plus)
        {
            registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T1_ID, RecipeCategories.BUGGY_ID);
            registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase2, 1, 4), RecipeCategories.CIRCUIT_FABRICATOR_ID);
            registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase, 1, 12), RecipeCategories.INGOT_COMPRESSOR_ID);
            registry.addRecipeCatalyst(new ItemStack(GCBlocks.refinery), RecipeCategories.REFINERY_ID);
            registry.addRecipeCatalyst(new ItemStack(GCBlocks.crafting), VanillaRecipeCategoryUid.CRAFTING);
        }
        else
        {
            ItemStack nasaWorkbench = new ItemStack(GCBlocks.nasaWorkbench);
            registry.addRecipeCategoryCraftingItem(nasaWorkbench, RecipeCategories.ROCKET_T1_ID);
            registry.addRecipeCategoryCraftingItem(nasaWorkbench, RecipeCategories.BUGGY_ID);
            registry.addRecipeCategoryCraftingItem(new ItemStack(GCBlocks.machineBase2, 1, 4), RecipeCategories.CIRCUIT_FABRICATOR_ID);
            registry.addRecipeCategoryCraftingItem(new ItemStack(GCBlocks.machineBase, 1, 12), RecipeCategories.INGOT_COMPRESSOR_ID);
            registry.addRecipeCategoryCraftingItem(new ItemStack(GCBlocks.refinery), RecipeCategories.REFINERY_ID);        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T1_ID, RecipeCategories.BUGGY_ID);
            registry.addRecipeCategoryCraftingItem(new ItemStack(GCBlocks.crafting), VanillaRecipeCategoryUid.CRAFTING);
        }

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

    public static void refreshJEIpre()
    {
        if (recipesCached != null)
        {
            IStackHelper stackHelper = registryCached.getJeiHelpers().getStackHelper();
            for (CircuitFabricatorRecipeWrapper recipe : CircuitFabricatorRecipeMaker.getRecipesList())
            {
                if (JEIversion450plus)
                    removeRecipe(recipesCached, recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
                else
                    recipesCached.removeRecipe(recipe);
            }
            for (IRecipe recipe : CompressorRecipes.getRecipeList())
            {
                if (JEIversion450plus)
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
                else
                    recipesCached.removeRecipe(recipe);
            }
        }
    }

    public static void refreshJEIpost()
    {
        if (recipesCached != null)
        {
            IStackHelper stackHelper = registryCached.getJeiHelpers().getStackHelper();
            for (CircuitFabricatorRecipeWrapper recipe : CircuitFabricatorRecipeMaker.getRecipesList())
            {
                if (JEIversion450plus)
                    recipesCached.addRecipe(recipe, RecipeCategories.CIRCUIT_FABRICATOR_ID);
                else
                    recipesCached.addRecipe(recipe);
            }
            for (IRecipe recipe : CompressorRecipes.getRecipeList())
            {
                if (JEIversion450plus)
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
                else
                    recipesCached.addRecipe(recipe);
            }
        }
    }
    
    // This is a hacky solution because Mezz didn't backport https://github.com/mezz/JustEnoughItems/commit/48fea48ed107f055f2f8196d0ba3a2de33187a4f
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
