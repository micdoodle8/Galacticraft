package micdoodle8.mods.galacticraft.core.client.jei;

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
import micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor.OxygenCompressorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor.OxygenCompressorRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor.OxygenCompressorRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeWrapper;
import micdoodle8.mods.galacticraft.core.recipe.ShapedRecipeNBT;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

@JEIPlugin
public class GalacticraftJEI extends BlankModPlugin
{
    private static IModRegistry registryCached = null;
    private static IRecipeRegistry recipesCached = null;
    
    private static boolean hiddenSteel = false;
    private static boolean hiddenAdventure = false;
    public static List<IRecipeWrapper> hidden = new LinkedList<>();
    private static IRecipeCategory ingotCompressorCategory;

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
        registry.handleRecipes(OxygenCompressorRecipeWrapper.class, recipe -> recipe, RecipeCategories.OXYGEN_COMPRESSOR_ID);
        registry.handleRecipes(ShapedRecipeNBT.class, NBTSensitiveShapedRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);

        registry.addRecipes(Tier1RocketRecipeMaker.getRecipesList(), RecipeCategories.ROCKET_T1_ID);
        registry.addRecipes(BuggyRecipeMaker.getRecipesList(), RecipeCategories.BUGGY_ID);
        registry.addRecipes(CircuitFabricatorRecipeMaker.getRecipesList(), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipes(CompressorRecipes.getRecipeListAll(), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipes(OxygenCompressorRecipeMaker.getRecipesList(), RecipeCategories.OXYGEN_COMPRESSOR_ID);
        registry.addRecipes(RefineryRecipeMaker.getRecipesList(), RecipeCategories.REFINERY_ID);

        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.ROCKET_T1_ID, RecipeCategories.BUGGY_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase2, 1, 4), RecipeCategories.CIRCUIT_FABRICATOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase, 1, 12), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.machineBase2, 1, 0), RecipeCategories.INGOT_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.oxygenCompressor), RecipeCategories.OXYGEN_COMPRESSOR_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.refinery), RecipeCategories.REFINERY_ID);
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.crafting), VanillaRecipeCategoryUid.CRAFTING);
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new MagneticCraftingTransferInfo());

        this.addInformationPages(registry);
        GCItems.hideItemsJEI(registry.getJeiHelpers().getIngredientBlacklist());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        ingotCompressorCategory = new IngotCompressorRecipeCategory(guiHelper);
        registry.addRecipeCategories(new Tier1RocketRecipeCategory(guiHelper),
                new BuggyRecipeCategory(guiHelper),
                new CircuitFabricatorRecipeCategory(guiHelper),
                ingotCompressorCategory,
                new OxygenCompressorRecipeCategory(guiHelper),
                new RefineryRecipeCategory(guiHelper));
    }

    private void addInformationPages(IModRegistry registry)
    {
        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenPipe), ItemStack.class, GCCoreUtil.translate("jei.fluid_pipe.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.fuelLoader), ItemStack.class, GCCoreUtil.translate("jei.fuel_loader.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenCollector), ItemStack.class, GCCoreUtil.translate("jei.oxygen_collector.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenDistributor), ItemStack.class, GCCoreUtil.translate("jei.oxygen_distributor.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.oxygenSealer), ItemStack.class, GCCoreUtil.translate("jei.oxygen_sealer.info"));
        if (CompatibilityManager.isAppEngLoaded())
        {
            registry.addIngredientInfo(new ItemStack(GCBlocks.machineBase2), ItemStack.class, new String [] { GCCoreUtil.translate("jei.electric_compressor.info"), GCCoreUtil.translate("jei.electric_compressor.appeng.info") });
        }
        else
        {
            registry.addIngredientInfo(new ItemStack(GCBlocks.machineBase2), ItemStack.class, GCCoreUtil.translate("jei.electric_compressor.info"));
        }
        registry.addIngredientInfo(new ItemStack(GCBlocks.crafting), ItemStack.class, GCCoreUtil.translate("jei.magnetic_crafting.info"));
        registry.addIngredientInfo(new ItemStack(GCBlocks.brightLamp), ItemStack.class, GCCoreUtil.translate("jei.arc_lamp.info"));
        registry.addIngredientInfo(new ItemStack(GCItems.wrench), ItemStack.class, GCCoreUtil.translate("jei.wrench.info"));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime rt)
    {
        recipesCached = rt.getRecipeRegistry();
    }

    public static void updateHidden(boolean hideSteel, boolean hideAdventure)
    {
        boolean changeHidden = false;
        if (hideSteel != hiddenSteel)
        {
            hiddenSteel = hideSteel;
            changeHidden = true;
        }
        if (hideAdventure != hiddenAdventure)
        {
            hiddenAdventure = hideAdventure;
            changeHidden = true;
        }
        if (changeHidden && recipesCached != null)
        {
            unhide();
            List<IRecipe> toHide = CompressorRecipes.getRecipeListHidden(hideSteel, hideAdventure);
            hidden.clear();
            List<IRecipeWrapper> allRW = recipesCached.getRecipeWrappers(ingotCompressorCategory);
            for (IRecipe recipe : toHide)
            {
                hidden.add(recipesCached.getRecipeWrapper(recipe, RecipeCategories.INGOT_COMPRESSOR_ID));
            }
            hide();
        }
    }
    
    private static void hide()
    {
        for (IRecipeWrapper wrapper : hidden)
        {
            recipesCached.hideRecipe(wrapper);
        }
    }

    private static void unhide()
    {
        for (IRecipeWrapper wrapper : hidden)
        {
            recipesCached.unhideRecipe(wrapper);
        }
    }
}
